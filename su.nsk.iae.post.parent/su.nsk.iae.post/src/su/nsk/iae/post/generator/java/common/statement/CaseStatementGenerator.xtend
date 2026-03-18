package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.CaseStatement
import su.nsk.iae.post.poST.CaseElement
import su.nsk.iae.post.poST.CaseListElement
import su.nsk.iae.post.generator.java.common.context.GenerationContext
import static extension su.nsk.iae.post.generator.java.common.util.ExpressionGenerator.*
import static extension su.nsk.iae.post.generator.java.common.util.TypeUtil.*

class CaseStatementGenerator implements IStatementGenerator {

	val StatementListGenerator stmtGen
	
	new(StatementListGenerator stmtGen) {
        this.stmtGen = stmtGen
    }

	// Ïğîâåğÿåò, ìîæåò ëè ãåíåğàòîğ îáğàáîòàòü äàííûé îïåğàòîğ
	override supports(Statement stmt) {
		stmt instanceof CaseStatement
	}

	// Ãåíåğèğóåò Java-êîä äëÿ CASE
	override generate(Statement stmt, GenerationContext ctx, String indent) {

		val s = stmt as CaseStatement
		val builder = new StringBuilder

		val condExpr = generate(s.cond, ctx)
		val caseType = getExprType(s.cond, ctx)
		val nextIndent = indent + "    "

		builder.append(
'''
«indent»final Object __caseVal = «condExpr»;
'''
		)

		var boolean first = true

		for (CaseElement el : s.caseElements) {

			val cond = generateCaseCondition(el, ctx, caseType)

			if (first) {
				builder.append(
'''
«indent»if («cond») {
«stmtGen.generate(el.statement, ctx, nextIndent)»
«indent»}
'''
				)
				first = false
			} else {
				builder.append(
'''
«indent»else if («cond») {
«stmtGen.generate(el.statement, ctx, nextIndent)»
«indent»}
'''
				)
			}
		}

		// ===== ELSE =====
		if (s.elseStatement !== null) {

			builder.append(
'''
«indent»else {
«stmtGen.generate(s.elseStatement, ctx, nextIndent)»
«indent»}
'''
			)
		}

		builder.toString
	}

	// Ãåíåğèğóåò óñëîâèå äëÿ case-âåòêè
	private def String generateCaseCondition(
	    CaseElement el,
	    GenerationContext ctx,
	    String caseType
	) {
	
	    val parts = newArrayList
	
	    for (CaseListElement e : el.caseList.caseListElement) {
	
	        var String valueExpr
	
	        // ===== literal =====
	        if (e.num !== null) {
	
	            val v =
	                if (e.num.ISig)
	                    "-" + e.num.value
	                else
	                    e.num.value
	
	            valueExpr = v
	        }
	
	        // ===== variable =====
	        else if (e.variable !== null) {
	
	            val name = e.variable.name
				val resolved = ctx.resolveAlias(name)
				
				if (ctx.hasConst(resolved)) {
				    valueExpr = ctx.getConst(resolved).toString
				} else {
				    valueExpr = readVar(name, ctx)
				}
	        }
	        
	        if (valueExpr === null)
		        throw new IllegalStateException(
		            "Unsupported CASE element: " + e
		        )
	
	        // ===== comparison =====
	
	        if (caseType == "REAL" || caseType == "LREAL") {

			    parts.add(
			        '''((Number)(__caseVal)).doubleValue() == ((Number)(«valueExpr»)).doubleValue()'''
			    )
			
			} else if (caseType.isNumeric) {
			
			    parts.add(
			        '''((Number)(__caseVal)).longValue() == ((Number)(«valueExpr»)).longValue()'''
			    )
			} else if (caseType == "BOOL") {
	
	            parts.add(
	                '''((Boolean)__caseVal) == «valueExpr»'''
	            )
	
	        } else if (caseType == "STRING" || caseType == "WSTRING") {
	
	            parts.add(
	                '''Objects.equals(__caseVal, «valueExpr»)'''
	            )
	
	        } else {
	
	            throw new IllegalStateException(
	                "Unsupported CASE type: " + caseType
	            )
	        }
	    }
	
	    if (parts.empty)
	        "false"
	    else
	        parts.join(" || ")
	}
}