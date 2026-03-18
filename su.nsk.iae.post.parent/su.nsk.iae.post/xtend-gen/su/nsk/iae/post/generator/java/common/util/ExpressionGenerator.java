package su.nsk.iae.post.generator.java.common.util;

import java.util.Collections;
import java.util.Objects;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.poST.AddExpression;
import su.nsk.iae.post.poST.AddOperator;
import su.nsk.iae.post.poST.AndExpression;
import su.nsk.iae.post.poST.ArrayVariable;
import su.nsk.iae.post.poST.CompExpression;
import su.nsk.iae.post.poST.CompOperator;
import su.nsk.iae.post.poST.Constant;
import su.nsk.iae.post.poST.EquExpression;
import su.nsk.iae.post.poST.EquOperator;
import su.nsk.iae.post.poST.Expression;
import su.nsk.iae.post.poST.FunctionCall;
import su.nsk.iae.post.poST.IntegerLiteral;
import su.nsk.iae.post.poST.MulExpression;
import su.nsk.iae.post.poST.MulOperator;
import su.nsk.iae.post.poST.NumericLiteral;
import su.nsk.iae.post.poST.PowerExpression;
import su.nsk.iae.post.poST.PrimaryExpression;
import su.nsk.iae.post.poST.ProcessStatusExpression;
import su.nsk.iae.post.poST.RealLiteral;
import su.nsk.iae.post.poST.SignedInteger;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.TimeLiteral;
import su.nsk.iae.post.poST.UnaryExpression;
import su.nsk.iae.post.poST.UnaryOperator;
import su.nsk.iae.post.poST.XorExpression;

