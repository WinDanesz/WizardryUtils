package com.windanesz.wizardryutils.item;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.item.ItemSpellBook;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryTabs;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellProperties;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class ItemDustySpellBook extends Item {

	public ItemDustySpellBook() {
		super();
		setMaxStackSize(1);
		setCreativeTab(WizardryTabs.WIZARDRY);
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

		if (!world.isRemote) {

			List<Item> bookTypeList = ForgeRegistries.ITEMS.getValuesCollection().stream().filter(i -> i instanceof ItemSpellBook).collect(Collectors.toList());
			Spell newSpell = getRandomSpell();

			for (int i = 0; i < bookTypeList.size(); i++) {
				Item currentBook = bookTypeList.get(i);
				if (newSpell.applicableForItem(currentBook)) {
					return new ItemStack(currentBook, 1, newSpell.metadata());
				}
			}
		}

		return super.onItemUseFinish(stack, world, entityLiving);
	}

	private static Spell getRandomSpell() {
		List<Spell> spells = Spell.getSpells(new Spell.TierElementFilter(null, null, SpellProperties.Context.BOOK));
		spells.removeIf((new Spell.TierElementFilter(null, null, SpellProperties.Context.LOOTING)).negate());

		if (spells.size() != 0) {
			return spells.get(itemRand.nextInt(spells.size()));
		}

		return Spells.none;
	}
}
