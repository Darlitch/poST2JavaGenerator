package su.nsk.iae.post.generator.java.common.util;

import java.util.Collections;
import java.util.Objects;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class TypeUtil {
  public static String javaType(final String type) {
    String _switchResult = null;
    boolean _matched = false;
    if (Objects.equals(type, "BOOL")) {
      _matched=true;
      _switchResult = "Boolean";
    }
    if (!_matched) {
      boolean _contains = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("SINT", "INT", "DINT", "USINT", "UINT", "UDINT", "BYTE", "WORD", "DWORD")).contains(type);
      if (_contains) {
        _matched=true;
        _switchResult = "Integer";
      }
    }
    if (!_matched) {
      boolean _contains_1 = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("LINT", "ULINT", "LWORD", "TIME")).contains(type);
      if (_contains_1) {
        _matched=true;
        _switchResult = "Long";
      }
    }
    if (!_matched) {
      if (Objects.equals(type, "REAL")) {
        _matched=true;
        _switchResult = "Float";
      }
    }
    if (!_matched) {
      if (Objects.equals(type, "LREAL")) {
        _matched=true;
        _switchResult = "Double";
      }
    }
    if (!_matched) {
      boolean _contains_2 = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("STRING", "WSTRING")).contains(type);
      if (_contains_2) {
        _matched=true;
        _switchResult = "String";
      }
    }
    if (!_matched) {
      _switchResult = "Object";
    }
    return _switchResult;
  }

  public static String defaultValue(final String type) {
    String _switchResult = null;
    boolean _matched = false;
    if (Objects.equals(type, "BOOL")) {
      _matched=true;
      _switchResult = "false";
    }
    if (!_matched) {
      if (Objects.equals(type, "REAL")) {
        _matched=true;
        _switchResult = "0.0f";
      }
    }
    if (!_matched) {
      if (Objects.equals(type, "LREAL")) {
        _matched=true;
        _switchResult = "0.0";
      }
    }
    if (!_matched) {
      boolean _contains = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("STRING", "WSTRING")).contains(type);
      if (_contains) {
        _matched=true;
        _switchResult = "\"\"";
      }
    }
    if (!_matched) {
      boolean _contains_1 = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("TIME", "LINT", "ULINT", "LWORD")).contains(type);
      if (_contains_1) {
        _matched=true;
        _switchResult = "0L";
      }
    }
    if (!_matched) {
      boolean _contains_2 = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("SINT", "INT", "DINT", "USINT", "UINT", "UDINT", "BYTE", "WORD", "DWORD")).contains(type);
      if (_contains_2) {
        _matched=true;
        _switchResult = "0";
      }
    }
    if (!_matched) {
      _switchResult = "null";
    }
    return _switchResult;
  }

  public static boolean isNumeric(final String type) {
    boolean _switchResult = false;
    boolean _matched = false;
    boolean _contains = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("BOOL", "STRING", "WSTRING")).contains(type);
    if (_contains) {
      _matched=true;
      _switchResult = false;
    }
    if (!_matched) {
      _switchResult = true;
    }
    return _switchResult;
  }

  public static boolean isBoolean(final String type) {
    return Objects.equals(type, "BOOL");
  }

  public static int numericRank(final String type) {
    int _switchResult = (int) 0;
    boolean _matched = false;
    if (Objects.equals(type, "LREAL")) {
      _matched=true;
      _switchResult = 4;
    }
    if (!_matched) {
      if (Objects.equals(type, "REAL")) {
        _matched=true;
        _switchResult = 3;
      }
    }
    if (!_matched) {
      if (Objects.equals(type, Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("LINT", "ULINT", "TIME", "LWORD")))) {
        _matched=true;
        _switchResult = 2;
      }
    }
    if (!_matched) {
      if (Objects.equals(type, Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("SINT", "INT", "DINT", "USINT", "UINT", "UDINT", "BYTE", "WORD", "DWORD")))) {
        _matched=true;
        _switchResult = 1;
      }
    }
    if (!_matched) {
      _switchResult = 0;
    }
    return _switchResult;
  }

  public static String promoteNumeric(final String t1, final String t2) {
    String _xblockexpression = null;
    {
      if ((Objects.equals(t1, "BOOL") || Objects.equals(t2, "BOOL"))) {
        throw new IllegalStateException(
          ((("Arithmetic operation on BOOL type is not allowed: " + t1) + ", ") + t2));
      }
      final int r1 = TypeUtil.numericRank(t1);
      final int r2 = TypeUtil.numericRank(t2);
      final int r = Math.max(r1, r2);
      String _switchResult = null;
      switch (r) {
        case 4:
          _switchResult = "LREAL";
          break;
        case 3:
          _switchResult = "REAL";
          break;
        case 2:
          _switchResult = "LINT";
          break;
        case 1:
          _switchResult = "INT";
          break;
        default:
          throw new IllegalStateException(
            ((("Unsupported numeric promotion: " + t1) + ", ") + t2));
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }

  public static String toInt(final String expr) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("((Number)(");
    _builder.append(expr);
    _builder.append(")).intValue()");
    return _builder.toString();
  }

  public static boolean isBitwiseCapable(final String type) {
    boolean _switchResult = false;
    boolean _matched = false;
    if (Objects.equals(type, "BOOL")) {
      _matched=true;
      _switchResult = false;
    }
    if (!_matched) {
      if (Objects.equals(type, "TIME")) {
        _matched=true;
        _switchResult = false;
      }
    }
    if (!_matched) {
      if (Objects.equals(type, Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("STRING", "WSTRING")))) {
        _matched=true;
        _switchResult = false;
      }
    }
    if (!_matched) {
      _switchResult = true;
    }
    return _switchResult;
  }

  public static boolean isBitwiseInteger(final String type) {
    return (((TypeUtil.numericRank(type) > 0) && (!Objects.equals(type, "REAL"))) && (!Objects.equals(type, "LREAL")));
  }

  public static boolean canBitwise(final String t1, final String t2) {
    return (TypeUtil.isBitwiseInteger(t1) && TypeUtil.isBitwiseInteger(t2));
  }

  public static boolean canAssign(final String target, final String source) {
    boolean _equals = Objects.equals(target, source);
    if (_equals) {
      return true;
    }
    if ((Objects.equals(target, "TIME") || Objects.equals(source, "TIME"))) {
      return false;
    }
    if ((TypeUtil.isNumeric(target) && TypeUtil.isNumeric(source))) {
      int _numericRank = TypeUtil.numericRank(target);
      int _numericRank_1 = TypeUtil.numericRank(source);
      return (_numericRank >= _numericRank_1);
    }
    return false;
  }
}
