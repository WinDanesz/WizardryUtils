# Minion Spell NBT Data Examples

## Overview

With NBT support, you can create highly customized minions including:
- **Baby/Child variants** of mobs
- **Custom names** and name visibility
- **Equipment** (armor, weapons, tools)
- **AI behaviors** and mob-specific properties
- **Profession/variant selection** (villagers, cats, horses, etc.)
- **Team assignments** and custom tags
- **Mod-specific NBT** for modded entities

> ⚠️ **IMPORTANT**: All NBT data objects must be explicitly cast to `IData` using `as IData` in ZenScript!

---

## Basic NBT Examples

### Baby Mobs

Create baby versions of any mob:

```zenscript
// Baby zombie
var nbt = {
    IsBaby: 1 as byte
} as IData;  // IMPORTANT: Cast to IData!

mods.wizardryutils.MinionSpells.create("baby_zombie", "minecraft:zombie", null, null, nbt);

// Baby zombie with custom name
var nbt = {
    IsBaby: 1 as byte,
    CustomName: "Tiny Terror",
    CustomNameVisible: 1 as byte
} as IData;

mods.wizardryutils.MinionSpells.create("named_baby_zombie", "minecraft:zombie", null, null, nbt);
```

### Custom Names

```zenscript
// Named skeleton
var nbt = {
    CustomName: "Bone Warrior",
    CustomNameVisible: 1 as byte
} as IData;

mods.wizardryutils.MinionSpells.create("bone_warrior", "minecraft:skeleton", nbt);

// Named with formatting codes
var nbt = {
    CustomName: "§c§lFlaming Zombie",  // Red + Bold
    CustomNameVisible: 1 as byte
} as IData;

mods.wizardryutils.MinionSpells.create("flaming_zombie", "minecraft:zombie", nbt);
```

---

## Equipment Examples

### Armed Minions

Give your minions weapons and armor:

```zenscript
// Diamond-armored zombie with sword
var nbt = {
    HandItems: [
        {id: "minecraft:diamond_sword", Count: 1 as byte},
        {}
    ],
    ArmorItems: [
        {id: "minecraft:diamond_boots", Count: 1 as byte},
        {id: "minecraft:diamond_leggings", Count: 1 as byte},
        {id: "minecraft:diamond_chestplate", Count: 1 as byte},
        {id: "minecraft:diamond_helmet", Count: 1 as byte}
    ],
    HandDropChances: [0.0f, 0.0f],
    ArmorDropChances: [0.0f, 0.0f, 0.0f, 0.0f]
} as IData;
mods.wizardryutils.MinionSpells.create("diamond_warrior", "minecraft:zombie", nbt);

// Skeleton archer with enchanted bow
var nbt = {
    HandItems: [
        {
            id: "minecraft:bow",
            Count: 1 as byte,
            tag: {
                ench: [
                    {id: 48 as short, lvl: 5 as short},  // Power V
                    {id: 50 as short, lvl: 1 as short}   // Flame I
                ]
            }
        },
        {}
    ],
    HandDropChances: [0.0f, 0.0f]
} as IData;
mods.wizardryutils.MinionSpells.create("flame_archer", "minecraft:skeleton", nbt);
```

### Modded Equipment

```zenscript
// Minion with modded weapon
var nbt = {
    HandItems: [
        {id: "ebwizardry:magic_wand", Count: 1 as byte},
        {}
    ],
    HandDropChances: [0.0f, 0.0f]
} as IData;
mods.wizardryutils.MinionSpells.create("wizard_minion", "minecraft:zombie", nbt);
```

---

## Mob-Specific NBT

### Zombies

```zenscript
// Zombie villager with profession
var nbt = {
    IsVillager: 1 as byte,
    VillagerProfession: 3  // Blacksmith
};
mods.wizardryutils.MinionSpells.create("zombie_blacksmith", "minecraft:zombie", null, null, nbt);

// Converting zombie (won't actually convert, just visual)
var nbt = {
    ConversionTime: -1  // Perpetually converting
};
mods.wizardryutils.MinionSpells.create("converting_zombie", "minecraft:zombie", null, null, nbt);
```

