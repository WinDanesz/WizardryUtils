package com.windanesz.wizardryutils.integration.baubles;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import com.windanesz.wizardryutils.Settings;
import com.windanesz.wizardryutils.item.ITickableArtefact;
import com.windanesz.wizardryutils.item.ItemNewArtefact;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a replica of Wizardry's Baubles integration {@link electroblob.wizardry.integration.baubles},
 * its necessary to add support to new bauble types till an API exists for {@link electroblob.wizardry.item.ItemArtefact.Type}.
 *
 * @author Electroblob, WinDanesz
 */
public final class BaublesIntegration {

	public static final String BAUBLES_MOD_ID = "baubles";

	private static final Map<ItemArtefact.Type, BaubleType> DEFAULT_ARTEFACT_TYPE_MAP = new EnumMap<>(ItemArtefact.Type.class);
	private static final Map<ItemNewArtefact.Type, BaubleType> OTHER_ARTEFACT_TYPE_MAP = new EnumMap<>(ItemNewArtefact.Type.class);

	private static boolean baublesLoaded;

	public static void init() {

		baublesLoaded = Loader.isModLoaded(BAUBLES_MOD_ID);

		if (!enabled()) { return; }

		DEFAULT_ARTEFACT_TYPE_MAP.put(ItemArtefact.Type.RING, BaubleType.RING);
		DEFAULT_ARTEFACT_TYPE_MAP.put(ItemArtefact.Type.AMULET, BaubleType.AMULET);
		DEFAULT_ARTEFACT_TYPE_MAP.put(ItemArtefact.Type.CHARM, BaubleType.CHARM);

		OTHER_ARTEFACT_TYPE_MAP.put(ItemNewArtefact.Type.BELT, BaubleType.BELT);
		OTHER_ARTEFACT_TYPE_MAP.put(ItemNewArtefact.Type.HEAD, BaubleType.HEAD);
		OTHER_ARTEFACT_TYPE_MAP.put(ItemNewArtefact.Type.BODY, BaubleType.HEAD);
	}

	public static boolean enabled() {
		return Settings.generalSettings.baubles_integration && baublesLoaded;
	}

	// Wrappers for BaublesApi methods

	/**
	 * Returns a list of artefact stacks equipped of the given types. <i>This method does not check whether artefacts
	 * have been disabled in the config! {ItemNewArtefact#getActiveArtefacts(EntityPlayer, ItemNewArtefact.AdditionalType...)}
	 * should be used instead of this method in nearly all cases.</i>
	 *
	 * @param player The player whose inventory is to be checked.
	 * @param types  Zero or more artefact types to check for. If omitted, searches for all types.
	 * @return A list of equipped artefact {@code ItemStacks}.
	 */
	// This could return all ItemStacks, but if an artefact type is given this doesn't really make sense.
	public static List<ItemNewArtefact> getEquippedArtefacts(EntityPlayer player, ItemNewArtefact.Type... types) {

		List<ItemNewArtefact> artefacts = new ArrayList<>();

		for (ItemNewArtefact.Type type : types) {
			for (int slot : OTHER_ARTEFACT_TYPE_MAP.get(type).getValidSlots()) {
				ItemStack stack = BaublesApi.getBaublesHandler(player).getStackInSlot(slot);
				if (stack.getItem() instanceof ItemNewArtefact) { artefacts.add((ItemNewArtefact) stack.getItem()); }
			}
		}

		return artefacts;
	}

	// Shamelessly copied from The Twilight Forest, with a few modifications
	@SuppressWarnings("unchecked")
	public static final class ArtefactBaubleProvider implements ICapabilityProvider {

		private BaubleType type;

		public ArtefactBaubleProvider(ItemNewArtefact.Type type) {
			this.type = OTHER_ARTEFACT_TYPE_MAP.get(type);
		}

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;
		}

		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			// This lambda expression is an implementation of the entire IBauble interface
			return capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE ? (T) (IBauble) itemStack -> type : null;
		}
	}

	/**
	 * Gets all equipped artefacts for the given player with their slot number.
	 * @param player the player to check
	 * @return all artefacts, including both the default and new types and their current bauble slot. Excludes BaubleType.TRINKLET, as that is not used as an artefact anyways.
	 */
	public static Map<Integer, ItemStack> getAllEquippedArtefacts(EntityPlayer player) {

		Map<Integer, ItemStack> artefacts = new HashMap<>();

		for (int i = 0; i <= 6; i++) {
			ItemStack stack = BaublesApi.getBaublesHandler(player).getStackInSlot(i);
			if (stack.getItem() instanceof ItemArtefact || stack.getItem() instanceof ItemArtefact) {
				artefacts.put(i, stack);
			}
		}

		return artefacts;
	}

	 /**
	 * Returns all equipped artefact ItemStack of the given type for the given player. This includes both the new types of {@link ItemNewArtefact} and the other types {@link ItemArtefact}.
	 * @param player player to check
	 * @param artefactType this must be an array of {@link ItemArtefact.Type} enums and {@link ItemNewArtefact.Type} enum, the method won't do anything for other objects!
	 * @return a List of ItemStacks for the given artefact type.
	 */
	public static List<ItemStack> getEquippedArtefactStacks(EntityPlayer player, Object... artefactType) {

		List<ItemStack> artefacts = new ArrayList<>();

		for (Object type : artefactType) {

			if (type instanceof ItemArtefact.Type) {
				// check the default types.. (AMULET, RING, CHARM)
				for (int slot : DEFAULT_ARTEFACT_TYPE_MAP.get((ItemArtefact.Type) type).getValidSlots()) {
					ItemStack stack = BaublesApi.getBaublesHandler(player).getStackInSlot(slot);
					if (stack.getItem() instanceof ItemArtefact) { artefacts.add(stack); }
				}
			} else if (type instanceof ItemNewArtefact.Type) {
				// check the other types (HEAD, BELT, BODY)
				for (int slot : OTHER_ARTEFACT_TYPE_MAP.get((ItemNewArtefact.Type) type).getValidSlots()) {
					ItemStack stack = BaublesApi.getBaublesHandler(player).getStackInSlot(slot);
					if (stack.getItem() instanceof ItemNewArtefact) { artefacts.add(stack); }
				}
			}
		}

		return artefacts;
	}

	public static void setArtefactToSlot(EntityPlayer player, ItemStack stack, ItemArtefact.Type type) {
		setArtefactToSlot(player, stack, type, 0);
	}

	public static void setArtefactToSlot(EntityPlayer player, ItemStack stack, ItemArtefact.Type type, int slotId) {
		BaublesApi.getBaublesHandler(player).setStackInSlot(DEFAULT_ARTEFACT_TYPE_MAP.get(type).getValidSlots()[slotId], stack);
	}

	public static void tickWornArtefacts(EntityPlayer player) {

		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
		for (int i = 0; i < baubles.getSlots(); i++) {
			ItemStack stack = baubles.getStackInSlot(i);
			if (stack.getItem() instanceof ITickableArtefact) {
				((ITickableArtefact) stack.getItem()).onWornTick(stack, player);
			}
		}
	}
}
