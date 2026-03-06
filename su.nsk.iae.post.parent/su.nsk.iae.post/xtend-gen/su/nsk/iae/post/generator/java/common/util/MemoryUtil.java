package su.nsk.iae.post.generator.java.common.util;

import su.nsk.iae.post.poST.TimeLiteral;

@SuppressWarnings("all")
public class MemoryUtil {
  public static String globalTime() {
    return "_global_time";
  }

  public static String processVar(final String process, final String varName) {
    return ((("_p_" + process) + "_v_") + varName);
  }

  public static String arrayCell(final String array, final String indexExpr) {
    return (((("\"" + array) + "_\" + (") + indexExpr) + ")");
  }

  public static String stateField() {
    return "state";
  }

  public static String timerField() {
    return "timerBaseTime";
  }

  public static long parseTimeValue(final TimeLiteral literal) {
    long _xblockexpression = (long) 0;
    {
      long total = 0L;
      String str = literal.getInterval().trim();
      long sign = 1L;
      boolean _startsWith = str.startsWith("-");
      if (_startsWith) {
        sign = (-1L);
        str = str.substring(1);
      }
      while ((!str.isEmpty())) {
        {
          int i = 0;
          while (((i < str.length()) && Character.isDigit(str.charAt(i)))) {
            i++;
          }
          if ((i == 0)) {
            throw new IllegalStateException(("Invalid TIME literal: " + literal));
          }
          final long value = Long.parseLong(str.substring(0, i));
          str = str.substring(i);
          long delta = 0;
          boolean _startsWith_1 = str.startsWith("ms");
          if (_startsWith_1) {
            delta = value;
            str = str.substring(2);
          } else {
            boolean _startsWith_2 = str.startsWith("d");
            if (_startsWith_2) {
              delta = Math.multiplyExact(value, 86_400_000L);
              str = str.substring(1);
            } else {
              boolean _startsWith_3 = str.startsWith("h");
              if (_startsWith_3) {
                delta = Math.multiplyExact(value, 3_600_000L);
                str = str.substring(1);
              } else {
                boolean _startsWith_4 = str.startsWith("m");
                if (_startsWith_4) {
                  delta = Math.multiplyExact(value, 60_000L);
                  str = str.substring(1);
                } else {
                  boolean _startsWith_5 = str.startsWith("s");
                  if (_startsWith_5) {
                    delta = Math.multiplyExact(value, 1_000L);
                    str = str.substring(1);
                  } else {
                    throw new IllegalStateException(("Invalid TIME unit: " + literal));
                  }
                }
              }
            }
          }
          total = Math.addExact(total, delta);
        }
      }
      final long result = Math.multiplyExact(total, sign);
      _xblockexpression = result;
    }
    return _xblockexpression;
  }

  public static String parseTime(final TimeLiteral literal) {
    long _parseTimeValue = MemoryUtil.parseTimeValue(literal);
    return (Long.valueOf(_parseTimeValue) + "L");
  }
}
