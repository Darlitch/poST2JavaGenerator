package su.nsk.iae.post.generator.java.common.util

import su.nsk.iae.post.poST.Expression
import su.nsk.iae.post.poST.PrimaryExpression
import su.nsk.iae.post.poST.UnaryExpression
import su.nsk.iae.post.poST.PowerExpression
import su.nsk.iae.post.poST.MulExpression
import su.nsk.iae.post.poST.AddExpression
import su.nsk.iae.post.poST.EquExpression
import su.nsk.iae.post.poST.CompExpression
import su.nsk.iae.post.poST.AndExpression
import su.nsk.iae.post.poST.XorExpression
import su.nsk.iae.post.poST.AddOperator
import su.nsk.iae.post.poST.MulOperator
import su.nsk.iae.post.poST.EquOperator
import su.nsk.iae.post.poST.CompOperator
import su.nsk.iae.post.poST.UnaryOperator
import su.nsk.iae.post.poST.Constant
import su.nsk.iae.post.poST.IntegerLiteral
import su.nsk.iae.post.poST.RealLiteral
import su.nsk.iae.post.poST.ProcessStatusExpression

import su.nsk.iae.post.generator.java.common.context.GenerationContext

import static extension su.nsk.iae.post.generator.java.common.util.MemoryUtil.*
import static extension su.nsk.iae.post.generator.java.common.util.TypeUtil.*

class ExpressionGenerator {

	def static String generate(Expression exp, GenerationContext ctx) {
		switch exp {

			PrimaryExpression:
				generatePrimary(exp, ctx)

			UnaryExpression:
				generateUnary(exp, ctx)

			PowerExpression:
				generatePower(exp, ctx)

			MulExpression: {
			    val t1 = requireType(exp.left, ctx)
			    val t2 = requireType(exp.right, ctx)
			
			    promoteNumeric(t1, t2) // validate types
			
			    if (exp.mulOp == MulOperator.DIV) {

			        '''((double)(«generate(exp.left, ctx)») / (double)(«generate(exp.right, ctx)»))'''
			
			    } else {
			
			        '''(«generate(exp.left, ctx)» «mulOp(exp.mulOp)» «generate(exp.right, ctx)»)'''
			
			    }
			}

			AddExpression: {
			    val t1 = requireType(exp.left, ctx)
			    val t2 = requireType(exp.right, ctx)
			
			    promoteNumeric(t1, t2) // validate types
			
			    '''(«generate(exp.left, ctx)» «addOp(exp.addOp)» «generate(exp.right, ctx)»)'''
			}

			EquExpression:
				generateComparison(exp.left, exp.right, exp.equOp, ctx)

			CompExpression:
				generateComparison(exp.left, exp.right, exp.compOp, ctx)

			AndExpression: 
				generateLogicalOrBitwise(exp.left, exp.right, "&&", "&", ctx, "AND")

			XorExpression: 
				generateLogicalOrBitwise(exp.left, exp.right, "^", "^", ctx, "XOR")

			Expression: 
				generateLogicalOrBitwise(exp.left, exp.right, "||", "|", ctx, "OR")
		}
	}

	// ================= TYPE DETECTION =================

