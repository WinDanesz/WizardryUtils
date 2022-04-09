package com.windanesz.wizardryutils.capability;

import com.google.common.base.Predicate;
import com.windanesz.wizardryutils.WizardryUtils;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtByTarget;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtTarget;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.EntityWizard;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.UUID;

/**
 * Summoned entity capability, based on {@link electroblob.wizardry.data.WizardData} - Author: Electroblob
 *
 * @author WinDanesz, Electroblob
 */
// On the plus side, having to rethink this class allowed me to clean it up a lot.
@Mod.EventBusSubscriber
public class SummonedCreatureData implements INBTSerializable<NBTTagCompound> {

	/**
	 * Static instance of what I like to refer to as the capability key. Private because, well, it's internal!
	 */
	// This annotation does some crazy Forge magic behind the scenes and assigns this field a value.
	@CapabilityInject(SummonedCreatureData.class)
	private static final Capability<SummonedCreatureData> SUMMONED_CREATURE_DATA_CAPABILITY = null;
	/**
	 * The player this WizardData instance belongs to.
	 */
	private final EntityLivingBase minion;
	// Field implementations
	private int lifetime = -1;
	private boolean summoned = false;
	private UUID casterUUID;
	private UUID owner;

	public SummonedCreatureData() {
		this(null); // Nullary constructor for the registration method factory parameter
	}

	public SummonedCreatureData(EntityLivingBase entity) {
		this.minion = entity;
	}

	/**
	 * Called from preInit in the main mod class to register the WizardData capability.
	 */
	public static void register() {

		// Yes - by the looks of it, having an interface is completely unnecessary in this case.
		CapabilityManager.INSTANCE.register(SummonedCreatureData.class, new IStorage<SummonedCreatureData>() {
			// These methods are only called by Capability.writeNBT() or Capability.readNBT(), which in turn are
			// NEVER CALLED. Unless I'm missing some reflective invocation, that means this entire class serves only
			// to allow capabilities to be saved and loaded manually. What that would be useful for I don't know.
			// (If an API forces most users to write redundant code for no reason, it's not user friendly, is it?)
			// ... well, that's my rant for today!
			@Override
			public NBTBase writeNBT(Capability<SummonedCreatureData> capability, SummonedCreatureData instance, EnumFacing side) {
				return null;
			}

			@Override
			public void readNBT(Capability<SummonedCreatureData> capability, SummonedCreatureData instance, EnumFacing side, NBTBase nbt) {}

		}, SummonedCreatureData::new);
	}

	/**
	 * Returns the WizardData instance for the specified entity.
	 */
	public static SummonedCreatureData get(EntityLivingBase entity) {
		return entity.getCapability(SUMMONED_CREATURE_DATA_CAPABILITY, null);
	}

	public static boolean isSummonedEntity(Entity entity) {
		return entity instanceof EntityCreature && get((EntityCreature) entity) != null && get((EntityCreature) entity).summoned;
	}

	// ============================================== Event Handlers ==============================================

