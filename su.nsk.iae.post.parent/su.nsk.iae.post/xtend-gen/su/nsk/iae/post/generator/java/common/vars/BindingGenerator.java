package su.nsk.iae.post.generator.java.common.vars;

import java.util.Objects;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.util.CompileTimeEvaluator;
import su.nsk.iae.post.poST.AttachVariableConfElement;
import su.nsk.iae.post.poST.Constant;
import su.nsk.iae.post.poST.IntegerLiteral;
import su.nsk.iae.post.poST.NumericLiteral;
import su.nsk.iae.post.poST.RealLiteral;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.TemplateProcessAttachVariableConfElement;
import su.nsk.iae.post.poST.TimeLiteral;
import su.nsk.iae.post.poST.Variable;

@SuppressWarnings("all")
public class BindingGenerator {
  public static void generate(final AttachVariableConfElement bind, final GenerationContext ctx) {
    final String left = bind.getProgramVar().getName();
    SymbolicVariable _attVar = bind.getAttVar();
    boolean _tripleNotEquals = (_attVar != null);
    if (_tripleNotEquals) {
      final String right = bind.getAttVar().getName();
      boolean _hasProcess = ctx.hasProcess(right);
      if (_hasProcess) {
        boolean _hasProcess_1 = ctx.hasProcess(left);
        if (_hasProcess_1) {
          throw new IllegalStateException(
            ("Cannot bind to process name: " + left));
        }
        final String fieldName = ctx.resolveProcess(right);
        final String type = ctx.getProcessTypeByFieldName(fieldName);
        ctx.registerProcess(left, fieldName, type);
        return;
      }
      final String target = ctx.resolveAlias(right);
      ctx.registerAlias(left, target);
      boolean _hasArrayStart = ctx.hasArrayStart(target);
      if (_hasArrayStart) {
        ctx.registerArrayStart(left, ctx.getArrayStart(target));
        ctx.registerArrayType(left, ctx.getArrayElementType(target));
      }
      return;
    }
    Constant _const = bind.getConst();
    boolean _tripleNotEquals_1 = (_const != null);
    if (_tripleNotEquals_1) {
      final Object value = CompileTimeEvaluator.eval(bind.getConst());
      ctx.registerConst(left, value);
      ctx.registerVar(left, BindingGenerator.inferConstType(bind.getConst()));
      return;
    }
  }

  public static void generate(final TemplateProcessAttachVariableConfElement bind, final GenerationContext ctx) {
    final String left = bind.getProgramVar().getName();
    Variable _attVar = bind.getAttVar();
    boolean _tripleNotEquals = (_attVar != null);
    if (_tripleNotEquals) {
      final String right = bind.getAttVar().getName();
      final String target = ctx.resolveAlias(right);
      ctx.registerAlias(left, target);
      boolean _hasArrayStart = ctx.hasArrayStart(target);
      if (_hasArrayStart) {
        ctx.registerArrayStart(left, ctx.getArrayStart(target));
        ctx.registerArrayType(left, ctx.getArrayElementType(target));
      }
      return;
    }
    Constant _const = bind.getConst();
    boolean _tripleNotEquals_1 = (_const != null);
    if (_tripleNotEquals_1) {
      final Object value = CompileTimeEvaluator.eval(bind.getConst());
      ctx.registerConst(left, value);
      ctx.registerVar(left, BindingGenerator.inferConstType(bind.getConst()));
      return;
    }
  }

