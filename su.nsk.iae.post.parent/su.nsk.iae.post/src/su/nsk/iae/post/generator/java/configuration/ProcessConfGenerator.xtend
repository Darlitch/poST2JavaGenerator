package su.nsk.iae.post.generator.java.configuration

import su.nsk.iae.post.poST.TemplateProcessConfElement
import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.vars.BindingGenerator
import su.nsk.iae.post.poST.AttachVariableConfElement
import su.nsk.iae.post.poST.TemplateProcessAttachVariableConfElement

class ProcessConfGenerator {

    def String generate(
	    TemplateProcessConfElement proc,
	    GenerationContext ctx,
	    String programInstance,
	    String programType,
	    String indent
	) {
	
	    val builder = new StringBuilder
	
	    val procName = "proc_" + proc.name
	    val procType = processJavaTypeName(proc.process.name)
	
	    ctx.registerProcess(proc.name, procName, procType)
	
	    // ===== alias map =====
	    builder.append(
'''
«indent»

«indent»Map<String,String> «procName»_aliases = new HashMap<>();
'''
	    )
	
	    // ===== ïàðàìåòðû =====
	    if (proc.args !== null) {
	
	        for (p : proc.args.elements) {
	            BindingGenerator.generate(p, ctx)
	        }
	
	        for (p : proc.args.elements) {
	            if (p instanceof AttachVariableConfElement ||
	                p instanceof TemplateProcessAttachVariableConfElement) {
	
	                builder.append(
	                    BindingGenerator.generateAlias(p, ctx, procName, indent)
	                )
	            }
	        }
	    }
	
	    // ===== ñîçäàíèå =====
	    builder.append(
'''
«indent»«programType».«procType» «procName» = new «programType».«procType»("«procName»", memory, «procName»_aliases, processMap);
«indent»«programInstance».registerProcess(«procName»);
'''
	    )
	
	    // ===== process binding =====
	    if (proc.args !== null) {
	        for (p : proc.args.elements) {
	            if (p instanceof AttachVariableConfElement ||
	                p instanceof TemplateProcessAttachVariableConfElement) {
	
	                builder.append(
	                    BindingGenerator.generateProcessBinding(p, ctx, procName, indent)
	                )
	            }
	        }
	    }
	
	    // ===== ACTIVE =====
	    if (proc.active) {
	        builder.append(
'''
«indent»«procName».start();
'''
	        )
	    }
	
	    return builder.toString
	}
	
	private def String processJavaTypeName(String processName) {
	    processName + "Process"
	}

}