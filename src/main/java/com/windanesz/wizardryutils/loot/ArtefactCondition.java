package com.windanesz.wizardryutils.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.windanesz.wizardryutils.WizardryUtils;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Random;

public class ArtefactCondition implements LootCondition {
	private final String requiredArtefact;
	private final boolean invert;

	public ArtefactCondition(String requiredArtefact, boolean invert) {
		this.requiredArtefact = requiredArtefact;
		this.invert = invert;
	}

	public boolean testCondition(Random rand, LootContext context) {

		// This method is badly-named, loot chests pass a player through too, not just mobs
		// (And WHY does it only return an entity?! The underlying field is always a player so I'm casting it anyway)
		EntityPlayer player = (EntityPlayer) context.getKillerPlayer();

		Item artefact = ForgeRegistries.ITEMS.getValue(new ResourceLocation(requiredArtefact));
		if (artefact == null) {
			Wizardry.logger.warn("Couldn't locate required artefact item in loottable condition!");
		} else if (player != null) {
			boolean isActive = ItemArtefact.isArtefactActive(player, artefact);
			if (invert) {
				return !isActive;
			} else {
				return isActive;
			}
		}
		return false;
	}

	public static class Serializer extends LootCondition.Serializer<ArtefactCondition> {

		public Serializer() {
			super(new ResourceLocation(WizardryUtils.MODID, "artefact_condition"), ArtefactCondition.class);
		}

		public void serialize(JsonObject json, ArtefactCondition value, JsonSerializationContext context) {
			json.addProperty("required_artefact", value.requiredArtefact);
			json.addProperty("invert", value.invert);
		}

		public ArtefactCondition deserialize(JsonObject object, JsonDeserializationContext context) {

			String requiredArtefact = JsonUtils.getString(object, "required_artefact");
			boolean invert = JsonUtils.getBoolean(object, "invert", false);

			return new ArtefactCondition(requiredArtefact, invert);
		}
	}
}
