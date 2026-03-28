package su.nsk.iae.post.generator.java.configuration

import java.util.ArrayList
import java.util.List

import su.nsk.iae.post.poST.Configuration
import su.nsk.iae.post.poST.ProgramConfiguration
import su.nsk.iae.post.poST.Resource
import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.vars.VarMemoryGenerator

class ConfigurationSimulationGenerator {

    val ResourceGenerator resourceGen = new ResourceGenerator
    val SimulationClassGenerator simulationGen = new SimulationClassGenerator

    def String generate(Configuration conf, GenerationContext ctx) {
        val fields = generateFields(conf, "    ")
        val constructorBody = generateConstructorBody(conf, ctx, "        ")
        val programRunBody = generateProgramRunBody(conf, "        ")

        simulationGen.generate(
            fields,
            constructorBody,
            programRunBody
        )
    }

    private def String generateFields(Configuration conf, String indent) {
        val builder = new StringBuilder

        for (pc : collectProgramConfigurations(conf)) {
            builder.append(
'''
«indent»private final «pc.program.name» «pc.name»;
'''
            )
        }

        builder.toString
    }

    private def String generateConstructorBody(
	    Configuration conf,
	    GenerationContext ctx,
	    String indent
	) {
	    val builder = new StringBuilder
	
	    for (g : conf.eAllContents.toIterable.filter(su.nsk.iae.post.poST.GlobalVarDeclaration)) {
	        if (g.eContainer instanceof Configuration) {
	            builder.append(
	                su.nsk.iae.post.generator.java.common.vars.GlobalVarDeclarationGenerator.generate(g, ctx, indent)
	            )
	        }
	    }
	
	    for (Resource r : conf.resources) {
	    	for (ProgramConfiguration pc : r.resStatement.programConfs) {

		    val program = pc.program
		
		    for (v : program.progInVars)
		        for (decl : v.vars)
		            builder.append(VarMemoryGenerator.generate(decl, ctx, indent))
		
		    for (v : program.progOutVars)
		        for (decl : v.vars)
		            builder.append(VarMemoryGenerator.generate(decl, ctx, indent))
		
		    for (v : program.progVars)
		        for (decl : v.vars)
		            builder.append(VarMemoryGenerator.generate(decl, ctx, indent))
		
		    for (v : program.progInOutVars)
		        for (decl : v.vars)
		            builder.append(VarMemoryGenerator.generate(decl, ctx, indent))
		
		    for (v : program.progTempVars)
		        for (decl : v.vars)
		            builder.append(VarMemoryGenerator.generate(decl, ctx, indent))
		}
	        builder.append(
	            resourceGen.generate(r, ctx, indent)
	        )
	    }
	
	    builder.toString
	}

    private def String generateProgramRunBody(Configuration conf, String indent) {
        val builder = new StringBuilder

        for (pc : collectProgramConfigurations(conf)) {
            builder.append(
'''
«indent»«pc.name».runIter(taskTimeMs);
'''
            )
        }

        builder.toString
    }

    private def List<ProgramConfiguration> collectProgramConfigurations(Configuration conf) {
        val result = new ArrayList<ProgramConfiguration>

        for (Resource r : conf.resources) {
            if (r.resStatement !== null) {
                result.addAll(r.resStatement.programConfs)
            }
        }

        result
    }
}
