package su.nsk.iae.post.generator.java.common.vars

import su.nsk.iae.post.poST.GlobalVarDeclaration
import su.nsk.iae.post.generator.java.common.context.GenerationContext
import static extension su.nsk.iae.post.generator.java.common.util.TypeUtil.*

class GlobalVarDeclarationGenerator {

    def static String generate(GlobalVarDeclaration decl, GenerationContext ctx) {

        val builder = new StringBuilder

        // ===== îáû÷íûå global vars =====
        for (v : decl.varsSimple) {
        	
        	for (vname : v.varList.vars) {
		        ctx.registerGlobalVar(vname.name)
		    }

            if (v.arrSpec !== null) {
                builder.append(
                    ArrayMemoryGenerator.generate(v, ctx)
                )
            } else {
                builder.append(
                    VarMemoryGenerator.generate(v, ctx)
                )
            }
        }

        // ===== vars AT %IX0.0 =====
        for (v : decl.varsAs) {

            val type = v.type

            for (varName : v.varList.vars) {

                val name = varName.name

                builder.append(
'''
memory.put("«name»", «defaultValue(type)»);
'''
                )

                ctx.registerVar(name, type)
                ctx.registerGlobalVar(name)
            }
        }

        builder.toString
    }

}