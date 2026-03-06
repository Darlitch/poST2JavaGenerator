package su.nsk.iae.post.generator.java.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.util.ExpressionGenerator;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.WhileStatement;

@SuppressWarnings("all")
public class WhileStatementGenerator implements IStatementGenerator {
  private final StatementListGenerator stmtGen;

  public WhileStatementGenerator(final StatementListGenerator stmtGen) {
    this.stmtGen = stmtGen;
  }

  @Override
  public boolean supports(final Statement stmt) {
    return (stmt instanceof WhileStatement);
  }

  @Override
  public String generate(final Statement stmt, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final WhileStatement s = ((WhileStatement) stmt);
      final StringBuilder builder = new StringBuilder();
      final String nextIndent = (indent + "    ");
      final String cond = ExpressionGenerator.generate(s.getCond(), ctx);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(indent);
      _builder.append("while (");
      _builder.append(cond);
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      String _generate = this.stmtGen.generate(s.getStatement(), ctx, nextIndent);
      _builder.append(_generate);
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
