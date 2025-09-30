
package com.windanesz.wizardryutils.integration.crafttweaker.imbuement_altar;

import electroblob.wizardry.constants.Element;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class ImbuementAltarRecipe {

    private final ItemStack input;
    private final Element[] elements;
    private final ItemStack output;

    public ImbuementAltarRecipe(ItemStack input, Element[] elements, ItemStack output) {
        this.input = input;
        this.elements = elements;
        this.output = output;
    }

    public boolean matches(ItemStack input, Element[] elements) {
        if (!ItemStack.areItemStacksEqual(this.input, input)) {
            return false;
        }
        return Arrays.equals(this.elements, elements);
    }

    public ItemStack getOutput() {
        return this.output.copy();
    }

    public ItemStack getInput() {
        return this.input.copy();
    }

    public electroblob.wizardry.constants.Element[] getElements() {
        return this.elements == null ? null : Arrays.copyOf(this.elements, this.elements.length);
    }
}
