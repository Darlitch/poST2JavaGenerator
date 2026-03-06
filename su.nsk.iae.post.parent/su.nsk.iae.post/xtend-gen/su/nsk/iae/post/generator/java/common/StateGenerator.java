package su.nsk.iae.post.generator.java.common;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.statement.StatementListGenerator;
import su.nsk.iae.post.poST.State;

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
      _builder.append(indent);
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      builder.append(_builder);
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
}
