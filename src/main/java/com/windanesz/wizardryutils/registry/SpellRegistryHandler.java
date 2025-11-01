
package com.windanesz.wizardryutils.registry;

import com.windanesz.wizardryutils.integration.crafttweaker.spell.ZenMinionSpell;
import com.windanesz.wizardryutils.integration.crafttweaker.spell.ZenBuffSpell;
import com.windanesz.wizardryutils.integration.crafttweaker.spell.ZenSpellProjectile;
import com.windanesz.wizardryutils.integration.crafttweaker.spell.ZenItemConjuration;
import com.windanesz.wizardryutils.integration.crafttweaker.spell.ZenAreaEffectSpell;
import com.windanesz.wizardryutils.integration.crafttweaker.spell.ZenCommandSpell;
import electroblob.wizardry.spell.Spell;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.ArrayList;

@Mod.EventBusSubscriber()
public class SpellRegistryHandler {

    @SubscribeEvent
    public static void registerSpells(RegistryEvent.Register<Spell> event) {
        // Create a new ArrayList to avoid ConcurrentModificationException
        List<ZenMinionSpell.MinionSpellEntry> minionSpellEntries = new ArrayList<>(ZenMinionSpell.entries);
        for (ZenMinionSpell.MinionSpellEntry entry : minionSpellEntries) {
            Spell spell = ZenMinionSpell.instantiate(entry);
            event.getRegistry().register(spell);    
        }

		// Create a new ArrayList to avoid ConcurrentModificationException
		List<ZenBuffSpell.BuffSpellEntry> buffSpellEntries = new ArrayList<>(ZenBuffSpell.entries);
		for (ZenBuffSpell.BuffSpellEntry entry : buffSpellEntries) {
			Spell spell = ZenBuffSpell.instantiate(entry);
			event.getRegistry().register(spell);
		}

		// Create a new ArrayList to avoid ConcurrentModificationException
		List<ZenAreaEffectSpell.AreaEffectSpellEntry> areaEffectSpellEntries = new ArrayList<>(ZenAreaEffectSpell.entries);
		for (ZenAreaEffectSpell.AreaEffectSpellEntry entry : areaEffectSpellEntries) {
			Spell spell = ZenAreaEffectSpell.instantiate(entry);
			event.getRegistry().register(spell);
		}

        // Create a new ArrayList to avoid ConcurrentModificationException
        List<ZenSpellProjectile.ProjectileSpellEntry> projectileSpellEntries = new ArrayList<>(ZenSpellProjectile.entries);
        for (ZenSpellProjectile.ProjectileSpellEntry entry : projectileSpellEntries) {
            Spell spell = ZenSpellProjectile.instantiate(entry);
            event.getRegistry().register(spell);
        }

        // Create a new ArrayList to avoid ConcurrentModificationException
        List<ZenItemConjuration.ConjurationSpellEntry> conjurationSpellEntries = new ArrayList<>(ZenItemConjuration.entries);
        for (ZenItemConjuration.ConjurationSpellEntry entry : conjurationSpellEntries) {
            Spell spell = ZenItemConjuration.instantiate(entry);
            event.getRegistry().register(spell);
        }

        // Create a new ArrayList to avoid ConcurrentModificationException
        List<ZenCommandSpell.CommandSpellEntry> commandSpellEntries = new ArrayList<>(ZenCommandSpell.entries);
        for (ZenCommandSpell.CommandSpellEntry entry : commandSpellEntries) {
            Spell spell = ZenCommandSpell.instantiate(entry);
            event.getRegistry().register(spell);
        }
    }
}
