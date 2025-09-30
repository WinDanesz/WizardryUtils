
package com.windanesz.wizardryutils.integration.crafttweaker.imbuement_altar;

import electroblob.wizardry.constants.Element;
import electroblob.wizardry.event.ImbuementActivateEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;

public class ImbuementAltarCrafttweakerEventHandler {

    @SubscribeEvent
    public void onImbuementActivate(ImbuementActivateEvent event) {
        try {
            // Get the input ItemStack from the event
            ItemStack input = getInput(event);
            if (input == null || input.isEmpty()) return;

            // Get the elements array from the event
            Element[] elements = getElements(event);
            if (elements == null) return;

            // Find a matching CraftTweaker recipe
            ImbuementAltarRecipe recipe = ImbuementAltarRecipeRegistry.findMatchingRecipe(input, elements);

            if (recipe != null) {
                // Set the event result using reflection
                setEventResult(event, recipe.getOutput());
            }
        } catch (Exception e) {
            // If anything fails, silently ignore for compatibility
            e.printStackTrace();
        }
    }

    /**
     * Gets the input ItemStack from the event using reflection.
     */
    private ItemStack getInput(ImbuementActivateEvent event) {
        try {
            Field inputField = ImbuementActivateEvent.class.getDeclaredField("input");
            inputField.setAccessible(true);
            return (ItemStack) inputField.get(event);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the elements array from the event using reflection.
     */
    private Element[] getElements(ImbuementActivateEvent event) {
        try {
            Field elementsField = ImbuementActivateEvent.class.getDeclaredField("receptacleElements");
            elementsField.setAccessible(true);
            return (Element[]) elementsField.get(event);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Sets the event result using reflection.
     */
    private void setEventResult(ImbuementActivateEvent event, ItemStack result) {
        try {
            Field resultField = ImbuementActivateEvent.class.getDeclaredField("eventResult");
            resultField.setAccessible(true);
            resultField.set(event, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

