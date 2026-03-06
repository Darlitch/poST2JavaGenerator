package su.nsk.iae.post.generator.java.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.poST.ExitStatement;
import su.nsk.iae.post.poST.Statement;

@SuppressWarnings("all")
public class ExitStatementGenerator implements IStatementGenerator {
  @Override
  public boolean supports(final Statement stmt) {
    return (stmt instanceof ExitStatement);
  }

  @Override
  public String generate(final Statement stmt, final GenerationContext ctx, final String indent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("break;");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
}
