package com.windanesz.wizardryutils.integration.crafttweaker.brackethandlers;

import com.windanesz.wizardryutils.integration.crafttweaker.spell.ZenSpell;
import crafttweaker.annotations.BracketHandler;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.zenscript.GlobalRegistry;
import crafttweaker.zenscript.IBracketHandler;
import electroblob.wizardry.spell.Spell;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.type.natives.JavaMethod;

import java.util.List;
import java.util.stream.Collectors;

@BracketHandler
@ZenRegister
public class BracketHandlerSpell implements IBracketHandler {

	private static final IJavaMethod method = JavaMethod.get(GlobalRegistry.getTypes(), BracketHandlerSpell.class, "getSpell", String.class);

	public BracketHandlerSpell() {
	}

	public static ZenSpell getSpell(String name) {
		return new ZenSpell(Spell.get(name));
	}

	@Override
	public IZenSymbol resolve(IEnvironmentGlobal iEnvironmentGlobal, List<Token> list) {
		if (list.size() <= 2 || !list.get(0).getValue().equalsIgnoreCase("spell")) {
			return null;
		}
		return zenPosition -> new ExpressionCallStatic(zenPosition, iEnvironmentGlobal, method,
				new ExpressionString(zenPosition, list.subList(2, list.size()).stream().map(Token::getValue).collect(Collectors.joining())));
	}
}