### Skeletons

```zenscript
// Wither skeleton
var nbt = {
    SkeletonType: 1  // 0=normal, 1=wither, 2=stray
};
mods.wizardryutils.MinionSpells.create("wither_skeleton_minion", "minecraft:skeleton", null, null, nbt);

// Stray skeleton (from cold biomes)
var nbt = {
    SkeletonType: 2
};
mods.wizardryutils.MinionSpells.create("stray_minion", "minecraft:skeleton", null, null, nbt);
```

### Creepers

```zenscript
// Charged creeper (lightning effect)
var nbt = {
    powered: 1 as byte
};
mods.wizardryutils.MinionSpells.create("charged_creeper", "minecraft:creeper", null, null, nbt);

// Creeper with custom explosion radius
var nbt = {
    ExplosionRadius: 5 as byte,  // Default is 3
    Fuse: 20 as short             // Fuse time in ticks
};
mods.wizardryutils.MinionSpells.create("big_boom", "minecraft:creeper", null, null, nbt);
```

### Spiders

```zenscript
// Cave spider minion
var nbt = {
    // Cave spiders are a different entity, but you can use regular spider with custom attributes
    CustomName: "Cave Crawler"
};
mods.wizardryutils.MinionSpells.create("cave_spider", "minecraft:cave_spider", null, null, nbt);
```

### Slimes & Magma Cubes

```zenscript
// Tiny slime
var nbt = {
    Size: 0  // 0=tiny, 1=small, 2=medium, 3=large
};
mods.wizardryutils.MinionSpells.create("tiny_slime", "minecraft:slime", null, null, nbt);

// Huge magma cube
var nbt = {
    Size: 3
};
mods.wizardryutils.MinionSpells.create("huge_magma", "minecraft:magma_cube", null, null, nbt);
```

### Wolves

```zenscript
// Angry wolf
var nbt = {
    Angry: 1 as byte,
    CustomName: "Feral Wolf"
};
mods.wizardryutils.MinionSpells.create("feral_wolf", "minecraft:wolf", null, null, nbt);

// Tamed wolf with collar color
var nbt = {
    Owner: "",  // Empty owner means it follows caster
    CollarColor: 14 as byte  // 14 = Red (see color values below)
};
mods.wizardryutils.MinionSpells.create("tamed_wolf", "minecraft:wolf", null, null, nbt);
```

**Collar Colors:**
- 0: White
- 1: Orange
- 2: Magenta
- 3: Light Blue
- 4: Yellow
- 5: Lime
- 6: Pink
- 7: Gray
- 8: Light Gray
- 9: Cyan
- 10: Purple
- 11: Blue
- 12: Brown
- 13: Green
- 14: Red
- 15: Black

---

## Advanced Combinations

### Elite Warrior with Full Customization

Combining NBT, potions, and attributes:

