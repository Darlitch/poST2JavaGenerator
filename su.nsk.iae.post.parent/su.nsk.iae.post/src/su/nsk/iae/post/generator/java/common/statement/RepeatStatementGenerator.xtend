package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.RepeatStatement

import su.nsk.iae.post.generator.java.common.context.GenerationContext

import static extension su.nsk.iae.post.generator.java.common.util.ExpressionGenerator.*

class RepeatStatementGenerator implements IStatementGenerator {

	val StatementListGenerator stmtGen
	
	new(StatementListGenerator stmtGen) {
        this.stmtGen = stmtGen
    }

	// Проверяет, может ли генератор обработать данный оператор
	override supports(Statement stmt) {
		stmt instanceof RepeatStatement
	}

	// Генерирует Java-код для REPEAT ... UNTIL
	override generate(Statement stmt, GenerationContext ctx, String indent) {

		val s = stmt as RepeatStatement
		val builder = new StringBuilder

		val nextIndent = indent + "    "

		// ===== условие =====
		val cond = generate(s.cond, ctx)

		builder.append(
'''
«indent»do {
«stmtGen.generate(s.statement, ctx, nextIndent)»
«indent»}
«indent»while (!(«cond»));
'''
		)

		builder.toString
	}
}