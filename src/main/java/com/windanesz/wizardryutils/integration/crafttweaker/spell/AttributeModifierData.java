package com.windanesz.wizardryutils.integration.crafttweaker.spell;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenConstructor;

@ZenRegister
@ZenClass("mods.wizardryutils.AttributeModifierData")
public class AttributeModifierData {
	public final String attributeName;
	public final double amount;
	public final int operation; // 0 = add, 1 = multiply base, 2 = multiply total

	@ZenConstructor
	public AttributeModifierData(String attributeName, double amount, int operation) {
		this.attributeName = attributeName;
		this.amount = amount;
		this.operation = operation;
	}
}
