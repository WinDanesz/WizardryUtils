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
        public final String itemName;

        public ConjurationSpellEntry(String name, String itemName) {
            this.name = name;
            this.itemName = itemName;
        }
    }

    public static final List<ConjurationSpellEntry> entries = new ArrayList<>();

    @ZenMethod
    // ContentTweaker calls this too early, even before Wizardry's spells are instantiated.
    // Stashing entries avoids network ID shifts from parent constructors.
    public static void create(String name, String itemName) {
        entries.add(new ConjurationSpellEntry(name, itemName));
    }

    public static Spell instantiate(ConjurationSpellEntry entry) {
        if (entry == null) {
            throw new RuntimeException("ConjurationSpellEntry cannot be null!");
        }

        String name = entry.name;
        String itemName = entry.itemName;

        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
        if (item == null) {
            throw new RuntimeException("Could not find item " + itemName + "!");
        }

        SpellDynamicConjuration spell = new SpellDynamicConjuration(
                "contenttweaker",
                name,
                item
        );

        return spell;
    }
}