@SuppressWarnings("all")
public class ExpressionGenerator {
  public static String generate(final Expression exp, final GenerationContext ctx) {
    String _switchResult = null;
    boolean _matched = false;
    if (exp instanceof PrimaryExpression) {
      _matched=true;
      _switchResult = ExpressionGenerator.generatePrimary(((PrimaryExpression)exp), ctx);
    }
    if (!_matched) {
      if (exp instanceof UnaryExpression) {
        _matched=true;
        _switchResult = ExpressionGenerator.generateUnary(((UnaryExpression)exp), ctx);
      }
    }
    if (!_matched) {
      if (exp instanceof PowerExpression) {
        _matched=true;
        _switchResult = ExpressionGenerator.generatePower(((PowerExpression)exp), ctx);
      }
    }
    if (!_matched) {
      if (exp instanceof MulExpression) {
        _matched=true;
        String _xblockexpression = null;
        {
          final String t1 = ExpressionGenerator.requireType(((MulExpression)exp).getLeft(), ctx);
          final String t2 = ExpressionGenerator.requireType(((MulExpression)exp).getRight(), ctx);
          TypeUtil.promoteNumeric(t1, t2);
          String _xifexpression = null;
          MulOperator _mulOp = ((MulExpression)exp).getMulOp();
          boolean _equals = Objects.equals(_mulOp, MulOperator.DIV);
          if (_equals) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("((double)(");
            String _generate = ExpressionGenerator.generate(((MulExpression)exp).getLeft(), ctx);
            _builder.append(_generate);
            _builder.append(") / (double)(");
            String _generate_1 = ExpressionGenerator.generate(((MulExpression)exp).getRight(), ctx);
            _builder.append(_generate_1);
            _builder.append("))");
            _xifexpression = _builder.toString();
          } else {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append("(");
            String _generate_2 = ExpressionGenerator.generate(((MulExpression)exp).getLeft(), ctx);
            _builder_1.append(_generate_2);
            _builder_1.append(" ");
            String _mulOp_1 = ExpressionGenerator.mulOp(((MulExpression)exp).getMulOp());
            _builder_1.append(_mulOp_1);
            _builder_1.append(" ");
            String _generate_3 = ExpressionGenerator.generate(((MulExpression)exp).getRight(), ctx);
            _builder_1.append(_generate_3);
            _builder_1.append(")");
            _xifexpression = _builder_1.toString();
          }
          _xblockexpression = _xifexpression;
        }
        _switchResult = _xblockexpression;
      }
    }
    if (!_matched) {
      if (exp instanceof AddExpression) {
        _matched=true;
        String _xblockexpression = null;
        {
          final String t1 = ExpressionGenerator.requireType(((AddExpression)exp).getLeft(), ctx);
          final String t2 = ExpressionGenerator.requireType(((AddExpression)exp).getRight(), ctx);
          TypeUtil.promoteNumeric(t1, t2);
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("(");
          String _generate = ExpressionGenerator.generate(((AddExpression)exp).getLeft(), ctx);
          _builder.append(_generate);
          _builder.append(" ");
          String _addOp = ExpressionGenerator.addOp(((AddExpression)exp).getAddOp());
          _builder.append(_addOp);
          _builder.append(" ");
          String _generate_1 = ExpressionGenerator.generate(((AddExpression)exp).getRight(), ctx);
          _builder.append(_generate_1);
          _builder.append(")");
          _xblockexpression = _builder.toString();
        }
        _switchResult = _xblockexpression;
      }
    }
    if (!_matched) {
      if (exp instanceof EquExpression) {
        _matched=true;
        _switchResult = ExpressionGenerator.generateComparison(((EquExpression)exp).getLeft(), ((EquExpression)exp).getRight(), ((EquExpression)exp).getEquOp(), ctx);
      }
    }
    if (!_matched) {
      if (exp instanceof CompExpression) {
        _matched=true;
        _switchResult = ExpressionGenerator.generateComparison(((CompExpression)exp).getLeft(), ((CompExpression)exp).getRight(), ((CompExpression)exp).getCompOp(), ctx);
      }
    }
    if (!_matched) {
      if (exp instanceof AndExpression) {
        _matched=true;
        _switchResult = ExpressionGenerator.generateLogicalOrBitwise(((AndExpression)exp).getLeft(), ((AndExpression)exp).getRight(), "&&", "&", ctx, "AND");
      }
    }
    if (!_matched) {
      if (exp instanceof XorExpression) {
        _matched=true;
        _switchResult = ExpressionGenerator.generateLogicalOrBitwise(((XorExpression)exp).getLeft(), ((XorExpression)exp).getRight(), "^", "^", ctx, "XOR");
      }
    }
    if (!_matched) {
      if (exp instanceof Expression) {
        _matched=true;
        _switchResult = ExpressionGenerator.generateLogicalOrBitwise(exp.getLeft(), exp.getRight(), "||", "|", ctx, "OR");
      }
    }
    return _switchResult;
  }

  public static String getExprType(final Expression exp, final GenerationContext ctx) {
    boolean _matched = false;
    if (exp instanceof PrimaryExpression) {
      _matched=true;
      SymbolicVariable _variable = ((PrimaryExpression)exp).getVariable();
      boolean _tripleNotEquals = (_variable != null);
      if (_tripleNotEquals) {
        final String name = ((PrimaryExpression)exp).getVariable().getName();
        final String resolved = ctx.resolveAlias(name);
        boolean _hasConst = ctx.hasConst(resolved);
        if (_hasConst) {
          final Object value = ctx.getConst(resolved);
          String _switchResult_1 = null;
          boolean _matched_1 = false;
          if (value instanceof Boolean) {
            _matched_1=true;
            _switchResult_1 = "BOOL";
          }
          if (!_matched_1) {
            if (value instanceof Integer) {
              _matched_1=true;
              _switchResult_1 = "INT";
            }
          }
          if (!_matched_1) {
            if (value instanceof Long) {
              _matched_1=true;
              _switchResult_1 = "LINT";
            }
          }
          if (!_matched_1) {
            if (value instanceof Double) {
              _matched_1=true;
              _switchResult_1 = "LREAL";
            }
          }
          if (!_matched_1) {
            if (value instanceof Float) {
              _matched_1=true;
              _switchResult_1 = "REAL";
            }
          }
          if (!_matched_1) {
            if (value instanceof String) {
              _matched_1=true;
              _switchResult_1 = "STRING";
            }
          }
          if (!_matched_1) {
            throw new IllegalStateException(
              ("Unsupported const type: " + value));
          }
          return _switchResult_1;
        }
        return ctx.resolveVarType(resolved);
      }
      ArrayVariable _array = ((PrimaryExpression)exp).getArray();
      boolean _tripleNotEquals_1 = (_array != null);
      if (_tripleNotEquals_1) {
        final String arrName = ctx.resolveAlias(((PrimaryExpression)exp).getArray().getVariable().getName());
        return ctx.getArrayElementType(arrName);
      }
      ProcessStatusExpression _procStatus = ((PrimaryExpression)exp).getProcStatus();
      boolean _tripleNotEquals_2 = (_procStatus != null);
      if (_tripleNotEquals_2) {
        return "BOOL";
      }
      Constant _const = ((PrimaryExpression)exp).getConst();
      boolean _tripleNotEquals_3 = (_const != null);
      if (_tripleNotEquals_3) {
        final Constant c = ((PrimaryExpression)exp).getConst();
        NumericLiteral _num = c.getNum();
        if ((_num instanceof IntegerLiteral)) {
          NumericLiteral _num_1 = c.getNum();
          final IntegerLiteral lit = ((IntegerLiteral) _num_1);
          String _xifexpression = null;
          String _type = lit.getType();
          boolean _tripleNotEquals_4 = (_type != null);
          if (_tripleNotEquals_4) {
            _xifexpression = lit.getType();
          } else {
            _xifexpression = "INT";
          }
          return _xifexpression;
        }
        NumericLiteral _num_2 = c.getNum();
        if ((_num_2 instanceof RealLiteral)) {
          NumericLiteral _num_3 = c.getNum();
          final RealLiteral lit_1 = ((RealLiteral) _num_3);
          String _xifexpression_1 = null;
          String _type_1 = lit_1.getType();
          boolean _tripleNotEquals_5 = (_type_1 != null);
          if (_tripleNotEquals_5) {
            _xifexpression_1 = lit_1.getType();
          } else {
            _xifexpression_1 = "LREAL";
          }
          return _xifexpression_1;
        }
        TimeLiteral _time = c.getTime();
        boolean _tripleNotEquals_6 = (_time != null);
        if (_tripleNotEquals_6) {
          return "TIME";
        }
        if ((Objects.equals(c.getOth(), "TRUE") || Objects.equals(c.getOth(), "FALSE"))) {
          return "BOOL";
        }
        if (((c.getOth() != null) && 
          ((c.getOth().startsWith("2#") || 
            c.getOth().startsWith("8#")) || 
            c.getOth().startsWith("16#")))) {
          return "INT";
        }
      }
      Expression _nestExpr = ((PrimaryExpression)exp).getNestExpr();
      boolean _tripleNotEquals_7 = (_nestExpr != null);
      if (_tripleNotEquals_7) {
        return ExpressionGenerator.getExprType(((PrimaryExpression)exp).getNestExpr(), ctx);
      }
      throw new IllegalStateException(("Unknown primary expression: " + exp));
    }
    if (!_matched) {
      if (exp instanceof UnaryExpression) {
        _matched=true;
        final String t = ExpressionGenerator.getExprType(((UnaryExpression)exp).getRight(), ctx);
        UnaryOperator _unOp = ((UnaryExpression)exp).getUnOp();
        if (_unOp != null) {
          switch (_unOp) {
            case NOT:
              boolean _equals = Objects.equals(t, "BOOL");
              if (_equals) {
                return t;
              }
              if ((((TypeUtil.numericRank(t) > 0) && (!Objects.equals(t, "REAL"))) && (!Objects.equals(t, "LREAL")))) {
                return t;
              }
              throw new IllegalStateException(
                ("NOT applied to invalid type: " + t));
            case UNMINUS:
              boolean _isNumeric = TypeUtil.isNumeric(t);
              boolean _not = (!_isNumeric);
              if (_not) {
                throw new IllegalStateException(
                  ("Unary minus applied to non-numeric type: " + t));
              }
              return t;
            default:
              break;
          }
        }
      }
    }
    if (!_matched) {
      if (exp instanceof PowerExpression) {
        _matched=true;
        final String t1 = ExpressionGenerator.getExprType(((PowerExpression)exp).getLeft(), ctx);
        final String t2 = ExpressionGenerator.getExprType(((PowerExpression)exp).getRight(), ctx);
        if ((Objects.equals(t1, "TIME") || Objects.equals(t2, "TIME"))) {
          throw new IllegalStateException(
            "POWER operator not allowed for TIME");
        }
        return TypeUtil.promoteNumeric(t1, t2);
      }
    }
    if (!_matched) {
      if (exp instanceof MulExpression) {
        _matched=true;
        final String t1 = ExpressionGenerator.getExprType(((MulExpression)exp).getLeft(), ctx);
        final String t2 = ExpressionGenerator.getExprType(((MulExpression)exp).getRight(), ctx);
        return TypeUtil.promoteNumeric(t1, t2);
      }
    }
    if (!_matched) {
      if (exp instanceof AddExpression) {
        _matched=true;
        final String t1 = ExpressionGenerator.getExprType(((AddExpression)exp).getLeft(), ctx);
        final String t2 = ExpressionGenerator.getExprType(((AddExpression)exp).getRight(), ctx);
        return TypeUtil.promoteNumeric(t1, t2);
      }
    }
    if (!_matched) {
      if (exp instanceof EquExpression) {
        _matched=true;
      }
      if (!_matched) {
        if (exp instanceof CompExpression) {
          _matched=true;
        }
      }
      if (_matched) {
        return "BOOL";
      }
    }
    if (!_matched) {
      if (exp instanceof AndExpression) {
        _matched=true;
        final String t1 = ExpressionGenerator.getExprType(((AndExpression)exp).getLeft(), ctx);
        final String t2 = ExpressionGenerator.getExprType(((AndExpression)exp).getRight(), ctx);
        if ((Objects.equals(t1, "BOOL") && Objects.equals(t2, "BOOL"))) {
          return "BOOL";
        }
        boolean _canBitwise = TypeUtil.canBitwise(t1, t2);
        if (_canBitwise) {
          return TypeUtil.promoteNumeric(t1, t2);
        }
        throw new IllegalStateException(
          ((("AND not allowed for types: " + t1) + ", ") + t2));
      }
    }
    if (!_matched) {
      if (exp instanceof XorExpression) {
        _matched=true;
        final String t1 = ExpressionGenerator.getExprType(((XorExpression)exp).getLeft(), ctx);
        final String t2 = ExpressionGenerator.getExprType(((XorExpression)exp).getRight(), ctx);
        if ((Objects.equals(t1, "BOOL") && Objects.equals(t2, "BOOL"))) {
          return "BOOL";
        }
        boolean _canBitwise = TypeUtil.canBitwise(t1, t2);
        if (_canBitwise) {
          return TypeUtil.promoteNumeric(t1, t2);
        }
        throw new IllegalStateException(
          ((("XOR not allowed for types: " + t1) + ", ") + t2));
      }
    }
    if (!_matched) {
      if (exp instanceof Expression) {
        _matched=true;
        final String t1 = ExpressionGenerator.getExprType(exp.getLeft(), ctx);
        final String t2 = ExpressionGenerator.getExprType(exp.getRight(), ctx);
        if ((Objects.equals(t1, "BOOL") && Objects.equals(t2, "BOOL"))) {
          return "BOOL";
        }
        boolean _canBitwise = TypeUtil.canBitwise(t1, t2);
        if (_canBitwise) {
          return TypeUtil.promoteNumeric(t1, t2);
        }
        throw new IllegalStateException(
          ((("OR not allowed for types: " + t1) + ", ") + t2));
      }
    }
    if (!_matched) {
      throw new IllegalStateException(
        ("Unsupported expression type: " + exp));
    }
    return null;
  }

  private static String generatePower(final PowerExpression exp, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      final String leftType = ExpressionGenerator.requireType(exp.getLeft(), ctx);
      final String rightType = ExpressionGenerator.requireType(exp.getRight(), ctx);
      if ((Objects.equals(leftType, "TIME") || Objects.equals(rightType, "TIME"))) {
        throw new IllegalStateException(
          "POWER operator (**) is not allowed for TIME type");
      }
      final String resultType = TypeUtil.promoteNumeric(leftType, rightType);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Math.pow(");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("(double)");
      String _generate = ExpressionGenerator.generate(exp.getLeft(), ctx);
      _builder.append(_generate, "\t\t\t");
      _builder.append(",");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t\t");
      _builder.append("(double)");
      String _generate_1 = ExpressionGenerator.generate(exp.getRight(), ctx);
      _builder.append(_generate_1, "\t\t\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append(")");
      final String pow = _builder.toString();
      String _switchResult = null;
      boolean _matched = false;
      if (Objects.equals(resultType, "LREAL")) {
        _matched=true;
        _switchResult = pow;
      }
      if (!_matched) {
        if (Objects.equals(resultType, "REAL")) {
          _matched=true;
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("(float)");
          _builder_1.append(pow);
          _switchResult = _builder_1.toString();
        }
      }
      if (!_matched) {
        if (Objects.equals(resultType, Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("LINT", "ULINT", "LWORD")))) {
          _matched=true;
          StringConcatenation _builder_2 = new StringConcatenation();
          _builder_2.append("(long)");
          _builder_2.append(pow);
          _switchResult = _builder_2.toString();
        }
      }
      if (!_matched) {
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("(int)");
        _builder_3.append(pow);
        _switchResult = _builder_3.toString();
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }

  private static String generateComparison(final Expression leftExp, final Expression rightExp, final Object op, final GenerationContext ctx) {
    final String leftType = ExpressionGenerator.getExprType(leftExp, ctx);
    final String rightType = ExpressionGenerator.getExprType(rightExp, ctx);
    boolean _notEquals = (!Objects.equals(leftType, rightType));
    if (_notEquals) {
      if ((TypeUtil.isNumeric(leftType) && TypeUtil.isNumeric(rightType))) {
      } else {
        if (((Objects.equals(leftType, "STRING") || Objects.equals(leftType, "WSTRING")) && (Objects.equals(rightType, "STRING") || Objects.equals(rightType, "WSTRING")))) {
        } else {
          throw new IllegalStateException(
            ((("Cannot compare types: " + leftType) + " and ") + rightType));
        }
      }
    }
    final String left = ExpressionGenerator.generate(leftExp, ctx);
    final String right = ExpressionGenerator.generate(rightExp, ctx);
    if ((Objects.equals(leftType, "STRING") || Objects.equals(leftType, "WSTRING"))) {
      boolean _equals = Objects.equals(op, CompOperator.EQUAL);
      if (_equals) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("Objects.equals(");
        _builder.append(left);
        _builder.append(", ");
        _builder.append(right);
        _builder.append(")");
        return _builder.toString();
      }
      boolean _equals_1 = Objects.equals(op, CompOperator.NOT_EQUAL);
      if (_equals_1) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("!Objects.equals(");
        _builder_1.append(left);
        _builder_1.append(", ");
        _builder_1.append(right);
        _builder_1.append(")");
        return _builder_1.toString();
      }
      throw new IllegalStateException(
        "Ordering comparison not supported for STRING type");
    }
    if ((TypeUtil.isNumeric(leftType) && TypeUtil.isNumeric(rightType))) {
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("((double)(");
      _builder_2.append(left);
      _builder_2.append("))");
      final String leftVal = _builder_2.toString();
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.append("((double)(");
      _builder_3.append(right);
      _builder_3.append("))");
      final String rightVal = _builder_3.toString();
      String _xifexpression = null;
      if ((op instanceof CompOperator)) {
        String _xifexpression_1 = null;
        boolean _equals_2 = Objects.equals(op, CompOperator.EQUAL);
        if (_equals_2) {
          _xifexpression_1 = "==";
        } else {
          _xifexpression_1 = "!=";
        }
        _xifexpression = _xifexpression_1;
      } else {
        _xifexpression = ExpressionGenerator.equOp(((EquOperator) op));
      }
      final String operator = _xifexpression;
      StringConcatenation _builder_4 = new StringConcatenation();
      _builder_4.append("(");
      _builder_4.append(leftVal);
      _builder_4.append(" ");
      _builder_4.append(operator);
      _builder_4.append(" ");
      _builder_4.append(rightVal);
      _builder_4.append(")");
      return _builder_4.toString();
    }
    boolean _equals_3 = Objects.equals(leftType, "BOOL");
    if (_equals_3) {
      String _xifexpression_2 = null;
      if ((op instanceof CompOperator)) {
        String _xifexpression_3 = null;
        boolean _equals_4 = Objects.equals(op, CompOperator.EQUAL);
        if (_equals_4) {
          _xifexpression_3 = "==";
        } else {
          _xifexpression_3 = "!=";
        }
        _xifexpression_2 = _xifexpression_3;
      } else {
        throw new IllegalStateException(
          "Ordering comparison not allowed for BOOL");
      }
      final String operator_1 = _xifexpression_2;
      StringConcatenation _builder_5 = new StringConcatenation();
      _builder_5.append("(");
      _builder_5.append(left);
      _builder_5.append(" ");
      _builder_5.append(operator_1);
      _builder_5.append(" ");
      _builder_5.append(right);
      _builder_5.append(")");
      return _builder_5.toString();
    }
    throw new IllegalStateException(
      ((((("Unsupported comparison: " + leftType) + " ") + op) + " ") + rightType));
  }

  private static String generatePrimary(final PrimaryExpression exp, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      Constant _const = exp.getConst();
      boolean _tripleNotEquals = (_const != null);
      if (_tripleNotEquals) {
        return ExpressionGenerator.generateConstant(exp.getConst());
      }
      SymbolicVariable _variable = exp.getVariable();
      boolean _tripleNotEquals_1 = (_variable != null);
      if (_tripleNotEquals_1) {
        final String name = exp.getVariable().getName();
        final String resolved = ctx.resolveAlias(name);
        boolean _hasConst = ctx.hasConst(resolved);
        if (_hasConst) {
          final Object value = ctx.getConst(resolved);
          if ((value instanceof String)) {
            String _replace = ((String)value).replace("\"", "\\\"");
            String _plus = ("\"" + _replace);
            return (_plus + "\"");
          }
          return value.toString();
        }
        return ExpressionGenerator.readVar(name, ctx);
      }
      ArrayVariable _array = exp.getArray();
      boolean _tripleNotEquals_2 = (_array != null);
      if (_tripleNotEquals_2) {
        final String arrName = ctx.resolveAlias(exp.getArray().getVariable().getName());
        final String indexExpr = ExpressionGenerator.generate(exp.getArray().getIndex(), ctx);
        final String type = ctx.getArrayElementType(arrName);
        final String javaType = TypeUtil.javaType(type);
        final int start = ctx.getArrayStart(arrName);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("((");
        _builder.append(javaType);
        _builder.append(") getArrayValue(\"");
        _builder.append(arrName);
        _builder.append("\", ");
        _builder.append(indexExpr);
        _builder.append(", ");
        _builder.append(start);
        _builder.append("))");
        return _builder.toString();
      }
      ProcessStatusExpression _procStatus = exp.getProcStatus();
      boolean _tripleNotEquals_3 = (_procStatus != null);
      if (_tripleNotEquals_3) {
        return ExpressionGenerator.generateProcessStatus(exp.getProcStatus(), ctx);
      }
      FunctionCall _funCall = exp.getFunCall();
      boolean _tripleNotEquals_4 = (_funCall != null);
      if (_tripleNotEquals_4) {
        StringConcatenation _builder_1 = new StringConcatenation();
        String _name = exp.getFunCall().getFunction().getName();
        _builder_1.append(_name);
        _builder_1.append("()");
        return _builder_1.toString();
      }
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("(");
      String _generate = ExpressionGenerator.generate(exp.getNestExpr(), ctx);
      _builder_2.append(_generate);
      _builder_2.append(")");
      _xblockexpression = _builder_2.toString();
    }
    return _xblockexpression;
  }

  public static String readVar(final String name, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      final String resolved = ctx.resolveAlias(name);
      boolean _hasConst = ctx.hasConst(name);
      if (_hasConst) {
        return ctx.getConst(name).toString();
      }
      boolean _hasConst_1 = ctx.hasConst(resolved);
      if (_hasConst_1) {
        final Object value = ctx.getConst(resolved);
        if ((value instanceof String)) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("\"");
          _builder.append(((String)value));
          _builder.append("\"");
          return _builder.toString();
        }
        return value.toString();
      }
      String resolved2 = ctx.resolveVarName(name);
      final String javaType = TypeUtil.javaType(ctx.resolveVarType(name));
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("((");
      _builder_1.append(javaType);
      _builder_1.append(")memory.get(\"");
      _builder_1.append(resolved2);
      _builder_1.append("\"))");
      _xblockexpression = _builder_1.toString();
    }
    return _xblockexpression;
  }

  public static String writeVar(final String name, final String valueExpr, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      final String resolved = ctx.resolveVarName(name);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("memory.put(\"");
      _builder.append(resolved);
      _builder.append("\", ");
      _builder.append(valueExpr);
      _builder.append(");");
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }

  private static String generateUnary(final UnaryExpression exp, final GenerationContext ctx) {
    String _switchResult = null;
    UnaryOperator _unOp = exp.getUnOp();
    if (_unOp != null) {
      switch (_unOp) {
        case NOT:
          final String t = ExpressionGenerator.requireType(exp.getRight(), ctx);
          boolean _equals = Objects.equals(t, "BOOL");
          if (_equals) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("!(");
            String _generate = ExpressionGenerator.generate(exp.getRight(), ctx);
            _builder.append(_generate);
            _builder.append(")");
            return _builder.toString();
          }
          boolean _isBitwiseInteger = TypeUtil.isBitwiseInteger(t);
          if (_isBitwiseInteger) {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append("~(");
            String _generate_1 = ExpressionGenerator.generate(exp.getRight(), ctx);
            _builder_1.append(_generate_1);
            _builder_1.append(")");
            return _builder_1.toString();
          }
          throw new IllegalStateException(
            ("NOT not allowed for type: " + t));
        case UNMINUS:
          String _xblockexpression = null;
          {
            final String type = ExpressionGenerator.requireType(exp.getRight(), ctx);
            boolean _isNumeric = TypeUtil.isNumeric(type);
            boolean _not = (!_isNumeric);
            if (_not) {
              throw new IllegalStateException(
                ("Unary minus applied to non-numeric type: " + type));
            }
            StringConcatenation _builder_2 = new StringConcatenation();
            _builder_2.append("-(");
            String _generate_2 = ExpressionGenerator.generate(exp.getRight(), ctx);
            _builder_2.append(_generate_2);
            _builder_2.append(")");
            _xblockexpression = _builder_2.toString();
          }
          _switchResult = _xblockexpression;
          break;
        default:
          break;
      }
    }
    return _switchResult;
  }

  private static String generateConstant(final Constant c) {
    String _xblockexpression = null;
    {
      NumericLiteral _num = c.getNum();
      boolean _tripleNotEquals = (_num != null);
      if (_tripleNotEquals) {
        NumericLiteral _num_1 = c.getNum();
        if ((_num_1 instanceof IntegerLiteral)) {
          NumericLiteral _num_2 = c.getNum();
          final IntegerLiteral lit = ((IntegerLiteral) _num_2);
          final SignedInteger signed = lit.getValue();
          String _xifexpression = null;
          boolean _isISig = signed.isISig();
          if (_isISig) {
            _xifexpression = "-";
          } else {
            _xifexpression = "";
          }
          final String sign = _xifexpression;
          String _value = signed.getValue();
          return (sign + _value);
        }
        NumericLiteral _num_3 = c.getNum();
        if ((_num_3 instanceof RealLiteral)) {
          NumericLiteral _num_4 = c.getNum();
          final RealLiteral lit_1 = ((RealLiteral) _num_4);
          String _xifexpression_1 = null;
          boolean _isRSig = lit_1.isRSig();
          if (_isRSig) {
            _xifexpression_1 = "-";
          } else {
            _xifexpression_1 = "";
          }
          final String sign_1 = _xifexpression_1;
          String _value_1 = lit_1.getValue();
          return (sign_1 + _value_1);
        }
      }
      TimeLiteral _time = c.getTime();
      boolean _tripleNotEquals_1 = (_time != null);
      if (_tripleNotEquals_1) {
        return MemoryUtil.parseTime(c.getTime());
      }
      String _oth = c.getOth();
      boolean _equals = Objects.equals(_oth, "TRUE");
      if (_equals) {
        return "true";
      }
      String _oth_1 = c.getOth();
      boolean _equals_1 = Objects.equals(_oth_1, "FALSE");
      if (_equals_1) {
        return "false";
      }
      _xblockexpression = c.getOth();
    }
    return _xblockexpression;
  }

  private static String generateProcessStatus(final ProcessStatusExpression exp, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      final String fieldName = ctx.resolveProcess(exp.getProcess().getName());
      boolean _isActive = exp.isActive();
      if (_isActive) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("isActive(");
        _builder.append(fieldName);
        _builder.append(")");
        return _builder.toString();
      }
      boolean _isInactive = exp.isInactive();
      if (_isInactive) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("isInactive(");
        _builder_1.append(fieldName);
        _builder_1.append(")");
        return _builder_1.toString();
      }
      boolean _isStop = exp.isStop();
      if (_isStop) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("isStop(");
        _builder_2.append(fieldName);
        _builder_2.append(")");
        return _builder_2.toString();
      }
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.append("isError(");
      _builder_3.append(fieldName);
      _builder_3.append(")");
      _xblockexpression = _builder_3.toString();
    }
    return _xblockexpression;
  }

  private static String addOp(final AddOperator op) {
    String _xifexpression = null;
    boolean _equals = Objects.equals(op, AddOperator.PLUS);
    if (_equals) {
      _xifexpression = "+";
    } else {
      _xifexpression = "-";
    }
    return _xifexpression;
  }

  private static String mulOp(final MulOperator op) {
    String _switchResult = null;
    if (op != null) {
      switch (op) {
        case MUL:
          _switchResult = "*";
          break;
        case DIV:
          _switchResult = "/";
          break;
        case MOD:
          _switchResult = "%";
          break;
        default:
          break;
      }
    }
    return _switchResult;
  }

  private static String equOp(final EquOperator op) {
    String _switchResult = null;
    if (op != null) {
      switch (op) {
        case LESS:
          _switchResult = "<";
          break;
        case LESS_EQU:
          _switchResult = "<=";
          break;
        case GREATER:
          _switchResult = ">";
          break;
        case GREATER_EQU:
          _switchResult = ">=";
          break;
        default:
          break;
      }
    }
    return _switchResult;
  }

  private static String requireType(final Expression exp, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      final String type = ExpressionGenerator.getExprType(exp, ctx);
      if ((type == null)) {
        throw new IllegalStateException(
          ("Cannot determine type of expression: " + exp));
      }
      _xblockexpression = type;
    }
    return _xblockexpression;
  }

  private static String generateLogicalOrBitwise(final Expression left, final Expression right, final String boolOp, final String bitOp, final GenerationContext ctx, final String opName) {
    final String t1 = ExpressionGenerator.requireType(left, ctx);
    final String t2 = ExpressionGenerator.requireType(right, ctx);
    if ((Objects.equals(t1, "BOOL") && Objects.equals(t2, "BOOL"))) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("(");
      String _generate = ExpressionGenerator.generate(left, ctx);
      _builder.append(_generate);
      _builder.append(" ");
      _builder.append(boolOp);
      _builder.append(" ");
      String _generate_1 = ExpressionGenerator.generate(right, ctx);
      _builder.append(_generate_1);
      _builder.append(")");
      return _builder.toString();
    }
    boolean _canBitwise = TypeUtil.canBitwise(t1, t2);
    if (_canBitwise) {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("(");
      String _generate_2 = ExpressionGenerator.generate(left, ctx);
      _builder_1.append(_generate_2);
      _builder_1.append(" ");
      _builder_1.append(bitOp);
      _builder_1.append(" ");
      String _generate_3 = ExpressionGenerator.generate(right, ctx);
      _builder_1.append(_generate_3);
      _builder_1.append(")");
      return _builder_1.toString();
    }
    throw new IllegalStateException(
      ((((opName + " not allowed for types: ") + t1) + ", ") + t2));
  }
}
