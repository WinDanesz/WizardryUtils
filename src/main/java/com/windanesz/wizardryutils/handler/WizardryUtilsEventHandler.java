package com.windanesz.wizardryutils.handler;

import com.windanesz.wizardryutils.integration.baubles.BaublesIntegration;
import com.windanesz.wizardryutils.server.Attributes;
import com.windanesz.wizardryutils.server.RangedAttributeElemental;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.item.IManaStoringItem;
import electroblob.wizardry.item.ISpellCastingItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;

@Mod.EventBusSubscriber
public class WizardryUtilsEventHandler {

	private WizardryUtilsEventHandler() {} // no instances

	@SubscribeEvent
	public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {

		// tick for condensing attribute
		if (!event.player.world.isRemote && event.phase == TickEvent.Phase.START) {

			EntityPlayer player = event.player;
			if (player.ticksExisted % 90 == 0) {
				IAttributeInstance attribute = player.getEntityAttribute(Attributes.CONDENSING);
				//noinspection ConstantConditions
				if (attribute != null && attribute.getAttributeValue() != 0) {
					double amount = attribute.getAttributeValue();
					int mana = (int) amount;
					// always positive
					double fraction = amount - Math.floor(amount);

					// might be not perfect, but needed a way to restore mana even for smaller amounts than 1. The rounding to int didn't allow any fractions.
					if (fraction != 0 && event.player.world.rand.nextFloat() <= fraction) {
						if (amount > 0) {
							// round up
							mana = (int) Math.ceil(amount);
						} else {
							mana = (int) Math.floor(amount);
						}
					}

					ItemStack mainHand = player.getHeldItemMainhand();
					if (mainHand.getItem() instanceof ISpellCastingItem && mainHand.getItem() instanceof IManaStoringItem) {
						handleCondensing(mainHand, mana);
					}
					ItemStack offHand = player.getHeldItemOffhand();
					if (offHand.getItem() instanceof ISpellCastingItem && offHand.getItem() instanceof IManaStoringItem) {
						handleCondensing(offHand, mana);
					}
				}
			}
		}

		// tick artefacts
		if (event.phase == TickEvent.Phase.END) {
			EntityPlayer player = event.player;
			if (BaublesIntegration.enabled()) {
				BaublesIntegration.tickWornArtefacts(player);
			}
		}
	}

	private static void handleCondensing(ItemStack stack, int amount) {
		if (amount > 0) {
			((IManaStoringItem) stack.getItem()).rechargeMana(stack, amount);
		} else {
			((IManaStoringItem) stack.getItem()).consumeMana(stack, -1 * amount, null);
		}
	}

	/**
	 * Attribute attaching
	 *
	 * @param event
	 */
	@SubscribeEvent
	public static void onEntityConstructingEvent(EntityEvent.EntityConstructing event) {
		Entity entity = event.getEntity();
		if (entity instanceof EntityLivingBase) {
			Attributes.addAttributes((EntityLivingBase) entity);
		}
	}

	/**
	 * Making use of the attributes
	 *
	 * @param event
	 */
	@SubscribeEvent
	public static void onSpellCastEventPre(SpellCastEvent.Pre event) {
		if (!event.getWorld().isRemote && event.getCaster() != null) {
			EntityLivingBase caster = event.getCaster();

			for (Map.Entry<RangedAttribute, String> entry : Attributes.getSpellModifierAttributes().entrySet()) {

				if (entry.getKey() == Attributes.CONDENSING) {
					continue;
				}

				IAttributeInstance attributeInstance = caster.getEntityAttribute(entry.getKey());
				//noinspection ConstantConditions

				if (attributeInstance != null) {
					if (!(attributeInstance.getAttribute() instanceof RangedAttributeElemental) ||
							(attributeInstance.getAttribute() instanceof RangedAttributeElemental && event.getSpell().getElement()
									== ((RangedAttributeElemental) attributeInstance.getAttribute()).element)) {

						String modifier = entry.getValue();
						float oldValue = event.getModifiers().get(modifier);
						float newValue = (float) (oldValue * (0.01 * attributeInstance.getAttributeValue()));
						event.getModifiers().set(modifier, newValue, true);
					}
				}
			}
		}
	}

}
