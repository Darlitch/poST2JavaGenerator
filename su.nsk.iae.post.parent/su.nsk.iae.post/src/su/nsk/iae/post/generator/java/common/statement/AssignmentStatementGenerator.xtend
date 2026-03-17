package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.AssignmentStatement
import su.nsk.iae.post.poST.SymbolicVariable
import su.nsk.iae.post.poST.ArrayVariable

import su.nsk.iae.post.generator.java.common.context.GenerationContext

import static extension su.nsk.iae.post.generator.java.common.util.ExpressionGenerator.*
import static extension su.nsk.iae.post.generator.java.common.util.TypeUtil.*

class AssignmentStatementGenerator implements IStatementGenerator {

	// Проверяет, может ли генератор обработать данный оператор
	override supports(Statement stmt) {
		stmt instanceof AssignmentStatement
	}

	// Генерирует Java-код для присваивания
	override generate(Statement stmt, GenerationContext ctx, String indent) {

		val s = stmt as AssignmentStatement
		val builder = new StringBuilder

		// ===== значение правой части =====
		val valueExpr = generate(s.value, ctx)

		// ===== обычная переменная =====
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

		// ===== элемент массива =====
		if (s.array !== null) {

    		val arr = s.array
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
			
			builder.append(
			    indent + '''setArrayValue("«arrName»", «indexExpr», «start», «valueExpr»);'''
			)

			return builder.toString
		}

		throw new IllegalStateException(
			"Unsupported assignment target: " + s.variable
		)
	}
}