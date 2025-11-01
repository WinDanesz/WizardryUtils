# SpellDynamicAreaEffect - Usage Guide

## Overview

This implementation provides a generic Area of Effect (AoE) spell system for Electroblob's Wizardry, allowing you to create custom area effect spells via ZenScript/CraftTweaker that apply potion effects to entities within a radius.

## Files Created

1. **EntityDynamicConstruct.java** - A generic construct entity that applies potion effects to nearby entities
   - Location: `src/main/java/com/windanesz/wizardryutils/entity/EntityDynamicConstruct.java`
   
2. **SpellDynamicAreaEffect.java** - The spell class that spawns the construct entity
   - Location: `src/main/java/com/windanesz/wizardryutils/spell/SpellDynamicAreaEffect.java`
   
3. **ZenAreaEffectSpell.java** - ZenScript integration for creating area effect spells
   - Location: `src/main/java/com/windanesz/wizardryutils/integration/crafttweaker/spell/ZenAreaEffectSpell.java`

## Files Modified

1. **SpellRegistryHandler.java** - Added registration for area effect spells
2. **EntityRegistry.java** - Added registration for the EntityDynamicConstruct entity

## How It Works

### Entity Behavior
- The `EntityDynamicConstruct` spawns at the target location
- Every 20 ticks (1 second) it applies potion effects to all valid entities within the radius
- It respects ally/enemy targeting based on the caster
- The construct has a configurable lifetime and automatically despawns

### Spell Properties
The spell uses these properties (configurable in spell JSON):
- `effect_radius` - Radius in which entities are affected
- `effect_duration` - Duration of applied potion effects  
- `lifetime` - How long the construct exists

### Modifiers Support
- **Duration Upgrade**: Increases both construct lifetime and potion effect duration
- **Blast Upgrade**: Increases the effect radius
- **Potency**: Increases potion effect amplifier
- **Range Upgrade**: Affects spell cast distance

## ZenScript Usage

### Creating a New Area Effect Spell

```zenscript
import mods.wizardryutils.AreaEffectSpells;

// Create a healing aura that applies regeneration
AreaEffectSpells.create(
    "healing_aura",           // Spell name
    0.3,                      // Red (0.0-1.0)
    1.0,                      // Green (0.0-1.0)
    0.3,                      // Blue (0.0-1.0)
    ["minecraft:regeneration"] // Array of potion effect IDs
);

// Create a poison cloud with multiple effects
AreaEffectSpells.create(
    "poison_cloud",
    0.2,                      // Red
    0.8,                      // Green
    0.2,                      // Blue
    [
        "minecraft:poison",
        "minecraft:weakness",
        "minecraft:slowness"
    ]
);

// Create a speed boost aura
AreaEffectSpells.create(
    "speed_aura",
    0.0,                      // Red
    0.5,                      // Green  
    1.0,                      // Blue
    [
        "minecraft:speed",
        "minecraft:jump_boost"
    ]
);
```

### Spell JSON Configuration

After creating the spell via ZenScript, you'll need to create a JSON file for it:

**File**: `config/ebwizardry/spells/contenttweaker/healing_aura.json`

```json
{
  "enabled": true,
  "tier": "apprentice",
  "element": "healing",
  "type": "defence",
  "cost": 25,
  "chargeup": 0,
  "cooldown": 100,
  "base_properties": {
    "effect_radius": 5.0,
    "effect_duration": 600,
    "lifetime": 200
  }
}
```

## Features

### Customizable Appearance
- RGB color values control particle effects
- Particles spawn around the construct perimeter

### Flexible Targeting
- Can affect allies or enemies (configurable in entity class)
- Doesn't affect the caster
- Works with Wizardry's targeting system

### Multi-Potion Support
- Apply multiple potion effects simultaneously
- Each effect respects duration and potency modifiers

### Compatible with All Cast Methods
- Player casting
- NPC/mob casting
- Dispenser casting
- Works with targeting systems

## Example Use Cases

1. **Healing Shrine**: Creates a stationary healing zone
2. **Poison Cloud**: Debuffs enemies in an area
3. **Battle Standard**: Buffs allies around a location
4. **Elemental Field**: Creates area denial zones
5. **Support Auras**: Long-duration buff zones for bases

## Technical Details

### Construct Properties
- Lifetime: Configurable, affected by duration upgrades
- Radius: Configurable, affected by blast upgrades
- Tick Interval: 20 ticks (1 second) between effect applications
- Collision: No collision (noClip = true)

### Effect Application
- Duration: 600 ticks (30 seconds) base, multiplied by duration upgrade
- Amplifier: Based on potency modifier (potency - 1)
- Hidden Particles: Effects are applied without visible particles on entities

## Notes

- The spell uses `SpellActions.POINT` for targeting
- Constructs are registered as `wizardryutils:dynamic_construct`
- Spells created via ZenScript use the `contenttweaker` mod ID
- The system follows the same pattern as other dynamic spell types (minions, projectiles, buffs)
