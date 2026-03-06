package su.nsk.iae.post.generator.java.common.statement;

import java.util.ArrayList;
import java.util.Objects;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.util.ExpressionGenerator;
import su.nsk.iae.post.generator.java.common.util.TypeUtil;
import su.nsk.iae.post.poST.CaseElement;
import su.nsk.iae.post.poST.CaseListElement;
import su.nsk.iae.post.poST.CaseStatement;
import su.nsk.iae.post.poST.SignedInteger;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.StatementList;
import su.nsk.iae.post.poST.SymbolicVariable;

@SuppressWarnings("all")
public class CaseStatementGenerator implements IStatementGenerator {
  private final StatementListGenerator stmtGen;

  public CaseStatementGenerator(final StatementListGenerator stmtGen) {
    this.stmtGen = stmtGen;
  }

  @Override
  public boolean supports(final Statement stmt) {
    return (stmt instanceof CaseStatement);
  }

  @Override
  public String generate(final Statement stmt, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final CaseStatement s = ((CaseStatement) stmt);
      final StringBuilder builder = new StringBuilder();
      final String condExpr = ExpressionGenerator.generate(s.getCond(), ctx);
      final String caseType = ExpressionGenerator.getExprType(s.getCond(), ctx);
      final String nextIndent = (indent + "    ");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(indent);
      _builder.append("final Object __caseVal = ");
      _builder.append(condExpr);
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      builder.append(_builder);
      boolean first = true;
      EList<CaseElement> _caseElements = s.getCaseElements();
      for (final CaseElement el : _caseElements) {
        {
          final String cond = this.generateCaseCondition(el, ctx, caseType);
          if (first) {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append(indent);
            _builder_1.append("if (");
            _builder_1.append(cond);
            _builder_1.append(") {");
            _builder_1.newLineIfNotEmpty();
            String _generate = this.stmtGen.generate(el.getStatement(), ctx, nextIndent);
            _builder_1.append(_generate);
            _builder_1.newLineIfNotEmpty();
            _builder_1.append(indent);
            _builder_1.append("}");
            _builder_1.newLineIfNotEmpty();
            builder.append(_builder_1);
            first = false;
          } else {
            StringConcatenation _builder_2 = new StringConcatenation();
            _builder_2.append(indent);
            _builder_2.append("else if (");
            _builder_2.append(cond);
            _builder_2.append(") {");
            _builder_2.newLineIfNotEmpty();
            String _generate_1 = this.stmtGen.generate(el.getStatement(), ctx, nextIndent);
            _builder_2.append(_generate_1);
            _builder_2.newLineIfNotEmpty();
            _builder_2.append(indent);
            _builder_2.append("}");
            _builder_2.newLineIfNotEmpty();
            builder.append(_builder_2);
          }
        }
      }
      StatementList _elseStatement = s.getElseStatement();
      boolean _tripleNotEquals = (_elseStatement != null);
      if (_tripleNotEquals) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append(indent);
        _builder_1.append("else {");
        _builder_1.newLineIfNotEmpty();
        String _generate = this.stmtGen.generate(s.getElseStatement(), ctx, nextIndent);
        _builder_1.append(_generate);
        _builder_1.newLineIfNotEmpty();
        _builder_1.append(indent);
        _builder_1.append("}");
        _builder_1.newLineIfNotEmpty();
        builder.append(_builder_1);
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }

  private String generateCaseCondition(final CaseElement el, final GenerationContext ctx, final String caseType) {
    String _xblockexpression = null;
    {
      final ArrayList<Object> parts = CollectionLiterals.<Object>newArrayList();
      EList<CaseListElement> _caseListElement = el.getCaseList().getCaseListElement();
      for (final CaseListElement e : _caseListElement) {
        {
          String valueExpr = null;
          SignedInteger _num = e.getNum();
          boolean _tripleNotEquals = (_num != null);
          if (_tripleNotEquals) {
            String _xifexpression = null;
            boolean _isISig = e.getNum().isISig();
            if (_isISig) {
              String _value = e.getNum().getValue();
              _xifexpression = ("-" + _value);
            } else {
              _xifexpression = e.getNum().getValue();
            }
            final String v = _xifexpression;
            valueExpr = v;
          } else {
            SymbolicVariable _variable = e.getVariable();
            boolean _tripleNotEquals_1 = (_variable != null);
            if (_tripleNotEquals_1) {
              final String name = e.getVariable().getName();
              final String resolved = ctx.resolveAlias(name);
              boolean _hasConst = ctx.hasConst(resolved);
              if (_hasConst) {
                valueExpr = ctx.getConst(resolved).toString();
              } else {
                valueExpr = ExpressionGenerator.readVar(name, ctx);
              }
            }
          }
          if ((valueExpr == null)) {
            throw new IllegalStateException(
              ("Unsupported CASE element: " + e));
          }
          if ((Objects.equals(caseType, "REAL") || Objects.equals(caseType, "LREAL"))) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("((Number)(__caseVal)).doubleValue() == ((Number)(");
            _builder.append(valueExpr);
            _builder.append(")).doubleValue()");
            parts.add(_builder.toString());
          } else {
            boolean _isNumeric = TypeUtil.isNumeric(caseType);
            if (_isNumeric) {
              StringConcatenation _builder_1 = new StringConcatenation();
              _builder_1.append("((Number)(__caseVal)).longValue() == ((Number)(");
              _builder_1.append(valueExpr);
              _builder_1.append(")).longValue()");
              parts.add(_builder_1.toString());
            } else {
              boolean _equals = Objects.equals(caseType, "BOOL");
              if (_equals) {
                StringConcatenation _builder_2 = new StringConcatenation();
                _builder_2.append("((Boolean)__caseVal) == ");
                _builder_2.append(valueExpr);
                parts.add(_builder_2.toString());
              } else {
                if ((Objects.equals(caseType, "STRING") || Objects.equals(caseType, "WSTRING"))) {
                  StringConcatenation _builder_3 = new StringConcatenation();
                  _builder_3.append("java.util.Objects.equals(__caseVal, ");
                  _builder_3.append(valueExpr);
                  _builder_3.append(")");
                  parts.add(_builder_3.toString());
                } else {
                  throw new IllegalStateException(
                    ("Unsupported CASE type: " + caseType));
                }
              }
            }
          }
        }
      }
      String _xifexpression = null;
      boolean _isEmpty = parts.isEmpty();
      if (_isEmpty) {
        _xifexpression = "false";
      } else {
        _xifexpression = IterableExtensions.join(parts, " || ");
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
}
