package su.nsk.iae.post.generator.java.common.statement;

import java.util.Objects;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.util.ExpressionGenerator;
import su.nsk.iae.post.generator.java.common.util.TypeUtil;
import su.nsk.iae.post.poST.ArrayVariable;
import su.nsk.iae.post.poST.AssignmentStatement;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.SymbolicVariable;

@SuppressWarnings("all")
public class AssignmentStatementGenerator implements IStatementGenerator {
  @Override
  public boolean supports(final Statement stmt) {
    return (stmt instanceof AssignmentStatement);
  }

  @Override
  public String generate(final Statement stmt, final GenerationContext ctx, final String indent) {
    final AssignmentStatement s = ((AssignmentStatement) stmt);
    final StringBuilder builder = new StringBuilder();
    String _xifexpression = null;
    SymbolicVariable _variable = s.getVariable();
    boolean _tripleNotEquals = (_variable != null);
    if (_tripleNotEquals) {
      _xifexpression = ctx.resolveVarType(s.getVariable().getName());
    } else {
      _xifexpression = ctx.getArrayElementType(
        ctx.resolveAlias(s.getArray().getVariable().getName()));
    }
    final String leftType = _xifexpression;
    final String rightType = ExpressionGenerator.getExprType(s.getValue(), ctx);
    String valueExpr = ExpressionGenerator.generate(s.getValue(), ctx);
    if ((((!Objects.equals(leftType, rightType)) && TypeUtil.isNumeric(leftType)) && TypeUtil.isNumeric(rightType))) {
      valueExpr = TypeUtil.castTo(valueExpr, leftType);
    }
    SymbolicVariable _variable_1 = s.getVariable();
    if ((_variable_1 instanceof SymbolicVariable)) {
      SymbolicVariable _variable_2 = s.getVariable();
      final String name = ((SymbolicVariable) _variable_2).getName();
      final String varType = ctx.resolveVarType(name);
      final String exprType = ExpressionGenerator.getExprType(s.getValue(), ctx);
      boolean _canAssign = TypeUtil.canAssign(varType, exprType);
      boolean _not = (!_canAssign);
      if (_not) {
        throw new IllegalStateException(
          ((("Type mismatch in assignment: " + varType) + " := ") + exprType));
      }
      String _writeVar = ExpressionGenerator.writeVar(name, valueExpr, ctx);
      String _plus = (indent + _writeVar);
      builder.append(_plus);
      return builder.toString();
    }
    ArrayVariable _array = s.getArray();
    boolean _tripleNotEquals_1 = (_array != null);
    if (_tripleNotEquals_1) {
      final ArrayVariable arr = s.getArray();
      final String arrName = arr.getVariable().getName();
      final String elementType = ctx.getArrayElementType(arrName);
      final String exprType_1 = ExpressionGenerator.getExprType(s.getValue(), ctx);
      boolean _canAssign_1 = TypeUtil.canAssign(elementType, exprType_1);
      boolean _not_1 = (!_canAssign_1);
      if (_not_1) {
        throw new IllegalStateException(
          ((("Type mismatch in array assignment: " + elementType) + " := ") + exprType_1));
      }
      final int start = ctx.getArrayStart(arrName);
      final String indexExpr = ExpressionGenerator.generate(arr.getIndex(), ctx);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("setArrayValue(\"");
      _builder.append(arrName);
      _builder.append("\", ");
      _builder.append(indexExpr);
      _builder.append(", ");
      _builder.append(start);
      _builder.append(", ");
      _builder.append(valueExpr);
      _builder.append(");");
      String _plus_1 = (indent + _builder);
      builder.append(_plus_1);
      return builder.toString();
    }
    SymbolicVariable _variable_3 = s.getVariable();
    String _plus_2 = ("Unsupported assignment target: " + _variable_3);
    throw new IllegalStateException(_plus_2);
  }
}
