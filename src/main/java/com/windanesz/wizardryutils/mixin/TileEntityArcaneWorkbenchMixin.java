//package com.windanesz.wizardryutils.mixin;
//
//import com.windanesz.wizardryutils.integration.crafttweaker.arcane_workbench.ArcaneWorkbenchRecipeRegistry;
//import electroblob.wizardry.inventory.ContainerArcaneWorkbench;
//import electroblob.wizardry.item.IWorkbenchItem;
//import electroblob.wizardry.tileentity.TileEntityArcaneWorkbench;
//import net.minecraft.item.ItemStack;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Mixin(value = TileEntityArcaneWorkbench.class, remap = false)
//public class TileEntityArcaneWorkbenchMixin {
//
//	@Inject(method = "isItemValidForSlot", at = @At("HEAD"), cancellable = true)
//	private void isItemValidForSlot(int slotNumber, ItemStack itemstack, CallbackInfoReturnable<Boolean> cir) {
//		if (slotNumber == ContainerArcaneWorkbench.CENTRE_SLOT) {
//			if (!(itemstack.getItem() instanceof IWorkbenchItem)) {
//				if (ArcaneWorkbenchRecipeRegistry.isItemUsedInAnyRecipe(itemstack)) {
//					cir.setReturnValue(true);
//				}
//			}
//		}
//	}
//}
