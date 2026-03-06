package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.TimeoutStatement

import su.nsk.iae.post.generator.java.common.context.GenerationContext

import static extension su.nsk.iae.post.generator.java.common.util.ExpressionGenerator.*
import static extension su.nsk.iae.post.generator.java.common.util.MemoryUtil.*

class TimeoutStatementGenerator implements IStatementGenerator {

	val StatementListGenerator stmtGen
	
	new(StatementListGenerator stmtGen) {
        this.stmtGen = stmtGen
    }

	// Ïðîâåðÿåò, ìîæåò ëè ãåíåðàòîð îáðàáîòàòü äàííûé îïåðàòîð
	override supports(Statement stmt) {
		stmt instanceof TimeoutStatement
	}

	// Ãåíåðèðóåò Java-êîä äëÿ TIMEOUT
	override generate(Statement stmt, GenerationContext ctx, String indent) {

		val s = stmt as TimeoutStatement
		val nextIndent = indent + "    "

		val timeoutExpr =
			if (s.const !== null)
				parseTime(s.const.time)
			else
				readVar(s.variable.name, ctx)

		'''
«indent»if (((Long)memory.get("«globalTime()»")) - this.«timerField()» >= «timeoutExpr») {
«stmtGen.generate(s.statement, ctx, nextIndent)»
«indent»}
'''
	}
}