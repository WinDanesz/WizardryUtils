package com.windanesz.wizardryutils.tools;

import electroblob.wizardry.registry.WizardryPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class WizardryUtilsTools {

	/**
	 * Static method to give an ItemStack to a player. Handles side checks and null checks, prioritizes the hands.
	 *
	 * @param player the player who receives the item
	 * @param stack  the stack to give
	 * @return false if failed to give, true if successfully gave the item
	 */
	public static boolean giveStackToPlayer(EntityPlayer player, ItemStack stack) {
		if (player != null && stack != null && !stack.isEmpty()) {

			if (!player.world.isRemote) {

				if (player.getHeldItemMainhand().isEmpty()) {
					// main hand
					player.setHeldItem(EnumHand.MAIN_HAND, stack);
				} else if (player.getHeldItemOffhand().isEmpty()) {
					// offhand
					player.setHeldItem(EnumHand.OFF_HAND, stack);
				} else {
					// any slot
					if (!player.inventory.addItemStackToInventory(stack)) {
						// or just drop the item..
						player.dropItem(stack, false);
					}
				}

				return true;
			}
		}

		return false;
	}

	/**
	 * A {@link EntityLivingBase#isEntityUndead()} check, which also handles considering entities with Curse of Undeath as undeads.
	 *
	 * @param entity the entity to check
	 * @return true, if the entity is an undead or is cursed to be an undead
	 */
	public static boolean isEntityConsideredUndead(Entity entity) {
		return entity instanceof EntityLivingBase && (((EntityLivingBase) entity).isEntityUndead() || ((EntityLivingBase) entity).isPotionActive(WizardryPotions.curse_of_undeath));
	}

	public static Double getAttribute(EntityLivingBase entity, String attributeName) {
		for (IAttributeInstance instance : entity.getAttributeMap().getAllAttributes()) {
			if (instance.getAttribute().getName().equals(attributeName)) {
				return instance.getAttributeValue();
			}
		}

		return null;
	}
}
