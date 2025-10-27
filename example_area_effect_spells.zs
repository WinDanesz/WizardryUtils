// Example ZenScript file for creating Area Effect spells with WizardryUtils
// Place this file in: scripts/area_effect_spells.zs

import mods.wizardryutils.AreaEffectSpells;

// Healing Aura - Creates a healing zone that regenerates health
ZenAreaEffectSpell.create(
    "healing_aura",
    0.3,  // Red
    1.0,  // Green
    0.3,  // Blue
    ["minecraft:regeneration"]
);

// Poison Cloud - Applies poison and weakness to enemies
AreaEffectSpells.create(
    "poison_cloud",
    0.2,  // Red (dark green)
    0.8,  // Green
    0.2,  // Blue
    [
        "minecraft:poison",
        "minecraft:weakness"
    ]
);

// Speed Aura - Boosts movement and jump for allies
AreaEffectSpells.create(
    "speed_aura",
    0.0,  // Red
    0.5,  // Green
    1.0,  // Blue
    [
        "minecraft:speed",
        "minecraft:jump_boost"
    ]
);

// Fire Shrine - Grants fire resistance
AreaEffectSpells.create(
    "fire_shrine",
    1.0,  // Red (orange)
    0.5,  // Green
    0.0,  // Blue
    ["minecraft:fire_resistance"]
);

// Wither Field - Dangerous area that withers enemies
AreaEffectSpells.create(
    "wither_field",
    0.2,  // Red (dark gray)
    0.2,  // Green
    0.2,  // Blue
    [
        "minecraft:wither",
        "minecraft:blindness"
    ]
);

// Battle Banner - Multi-buff support aura
AreaEffectSpells.create(
    "battle_banner",
    1.0,  // Red (gold)
    0.8,  // Green
    0.0,  // Blue
    [
        "minecraft:strength",
        "minecraft:resistance",
        "minecraft:speed"
    ]
);