	def static String getExprType(Expression exp, GenerationContext ctx) {

	    switch exp {
	
	        // ================= PRIMARY =================
	
	        PrimaryExpression: {
	
	            // ----- variable -----
	            if (exp.variable !== null) {

				    val name = exp.variable.name
				    val resolved = ctx.resolveAlias(name)
				
				    if (ctx.hasConst(resolved)) {
				        val value = ctx.getConst(resolved)
				
				        return switch value {
				            Boolean: "BOOL"
				            Integer: "INT"
				            Long: "LINT"
				            Double: "LREAL"
				            Float: "REAL"
				            String: "STRING"
				            default: throw new IllegalStateException(
				                "Unsupported const type: " + value
				            )
				        }
				    }
				
				    return ctx.resolveVarType(resolved)
				}
	
	            // ----- array access -----
	            if (exp.array !== null) {
	                val arrName = ctx.resolveAlias(exp.array.variable.name)
	                return ctx.getArrayElementType(arrName)
	            }
	            
	            // PROCESS STATUS
			    if (exp.procStatus !== null)
			        return "BOOL"
				
	            // ----- constant -----
	            if (exp.const !== null) {
	
	                val c = exp.const
	
	                // INTEGER
	                if (c.num instanceof IntegerLiteral) {
	                    val lit = c.num as IntegerLiteral
	                    return if (lit.type !== null) lit.type else "INT"
	                }
	
	                // REAL
	                if (c.num instanceof RealLiteral) {
	                    val lit = c.num as RealLiteral
	                    return if (lit.type !== null) lit.type else "LREAL"
	                }
	
	                // TIME
	                if (c.time !== null)
	                    return "TIME"
	
	                // BOOLEAN
	                if (c.oth == "TRUE" || c.oth == "FALSE")
	                    return "BOOL"
	
	                // BIT literals (2#, 8#, 16#)
	                if (c.oth !== null && (
	                        c.oth.startsWith("2#") ||
	                        c.oth.startsWith("8#") ||
	                        c.oth.startsWith("16#")
	                    ))
	                    return "INT"
	            }
	
	            // nested expression
	            if (exp.nestExpr !== null)
	                return getExprType(exp.nestExpr, ctx)
	
	            throw new IllegalStateException("Unknown primary expression: " + exp)
	        }
	
	        // ================= UNARY =================
	
	        UnaryExpression: {
	
	            val t = getExprType(exp.right, ctx)
	
	            switch exp.unOp {
	
	                case UnaryOperator.NOT: {

					    if (t == "BOOL")
					        return t
					
					    if (t.numericRank > 0 && t != "REAL" && t != "LREAL")
					        return t
					
					    throw new IllegalStateException(
					        "NOT applied to invalid type: " + t
					    )
					}
	
	                case UnaryOperator.UNMINUS: {
	                    if (!t.isNumeric)
	                        throw new IllegalStateException(
	                            "Unary minus applied to non-numeric type: " + t
	                        )
	                    return t
	                }
	            }
	        }
	
	        // ================= POWER =================
	
	        PowerExpression: {
	            val t1 = getExprType(exp.left, ctx)
	            val t2 = getExprType(exp.right, ctx)
	
	            if (t1 == "TIME" || t2 == "TIME")
	                throw new IllegalStateException(
	                    "POWER operator not allowed for TIME"
	                )
	
	            return promoteNumeric(t1, t2)
	        }
	
	        // ================= MUL =================
	
	        MulExpression: {
	            val t1 = getExprType(exp.left, ctx)
	            val t2 = getExprType(exp.right, ctx)
	            return promoteNumeric(t1, t2)
	        }
	
	        // ================= ADD =================
	
	        AddExpression: {
	            val t1 = getExprType(exp.left, ctx)
	            val t2 = getExprType(exp.right, ctx)
	            return promoteNumeric(t1, t2)
	        }
	
	        // ================= COMPARISON (=, <>, <, >, <=, >=) =================
	
	        EquExpression,
	        CompExpression:
	            return "BOOL"
	
	        // ================= AND =================
	
	        AndExpression: {

			    val t1 = getExprType(exp.left, ctx)
			    val t2 = getExprType(exp.right, ctx)
			
			    // BOOL && BOOL
			    if (t1 == "BOOL" && t2 == "BOOL")
			        return "BOOL"
			
			    // bitwise
			    if (canBitwise(t1, t2))
			        return promoteNumeric(t1, t2)
			
			    throw new IllegalStateException(
			        "AND not allowed for types: " + t1 + ", " + t2
			    )
			}
	
	        // ================= XOR =================
	
	        XorExpression: {

			    val t1 = getExprType(exp.left, ctx)
			    val t2 = getExprType(exp.right, ctx)
			
			    if (t1 == "BOOL" && t2 == "BOOL")
			        return "BOOL"
			
			    if (canBitwise(t1, t2))
			        return promoteNumeric(t1, t2)
			
			    throw new IllegalStateException(
			        "XOR not allowed for types: " + t1 + ", " + t2
			    )
			}
	
	        // ================= OR =================
	
	        Expression: {

			    val t1 = getExprType(exp.left, ctx)
			    val t2 = getExprType(exp.right, ctx)
			
			    if (t1 == "BOOL" && t2 == "BOOL")
			        return "BOOL"
			
			    if (canBitwise(t1, t2))
			        return promoteNumeric(t1, t2)
			
			    throw new IllegalStateException(
			        "OR not allowed for types: " + t1 + ", " + t2
			    )
			}
	
	        default:
	            throw new IllegalStateException(
	                "Unsupported expression type: " + exp
	            )
	    }
	}

	// ================= POWER WITH PROMOTION =================

