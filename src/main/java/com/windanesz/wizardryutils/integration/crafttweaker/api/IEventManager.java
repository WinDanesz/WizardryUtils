package com.windanesz.wizardryutils.integration.crafttweaker.api;

import com.windanesz.wizardryutils.integration.crafttweaker.CrTImplementationAPI;
import com.windanesz.wizardryutils.integration.crafttweaker.events.ZenSpellCastEventPost;
import com.windanesz.wizardryutils.integration.crafttweaker.events.ZenSpellCastEventPre;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.event.IEventHandle;
import crafttweaker.util.IEventHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * author WinDanesz
 */
@ZenClass("mods.wizardryutils.events.IEventManager")
@ZenRegister
public interface IEventManager {

	@ZenMethod
	static IEventManager getEvents() {
		return CrTImplementationAPI.events;
	}

	@ZenMethod
	void clear();

	@ZenMethod
	IEventHandle onSpellCastEventPre(IEventHandler<ZenSpellCastEventPre> ev);

	@ZenMethod
	IEventHandle onSpellCastEventPost(IEventHandler<ZenSpellCastEventPost> ev);
}
