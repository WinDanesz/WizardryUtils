package com.windanesz.wizardryutils;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static electroblob.wizardry.Settings.ARTEFACTS_CATEGORY;

@Config(modid = WizardryUtils.MODID, name = "WizardryUtils") // No fancy configs here so we can use the annotation, hurrah!
public class Settings {

	/**
	 * Helper method to figure out if an item was disabled in the ebwiz configs, as unfortunately temArtefact#enabled private and has no getter method
	 *
	 * @param artefact to check
	 * @return true if the item is enabled (or if it has no config)
	 */
	public static boolean isArtefactEnabled(Item artefact) {
		if (artefact instanceof ItemArtefact &&
				(Wizardry.settings.getConfigCategory(ARTEFACTS_CATEGORY).containsKey(artefact.getRegistryName().toString()))) {
			return (Wizardry.settings.getConfigCategory(ARTEFACTS_CATEGORY).get(artefact.getRegistryName().toString()).getBoolean());
		}

		// no setting to control this item so it shouldn't be disabled..
		return true;
	}

	@SuppressWarnings("unused")
	@Mod.EventBusSubscriber(modid = WizardryUtils.MODID)
	private static class EventHandler {
		/**
		 * Inject the new values and save to the config file when the config has been changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(WizardryUtils.MODID)) {
				ConfigManager.sync(WizardryUtils.MODID, Config.Type.INSTANCE);
			}
		}
	}

	@Config.Name("General Settings")
	@Config.LangKey("settings.wizardryutils:general_settings")
	public static GeneralSettings generalSettings = new GeneralSettings();

	public static class GeneralSettings {

		@Config.Name("Baubles Integration")
		@Config.Comment("Enable/Disable Baubles integration for the new artefact types (belt, helm, etc). This does NOT affect Electroblob's Wizardry's own Baubles support implementation (ring, amulet, charm)!")
		@Config.RequiresMcRestart
		public boolean baubles_integration = true;
	}
}
