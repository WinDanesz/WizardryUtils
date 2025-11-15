# Minion Spell Customization Guide

## Overview

`ZenMinionSpell` now supports extensive customization of summoned minions, including:
- ✅ Potion effects
- ✅ Attribute modifiers (health, attack damage, speed, etc.)
- ✅ **NBT data (equipment, variants, custom properties)**
- ✅ Lazy loading with registry names (compatible with third-party mods)
- ✅ Memoization (efficient entity class caching)

> 📘 **See also:** [MINION_NBT_EXAMPLES.md](MINION_NBT_EXAMPLES.md) for comprehensive NBT examples including baby mobs, equipment, variants, and modded entities.

---

## Basic Usage

### Simple Minion Spell (No Customization)

```zenscript
// Create a basic zombie minion spell
mods.wizardryutils.MinionSpells.create("zombie_minion", "minecraft:zombie");

// Create a skeleton archer minion
mods.wizardryutils.MinionSpells.create("skeleton_minion", "minecraft:skeleton");

// Works with modded entities too!
mods.wizardryutils.MinionSpells.create("ice_wraith_minion", "ebwizardry:ice_wraith");
```

---

## Advanced Usage: Custom Minions

### With Potion Effects

```zenscript
import mods.wizardryutils.MinionSpells.PotionEffectData;
import mods.wizardryutils.MinionSpells.AttributeModifierData;

// Create a super-powered zombie with strength and regeneration
var potionEffects = [
    PotionEffectData("minecraft:strength", 1, false),        // Strength II (no particles)
    PotionEffectData("minecraft:regeneration", 0, true),     // Regeneration I (with particles)
    PotionEffectData("minecraft:resistance", 0, false)       // Resistance I
] as PotionEffectData[];

mods.wizardryutils.MinionSpells.createCustom(
    "buffed_zombie",
    "minecraft:zombie",
    potionEffects,
    null  // no attribute mods
);
```

### With Attribute Modifiers

```zenscript
import mods.wizardryutils.MinionSpells.AttributeModifierData;

// Create a tank zombie with bonus health
var attributeMods = [
    AttributeModifierData("generic.maxHealth", 20.0, 0),     // +20 health (operation 0 = add)
    AttributeModifierData("generic.knockbackResistance", 0.5, 0)  // +0.5 knockback resistance
] as AttributeModifierData[];

mods.wizardryutils.MinionSpells.createCustom(
    "tank_zombie",
    "minecraft:zombie",
    null,  // no potion effects
    attributeMods
);
```

### Full Customization Example

```zenscript
import mods.wizardryutils.MinionSpells.PotionEffectData;
import mods.wizardryutils.MinionSpells.AttributeModifierData;

// Create an elite minion with both effects and attribute mods
var potionEffects = [
    PotionEffectData("minecraft:strength", 2, false),        // Strength III
    PotionEffectData("minecraft:speed", 1, false),           // Speed II
    PotionEffectData("minecraft:fire_resistance", 0, false)  // Fire Resistance I
] as PotionEffectData[];

var attributeMods = [
    AttributeModifierData("generic.maxHealth", 40.0, 0),     // +40 health
    AttributeModifierData("generic.attackDamage", 1.0, 2),   // +100% attack damage (double damage)
    AttributeModifierData("generic.movementSpeed", 0.3, 2),  // +30% movement speed
    AttributeModifierData("generic.armor", 10.0, 0)          // +10 armor points
] as AttributeModifierData[];

mods.wizardryutils.MinionSpells.createCustom(
    "elite_zombie_warrior",
    "minecraft:zombie",
    potionEffects,
    attributeMods
);
```

### With NBT Data (Equipment, Variants, etc.)

> ⚠️ **CRITICAL**: NBT data objects MUST be cast to `IData` using `as IData` or you'll get a type error!

