package com.windanesz.wizardryutils.server;

import com.windanesz.wizardryutils.Settings;
import com.windanesz.wizardryutils.WizardryUtils;
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
	public static RangedAttribute COOLDOWN = (RangedAttribute) new RangedAttribute(null, WizardryUtils.MODID + ".SpellCooldown", 100, -500, MAX_VALUE).setShouldWatch(true);
	public static RangedAttribute CONDENSING = (RangedAttribute) new RangedAttribute(null, WizardryUtils.MODID + ".SpellCondensing", 0, -500, MAX_VALUE).setShouldWatch(true);
	//public static RangedAttribute SIPHON = (RangedAttribute) new RangedAttribute(null, WizardryUtils.MODID + ".SpellSiphon", 100, -500, MAX_VALUE).setShouldWatch(true);

	public static HashMap<RangedAttribute, String> getSpellModifierAttributes() {
		return ATTRIBUTE_HASH_MAP;
	}

	public static void init() {
		putAll();
	}

	private static void putAll() {
		ATTRIBUTE_HASH_MAP.put(POTENCY, SpellModifiers.POTENCY);
		ATTRIBUTE_HASH_MAP.put(CHARGEUP, SpellModifiers.CHARGEUP);
		ATTRIBUTE_HASH_MAP.put(COST, SpellModifiers.COST);
		ATTRIBUTE_HASH_MAP.put(DURATION, "duration");
		ATTRIBUTE_HASH_MAP.put(BLAST, "blast");
		ATTRIBUTE_HASH_MAP.put(COOLDOWN, "cooldown");
		ATTRIBUTE_HASH_MAP.put(CONDENSING, "condenser");
		//ATTRIBUTE_HASH_MAP.put(SIPHON, "siphon");
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
