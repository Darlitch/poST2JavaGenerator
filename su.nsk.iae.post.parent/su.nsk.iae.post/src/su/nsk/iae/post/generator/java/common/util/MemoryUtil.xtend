package su.nsk.iae.post.generator.java.common.util

import su.nsk.iae.post.poST.TimeLiteral

class MemoryUtil {

    // имя глобального времени
    def static String globalTime() {
        "_global_time"
    }

    // _p_<process>_v_<var>
    def static String processVar(String process, String varName) {
        "_p_" + process + "_v_" + varName
    }

    // "arr_" + indexExpr
    def static String arrayCell(String array, String indexExpr) {
        "\"" + array + "_\" + (" + indexExpr + ")"
    }

    // имя поля состояния процесса (runtime)
    def static String stateField() {
        "state"
    }

    // имя поля таймера процесса (runtime)
    def static String timerField() {
        "timerBaseTime"
    }

    // T#... → миллисекунды
    // COMPILE-TIME VALUE
    def static long parseTimeValue(TimeLiteral literal) {

        var long total = 0L
	    var String str = literal.interval.trim
	
	    var sign = 1L
	    if (str.startsWith("-")) {
	        sign = -1L
	        str = str.substring(1)
	    }
	
	    while (!str.isEmpty) {
	
	        var i = 0
	        while (i < str.length && Character.isDigit(str.charAt(i)))
	            i++
	
	        if (i == 0)
	            throw new IllegalStateException("Invalid TIME literal: " + literal)
	
	        val value = Long.parseLong(str.substring(0, i))
	        str = str.substring(i)
	
	        var long delta
	
	        if (str.startsWith("ms")) {
	            delta = value
	            str = str.substring(2)
	
	        } else if (str.startsWith("d")) {
	            delta = Math.multiplyExact(value, 86_400_000L)
	            str = str.substring(1)
	
	        } else if (str.startsWith("h")) {
	            delta = Math.multiplyExact(value, 3_600_000L)
	            str = str.substring(1)
	
	        } else if (str.startsWith("m")) {
	            delta = Math.multiplyExact(value, 60_000L)
	            str = str.substring(1)
	
	        } else if (str.startsWith("s")) {
	            delta = Math.multiplyExact(value, 1_000L)
	            str = str.substring(1)
	
	        } else {
	            throw new IllegalStateException("Invalid TIME unit: " + literal)
	        }
	
	        total = Math.addExact(total, delta)
	    }
	
	    val result = Math.multiplyExact(total, sign)
	    result
    }
    
    def static String parseTime(TimeLiteral literal) {
	    parseTimeValue(literal) + "L"
	}

}