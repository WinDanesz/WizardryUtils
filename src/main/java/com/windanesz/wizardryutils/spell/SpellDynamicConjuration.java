package com.windanesz.wizardryutils.spell;

import com.windanesz.wizardryutils.capability.SummonedItemCapability;
import electroblob.wizardry.item.IConjuredItem;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.ParticleBuilder.Type;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class SpellDynamicConjuration extends Spell {

	public static final String ITEM_LIFETIME = "item_lifetime";

	/** The item that is conjured by this spell. Should implement {@link IConjuredItem}. */
	protected final Item item;

	public SpellDynamicConjuration(String modID, String name, Item item){
		super(modID, name, SpellActions.IMBUE, false);
		this.item = item;
		addProperties(ITEM_LIFETIME);
		SummonedItemCapability.ITEMS_TO_APPLY_TO.add(item);
	}
	
	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers){

		if(conjureItem(caster, modifiers)){
			
			if(world.isRemote) spawnParticles(world, caster, modifiers);
			
			this.playSound(world, caster, ticksInUse, -1, modifiers);
			return true;
		}
		
		return false;
	}
	
	/** Spawns sparkle particles around the caster. Override to add a custom particle effect. Only called client-side. */
	protected void spawnParticles(World world, EntityLivingBase caster, SpellModifiers modifiers){
		
		for(int i=0; i<10; i++){
			double x = caster.posX + world.rand.nextDouble() * 2 - 1;
			double y = caster.posY + caster.getEyeHeight() - 0.5 + world.rand.nextDouble();
			double z = caster.posZ + world.rand.nextDouble() * 2 - 1;
			ParticleBuilder.create(Type.SPARKLE).pos(x, y, z).vel(0, 0.1, 0).clr(0.7f, 0.9f, 1).spawn(world);
		}
	}
	
	/** Adds this spell's item to the given player's inventory, placing it in the main hand if the main hand is empty.
	 * Returns true if the item was successfully added to the player's inventory, false if there as no space or if the
	 * player already had the item. Override to add special conjuring behaviour. */
	protected boolean conjureItem(EntityPlayer caster, SpellModifiers modifiers){

		ItemStack stack = new ItemStack(item);

		//IConjuredItem.setDurationMultiplier(stack, modifiers.get(WizardryItems.duration_upgrade));
		//IConjuredItem.setDamageMultiplier(stack, modifiers.get(SpellModifiers.POTENCY));

		stack = addItemExtras(caster, stack, modifiers);

		SummonedItemCapability caps = SummonedItemCapability.get(stack);
		caps.setLifetime(getProperty(ITEM_LIFETIME).intValue());
		caps.setOwnerId(caster.getUniqueID());
		setConjuredName(stack);

		if(caster.getHeldItemMainhand().isEmpty()){
			caster.setHeldItem(EnumHand.MAIN_HAND, stack);
		}else if (caster.getHeldItemOffhand().isEmpty()){
			caster.setHeldItem(EnumHand.OFF_HAND, stack);
		} else {
			if (!caster.world.isRemote) {
				caster.sendStatusMessage(new TextComponentTranslation("message.wizardryutils:must_summon_with_empty_hand"), false);
			}
			return false;
		}

		return true;
	}

	/**
	 * Called directly <i>before</i> the conjured item is added to the inventory to perform additional behaviour (such
	 * as NBT modification). Does nothing by default.
	 * @param caster The player that cast this spell
	 * @param stack The item stack being conjured
	 * @param modifiers The modifiers this spell was cast with
	 */
	protected ItemStack addItemExtras(EntityPlayer caster, ItemStack stack, SpellModifiers modifiers){ return stack; }

	public ItemStack setConjuredName(ItemStack stack) {
		NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		NBTTagCompound display = new NBTTagCompound();
		if (nbt.hasKey("display")) {
			display = nbt.getCompoundTag("display");
		}
		display.setString("Name", "Bound " + stack.getDisplayName());
		nbt.setTag("display", display);
		stack.setTagCompound(nbt);

		return stack;
	}

	public Item getItem() {
		return item;
	}
}
