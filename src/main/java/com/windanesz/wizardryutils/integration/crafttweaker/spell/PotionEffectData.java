package com.windanesz.wizardryutils.integration.crafttweaker.spell;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenConstructor;

@ZenRegister
@ZenClass("mods.wizardryutils.PotionEffectData")
public class PotionEffectData {
	public final String potionRegistryName;
	public final int amplifier;
	public final boolean showParticles;

	@ZenConstructor
	public PotionEffectData(String potionRegistryName, int amplifier, boolean showParticles) {
		this.potionRegistryName = potionRegistryName;
		this.amplifier = amplifier;
		this.showParticles = showParticles;
	}
}
