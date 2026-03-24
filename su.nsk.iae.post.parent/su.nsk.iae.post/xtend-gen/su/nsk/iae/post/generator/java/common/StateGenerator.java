package su.nsk.iae.post.generator.java.common;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.statement.StatementListGenerator;
import su.nsk.iae.post.generator.java.common.statement.TimeoutStatementGenerator;
import su.nsk.iae.post.poST.State;
import su.nsk.iae.post.poST.TimeoutStatement;

@SuppressWarnings("all")
public class StateGenerator {
  private final StatementListGenerator stmtGen;

  public StateGenerator(final StatementListGenerator stmtGen) {
    this.stmtGen = stmtGen;
  }

  public String generate(final State state, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      final String nextIndent = (indent + "    ");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(indent);
      _builder.append("case ");
      String _name = state.getName();
      _builder.append(_name);
      _builder.append(" -> {");
      _builder.newLineIfNotEmpty();
      String _generate = this.stmtGen.generate(state.getStatement(), ctx, nextIndent);
      _builder.append(_generate);
      _builder.newLineIfNotEmpty();
      builder.append(_builder);
      TimeoutStatement _timeout = state.getTimeout();
      boolean _tripleNotEquals = (_timeout != null);
      if (_tripleNotEquals) {
        builder.append(
          new TimeoutStatementGenerator(this.stmtGen).generateTimeout(state.getTimeout(), ctx, nextIndent));
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(indent);
      _builder_1.append("}");
      _builder_1.newLineIfNotEmpty();
      builder.append(_builder_1);
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
}
