package su.nsk.iae.post.generator.java.common.util;

import java.util.Objects;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.poST.Constant;
import su.nsk.iae.post.poST.Expression;
import su.nsk.iae.post.poST.IntegerLiteral;
import su.nsk.iae.post.poST.NumericLiteral;
import su.nsk.iae.post.poST.PrimaryExpression;
import su.nsk.iae.post.poST.RealLiteral;
import su.nsk.iae.post.poST.SignedInteger;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.TimeLiteral;

@SuppressWarnings("all")
public class CompileTimeEvaluator {
  public static Object eval(final Constant c) {
    NumericLiteral _num = c.getNum();
    boolean _tripleNotEquals = (_num != null);
    if (_tripleNotEquals) {
      NumericLiteral _num_1 = c.getNum();
      if ((_num_1 instanceof IntegerLiteral)) {
        NumericLiteral _num_2 = c.getNum();
        final IntegerLiteral lit = ((IntegerLiteral) _num_2);
        final SignedInteger signed = lit.getValue();
        int _xifexpression = (int) 0;
        boolean _isISig = signed.isISig();
        if (_isISig) {
          _xifexpression = (-1);
        } else {
          _xifexpression = 1;
        }
        final int sign = _xifexpression;
        int _parseInt = Integer.parseInt(signed.getValue().trim());
        return Integer.valueOf((sign * _parseInt));
      }
      NumericLiteral _num_3 = c.getNum();
      if ((_num_3 instanceof RealLiteral)) {
        NumericLiteral _num_4 = c.getNum();
        final RealLiteral lit_1 = ((RealLiteral) _num_4);
        double _xifexpression_1 = (double) 0;
        boolean _isRSig = lit_1.isRSig();
        if (_isRSig) {
          _xifexpression_1 = (-1.0);
        } else {
          _xifexpression_1 = 1.0;
        }
        final double sign_1 = _xifexpression_1;
        double _parseDouble = Double.parseDouble(lit_1.getValue());
        return Double.valueOf((sign_1 * _parseDouble));
      }
    }
    TimeLiteral _time = c.getTime();
    boolean _tripleNotEquals_1 = (_time != null);
    if (_tripleNotEquals_1) {
      return Long.valueOf(MemoryUtil.parseTimeValue(c.getTime()));
    }
    String _oth = c.getOth();
    boolean _equals = Objects.equals(_oth, "TRUE");
    if (_equals) {
      return Boolean.valueOf(true);
    }
    String _oth_1 = c.getOth();
    boolean _equals_1 = Objects.equals(_oth_1, "FALSE");
    if (_equals_1) {
      return Boolean.valueOf(false);
    }
    throw new IllegalStateException(("Unsupported constant: " + c));
  }

  public static int evalInt(final Expression expr, final GenerationContext ctx) {
    if ((expr instanceof PrimaryExpression)) {
      final PrimaryExpression pe = ((PrimaryExpression) expr);
      Constant _const = pe.getConst();
      boolean _tripleNotEquals = (_const != null);
      if (_tripleNotEquals) {
        final Constant c = pe.getConst();
        NumericLiteral _num = c.getNum();
        if ((_num instanceof IntegerLiteral)) {
          NumericLiteral _num_1 = c.getNum();
          final IntegerLiteral lit = ((IntegerLiteral) _num_1);
          final SignedInteger signed = lit.getValue();
          int _xifexpression = (int) 0;
          boolean _isISig = signed.isISig();
          if (_isISig) {
            _xifexpression = (-1);
          } else {
            _xifexpression = 1;
          }
          final int sign = _xifexpression;
          int _parseInt = Integer.parseInt(signed.getValue().trim());
          return (sign * _parseInt);
        }
        if (((c.getOth() != null) && c.getOth().startsWith("16#"))) {
          return Integer.parseInt(c.getOth().substring(3), 16);
        }
        if (((c.getOth() != null) && c.getOth().startsWith("2#"))) {
          return Integer.parseInt(c.getOth().substring(2), 2);
        }
        if (((c.getOth() != null) && c.getOth().startsWith("8#"))) {
          return Integer.parseInt(c.getOth().substring(2), 8);
        }
      }
      SymbolicVariable _variable = pe.getVariable();
      if ((_variable instanceof SymbolicVariable)) {
        final String name = ctx.resolveAlias(pe.getVariable().getName());
        boolean _hasConst = ctx.hasConst(name);
        if (_hasConst) {
          Object _const_1 = ctx.getConst(name);
          return ((Number) _const_1).intValue();
        }
      }
    }
    throw new IllegalStateException(
      ("Unsupported compile-time integer expression: " + expr));
  }

  public static Object evalExpression(final Expression expr) {
    if ((expr instanceof PrimaryExpression)) {
      final PrimaryExpression pe = ((PrimaryExpression) expr);
      Constant _const = pe.getConst();
      boolean _tripleNotEquals = (_const != null);
      if (_tripleNotEquals) {
        return CompileTimeEvaluator.eval(pe.getConst());
      }
    }
    throw new IllegalStateException(
      ("Unsupported constant expression: " + expr));
  }
}
