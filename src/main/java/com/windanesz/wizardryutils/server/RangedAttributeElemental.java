package com.windanesz.wizardryutils.server;

import electroblob.wizardry.constants.Element;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

import javax.annotation.Nullable;

public class RangedAttributeElemental extends RangedAttribute {

	public final Element element;
	public final String modifier;

	public RangedAttributeElemental(@Nullable
	IAttribute parentIn, String unlocalizedNameIn, double defaultValue, double minimumValueIn, double maximumValueIn, Element element, String modifier) {
		super(parentIn, unlocalizedNameIn, defaultValue, minimumValueIn, maximumValueIn);
		this.element = element;
		this.modifier = modifier;
	}
}
