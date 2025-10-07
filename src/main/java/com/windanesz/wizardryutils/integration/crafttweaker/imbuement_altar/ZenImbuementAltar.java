//
//package com.windanesz.wizardryutils.integration.crafttweaker.imbuement_altar;
//
//import crafttweaker.api.item.IItemStack;
//import crafttweaker.api.minecraft.CraftTweakerMC;
//import crafttweaker.annotations.ZenRegister;
//import electroblob.wizardry.constants.Element;
//import stanhebben.zenscript.annotations.ZenClass;
//import stanhebben.zenscript.annotations.ZenMethod;
//
//import java.util.Arrays;
//
//@ZenClass("mods.wizardryutils.ImbuementAltar")
//@ZenRegister
//public class ZenImbuementAltar {
//
//    @ZenMethod
//    public static void addRecipe(IItemStack output, IItemStack input, String[] elements) {
//        ImbuementAltarRecipeRegistry.addRecipe(new ImbuementAltarRecipe(
//                CraftTweakerMC.getItemStack(input),
//                Arrays.stream(elements).map(Element::valueOf).toArray(Element[]::new),
//                CraftTweakerMC.getItemStack(output)
//        ));
//    }
//}