	private def static String generatePower(PowerExpression exp, GenerationContext ctx) {

		val leftType = requireType(exp.left, ctx)
		val rightType = requireType(exp.right, ctx)
		
		// ===== запрет TIME =====
		if (leftType == "TIME" || rightType == "TIME")
			throw new IllegalStateException(
				"POWER operator (**) is not allowed for TIME type"
			)
		
		val resultType = promoteNumeric(leftType, rightType)

		val pow = '''Math.pow(
			(double)«generate(exp.left, ctx)»,
			(double)«generate(exp.right, ctx)»
		)'''

		switch resultType {

			case "LREAL":
				pow

			case "REAL":
				'''(float)«pow»'''

			case #["LINT","ULINT","LWORD"]:
				'''(long)«pow»'''

			default:
				'''(int)«pow»'''
		}
	}

	// ================= STRING EQUALITY =================

	private def static String generateComparison(
	    Expression leftExp,
	    Expression rightExp,
	    Object op,
	    GenerationContext ctx
	) {
	
	    val leftType = getExprType(leftExp, ctx)
	    val rightType = getExprType(rightExp, ctx)
	
	    // ===== TYPE VALIDATION =====
	
	    if (leftType != rightType) {
	
	        // numeric comparisons allowed
	        if (leftType.isNumeric && rightType.isNumeric) {
	            // ok
	        }
	
	        // STRING <-> WSTRING allowed
	        else if (
	            (leftType == "STRING" || leftType == "WSTRING") &&
	            (rightType == "STRING" || rightType == "WSTRING")
	        ) {
	            // ok
	        }
	
	        else {
	            throw new IllegalStateException(
	                "Cannot compare types: " + leftType + " and " + rightType
	            )
	        }
	    }
	
	    val left = generate(leftExp, ctx)
	    val right = generate(rightExp, ctx)
	
	    // ===== STRING COMPARISON =====
	
	    if (leftType == "STRING" || leftType == "WSTRING") {
	
	        if (op == CompOperator.EQUAL)
	            return '''Objects.equals(«left», «right»)'''
	
	        if (op == CompOperator.NOT_EQUAL)
	            return '''!Objects.equals(«left», «right»)'''
	
	        throw new IllegalStateException(
	            "Ordering comparison not supported for STRING type"
	        )
	    }
	
	    // ===== NUMERIC COMPARISON =====
	
	    if (leftType.isNumeric && rightType.isNumeric) {
	
	        val leftVal  = '''((double)(«left»))'''
	        val rightVal = '''((double)(«right»))'''
	
	        val operator =
	            if (op instanceof CompOperator) {
	                if (op == CompOperator.EQUAL) "==" else "!="
	            } else {
	                equOp(op as EquOperator)
	            }
	
	        return '''(«leftVal» «operator» «rightVal»)'''
	    }
	
	    // ===== BOOLEAN =====
	
	    if (leftType == "BOOL") {
	
	        val operator =
	            if (op instanceof CompOperator) {
	                if (op == CompOperator.EQUAL) "==" else "!="
	            } else {
	                throw new IllegalStateException(
	                    "Ordering comparison not allowed for BOOL"
	                )
	            }
	
	        return '''(«left» «operator» «right»)'''
	    }
	
	    throw new IllegalStateException(
	        "Unsupported comparison: " + leftType + " " + op + " " + rightType
	    )
	}

	// ================= PRIMARY =================

	private def static String generatePrimary(PrimaryExpression exp, GenerationContext ctx) {

		if (exp.const !== null)
			return generateConstant(exp.const)

		if (exp.variable !== null) {

		    val name = exp.variable.name
		
		    // ===== compile-time constant =====
		    val resolved = ctx.resolveAlias(name)
		    if (ctx.hasConst(resolved)) {
		    	val value = ctx.getConst(resolved)

				if (value instanceof String)
				    return "\"" + value.replace("\"", "\\\"") + "\""
				
				return value.toString
		    }
		
		    // ===== обычная переменная =====
		    return readVar(name, ctx)
		}

		if (exp.array !== null) {

		    val arrName = ctx.resolveAlias(exp.array.variable.name)
		    val indexExpr = generate(exp.array.index, ctx)
		
		    val type = ctx.getArrayElementType(arrName)
		    val javaType = type.javaType
			val start = ctx.getArrayStart(arrName)
	
			return '''((«javaType») getArrayValue("«arrName»", «indexExpr», «start»))'''
		}	

		if (exp.procStatus !== null)
			return generateProcessStatus(exp.procStatus, ctx)

		if (exp.funCall !== null)
			return '''«exp.funCall.function.name»()'''

		'''(«generate(exp.nestExpr, ctx)»)'''
	}

