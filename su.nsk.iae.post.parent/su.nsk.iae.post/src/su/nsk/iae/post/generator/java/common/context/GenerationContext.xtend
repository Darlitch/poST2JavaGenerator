package su.nsk.iae.post.generator.java.common.context

import java.util.HashMap
import java.util.HashSet
import java.util.Map
import java.util.Set

// контекст генерации: хранит registry типов переменных
class GenerationContext {

	// имя ячейки памяти → poST тип
	val Map<String, String> varTypes = new HashMap
	
	// имя константы → compile-time значение (boxed Java type)
	val Map<String, Object> constValues = new HashMap
	
	// ================= VAR TYPES =================

	// зарегистрировать тип переменной
	def void registerVar(String memoryName, String postType) {
		varTypes.put(memoryName, postType)
	}

	// получить тип переменной
//	private def String getType(String memoryName) {
//		varTypes.get(memoryName)
//	}

	// есть ли информация о типе
	def boolean hasType(String memoryName) {
		varTypes.containsKey(memoryName)
	}
	
	def String resolveVarType(String name) {
	    val resolved = resolveAlias(name)
	
	    val type = varTypes.get(resolved)
	
	    if (type === null)
	        throw new IllegalStateException(
	            "Type not registered for variable: " + resolved
	        )
	
	    type
	}
	
	def String resolveVarName(String name) {
	    val resolved = resolveAlias(name)
	
	    if (!varTypes.containsKey(resolved))
	        throw new IllegalStateException(
	            "Unknown variable: " + resolved
	        )
	
	    resolved
	}
	
	// ================= CONSTANTS =================

	// зарегистрировать константу
	def void registerConst(String name, Object value) {
		constValues.put(name, value)
	}

	// получить значение константы
	def Object getConst(String name) {
		constValues.get(name)
	}

	// есть ли такая константа
	def boolean hasConst(String name) {
		constValues.containsKey(name)
	}
	
	// ================= VARIABLE REGISTRY =================

	val Set<String> inputVars = new HashSet
	val Set<String> outputVars = new HashSet
	val Set<String> globalVars = new HashSet
	val Set<String> localVars = new HashSet
	
	def void registerInputVar(String name) {
	    inputVars.add(name)
	}
	
	def void registerOutputVar(String name) {
	    outputVars.add(name)
	}
	
	def void registerGlobalVar(String name) {
	    globalVars.add(name)
	}
	
	def void registerLocalVar(String name) {
	    localVars.add(name)
	}
	
	def Set<String> getInputVars() {
	    inputVars
	}
	
	def Set<String> getOutputVars() {
	    outputVars
	}
	
	def Set<String> getGlobalVars() {
	    globalVars
	}
	
	def Set<String> getLocalVars() {
	    localVars
	}
	
	// ================= ARRAY BOUNDS =================

	// имя массива → начальный индекс
	val Map<String, Integer> arrayStarts = new HashMap
	
	// имя массива → poST тип элемента
	val Map<String, String> arrayElementTypes = new HashMap
	
	// зарегистрировать старт индекса массива
	def void registerArrayStart(String arrayName, int start) {
		arrayStarts.put(arrayName, start)
	}
	
	// получить старт индекса
	def int getArrayStart(String arrayName) {
		if (!arrayStarts.containsKey(arrayName))
			throw new IllegalStateException(
				"Array start not registered: " + arrayName
			)
		arrayStarts.get(arrayName)
	}
	
	// есть ли информация
	def boolean hasArrayStart(String arrayName) {
		arrayStarts.containsKey(arrayName)
	}
	
	def void registerArrayType(String arrayName, String elementType) {
			arrayElementTypes.put(arrayName, elementType)
		}
		
		def String getArrayElementType(String arrayName) {
		val type = arrayElementTypes.get(arrayName)
		if (type === null)
			throw new IllegalStateException(
				"Array element type not registered: " + arrayName
			)
		type
	}
	
	def boolean hasArrayElementType(String arrayName) {
		arrayElementTypes.containsKey(arrayName)
	}
	
	// ================= ALIASES =================

	// alias → реальное имя ячейки памяти
	val Map<String, String> aliases = new HashMap

	// зарегистрировать alias
	def void registerAlias(String alias, String target) {
		aliases.put(alias, target)
	}

	// есть ли alias
	def boolean hasAlias(String name) {
		aliases.containsKey(name)
	}

	// получить непосредственную цель alias
	def String getAliasTarget(String name) {
		aliases.get(name)
	}

	// рекурсивно разрешить alias до реального имени
	def String resolveAlias(String name) {

		var current = name

		val visited = new HashSet<String>()
		while (aliases.containsKey(current)) {
		    if (!visited.add(current))
		        throw new IllegalStateException("Alias cycle detected while resolving '" + name + "' at '" + current + "'")
		    current = aliases.get(current)
		}

		current
	}
	
	// ================= PROCESS REFERENCES =================

	// poST имя процесса → Java имя поля
	val Map<String, String> processRefs = new HashMap
	
	// Java имя поля процесса → тип класса процесса
	val Map<String, String> processTypes = new HashMap

	def void registerProcess(String postName, String javaFieldName, String processType) {
		processRefs.put(postName, javaFieldName)
		processTypes.put(javaFieldName, processType)
	}

	def boolean hasProcess(String name) {
		processRefs.containsKey(name)
	}

	def String resolveProcess(String name) {
		val resolved = processRefs.get(name)
		if (resolved === null)
			throw new IllegalStateException(
				"Process not registered: " + name
			)
		resolved
	}
	
	def String getProcessTypeByFieldName(String name) {
		val type = processTypes.get(name)
		if (type === null)
			throw new IllegalStateException(
				"Process type not registered: " + name
			)
		type
	}
}