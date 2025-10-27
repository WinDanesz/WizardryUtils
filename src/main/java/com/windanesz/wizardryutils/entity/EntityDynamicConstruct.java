package com.windanesz.wizardryutils.entity;

import com.windanesz.wizardryutils.WizardryUtils;
import electroblob.wizardry.entity.construct.EntityScaledConstruct;
import electroblob.wizardry.item.ISpellCastingItem;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Generic construct entity that applies potion effects to entities within a radius.
 * This allows dynamic creation of area effect spells via ZenScript.
 *
 * @author WinDanesz
 */
public class EntityDynamicConstruct extends EntityScaledConstruct {

	protected float sizeMultiplier = 1;
	private Supplier<Potion>[] potionSuppliers;
	private float r, g, b;
	private double effectRadius = 3.0;
	private int tickInterval = 20;
	private int creatureTargeting = 0; // 0: enemies, 1: allies, 2: allies + caster
	private int lifetime = 600;
	private UUID casterUUID;
	private EntityLivingBase casterCache;
	private SpellModifiers modifiers = new SpellModifiers();

	public EntityDynamicConstruct(World world) {
		super(world);
		this.setSize(1.0F, 1.0F);
		this.noClip = true;
	}

	public EntityDynamicConstruct(World world, double x, double y, double z, EntityLivingBase caster, int lifetime, float r, float g, float b, Supplier<Potion>[] potionSuppliers, double effectRadius) {
		this(world);
		this.setPosition(x, y, z);
		this.r = r;
		this.g = g;
		this.b = b;
		this.potionSuppliers = potionSuppliers;
		this.effectRadius = effectRadius;
		this.lifetime = lifetime;
		if (caster != null) {
			this.casterUUID = caster.getUniqueID();
			this.casterCache = caster;
		}
	}

	public float getSizeMultiplier() {
		return sizeMultiplier;
	}

	public void setSizeMultiplier(float sizeMultiplier) {
		this.sizeMultiplier = sizeMultiplier;
		setSize(shouldScaleWidth() ? width * sizeMultiplier : width, shouldScaleHeight() ? height * sizeMultiplier : height);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		// Check lifetime
		if (this.ticksExisted > lifetime && lifetime != -1) {
			this.despawn();
		}

		if (!world.isRemote && ticksExisted % tickInterval == 0 && potionSuppliers != null) {
			applyEffects();
		}

		if (world.isRemote) {
			spawnParticles();
		}
	}

	private void applyEffects() {
		double radius = effectRadius * modifiers.get(WizardryItems.blast_upgrade);

		AxisAlignedBB aabb = new AxisAlignedBB(posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius);

		List<EntityLivingBase> targets = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);

