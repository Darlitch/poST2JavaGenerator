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

	// Ïğîâåğÿåò, ìîæåò ëè ãåíåğàòîğ îáğàáîòàòü äàííûé îïåğàòîğ
	override supports(Statement stmt) {
		stmt instanceof ForStatement
	}

	// Ãåíåğèğóåò Java-êîä äëÿ FOR
	override generate(Statement stmt, GenerationContext ctx, String indent) {

		val s = stmt as ForStatement
		val builder = new StringBuilder

		val nextIndent = indent + "    "

		// ===== ïåğåìåííàÿ öèêëà =====
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

«indent»writeVar("«resolved»", __start);

«indent»while (loopCond("«resolved»", __end, __step)) {
«stmtGen.generate(s.statement, ctx, nextIndent)»
«nextIndent»writeVar("«resolved»", «readVar(varName, ctx)» + __step);
«indent»}
'''
		)

		builder.toString
	}
}