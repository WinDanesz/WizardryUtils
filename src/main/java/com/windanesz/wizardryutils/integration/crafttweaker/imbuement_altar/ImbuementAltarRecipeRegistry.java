//
//package com.windanesz.wizardryutils.integration.crafttweaker.imbuement_altar;
//
//import electroblob.wizardry.constants.Element;
//import net.minecraft.item.ItemStack;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ImbuementAltarRecipeRegistry {
//
//    private static final List<ImbuementAltarRecipe> recipes = new ArrayList<>();
//
//    public static void addRecipe(ImbuementAltarRecipe recipe) {
//        recipes.add(recipe);
//    }
//
//    public static ImbuementAltarRecipe findMatchingRecipe(ItemStack input, Element[] elements) {
//        for (ImbuementAltarRecipe recipe : recipes) {
//            if (recipe.matches(input, elements)) {
//                return recipe;
//            }
//        }
//        return null;
//    }
//
//    public static List<ImbuementAltarRecipe> getRecipes() {
//        return recipes;
//    }
//}
