package com.windanesz.wizardryutils;


import com.windanesz.wizardryutils.capability.SummonedCreatureData;
import com.windanesz.wizardryutils.integration.Integration;
import com.windanesz.wizardryutils.integration.baubles.BaublesIntegration;
import com.windanesz.wizardryutils.network.SBPacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
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

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		proxy.registerRenderers();

		BaublesIntegration.init();

		// Capabilities
		SummonedCreatureData.register();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(instance);
		proxy.registerParticles();
		proxy.init();
		SBPacketHandler.initPackets();
		//NetworkRegistry.INSTANCE.registerGuiHandler(this, new SBGuiHandler());

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Integration.setDisables();
	}

	@EventHandler
	public void serverStartup(FMLServerStartingEvent event) { }
}
