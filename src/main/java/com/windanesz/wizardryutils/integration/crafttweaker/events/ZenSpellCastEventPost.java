package com.windanesz.wizardryutils.integration.crafttweaker.events;

import com.windanesz.wizardryutils.integration.crafttweaker.api.ISpellCastEvent;
import com.windanesz.wizardryutils.integration.crafttweaker.spell.ZenSpell;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import electroblob.wizardry.event.SpellCastEvent;
import net.minecraft.entity.player.EntityPlayer;

/**
 * author WinDanesz
 */

public class ZenSpellCastEventPost implements ISpellCastEvent {

	private SpellCastEvent.Post event;

	public ZenSpellCastEventPost(SpellCastEvent.Post event) {
		this.event = event;
	}

	@Override
	public IWorld getWorld() {
		return CraftTweakerMC.getIWorld(event.getWorld());
	}

	@Override
	public ZenSpell getSpell() {
		return new ZenSpell(event.getSpell());
	}

	@Override
	public float getModifier(String modifier) {
		return event.getModifiers().get(modifier);
	}

	@Override
	public void setModifier(String modifier, float amount) {
		event.getModifiers().set(modifier, amount, true);
	}

	@Override
	public String getSource() {
		return event.getSource().name().toUpperCase();
	}

	@Override
	public IEntityLivingBase getCaster() {
		return CraftTweakerMC.getIEntityLivingBase(event.getCaster());
	}

	@Override
	public IPlayer getPlayer() {
		return event.getCaster() instanceof EntityPlayer ? CraftTweakerMC.getIPlayer((EntityPlayer) event.getCaster()) : null;
	}

	@Override
	public IFacing getFacing() {
		return CraftTweakerMC.getIFacing(event.getDirection());
	}

	@Override
	public double getX() {
		return event.getX();
	}

	@Override
	public double getY() {
		return event.getY();
	}

	@Override
	public double getZ() {
		return event.getZ();
	}
}
