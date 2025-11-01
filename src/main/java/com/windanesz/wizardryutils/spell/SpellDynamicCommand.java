package com.windanesz.wizardryutils.spell;

import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Generic command spell that executes a Minecraft command when cast.
 * This allows dynamic creation of command spells via ZenScript.
 *
 * @author WinDanesz
 */
public class SpellDynamicCommand extends Spell {

	private final String command;

	public SpellDynamicCommand(String modID, String name, String command) {
		super(modID, name, SpellActions.POINT, false);
		this.command = command;
		this.npcSelector((e, o) -> true);
	}

	@Override
	public boolean requiresPacket() {
		return false;
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return true;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (!world.isRemote) {
			executeCommand(world, caster, caster.getPosition(), modifiers);
		}
		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (!world.isRemote) {
			BlockPos pos = target != null ? target.getPosition() : caster.getPosition();
			executeCommand(world, caster, pos, modifiers);
		}
		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers) {
		if (!world.isRemote) {
			executeCommand(world, null, new BlockPos(x, y, z), modifiers);
		}
		this.playSound(world, x - direction.getXOffset(), y - direction.getYOffset(), z - direction.getZOffset(), ticksInUse, duration, modifiers);
		return true;
	}

	private void executeCommand(World world, EntityLivingBase caster, BlockPos pos, SpellModifiers modifiers) {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (server == null) return;

		// Replace placeholders in command
		String processedCommand = command
				.replace("@caster", caster != null ? caster.getName() : "Dispenser")
				.replace("@x", String.valueOf(pos.getX()))
				.replace("@y", String.valueOf(pos.getY()))
				.replace("@z", String.valueOf(pos.getZ()))
				.replace("@dim", String.valueOf(world.provider.getDimension()));

		// Create command sender
		ICommandSender sender = caster != null ? caster : new ICommandSender() {
			@Override
			public String getName() {
				return "Spell";
			}

			@Override
			public boolean canUseCommand(int permLevel, String commandName) {
				return true;
			}

			@Override
			public BlockPos getPosition() {
				return pos;
			}

			@Override
			public World getEntityWorld() {
				return world;
			}

			@Override
			public MinecraftServer getServer() {
				return server;
			}
		};

		// Execute command
		try {
			server.getCommandManager().executeCommand(sender, processedCommand);
		} catch (Exception e) {
			if (caster instanceof EntityPlayer) {
				((EntityPlayer) caster).sendMessage(new TextComponentString("§cCommand failed: " + e.getMessage()));
			}
		}
	}
}