```zenscript
import mods.wizardryutils.MinionSpells.PotionEffectData;
import mods.wizardryutils.MinionSpells.AttributeModifierData;

// Baby zombie with diamond armor and custom name
var nbt = {
    IsBaby: 1 as byte,
    CustomName: "§c§lTiny Terror",
    CustomNameVisible: 1 as byte,
    HandItems: [
        {id: "minecraft:diamond_sword", Count: 1 as byte},
        {}
    ],
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

var mods = [
    AttributeModifierData("generic.maxHealth", 20.0, 0)
] as AttributeModifierData[];

mods.wizardryutils.MinionSpells.createWithNBT(
    "baby_warrior",
    "minecraft:zombie",
    effects,
    mods,
    nbt
);
```

> 💡 **For more NBT examples**, see [MINION_NBT_EXAMPLES.md](MINION_NBT_EXAMPLES.md) with examples for baby mobs, equipment, mob variants, and modded entities.
> 
> ⚠️ **REMEMBER**: All examples in the NBT guide need `as IData` added - see [NBT_QUICK_FIX.md](NBT_QUICK_FIX.md) for details!

---

## API Reference

### Methods

#### `create(name, entityRegistryName)`
Creates a basic minion spell with no customization.

#### `createCustom(name, entityRegistryName, potionEffects, attributeModifiers)`
Creates a minion spell with potion effects and attribute modifiers.

#### `createWithNBT(name, entityRegistryName, potionEffects, attributeModifiers, nbtData)`
Creates a fully customized minion spell including NBT data for equipment, variants, and special properties.

**All arrays and nbtData can be `null` if not needed.**

---

### PotionEffectData Constructor

```zenscript
PotionEffectData(potionRegistryName, amplifier, showParticles)
```

**Parameters:**
- `potionRegistryName` (String): Registry name of the potion effect (e.g., `"minecraft:strength"`)
- `amplifier` (int): Effect amplifier/level (0 = level I, 1 = level II, etc.)
- `showParticles` (boolean): Whether to show potion particles

**Common Potion Effects:**
- `minecraft:strength` - Increased melee damage
- `minecraft:speed` - Increased movement speed
- `minecraft:regeneration` - Health regeneration
- `minecraft:resistance` - Damage reduction
- `minecraft:fire_resistance` - Immunity to fire
- `minecraft:invisibility` - Makes minion invisible
- `minecraft:absorption` - Extra yellow hearts

---

### AttributeModifierData Constructor

```zenscript
AttributeModifierData(attributeName, amount, operation)
```

**Parameters:**
- `attributeName` (String): Name of the attribute to modify
- `amount` (double): The amount to modify by
- `operation` (int): How to apply the modifier:
  - `0` = **Add** - Adds to base value
  - `1` = **Multiply Base** - Multiplies base value (then adds to total)
  - `2` = **Multiply Total** - Multiplies total value (applied last)

**Common Attributes:**
- `generic.maxHealth` - Maximum health
- `generic.followRange` - Detection/follow range
- `generic.knockbackResistance` - Resistance to knockback (0.0 to 1.0)
- `generic.movementSpeed` - Movement speed
- `generic.attackDamage` - Melee attack damage
- `generic.armor` - Armor points
- `generic.armorToughness` - Armor toughness

**Operation Examples:**
```zenscript
// Operation 0 (Add): 10 base health + 20 from modifier = 30 total
AttributeModifierData("generic.maxHealth", 20.0, 0)

// Operation 1 (Multiply Base): 10 base * (1 + 0.5) = 15 total
AttributeModifierData("generic.maxHealth", 0.5, 1)

// Operation 2 (Multiply Total): everything * (1 + 0.5) = 1.5x final
AttributeModifierData("generic.maxHealth", 0.5, 2)
```

---

## Advanced Examples

### Glass Cannon Minion
High damage, low health:
```zenscript
var effects = [
    PotionEffectData("minecraft:strength", 3, false)  // Strength IV
] as PotionEffectData[];

var mods = [
    AttributeModifierData("generic.maxHealth", -10.0, 0),    // -10 health
    AttributeModifierData("generic.attackDamage", 2.0, 2),   // +200% damage (3x total)
    AttributeModifierData("generic.movementSpeed", 0.5, 2)   // +50% speed
] as AttributeModifierData[];

mods.wizardryutils.MinionSpells.createCustom("glass_cannon", "minecraft:zombie", effects, mods);
```

