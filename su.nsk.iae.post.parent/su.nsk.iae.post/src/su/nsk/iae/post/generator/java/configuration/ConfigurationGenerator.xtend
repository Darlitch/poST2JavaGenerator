package su.nsk.iae.post.generator.java.configuration

import su.nsk.iae.post.poST.Configuration
import su.nsk.iae.post.poST.Resource
import su.nsk.iae.post.poST.ProgramConfiguration

import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.vars.GlobalVarDeclarationGenerator
import su.nsk.iae.post.poST.GlobalVarDeclaration

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

public class «name»Simulation {

    public static void main(String[] args) throws Exception {

'''
        )
        
        builder.append(
'''
«IND»Map<String,Object> memory = new HashMap<>();

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

            builder.append(
                resourceGen.generate(r, ctx, IND)
            )

            // îïðåäåëÿåì èìÿ program instance
            for (ProgramConfiguration pc : r.resStatement.programConfs) {
                programInstance = pc.name
            }
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