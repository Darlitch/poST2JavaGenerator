package su.nsk.iae.post.generator.java.common.vars

import su.nsk.iae.post.poST.VarInitDeclaration
import su.nsk.iae.post.generator.java.common.context.GenerationContext
import static extension su.nsk.iae.post.generator.java.common.util.ExpressionGenerator.*
import static extension su.nsk.iae.post.generator.java.common.util.TypeUtil.*

class VarMemoryGenerator {

	// генерирует memory.put + регистрирует типы
	def static String generate(VarInitDeclaration decl, GenerationContext ctx) {

		val builder = new StringBuilder

		val type = decl.spec.type

		for (v : decl.varList.vars) {

			val name = v.name

			val init =
				if (decl.spec.value !== null)
					generate(decl.spec.value, ctx)
				else
					defaultValue(type)

			builder.append(
				'''memory.put("«name»", «init»);
'''
			)

			ctx.registerVar(name, type)
		}

		builder.toString
	}
}