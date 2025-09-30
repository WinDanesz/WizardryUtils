
package com.windanesz.wizardryutils.integration.crafttweaker.spell;

import crafttweaker.annotations.ZenRegister;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.spell.SpellBuff;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.wizardryutils.BuffSpells")
@ZenRegister
public class ZenBuffSpell {

	public static final class BuffSpellEntry {
		public final String name;
		public final String[] potions;
		public final float r;
		public final float g;
		public final float b;

		public BuffSpellEntry(String name, String[] potions, float r, float g, float b) {
			this.name = name;
			this.potions = potions;
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}

	public static final List<BuffSpellEntry> entries = new ArrayList<>();

	// ContentTweaker calls this too early, even before Wizardry's spells are instantiated.
	// Stashing entries avoids network ID shifts from parent constructors.
	@ZenMethod
	public static void create(String name, float r, float g, float b, String[] potions) {
		entries.add(new BuffSpellEntry(name, potions, r, g, b));
	}

	public static Spell instantiate(BuffSpellEntry entry) {
		if (entry == null) {
			throw new RuntimeException("BuffSpellEntry cannot be null!");
		}

		@SuppressWarnings("unchecked")
		Supplier<Potion>[] potionSuppliers = (Supplier<Potion>[]) new Supplier[entry.potions.length];
		
		for (int i = 0; i < entry.potions.length; i++) {
			final String potionName = entry.potions[i];
			Potion potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(potionName));
			if (potion == null) {
				throw new RuntimeException("Could not find potion effect " + potionName + "!");
			}
			potionSuppliers[i] = () -> potion;
		}

		SpellBuff spell = new SpellBuff(
				"contenttweaker",
				entry.name,
				entry.r,
				entry.g,
				entry.b,
				potionSuppliers
		);

		return spell;
	}
}
