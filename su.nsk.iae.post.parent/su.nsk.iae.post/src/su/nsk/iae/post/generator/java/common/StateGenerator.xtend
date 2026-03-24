package su.nsk.iae.post.generator.java.common

import su.nsk.iae.post.poST.State
import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.statement.StatementListGenerator
import su.nsk.iae.post.generator.java.common.statement.TimeoutStatementGenerator

class StateGenerator {

    val StatementListGenerator stmtGen

    new(StatementListGenerator stmtGen) {
        this.stmtGen = stmtGen
    }

    // ãåíåğèğóåò case âåòêó äëÿ switch(state)
    def String generate(State state, GenerationContext ctx, String indent) {

        val builder = new StringBuilder
        val nextIndent = indent + "    "

	builder.append(
'''
«indent»case «state.name» -> {
«stmtGen.generate(state.statement, ctx, nextIndent)»
'''
	)

	if (state.timeout !== null) {
	    builder.append(
	        new TimeoutStatementGenerator(stmtGen)
	            .generateTimeout(state.timeout, ctx, nextIndent)
	    )
	}

	builder.append(
'''
«indent»}
'''
	)

        builder.toString
    }

}