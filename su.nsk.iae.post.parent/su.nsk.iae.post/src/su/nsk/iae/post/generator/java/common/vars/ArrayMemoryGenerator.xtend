package su.nsk.iae.post.generator.java.common.vars

import su.nsk.iae.post.poST.VarInitDeclaration
import su.nsk.iae.post.poST.PrimaryExpression
import su.nsk.iae.post.poST.SymbolicVariable
import su.nsk.iae.post.generator.java.common.context.GenerationContext

import static extension su.nsk.iae.post.generator.java.common.util.ExpressionGenerator.*
import su.nsk.iae.post.poST.Expression
import static extension su.nsk.iae.post.generator.java.common.util.TypeUtil.*

import su.nsk.iae.post.generator.java.common.util.CompileTimeEvaluator

class ArrayMemoryGenerator {
	static val int MAX_ARRAY_SIZE = 1_000_000

	def static String generate(VarInitDeclaration decl, GenerationContext ctx) {

		val builder = new StringBuilder
		val arrSpec = decl.arrSpec

		// ===== ARRAY [*] =====
		if (arrSpec.init.interval === null) {

			for (v : decl.varList.vars) {
				builder.append(
					'''memory.put("«v.name»", new java.util.ArrayList<String>());
'''
				)
				ctx.registerArrayStart(v.name, 0)
			}

			return builder.toString
		}

		val type = arrSpec.init.type
		val interval = arrSpec.init.interval

		// ===== вычисляем границы ОДИН раз =====
		val start = CompileTimeEvaluator.evalInt(interval.start, ctx)
		val end   = CompileTimeEvaluator.evalInt(interval.end, ctx)
		
		if (end < start)
		    throw new IllegalStateException(
		        "Array upper bound is less than lower bound: " + start + " .. " + end
		    )
		
		val long size = (end as long) - (start as long) + 1L
		
		if (size <= 0L)
		    throw new IllegalStateException(
		        "Invalid array size: " + size
		    )
		
		if (size > MAX_ARRAY_SIZE)
		    throw new IllegalStateException(
		        "Array too large (" + size + "). Maximum allowed: " + MAX_ARRAY_SIZE
		    )

		val values =
			if (arrSpec.values !== null)
				arrSpec.values.elements
			else
				null

		for (v : decl.varList.vars) {

			val arrName = v.name
			
			// РЕГИСТРИРУЕМ start один раз на массив
			ctx.registerArrayStart(arrName, start)
			ctx.registerArrayType(arrName, type)

			// ===== INIT =====
			if (values !== null) {

				val allRefs = values.forall[
					it instanceof PrimaryExpression &&
					(it as PrimaryExpression).variable instanceof SymbolicVariable
				]

				// ---- массив ссылок ----
				if (allRefs) {

					val expectedSize = end - start + 1

					if (values.size != expectedSize)
						throw new IllegalStateException(
							"Reference array size mismatch for '" + arrName +
							"': expected " + expectedSize +
							", got " + values.size
						)
						
					for (e : values) {
				        val varName = (e as PrimaryExpression).variable.name
				        val resolved = ctx.resolveAlias(varName)
				
				        // переменная должна быть зарегистрирована
				        if (!ctx.hasType(resolved))
				            throw new IllegalStateException(
				                "Unknown variable in reference array init: " + varName
				            )
				
				        // это не должен быть процесс
				        if (ctx.hasProcess(resolved))
				            throw new IllegalStateException(
				                "Process cannot be used as array element: " + varName
				            )
				    }
				    
				    for (e : values) {
					    val elementType = ctx.getArrayElementType(arrName)
					    val targetType = ctx.resolveVarType((e as PrimaryExpression).variable.name)
					
					    if (elementType != targetType)
					        throw new IllegalStateException(
					            "Type mismatch in reference array '" + arrName +
					            "': expected " + elementType +
					            ", got " + targetType
					        )
					}

					builder.append(
						'''memory.put(
						    "«arrName»",
						    new java.util.ArrayList<String>(
						        java.util.List.of(
						            «FOR e : values SEPARATOR ", "»
						                "«ctx.resolveAlias((e as PrimaryExpression).variable.name)»"
						            «ENDFOR»
						        )
						    )
						);
'''
					)
				}
				// ---- массив значений ----
				else {
					generateValueArray(
					    builder,
					    arrName,
					    start,
					    end,
					    type,
					    values,
					    ctx
					)
				}
			}
			// ===== NO INIT =====
			else {
				generateDefaultArray(
				    builder,
				    arrName,
				    start,
				    end,
				    type,
				    ctx
				)
			}
		}

		builder.toString
	}
	
	
	private static def void generateValueArray(
	    StringBuilder builder,
	    String arrName,
	    int start,
	    int end,
	    String type,
	    java.util.List<Expression> values,
	    GenerationContext ctx
	) {
	
	    val cellNames = newArrayList
	    var idx = 0
	
	    for (i : start .. end) {
	
	        val cell = arrName + "_" + i
	        cellNames.add(cell)
	
	        val init =
	            if (idx < values.size)
	                generate(values.get(idx), ctx)
	            else
	                defaultValue(type)
	
	        builder.append(
	            '''memory.put("«cell»", «init»);
	'''
	        )
	
	        ctx.registerVar(cell, type)
	        idx++
	    }
	
	    builder.append(
	        '''memory.put(
	            "«arrName»",
	            new java.util.ArrayList<String>(
	                java.util.List.of(«FOR c : cellNames SEPARATOR ", "»"«c»"«ENDFOR»)
	            )
	        );
	'''
	    )
	}
	
	private static def void generateDefaultArray(
	    StringBuilder builder,
	    String arrName,
	    int start,
	    int end,
	    String type,
	    GenerationContext ctx
	) {
	
	    val cellNames = newArrayList
	
	    for (i : start .. end) {
	
	        val cell = arrName + "_" + i
	        cellNames.add(cell)
	
	        builder.append(
	            '''memory.put("«cell»", «defaultValue(type)»);
	'''
	        )
	
	        ctx.registerVar(cell, type)
	    }
	
	    builder.append(
	        '''memory.put(
	            "«arrName»",
	            new java.util.ArrayList<String>(
	                java.util.List.of(«FOR c : cellNames SEPARATOR ", "»"«c»"«ENDFOR»)
	            )
	        );
	'''
	    )
	}
}