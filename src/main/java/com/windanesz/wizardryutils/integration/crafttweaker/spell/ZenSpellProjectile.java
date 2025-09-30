
package com.windanesz.wizardryutils.integration.crafttweaker.spell;

import com.windanesz.wizardryutils.spell.SpellIProjectile;
import crafttweaker.annotations.ZenRegister;
import electroblob.wizardry.spell.Spell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.wizardryutils.ProjectileSpells")
@ZenRegister
public class ZenSpellProjectile {

    public static final class ProjectileSpellEntry {
        public final String name;
        public final String entityClassName;

        public ProjectileSpellEntry(String name, String entityClassName) {
            this.name = name;
            this.entityClassName = entityClassName;
        }
    }

    public static final List<ProjectileSpellEntry> entries = new ArrayList<>();

    /**
     * Creates a new projectile spell using the specified entity class.
     * <p>
     * Takes a fully qualified class path instead of entity registry name to avoid early registration timing issues.
     * The class is instantiated via reflection at runtime when the spell is cast, not during registration.
     * <p>
     * Example usage in CraftTweaker:
     * <pre>
     * // For vanilla arrows
     * mods.wizardryutils.ProjectileSpells.create("arrow_spell", "net.minecraft.entity.projectile.EntityArrow");
     * 
     * // For modded projectiles
     * mods.wizardryutils.ProjectileSpells.create("custom_spell", "com.example.mod.entity.EntityCustomProjectile");
     * </pre>
     * 
     * @param name The name of the spell (will be prefixed with the mod ID)
     * @param entityClassName The fully qualified class name of the projectile entity (must extend Entity and implement IProjectile)
     */
    @ZenMethod
    public static void create(String name, String entityClassName) {
        entries.add(new ProjectileSpellEntry(name, entityClassName));
    }

    public static Spell instantiate(ProjectileSpellEntry entry) {
        if (entry == null) {
            throw new RuntimeException("ProjectileSpellEntry cannot be null!");
        }

        String name = entry.name;
        String entityClassName = entry.entityClassName;

        // Validate the class exists and is a valid projectile at registration time
        try {
            Class<?> entityClass = Class.forName(entityClassName);
            if (!Entity.class.isAssignableFrom(entityClass)) {
                throw new RuntimeException("Class " + entityClassName + " is not an Entity!");
            }
            if (!IProjectile.class.isAssignableFrom(entityClass)) {
                throw new RuntimeException("Class " + entityClassName + " does not implement IProjectile!");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find projectile class " + entityClassName + "!", e);
        }

        SpellIProjectile<?> spell = new SpellIProjectile<>(
                "contenttweaker",
                name,
                (world) -> {
                    try {
                        Class<?> entityClass = Class.forName(entityClassName);
                        Constructor<?> constructor = entityClass.getConstructor(World.class);
                        Entity entity = (Entity) constructor.newInstance(world);
                        
                        if (entity instanceof IProjectile) {
                            @SuppressWarnings("unchecked")
                            Entity projectile = entity;
                            return (Entity & IProjectile) projectile;
                        } else {
                            throw new RuntimeException("Entity " + entityClassName + " is not a valid projectile entity (must implement IProjectile)!");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Could not instantiate projectile entity " + entityClassName + "!", e);
                    }
                }
        );

        return spell;
    }
}
