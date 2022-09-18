package com.windanesz.wizardryutils.tools;

import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellProperties;

import java.io.FileWriter;
import java.io.IOException;

public class GeneratorSnippets {

	public static void writeWikiPage(String modid, String githubRepo) {
		int i = 1;
		String firstAppeared = "1.0";

		for (Spell spell : Spell.getSpells(spell -> spell.getRegistryName().getNamespace().equals(modid))) {

			try {

				FileWriter writer = new FileWriter("generated\\" + spell.getDisplayName().replace(' ', '-') + ".md");

				writer.write("| " + spell.getDisplayName() + " |![](https://github.com/" + githubRepo + "/blob/1.12.2/src/main/resources/assets/" + modid + "/textures/spells/" + spell.getUnlocalisedName() + ".png)|\n" +
						"|---|---|\n" +
						"| Tier | " + spell.getTier().getDisplayName() + " |\n" +
						"| Element | " + spell.getElement().getDisplayName() + " |\n" +
						"| Type | " + spell.getType().getDisplayName() + " |\n" +
						"| Mana Cost | " + spell.getCost() + " |\n" +
						"| Continuous | " + (spell.isContinuous ? "Yes" : "No") + " |\n" +
						"| Cast by wizards | " + (spell.isEnabled(SpellProperties.Context.NPCS) ? "Yes" : "No") + " |\n" +
						"| Cast by dispensers | " + (spell.isEnabled(SpellProperties.Context.DISPENSERS) ? "Yes" : "No") + " |\n" +
						"| ID | `" + spell.getRegistryName() + "` |\n" +
						"| Metadata | " + i++ + " |\n" +
						// "| First appeared in | Wizardry " + firstAppeared + " |\n" +
						"## Description\n" +
						"_" + spell.getDescription() + "_");

				writer.close();

			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
