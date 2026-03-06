package su.nsk.iae.post.generator.java.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.util.ExpressionGenerator;
import su.nsk.iae.post.generator.java.common.util.TypeUtil;
import su.nsk.iae.post.poST.Expression;
import su.nsk.iae.post.poST.ForStatement;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.SymbolicVariable;

@SuppressWarnings("all")
public class ForStatementGenerator implements IStatementGenerator {
  private final StatementListGenerator stmtGen;

  public ForStatementGenerator(final StatementListGenerator stmtGen) {
    this.stmtGen = stmtGen;
  }

  @Override
  public boolean supports(final Statement stmt) {
    return (stmt instanceof ForStatement);
  }

  @Override
  public String generate(final Statement stmt, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final ForStatement s = ((ForStatement) stmt);
      final StringBuilder builder = new StringBuilder();
      final String nextIndent = (indent + "    ");
      SymbolicVariable _variable = s.getVariable();
      final String varName = ((SymbolicVariable) _variable).getName();
      final String resolved = ctx.resolveVarName(varName);
      final String startExpr = ExpressionGenerator.generate(s.getForList().getStart(), ctx);
      final String endExpr = ExpressionGenerator.generate(s.getForList().getEnd(), ctx);
      String _xifexpression = null;
      Expression _step = s.getForList().getStep();
      boolean _tripleNotEquals = (_step != null);
      if (_tripleNotEquals) {
        _xifexpression = ExpressionGenerator.generate(s.getForList().getStep(), ctx);
      } else {
        _xifexpression = "1";
      }
      final String stepExpr = _xifexpression;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(indent);
      _builder.append("int __start = ");
      String _int = TypeUtil.toInt(startExpr);
      _builder.append(_int);
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("int __end   = ");
      String _int_1 = TypeUtil.toInt(endExpr);
      _builder.append(_int_1);
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("int __step  = ");
      String _int_2 = TypeUtil.toInt(stepExpr);
      _builder.append(_int_2);
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(indent);
      _builder.append("if (__step == 0)");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("    throw new RuntimeException(\"FOR step cannot be zero\");");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(indent);
      _builder.append("memory.put(\"");
      _builder.append(resolved);
      _builder.append("\", __start);");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(indent);
      _builder.append("while (");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("       (__step >= 0 && ");
      String _readVar = ExpressionGenerator.readVar(varName, ctx);
      _builder.append(_readVar);
      _builder.append(" <= __end)");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("    || (__step < 0  && ");
      String _readVar_1 = ExpressionGenerator.readVar(varName, ctx);
      _builder.append(_readVar_1);
      _builder.append(" >= __end)");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      String _generate = this.stmtGen.generate(s.getStatement(), ctx, nextIndent);
      _builder.append(_generate);
      _builder.newLineIfNotEmpty();
      _builder.append(nextIndent);
      _builder.append("memory.put(");
      _builder.newLineIfNotEmpty();
      _builder.append(nextIndent);
      _builder.append("    \"");
      _builder.append(resolved);
      _builder.append("\",");
      _builder.newLineIfNotEmpty();
      _builder.append(nextIndent);
      _builder.append("    ");
      String _readVar_2 = ExpressionGenerator.readVar(varName, ctx);
      _builder.append(_readVar_2);
      _builder.append(" + __step");
      _builder.newLineIfNotEmpty();
      _builder.append(nextIndent);
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      builder.append(_builder);
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
}
