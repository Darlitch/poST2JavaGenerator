package su.nsk.iae.post.generator.java.configuration

import su.nsk.iae.post.poST.ProgramConfiguration
import su.nsk.iae.post.poST.AttachVariableConfElement
import su.nsk.iae.post.poST.TemplateProcessAttachVariableConfElement
import su.nsk.iae.post.poST.TemplateProcessConfElement

import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.configuration.ProcessConfGenerator

class ProgramConfGenerator {

    def String generate(ProgramConfiguration conf, GenerationContext ctx, String indent) {

        val builder = new StringBuilder

        val instanceName = conf.name
        val programType = conf.program.name

        // ===== ñîçäàíèå ïðîãðàììû =====
        builder.append(
'''
«indent»this.«instanceName» = new «programType»(memory, processMap);
'''
        )

	    // ===== ñîçäàíèå ïðîöåññîâ =====
	    if (conf.args !== null) {
	    	val procGen = new ProcessConfGenerator
	
	        for (arg : conf.args.elements) {
			    if (arg instanceof TemplateProcessConfElement) {
			        builder.append(
			            procGen.generate(arg, ctx, instanceName, programType, indent)
			        )
			    }
			}
	    }

        builder.toString
    }

}