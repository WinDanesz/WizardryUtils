
package com.windanesz.wizardryutils.mixin;

import com.windanesz.wizardryutils.integration.crafttweaker.arcane_workbench.ArcaneWorkbenchRecipe;
import com.windanesz.wizardryutils.integration.crafttweaker.arcane_workbench.ArcaneWorkbenchRecipeRegistry;
import electroblob.wizardry.inventory.ContainerArcaneWorkbench;
import electroblob.wizardry.item.IWorkbenchItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mixin(value = ContainerArcaneWorkbench.class, remap = false)
public class ContainerArcaneWorkbenchMixin {

    @Inject(method = "onApplyButtonPressed", at = @At("HEAD"), cancellable = true)
    private void onApplyButtonPressed(EntityPlayer player, CallbackInfo ci) {
        ContainerArcaneWorkbench container = (ContainerArcaneWorkbench) (Object) this;
        Slot centerSlot = container.getSlot(ContainerArcaneWorkbench.CENTRE_SLOT);

        if (!(centerSlot.getStack().getItem() instanceof IWorkbenchItem)) {
            Slot crystalSlot = container.getSlot(ContainerArcaneWorkbench.CRYSTAL_SLOT);
            Slot upgradeSlot = container.getSlot(ContainerArcaneWorkbench.UPGRADE_SLOT);
            Slot[] spellBookSlots = new Slot[8];
            for (int i = 0; i < 8; i++) {
                spellBookSlots[i] = container.getSlot(i);
            }

            ArcaneWorkbenchRecipe recipe = ArcaneWorkbenchRecipeRegistry.findMatchingRecipe(
                    centerSlot.getStack(),
                    crystalSlot.getStack(),
                    upgradeSlot.getStack(),
                    Arrays.stream(spellBookSlots).map(Slot::getStack).collect(Collectors.toList())
            );

            if (recipe != null) {
                centerSlot.putStack(recipe.getOutput());
                crystalSlot.decrStackSize(1);
                upgradeSlot.decrStackSize(1);
                for (Slot spellBook : spellBookSlots) {
                    spellBook.decrStackSize(1);
                }
                ci.cancel();
            }
        }
    }
}
