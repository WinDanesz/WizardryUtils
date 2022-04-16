package com.windanesz.wizardryutils;

import com.windanesz.wizardryutils.item.ItemNewArtefact;
import electroblob.wizardry.Wizardry;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ArtefactSettings {

	/** The unlocalised name of the artefacts config category. */
	public static final String ADDITIONAL_ARTEFACTS_CATEGORY = "artefacts";

	/** The config file. */
	private Configuration config;

	private List<String> redundantKeys = new ArrayList<>();

	/**
	 * Called from preInit to initialise the config file. The first part of the config file has to be done here (as is
	 * conventional) so that the various registries can change what they do accordingly. The spell part of the config
	 * and the resistance part of the config have to be done in the init method since they depend on the registry events.
	 */
	void initConfig(FMLPreInitializationEvent event){

		redundantKeys.clear();

		config = new Configuration(new File(Wizardry.configDirectory, WizardryUtils.MODID + "_artefacts.cfg"));
		config.load();

		WizardryUtils.logger.info("Setting up main config");

		config.save();
	}

	/**
	 * Called from init to initialise the parts of the config file that have to be done after registry events are fired.
	 * For example, the spell config is done here and not in preInit because all spells must be registered before it is
	 * added, including those in other mods.
	 */
	void initConfigExtras(){

		WizardryUtils.logger.info("Setting up artefacts (HEAD, BELT, BODY slot) config");
		setupArtefactsConfig();

		config.save();
	}

	/** Called to save changes to the config file after it has been edited in game from the menus. */
	void saveConfigChanges(){

		redundantKeys.clear();

		WizardryUtils.logger.info("Saving in-game config changes");

		setupArtefactsConfig();

		config.save();
	}

	void checkForRedundantOptions(String categoryName, Collection<String> validKeys){

		ConfigCategory category = config.getCategory(categoryName);
		boolean redundantKeysFound = false;

		for(String key : category.keySet()){
			if(!validKeys.contains(key)){
				redundantKeys.add(key);
				if(!redundantKeysFound){
					redundantKeysFound = true;
					WizardryUtils.logger.info("Config category {} contains redundant options:", categoryName);
				}
				WizardryUtils.logger.info(key);
			}
		}

		if(redundantKeysFound){
			WizardryUtils.logger.info("These options will have no effect (they are probably left over from an older version of Wizardy Utils or an addon which depends on it). It is recommended to either remove them manually, or delete wizardryutils_artefacts.cfg and allow a fresh config to be generated.");
		}
	}

	/** Returns true if the config file contains keys that are not valid options (usually because of updates). */
	public boolean hasRedundantKeys(){
		return !redundantKeys.isEmpty();
	}

	public ConfigCategory getConfigCategory(String name){
		return config.getCategory(name);
	}

	private void setupArtefactsConfig(){

		config.addCustomCategoryComment(ADDITIONAL_ARTEFACTS_CATEGORY,
				"Set an item to false to disable it. Disabled items will still appear in-game but will have no effect when worn. It is also advisable to remove disabled items from wizardry's (and addons') loot tables. Disable an item if it is causing problems, conflicts with another mod or creates an unintended exploit.");

		Property property;

		List<String> keys = new ArrayList<>();

		for(Item item : Item.REGISTRY){
			if(item instanceof ItemNewArtefact){
				keys.add(item.getRegistryName().toString());
				property = config.get(ADDITIONAL_ARTEFACTS_CATEGORY, item.getRegistryName().toString(), true,
						"Set to false to disable this item");
				// Uses the same config key as the item name, because - well, that's what it's called!
				property.setLanguageKey(item.getTranslationKey() + ".name");
				Wizardry.proxy.setToNamedBooleanEntry(property);
				((ItemNewArtefact)item).setEnabled(property.getBoolean());
			}
		}

		checkForRedundantOptions(ADDITIONAL_ARTEFACTS_CATEGORY, keys);
	}
}
