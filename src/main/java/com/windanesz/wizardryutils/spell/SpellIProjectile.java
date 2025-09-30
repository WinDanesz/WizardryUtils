package com.windanesz.wizardryutils.spell;

import com.windanesz.wizardryutils.WizardryUtils;
import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.EnumAction;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Generic superclass for all spells which launch any entity that implements IProjectile.
 * This allows for more flexibility than just EntityThrowable or EntityMagicProjectile.
 * Can be used with arrows, snowballs, custom projectiles, etc.
 */
public class SpellIProjectile<T extends Entity & IProjectile> extends Spell {
	private static final float DISPENSER_INACCURACY = 1;
	private static final float FALLBACK_VELOCITY = 1.5f;

	protected final Function<World, T> projectileFactory;

	public SpellIProjectile(String name, Function<World, T> projectileFactory) {
		this(WizardryUtils.MODID, name, projectileFactory);
	}

	public SpellIProjectile(String modID, String name, Function<World, T> projectileFactory) {
		super(modID, name, EnumAction.NONE, false);
		this.projectileFactory = projectileFactory;
		this.npcSelector((e, o) -> true);
		addProperties(RANGE);
	}

	@Override
	public boolean requiresPacket() {
		return false;
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return true;
	}

	protected float calculateVelocity(T projectile, SpellModifiers modifiers, float launchHeight) {
		float range = getProperty(RANGE).floatValue() * modifiers.get(WizardryItems.range_upgrade);

		if (projectile.hasNoGravity()) {
			if (projectile.ticksExisted <= 0) {
				return FALLBACK_VELOCITY;
			}
			return range / projectile.ticksExisted;
		} else {
			float g = 0.03f;
			return range / MathHelper.sqrt(2 * launchHeight / g);
		}
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (!world.isRemote) {
			T projectile = projectileFactory.apply(world);
			
			// Set shooter before positioning to prevent self-collision
			if (projectile instanceof EntityArrow) {
				EntityArrow arrow = (EntityArrow) projectile;
				arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
				arrow.shootingEntity = caster;
			}
			
			projectile.setPosition(caster.posX, caster.posY + caster.getEyeHeight() - 0.1, caster.posZ);

			Vec3d look = caster.getLookVec();
			float velocity = calculateVelocity(projectile, modifiers, caster.getEyeHeight() - 0.1f);
			projectile.shoot(look.x, look.y, look.z, velocity, 1.0f);

			addProjectileExtras(projectile, caster, modifiers);
			world.spawnEntity(projectile);

			caster.swingArm(hand);
			this.playSound(world, caster, ticksInUse, -1, modifiers);
		}
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (target != null && !world.isRemote) {
			T projectile = projectileFactory.apply(world);
			
			// Set shooter before positioning to prevent self-collision
			if (projectile instanceof EntityArrow) {
				EntityArrow arrow = (EntityArrow) projectile;
				arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
				arrow.shootingEntity = caster;
			}
			
			projectile.setPosition(caster.posX, caster.posY + caster.getEyeHeight() - 0.1, caster.posZ);

			// Calculate trajectory
			double dx = target.posX - caster.posX;
			double dy = target.posY + target.height / 2 - (caster.posY + caster.getEyeHeight() - 0.1);
			double dz = target.posZ - caster.posZ;

			float velocity = calculateVelocity(projectile, modifiers, caster.getEyeHeight() - 0.1f);
			float aimingError = caster instanceof ISpellCaster ? ((ISpellCaster) caster).getAimingError(world.getDifficulty()) : EntityUtils.getDefaultAimingError(world.getDifficulty());

			projectile.shoot(dx, dy, dz, velocity, aimingError);
			addProjectileExtras(projectile, caster, modifiers);
			world.spawnEntity(projectile);

			caster.swingArm(hand);
			this.playSound(world, caster, ticksInUse, -1, modifiers);
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers) {
		if (!world.isRemote) {
			T projectile = projectileFactory.apply(world);
			
			// Set arrow properties (no shooter from dispenser, but still prevent pickup)
			if (projectile instanceof EntityArrow) {
				((EntityArrow) projectile).pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
			}
			
			projectile.setPosition(x, y, z);

			Vec3i vec = direction.getDirectionVec();
			float velocity = calculateVelocity(projectile, modifiers, 0.375f);
			projectile.shoot(vec.getX(), vec.getY(), vec.getZ(), velocity, DISPENSER_INACCURACY);

			addProjectileExtras(projectile, null, modifiers);
			world.spawnEntity(projectile);

			this.playSound(world, x - direction.getXOffset(), y - direction.getYOffset(), z - direction.getZOffset(), ticksInUse, duration, modifiers);
			return true;
		}
		return true;
	}

	/**
	 * Called just before the projectile is spawned. Override this to add additional properties to the projectile.
	 *
	 * @param projectile The projectile entity being spawned
	 * @param caster     The entity casting the spell, or null if cast by a dispenser
	 * @param modifiers  The spell modifiers
	 */
	protected void addProjectileExtras(T projectile, @Nullable EntityLivingBase caster, SpellModifiers modifiers) {
		// Subclasses can override this to add additional properties to the projectile
	}
}