		for (EntityLivingBase target : targets) {
			if (target == null) continue;

			// Check if we should affect this entity based on creature_targeting
			if (getCaster() != null) {
				boolean isCaster = target == getCaster();
				boolean isAlly = !AllyDesignationSystem.isValidTarget(getCaster(), target);
				
				// Apply targeting rules based on creatureTargeting value
				if (creatureTargeting == 0) { // Enemies only
					if (isAlly || isCaster) continue;
				} else if (creatureTargeting == 1) { // Allies only (not caster)
					if (!isAlly || isCaster) continue;
				} else if (creatureTargeting == 2) { // Allies + caster
					if (!isAlly && !isCaster) continue;
				}
			}

			// Apply all potion effects
			for (Supplier<Potion> potionSupplier : potionSuppliers) {
				if (potionSupplier != null) {
					Potion potion = potionSupplier.get();
					if (potion != null) {
						int duration = (int) (600 * modifiers.get(WizardryItems.duration_upgrade));
						int amplifier = (int) (modifiers.get(SpellModifiers.POTENCY) - 1);
						target.addPotionEffect(new PotionEffect(potion, duration, amplifier, false, true));
					}
				}
			}
		}
	}

	private void spawnParticles() {
		double radius = effectRadius * modifiers.get(WizardryItems.blast_upgrade);

		for (int i = 0; i < 3; i++) {
			double dx = (rand.nextDouble() - 0.5) * radius * 2;
			double dy = (rand.nextDouble() - 0.5) * radius * 2;
			double dz = (rand.nextDouble() - 0.5) * radius * 2;

			ParticleBuilder.create(ParticleBuilder.Type.SPARKLE).pos(posX + dx, posY + dy, posZ + dz).vel(0, 0.02, 0).clr(r, g, b).spawn(world);
		}
	}

	/**
	 * Returns true if the width of this entity's bounding box should be scaled by the size multiplier on creation.
	 */
	protected boolean shouldScaleWidth() {
		return true;
	}

	/**
	 * Returns true if the height of this entity's bounding box should be scaled by the size multiplier on creation.
	 */
	protected boolean shouldScaleHeight() {
		return true;
	}


	@Override
	public void writeSpawnData(ByteBuf data) {
		data.writeInt(lifetime);
		data.writeInt(getCaster() == null ? -1 : getCaster().getEntityId());
		data.writeFloat(sizeMultiplier);
		data.writeFloat(r);
		data.writeFloat(g);
		data.writeFloat(b);
		data.writeFloat(modifiers.get(WizardryItems.blast_upgrade));
	}

	@Override
	public void readSpawnData(ByteBuf data) {

		lifetime = data.readInt();

		int id = data.readInt();

		if (id == -1) {
			setCaster(null);
		} else {
			Entity entity = world.getEntityByID(id);
			if (entity instanceof EntityLivingBase) {
				setCaster((EntityLivingBase) entity);
			} else {
				WizardryUtils.logger.warn("Construct caster with ID in spawn data not found");
			}
		}
		setSizeMultiplier(data.readFloat()); // Set the width correctly on the client side
		r = data.readFloat();
		g = data.readFloat();
		b = data.readFloat();
		float blastModifier = data.readFloat();
		modifiers.set(WizardryItems.blast_upgrade, blastModifier, false);
	}

	public EntityDynamicConstruct setModifiers(SpellModifiers modifiers) {
		this.modifiers = modifiers;
		return this;
	}

	public EntityDynamicConstruct setEffectRadius(double radius) {
		this.effectRadius = radius;
		return this;
	}

	public EntityDynamicConstruct setTickInterval(int interval) {
		this.tickInterval = interval;
		return this;
	}

	public EntityDynamicConstruct setCreatureTargeting(int creatureTargeting) {
		this.creatureTargeting = creatureTargeting;
		return this;
	}

	// Overrides the original to stop the entity moving when it intersects stuff. The default arrow does this to allow
	// it to stick in blocks.
	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
		this.setPosition(x, y, z);
		this.setRotation(yaw, pitch);
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {

		// Permanent constructs can now be dispelled by sneak-right-clicking
		if (lifetime == -1 && getCaster() == player && player.isSneaking() && player.getHeldItem(hand).getItem() instanceof ISpellCastingItem) {
			this.despawn();
			return EnumActionResult.SUCCESS;
		}

		return super.applyPlayerInteraction(player, vec, hand);
	}

	/**
	 * Defaults to just setDead() in EntityMagicConstruct, but is provided to allow subclasses to override this e.g.
	 * bubble uses it to dismount the entity inside it and play the 'pop' sound before calling super(). You should
	 * always call super() when overriding this method, in case it changes. There is no need, therefore, to call
	 * setDead() when overriding.
	 */
	public void despawn() {
		this.setDead();
	}

	@Override
	protected void entityInit() {
		// We could leave this unimplemented, but since the majority of subclasses don't use it, let's make it optional
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		if (nbt.hasUniqueId("casterUUID")) casterUUID = nbt.getUniqueId("casterUUID");
		lifetime = nbt.getInteger("lifetime");
		damageMultiplier = nbt.getFloat("damageMultiplier");
		setSizeMultiplier(nbt.getFloat("sizeMultiplier"));
		creatureTargeting = nbt.getInteger("creatureTargeting");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		if (casterUUID != null) {
			nbt.setUniqueId("casterUUID", casterUUID);
		}
		nbt.setInteger("lifetime", lifetime);
		nbt.setFloat("damageMultiplier", damageMultiplier);
		nbt.setFloat("sizeMultiplier", sizeMultiplier);
		nbt.setInteger("creatureTargeting", creatureTargeting);
	}

	@Nullable
	@Override
	public UUID getOwnerId() {
		return casterUUID;
	}

	@Nullable
	@Override
	public Entity getOwner() {
		return getCaster(); // Delegate to getCaster
	}

	/**
	 * Returns the EntityLivingBase that created this construct, or null if it no longer exists. Cases where the entity
	 * may no longer exist are: entity died or was deleted, mob despawned, player logged out, entity teleported to
	 * another dimension, or this construct simply had no caster in the first place.
	 */
	@Nullable
	public EntityLivingBase getCaster() { // Kept despite the above method because it returns an EntityLivingBase

		Entity entity = EntityUtils.getEntityByUUID(world, getOwnerId());

		if (entity != null && !(entity instanceof EntityLivingBase)) { // Should never happen
			WizardryUtils.logger.warn("{} has a non-living owner!", this);
			entity = null;
		}

		return (EntityLivingBase) entity;
	}

	public void setCaster(@Nullable EntityLivingBase caster) {
		this.casterUUID = caster == null ? null : caster.getUniqueID();
	}

	/**
	 * Shorthand for {@link AllyDesignationSystem#isValidTarget(Entity, Entity)}, with the owner of this construct as the
	 * attacker. Also allows subclasses to override it if they wish to do so.
	 */
	public boolean isValidTarget(Entity target) {
		return AllyDesignationSystem.isValidTarget(this.getCaster(), target);
	}

	@Override
	public SoundCategory getSoundCategory() {
		return WizardrySounds.SPELLS;
	}

	@Override
	public boolean canRenderOnFire() {
		return false;
	}

	@Override
	public boolean isPushedByWater() {
		return false;
	}
}
