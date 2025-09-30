
package com.windanesz.wizardryutils.integration.crafttweaker.arcane_workbench;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.stream.Collectors;

@ZenClass("mods.wizardryutils.ArcaneWorkbench")
@ZenRegister
public class ZenArcaneWorkbench {

    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack center, IItemStack crystal, IItemStack upgrade, IItemStack[] spellbooks) {
        ArcaneWorkbenchRecipeRegistry.addRecipe(new ArcaneWorkbenchRecipe(
                CraftTweakerMC.getItemStack(center),
                CraftTweakerMC.getItemStack(crystal),
                CraftTweakerMC.getItemStack(upgrade),
                Arrays.stream(spellbooks).map(CraftTweakerMC::getItemStack).collect(Collectors.toList()),
                CraftTweakerMC.getItemStack(output)
        ));
    }
}
