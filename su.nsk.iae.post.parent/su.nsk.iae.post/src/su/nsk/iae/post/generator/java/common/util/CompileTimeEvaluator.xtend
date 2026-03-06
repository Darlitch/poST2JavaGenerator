package su.nsk.iae.post.generator.java.common.util

import su.nsk.iae.post.poST.Expression
import su.nsk.iae.post.poST.PrimaryExpression
import su.nsk.iae.post.poST.SymbolicVariable
import su.nsk.iae.post.poST.IntegerLiteral
import su.nsk.iae.post.poST.Constant
import su.nsk.iae.post.poST.RealLiteral
import static extension su.nsk.iae.post.generator.java.common.util.MemoryUtil.*
import su.nsk.iae.post.generator.java.common.context.GenerationContext

class CompileTimeEvaluator {
	
	// UNIVERSAL LITERAL → JAVA
	def static Object eval(Constant c) {

		if (c.num !== null) {
	
			if (c.num instanceof IntegerLiteral) {
				val lit = c.num as IntegerLiteral
				val signed = lit.value
				val sign = if (signed.ISig) -1 else 1
				return sign * Integer.parseInt(signed.value.trim)
			}
	
			if (c.num instanceof RealLiteral) {
			    val lit = c.num as RealLiteral
			    val sign = if (lit.isRSig) -1.0 else 1.0
			    return sign * Double.parseDouble(lit.value)
			}
		}
	
		if (c.time !== null)
			return parseTimeValue(c.time)
	
		if (c.oth == "TRUE")
			return true
	
		if (c.oth == "FALSE")
			return false
	
		throw new IllegalStateException("Unsupported constant: " + c)
	}

	
	// COMPILE-TIME INTEGER ONLY
	def static int evalInt(Expression expr, GenerationContext ctx) {

		if (expr instanceof PrimaryExpression) {

			val pe = expr as PrimaryExpression

			// ===== numeric literal =====
			if (pe.const !== null) {

				val c = pe.const

				// обычное целое (5, -3, INT#5)
				if (c.num instanceof IntegerLiteral) {

					val lit = c.num as IntegerLiteral
					val signed = lit.value
				
					val sign =
						if (signed.ISig) -1 else 1
				
					return sign * Integer.parseInt(signed.value.trim)
				}

				// HEX: 16#FF
				if (c.oth !== null && c.oth.startsWith("16#"))
					return Integer.parseInt(c.oth.substring(3), 16)

				// BIN: 2#1010
				if (c.oth !== null && c.oth.startsWith("2#"))
					return Integer.parseInt(c.oth.substring(2), 2)

				// OCT: 8#77
				if (c.oth !== null && c.oth.startsWith("8#"))
					return Integer.parseInt(c.oth.substring(2), 8)
			}

			// ===== constant reference =====
			if (pe.variable instanceof SymbolicVariable) {

				val name = ctx.resolveAlias(pe.variable.name)

				if (ctx.hasConst(name))
					return (ctx.getConst(name) as Number).intValue
			}
		}

		throw new IllegalStateException(
			"Unsupported compile-time integer expression: " + expr
		)
	}
	
}