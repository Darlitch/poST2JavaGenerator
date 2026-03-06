package su.nsk.iae.post.generator.java.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.poST.SetStateStatement;
import su.nsk.iae.post.poST.Statement;

@SuppressWarnings("all")
public class SetStateStatementGenerator implements IStatementGenerator {
  @Override
  public boolean supports(final Statement stmt) {
    return (stmt instanceof SetStateStatement);
  }

  @Override
  public String generate(final Statement stmt, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final SetStateStatement s = ((SetStateStatement) stmt);
      boolean _isNext = s.isNext();
      if (_isNext) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(indent);
        _builder.append("setNext();");
        _builder.newLineIfNotEmpty();
        return _builder.toString();
      }
      final String stateName = s.getState().getName();
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(indent);
      _builder_1.append("setState(State.");
      _builder_1.append(stateName);
      _builder_1.append(");");
      _builder_1.newLineIfNotEmpty();
      _xblockexpression = _builder_1.toString();
    }
    return _xblockexpression;
  }
}
