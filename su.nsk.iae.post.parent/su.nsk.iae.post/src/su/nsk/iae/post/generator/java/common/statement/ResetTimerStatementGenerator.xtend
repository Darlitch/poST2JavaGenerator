package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.ResetTimerStatement

import su.nsk.iae.post.generator.java.common.context.GenerationContext
import static extension su.nsk.iae.post.generator.java.common.util.MemoryUtil.*

class ResetTimerStatementGenerator implements IStatementGenerator {

	// Проверяет, может ли генератор обработать данный оператор
	override supports(Statement stmt) {
		stmt instanceof ResetTimerStatement
	}

	// Генерирует Java-код для RESET TIMER
	override generate(Statement stmt, GenerationContext ctx, String indent) {

		'''
«indent»«timerField()» = ((Long)memory.get("«globalTime()»"));
'''
	}
}