	def static String readVar(String name, GenerationContext ctx) {
		val resolved = ctx.resolveAlias(name)
		if (ctx.hasConst(name))
    		return ctx.getConst(name).toString
		if (ctx.hasConst(resolved)) {
	        val value = ctx.getConst(resolved)
	        if (value instanceof String) {
	            return '''"«value»"'''
	        }
	        return value.toString
	    }
		var resolved2 = ctx.resolveVarName(name)
		val javaType = ctx.resolveVarType(name).javaType

		'''((«javaType»)memory.get("«resolved2»"))'''
	}
	
	// ================= VARIABLE WRITE =================

	// генерирует запись в ячейку памяти
	def static String writeVar(String name, String valueExpr, GenerationContext ctx) {
	
		val resolved = ctx.resolveVarName(name)
	
		'''memory.put("«resolved»", «valueExpr»);'''
	}

	// ================= UNARY =================

	private def static String generateUnary(UnaryExpression exp, GenerationContext ctx) {
		switch exp.unOp {
			case UnaryOperator.NOT: {
			
			    val t = requireType(exp.right, ctx)
			
			    // BOOL
			    if (t == "BOOL")
			        return '''!(«generate(exp.right, ctx)»)'''
			
			    // BIT STRING / INTEGER
			    if (isBitwiseInteger(t))
			        return '''~(«generate(exp.right, ctx)»)'''
			
			    throw new IllegalStateException(
			        "NOT not allowed for type: " + t
			    )
			}
			case UnaryOperator.UNMINUS: {
			    val type = requireType(exp.right, ctx)
			
			    if (!type.isNumeric)
			        throw new IllegalStateException(
			            "Unary minus applied to non-numeric type: " + type
			        )
			
			    '''-(«generate(exp.right, ctx)»)'''
			}
		}
	}

	// ================= CONSTANT =================

	private def static String generateConstant(Constant c) {

		if (c.num !== null) {

			if (c.num instanceof IntegerLiteral) {
				val lit = c.num as IntegerLiteral
				val signed = lit.value
				val sign = if (signed.ISig) "-" else ""
				return sign + signed.value
			}

			if (c.num instanceof RealLiteral) {
			    val lit = c.num as RealLiteral
			    val sign = if (lit.isRSig) "-" else ""
			    return sign + lit.value
			}
		}

		if (c.time !== null)
			return parseTime(c.time)

		if (c.oth == "TRUE")
			return "true"

		if (c.oth == "FALSE")
			return "false"

		c.oth
	}

	// ================= PROCESS STATUS =================

	private def static String generateProcessStatus(
		ProcessStatusExpression exp,
		GenerationContext ctx
	) {
	
		val fieldName = ctx.resolveProcess(exp.process.name)

	    if (exp.active)
	        return '''isActive(«fieldName»)'''
	
	    if (exp.inactive)
	        return '''isInactive(«fieldName»)'''
	
	    if (exp.stop)
	        return '''isStop(«fieldName»)'''
	
	    '''isError(«fieldName»)'''
	}

	// ================= OPERATORS =================

	private def static String addOp(AddOperator op) {
		if (op == AddOperator.PLUS) "+" else "-"
	}

	private def static String mulOp(MulOperator op) {
		switch op {
			case MulOperator.MUL: "*"
			case MulOperator.DIV: "/"
			case MulOperator.MOD: "%"
		}
	}

	private def static String equOp(EquOperator op) {
		switch op {
			case EquOperator.LESS: "<"
			case EquOperator.LESS_EQU: "<="
			case EquOperator.GREATER: ">"
			case EquOperator.GREATER_EQU: ">="
		}
	}
	
	private def static String requireType(Expression exp, GenerationContext ctx) {
	    val type = getExprType(exp, ctx)
	    if (type === null)
	        throw new IllegalStateException(
	            "Cannot determine type of expression: " + exp
	        )
	    type
	}
	
	private def static String generateLogicalOrBitwise(
	    Expression left,
	    Expression right,
	    String boolOp,
	    String bitOp,
	    GenerationContext ctx,
	    String opName
	) {
	
	    val t1 = requireType(left, ctx)
	    val t2 = requireType(right, ctx)
	
	    // BOOL operation
	    if (t1 == "BOOL" && t2 == "BOOL")
	        return '''(«generate(left, ctx)» «boolOp» «generate(right, ctx)»)'''
	
	    // bitwise operation
	    if (canBitwise(t1, t2))
	        return '''(«generate(left, ctx)» «bitOp» «generate(right, ctx)»)'''
	
	    throw new IllegalStateException(
	        opName + " not allowed for types: " + t1 + ", " + t2
	    )
	}
}