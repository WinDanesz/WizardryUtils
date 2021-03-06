package com.windanesz.wizardryutils.item;

import com.windanesz.wizardryutils.handler.WizardryUtilsEventHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Interface for artefacts that needs to be ticked while they are in a Baubles slot.
 */
public interface ITickableArtefact {

	/**
	 * Called each tick the itemstack is in the baubles inventory.
	 * Called by {@link WizardryUtilsEventHandler#onPlayerTickEvent(net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent)}.
	 * Note that the item won't be ticked in any other inventory slots from this method!
	 * @param itemstack the itemstack of the (artefact) item
	 * @param player the player wearing this itemstack in their baubles inventory
	 */
	void onWornTick(ItemStack itemstack, EntityLivingBase player);

}