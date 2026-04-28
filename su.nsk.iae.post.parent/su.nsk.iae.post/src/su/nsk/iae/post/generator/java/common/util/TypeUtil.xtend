package su.nsk.iae.post.generator.java.common.util

class TypeUtil {

	// poST → Java тип
	def static String javaType(String type) {
		switch(type) {

			case "BOOL":
				"Boolean"

			case #["SINT","INT","DINT","USINT","UINT","UDINT","BYTE","WORD","DWORD"].contains(type):
				"Integer"

			case #["LINT","ULINT","LWORD","TIME"].contains(type):
				"Long"

			case "REAL":
				"Float"

			case "LREAL":
				"Double"

			case #["STRING","WSTRING"].contains(type):
				"String"

			default:
				"Object"
		}
	}

	// значение по умолчанию
	def static String defaultValue(String type) {
		switch(type) {

			case "BOOL":
				"false"

			case "REAL":
				"0.0f"

			case "LREAL":
				"0.0"

			case #["STRING","WSTRING"].contains(type):
				"\"\""

			case #["TIME","LINT","ULINT","LWORD"].contains(type):
				"0L"

			case #["SINT","INT","DINT","USINT","UINT","UDINT","BYTE","WORD","DWORD"].contains(type):
				"0"

			default:
				"null"
		}
	}

	// числовой тип?
	def static boolean isNumeric(String type) {
		switch(type) {
			case #["BOOL","STRING","WSTRING"].contains(type):
				false
			default:
				true
		}
	}

	// булевый тип?
	def static boolean isBoolean(String type) {
		type == "BOOL"
	}
	
	def static int numericRank(String type) {
		if (type == "LREAL") return 4
	    if (type == "REAL") return 3
	
	    if (#["LINT","ULINT","TIME","LWORD"].contains(type)) return 2
	
	    if (#["SINT","INT","DINT","USINT","UINT","UDINT","BYTE","WORD","DWORD"].contains(type))
	        return 1
	
	    return 0
	}
	
	def static String promoteNumeric(String t1, String t2) {
		// ===== запрет BOOL =====
	    if (t1 == "BOOL" || t2 == "BOOL")
	        throw new IllegalStateException(
	            "Arithmetic operation on BOOL type is not allowed: " + t1 + ", " + t2
	        )
		val r1 = numericRank(t1)
		val r2 = numericRank(t2)
	
		val r = Math.max(r1, r2)
	
		switch r {
			case 4: "LREAL"
			case 3: "REAL"
			case 2: "LINT"
			case 1: "INT"
			default: throw new IllegalStateException(
                "Unsupported numeric promotion: " + t1 + ", " + t2
            )
		}
	}
	
	def static String toInt(String expr) {
	    '''((Number)(«expr»)).intValue()'''
	}
	
	def static boolean isBitwiseCapable(String type) {
	    switch(type) {
	        case "BOOL": false
	        case "TIME": false
	        case #["STRING","WSTRING"]: false
	        default: true
	    }
	}
	
	def static boolean isBitwiseInteger(String type) {
	    numericRank(type) > 0 && type != "REAL" && type != "LREAL"
	}
	
	def static boolean canBitwise(String t1, String t2) {
	    isBitwiseInteger(t1) && isBitwiseInteger(t2)
	}
	
	def static boolean canAssign(String target, String source) {
	
	    // одинаковый тип
	    if (target == source)
	        return true
	
	    // TIME можно присваивать только TIME
	    if (target == "TIME" || source == "TIME")
	        return false
	
	    // числовые типы
	    if (target.isNumeric && source.isNumeric)
			return true
	
	    return false
	}
	
	def static String castTo(String expr, String type) {
	    switch type {
	        case "REAL":  '''((float)(«expr»))'''
	        case "LREAL": '''((double)(«expr»))'''
	        case "INT":   '''((int)(«expr»))'''
	        case "LINT":  '''((long)(«expr»))'''
	        default: expr
	    }
	}

}