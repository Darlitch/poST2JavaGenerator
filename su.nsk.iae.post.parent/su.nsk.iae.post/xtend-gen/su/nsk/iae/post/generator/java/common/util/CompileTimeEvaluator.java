package su.nsk.iae.post.generator.java.common.util;

import java.util.Objects;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.poST.AddExpression;
import su.nsk.iae.post.poST.AddOperator;
import su.nsk.iae.post.poST.Constant;
import su.nsk.iae.post.poST.Expression;
import su.nsk.iae.post.poST.IntegerLiteral;
import su.nsk.iae.post.poST.MulExpression;
import su.nsk.iae.post.poST.MulOperator;
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

  public static Object evalExpression(final Expression expr, final GenerationContext ctx) {
    if ((expr instanceof PrimaryExpression)) {
      final PrimaryExpression pe = ((PrimaryExpression) expr);
      Constant _const = pe.getConst();
      boolean _tripleNotEquals = (_const != null);
      if (_tripleNotEquals) {
        return CompileTimeEvaluator.eval(pe.getConst());
      }
      SymbolicVariable _variable = pe.getVariable();
      if ((_variable instanceof SymbolicVariable)) {
        final String name = ctx.resolveAlias(pe.getVariable().getName());
        boolean _hasConst = ctx.hasConst(name);
        if (_hasConst) {
          return ctx.getConst(name);
        }
      }
      return null;
    }
    if ((expr instanceof MulExpression)) {
      final MulExpression m = ((MulExpression) expr);
      final Object left = CompileTimeEvaluator.evalExpression(m.getLeft(), ctx);
      final Object right = CompileTimeEvaluator.evalExpression(m.getRight(), ctx);
      if (((left == null) || (right == null))) {
        return null;
      }
      MulOperator _mulOp = m.getMulOp();
      if (_mulOp != null) {
        switch (_mulOp) {
          case MUL:
            double _doubleValue = ((Number) left).doubleValue();
            double _doubleValue_1 = ((Number) right).doubleValue();
            return Double.valueOf((_doubleValue * _doubleValue_1));
          case DIV:
            double _doubleValue_2 = ((Number) left).doubleValue();
            double _doubleValue_3 = ((Number) right).doubleValue();
            return Double.valueOf((_doubleValue_2 / _doubleValue_3));
          case MOD:
            long _longValue = ((Number) left).longValue();
            long _longValue_1 = ((Number) right).longValue();
            return Long.valueOf((_longValue % _longValue_1));
          default:
            break;
        }
      }
    }
    if ((expr instanceof AddExpression)) {
      final AddExpression a = ((AddExpression) expr);
      final Object left_1 = CompileTimeEvaluator.evalExpression(a.getLeft(), ctx);
      final Object right_1 = CompileTimeEvaluator.evalExpression(a.getRight(), ctx);
      if (((left_1 == null) || (right_1 == null))) {
        return null;
      }
      AddOperator _addOp = a.getAddOp();
      if (_addOp != null) {
        switch (_addOp) {
          case PLUS:
            double _doubleValue_4 = ((Number) left_1).doubleValue();
            double _doubleValue_5 = ((Number) right_1).doubleValue();
            return Double.valueOf((_doubleValue_4 + _doubleValue_5));
          case MINUS:
            double _doubleValue_6 = ((Number) left_1).doubleValue();
            double _doubleValue_7 = ((Number) right_1).doubleValue();
            return Double.valueOf((_doubleValue_6 - _doubleValue_7));
          default:
            break;
        }
      }
    }
    return null;
  }
}
