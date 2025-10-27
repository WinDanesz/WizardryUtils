package com.windanesz.wizardryutils.spell;

import com.windanesz.wizardryutils.entity.EntityDynamicConstruct;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Supplier;

/**
 * Generic area effect spell that spawns a construct entity applying potion effects to nearby entities.
 * This allows dynamic creation of area effect spells via ZenScript.
 *
 * @author WinDanesz
 */
public class SpellDynamicAreaEffect extends Spell {

	public static final String EFFECT_RADIUS = "effect_radius";
	public static final String EFFECT_DURATION = "effect_duration";
	public static final String CONSTRUCT_LIFETIME = "lifetime";
	public static final String CREATURE_TARGETING = "creature_targeting";

	private final Supplier<Potion>[] potionSuppliers;
	private final float r, g, b;

	public SpellDynamicAreaEffect(String modID, String name, float r, float g, float b, Supplier<Potion>[] potionSuppliers) {
		super(modID, name, SpellActions.POINT, false);
		this.r = r;
		this.g = g;
		this.b = b;
		this.potionSuppliers = potionSuppliers;
		addProperties(EFFECT_RADIUS, EFFECT_DURATION, CONSTRUCT_LIFETIME, CREATURE_TARGETING);
		this.npcSelector((e, o) -> true);
	}

	@Override
	public boolean requiresPacket() {
		return false;
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return true;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {

		Vec3d look = caster.getLookVec();
		double distance = 5.0 * modifiers.get(WizardryItems.range_upgrade);

		double x = caster.posX + look.x * distance;
		double y = caster.posY + caster.getEyeHeight() + look.y * distance;
		double z = caster.posZ + look.z * distance;

		if (!world.isRemote) {
			spawnConstruct(world, x, y, z, caster, modifiers);
		}

		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {

		if (target != null) {
			if (!world.isRemote) {
				spawnConstruct(world, target.posX, target.posY + target.height / 2, target.posZ, caster, modifiers);
			}
		} else {
			Vec3d look = caster.getLookVec();
			double distance = 5.0 * modifiers.get(WizardryItems.range_upgrade);

			double x = caster.posX + look.x * distance;
			double y = caster.posY + caster.getEyeHeight() + look.y * distance;
			double z = caster.posZ + look.z * distance;

			if (!world.isRemote) {
				spawnConstruct(world, x, y, z, caster, modifiers);
			}
		}

		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers) {

		if (!world.isRemote) {
			spawnConstruct(world, x, y, z, null, modifiers);
		}

		this.playSound(world, x - direction.getXOffset(), y - direction.getYOffset(), z - direction.getZOffset(), ticksInUse, duration, modifiers);
		return true;
	}

	private void spawnConstruct(World world, double x, double y, double z, EntityLivingBase caster, SpellModifiers modifiers) {
		int lifetime = (int) (getProperty(CONSTRUCT_LIFETIME).floatValue() * modifiers.get(WizardryItems.duration_upgrade));
		double radius = getProperty(EFFECT_RADIUS).doubleValue();
		int creatureTargeting = getProperty(CREATURE_TARGETING).intValue();

		EntityDynamicConstruct construct = new EntityDynamicConstruct(world, x, y, z, caster, lifetime, r, g, b, potionSuppliers, radius);
		construct.setModifiers(modifiers);
		construct.setCreatureTargeting(creatureTargeting);

		world.spawnEntity(construct);
	}
}
