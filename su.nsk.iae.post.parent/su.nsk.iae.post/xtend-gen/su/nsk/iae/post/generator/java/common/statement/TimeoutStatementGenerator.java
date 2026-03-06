package su.nsk.iae.post.generator.java.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.util.ExpressionGenerator;
import su.nsk.iae.post.generator.java.common.util.MemoryUtil;
import su.nsk.iae.post.poST.Constant;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.TimeoutStatement;

@SuppressWarnings("all")
public class TimeoutStatementGenerator implements IStatementGenerator {
  private final StatementListGenerator stmtGen;

  public TimeoutStatementGenerator(final StatementListGenerator stmtGen) {
    this.stmtGen = stmtGen;
  }

  @Override
  public boolean supports(final Statement stmt) {
    return (stmt instanceof TimeoutStatement);
  }

  @Override
  public String generate(final Statement stmt, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final TimeoutStatement s = ((TimeoutStatement) stmt);
      final String nextIndent = (indent + "    ");
      String _xifexpression = null;
      Constant _const = s.getConst();
      boolean _tripleNotEquals = (_const != null);
      if (_tripleNotEquals) {
        _xifexpression = MemoryUtil.parseTime(s.getConst().getTime());
      } else {
        _xifexpression = ExpressionGenerator.readVar(s.getVariable().getName(), ctx);
      }
      final String timeoutExpr = _xifexpression;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(indent);
      _builder.append("if (((Long)memory.get(\"");
      String _globalTime = MemoryUtil.globalTime();
      _builder.append(_globalTime);
      _builder.append("\")) - this.");
      String _timerField = MemoryUtil.timerField();
      _builder.append(_timerField);
      _builder.append(" >= ");
      _builder.append(timeoutExpr);
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      String _generate = this.stmtGen.generate(s.getStatement(), ctx, nextIndent);
      _builder.append(_generate);
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
}
