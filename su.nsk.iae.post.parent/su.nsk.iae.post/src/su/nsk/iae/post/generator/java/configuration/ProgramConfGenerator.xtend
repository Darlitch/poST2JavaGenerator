package su.nsk.iae.post.generator.java.configuration

import su.nsk.iae.post.poST.ProgramConfiguration
import su.nsk.iae.post.poST.AttachVariableConfElement

import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.vars.BindingGenerator

class ProgramConfGenerator {

    def String generate(ProgramConfiguration conf, GenerationContext ctx, String indent) {

        val builder = new StringBuilder

        val instanceName = conf.name
        val programType = conf.program.name

        // ===== ñîçäàíèå ïğîãğàììû =====
        builder.append(
'''
«indent»«programType» «instanceName» = new «programType»(memory);
'''
        )

        // ===== binding àğãóìåíòîâ =====
        if (conf.args !== null) {

            for (arg : conf.args.elements) {

                if (arg instanceof AttachVariableConfElement) {
                    BindingGenerator.generate(arg, ctx)
                }
            }
        }

        builder.toString
    }

}