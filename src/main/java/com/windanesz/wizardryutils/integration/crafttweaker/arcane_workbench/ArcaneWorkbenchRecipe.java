
package com.windanesz.wizardryutils.integration.crafttweaker.arcane_workbench;

import net.minecraft.item.ItemStack;

import java.util.List;

public class ArcaneWorkbenchRecipe {

    private final ItemStack centerInput;
    private final ItemStack crystalInput;
    private final ItemStack upgradeInput;
    private final List<ItemStack> spellbookInputs;
    private final ItemStack output;

    public ArcaneWorkbenchRecipe(ItemStack centerInput, ItemStack crystalInput, ItemStack upgradeInput, List<ItemStack> spellbookInputs, ItemStack output) {
        this.centerInput = centerInput;
        this.crystalInput = crystalInput;
        this.upgradeInput = upgradeInput;
        this.spellbookInputs = spellbookInputs;
        this.output = output;
    }

    public boolean matches(ItemStack center, ItemStack crystal, ItemStack upgrade, List<ItemStack> spellbooks) {
        if (!ItemStack.areItemStacksEqual(this.centerInput, center)) {
            return false;
        }
        if (!ItemStack.areItemStacksEqual(this.crystalInput, crystal)) {
            return false;
        }
        if (!ItemStack.areItemStacksEqual(this.upgradeInput, upgrade)) {
            return false;
        }
        if (this.spellbookInputs.size() != spellbooks.size()) {
            return false;
        }
        for (int i = 0; i < this.spellbookInputs.size(); i++) {
            if (!ItemStack.areItemStacksEqual(this.spellbookInputs.get(i), spellbooks.get(i))) {
                return false;
            }
        }
        return true;
    }

    public ItemStack getOutput() {
        return this.output.copy();
    }

	public ItemStack getCenterInput() {
		return this.centerInput.copy();
	}

	public ItemStack getCrystalInput() {
		return this.crystalInput.copy();
	}

	public List<ItemStack> getSpellbookInputs() {
		return this.spellbookInputs.stream().map(ItemStack::copy).collect(java.util.stream.Collectors.toList());
	}

	public ItemStack getUpgradeInput() {
		return this.upgradeInput.copy();
	}
}
