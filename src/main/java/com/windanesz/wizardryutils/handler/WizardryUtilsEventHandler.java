package com.windanesz.wizardryutils.handler;

import com.windanesz.wizardryutils.integration.baubles.BaublesIntegration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class WizardryUtilsEventHandler {

	private WizardryUtilsEventHandler() {} // no instances

	@SubscribeEvent
	public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {

		// tick artefacts
		if (event.phase == TickEvent.Phase.END) {
			EntityPlayer player = event.player;
			if (BaublesIntegration.enabled()) {
				BaublesIntegration.tickWornArtefacts(player);
			}
		}
	}
}
