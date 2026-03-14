package su.nsk.iae.post.generator.java.configuration

import su.nsk.iae.post.poST.Resource
import su.nsk.iae.post.poST.Task
import su.nsk.iae.post.poST.ProgramConfiguration
import su.nsk.iae.post.generator.java.common.vars.GlobalVarDeclarationGenerator

import su.nsk.iae.post.generator.java.common.context.GenerationContext

class ResourceGenerator {

    val TaskGenerator taskGen = new TaskGenerator
    val ProgramConfGenerator programGen = new ProgramConfGenerator

    def String generate(Resource resource, GenerationContext ctx) {

        val builder = new StringBuilder
        
        // ===== GLOBAL VARS =====
		for (g : resource.resGlobVars) {
	        GlobalVarDeclarationGenerator.generate(g, ctx)
		}

        val single = resource.resStatement

        // ===== TASK =====
        for (Task t : single.tasks) {
            builder.append(
                taskGen.generate(t)
            )
        }

        // ===== PROGRAM CONFIG =====
        for (ProgramConfiguration pc : single.programConfs) {
            builder.append(
                programGen.generate(pc, ctx)
            )
        }

        builder.toString
    }

}