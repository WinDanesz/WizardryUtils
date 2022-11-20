package com.windanesz.wizardryutils.item;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.registry.WizardryTabs;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemDustyArtefact extends Item {

	public final String type;

	public ItemDustyArtefact(String type) {
		super();
		setMaxStackSize(1);
		setCreativeTab(WizardryTabs.WIZARDRY);
		this.type = type;
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 20;
	}

	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BLOCK;
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(Wizardry.proxy.translate("item." + this.getRegistryName().toString() + ".desc"));
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {

		if (entityLiving instanceof EntityPlayer && !world.isRemote) {
			for (int i = 0; i < 60; i++) {

				LootTable table = world.getLootTableManager().getLootTableFromLocation(new ResourceLocation(Wizardry.MODID, "chests/shrine"));
				LootContext context = new LootContext.Builder((WorldServer) world).withPlayer((EntityPlayer) entityLiving).withLuck(0).build();

				List<ItemStack> artefact = new ArrayList<>();
				table.getPool("artefact").generateLoot(artefact, world.rand, context);

				String pattern = "^" + type +"_.*";
				if (!artefact.isEmpty() && artefact.get(0).getItem().getRegistryName().getPath().matches(pattern)) {
					return artefact.get(0);
				}
			}
		}

		return super.onItemUseFinish(stack, world, entityLiving);
	}
}
