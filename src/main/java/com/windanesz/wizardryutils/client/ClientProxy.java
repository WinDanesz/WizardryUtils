package com.windanesz.wizardryutils.client;

import com.windanesz.wizardryutils.CommonProxy;
import com.windanesz.wizardryutils.entity.EntityDynamicConstruct;
import electroblob.wizardry.client.renderer.entity.RenderBlank;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	/**
	 * Called from preInit() in the main mod class to initialise the renderers.
	 */
	public void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityDynamicConstruct.class, RenderBlank::new);
	}
}