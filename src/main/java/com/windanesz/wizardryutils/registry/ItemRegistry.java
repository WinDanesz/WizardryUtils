package com.windanesz.wizardryutils.registry;

import com.windanesz.wizardryutils.WizardryUtils;
import com.windanesz.wizardryutils.item.ItemDustyArtefact;
import com.windanesz.wizardryutils.item.ItemDustyCasket;
import com.windanesz.wizardryutils.item.ItemDustySpellBook;
import com.windanesz.wizardryutils.item.ItemNewArtefact;
import electroblob.wizardry.registry.WizardryTabs;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@ObjectHolder(WizardryUtils.MODID)
@Mod.EventBusSubscriber
public final class ItemRegistry {

	private ItemRegistry() {} // No instances!

	public static final Item dusty_amulet = placeholder();
	public static final Item dusty_ring = placeholder();
	public static final Item dusty_charm = placeholder();
	public static final Item dusty_headgear = placeholder();
	public static final Item dusty_spell_book = placeholder();
	public static final Item dusty_casket = placeholder();

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {

		IForgeRegistry<Item> registry = event.getRegistry();

		// Test item, only added in dev env!
		if (FMLLaunchHandler.isDeobfuscatedEnvironment()) {
			WizardryUtils.logger.info("Registering debug artefact items...");
			ItemRegistry.registerItemArtefact(registry, "head_test", WizardryUtils.MODID, new ItemNewArtefact(EnumRarity.UNCOMMON, ItemNewArtefact.Type.HEAD));
			ItemRegistry.registerItemArtefact(registry, "belt_test", WizardryUtils.MODID, new ItemNewArtefact(EnumRarity.RARE, ItemNewArtefact.Type.BELT));
			ItemRegistry.registerItemArtefact(registry, "body_test", WizardryUtils.MODID, new ItemNewArtefact(EnumRarity.EPIC, ItemNewArtefact.Type.BODY));
		}

		registerItem(registry,  "dusty_amulet", WizardryUtils.MODID, new ItemDustyArtefact("amulet"));
		registerItem(registry,  "dusty_ring", WizardryUtils.MODID, new ItemDustyArtefact("ring"));
		registerItem(registry,  "dusty_charm", WizardryUtils.MODID, new ItemDustyArtefact("charm"));
		registerItem(registry,  "dusty_headgear", WizardryUtils.MODID, new ItemDustyArtefact("head"));
		registerItem(registry,  "dusty_belt", WizardryUtils.MODID, new ItemDustyArtefact("belt"));
		registerItem(registry,  "dusty_spell_book", WizardryUtils.MODID, new ItemDustySpellBook());
		registerItem(registry,  "dusty_casket", WizardryUtils.MODID, new ItemDustyCasket());
	}

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	public static <T> T placeholder() { return null; }

	/**
	 * Registers artefacts into the item registry, also handles loot injection to the Wizardry artefact loot tables.
	 * Categorization happens based on EnumRarity (Uncommon/Rare/Epic) - the standard Wizardry artefact rarities.
	 */
	public static void registerItemArtefact(IForgeRegistry<Item> registry, String name, String modid, Item item) {
		registerItemArtefact(registry, name, modid, item, true);
	}

	/**
	 * Registers artefacts into the item registry, also handles loot injection to the Wizardry artefact loot tables.
	 * Categorization happens based on EnumRarity (Uncommon/Rare/Epic) - the standard Wizardry artefact rarities.
	 */
	public static void registerItemArtefact(IForgeRegistry<Item> registry, String name, String modid, Item item, boolean addToLoot) {
		registerItem(registry, name, modid, item, false);
		if (addToLoot) {
			LootRegistry.addArtefact(item);
		}
	}

	// below registry methods are courtesy of EB
	public static void registerItem(IForgeRegistry<Item> registry, String name, String modid, Item item) {
		registerItem(registry, name, modid, item, false);
	}

	public static void registerItem(IForgeRegistry<Item> registry, String name, String modid, Item item, boolean setTabIcon) {
		item.setRegistryName(modid, name);
		item.setTranslationKey(item.getRegistryName().toString());
		registry.register(item);

		if (setTabIcon && item.getCreativeTab() instanceof WizardryTabs.CreativeTabSorted) {
			((WizardryTabs.CreativeTabSorted) item.getCreativeTab()).setIconItem(new ItemStack(item));
		}

		if (item.getCreativeTab() instanceof WizardryTabs.CreativeTabListed) {
			((WizardryTabs.CreativeTabListed) item.getCreativeTab()).order.add(item);
		}
	}

	public static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		Item itemblock = new ItemBlock(block).setRegistryName(block.getRegistryName());
		registry.register(itemblock);
	}

	public static void registerItemBlock(IForgeRegistry<Item> registry, Block block, Item itemblock) {
		itemblock.setRegistryName(block.getRegistryName());
		registry.register(itemblock);
	}
}