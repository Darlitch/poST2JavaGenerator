package su.nsk.iae.post.generator.java.configuration

import su.nsk.iae.post.poST.ProgramConfiguration
import su.nsk.iae.post.poST.AttachVariableConfElement
import su.nsk.iae.post.poST.TemplateProcessAttachVariableConfElement
import su.nsk.iae.post.poST.TemplateProcessConfElement

import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.vars.BindingGenerator

class ProgramConfGenerator {

    def String generate(ProgramConfiguration conf, GenerationContext ctx, String indent) {

        val builder = new StringBuilder

        val instanceName = conf.name
        val programType = conf.program.name

        // ===== ñîçäàíèå ïðîãðàììû =====
        builder.append(
'''
«indent»«programType» «instanceName» = new «programType»(memory);
'''
        )

	    // ===== ñîçäàíèå ïðîöåññîâ =====
	    if (conf.args !== null) {
	
	        for (arg : conf.args.elements) {
	
	            // PROCESS ýëåìåíò (î÷åíü âàæíî!)
	            if (arg instanceof TemplateProcessConfElement) {
	
	                val proc = arg
	
	                val procName = proc.name                 // control1
	                val procType = proc.process.name         // Control
	                
	                ctx.registerProcess(procName, procName, procType)
	
	                // ===== alias map =====
	                builder.append(
'''

«indent»Map<String,String> «procName»_aliases = new HashMap<>();
'''
	                )
	
	                // ===== ïàðàìåòðû ïðîöåññà =====
	                if (proc.args !== null) {
	                	for (p : proc.args.elements) {
						    BindingGenerator.generate(p, ctx)
						}
	                    for (p : proc.args.elements) {
	
	                        if (p instanceof AttachVariableConfElement||
    							p instanceof TemplateProcessAttachVariableConfElement) {
	
	                            builder.append(
	                                BindingGenerator.generateAlias(p, ctx, procName, indent)
	                            )
	                        }
	                    }
	                }
	
	                // ===== ñîçäàíèå ïðîöåññà =====
	                builder.append(
'''
«indent»«procType» «procName» = new «procType»("«procName»", memory, «procName»_aliases);
«indent»«instanceName».registerProcess(«procName»);
'''
	                )
	                
	                if (proc.args !== null) {
	                    for (p : proc.args.elements) {
	
	                        if (p instanceof AttachVariableConfElement||
    							p instanceof TemplateProcessAttachVariableConfElement) {
	
	                            builder.append(
	                                BindingGenerator.generateProcessBinding(p, ctx, procName, indent)
	                            )
	                        }
	                    }
	                }
	                if (proc.active) {
					    builder.append(
'''
«indent»«procName».start();
'''
					    )
					}
	            }
	        }
	    }

        builder.toString
    }

}