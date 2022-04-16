package com.windanesz.wizardryutils.registry;

import com.windanesz.wizardryutils.Settings;
import com.windanesz.wizardryutils.WizardryUtils;
import electroblob.wizardry.Wizardry;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class responsible for registering WizardryUtil's loot tables and injecting artefacts to loot tables.
 *
 * @author WinDanesz
 */
@Mod.EventBusSubscriber
public class LootRegistry {

	private static List<Item> artefacts = new ArrayList<>();

	private LootRegistry() {} // No instances!

	public static void addArtefact(Item artefact) {
		artefacts.add(artefact);
	}

	@SubscribeEvent
	public static void onLootTableLoadEvent(LootTableLoadEvent event) {
		/////////////// Artefact injection ///////////////

		if (Settings.generalSettings.auto_inject_artefacts_to_shrines) {

			if (event.getName().getNamespace().equals(Wizardry.MODID)) {

				// pattern for any artefact subset table
				Pattern p = Pattern.compile(".*subsets/(.*)_artefacts");
				Matcher m = p.matcher(event.getName().getPath());

				if (m.find()) {

					// default pool name prefix is <tier>_artefacts
					String poolName = m.group(1) + "_artefacts";

					// turning this prefix into an EnumRarity
					EnumRarity rarity = EnumRarity.valueOf(m.group(1).toUpperCase());

					// items of this list will be injected into this loot table
					List<Item> inject = new ArrayList<>();

					// check all artefacts in the artefact list, if rarity matches, they'll be added to the inject list
					for (Item artefact : artefacts) {
						if (artefact.getForgeRarity(new ItemStack(artefact)) == rarity) {
							inject.add(artefact);
						}
					}

					if (!inject.isEmpty()) {
						int index = 0;
						for (Item item : inject) {
							WizardryUtils.logger.debug("Injecting loot entry item " + item.getRegistryName().toString() + " to " + poolName + " Wizardry loot table.");
							event.getTable().getPool(poolName).addEntry(new LootEntryItem(item, 1, 0, new LootFunction[0], new LootCondition[0],
									item.getRegistryName().getPath() + "_" + index));
						}
					}
				}
			}
		}
	}

	/**
	 * Injects every element of sourcePool into targetPool
	 */
	private static void injectEntries(LootPool sourcePool, LootPool targetPool) {
		// Accessing {@link net.minecraft.world.storage.loot.LootPool.lootEntries}
		if (sourcePool != null && targetPool != null) {
			List<LootEntry> lootEntries = ObfuscationReflectionHelper.getPrivateValue(LootPool.class, sourcePool, "field_186453_a");

			for (LootEntry entry : lootEntries) {
				targetPool.addEntry(entry);
			}
		} else {
			WizardryUtils.logger.warn("Attempted to inject to null pool source or target.");
		}

	}

	private static LootPool getAdditive(String entryName, String poolName) {
		return new LootPool(new LootEntry[] {getAdditiveEntry(entryName, 1)}, new LootCondition[0],
				new RandomValueRange(1), new RandomValueRange(0, 1), WizardryUtils.MODID + "_" + poolName);
	}

	private static LootEntryTable getAdditiveEntry(String name, int weight) {
		return new LootEntryTable(new ResourceLocation(name), weight, 0, new LootCondition[0],
				WizardryUtils.MODID + "_additive_entry");
	}

}
