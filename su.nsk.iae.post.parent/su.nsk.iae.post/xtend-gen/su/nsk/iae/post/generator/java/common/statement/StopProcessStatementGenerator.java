package su.nsk.iae.post.generator.java.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.StopProcessStatement;
import su.nsk.iae.post.poST.Variable;

@SuppressWarnings("all")
public class StopProcessStatementGenerator implements IStatementGenerator {
  @Override
  public boolean supports(final Statement stmt) {
    return (stmt instanceof StopProcessStatement);
  }

  @Override
  public String generate(final Statement stmt, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final StopProcessStatement s = ((StopProcessStatement) stmt);
      Variable _process = s.getProcess();
      boolean _tripleNotEquals = (_process != null);
      if (_tripleNotEquals) {
        final String processName = s.getProcess().getName();
        boolean _hasProcess = ctx.hasProcess(processName);
        boolean _not = (!_hasProcess);
        if (_not) {
          throw new IllegalStateException(
            ("Unknown process in STOP PROCESS: " + processName));
        }
        final String name = ctx.resolveProcess(processName);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(indent);
        _builder.append("getProcess(\"");
        _builder.append(name);
        _builder.append("\").stop();");
        _builder.newLineIfNotEmpty();
        return _builder.toString();
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(indent);
      _builder_1.append("this.stop();");
      _builder_1.newLineIfNotEmpty();
      _xblockexpression = _builder_1.toString();
    }
    return _xblockexpression;
  }
}
