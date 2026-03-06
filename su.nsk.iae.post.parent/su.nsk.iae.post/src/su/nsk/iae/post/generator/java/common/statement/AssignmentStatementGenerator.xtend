package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.AssignmentStatement
import su.nsk.iae.post.poST.SymbolicVariable
import su.nsk.iae.post.poST.ArrayVariable

import su.nsk.iae.post.generator.java.common.context.GenerationContext

import static extension su.nsk.iae.post.generator.java.common.util.ExpressionGenerator.*
import static extension su.nsk.iae.post.generator.java.common.util.TypeUtil.*

class AssignmentStatementGenerator implements IStatementGenerator {

	// Ïğîâåğÿåò, ìîæåò ëè ãåíåğàòîğ îáğàáîòàòü äàííûé îïåğàòîğ
	override supports(Statement stmt) {
		stmt instanceof AssignmentStatement
	}

	// Ãåíåğèğóåò Java-êîä äëÿ ïğèñâàèâàíèÿ
	override generate(Statement stmt, GenerationContext ctx, String indent) {

		val s = stmt as AssignmentStatement
		val builder = new StringBuilder

		// ===== çíà÷åíèå ïğàâîé ÷àñòè =====
		val valueExpr = generate(s.value, ctx)

		// ===== îáû÷íàÿ ïåğåìåííàÿ =====
		if (s.variable instanceof SymbolicVariable) {

			val name = (s.variable as SymbolicVariable).name
			
			val varType = ctx.resolveVarType(name)
			val exprType = getExprType(s.value, ctx)
			
			if (!canAssign(varType, exprType))
			    throw new IllegalStateException(
			        "Type mismatch in assignment: " +
			        varType + " := " + exprType
			    )

			builder.append(
				indent + writeVar(name, valueExpr, ctx)
			)

			return builder.toString
		}

		// ===== ıëåìåíò ìàññèâà =====
		if (s.variable instanceof ArrayVariable) {

			val arr = s.variable as ArrayVariable
			val arrName = ctx.resolveAlias(arr.variable.name)
			
			val elementType = ctx.getArrayElementType(arrName)
			val exprType = getExprType(s.value, ctx)
			
			if (!canAssign(elementType, exprType))
			    throw new IllegalStateException(
			        "Type mismatch in array assignment: " +
			        elementType + " := " + exprType
			    )

			val start = ctx.getArrayStart(arrName)

			val indexExpr = generate(arr.index, ctx)

			val nextIndent = indent + "    "

			builder.append(
'''
«indent»{
«nextIndent»int __idx = «toInt(indexExpr)»;
«nextIndent»int __offset = __idx - «start»;

«nextIndent»java.util.List<String> __list =
«nextIndent»    (java.util.List<String>) memory.get("«arrName»");

«nextIndent»if (__offset < 0 || __offset >= __list.size())
«nextIndent»    throw new RuntimeException(
«nextIndent»        "Array index out of bounds: «arrName»[" + __idx + "]"
«nextIndent»    );

«nextIndent»String __cell = __list.get(__offset);

«nextIndent»memory.put(__cell, «valueExpr»);
«indent»}
'''
			)

			return builder.toString
		}

		throw new IllegalStateException(
			"Unsupported assignment target: " + s.variable
		)
	}
}