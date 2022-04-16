package com.windanesz.wizardryutils;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = WizardryUtils.MODID, name = "WizardryUtils") // No fancy configs here so we can use the annotation, hurrah!
public class Settings {

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

		@Config.Name("Inject Artefacts To Loot Tables")
		@Config.Comment("Enable/Disable the functionality to automatically inject spell pack artefacts managed by WizardryUtils into the Wizardry Shrine loot tables."
				+ "\nDisabling this will cause that no spell pack which depends on WizardryUtils will have its artefacts appearing in Shrine chests!")
		@Config.RequiresMcRestart
		public boolean auto_inject_artefacts_to_shrines = true;
	}
}
