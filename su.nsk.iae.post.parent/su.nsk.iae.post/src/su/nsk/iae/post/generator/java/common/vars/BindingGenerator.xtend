package su.nsk.iae.post.generator.java.common.vars

import su.nsk.iae.post.poST.AttachVariableConfElement
import su.nsk.iae.post.poST.TemplateProcessAttachVariableConfElement
import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.util.CompileTimeEvaluator
import su.nsk.iae.post.poST.Constant
import su.nsk.iae.post.poST.IntegerLiteral
import su.nsk.iae.post.poST.RealLiteral

class BindingGenerator {

	def static void generate(AttachVariableConfElement bind, GenerationContext ctx) {

		val left = bind.programVar.name

		// ===== variable → variable =====
		if (bind.attVar !== null) {

			val right = bind.attVar.name

			// PROCESS BINDING
			if (ctx.hasProcess(right)) {
				if (ctx.hasProcess(left))
				    throw new IllegalStateException(
				        "Cannot bind to process name: " + left
				    )

				val fieldName = ctx.resolveProcess(right)
				val type = ctx.getProcessTypeByFieldName(fieldName)

				ctx.registerProcess(left, fieldName, type)
				return
			}

			// MEMORY BINDING
			val target = ctx.resolveAlias(right)
			
			// обычный alias
			ctx.registerAlias(left, target)
			
			// если это массив — переносим метаданные
			if (ctx.hasArrayStart(target)) {
			    ctx.registerArrayStart(left, ctx.getArrayStart(target))
			    ctx.registerArrayType(left, ctx.getArrayElementType(target))
			}
			
			// ===== REGISTER REAL INPUT/OUTPUT =====

			if (ctx.isProcessInput(left)) {
			    ctx.registerInputVar(target)
			}
			
			if (ctx.isProcessOutput(left)) {
			    ctx.registerOutputVar(target)
			}
			
			return
		}

		// ===== variable → constant =====
		if (bind.const !== null) {

			val value = CompileTimeEvaluator.eval(bind.const)
			ctx.registerConst(left, value)
			ctx.registerVar(left, inferConstType(bind.const))
			return
		}
	}
	
	def static void generate(
	    TemplateProcessAttachVariableConfElement bind,
	    GenerationContext ctx
	) {
	
	    val left = bind.programVar.name
	
	    // ===== variable → variable =====
	    if (bind.attVar !== null) {
	
	        val right = bind.attVar.name
	
	        val target = ctx.resolveAlias(right)
	
	        ctx.registerAlias(left, target)
	
	        if (ctx.hasArrayStart(target)) {
	            ctx.registerArrayStart(left, ctx.getArrayStart(target))
	            ctx.registerArrayType(left, ctx.getArrayElementType(target))
	        }
	        
	        // ===== REGISTER REAL INPUT/OUTPUT =====

			if (ctx.isProcessInput(left)) {
			    ctx.registerInputVar(target)
			}
			
			if (ctx.isProcessOutput(left)) {
			    ctx.registerOutputVar(target)
			}
	
	        return
	    }
	
	    // ===== variable → constant =====
	    if (bind.const !== null) {
	
	        val value = CompileTimeEvaluator.eval(bind.const)
	
	        ctx.registerConst(left, value)
	        ctx.registerVar(left, inferConstType(bind.const))
	
	        return
	    }
	}
	
	// ================= INFER CONST TYPE =================

    private static def String inferConstType(Constant c) {

        if (c.num !== null) {

            if (c.num instanceof IntegerLiteral) {
                val lit = c.num as IntegerLiteral
                return if (lit.type !== null) lit.type else "INT"
            }

            if (c.num instanceof RealLiteral) {
                val lit = c.num as RealLiteral
                return if (lit.type !== null) lit.type else "LREAL"
            }
        }

        if (c.time !== null)
            return "TIME"

        if (c.oth == "TRUE" || c.oth == "FALSE")
            return "BOOL"

//        СТРОКОВЫХ ЛИТЕРАЛОВ НЕТ
//        if (c.oth !== null && c.oth.startsWith("\""))
//            return "STRING"

        // fallback
        "INT"
    }
    
    def static String generateAlias(
	    AttachVariableConfElement bind,
	    GenerationContext ctx,
	    String procName,
	    String indent
	) {
	    if (bind.attVar === null)
	        return ""
	
	    val left = bind.programVar.name
	    val right = bind.attVar.name
	
	    if (ctx.hasProcess(right))
	        return ""
	
	    return '''
«indent»«procName»_aliases.put("«left»", "«right»");
	'''
	}
	
	def static String generateAlias(
	    TemplateProcessAttachVariableConfElement bind,
	    GenerationContext ctx,
		String procName,
	    String indent
	) {
	    if (bind.attVar === null)
	        return ""
	
	    val left = bind.programVar.name
	    val right = bind.attVar.name
	
	    if (ctx.hasProcess(right))
	        return ""
	
	    return '''
«indent»«procName»_aliases.put("«left»", "«right»");
	'''
	}
	
	def static String generateProcessBinding(
	    AttachVariableConfElement bind,
	    GenerationContext ctx,
	    String procName,
	    String indent
	) {
	    if (bind.attVar === null)
	        return ""
	
	    val left = bind.programVar.name
	    val right = bind.attVar.name
	
	    if (!ctx.hasProcess(right))
	        return ""
	
	    val field = ctx.resolveProcess(right)
	
	    return '''
«indent»«procName».setProcess("«left»", «field»);
	'''
	}
	
	def static String generateProcessBinding(
	    TemplateProcessAttachVariableConfElement bind,
	    GenerationContext ctx,
	    String procName,
	    String indent
	) {
	    if (bind.attVar === null)
	        return ""
	
	    val left = bind.programVar.name
	    val right = bind.attVar.name
	
	    if (!ctx.hasProcess(right))
	        return ""
	
	    val field = ctx.resolveProcess(right)
	
	    return '''
«indent»«procName».setProcess("«left»", «field»);
	'''
	}
}