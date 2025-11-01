package com.windanesz.wizardryutils.integration.crafttweaker.spell;

import com.windanesz.wizardryutils.spell.SpellDynamicCommand;
import crafttweaker.annotations.ZenRegister;
import electroblob.wizardry.spell.Spell;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.wizardryutils.CommandSpells")
@ZenRegister
public class ZenCommandSpell {

	public static final class CommandSpellEntry {
		public final String name;
		public final String command;

		public CommandSpellEntry(String name, String command) {
			this.name = name;
			this.command = command;
		}
	}

	public static final List<CommandSpellEntry> entries = new ArrayList<>();

	/**
	 * Creates a spell that executes a Minecraft command when cast.
	 * <p>
	 * The command is executed as the caster (player) or from the world if cast by a dispenser.
	 * You can use placeholders in the command:
	 * <ul>
	 *   <li>{@code @caster} - The caster's name (player name or "Dispenser")</li>
	 *   <li>{@code @x}, {@code @y}, {@code @z} - Cast position coordinates</li>
	 *   <li>{@code @dim} - Dimension ID</li>
	 * </ul>
	 * <p>
	 * Example usage in CraftTweaker:
	 * <pre>
	 * // Simple command
	 * mods.wizardryutils.CommandSpells.create("summon_lightning", "summon lightning_bolt @x @y @z");
	 * 
	 * // Command with caster placeholder
	 * mods.wizardryutils.CommandSpells.create("teleport_home", "tp @caster ~ 100 ~");
	 * 
	 * // Give items to caster
	 * mods.wizardryutils.CommandSpells.create("conjure_diamond", "give @caster minecraft:diamond 1");
	 * </pre>
	 * 
	 * @param name The name of the spell (will be prefixed with "contenttweaker:")
	 * @param command The Minecraft command to execute (without leading slash)
	 */
	@ZenMethod
	public static void create(String name, String command) {
		entries.add(new CommandSpellEntry(name, command));
	}

	public static Spell instantiate(CommandSpellEntry entry) {
		if (entry == null) {
			throw new RuntimeException("CommandSpellEntry cannot be null!");
		}

		return new SpellDynamicCommand("contenttweaker", entry.name, entry.command);
	}
}
