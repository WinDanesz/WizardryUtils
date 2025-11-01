package com.windanesz.wizardryutils.integration.crafttweaker.spell;

import com.windanesz.wizardryutils.spell.SpellDynamicAreaEffect;
import crafttweaker.annotations.ZenRegister;
import electroblob.wizardry.spell.Spell;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.wizardryutils.AreaEffectSpells")
@ZenRegister
public class ZenAreaEffectSpell {

	public static final class AreaEffectSpellEntry {
		public final String name;
		public final String[] potions;
		public final float r;
		public final float g;
		public final float b;

		public AreaEffectSpellEntry(String name, String[] potions, float r, float g, float b) {
			this.name = name;
			this.potions = potions;
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}

	public static final List<AreaEffectSpellEntry> entries = new ArrayList<>();

	/**
	 * Creates a new area effect spell that spawns a construct applying potion effects to nearby entities.
	 * 
	 * @param name The name of the spell (will be prefixed with the mod ID)
	 * @param r Red color component (0.0 - 1.0)
	 * @param g Green color component (0.0 - 1.0)
	 * @param b Blue color component (0.0 - 1.0)
	 * @param potions Array of potion effect registry names (e.g., ["minecraft:regeneration", "minecraft:speed"])
	 */
	@ZenMethod
	public static void create(String name, float r, float g, float b, String[] potions) {
		entries.add(new AreaEffectSpellEntry(name, potions, r, g, b));
	}

	public static Spell instantiate(AreaEffectSpellEntry entry) {
		if (entry == null) {
			throw new RuntimeException("AreaEffectSpellEntry cannot be null!");
		}

		@SuppressWarnings("unchecked")
		Supplier<Potion>[] potionSuppliers = (Supplier<Potion>[]) new Supplier[entry.potions.length];
		
		for (int i = 0; i < entry.potions.length; i++) {
			final String potionName = entry.potions[i];
			// Defer potion lookup until the supplier is actually called (at spell cast time)
			// This allows third-party mods to register their potions before they're needed
			// Uses a memoizing supplier to cache the result after the first lookup
			potionSuppliers[i] = new Supplier<Potion>() {
				private Potion cached = null;
				
				@Override
				public Potion get() {
					if (cached == null) {
						cached = ForgeRegistries.POTIONS.getValue(new ResourceLocation(potionName));
						if (cached == null) {
							throw new RuntimeException("Could not find potion effect " + potionName + "!");
						}
					}
					return cached;
				}
			};
		}

		SpellDynamicAreaEffect spell = new SpellDynamicAreaEffect(
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
