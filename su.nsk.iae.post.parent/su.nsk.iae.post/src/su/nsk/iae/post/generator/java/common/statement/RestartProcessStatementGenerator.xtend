package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.StartProcessStatement

import su.nsk.iae.post.generator.java.common.context.GenerationContext

class RestartProcessStatementGenerator implements IStatementGenerator {

	override supports(Statement stmt) {
		stmt instanceof StartProcessStatement &&
		(stmt as StartProcessStatement).process === null
	}

	override generate(Statement stmt, GenerationContext ctx, String indent) {

		'''
«indent»this.start();
'''
	}
}