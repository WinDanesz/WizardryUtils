package com.windanesz.wizardryutils.registry;

import com.windanesz.wizardryutils.WizardryUtils;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

//@GameRegistry.ObjectHolder(WizardryUtils.MODID)
@Mod.EventBusSubscriber
public class BlockRegistry {

	private BlockRegistry() {} // no instances

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	public static <T> T placeholder() { return null; }

	public static void registerBlock(IForgeRegistry<Block> registry, String name, Block block) {
		block.setRegistryName(WizardryUtils.MODID, name);
		block.setTranslationKey(block.getRegistryName().toString());
		registry.register(block);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
	}

//	/** Called from the preInit method in the main mod class to register all the tile entities. */
//	public static void registerTileEntities(){
//		// TODO
//	}

}
