package su.nsk.iae.post.generator.java.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.util.MemoryUtil;
import su.nsk.iae.post.poST.ResetTimerStatement;
import su.nsk.iae.post.poST.Statement;

@SuppressWarnings("all")
public class ResetTimerStatementGenerator implements IStatementGenerator {
  @Override
  public boolean supports(final Statement stmt) {
    return (stmt instanceof ResetTimerStatement);
  }

  @Override
  public String generate(final Statement stmt, final GenerationContext ctx, final String indent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    String _timerField = MemoryUtil.timerField();
    _builder.append(_timerField);
    _builder.append(" = ((Long)memory.get(\"");
    String _globalTime = MemoryUtil.globalTime();
    _builder.append(_globalTime);
    _builder.append("\"));");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
}