### Tank Minion
Slow but tanky:
```zenscript
var effects = [
    PotionEffectData("minecraft:resistance", 2, false),      // Resistance III
    PotionEffectData("minecraft:regeneration", 1, true),     // Regeneration II
    PotionEffectData("minecraft:slowness", 0, false)         // Slowness I
] as PotionEffectData[];

var mods = [
    AttributeModifierData("generic.maxHealth", 60.0, 0),          // +60 health
    AttributeModifierData("generic.armor", 20.0, 0),              // +20 armor
    AttributeModifierData("generic.knockbackResistance", 1.0, 0)  // 100% knockback resist
] as AttributeModifierData[];

mods.wizardryutils.MinionSpells.createCustom("tank", "minecraft:zombie_pigman", effects, mods);
```

### Blazing Fast Minion
```zenscript
var effects = [
    PotionEffectData("minecraft:speed", 3, true)  // Speed IV
] as PotionEffectData[];

var mods = [
    AttributeModifierData("generic.movementSpeed", 1.0, 2),  // +100% speed (2x)
    AttributeModifierData("generic.followRange", 20.0, 0)    // +20 block follow range
] as AttributeModifierData[];

mods.wizardryutils.MinionSpells.createCustom("speedy", "minecraft:skeleton", effects, mods);
```

---

## Technical Details

### Lazy Loading
- Entity classes are looked up **at spell cast time**, not at registration
- This prevents issues with third-party mods that register entities late
- Uses **memoization** to cache the class after first lookup (efficient)

### Potion Effect Duration
- All potion effects are applied with `Integer.MAX_VALUE` duration
- Effectively **permanent** for the minion's lifetime
- Minions despawn based on the spell's `minion_lifetime` property

### Attribute Modifier Persistence
- Attribute modifiers are applied when the minion spawns
- They persist for the minion's entire lifetime
- Modifiers stack with the spell's built-in potency/health modifiers

---

## Compatibility

✅ **Compatible with:**
- Vanilla Minecraft entities
- Electroblob's Wizardry entities
- Any modded entities extending `EntityCreature`
- Third-party mods (uses lazy loading)

❌ **Not compatible with:**
- Entities that don't extend `EntityCreature`
- Boss mobs (most have special summoning logic)

---

## Tips & Best Practices

1. **Balance your minions** - Overpowered minions can break gameplay
2. **Test in creative** - Use `/summon` to verify entity registry names
3. **Use operation 2 for percentages** - More intuitive for scaling
4. **Hide particles on subtle effects** - Set `showParticles` to `false`
5. **Check mod documentation** - Some mods add custom attributes
6. **Combine with spell properties** - Use JSON to set `minion_lifetime`, `minion_count`, etc.

---

## Troubleshooting

**"Could not find entity with registry name..."**
- Verify the entity registry name is correct
- Check if the mod adding that entity is loaded
- Try using `/summon modid:entity_name` to test

**"Entity does not extend EntityCreature"**
- Not all entities can be minions
- Only creatures (not items, projectiles, etc.) work
- Some bosses have special handling and won't work

**Attributes not applying**
- Verify the attribute name is correct (case-sensitive!)
- Some entities don't have all attributes (e.g., no attack damage on passive mobs)
- Check operation value (0, 1, or 2)

---

## Migration from Old Class-Path System

**Old syntax (deprecated):**
```zenscript
mods.wizardryutils.MinionSpells.create("zombie", "net.minecraft.entity.monster.EntityZombie");
```

**New syntax:**
```zenscript
mods.wizardryutils.MinionSpells.create("zombie", "minecraft:zombie");
```

**Benefits:**
- ✅ Cleaner, more intuitive
- ✅ Works with third-party mods
- ✅ Consistent with item/potion spell syntax
- ✅ No dependency on internal package structure
