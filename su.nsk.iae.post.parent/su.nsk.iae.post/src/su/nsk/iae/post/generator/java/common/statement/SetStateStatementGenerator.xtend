package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.SetStateStatement

import su.nsk.iae.post.generator.java.common.context.GenerationContext

class SetStateStatementGenerator implements IStatementGenerator {

	// Проверяет, может ли генератор обработать данный оператор
	override supports(Statement stmt) {
		stmt instanceof SetStateStatement
	}

	// Генерирует Java-код для SET STATE / SET NEXT
	override generate(Statement stmt, GenerationContext ctx, String indent) {

		val s = stmt as SetStateStatement

		// ===== SET NEXT =====
		if (s.next) {
			return '''
«indent»setNext();
'''
		}

		// ===== SET STATE =====
		val stateName = s.state.name

		'''
«indent»setState(State.«stateName»);
'''
	}
}