package com.windanesz.wizardryutils.capability;

import com.windanesz.wizardryutils.WizardryUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;

/**
 * Summoned item capability, these items disappear over time and cannot leave the inventory
 *
 * @author WinDanesz
 */
// On the plus side, having to rethink this class allowed me to clean it up a lot.
@Mod.EventBusSubscriber
public class SummonedItemCapability extends SummonedThing implements INBTSerializable<NBTTagCompound> {

	public static HashSet<Item> ITEMS_TO_APPLY_TO = new HashSet<Item>();
	/**
	 * Static instance of what I like to refer to as the capability key. Private because, well, it's internal!
	 * This annotation does some crazy Forge magic behind the scenes and assigns this field a value.
	 */
	@CapabilityInject(SummonedItemCapability.class)
	private static final Capability<SummonedItemCapability> SUMMONED_ITEM_CAPABILITY = null;

	/**
	 * The ItemStack this capability instance belongs to.
	 */
	private final ItemStack stack;

	public SummonedItemCapability() {
		this(null); // Nullary constructor for the registration method factory parameter
	}

	public SummonedItemCapability(ItemStack stack) {
		this.stack = stack;
	}

	/**
	 * Called from preInit in the main mod class to register the WizardData capability.
	 */
	public static void register() {

		// Yes - by the looks of it, having an interface is completely unnecessary in this case.
		CapabilityManager.INSTANCE.register(SummonedItemCapability.class, new IStorage<SummonedItemCapability>() {
			// These methods are only called by Capability.writeNBT() or Capability.readNBT(), which in turn are
			// NEVER CALLED. Unless I'm missing some reflective invocation, that means this entire class serves only
			// to allow capabilities to be saved and loaded manually. What that would be useful for I don't know.
			// (If an API forces most users to write redundant code for no reason, it's not user friendly, is it?)
			// ... well, that's my rant for today!
			@Override
			public NBTBase writeNBT(Capability<SummonedItemCapability> capability, SummonedItemCapability instance, EnumFacing side) {
				return null;
			}

			@Override
			public void readNBT(Capability<SummonedItemCapability> capability, SummonedItemCapability instance, EnumFacing side, NBTBase nbt) {}

		}, SummonedItemCapability::new);
	}

	/**
	 * Returns the WizardData instance for the specified ItemStack.
	 */
	public static SummonedItemCapability get(ItemStack stack) {
		return stack.getCapability(SUMMONED_ITEM_CAPABILITY, null);
	}

	/**
	 * Checks if the given ItemStack is considered as a summon (= has the {@link SummonedItemCapability#SUMMONED_ITEM_CAPABILITY}) capability).
	 *
	 * @param stack to check
	 * @return true if the has has the capability, false otherwise
	 */
	public static boolean isSummonedItem(ItemStack stack) {
		return stack != null && stack != ItemStack.EMPTY && get(stack) != null && get(stack).summoned;
	}

	// ============================================== Event Handlers ==============================================
	// See {@link com.windanesz.wizardryutils.client.ClientEventHandler} for the client-only events, like the tooltip attachment

	@SubscribeEvent
	public static void attachCapability(AttachCapabilitiesEvent<ItemStack> event) {
		if (event.getObject() != null && ITEMS_TO_APPLY_TO.contains(event.getObject().getItem())) {
			event.addCapability(new ResourceLocation(WizardryUtils.MODID, "SummonedItemData"),
					new Provider((ItemStack) event.getObject()));
		}
	}


	/**
	 * Summoned ItemStacks cannot be thrown away!
	 * Canceling the event will stop the items from entering the world, but will not prevent them being removed from the inventory - and thus removed from the system,
	 * hence it needs to be re-added to the inventory.
	 *
	 * @param event ItemTossEvent Event that is fired whenever a player tosses (Q) an item or drag-n-drops a stack of items outside the inventory GUI screens.
	 */
	@SubscribeEvent
	public static void onItemTossEvent(ItemTossEvent event) {
		// Prevents conjured items being thrown by dragging and dropping outside the inventory.
		if (isSummonedItem(event.getEntityItem().getItem())) {
			event.setCanceled(true);
			//event.getPlayer().inventory.addItemStackToInventory(event.getEntityItem().getItem());
		}
	}

	/**
	 * Conjured items shouldn't spill on the ground when their holder dies.
	 *
	 * @param event LivingDropsEvent is fired when an Entity's death causes dropped items to appear.
	 */
	@SubscribeEvent
	public static void onLivingDropsEvent(LivingDropsEvent event) {
		// Destroys conjured items if their caster dies.
		for (EntityItem item : event.getDrops()) {
			// Apparently some mods don't behave and shove null items in the list, quite why I have no idea
			if (item != null && item.getItem() != null && isSummonedItem(item.getItem())) {
				item.setDead();
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {

		// tick artefacts
		if (event.phase == TickEvent.Phase.END) {
			EntityPlayer player = event.player;

			for (ItemStack stack : player.inventory.offHandInventory) {
				if (isSummonedItem(player.getHeldItemOffhand())) {
					if (player.getHeldItemOffhand() != stack) {
						stack.shrink(1);
						return;
					}
					get(stack).updateDelegate(stack);
				}
			}

			for (ItemStack stack : player.inventory.mainInventory) {
				if (isSummonedItem(stack)) {
					if ((player.getHeldItemMainhand() != stack)) {
						stack.shrink(1);
						return;
					}
					get(stack).updateDelegate(stack);
				}
			}

//			for (ItemStack stack : player.inventory.armorInventory) {
//				if (isSummonedItem(stack)) {
//					get(stack).updateDelegate(stack);
//				}
//			}


		}
	}

	@SubscribeEvent
	public static void onPlayerInteractEvent(PlayerInteractEvent.RightClickBlock event) {
		if (!event.getEntityPlayer().world.isRemote) {

			EntityPlayer player = event.getEntityPlayer();
			if (isSummonedItem(player.getHeldItemOffhand())) {
				player.setHeldItem(EnumHand.OFF_HAND, ItemStack.EMPTY);
				}

			if (isSummonedItem(player.getHeldItemMainhand())) {
				player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
			}
		}
	}

	void updateDelegate(ItemStack stack) {
		// TODO
		// Don't do anything for not summoned itemstacks, shouldn't happen anyways as this event is only called for summons!
		if (!summoned) { return; }

		if (this.getLifetime() <= 0) {
			this.stack.shrink(1);
		}

		this.setLifetime(getLifetime() -1);
	}


	// ========================================== Capability Boilerplate ==========================================

	/**
	 * This is a nested class for a few reasons: firstly, it makes sense because instances of this and WizardData go
	 * hand-in-hand; secondly, it's too short to be worth a separate file; and thirdly (and most importantly) it allows
	 * me to access WIZARD_DATA_CAPABILITY while keeping it private.
	 */
	public static class Provider implements ICapabilitySerializable<NBTTagCompound> {

		private final SummonedItemCapability data;

		public Provider(ItemStack stack) {
			data = new SummonedItemCapability(stack);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == SUMMONED_ITEM_CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

			if (capability == SUMMONED_ITEM_CAPABILITY) {
				return SUMMONED_ITEM_CAPABILITY.cast(data);
			}

			return null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return data.serializeNBT();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			data.deserializeNBT(nbt);
		}

	}

}
