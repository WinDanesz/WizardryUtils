package com.windanesz.wizardryutils.entity.ai;

import com.windanesz.wizardryutils.capability.SummonedCreatureData;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.EntityWizard;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.util.AllyDesignationSystem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;

public class EntityAIMinionOwnerHurtByTarget extends EntityAITarget {
	EntityLiving minion;
	EntityLivingBase attacker;
	EntityLivingBase owner;
	private int timestamp;

	public EntityAIMinionOwnerHurtByTarget(EntityCreature theDefendingMinion) {
		super(theDefendingMinion, false);
		this.minion = theDefendingMinion;
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		if (this.owner == null) {
			if (!SummonedCreatureData.isSummonedEntity(this.minion)) {
				return false;
			} else {
				SummonedCreatureData data = SummonedCreatureData.get(this.minion);
				this.owner = data.getCaster();
			}
		}

		if (this.owner != null) {
			this.attacker = owner.getRevengeTarget();
			int i = owner.getRevengeTimer();
			return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && isValidTarget(this.attacker);
		}
		return false;
	}

	public void startExecuting() {
		this.taskOwner.setAttackTarget(this.attacker);

		if (owner != null) {
			this.timestamp = owner.getLastAttackedEntityTime();
		}

		super.startExecuting();
	}

	/**
	 * Determines whether the given target is valid. Used by the default target selector (see
	 * {@link ISummonedCreature#getTargetSelector()}) and revenge targeting checks. This method is responsible for the
	 * ally designation system, default classes that may be targeted and the config whitelist/blacklist.
	 * Implementors may override this if they want to do something different or add their own checks.
	 *
	 * @see AllyDesignationSystem#isValidTarget(Entity, Entity)
	 */
	private boolean isValidTarget(Entity target) {
		// If the target is valid based on the ADS...
		if (AllyDesignationSystem.isValidTarget(this.owner, target)) {

			// ...and is a player, they can be attacked, since players can't be in the whitelist or the
			// blacklist...
			if (target instanceof EntityPlayer) {
				// ...unless the creature was summoned by a good wizard who the player has not angered.
				if (this.owner instanceof EntityWizard) {
					if (this.owner.getRevengeTarget() != target
							&& ((EntityWizard) this.owner).getAttackTarget() != target) {
						return false;
					}
				}

				return true;
			}

			// ...and is a mob, a summoned creature, a wizard...
			if ((target instanceof IMob || target instanceof ISummonedCreature
					|| (target instanceof EntityWizard && !(this.owner instanceof EntityWizard))
					// ...or something that's attacking the owner...
					|| (target instanceof EntityLiving && ((EntityLiving) target).getAttackTarget() == this.owner)
					// ...or in the whitelist...
					|| Arrays.asList(Wizardry.settings.summonedCreatureTargetsWhitelist)
					.contains(EntityList.getKey(target.getClass())))
					// ...and isn't in the blacklist...
					&& !Arrays.asList(Wizardry.settings.summonedCreatureTargetsBlacklist)
					.contains(EntityList.getKey(target.getClass()))) {
				// ...it can be attacked.
				return true;
			}
		}

		return false;
	}
}
