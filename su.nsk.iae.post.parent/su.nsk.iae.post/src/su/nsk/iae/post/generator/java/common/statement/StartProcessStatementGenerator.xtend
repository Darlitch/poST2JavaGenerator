package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.StartProcessStatement

import su.nsk.iae.post.generator.java.common.context.GenerationContext

class StartProcessStatementGenerator implements IStatementGenerator {

	// Проверяет, может ли генератор обработать данный оператор
	override supports(Statement stmt) {
		stmt instanceof StartProcessStatement &&
		(stmt as StartProcessStatement).process !== null
	}

	// Генерирует Java-код для START PROCESS
	override generate(Statement stmt, GenerationContext ctx, String indent) {

		val s = stmt as StartProcessStatement

		// ===== имя процесса =====
		val name = s.process.name

		'''
«indent»getProcess("«name»").start();
'''
	}
}