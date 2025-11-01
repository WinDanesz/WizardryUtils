package com.windanesz.wizardryutils.integration.crafttweaker.spell;

import com.windanesz.wizardryutils.spell.SpellDynamicMinion;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import crafttweaker.api.minecraft.CraftTweakerMC;
import electroblob.wizardry.spell.Spell;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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
		public final String entityRegistryName;
		public final PotionEffectData[] potionEffects;
		public final AttributeModifierData[] attributeModifiers;
		public final IData nbtData;

		public MinionSpellEntry(String name, String entityRegistryName, PotionEffectData[] potionEffects, AttributeModifierData[] attributeModifiers, IData nbtData) {
			this.name = name;
			this.entityRegistryName = entityRegistryName;
			this.potionEffects = potionEffects != null ? potionEffects : new PotionEffectData[0];
			this.attributeModifiers = attributeModifiers != null ? attributeModifiers : new AttributeModifierData[0];
			this.nbtData = nbtData;
		}
	}

	public static final List<MinionSpellEntry> entries = new ArrayList<>();

	/**
	 * Creates a simple minion spell with just the entity.
	 * <p>
	 * Example: mods.wizardryutils.MinionSpells.create("zombie_minion", "minecraft:zombie");
	 */
	@ZenMethod
	public static void create(String name, String entityRegistryName) {
		create(name, entityRegistryName, null, null, null);
	}

	/**
	 * Creates a minion spell with customization options.
	 * <p>
	 * All parameters except name and entityRegistryName can be null.
	 * <p>
	 * Example usage:
	 * <pre>
	 * // Just NBT (baby zombie)
	 * var nbt = {IsBaby: 1 as byte} as IData;
	 * mods.wizardryutils.MinionSpells.create("baby_zombie", "minecraft:zombie", null, null, nbt);
	 * 
	 * // Just effects and attributes
	 * var effects = [PotionEffectData("minecraft:strength", 1, false)] as PotionEffectData[];
	 * var mods = [AttributeModifierData("generic.maxHealth", 20.0, 0)] as AttributeModifierData[];
	 * mods.wizardryutils.MinionSpells.create("super_zombie", "minecraft:zombie", effects, mods, null);
	 * 
	 * // Full customization
	 * mods.wizardryutils.MinionSpells.create("elite_guardian", "minecraft:zombie_pigman", effects, mods, nbt);
	 * </pre>
	 * 
	 * @param name The name of the spell (will be prefixed with "contenttweaker:")
	 * @param entityRegistryName The registry name of the entity (e.g., "minecraft:zombie")
	 * @param potionEffects Array of potion effects to apply (can be null)
	 * @param attributeModifiers Array of attribute modifiers to apply (can be null)
	 * @param nbtData NBT data to apply to the entity (can be null) - must be cast as IData
	 */
	@ZenMethod
	public static void create(String name, String entityRegistryName, PotionEffectData[] potionEffects, AttributeModifierData[] attributeModifiers, IData nbtData) {
		entries.add(new MinionSpellEntry(name, entityRegistryName, potionEffects, attributeModifiers, nbtData));
	}

	public static Spell instantiate(MinionSpellEntry entry) {
		if (entry == null) {
			throw new RuntimeException("MinionSpellEntry cannot be null!");
		}

		String name = entry.name;
		String entityRegistryName = entry.entityRegistryName;

		// Create spell with lazy entity lookup using a memoizing supplier
		SpellDynamicMinion<EntityCreature> spell = new SpellDynamicMinion<>(
				"contenttweaker",
				name,
				new java.util.function.Function<World, EntityCreature>() {
					private Class<? extends EntityCreature> cachedClass = null;
					
					@Override
					public EntityCreature apply(World world) {
						try {
							// Lazy lookup and cache the entity class on first call
							if (cachedClass == null) {
								Class<?> entityClass = net.minecraft.entity.EntityList.getClass(new ResourceLocation(entityRegistryName));
								if (entityClass == null) {
									throw new RuntimeException("Could not find entity with registry name " + entityRegistryName + "!");
								}
								if (!EntityCreature.class.isAssignableFrom(entityClass)) {
									throw new RuntimeException("Entity " + entityRegistryName + " does not extend EntityCreature!");
								}
								cachedClass = entityClass.asSubclass(EntityCreature.class);
							}
							
							// Create entity instance
							Constructor<? extends EntityCreature> constructor = cachedClass.getConstructor(World.class);
							EntityCreature entity = constructor.newInstance(world);
							
							// Apply NBT data first (so it can be overridden by effects/attributes if needed)
							if (entry.nbtData != null) {
								NBTTagCompound nbt = (NBTTagCompound) CraftTweakerMC.getNBT(entry.nbtData);
								if (nbt != null) {
									entity.readFromNBT(nbt);
									// Re-set position after NBT read as it might have been overwritten
									// Position will be set properly by the spell code later
								}
							}
							
							// Apply custom potion effects
							for (PotionEffectData effectData : entry.potionEffects) {
								Potion potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(effectData.potionRegistryName));
								if (potion != null) {
									entity.addPotionEffect(new PotionEffect(
											potion,
											Integer.MAX_VALUE, // Permanent
											effectData.amplifier,
											false, // ambient
											effectData.showParticles
									));
								}
							}
							
							// Apply custom attribute modifiers
							for (AttributeModifierData modData : entry.attributeModifiers) {
								IAttributeInstance attribute = entity.getAttributeMap().getAttributeInstanceByName(modData.attributeName);
								if (attribute != null) {
									attribute.applyModifier(new AttributeModifier(
											"zenscript_custom_" + modData.attributeName,
											modData.amount,
											modData.operation
									));
								}
							}
							
							// Set drop chance to 0 for all equipment slots
							for (net.minecraft.inventory.EntityEquipmentSlot slot : net.minecraft.inventory.EntityEquipmentSlot.values()) {
								entity.setDropChance(slot, 0.0f);
							}
							
							return entity;
						} catch (Exception e) {
							throw new RuntimeException("Could not instantiate minion entity " + entityRegistryName + "!", e);
						}
					}
				}
		);

		return spell;
	}
}
