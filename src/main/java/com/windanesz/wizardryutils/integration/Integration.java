package com.windanesz.wizardryutils.integration;

import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.Spell;
import net.minecraft.item.Item;

import java.util.LinkedHashMap;
import java.util.List;

public abstract class Integration {

	private static final LinkedHashMap<String, Integration> INTEGRATIONS = new LinkedHashMap<>();

	/**
	 * Called during PostInitialization, disables spells and artefacts for supported mods which are not present
	 */
	public static void setDisables() {
		getRegistry().forEach((modId, integration) -> {
			if (!integration.isEnabled()) {
				// disable the spells
				integration.getSpells().forEach(spell -> spell.setEnabled(false));
				// disable artefacts
				integration.getArtefacts().forEach(item -> ((ItemArtefact) item).setEnabled(false));
			}
		});
	}

	public static void register(String modid, Integration instance) {
		INTEGRATIONS.put(modid, instance);
	}

	public static LinkedHashMap<String, Integration> getRegistry() {
		return INTEGRATIONS;
	}

	public abstract void init();

	public abstract String getModid();

	public abstract boolean isEnabled();

	/**
	 * List used to track which spells belong to this supported mod. Used for spell disabling in postInit in {@link Integration#setDisables()}.
	 * @return The list of this integration's spells.
	 */
	public abstract List<Spell> getSpells();

	/**
	 * Adds a spell to this integration's list of spells.
	 * @param spell spell to add.
	 * @return the passed in spell for method chaining.
	 */
	public abstract Spell addSpell(Spell spell);

	/**
	 * List used for loot injection in {@link SBLoot#onLootTableLoadEvent(net.minecraftforge.event.LootTableLoadEvent)} and to disable the artefact in
	 * {@link Integration#setDisables()} if the supported mod is not present.
	 *
	 * @return list of all registered ItemArtefacts for this supported mod
	 */
	public abstract List<Item> getArtefacts();

	public abstract void addArtefact(Item item);

	public abstract String getMissingSpellDesc();
}
