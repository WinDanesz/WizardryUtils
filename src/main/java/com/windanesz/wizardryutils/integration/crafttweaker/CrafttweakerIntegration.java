package com.windanesz.wizardryutils.integration.crafttweaker;

import com.windanesz.wizardryutils.Settings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

public class CrafttweakerIntegration {

	public static final String CRAFTTWEAKER_MOD_ID = "crafttweaker";

	private static boolean crafttweakerLoaded;

	public static void init() {
		crafttweakerLoaded = Loader.isModLoaded(CRAFTTWEAKER_MOD_ID);
		if (!enabled()) { return; }

		registerEvents();
	}

	public static void registerEvents() {
		MinecraftForge.EVENT_BUS.register(new CrTCommonEventHandler());
	}

	public static boolean enabled() {
		return Settings.generalSettings.baubles_integration && crafttweakerLoaded;
	}
}
