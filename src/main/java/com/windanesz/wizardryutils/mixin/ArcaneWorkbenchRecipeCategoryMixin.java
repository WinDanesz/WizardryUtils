
package com.windanesz.wizardryutils.mixin;

import com.windanesz.wizardryutils.integration.crafttweaker.arcane_workbench.ArcaneWorkbenchRecipeRegistry;
import electroblob.wizardry.integration.jei.ArcaneWorkbenchRecipeCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Mixin(value = ArcaneWorkbenchRecipeCategory.class, remap = false)
public class ArcaneWorkbenchRecipeCategoryMixin {

    @Inject(method = "generateRecipes", at = @At("RETURN"), cancellable = true)
    private static void generateRecipes(CallbackInfoReturnable<Collection<electroblob.wizardry.integration.jei.ArcaneWorkbenchRecipe>> cir) {
        Collection<electroblob.wizardry.integration.jei.ArcaneWorkbenchRecipe> recipes = cir.getReturnValue();

        recipes.addAll(ArcaneWorkbenchRecipeRegistry.getRecipes().stream().map(recipe -> {
            return new electroblob.wizardry.integration.jei.ArcaneWorkbenchRecipe(
                    recipe.getCenterInput(),
                    recipe.getSpellbookInputs(),
                    Collections.singletonList(recipe.getCrystalInput()),
                    Collections.singletonList(recipe.getUpgradeInput()),
                    recipe.getOutput()
            );
        }).collect(Collectors.toList()));

        cir.setReturnValue(recipes);
    }
}
