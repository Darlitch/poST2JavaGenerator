package su.nsk.iae.post.generator.java.configuration

import su.nsk.iae.post.poST.Configuration
import su.nsk.iae.post.poST.Resource
import su.nsk.iae.post.poST.ProgramConfiguration

import su.nsk.iae.post.generator.java.common.context.GenerationContext

class ConfigurationGenerator {

    val ResourceGenerator resourceGen = new ResourceGenerator

    def String generate(Configuration conf, GenerationContext ctx) {

        val builder = new StringBuilder
        val name = conf.name

        // áóäåì õðàíèòü èìÿ program instance
        var String programInstance = null

        builder.append(
'''
public class «name»Simulation {

    public static void main(String[] args) throws Exception {

'''
        )

        // ===== ðåñóðñû =====
        for (Resource r : conf.resources) {

            builder.append(
                resourceGen.generate(r, ctx)
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
        while (true) {

            «programInstance».runIter(taskTimeMs);

            Thread.sleep(taskTimeMs);
        }
    }

}
'''
        )

        builder.toString
    }

}