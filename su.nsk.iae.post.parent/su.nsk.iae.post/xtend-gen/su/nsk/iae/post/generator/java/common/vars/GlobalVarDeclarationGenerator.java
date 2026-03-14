package su.nsk.iae.post.generator.java.common.vars;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.util.TypeUtil;
import su.nsk.iae.post.poST.ArraySpecificationInit;
import su.nsk.iae.post.poST.GlobalVarDeclaration;
import su.nsk.iae.post.poST.GlobalVarInitDeclaration;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.VarInitDeclaration;

@SuppressWarnings("all")
public class GlobalVarDeclarationGenerator {
  public static String generate(final GlobalVarDeclaration decl, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      EList<VarInitDeclaration> _varsSimple = decl.getVarsSimple();
      for (final VarInitDeclaration v : _varsSimple) {
        {
          EList<SymbolicVariable> _vars = v.getVarList().getVars();
          for (final SymbolicVariable vname : _vars) {
            ctx.registerGlobalVar(vname.getName());
          }
          ArraySpecificationInit _arrSpec = v.getArrSpec();
          boolean _tripleNotEquals = (_arrSpec != null);
          if (_tripleNotEquals) {
            builder.append(
              ArrayMemoryGenerator.generate(v, ctx));
          } else {
            builder.append(
              VarMemoryGenerator.generate(v, ctx));
          }
        }
      }
      EList<GlobalVarInitDeclaration> _varsAs = decl.getVarsAs();
      for (final GlobalVarInitDeclaration v_1 : _varsAs) {
        {
          final String type = v_1.getType();
          EList<SymbolicVariable> _vars = v_1.getVarList().getVars();
          for (final SymbolicVariable varName : _vars) {
            {
              final String name = varName.getName();
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("memory.put(\"");
              _builder.append(name);
              _builder.append("\", ");
              String _defaultValue = TypeUtil.defaultValue(type);
              _builder.append(_defaultValue);
              _builder.append(");");
              _builder.newLineIfNotEmpty();
              builder.append(_builder);
              ctx.registerVar(name, type);
              ctx.registerGlobalVar(name);
            }
          }
        }
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
}