  private static String inferConstType(final Constant c) {
    String _xblockexpression = null;
    {
      NumericLiteral _num = c.getNum();
      boolean _tripleNotEquals = (_num != null);
      if (_tripleNotEquals) {
        NumericLiteral _num_1 = c.getNum();
        if ((_num_1 instanceof IntegerLiteral)) {
          NumericLiteral _num_2 = c.getNum();
          final IntegerLiteral lit = ((IntegerLiteral) _num_2);
          String _xifexpression = null;
          String _type = lit.getType();
          boolean _tripleNotEquals_1 = (_type != null);
          if (_tripleNotEquals_1) {
            _xifexpression = lit.getType();
          } else {
            _xifexpression = "INT";
          }
          return _xifexpression;
        }
        NumericLiteral _num_3 = c.getNum();
        if ((_num_3 instanceof RealLiteral)) {
          NumericLiteral _num_4 = c.getNum();
          final RealLiteral lit_1 = ((RealLiteral) _num_4);
          String _xifexpression_1 = null;
          String _type_1 = lit_1.getType();
          boolean _tripleNotEquals_2 = (_type_1 != null);
          if (_tripleNotEquals_2) {
            _xifexpression_1 = lit_1.getType();
          } else {
            _xifexpression_1 = "LREAL";
          }
          return _xifexpression_1;
        }
      }
      TimeLiteral _time = c.getTime();
      boolean _tripleNotEquals_3 = (_time != null);
      if (_tripleNotEquals_3) {
        return "TIME";
      }
      if ((Objects.equals(c.getOth(), "TRUE") || Objects.equals(c.getOth(), "FALSE"))) {
        return "BOOL";
      }
      _xblockexpression = "INT";
    }
    return _xblockexpression;
  }

  public static String generateAlias(final AttachVariableConfElement bind, final GenerationContext ctx, final String procName, final String indent) {
    final String left = bind.getProgramVar().getName();
    SymbolicVariable _attVar = bind.getAttVar();
    boolean _tripleNotEquals = (_attVar != null);
    if (_tripleNotEquals) {
      final String right = bind.getAttVar().getName();
      boolean _hasProcess = ctx.hasProcess(right);
      if (_hasProcess) {
        final String field = ctx.resolveProcess(right);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(indent);
        _builder.append(procName);
        _builder.append(".setProcess(\"");
        _builder.append(left);
        _builder.append("\", ");
        _builder.append(field);
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        return _builder.toString();
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(indent);
      _builder_1.append(procName);
      _builder_1.append("_aliases.put(\"");
      _builder_1.append(left);
      _builder_1.append("\", \"");
      _builder_1.append(right);
      _builder_1.append("\");");
      _builder_1.newLineIfNotEmpty();
      return _builder_1.toString();
    }
    return "";
  }

  public static String generateAlias(final TemplateProcessAttachVariableConfElement bind, final GenerationContext ctx, final String procName, final String indent) {
    final String left = bind.getProgramVar().getName();
    Variable _attVar = bind.getAttVar();
    boolean _tripleNotEquals = (_attVar != null);
    if (_tripleNotEquals) {
      final String right = bind.getAttVar().getName();
      boolean _hasProcess = ctx.hasProcess(right);
      if (_hasProcess) {
        final String field = ctx.resolveProcess(right);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(indent);
        _builder.append(procName);
        _builder.append(".setProcess(\"");
        _builder.append(left);
        _builder.append("\", ");
        _builder.append(field);
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        return _builder.toString();
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(indent);
      _builder_1.append(procName);
      _builder_1.append("_aliases.put(\"");
      _builder_1.append(left);
      _builder_1.append("\", \"");
      _builder_1.append(right);
      _builder_1.append("\");");
      _builder_1.newLineIfNotEmpty();
      return _builder_1.toString();
    }
    return "";
  }

  public static String generateAliasTemplate(final TemplateProcessAttachVariableConfElement bind, final String indent, final String aliasMapName) {
    final String left = bind.getProgramVar().getName();
    Variable _attVar = bind.getAttVar();
    boolean _tripleNotEquals = (_attVar != null);
    if (_tripleNotEquals) {
      final String right = bind.getAttVar().getName();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(indent);
      _builder.append(aliasMapName);
      _builder.append(".put(\"");
      _builder.append(left);
      _builder.append("\", \"");
      _builder.append(right);
      _builder.append("\");");
      _builder.newLineIfNotEmpty();
      return _builder.toString();
    }
    return "";
  }
}
