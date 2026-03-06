package su.nsk.iae.post.generator.java.common.vars.data

// описание переменной памяти
class VarData {

	String name       // имя ячейки памяти
	String type       // poST тип (INT, BOOL, ...)
	String initValue  // значение инициализации (строка Java)

	new(String name, String type, String initValue) {
		this.name = name
		this.type = type
		this.initValue = initValue
	}
}