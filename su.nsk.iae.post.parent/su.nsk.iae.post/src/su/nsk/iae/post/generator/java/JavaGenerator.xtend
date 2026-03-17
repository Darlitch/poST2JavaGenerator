package su.nsk.iae.post.generator.java

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import su.nsk.iae.post.poST.GlobalVarDeclaration

import su.nsk.iae.post.poST.Model
import su.nsk.iae.post.poST.Program
import su.nsk.iae.post.poST.Constant

import su.nsk.iae.post.generator.IPoSTGenerator
import su.nsk.iae.post.generator.java.common.ProgramGenerator
import su.nsk.iae.post.generator.java.common.IProcessGenerator
import su.nsk.iae.post.generator.java.configuration.ConfigurationGenerator
import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.util.CompileTimeEvaluator

class JavaGenerator implements IPoSTGenerator {

    override setModel(Model model) {
        // не требуется
    }

    override beforeGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {}

    override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {

        val model =
            input.allContents
                 .toIterable
                 .filter(Model)
                 .head

        val ctx = new GenerationContext

        println("Generating IProcess.java")
        fsa.generateFile(
            "IProcess.java",
            IProcessGenerator.generate()
        )
        registerGlobals(model, ctx)

        for (Program p : model.programs) {

            val code = new ProgramGenerator().generate(p, ctx)

            println("Generating program: " + p.name + ".java")
            fsa.generateFile(
                p.name + ".java",
                code
            )
        }

        if (model.conf !== null) {

            val code =
                new ConfigurationGenerator()
                    .generate(model.conf, ctx)

            println("Generating Simulation.java")
            fsa.generateFile(
                model.conf.name + "Simulation.java",
                code
            )
        }
    }
    
    def void registerGlobals(Model model, GenerationContext ctx) {

	    if (model.conf === null) {
	        return
	    }
	    
	    // ===== GLOBALS НА УРОВНЕ CONFIGURATION =====
		for (g : model.conf.eContents.filter(GlobalVarDeclaration)) {
		
		    for (decl : g.varsSimple) {
		
		        if (g.isConst()) {
		
		            for (vname : decl.varList.vars) {
		
		                val value = CompileTimeEvaluator.evalExpression(decl.spec.value)
		
		                ctx.registerConst(vname.name, value)
		                ctx.registerVar(vname.name, decl.spec.type)
		            }
		
		        } else {
		
		            for (vname : decl.varList.vars) {
		
		                val type =
		                    if (decl.spec !== null)
		                        decl.spec.type
		                    else
		                        decl.arrSpec.init.type
		
		                ctx.registerVar(vname.name, type)
		                ctx.registerGlobalVar(vname.name)
		            }
		        }
		    }
		}
	
	    for (res : model.conf.resources)
	        for (g : res.resGlobVars)
	            for (decl : g.varsSimple) {
	
	                if (g.isConst()) {
	
	                    for (vname : decl.varList.vars) {
	
	                        val value = CompileTimeEvaluator.evalExpression(decl.spec.value)
	
	                        ctx.registerConst(vname.name, value)
	                        ctx.registerVar(vname.name, decl.spec.type)
	
	                        println("CONST REGISTERED: " + vname.name) // debug
	                    }
	
	                } else {
	
	                    for (vname : decl.varList.vars) {
	
	                        val type =
	                            if (decl.spec !== null)
	                                decl.spec.type
	                            else
	                                decl.arrSpec.init.type
	
	                        ctx.registerVar(vname.name, type)
	                        ctx.registerGlobalVar(vname.name)
	                    }
	                }
	            }
	}

    override afterGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {}
}