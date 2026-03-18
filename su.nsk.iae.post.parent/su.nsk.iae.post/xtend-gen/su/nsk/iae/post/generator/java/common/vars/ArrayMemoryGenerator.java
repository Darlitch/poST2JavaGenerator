package su.nsk.iae.post.generator.java.common.vars;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.util.CompileTimeEvaluator;
import su.nsk.iae.post.generator.java.common.util.ExpressionGenerator;
import su.nsk.iae.post.generator.java.common.util.TypeUtil;
import su.nsk.iae.post.poST.ArrayInitialization;
import su.nsk.iae.post.poST.ArrayInterval;
import su.nsk.iae.post.poST.ArraySpecificationInit;
import su.nsk.iae.post.poST.Expression;
import su.nsk.iae.post.poST.PrimaryExpression;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.VarInitDeclaration;

@SuppressWarnings("all")
public class ArrayMemoryGenerator {
  private static final int MAX_ARRAY_SIZE = 1_000_000;

  public static String generate(final VarInitDeclaration decl, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      final ArraySpecificationInit arrSpec = decl.getArrSpec();
      ArrayInterval _interval = arrSpec.getInit().getInterval();
      boolean _tripleEquals = (_interval == null);
      if (_tripleEquals) {
        EList<SymbolicVariable> _vars = decl.getVarList().getVars();
        for (final SymbolicVariable v : _vars) {
          {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append(indent);
            _builder.append("memory.put(\"");
            String _name = v.getName();
            _builder.append(_name);
            _builder.append("\", new java.util.ArrayList<String>());");
            _builder.newLineIfNotEmpty();
            builder.append(_builder);
            ctx.registerArrayStart(v.getName(), 0);
          }
        }
        return builder.toString();
      }
      final String type = arrSpec.getInit().getType();
      final ArrayInterval interval = arrSpec.getInit().getInterval();
      final int start = CompileTimeEvaluator.evalInt(interval.getStart(), ctx);
      final int end = CompileTimeEvaluator.evalInt(interval.getEnd(), ctx);
      if ((end < start)) {
        throw new IllegalStateException(
          ((("Array upper bound is less than lower bound: " + Integer.valueOf(start)) + " .. ") + Integer.valueOf(end)));
      }
      final long size = ((((long) end) - ((long) start)) + 1L);
      if ((size <= 0L)) {
        throw new IllegalStateException(
          ("Invalid array size: " + Long.valueOf(size)));
      }
      if ((size > ArrayMemoryGenerator.MAX_ARRAY_SIZE)) {
        throw new IllegalStateException(
          ((("Array too large (" + Long.valueOf(size)) + "). Maximum allowed: ") + Integer.valueOf(ArrayMemoryGenerator.MAX_ARRAY_SIZE)));
      }
      EList<Expression> _xifexpression = null;
      ArrayInitialization _values = arrSpec.getValues();
      boolean _tripleNotEquals = (_values != null);
      if (_tripleNotEquals) {
        _xifexpression = arrSpec.getValues().getElements();
      } else {
        _xifexpression = null;
      }
      final EList<Expression> values = _xifexpression;
      EList<SymbolicVariable> _vars_1 = decl.getVarList().getVars();
      for (final SymbolicVariable v_1 : _vars_1) {
        {
          final String arrName = v_1.getName();
          ctx.registerArrayStart(arrName, start);
          ctx.registerArrayType(arrName, type);
          if ((values != null)) {
            final Function1<Expression, Boolean> _function = (Expression it) -> {
              return Boolean.valueOf(((it instanceof PrimaryExpression) && 
                (((PrimaryExpression) it).getVariable() instanceof SymbolicVariable)));
            };
            final boolean allRefs = IterableExtensions.<Expression>forall(values, _function);
            if (allRefs) {
              final int expectedSize = ((end - start) + 1);
              int _size = values.size();
              boolean _notEquals = (_size != expectedSize);
              if (_notEquals) {
                int _size_1 = values.size();
                String _plus = ((((("Reference array size mismatch for \'" + arrName) + 
                  "\': expected ") + Integer.valueOf(expectedSize)) + 
                  ", got ") + Integer.valueOf(_size_1));
                throw new IllegalStateException(_plus);
              }
              for (final Expression e : values) {
                {
                  final String varName = ((PrimaryExpression) e).getVariable().getName();
                  final String resolved = ctx.resolveAlias(varName);
                  boolean _hasType = ctx.hasType(resolved);
                  boolean _not = (!_hasType);
                  if (_not) {
                    throw new IllegalStateException(
                      ("Unknown variable in reference array init: " + varName));
                  }
                  boolean _hasProcess = ctx.hasProcess(resolved);
                  if (_hasProcess) {
                    throw new IllegalStateException(
                      ("Process cannot be used as array element: " + varName));
                  }
                }
              }
              for (final Expression e_1 : values) {
                {
                  final String elementType = ctx.getArrayElementType(arrName);
                  final String targetType = ctx.resolveVarType(((PrimaryExpression) e_1).getVariable().getName());
                  boolean _notEquals_1 = (!Objects.equals(elementType, targetType));
                  if (_notEquals_1) {
                    throw new IllegalStateException(
                      ((((("Type mismatch in reference array \'" + arrName) + 
                        "\': expected ") + elementType) + 
                        ", got ") + targetType));
                  }
                }
              }
              StringConcatenation _builder = new StringConcatenation();
              _builder.append(indent);
              _builder.append("memory.put(");
              _builder.newLineIfNotEmpty();
              _builder.append(indent);
              _builder.append("    \"");
              _builder.append(arrName);
              _builder.append("\",");
              _builder.newLineIfNotEmpty();
              _builder.append(indent);
              _builder.append("    new java.util.ArrayList<String>(");
              _builder.newLineIfNotEmpty();
              _builder.append(indent);
              _builder.append("        java.util.List.of(");
              _builder.newLineIfNotEmpty();
              {
                boolean _hasElements = false;
                for(final Expression e_2 : values) {
                  if (!_hasElements) {
                    _hasElements = true;
                  } else {
                    _builder.appendImmediate(", ", "");
                  }
                  _builder.append(indent);
                  _builder.append("                \"");
                  String _resolveAlias = ctx.resolveAlias(((PrimaryExpression) e_2).getVariable().getName());
                  _builder.append(_resolveAlias);
                  _builder.append("\"");
                  _builder.newLineIfNotEmpty();
                }
              }
              _builder.append(indent);
              _builder.append("        )");
              _builder.newLineIfNotEmpty();
              _builder.append(indent);
              _builder.append("    )");
              _builder.newLineIfNotEmpty();
              _builder.append(indent);
              _builder.append(");");
              _builder.newLineIfNotEmpty();
              builder.append(_builder);
            } else {
              ArrayMemoryGenerator.generateValueArray(builder, arrName, start, end, type, values, ctx, indent);
            }
          } else {
            ArrayMemoryGenerator.generateDefaultArray(builder, arrName, start, end, type, ctx, indent);
          }
        }
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }

  private static void generateValueArray(final StringBuilder builder, final String arrName, final int start, final int end, final String type, final List<Expression> values, final GenerationContext ctx, final String indent) {
    final ArrayList<String> cellNames = CollectionLiterals.<String>newArrayList();
    int idx = 0;
    IntegerRange _upTo = new IntegerRange(start, end);
    for (final Integer i : _upTo) {
      {
        final String cell = ((arrName + "_") + i);
        cellNames.add(cell);
        String _xifexpression = null;
        int _size = values.size();
        boolean _lessThan = (idx < _size);
        if (_lessThan) {
          _xifexpression = ExpressionGenerator.generate(values.get(idx), ctx);
        } else {
          _xifexpression = TypeUtil.defaultValue(type);
        }
        final String init = _xifexpression;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(indent);
        _builder.append("memory.put(\"");
        _builder.append(cell);
        _builder.append("\", ");
        _builder.append(init);
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        builder.append(_builder);
        ctx.registerVar(cell, type);
        idx++;
      }
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("memory.put(");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("    \"");
    _builder.append(arrName);
    _builder.append("\",");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("    new java.util.ArrayList<String>(");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("        java.util.List.of(");
    _builder.newLineIfNotEmpty();
    {
      boolean _hasElements = false;
      for(final String c : cellNames) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",\n", "");
        }
        _builder.append(indent);
        _builder.append("            \"");
        _builder.append(c);
        _builder.append("\"");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append(indent);
    _builder.append("        )");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("    )");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    builder.append(_builder);
  }

  private static void generateDefaultArray(final StringBuilder builder, final String arrName, final int start, final int end, final String type, final GenerationContext ctx, final String indent) {
    final ArrayList<String> cellNames = CollectionLiterals.<String>newArrayList();
    IntegerRange _upTo = new IntegerRange(start, end);
    for (final Integer i : _upTo) {
      {
        final String cell = ((arrName + "_") + i);
        cellNames.add(cell);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(indent);
        _builder.append("memory.put(\"");
        _builder.append(cell);
        _builder.append("\", ");
        String _defaultValue = TypeUtil.defaultValue(type);
        _builder.append(_defaultValue);
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        builder.append(_builder);
        ctx.registerVar(cell, type);
      }
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("memory.put(");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("    \"");
    _builder.append(arrName);
    _builder.append("\",");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("    new java.util.ArrayList<String>(");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("        java.util.List.of(");
    {
      boolean _hasElements = false;
      for(final String c : cellNames) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(", ", "");
        }
        _builder.append("\"");
        _builder.append(c);
        _builder.append("\"");
      }
    }
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("    )");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    builder.append(_builder);
  }
}
