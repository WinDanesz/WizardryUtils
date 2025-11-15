# NBT Data Quick Fix

## The Issue

When using NBT data with minion spells, you MUST cast the NBT object to `IData`:

### ❌ **WRONG** (Will cause "Bad type on operand stack" error):
```zenscript
var nbt = {
    IsBaby: 1 as byte
};
mods.wizardryutils.MinionSpells.createWithNBT("baby_zombie", "minecraft:zombie", null, null, nbt);
```

### ✅ **CORRECT**:
```zenscript
var nbt = {
    IsBaby: 1 as byte
} as IData;  // Add "as IData" here!

mods.wizardryutils.MinionSpells.createWithNBT("baby_zombie", "minecraft:zombie", null, null, nbt);
```

## Why?

CraftTweaker's ZenScript needs explicit type information to convert map literals to `IData`. Without the cast, it treats it as a generic map which cannot be passed to the Java method.

## All NBT Examples Require This

**Every NBT example** in the documentation needs `as IData`:

```zenscript
// Equipment example
var nbt = {
    HandItems: [
        {id: "minecraft:diamond_sword", Count: 1 as byte},
        {}
    ]
} as IData;  // Don't forget this!

// Custom name example  
var nbt = {
    CustomName: "Elite Warrior",
    CustomNameVisible: 1 as byte
} as IData;  // Always needed!

// Complex example
var nbt = {
    IsBaby: 1 as byte,
    CustomName: "Baby Warrior",
    HandItems: [...],
    ArmorItems: [...]
} as IData;  // Still required!
```

## Remember

**Always add `as IData` after your NBT object definition!**
