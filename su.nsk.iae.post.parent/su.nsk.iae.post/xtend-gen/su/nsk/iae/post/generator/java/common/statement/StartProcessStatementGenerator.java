package su.nsk.iae.post.generator.java.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.poST.StartProcessStatement;
import su.nsk.iae.post.poST.Statement;

@SuppressWarnings("all")
public class StartProcessStatementGenerator implements IStatementGenerator {
  @Override
  public boolean supports(final Statement stmt) {
    return ((stmt instanceof StartProcessStatement) && 
      (((StartProcessStatement) stmt).getProcess() != null));
  }

  @Override
  public String generate(final Statement stmt, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final StartProcessStatement s = ((StartProcessStatement) stmt);
      final String name = s.getProcess().getName();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(indent);
      _builder.append("getProcess(\"");
      _builder.append(name);
      _builder.append("\").start();");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
}
