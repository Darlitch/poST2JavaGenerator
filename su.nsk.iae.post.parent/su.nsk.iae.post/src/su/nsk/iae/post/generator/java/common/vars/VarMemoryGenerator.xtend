package su.nsk.iae.post.generator.java.common.vars

import su.nsk.iae.post.poST.VarInitDeclaration
import su.nsk.iae.post.generator.java.common.context.GenerationContext
import static extension su.nsk.iae.post.generator.java.common.util.ExpressionGenerator.*
import static extension su.nsk.iae.post.generator.java.common.util.TypeUtil.*

class VarMemoryGenerator {
	
	private def static String generateInternal(
	    VarInitDeclaration decl,
	    GenerationContext ctx,
	    String indent,
	    String target // "memory" или "localMemory"
	) {
	    val builder = new StringBuilder
	
	    val type =
	        if (decl.spec !== null)
	            decl.spec.type
	        else if (decl.arrSpec !== null)
	            decl.arrSpec.init.type
	        else
	            null
	
	    for (v : decl.varList.vars) {
	
	        val name = v.name
	        val resolved = ctx.resolveAlias(name)
	
	        // ❗ alias / process — пропускаем
	        if (!ctx.hasArrayStart(resolved) && !ctx.hasProcess(resolved) && !ctx.hasAlias(name)) {
	
	            val init =
	                if (decl.spec !== null && decl.spec.value !== null)
	                    generate(decl.spec.value, ctx)
	                else if (type !== null)
	                    defaultValue(type)
	                else
	                    "null"
	
	            builder.append(
	'''
	«indent»«target».put("«name»", «init»);
	'''
	            )
	
	            ctx.registerVar(name, type)
	        }
	    }
	
	    builder.toString
	}
	
	def static String generate(VarInitDeclaration decl, GenerationContext ctx, String indent) {
	    return generateInternal(decl, ctx, indent, "memory")
	}
	
	def static String generateLocal(VarInitDeclaration decl, GenerationContext ctx, String indent) {
	    return generateInternal(decl, ctx, indent, "localMemory")
	}

	// генерирует memory.put + регистрирует типы
//	def static String generate(VarInitDeclaration decl, GenerationContext ctx, String indent) {
//
//		val builder = new StringBuilder
//
//		val type =
//		    if (decl.spec !== null)
//		        decl.spec.type
//		    else if (decl.arrSpec !== null)
//		        decl.arrSpec.init.type
//		    else
//		        null
//
//		for (v : decl.varList.vars) {
//
//			val name = v.name
//			
//			val resolved = ctx.resolveAlias(name)
//
//			// ❗ ЕСЛИ ЭТО ALIAS НЕ ГЕНЕРИМ memory.put
//			if (!ctx.hasArrayStart(resolved) && !ctx.hasProcess(resolved) && !ctx.hasAlias(name)) {
//			    val init =
//				    if (decl.spec !== null && decl.spec.value !== null)
//				        generate(decl.spec.value, ctx)
//				    else if (type !== null)
//				        defaultValue(type)
//				    else
//				        "null"
//	
//				builder.append(
//					'''«indent»memory.put("«name»", «init»);
//	'''
//				)
//	
//				ctx.registerVar(name, type)
//			}
//
//		}
//
//		builder.toString
//	}
}