#loader contenttweaker

import crafttweaker.data.IData;
import mods.wizardryutils.PotionEffectData;
import mods.wizardryutils.AttributeModifierData;

// Baby zombie with diamond armor and custom name
var nbt = {
    IsBaby: 1 as byte,
    CustomName: "§c§lTiny Terror",
    CustomNameVisible: 1 as byte,
    ArmorItems: [
        {id: "minecraft:diamond_boots", Count: 1 as byte},
        {id: "minecraft:diamond_leggings", Count: 1 as byte},
        {id: "minecraft:diamond_chestplate", Count: 1 as byte},
        {id: "minecraft:diamond_helmet", Count: 1 as byte}
    ]
} as IData;  // IMPORTANT: Cast to IData!

var effects = [
    PotionEffectData("minecraft:strength", 1, false)
] as PotionEffectData[];

var attributes = [
    AttributeModifierData("generic.maxHealth", 20.0, 0)
] as AttributeModifierData[];

mods.wizardryutils.MinionSpells.create(
    "baby_warrior",
    "minecraft:zombie",
    effects,
    attributes,
    nbt
);