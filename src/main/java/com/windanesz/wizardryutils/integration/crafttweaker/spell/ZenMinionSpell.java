package com.windanesz.wizardryutils.integration.crafttweaker.spell;

import com.windanesz.wizardryutils.spell.SpellDynamicMinion;
import crafttweaker.annotations.ZenRegister;
import electroblob.wizardry.spell.Spell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.wizardryutils.MinionSpells")
@ZenRegister
public class ZenMinionSpell {

	public static final class MinionSpellEntry {
		public final String name;
		public final String entityClassName;

		public MinionSpellEntry(String name, String entityClassName) {
			this.name = name;
			this.entityClassName = entityClassName;
		}
	}

	public static final List<MinionSpellEntry> entries = new ArrayList<>();

	/**
	 * Creates a new minion spell using the specified entity class.
	 * <p>
	 * Takes a fully qualified class path instead of entity registry name to avoid early registration timing issues.
	 * The class is instantiated via reflection at runtime when the spell is cast, not during registration.
	 * <p>
	 * Example usage in CraftTweaker:
	 * <pre>
	 * // For vanilla zombies
	 * mods.wizardryutils.MinionSpells.create("zombie_minion", "net.minecraft.entity.monster.EntityZombie");
	 * 
	 * // For modded entities
	 * mods.wizardryutils.MinionSpells.create("custom_minion", "com.example.mod.entity.EntityCustomCreature");
	 * </pre>
	 * 
	 * @param name The name of the spell (will be prefixed with the mod ID)
	 * @param entityClassName The fully qualified class name of the minion entity (must extend EntityCreature)
	 */
	@ZenMethod
	public static void create(String name, String entityClassName) {
		entries.add(new MinionSpellEntry(name, entityClassName));
	}

	public static Spell instantiate(MinionSpellEntry entry) {
		if (entry == null) {
			throw new RuntimeException("MinionSpellEntry cannot be null!");
		}

		String name = entry.name;
		String entityClassName = entry.entityClassName;

		// Validate the class exists and is a valid minion entity at registration time
		try {
			Class<?> entityClass = Class.forName(entityClassName);
			if (!Entity.class.isAssignableFrom(entityClass)) {
				throw new RuntimeException("Class " + entityClassName + " is not an Entity!");
			}
			if (!EntityCreature.class.isAssignableFrom(entityClass)) {
				throw new RuntimeException("Class " + entityClassName + " does not extend EntityCreature!");
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Could not find minion entity class " + entityClassName + "!", e);
		}

		SpellDynamicMinion<EntityCreature> spell = new SpellDynamicMinion<>(
				"contenttweaker",
				name,
				world -> {
					try {
						Class<?> entityClass = Class.forName(entityClassName);
						Constructor<?> constructor = entityClass.getConstructor(World.class);
						EntityCreature entity = (EntityCreature) constructor.newInstance(world);
						
						// Set drop chance to 0 for all equipment slots
						for (net.minecraft.inventory.EntityEquipmentSlot slot : net.minecraft.inventory.EntityEquipmentSlot.values()) {
							entity.setDropChance(slot, 0.0f);
						}
						
						if (entity != null) {
							return entity;
						} else {
							throw new RuntimeException("Entity " + entityClassName + " is not a valid minion entity!");
						}
					} catch (Exception e) {
						throw new RuntimeException("Could not instantiate minion entity " + entityClassName + "!", e);
					}
				}
		);

		return spell;
	}
}
