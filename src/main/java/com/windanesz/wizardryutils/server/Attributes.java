package com.windanesz.wizardryutils.server;

import com.windanesz.wizardryutils.Settings;
import com.windanesz.wizardryutils.WizardryUtils;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

/**
 * Class of the new attributes, yay!
 * The magic happens in {@link com.windanesz.wizardryutils.handler.WizardryUtilsEventHandler}
 * <p>
 * Author: WinDanesz
 *
 * <p><b>Some info regarding using these</b></p>
 * Attribute attaching operations as in the Minecraft wiki:
 * <p><b>add</b> aka Operation 0: Increment X by Amount</p>
 * (amount +/-): Saved as operation 0. Adds all of the modifiers' amounts to the current value of the attribute. For example, modifying an attribute with
 * {Amount:2,Operation:0} and {Amount:4,Operation:0} with a Base of 3 results in 9 (3 + 2 + 4 = 9).
 * <p><b>multiply_base</b> aka Operation 1: Increment Y by X * Amount</p>
 * (amount % +/-, additive): Saved as operation 1. Multiplies the current value of the attribute by (1 + x), where x is the sum of the
 * modifiers' amounts. For example, modifying an attribute with {Amount:2,Operation:1} and {Amount:4,Operation:1} with a Base of 3 results in 21 (3 * (1 + 2 + 4) = 21).
 * <p><b>multiply</b> aka Operation 2: Y = Y * (1 + Amount) (equivalent to Increment Y by Y * Amount).</p>
 * (amount % +/-, multiplicative):
 * Saved as operation 2. For every modifier, multiplies the current value of the attribute by (1 + x), where x is the amount of the particular modifier.
 * Functions the same as Operation 1 if there is only a single modifier with operation 1 or 2. However, for multiple modifiers it multiplies the modifiers rather than adding them. For example, modifying an attribute with {Amount:2,Operation:2} and {Amount:4,Operation:2} with a Base of 3 results in 45 (3 * (1 + 2) * (1 + 4) = 45).[5]
 */
public class Attributes {

	public static final double MAX_VALUE = Settings.generalSettings.attribute_modifier_max;
	// This is just a map of everything because I'm lazy to list them always when needed.
	public static final HashMap<RangedAttribute, String> ATTRIBUTE_HASH_MAP = new HashMap<>();

	// setShouldWatch allows to share the attribute value and modifiers with the client
	public static RangedAttribute POTENCY = (RangedAttribute) new RangedAttribute(null, WizardryUtils.MODID + ".SpellPotency", 100, -500, MAX_VALUE).setShouldWatch(true);
	public static RangedAttribute CHARGEUP = (RangedAttribute) new RangedAttribute(null, WizardryUtils.MODID + ".SpellChargeup", 100, -500, MAX_VALUE).setShouldWatch(true);
	public static RangedAttribute COST = (RangedAttribute) new RangedAttribute(null, WizardryUtils.MODID + ".SpellCost", 100, -500, MAX_VALUE).setShouldWatch(true);
	public static RangedAttribute DURATION = (RangedAttribute) new RangedAttribute(null, WizardryUtils.MODID + ".SpellDuration", 100, -500, MAX_VALUE).setShouldWatch(true);
	public static RangedAttribute BLAST = (RangedAttribute) new RangedAttribute(null, WizardryUtils.MODID + ".SpellBlast", 100, -500, MAX_VALUE).setShouldWatch(true);
	public static RangedAttribute RANGE = (RangedAttribute) new RangedAttribute(null, WizardryUtils.MODID + ".SpellRange", 100, -500, MAX_VALUE).setShouldWatch(true);
	public static RangedAttribute COOLDOWN = (RangedAttribute) new RangedAttribute(null, WizardryUtils.MODID + ".SpellCooldown", 100, -500, MAX_VALUE).setShouldWatch(true);
	public static RangedAttribute CONDENSING = (RangedAttribute) new RangedAttribute(null, WizardryUtils.MODID + ".SpellCondensing", 0, -500, MAX_VALUE).setShouldWatch(true);
	//public static RangedAttribute SIPHON = (RangedAttribute) new RangedAttribute(null, WizardryUtils.MODID + ".SpellSiphon", 100, -500, MAX_VALUE).setShouldWatch(true);