	@SubscribeEvent
	public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityCreature) {
			event.addCapability(new ResourceLocation(WizardryUtils.MODID, "SummonedCreatureData"),
					new Provider((EntityLivingBase) event.getObject()));
		}
	}

	@SubscribeEvent
	public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
		if (isSummonedEntity(event.getEntity())) {
			EntityLiving entity = (EntityLiving) event.getEntity();

			SummonedCreatureData data = get(entity);
			data.updateEntityTargetTasks((EntityCreature) entity);
		}
	}

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingUpdateEvent event) {
		if (isSummonedEntity(event.getEntity())) {
			SummonedCreatureData.get((EntityCreature) event.getEntity()).updateDelegate();
		}
	}

	@SubscribeEvent
	public static void onLivingDropsEvent(LivingDropsEvent event) {
		if (isSummonedEntity(event.getEntity())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onLivingExperienceDropEvent(LivingExperienceDropEvent event) {
		if (isSummonedEntity(event.getEntity())) {
			event.setCanceled(true);
		}
	}

	public void updateEntityTargetTasks(EntityCreature minion) {
		while (minion.targetTasks.taskEntries.stream().anyMatch(taskEntry -> taskEntry.action != null)) {

			minion.targetTasks.taskEntries.stream().filter(taskEntry -> taskEntry.action != null)
					.findFirst().ifPresent(taskEntry -> minion.targetTasks.removeTask(taskEntry.action));
		}

		// target enemies that hurt the owner
		minion.targetTasks.addTask(1, new EntityAIMinionOwnerHurtByTarget(minion));
		// target enemies targeted by the owner
		minion.targetTasks.addTask(2, new EntityAIMinionOwnerHurtTarget(minion));

		minion.targetTasks.addTask(1, new EntityAIHurtByTarget(minion, false));
		minion.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(minion, EntityLivingBase.class,
				0, false, true, getTargetSelector()));
	}

	private Predicate<Entity> getTargetSelector() {
		return entity -> !entity.isInvisible() && (getCaster() == null ? entity instanceof EntityPlayer &&
				!((EntityPlayer) entity).isCreative() : isValidTarget(entity));
	}

	private boolean isValidTarget(Entity target) {
		// If the target is valid based on the ADS...
		if (AllyDesignationSystem.isValidTarget(this.getCaster(), target)) {

			if (target instanceof EntityLivingBase && isSummonedEntity(target)) {
				SummonedCreatureData data = get((EntityLivingBase)target);
				if ((data.getCaster() != null && data.getCaster() == this.getCaster()) || AllyDesignationSystem.isAllied(data.getCaster(), this.getCaster())) {
					return false;
				}
			}

			// ...and is a player, they can be attacked, since players can't be in the whitelist or the
			// blacklist...
			if (target instanceof EntityPlayer) {
				// ...unless the creature was summoned by a good wizard who the player has not angered.
				if (getCaster() instanceof EntityWizard) {
					if (getCaster().getRevengeTarget() != target
							&& ((EntityWizard) getCaster()).getAttackTarget() != target) {
						return false;
					}
				}

				return true;
			}

			// TODO: don't attack other summons!
			// ...and is a mob, a summoned creature, a wizard...
			if ((target instanceof IMob || target instanceof ISummonedCreature
					|| (target instanceof EntityWizard && !(getCaster() instanceof EntityWizard))
					// ...or something that's attacking the owner...
					|| (target instanceof EntityLiving && ((EntityLiving) target).getAttackTarget() == getCaster())
					// ...or in the whitelist...
					|| Arrays.asList(Wizardry.settings.summonedCreatureTargetsWhitelist)
					.contains(EntityList.getKey(target.getClass())))
					// ...and isn't in the blacklist...
					&& !Arrays.asList(Wizardry.settings.summonedCreatureTargetsBlacklist)
					.contains(EntityList.getKey(target.getClass()))) {
				// ...it can be attacked.
				return true;
			}
		}

		return false;
	}

	public int getLifetime() { return lifetime; }

	/**
	 * Sets the lifetime of this minion. Also handles marking it as a summoned entity if necessary.
	 *
	 * @param lifetime in ticks. Set to -1 to make the entity not despawn.
	 */
	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
		if (!summoned) { summoned = true; }
	}

	public UUID getOwnerId() {
		return casterUUID;
	}

	public void setOwnerId(UUID uuid) {
		this.casterUUID = uuid;
	}

	public EntityLivingBase getCaster() { // Kept despite the above method because it returns an EntityLivingBase

		Entity caster = EntityUtils.getEntityByUUID(minion.world, getOwnerId());

		if (caster != null && !(caster instanceof EntityLivingBase)) { // Should never happen
			WizardryUtils.logger.warn("{} has a non-living owner!", this);
			return null;
		}

		return (EntityLivingBase) caster;

	}

	/**
	 * Sets the caster of this minion who has summoned it. Also handles marking it as a summoned entity if necessary.
	 *
	 * @param caster the owner (caster) of this entity.
	 */
	public void setCaster(@Nullable EntityLivingBase caster) {
		setOwnerId(caster == null ? null : caster.getUniqueID());
		if (!summoned) { summoned = true; }
	}

	void updateDelegate() {

		// Don't do anything for not summoned entities
		if (!summoned) { return; }

		// For some reason Minecraft reads the entity from NBT just after the entity is created, so setting -1 as a
		// default lifetime doesn't work. The easiest way around this is to use 0 - nobody's going to need it!
		if (minion.ticksExisted > this.getLifetime() && this.getLifetime() > 0) {
			minion.setDead();
		}

		if (minion.world.isRemote && minion.world.rand.nextInt(8) == 0) {
			ParticleBuilder.create(ParticleBuilder.Type.DARK_MAGIC)
					.pos(minion.posX, minion.posY + minion.world.rand.nextDouble() * 1.5, minion.posZ)
					.clr(0.1f, 0.0f, 0.0f)
					.spawn(minion.world);
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		if (this.summoned) {

			nbt.setBoolean("summoned", true);

			if (this.getOwnerId() != null) {
				NBTTagCompound casterUUID = new NBTTagCompound();
				casterUUID.setUniqueId("casterUUID", this.getOwnerId());
				nbt.setTag("casterUUID", casterUUID);
			}
			if (lifetime != -1) {
				nbt.setInteger("lifetime", getLifetime());
			}
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt != null && !nbt.isEmpty()) {

			// not really using this key since setOwnerId() and setLifetime() sets it to true anyways.
			if (nbt.hasKey("summoned")) {
				if (nbt.hasKey("casterUUID")) {
					this.setOwnerId(nbt.getCompoundTag("casterUUID").getUniqueId("casterUUID"));
				}
				if (nbt.hasKey("lifetime")) {
					this.setLifetime(nbt.getInteger("lifetime"));
				}
			}
		}
	}

	// ========================================== Capability Boilerplate ==========================================

	/**
	 * This is a nested class for a few reasons: firstly, it makes sense because instances of this and WizardData go
	 * hand-in-hand; secondly, it's too short to be worth a separate file; and thirdly (and most importantly) it allows
	 * me to access WIZARD_DATA_CAPABILITY while keeping it private.
	 */
	public static class Provider implements ICapabilitySerializable<NBTTagCompound> {

		private final SummonedCreatureData data;

		public Provider(EntityLivingBase entity) {
			data = new SummonedCreatureData(entity);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == SUMMONED_CREATURE_DATA_CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

			if (capability == SUMMONED_CREATURE_DATA_CAPABILITY) {
				return SUMMONED_CREATURE_DATA_CAPABILITY.cast(data);
			}

			return null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return data.serializeNBT();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			data.deserializeNBT(nbt);
		}

	}

}
