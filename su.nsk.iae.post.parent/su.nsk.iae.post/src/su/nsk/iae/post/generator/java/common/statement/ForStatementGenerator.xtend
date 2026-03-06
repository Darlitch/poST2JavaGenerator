package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.ForStatement
import su.nsk.iae.post.poST.SymbolicVariable

import su.nsk.iae.post.generator.java.common.context.GenerationContext

import static extension su.nsk.iae.post.generator.java.common.util.ExpressionGenerator.*
import static extension su.nsk.iae.post.generator.java.common.util.TypeUtil.*

class ForStatementGenerator implements IStatementGenerator {

	val StatementListGenerator stmtGen
	
	new(StatementListGenerator stmtGen) {
        this.stmtGen = stmtGen
    }

	// Ïðîâåðÿåò, ìîæåò ëè ãåíåðàòîð îáðàáîòàòü äàííûé îïåðàòîð
	override supports(Statement stmt) {
		stmt instanceof ForStatement
	}

	// Ãåíåðèðóåò Java-êîä äëÿ FOR
	override generate(Statement stmt, GenerationContext ctx, String indent) {

		val s = stmt as ForStatement
		val builder = new StringBuilder

		val nextIndent = indent + "    "

		// ===== ïåðåìåííàÿ öèêëà =====
		val varName = (s.variable as SymbolicVariable).name
		val resolved = ctx.resolveVarName(varName)

		// ===== âû÷èñëÿåì start / end / step =====
		val startExpr = generate(s.forList.start, ctx)
		val endExpr   = generate(s.forList.end, ctx)

		val stepExpr =
			if (s.forList.step !== null)
				generate(s.forList.step, ctx)
			else
				"1"

		builder.append(
'''
«indent»int __start = «toInt(startExpr)»;
«indent»int __end   = «toInt(endExpr)»;
«indent»int __step  = «toInt(stepExpr)»;

«indent»if (__step == 0)
«indent»    throw new RuntimeException("FOR step cannot be zero");

«indent»memory.put("«resolved»", __start);

«indent»while (
«indent»       (__step >= 0 && «readVar(varName, ctx)» <= __end)
«indent»    || (__step < 0  && «readVar(varName, ctx)» >= __end)
«indent») {
«stmtGen.generate(s.statement, ctx, nextIndent)»
«nextIndent»memory.put(
«nextIndent»    "«resolved»",
«nextIndent»    «readVar(varName, ctx)» + __step
«nextIndent»);
«indent»}
'''
		)

		builder.toString
	}
}