	public static RangedAttributeElemental FIRE_POTENCY = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".FireSpellPotency", 100, -500, MAX_VALUE, Element.FIRE, SpellModifiers.POTENCY).setShouldWatch(true);
	public static RangedAttributeElemental FIRE_COST = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".FireSpellCost", 100, -500, MAX_VALUE, Element.FIRE, SpellModifiers.COST).setShouldWatch(true);
	public static RangedAttributeElemental FIRE_DURATION = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".FireSpellDuration", 100, -500, MAX_VALUE, Element.FIRE, "duration").setShouldWatch(true);
	public static RangedAttributeElemental FIRE_BLAST = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".FireSpellBlast", 100, -500, MAX_VALUE, Element.FIRE, "blast").setShouldWatch(true);
	public static RangedAttributeElemental FIRE_RANGE = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".FireSpellRange", 100, -500, MAX_VALUE, Element.FIRE, "range").setShouldWatch(true);
	public static RangedAttributeElemental FIRE_COOLDOWN = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".FireSpellCooldown", 100, -500, MAX_VALUE, Element.FIRE, "cooldown").setShouldWatch(true);

	public static RangedAttributeElemental ICE_POTENCY = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".IceSpellPotency", 100, -500, MAX_VALUE, Element.ICE, SpellModifiers.POTENCY).setShouldWatch(true);
	public static RangedAttributeElemental ICE_COST = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".IceSpellCost", 100, -500, MAX_VALUE, Element.ICE, SpellModifiers.COST).setShouldWatch(true);
	public static RangedAttributeElemental ICE_DURATION = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".IceSpellDuration", 100, -500, MAX_VALUE, Element.ICE, "duration").setShouldWatch(true);
	public static RangedAttributeElemental ICE_BLAST = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".IceSpellBlast", 100, -500, MAX_VALUE, Element.ICE, "blast").setShouldWatch(true);
	public static RangedAttributeElemental ICE_RANGE = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".IceSpellRange", 100, -500, MAX_VALUE, Element.ICE, "range").setShouldWatch(true);
	public static RangedAttributeElemental ICE_COOLDOWN = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".IceSpellCooldown", 100, -500, MAX_VALUE, Element.ICE, "cooldown").setShouldWatch(true);

	public static RangedAttributeElemental LIGHTNING_POTENCY = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".LightningSpellPotency", 100, -500, MAX_VALUE, Element.LIGHTNING, SpellModifiers.POTENCY).setShouldWatch(true);
	public static RangedAttributeElemental LIGHTNING_COST = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".LightningSpellCost", 100, -500, MAX_VALUE, Element.LIGHTNING, SpellModifiers.COST).setShouldWatch(true);
	public static RangedAttributeElemental LIGHTNING_DURATION = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".LightningSpellDuration", 100, -500, MAX_VALUE, Element.LIGHTNING, "duration").setShouldWatch(true);
	public static RangedAttributeElemental LIGHTNING_BLAST = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".LightningSpellBlast", 100, -500, MAX_VALUE, Element.LIGHTNING, "blast").setShouldWatch(true);
	public static RangedAttributeElemental LIGHTNING_RANGE = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".LightningSpellRange", 100, -500, MAX_VALUE, Element.LIGHTNING, "range").setShouldWatch(true);
	public static RangedAttributeElemental LIGHTNING_COOLDOWN = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".LightningSpellCooldown", 100, -500, MAX_VALUE, Element.LIGHTNING, "cooldown").setShouldWatch(true);

	public static RangedAttributeElemental NECROMANCY_POTENCY = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".NecromancySpellPotency", 100, -500, MAX_VALUE, Element.NECROMANCY, SpellModifiers.POTENCY).setShouldWatch(true);
	public static RangedAttributeElemental NECROMANCY_COST = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".NecromancySpellCost", 100, -500, MAX_VALUE, Element.NECROMANCY, SpellModifiers.COST).setShouldWatch(true);
	public static RangedAttributeElemental NECROMANCY_DURATION = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".NecromancySpellDuration", 100, -500, MAX_VALUE, Element.NECROMANCY, "duration").setShouldWatch(true);
	public static RangedAttributeElemental NECROMANCY_BLAST = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".NecromancySpellBlast", 100, -500, MAX_VALUE, Element.NECROMANCY, "blast").setShouldWatch(true);
	public static RangedAttributeElemental NECROMANCY_RANGE = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".NecromancySpellRange", 100, -500, MAX_VALUE, Element.NECROMANCY, "range").setShouldWatch(true);
	public static RangedAttributeElemental NECROMANCY_COOLDOWN = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".NecromancySpellCooldown", 100, -500, MAX_VALUE, Element.NECROMANCY, "cooldown").setShouldWatch(true);

	public static RangedAttributeElemental EARTH_POTENCY = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".EarthSpellPotency", 100, -500, MAX_VALUE, Element.EARTH, SpellModifiers.POTENCY).setShouldWatch(true);
	public static RangedAttributeElemental EARTH_COST = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".EarthSpellCost", 100, -500, MAX_VALUE, Element.EARTH, SpellModifiers.COST).setShouldWatch(true);
	public static RangedAttributeElemental EARTH_DURATION = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".EarthSpellDuration", 100, -500, MAX_VALUE, Element.EARTH, "duration").setShouldWatch(true);
	public static RangedAttributeElemental EARTH_BLAST = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".EarthSpellBlast", 100, -500, MAX_VALUE, Element.EARTH, "blast").setShouldWatch(true);
	public static RangedAttributeElemental EARTH_RANGE = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".EarthSpellRange", 100, -500, MAX_VALUE, Element.EARTH, "range").setShouldWatch(true);
	public static RangedAttributeElemental EARTH_COOLDOWN = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".EarthSpellCooldown", 100, -500, MAX_VALUE, Element.EARTH, "cooldown").setShouldWatch(true);

	public static RangedAttributeElemental SORCERY_POTENCY = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".SorcerySpellPotency", 100, -500, MAX_VALUE, Element.SORCERY, SpellModifiers.POTENCY).setShouldWatch(true);
	public static RangedAttributeElemental SORCERY_COST = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".SorcerySpellCost", 100, -500, MAX_VALUE, Element.SORCERY, SpellModifiers.COST).setShouldWatch(true);
	public static RangedAttributeElemental SORCERY_DURATION = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".SorcerySpellDuration", 100, -500, MAX_VALUE, Element.SORCERY, "duration").setShouldWatch(true);
	public static RangedAttributeElemental SORCERY_BLAST = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".SorcerySpellBlast", 100, -500, MAX_VALUE, Element.SORCERY, "blast").setShouldWatch(true);
	public static RangedAttributeElemental SORCERY_RANGE = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".SorcerySpellRange", 100, -500, MAX_VALUE, Element.SORCERY, "range").setShouldWatch(true);
	public static RangedAttributeElemental SORCERY_COOLDOWN = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".SorcerySpellCooldown", 100, -500, MAX_VALUE, Element.SORCERY, "cooldown").setShouldWatch(true);

	public static RangedAttributeElemental HEALING_POTENCY = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".HealingSpellPotency", 100, -500, MAX_VALUE, Element.HEALING, SpellModifiers.POTENCY).setShouldWatch(true);
	public static RangedAttributeElemental HEALING_COST = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".HealingSpellCost", 100, -500, MAX_VALUE, Element.HEALING, SpellModifiers.COST).setShouldWatch(true);
	public static RangedAttributeElemental HEALING_DURATION = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".HealingSpellDuration", 100, -500, MAX_VALUE, Element.HEALING, "duration").setShouldWatch(true);
	public static RangedAttributeElemental HEALING_BLAST = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".HealingSpellBlast", 100, -500, MAX_VALUE, Element.HEALING, "blast").setShouldWatch(true);
	public static RangedAttributeElemental HEALING_RANGE = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".HealingSpellRange", 100, -500, MAX_VALUE, Element.HEALING, "range").setShouldWatch(true);
	public static RangedAttributeElemental HEALING_COOLDOWN = (RangedAttributeElemental) new RangedAttributeElemental(null, WizardryUtils.MODID + ".HealingSpellCooldown", 100, -500, MAX_VALUE, Element.HEALING, "cooldown").setShouldWatch(true);

	public static HashMap<RangedAttribute, String> getSpellModifierAttributes() {
		return ATTRIBUTE_HASH_MAP;
	}

	public static void init() {
		ATTRIBUTE_HASH_MAP.put(POTENCY, SpellModifiers.POTENCY);
		ATTRIBUTE_HASH_MAP.put(CHARGEUP, SpellModifiers.CHARGEUP);
		ATTRIBUTE_HASH_MAP.put(COST, SpellModifiers.COST);
		ATTRIBUTE_HASH_MAP.put(DURATION, "duration");
		ATTRIBUTE_HASH_MAP.put(BLAST, "blast");
		ATTRIBUTE_HASH_MAP.put(COOLDOWN, "cooldown");
		ATTRIBUTE_HASH_MAP.put(CONDENSING, "condenser");
		ATTRIBUTE_HASH_MAP.put(RANGE, "range");

		ATTRIBUTE_HASH_MAP.put(FIRE_POTENCY, FIRE_POTENCY.modifier);
		ATTRIBUTE_HASH_MAP.put(FIRE_COST, FIRE_COST.modifier);
		ATTRIBUTE_HASH_MAP.put(FIRE_DURATION, FIRE_DURATION.modifier);
		ATTRIBUTE_HASH_MAP.put(FIRE_BLAST, FIRE_BLAST.modifier);
		ATTRIBUTE_HASH_MAP.put(FIRE_RANGE, FIRE_RANGE.modifier);
		ATTRIBUTE_HASH_MAP.put(FIRE_COOLDOWN, FIRE_COOLDOWN.modifier);
		ATTRIBUTE_HASH_MAP.put(ICE_POTENCY, ICE_POTENCY.modifier);
		ATTRIBUTE_HASH_MAP.put(ICE_COST, ICE_COST.modifier);
		ATTRIBUTE_HASH_MAP.put(ICE_DURATION, ICE_DURATION.modifier);
		ATTRIBUTE_HASH_MAP.put(ICE_BLAST, ICE_BLAST.modifier);
		ATTRIBUTE_HASH_MAP.put(ICE_RANGE, ICE_RANGE.modifier);
		ATTRIBUTE_HASH_MAP.put(ICE_COOLDOWN, ICE_COOLDOWN.modifier);
		ATTRIBUTE_HASH_MAP.put(LIGHTNING_POTENCY, LIGHTNING_POTENCY.modifier);
		ATTRIBUTE_HASH_MAP.put(LIGHTNING_COST, LIGHTNING_COST.modifier);
		ATTRIBUTE_HASH_MAP.put(LIGHTNING_DURATION, LIGHTNING_DURATION.modifier);
		ATTRIBUTE_HASH_MAP.put(LIGHTNING_BLAST, LIGHTNING_BLAST.modifier);
		ATTRIBUTE_HASH_MAP.put(LIGHTNING_RANGE, LIGHTNING_RANGE.modifier);
		ATTRIBUTE_HASH_MAP.put(LIGHTNING_COOLDOWN, LIGHTNING_COOLDOWN.modifier);
		ATTRIBUTE_HASH_MAP.put(NECROMANCY_POTENCY, NECROMANCY_POTENCY.modifier);
		ATTRIBUTE_HASH_MAP.put(NECROMANCY_COST, NECROMANCY_COST.modifier);
		ATTRIBUTE_HASH_MAP.put(NECROMANCY_DURATION, NECROMANCY_DURATION.modifier);
		ATTRIBUTE_HASH_MAP.put(NECROMANCY_BLAST, NECROMANCY_BLAST.modifier);
		ATTRIBUTE_HASH_MAP.put(NECROMANCY_RANGE, NECROMANCY_RANGE.modifier);
		ATTRIBUTE_HASH_MAP.put(NECROMANCY_COOLDOWN, NECROMANCY_COOLDOWN.modifier);
		ATTRIBUTE_HASH_MAP.put(EARTH_POTENCY, EARTH_POTENCY.modifier);
		ATTRIBUTE_HASH_MAP.put(EARTH_COST, EARTH_COST.modifier);
		ATTRIBUTE_HASH_MAP.put(EARTH_DURATION, EARTH_DURATION.modifier);
		ATTRIBUTE_HASH_MAP.put(EARTH_BLAST, EARTH_BLAST.modifier);
		ATTRIBUTE_HASH_MAP.put(EARTH_RANGE, EARTH_RANGE.modifier);
		ATTRIBUTE_HASH_MAP.put(EARTH_COOLDOWN, EARTH_COOLDOWN.modifier);
		ATTRIBUTE_HASH_MAP.put(SORCERY_POTENCY, SORCERY_POTENCY.modifier);
		ATTRIBUTE_HASH_MAP.put(SORCERY_COST, SORCERY_COST.modifier);
		ATTRIBUTE_HASH_MAP.put(SORCERY_DURATION, SORCERY_DURATION.modifier);
		ATTRIBUTE_HASH_MAP.put(SORCERY_BLAST, SORCERY_BLAST.modifier);
		ATTRIBUTE_HASH_MAP.put(SORCERY_RANGE, SORCERY_RANGE.modifier);
		ATTRIBUTE_HASH_MAP.put(SORCERY_COOLDOWN, SORCERY_COOLDOWN.modifier);
		ATTRIBUTE_HASH_MAP.put(HEALING_POTENCY, HEALING_POTENCY.modifier);
		ATTRIBUTE_HASH_MAP.put(HEALING_COST, HEALING_COST.modifier);
		ATTRIBUTE_HASH_MAP.put(HEALING_DURATION, HEALING_DURATION.modifier);
		ATTRIBUTE_HASH_MAP.put(HEALING_BLAST, HEALING_BLAST.modifier);
		ATTRIBUTE_HASH_MAP.put(HEALING_RANGE, HEALING_RANGE.modifier);
		ATTRIBUTE_HASH_MAP.put(HEALING_COOLDOWN, HEALING_COOLDOWN.modifier);
	}

	/**
	 * Attach the new attributes to entities. Only for those who can cast spells
	 *
	 * @param entity to attach the entities to
	 */
	public static void addAttributes(EntityLivingBase entity) {

		if (entity instanceof EntityPlayer || entity instanceof ISpellCaster) {
			AbstractAttributeMap attributeMap = entity.getAttributeMap();
			for (IAttribute attribute : ATTRIBUTE_HASH_MAP.keySet()) {
				attributeMap.registerAttribute(attribute);
			}
		}
	}
}
