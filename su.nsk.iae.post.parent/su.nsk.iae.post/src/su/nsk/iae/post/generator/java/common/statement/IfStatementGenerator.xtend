package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.IfStatement
import su.nsk.iae.post.generator.java.common.context.GenerationContext

import static extension su.nsk.iae.post.generator.java.common.util.ExpressionGenerator.*

class IfStatementGenerator implements IStatementGenerator {

	val StatementListGenerator stmtGen
	
	new(StatementListGenerator stmtGen) {
        this.stmtGen = stmtGen
    }

	// Ïğîâåğÿåò, ìîæåò ëè ãåíåğàòîğ îáğàáîòàòü äàííûé îïåğàòîğ
	override supports(Statement stmt) {
		stmt instanceof IfStatement
	}

	// Ãåíåğèğóåò Java-êîä äëÿ IF / ELSIF / ELSE
	override generate(Statement stmt, GenerationContext ctx, String indent) {

		val s = stmt as IfStatement
		val builder = new StringBuilder

		val nextIndent = indent + "    "

		// ===== IF =====
		val cond = generate(s.mainCond, ctx)

		builder.append(
'''
«indent»if («cond») {
«stmtGen.generate(s.mainStatement, ctx, nextIndent)»
«indent»}
'''
		)

		// ===== ELSIF =====
		for (i : 0 ..< s.elseIfCond.size) {

			val elsifCond = generate(s.elseIfCond.get(i), ctx)
			val elsifStmt = s.elseIfStatements.get(i)

			builder.append(
'''
«indent»else if («elsifCond») {
«stmtGen.generate(elsifStmt, ctx, nextIndent)»
«indent»}
'''
			)
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
}