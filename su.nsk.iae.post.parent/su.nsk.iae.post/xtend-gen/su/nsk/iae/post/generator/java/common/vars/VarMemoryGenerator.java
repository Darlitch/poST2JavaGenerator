package su.nsk.iae.post.generator.java.common.vars;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.util.ExpressionGenerator;
import su.nsk.iae.post.generator.java.common.util.TypeUtil;
import su.nsk.iae.post.poST.Expression;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.VarInitDeclaration;

@SuppressWarnings("all")
public class VarMemoryGenerator {
  public static String generate(final VarInitDeclaration decl, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      final String type = decl.getSpec().getType();
      EList<SymbolicVariable> _vars = decl.getVarList().getVars();
      for (final SymbolicVariable v : _vars) {
        {
          final String name = v.getName();
          String _xifexpression = null;
          Expression _value = decl.getSpec().getValue();
          boolean _tripleNotEquals = (_value != null);
          if (_tripleNotEquals) {
            _xifexpression = ExpressionGenerator.generate(decl.getSpec().getValue(), ctx);
          } else {
            _xifexpression = TypeUtil.defaultValue(type);
          }
          final String init = _xifexpression;
          StringConcatenation _builder = new StringConcatenation();
          _builder.append(indent);
          _builder.append("memory.put(\"");
          _builder.append(name);
          _builder.append("\", ");
          _builder.append(init);
          _builder.append(");");
          _builder.newLineIfNotEmpty();
          builder.append(_builder);
          ctx.registerVar(name, type);
        }
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
}
