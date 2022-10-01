package com.windanesz.wizardryutils.integration.crafttweaker;

import com.windanesz.wizardryutils.integration.crafttweaker.events.ZenSpellCastEventPost;
import com.windanesz.wizardryutils.integration.crafttweaker.events.ZenSpellCastEventPre;
import electroblob.wizardry.event.SpellCastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CrTCommonEventHandler {

	@SubscribeEvent
	public void onSpellCastEventPre(SpellCastEvent.Pre ev) {
		if (CrTImplementationAPI.events.hasSpellCastEventPre()) {
			CrTImplementationAPI.events.publishSpellCastEventPre(new ZenSpellCastEventPre(ev));
		}
	}

	@SubscribeEvent
	public void onSpellCastEventPost(SpellCastEvent.Post ev) {
		if (CrTImplementationAPI.events.hasSpellCastEventPost()) {
			CrTImplementationAPI.events.publishSpellCastEventPost(new ZenSpellCastEventPost(ev));
		}
	}
}
