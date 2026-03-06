package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.SubprogramControlStatement

import su.nsk.iae.post.generator.java.common.context.GenerationContext

class ReturnStatementGenerator implements IStatementGenerator {

	// Проверяет, может ли генератор обработать данный оператор
	override supports(Statement stmt) {
		stmt instanceof SubprogramControlStatement
	}

	// Генерирует Java-код для RETURN
	override generate(Statement stmt, GenerationContext ctx, String indent) {

		'''
«indent»return;
'''
	}
}