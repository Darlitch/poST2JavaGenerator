package su.nsk.iae.post.generator.java.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.poST.ErrorProcessStatement;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.Variable;

@SuppressWarnings("all")
public class ErrorProcessStatementGenerator implements IStatementGenerator {
  @Override
  public boolean supports(final Statement stmt) {
    return (stmt instanceof ErrorProcessStatement);
  }

  @Override
  public String generate(final Statement stmt, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final ErrorProcessStatement s = ((ErrorProcessStatement) stmt);
      Variable _process = s.getProcess();
      boolean _tripleNotEquals = (_process != null);
      if (_tripleNotEquals) {
        final String fieldName = ctx.resolveProcess(s.getProcess().getName());
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(indent);
        _builder.append(fieldName);
        _builder.append(".error();");
        _builder.newLineIfNotEmpty();
        return _builder.toString();
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(indent);
      _builder_1.append("this.error();");
      _builder_1.newLineIfNotEmpty();
      _xblockexpression = _builder_1.toString();
    }
    return _xblockexpression;
  }
}
