package com.windanesz.wizardryutils.capability;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.UUID;

public class SummonedThing implements INBTSerializable<NBTTagCompound> {

	public static final String SUMMONED_TAG = "summoned";
	public static final String CASTER_UUID_TAG = "casterUUID";
	public static final String LIFETIME_TAG = "lifetime";

	protected boolean summoned = false;

	// Field implementations
	protected int lifetime = -1;
	protected UUID casterUUID;
	protected UUID owner;

	@Override
	@SuppressWarnings("unchecked")
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		if (this.summoned) {

			nbt.setBoolean(SummonedItemCapability.SUMMONED_TAG, true);

			if (this.getOwnerId() != null) {
				NBTTagCompound casterUUID = new NBTTagCompound();
				casterUUID.setUniqueId(CASTER_UUID_TAG, this.getOwnerId());
				nbt.setTag(CASTER_UUID_TAG, casterUUID);
			}
			if (lifetime != -1) {
				nbt.setInteger(LIFETIME_TAG, getLifetime());
			}
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt != null && !nbt.isEmpty()) {

			// not really using this key since setOwnerId() and setLifetime() sets it to true anyways.
			if (nbt.hasKey(SummonedItemCapability.SUMMONED_TAG)) {
				if (nbt.hasKey(CASTER_UUID_TAG)) {
					this.setOwnerId(nbt.getCompoundTag(CASTER_UUID_TAG).getUniqueId(CASTER_UUID_TAG));
				}
				if (nbt.hasKey(LIFETIME_TAG)) {
					this.setLifetime(nbt.getInteger(LIFETIME_TAG));
				}
			}
		}
	}


	public int getLifetime() { return lifetime; }

	/**
	 * Sets the lifetime of this minion. Also handles marking it as a summoned entity if necessary.
	 *
	 * @param lifetime in ticks. Set to -1 to make the entity not despawn.
	 */
	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
		if (!summoned) { summoned = true; }
	}

	/**
	 * Sets the caster of this minion who has summoned it. Also handles marking it as a summoned entity if necessary.
	 *
	 * @param caster the owner (caster) of this entity.
	 */
	public void setCaster(@Nullable EntityLivingBase caster) {
		setOwnerId(caster == null ? null : caster.getUniqueID());
		if (!summoned) { summoned = true; }
	}

	public UUID getOwnerId() {
		return casterUUID;
	}

	public void setOwnerId(UUID uuid) {
		this.casterUUID = uuid;
	}

}
