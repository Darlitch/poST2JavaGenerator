package su.nsk.iae.post.generator.java.common.vars

import su.nsk.iae.post.poST.GlobalVarDeclaration
import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.util.CompileTimeEvaluator
import static extension su.nsk.iae.post.generator.java.common.util.TypeUtil.*

class GlobalVarDeclarationGenerator {

    def static String generate(GlobalVarDeclaration decl, GenerationContext ctx, String indent) {

        val builder = new StringBuilder

		// ===== CONSTANT =====
		if (decl.const) {
		    for (v : decl.varsSimple) {
		
		        for (vname : v.varList.vars) {
		
		            val value =
		                CompileTimeEvaluator.evalExpression(v.spec.value, ctx)
		
		            builder.append(
		'''
«indent»memory.put("«vname.name»", «value»);
		'''
		            )
		
		            ctx.registerConst(vname.name, value)
		            ctx.registerVar(vname.name, v.spec.type)
		            ctx.registerGlobalVar(vname.name)
		        }
		    }
		}
		
		// ===== îáû÷íûå global vars =====
		else {
		    for (v : decl.varsSimple) {
		
		        for (vname : v.varList.vars) {
		            ctx.registerGlobalVar(vname.name)
		        }
		
		        if (v.arrSpec !== null) {
		        	for (vname : v.varList.vars) {
		                ctx.registerArrayType(vname.name, v.arrSpec.init.type)
		                ctx.registerArrayStart(vname.name, 0)
		            }
		            builder.append(ArrayMemoryGenerator.generate(v, ctx, indent))
		        } else {
		            builder.append(VarMemoryGenerator.generate(v, ctx, indent))
		        }
		    }
		}

        // ===== vars AT %IX0.0 =====
        for (v : decl.varsAs) {

            val type = v.type

            for (varName : v.varList.vars) {

                val name = varName.name

                builder.append(
'''
«indent»memory.put("«name»", «defaultValue(type)»);
'''
                )

                ctx.registerVar(name, type)
                ctx.registerGlobalVar(name)
            }
        }

        builder.toString
    }

}