package su.nsk.iae.post.generator.java.common.vars;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.util.ExpressionGenerator;
import su.nsk.iae.post.generator.java.common.util.TypeUtil;
import su.nsk.iae.post.poST.ArraySpecificationInit;
import su.nsk.iae.post.poST.SimpleSpecificationInit;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.VarInitDeclaration;

@SuppressWarnings("all")
public class VarMemoryGenerator {
  public static String generate(final VarInitDeclaration decl, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      String _xifexpression = null;
      SimpleSpecificationInit _spec = decl.getSpec();
      boolean _tripleNotEquals = (_spec != null);
      if (_tripleNotEquals) {
        _xifexpression = decl.getSpec().getType();
      } else {
        String _xifexpression_1 = null;
        ArraySpecificationInit _arrSpec = decl.getArrSpec();
        boolean _tripleNotEquals_1 = (_arrSpec != null);
        if (_tripleNotEquals_1) {
          _xifexpression_1 = decl.getArrSpec().getInit().getType();
        } else {
          _xifexpression_1 = null;
        }
        _xifexpression = _xifexpression_1;
      }
      final String type = _xifexpression;
      EList<SymbolicVariable> _vars = decl.getVarList().getVars();
      for (final SymbolicVariable v : _vars) {
        {
          final String name = v.getName();
          final String resolved = ctx.resolveAlias(name);
          if ((((!ctx.hasArrayStart(resolved)) && (!ctx.hasProcess(resolved))) && (!ctx.hasAlias(name)))) {
            String _xifexpression_2 = null;
            if (((decl.getSpec() != null) && (decl.getSpec().getValue() != null))) {
              _xifexpression_2 = ExpressionGenerator.generate(decl.getSpec().getValue(), ctx);
            } else {
              String _xifexpression_3 = null;
              if ((type != null)) {
                _xifexpression_3 = TypeUtil.defaultValue(type);
              } else {
                _xifexpression_3 = "null";
              }
              _xifexpression_2 = _xifexpression_3;
            }
            final String init = _xifexpression_2;
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
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
}
