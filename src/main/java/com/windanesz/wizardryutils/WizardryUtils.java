package com.windanesz.wizardryutils;

import com.windanesz.wizardryutils.capability.SummonedCreatureData;
import com.windanesz.wizardryutils.capability.SummonedItemCapability;
import com.windanesz.wizardryutils.integration.baubles.BaublesIntegration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(modid = WizardryUtils.MODID, name = WizardryUtils.NAME, version = "@VERSION@", acceptedMinecraftVersions = "[@MCVERSION@]",
		dependencies = "required-after:ebwizardry@[@WIZARDRY_VERSION@,4.4)")
public class WizardryUtils {

	public static final String MODID = "wizardryutils";
	public static final String NAME = "WizardryUtils";

	public static final Random rand = new Random();

	public static Logger logger;

	// The instance of wizardry that Forge uses.
	@Mod.Instance(WizardryUtils.MODID)
	public static WizardryUtils instance;

	// Location of the proxy code, used by Forge.
	@SidedProxy(clientSide = "com.windanesz.wizardryutils.client.ClientProxy", serverSide = "com.windanesz.wizardryutils.CommonProxy")
	public static CommonProxy proxy;

	/** Static instance of the {@link ArtefactSettings} object for WizardryUtils. */
	public static final ArtefactSettings artefactSettings = new ArtefactSettings();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		proxy.registerRenderers();

		BaublesIntegration.init();

		// Capabilities
		SummonedCreatureData.register();
		SummonedItemCapability.register();

		artefactSettings.initConfig(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		artefactSettings.initConfigExtras();

		MinecraftForge.EVENT_BUS.register(instance);
		proxy.registerParticles();
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) { }

	@EventHandler
	public void serverStartup(FMLServerStartingEvent event) { }

	@SubscribeEvent
	public void onArtefactConfigChanged(net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent event){
		if(event.getModID().equals(WizardryUtils.MODID)){
			artefactSettings.saveConfigChanges();
		}
	}
}