```zenscript
import mods.wizardryutils.MinionSpells.PotionEffectData;
import mods.wizardryutils.MinionSpells.AttributeModifierData;

// NBT for equipment and appearance
var nbt = {
    CustomName: "§6§lElite Guardian",
    CustomNameVisible: 1 as byte,
    HandItems: [
        {
            id: "minecraft:diamond_sword",
            Count: 1 as byte,
            tag: {
                ench: [
                    {id: 16 as short, lvl: 5 as short},  // Sharpness V
                    {id: 20 as short, lvl: 2 as short}   // Fire Aspect II
                ]
            }
        },
        {}
    ],
    ArmorItems: [
        {id: "minecraft:diamond_boots", Count: 1 as byte, tag: {ench: [{id: 0 as short, lvl: 4 as short}]}},
        {id: "minecraft:diamond_leggings", Count: 1 as byte, tag: {ench: [{id: 0 as short, lvl: 4 as short}]}},
        {id: "minecraft:diamond_chestplate", Count: 1 as byte, tag: {ench: [{id: 0 as short, lvl: 4 as short}]}},
        {id: "minecraft:diamond_helmet", Count: 1 as byte, tag: {ench: [{id: 0 as short, lvl: 4 as short}]}}
    ],
    HandDropChances: [0.0f, 0.0f],
    ArmorDropChances: [0.0f, 0.0f, 0.0f, 0.0f]
};

// Potion effects
var effects = [
    PotionEffectData("minecraft:strength", 2, false),
    PotionEffectData("minecraft:resistance", 1, false),
    PotionEffectData("minecraft:speed", 1, false)
] as PotionEffectData[];

// Attribute modifiers
var mods = [
    AttributeModifierData("generic.maxHealth", 60.0, 0),
    AttributeModifierData("generic.followRange", 30.0, 0),
    AttributeModifierData("generic.knockbackResistance", 0.8, 0)
] as AttributeModifierData[];

mods.wizardryutils.MinionSpells.create("elite_guardian", "minecraft:zombie_pigman", effects, mods, nbt);
```

### Baby Dragon-Like Minion

```zenscript
var nbt = {
    CustomName: "§d§lDragonling",
    CustomNameVisible: 1 as byte,
    IsBaby: 1 as byte,
    powered: 1 as byte  // Glowing aura if it's a creeper
};

var effects = [
    PotionEffectData("minecraft:fire_resistance", 0, false),
    PotionEffectData("minecraft:strength", 2, true)
] as PotionEffectData[];

var mods = [
    AttributeModifierData("generic.maxHealth", 40.0, 0),
    AttributeModifierData("generic.movementSpeed", 0.5, 2)
] as AttributeModifierData[];

mods.wizardryutils.MinionSpells.create("dragonling", "minecraft:zombie", effects, mods, nbt);
```

---

## Modded Entity Examples

### Ice and Fire Dragons

```zenscript
// Ice dragon hatchling
var nbt = {
    AgeTicks: 0,  // Baby dragon
    Variant: 0,   // Ice variant (check mod documentation for variants)
    CustomName: "Frostbite"
};
mods.wizardryutils.MinionSpells.create("ice_dragon_hatchling", "iceandfire:ice_dragon", null, null, nbt);
```

### Lycanites Mobs

```zenscript
// Custom Lycanites mob variant
var nbt = {
    Variant: 2,  // Variant ID (mod-specific)
    SpawnEventType: "uncommon"
};
mods.wizardryutils.MinionSpells.create("lycanite_minion", "lycanitesmobs:cinder", null, null, nbt);
```

### Twilight Forest

```zenscript
// Twilight Forest minion
var nbt = {
    CustomName: "Forest Guardian"
};
mods.wizardryutils.MinionSpells.create("forest_guardian", "twilightforest:skeleton_druid", null, null, nbt);
```

---

## Useful NBT Tags Reference

### Common Entity Tags

```zenscript
{
    // Naming
    CustomName: "Name",
    CustomNameVisible: 1 as byte,
    
    // Age & Size
    IsBaby: 1 as byte,
    Age: -24000,  // Negative = baby, positive = adult
    Size: 2,      // For slimes/magma cubes
    
    // Equipment
    HandItems: [{}, {}],    // [mainhand, offhand]
    ArmorItems: [{}, {}, {}, {}],  // [feet, legs, chest, head]
    HandDropChances: [0.0f, 0.0f],
    ArmorDropChances: [0.0f, 0.0f, 0.0f, 0.0f],
    
    // AI & Behavior
    NoAI: 0 as byte,
    Silent: 0 as byte,
    NoGravity: 0 as byte,
    Invulnerable: 0 as byte,
    PersistenceRequired: 1 as byte,
    
    // Visual Effects
    Glowing: 1 as byte,
    Fire: 100 as short,  // Ticks on fire
    
    // Teams
    Team: "team_name"
}
```

