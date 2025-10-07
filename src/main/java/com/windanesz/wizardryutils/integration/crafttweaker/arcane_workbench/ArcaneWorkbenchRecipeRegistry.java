//
//package com.windanesz.wizardryutils.integration.crafttweaker.arcane_workbench;
//
//import net.minecraft.item.ItemStack;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ArcaneWorkbenchRecipeRegistry {
//
//    private static final List<ArcaneWorkbenchRecipe> recipes = new ArrayList<>();
//
//    public static void addRecipe(ArcaneWorkbenchRecipe recipe) {
//        recipes.add(recipe);
//    }
//
//    public static ArcaneWorkbenchRecipe findMatchingRecipe(ItemStack center, ItemStack crystal, ItemStack upgrade, List<ItemStack> spellbooks) {
//        for (ArcaneWorkbenchRecipe recipe : recipes) {
//            if (recipe.matches(center, crystal, upgrade, spellbooks)) {
//                return recipe;
//            }
//        }
//        return null;
//    }
//
//    public static List<ArcaneWorkbenchRecipe> getRecipes() {
//        return recipes;
//    }
//
//    public static boolean isItemUsedInAnyRecipe(ItemStack item) {
//        if (item == null) return false;
//        for (ArcaneWorkbenchRecipe recipe : recipes) {
//            ItemStack center = recipe.getCenterInput();
//            if (center != null && center.getItem() == item.getItem()) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
