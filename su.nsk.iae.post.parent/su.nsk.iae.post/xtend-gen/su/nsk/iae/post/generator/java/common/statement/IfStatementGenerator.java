package su.nsk.iae.post.generator.java.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.util.ExpressionGenerator;
import su.nsk.iae.post.poST.IfStatement;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.StatementList;

@SuppressWarnings("all")
public class IfStatementGenerator implements IStatementGenerator {
  private final StatementListGenerator stmtGen;

  public IfStatementGenerator(final StatementListGenerator stmtGen) {
    this.stmtGen = stmtGen;
  }

  @Override
  public boolean supports(final Statement stmt) {
    return (stmt instanceof IfStatement);
  }

  @Override
  public String generate(final Statement stmt, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final IfStatement s = ((IfStatement) stmt);
      final StringBuilder builder = new StringBuilder();
      final String nextIndent = (indent + "    ");
      final String cond = ExpressionGenerator.generate(s.getMainCond(), ctx);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(indent);
      _builder.append("if (");
      _builder.append(cond);
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      String _generate = this.stmtGen.generate(s.getMainStatement(), ctx, nextIndent);
      _builder.append(_generate);
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      builder.append(_builder);
      int _size = s.getElseIfCond().size();
      ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
      for (final Integer i : _doubleDotLessThan) {
        {
          final String elsifCond = ExpressionGenerator.generate(s.getElseIfCond().get((i).intValue()), ctx);
          final StatementList elsifStmt = s.getElseIfStatements().get((i).intValue());
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append(indent);
          _builder_1.append("else if (");
          _builder_1.append(elsifCond);
          _builder_1.append(") {");
          _builder_1.newLineIfNotEmpty();
          String _generate_1 = this.stmtGen.generate(elsifStmt, ctx, nextIndent);
          _builder_1.append(_generate_1);
          _builder_1.newLineIfNotEmpty();
          _builder_1.append(indent);
          _builder_1.append("}");
          _builder_1.newLineIfNotEmpty();
          builder.append(_builder_1);
        }
      }
      StatementList _elseStatement = s.getElseStatement();
      boolean _tripleNotEquals = (_elseStatement != null);
      if (_tripleNotEquals) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append(indent);
        _builder_1.append("else {");
        _builder_1.newLineIfNotEmpty();
        String _generate_1 = this.stmtGen.generate(s.getElseStatement(), ctx, nextIndent);
        _builder_1.append(_generate_1);
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
}
