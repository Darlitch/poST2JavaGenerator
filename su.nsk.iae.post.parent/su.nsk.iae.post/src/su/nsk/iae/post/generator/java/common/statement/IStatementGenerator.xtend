package su.nsk.iae.post.generator.java.common.statement

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.generator.java.common.context.GenerationContext

interface IStatementGenerator {

	// может ли этот генератор обработать данный statement
	def boolean supports(Statement stmt)
	
	// сгенерировать Java код
	def String generate(Statement stmt, GenerationContext ctx, String indent)

}