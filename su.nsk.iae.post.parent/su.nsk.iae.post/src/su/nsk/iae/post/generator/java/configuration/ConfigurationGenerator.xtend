package su.nsk.iae.post.generator.java.configuration

import su.nsk.iae.post.poST.Configuration
import su.nsk.iae.post.poST.Resource
import su.nsk.iae.post.poST.ProgramConfiguration

import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.vars.VarMemoryGenerator
import su.nsk.iae.post.generator.java.common.vars.GlobalVarDeclarationGenerator
import su.nsk.iae.post.poST.GlobalVarDeclaration
import su.nsk.iae.post.poST.Model

class ConfigurationGenerator {

    val ResourceGenerator resourceGen = new ResourceGenerator

    def String generate(Configuration conf, GenerationContext ctx) {
    	val IND = "        "

        val builder = new StringBuilder
        val name = conf.name

        // áóäåì õðàíèòü èìÿ program instance
        var String programInstance = null

        builder.append(
'''
import java.util.Map;
import java.util.HashMap;

public class Simulation {

    public static void main(String[] args) throws Exception {

'''
        )
        
        builder.append(
'''
«IND»Map<String,Object> memory = new HashMap<>();

«IND»Map<String, IProcess> processMap = new HashMap<>();

«IND»memory.put("_global_time", 0L);
'''
		)
	
        // ===== ðåñóðñû =====
        for (g : conf.eAllContents.toIterable.filter(GlobalVarDeclaration)) {
		    if (g.eContainer instanceof Configuration) {
		        builder.append(
		            GlobalVarDeclarationGenerator.generate(g, ctx, IND)
		        )
		    }
		}
		
        for (Resource r : conf.resources) {
		
		    for (ProgramConfiguration pc : r.resStatement.programConfs) {
		
		        val program = pc.program
		
		        // ===== PROGRAM VARS ===== 
		        for (v : program.progInVars)
		            for (decl : v.vars)
		                builder.append(VarMemoryGenerator.generate(decl, ctx, IND))
		
		        for (v : program.progOutVars)
		            for (decl : v.vars)
		                builder.append(VarMemoryGenerator.generate(decl, ctx, IND))
		
		        for (v : program.progVars)
		            for (decl : v.vars)
		                builder.append(VarMemoryGenerator.generate(decl, ctx, IND))
		
		        for (v : program.progInOutVars)
		            for (decl : v.vars)
		                builder.append(VarMemoryGenerator.generate(decl, ctx, IND))
		
		        for (v : program.progTempVars)
		            for (decl : v.vars)
		                builder.append(VarMemoryGenerator.generate(decl, ctx, IND))
		
		        // èìÿ èíñòàíñà
		        programInstance = pc.name
		    }
		    // ===== GLOBAL VARS ðåñóðñà =====
		    builder.append(
		        resourceGen.generate(r, ctx, IND)
		    )
		}

        if (programInstance === null)
            throw new IllegalStateException("No PROGRAM instance defined in CONFIGURATION")

        builder.append(
'''

«IND»while (true) {

«IND»    «programInstance».runIter(taskTimeMs);

«IND»    Thread.sleep(taskTimeMs);
«IND»}
    }

}
'''
        )

        builder.toString
    }
}