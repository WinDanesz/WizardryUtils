package com.windanesz.wizardryutils.integration.crafttweaker;

import com.windanesz.wizardryutils.integration.crafttweaker.api.IEventManager;
import com.windanesz.wizardryutils.integration.crafttweaker.events.ZenSpellCastEventPost;
import com.windanesz.wizardryutils.integration.crafttweaker.events.ZenSpellCastEventPre;
import crafttweaker.api.event.IEventHandle;
import crafttweaker.util.EventList;
import crafttweaker.util.IEventHandler;

public class CrTEventManager implements IEventManager {

	private final EventList<ZenSpellCastEventPre> spellCastEventPreEventList = new EventList<>();
	private final EventList<ZenSpellCastEventPost> spellCastEventPostEventList = new EventList<>();

	@Override
	public void clear() {
		spellCastEventPreEventList.clear();
		spellCastEventPostEventList.clear();
	}

	////// ---- SpellCastEvent.Pre ---- //////

	@Override
	public IEventHandle onSpellCastEventPre(IEventHandler<ZenSpellCastEventPre> ev) {
		return spellCastEventPreEventList.add(ev);
	}

	public boolean hasSpellCastEventPre() {
		return spellCastEventPreEventList.hasHandlers();
	}

	public void publishSpellCastEventPre(ZenSpellCastEventPre event) {
		spellCastEventPreEventList.publish(event);
	}

	////// ---- SpellCastEvent.Post ---- //////

	@Override
	public IEventHandle onSpellCastEventPost(IEventHandler<ZenSpellCastEventPost> ev) {
		return spellCastEventPostEventList.add(ev);
	}

	public boolean hasSpellCastEventPost() {
		return spellCastEventPostEventList.hasHandlers();
	}

	public void publishSpellCastEventPost(ZenSpellCastEventPost event) {
		spellCastEventPostEventList.publish(event);
	}
}
