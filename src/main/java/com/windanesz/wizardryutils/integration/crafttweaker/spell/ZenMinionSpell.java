package com.windanesz.wizardryutils.integration.crafttweaker.spell;

import com.windanesz.wizardryutils.spell.SpellDynamicMinion;
import crafttweaker.annotations.ZenRegister;
import electroblob.wizardry.spell.Spell;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.wizardryutils.MinionSpells")
@ZenRegister
public class ZenMinionSpell {

	public static final class MinionSpellEntry {
		public final String name;
		public final String entityName;

		public MinionSpellEntry(String name, String entityName) {
			this.name = name;
			this.entityName = entityName;
		}
	}

	public static final List<MinionSpellEntry> entries = new ArrayList<>();

	@ZenMethod
	// ContentTweaker calls this too early, even before Wizardry's spells are instantiated.
	// Stashing entries avoids network ID shifts from parent constructors.
	public static void create(String name, String entityName) {
		entries.add(new MinionSpellEntry(name, entityName));
	}

	public static Spell instantiate(MinionSpellEntry entry) {
		String name = entry.name;
		String entityName = entry.entityName;;
		create(name, entityName);

		EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityName));
		if (entityEntry == null) {
			throw new RuntimeException("Could not find minion entity " + entityName + "!");
		}

		SpellDynamicMinion<EntityCreature> spell = new SpellDynamicMinion<>(
				"contenttweaker",
				name,
				world -> {
					try {
						EntityCreature entity = (EntityCreature) entityEntry.newInstance(world);
						if (entity != null) return entity;
						throw new RuntimeException("Entity " + entityName + " is not a valid minion entity!");
					} catch (Exception e) {
						throw new RuntimeException("Could not instantiate minion entity " + entityName + "!", e);
					}
				}
		);

		return spell;
	}
}
