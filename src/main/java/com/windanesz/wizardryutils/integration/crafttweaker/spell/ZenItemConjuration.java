package com.windanesz.wizardryutils.integration.crafttweaker.spell;

import com.windanesz.wizardryutils.spell.SpellDynamicConjuration;
import crafttweaker.annotations.ZenRegister;
import electroblob.wizardry.spell.Spell;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.wizardryutils.ConjurationSpells")
@ZenRegister
public class ZenItemConjuration {

    public static final class ConjurationSpellEntry {
        public final String name;
        public final String itemRegistryName;

        public ConjurationSpellEntry(String name, String itemRegistryName) {
            this.name = name;
            this.itemRegistryName = itemRegistryName;
        }
    }

    public static final List<ConjurationSpellEntry> entries = new ArrayList<>();

    /**
     * Creates a new conjuration spell that summons the specified item.
     * <p>
     * Takes a registry name for the item. The item is looked up from the Forge registry at spell cast time,
     * not during registration, to avoid early registration timing issues.
     * <p>
     * Example usage in CraftTweaker:
     * <pre>
     * // For vanilla items
     * mods.wizardryutils.ConjurationSpells.create("iron_sword_spell", "minecraft:iron_sword");
     * 
     * // For modded items
     * mods.wizardryutils.ConjurationSpells.create("custom_item_spell", "modid:custom_item");
     * </pre>
     * 
     * @param name The name of the spell (will be prefixed with the mod ID)
     * @param itemRegistryName The registry name of the item to conjure (e.g., "minecraft:diamond_sword")
     */
    @ZenMethod
    public static void create(String name, String itemRegistryName) {
        entries.add(new ConjurationSpellEntry(name, itemRegistryName));
    }

    public static Spell instantiate(ConjurationSpellEntry entry) {
        if (entry == null) {
            throw new RuntimeException("ConjurationSpellEntry cannot be null!");
        }

        String name = entry.name;
        String itemRegistryName = entry.itemRegistryName;

        SpellDynamicConjuration spell = new SpellDynamicConjuration(
                "contenttweaker",
                name,
                () -> {
                    // Look up the item from the registry at cast time
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemRegistryName));
                    if (item == null) {
                        throw new RuntimeException("Could not find item " + itemRegistryName + " in registry!");
                    }
                    return item;
                }
        );

        return spell;
    }
}
