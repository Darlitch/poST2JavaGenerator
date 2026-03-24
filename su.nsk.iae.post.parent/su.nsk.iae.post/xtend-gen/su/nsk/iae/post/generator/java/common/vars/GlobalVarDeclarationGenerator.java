package su.nsk.iae.post.generator.java.common.vars;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.util.CompileTimeEvaluator;
import su.nsk.iae.post.generator.java.common.util.TypeUtil;
import su.nsk.iae.post.poST.ArraySpecificationInit;
import su.nsk.iae.post.poST.GlobalVarDeclaration;
import su.nsk.iae.post.poST.GlobalVarInitDeclaration;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.VarInitDeclaration;

@SuppressWarnings("all")
public class GlobalVarDeclarationGenerator {
  public static String generate(final GlobalVarDeclaration decl, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      boolean _isConst = decl.isConst();
      if (_isConst) {
        EList<VarInitDeclaration> _varsSimple = decl.getVarsSimple();
        for (final VarInitDeclaration v : _varsSimple) {
          EList<SymbolicVariable> _vars = v.getVarList().getVars();
          for (final SymbolicVariable vname : _vars) {
            {
              final Object value = CompileTimeEvaluator.evalExpression(v.getSpec().getValue(), ctx);
              StringConcatenation _builder = new StringConcatenation();
              _builder.append(indent);
              _builder.append("memory.put(\"");
              String _name = vname.getName();
              _builder.append(_name);
              _builder.append("\", ");
              _builder.append(value);
              _builder.append(");");
              _builder.newLineIfNotEmpty();
              builder.append(_builder);
              ctx.registerConst(vname.getName(), value);
              ctx.registerVar(vname.getName(), v.getSpec().getType());
              ctx.registerGlobalVar(vname.getName());
            }
          }
        }
      } else {
        EList<VarInitDeclaration> _varsSimple_1 = decl.getVarsSimple();
        for (final VarInitDeclaration v_1 : _varsSimple_1) {
          {
            EList<SymbolicVariable> _vars_1 = v_1.getVarList().getVars();
            for (final SymbolicVariable vname_1 : _vars_1) {
              ctx.registerGlobalVar(vname_1.getName());
            }
            ArraySpecificationInit _arrSpec = v_1.getArrSpec();
            boolean _tripleNotEquals = (_arrSpec != null);
            if (_tripleNotEquals) {
              EList<SymbolicVariable> _vars_2 = v_1.getVarList().getVars();
              for (final SymbolicVariable vname_2 : _vars_2) {
                {
                  ctx.registerArrayType(vname_2.getName(), v_1.getArrSpec().getInit().getType());
                  ctx.registerArrayStart(vname_2.getName(), 0);
                }
              }
              builder.append(ArrayMemoryGenerator.generate(v_1, ctx, indent));
            } else {
              builder.append(VarMemoryGenerator.generate(v_1, ctx, indent));
            }
          }
        }
      }
      EList<GlobalVarInitDeclaration> _varsAs = decl.getVarsAs();
      for (final GlobalVarInitDeclaration v_2 : _varsAs) {
        {
          final String type = v_2.getType();
          EList<SymbolicVariable> _vars_1 = v_2.getVarList().getVars();
          for (final SymbolicVariable varName : _vars_1) {
            {
              final String name = varName.getName();
              StringConcatenation _builder = new StringConcatenation();
              _builder.append(indent);
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
