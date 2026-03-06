package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.ErrorProcessStatement

import su.nsk.iae.post.generator.java.common.context.GenerationContext

class ErrorProcessStatementGenerator implements IStatementGenerator {

	override supports(Statement stmt) {
		stmt instanceof ErrorProcessStatement
	}

	override generate(Statement stmt, GenerationContext ctx, String indent) {

		val s = stmt as ErrorProcessStatement

		// ===== ERROR PROCESS p =====
		if (s.process !== null) {

			val fieldName = ctx.resolveProcess(s.process.name)

			return '''
«indent»«fieldName».error();
'''
		}

		// ===== ERROR =====
		'''
«indent»this.error();
'''
	}
}