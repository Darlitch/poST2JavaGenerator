package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.ExitStatement

import su.nsk.iae.post.generator.java.common.context.GenerationContext

class ExitStatementGenerator implements IStatementGenerator {

	// Проверяет, может ли генератор обработать данный оператор
	override supports(Statement stmt) {
		stmt instanceof ExitStatement
	}

	// Генерирует Java-код для EXIT
	override generate(Statement stmt, GenerationContext ctx, String indent) {

		'''
«indent»break;
'''
	}
}