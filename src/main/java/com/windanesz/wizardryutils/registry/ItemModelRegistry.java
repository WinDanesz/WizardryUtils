package com.windanesz.wizardryutils.registry;

import com.windanesz.wizardryutils.WizardryUtils;
import electroblob.wizardry.item.IMultiTexturedItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(Side.CLIENT)
public final class ItemModelRegistry {
	private ItemModelRegistry() { // no instances
	}

	private static List<String> ADDON_MODID_REGISTRY = new ArrayList<>();

	/**
	 * Call this method with your modid once to let WizardryUtils handle the registry of item models for the mod.
	 * @param modid the modid of the mod being registered
	 */
	public static void registerModForAutoItemModelRegistry(String modid) {
		if (!ADDON_MODID_REGISTRY.contains(modid)) {
			WizardryUtils.logger.info("Registered modid " + modid + " for automatic item model registry");
			ADDON_MODID_REGISTRY.add(modid);
		}
	}

	@SubscribeEvent
	public static void register(ModelRegistryEvent event) {

		// Automatic item model registry for each mods
		for(Item item : Item.REGISTRY){
			if(ADDON_MODID_REGISTRY.contains(item.getRegistryName().getNamespace())){
				registerItemModel(item); // Standard item model
			}
		}

	}

	// below registry methods are courtesy of EB
	public static void registerItemModel(Item item) {
		ModelBakery.registerItemVariants(item, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(item, s -> new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	private static <T extends Item & IMultiTexturedItem> void registerMultiTexturedModel(T item) {

		if (item.getHasSubtypes()) {
			NonNullList<ItemStack> items = NonNullList.create();
			item.getSubItems(item.getCreativeTab(), items);
			for (ItemStack stack : items) {
				ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(),
						new ModelResourceLocation(item.getModelName(stack), "inventory"));
			}
		}
	}

	private static void registerItemModel(Item item, int metadata, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, metadata,
				new ModelResourceLocation(item.getRegistryName(), variant));
	}

}

