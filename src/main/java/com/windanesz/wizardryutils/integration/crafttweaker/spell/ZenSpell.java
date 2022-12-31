package com.windanesz.wizardryutils.integration.crafttweaker.spell;

import crafttweaker.annotations.ZenRegister;
import electroblob.wizardry.spell.Spell;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * author WinDanesz
 */

@ZenClass("mods.wizardryutils.ZenSpell")
@ZenRegister
public class ZenSpell {

	private final Spell spell;

	public ZenSpell(Spell spell) {
		this.spell = spell;
	}

	@ZenMethod
	public static ZenSpell byMetadata(int metadata) {
		return new ZenSpell(Spell.byMetadata(metadata));
	}

	@ZenMethod
	public static ZenSpell getSpell(String name) {
		return new ZenSpell(Spell.get(name));
	}

	@ZenMethod
	public static int getMetaData(String spell) {
		return Spell.get(spell).metadata();
	}

	@ZenGetter("elementID")
	@ZenMethod
	public int getElementID() {
		return spell.getElement().ordinal();
	}

	@ZenGetter("element")
	public String getGetElement() {
		return spell.getElement().name().toUpperCase();
	}

	@ZenMethod
	public boolean hasProperty(String identifier) {return spell.hasProperty(identifier);}

	@ZenMethod
	public float getProperty(String identifier) {return spell.getProperty(identifier).floatValue();}
	//
	//

	@ZenGetter("networkID")
	@ZenMethod
	public int getNetworkID() {
		return spell.networkID();
	}

	@ZenGetter("name")
	@ZenMethod
	public String getName() {
		return spell.getRegistryName().toString();
	}

	@ZenGetter("tierID")
	@ZenMethod
	public int getTierID() {
		return spell.getTier().ordinal();
	}

	@ZenGetter("tier")
	@ZenMethod
	public String getTier() {
		return spell.getTier().name().toUpperCase();
	}

	@ZenGetter("typeID")
	@ZenMethod
	public int getTypeID() {
		return spell.getType().ordinal();
	}

	@ZenGetter("type")
	@ZenMethod
	public String getType() {
		return spell.getType().name().toUpperCase();
	}

	@ZenGetter("cost")
	@ZenMethod
	public int getCost() {
		return spell.getCost();
	}

	@ZenGetter("cooldown")
	@ZenMethod
	public int getCooldown() {
		return spell.getCooldown();
	}

	@ZenGetter("displayName")
	@ZenMethod
	public String getDisplayName() {
		return spell.getDisplayName();
	}

}
