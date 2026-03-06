package su.nsk.iae.post.generator.java.common.statement;

import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.poST.Statement;

@SuppressWarnings("all")
public interface IStatementGenerator {
  boolean supports(final Statement stmt);

  String generate(final Statement stmt, final GenerationContext ctx, final String indent);
}
