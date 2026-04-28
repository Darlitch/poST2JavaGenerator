package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.StopProcessStatement

import su.nsk.iae.post.generator.java.common.context.GenerationContext

class StopProcessStatementGenerator implements IStatementGenerator {

	override supports(Statement stmt) {
		stmt instanceof StopProcessStatement
	}

	override generate(Statement stmt, GenerationContext ctx, String indent) {

		val s = stmt as StopProcessStatement

		// ===== STOP PROCESS p =====
		if (s.process !== null) {

			val processName = s.process.name

			if (!ctx.hasProcess(processName))
			    throw new IllegalStateException(
			        "Unknown process in STOP PROCESS: " + processName
			    )
			
			val name = ctx.resolveProcess(processName)

			return '''
«indent»getProcess("«name»").stop();
'''
		}

		// ===== STOP =====
		'''
«indent»this.stop();
'''
	}
}