### Enchantment IDs

Common enchantments for equipment:

```zenscript
ench: [
    {id: 0 as short, lvl: 4 as short},   // Protection IV
    {id: 16 as short, lvl: 5 as short},  // Sharpness V
    {id: 20 as short, lvl: 2 as short},  // Fire Aspect II
    {id: 21 as short, lvl: 2 as short},  // Looting II
    {id: 34 as short, lvl: 3 as short},  // Unbreaking III
    {id: 48 as short, lvl: 5 as short},  // Power V
    {id: 49 as short, lvl: 2 as short},  // Punch II
    {id: 50 as short, lvl: 1 as short},  // Flame I
    {id: 51 as short, lvl: 1 as short}   // Infinity I
]
```

---

## Tips & Best Practices

### NBT Best Practices

1. **Test in creative first** - Use `/summon` to test NBT before creating spells
2. **Check mod documentation** - Modded entities have custom NBT tags
3. **Use F3+H** - Shows detailed item/entity NBT in tooltips
4. **NBT applied first** - NBT is applied before potions/attributes, so they can override NBT values
5. **Drop chances to 0** - Prevent equipment from dropping (already done by default)

### Common Pitfalls

❌ **Wrong byte types**
```zenscript
IsBaby: 1  // Wrong! Will cause issues
```
✅ **Correct:**
```zenscript
IsBaby: 1 as byte  // Correct
```

❌ **Missing Count in items**
```zenscript
{id: "minecraft:diamond_sword"}  // Missing Count
```
✅ **Correct:**
```zenscript
{id: "minecraft:diamond_sword", Count: 1 as byte}
```

### Testing NBT

Use `/summon` command to test NBT structure:

```
/summon minecraft:zombie ~ ~ ~ {IsBaby:1b,CustomName:"Test"}
```

Then copy the working NBT to your ZenScript.

---

## Advanced: Dynamic NBT

For even more advanced usage, you could potentially use CraftTweaker's data manipulation:

```zenscript
// Build NBT programmatically
var baseNBT = {
    CustomName: "Generated Minion",
    IsBaby: 1 as byte
};

// Add equipment dynamically
var equipment = {
    HandItems: [
        {id: "minecraft:diamond_sword", Count: 1 as byte},
        {}
    ]
};

// Merge (in actual use, you'd pass the final combined NBT)
mods.wizardryutils.MinionSpells.create("dynamic_minion", "minecraft:zombie", null, null, baseNBT);
```

---

## Troubleshooting

**"Entity spawns but NBT not applied"**
- Check NBT syntax (commas, colons, braces)
- Verify data types (byte, short, float, etc.)
- Test NBT with `/summon` command first

**"Entity spawns with wrong equipment"**
- Ensure `Count: 1 as byte` is present
- Check item registry name is correct
- Verify drop chances are set

**"Mod-specific NBT not working"**
- Consult mod documentation
- Use `/data get entity @e[type=modid:entity,limit=1]` to see actual NBT structure
- Some mods may override NBT on spawn

---

## Finding NBT for Modded Mobs

1. **Spawn the entity** in creative mode
2. **Use command**: `/data get entity @e[type=modid:entity_name,limit=1,sort=nearest]`
3. **Copy relevant NBT** tags
4. **Adapt to ZenScript** format (remove quotes from numbers, add `as byte`/`as short` etc.)

Example:
```
Minecraft: {IsBaby:1b}
ZenScript: IsBaby: 1 as byte
```

---

## Complete Examples Index

- **Baby mobs**: Baby zombies, slimes with custom sizes
- **Named minions**: Custom names with formatting
- **Equipped warriors**: Armed zombies with enchanted gear
- **Mob variants**: Wither skeletons, charged creepers, zombie villagers
- **Elite combinations**: Full customization with NBT + effects + attributes
- **Modded entities**: Ice and Fire, Lycanites, Twilight Forest

All examples are copy-paste ready for CraftTweaker scripts! 🎉

