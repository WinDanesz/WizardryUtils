
package com.windanesz.wizardryutils.mixin;

import com.windanesz.wizardryutils.integration.crafttweaker.imbuement_altar.ImbuementAltarRecipeRegistry;
import electroblob.wizardry.integration.jei.ImbuementAltarRecipeCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.stream.Collectors;

@Mixin(value = ImbuementAltarRecipeCategory.class, remap = false)
public class ImbuementAltarRecipeCategoryMixin {

    @Inject(method = "generateRecipes", at = @At("RETURN"), cancellable = true)
    private static void generateRecipes(CallbackInfoReturnable<Collection<electroblob.wizardry.integration.jei.ImbuementAltarRecipe>> cir) {
        Collection<electroblob.wizardry.integration.jei.ImbuementAltarRecipe> recipes = cir.getReturnValue();

        // Electroblob's JEI ImbuementAltarRecipe expects a List<List<ItemStack>> for the elements parameter.
        // Our CraftTweaker-facing recipe stores Element[]; for compile-time compatibility provide an empty list placeholder.
        java.util.List toAdd = ImbuementAltarRecipeRegistry.getRecipes().stream().map(recipe -> {
            return new electroblob.wizardry.integration.jei.ImbuementAltarRecipe(
                    recipe.getInput(),
                    java.util.Collections.emptyList(), // placeholder - elements rendered differently in JEI
                    recipe.getOutput()
            );
        }).collect(Collectors.toList());

        recipes.addAll((java.util.Collection) toAdd);
        cir.setReturnValue(recipes);
    }
}
