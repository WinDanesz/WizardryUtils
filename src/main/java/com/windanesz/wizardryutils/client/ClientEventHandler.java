package com.windanesz.wizardryutils.client;

import com.windanesz.wizardryutils.capability.SummonedItemCapability;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Mod.EventBusSubscriber
public class ClientEventHandler {

	@SubscribeEvent
	public static void tooltipEvent(ItemTooltipEvent event) {
		if (event.getEntityPlayer() != null && !event.getItemStack().isEmpty() && SummonedItemCapability.isSummonedItem(event.getItemStack())) {
			int lifetimeSeconds = (int) (SummonedItemCapability.get(event.getItemStack()).getLifetime() / 20);
			// adding to the first index, pushing the list
			event.getToolTip().add(1, TextFormatting.LIGHT_PURPLE + I18n.format("name.wizardryutils:bound_item", lifetimeSeconds));
		}
	}
}
