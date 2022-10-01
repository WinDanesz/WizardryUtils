package com.windanesz.wizardryutils.integration.crafttweaker.api;

import com.windanesz.wizardryutils.integration.crafttweaker.spell.ZenSpell;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * author WinDanesz
 */

@ZenRegister
@ZenClass("mods.wizardryutils.ISpellCastEvent")
public interface ISpellCastEvent {

	@ZenGetter("world")
	IWorld getWorld();

	@ZenGetter("spell")
	ZenSpell getSpell();

	@ZenMethod
	float getModifier(String modifier);

	@ZenMethod
	void setModifier(String modifier, float amount);

	@ZenGetter("source")
	String getSource();

	@ZenGetter("caster")
	IEntityLivingBase getCaster();

	@ZenGetter("player")
	IPlayer getPlayer();

	@ZenGetter("facing")
	IFacing getFacing();

	@ZenGetter("x")
	double getX();

	@ZenGetter("y")
	double getY();

	@ZenGetter("z")
	double getZ();
}
