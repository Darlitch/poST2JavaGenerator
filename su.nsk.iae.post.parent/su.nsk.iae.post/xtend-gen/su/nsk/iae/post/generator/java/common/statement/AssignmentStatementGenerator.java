package su.nsk.iae.post.generator.java.common.statement;

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
    final String valueExpr = ExpressionGenerator.generate(s.getValue(), ctx);
    SymbolicVariable _variable = s.getVariable();
    if ((_variable instanceof SymbolicVariable)) {
      SymbolicVariable _variable_1 = s.getVariable();
      final String name = ((SymbolicVariable) _variable_1).getName();
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
    SymbolicVariable _variable_2 = s.getVariable();
    if ((_variable_2 instanceof ArrayVariable)) {
      SymbolicVariable _variable_3 = s.getVariable();
      final ArrayVariable arr = ((ArrayVariable) _variable_3);
      final String arrName = ctx.resolveAlias(arr.getVariable().getName());
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
      final String nextIndent = (indent + "    ");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(indent);
      _builder.append("{");
      _builder.newLineIfNotEmpty();
      _builder.append(nextIndent);
      _builder.append("int __idx = ");
      String _int = TypeUtil.toInt(indexExpr);
      _builder.append(_int);
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _builder.append(nextIndent);
      _builder.append("int __offset = __idx - ");
      _builder.append(start);
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(nextIndent);
      _builder.append("java.util.List<String> __list =");
      _builder.newLineIfNotEmpty();
      _builder.append(nextIndent);
      _builder.append("    (java.util.List<String>) memory.get(\"");
      _builder.append(arrName);
      _builder.append("\");");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(nextIndent);
      _builder.append("if (__offset < 0 || __offset >= __list.size())");
      _builder.newLineIfNotEmpty();
      _builder.append(nextIndent);
      _builder.append("    throw new RuntimeException(");
      _builder.newLineIfNotEmpty();
      _builder.append(nextIndent);
      _builder.append("        \"Array index out of bounds: ");
      _builder.append(arrName);
      _builder.append("[\" + __idx + \"]\"");
      _builder.newLineIfNotEmpty();
      _builder.append(nextIndent);
      _builder.append("    );");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(nextIndent);
      _builder.append("String __cell = __list.get(__offset);");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(nextIndent);
      _builder.append("memory.put(__cell, ");
      _builder.append(valueExpr);
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      builder.append(_builder);
      return builder.toString();
    }
    SymbolicVariable _variable_4 = s.getVariable();
    String _plus_1 = ("Unsupported assignment target: " + _variable_4);
    throw new IllegalStateException(_plus_1);
  }
}
