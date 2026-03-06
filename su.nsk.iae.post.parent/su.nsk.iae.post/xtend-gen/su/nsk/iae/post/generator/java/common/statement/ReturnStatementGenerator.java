package su.nsk.iae.post.generator.java.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.SubprogramControlStatement;

@SuppressWarnings("all")
public class ReturnStatementGenerator implements IStatementGenerator {
  @Override
  public boolean supports(final Statement stmt) {
    return (stmt instanceof SubprogramControlStatement);
  }

  @Override
  public String generate(final Statement stmt, final GenerationContext ctx, final String indent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("return;");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
}
