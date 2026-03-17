package su.nsk.iae.post.generator.java.common.statement;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.StatementList;

@SuppressWarnings("all")
public class StatementListGenerator {
  private final List<IStatementGenerator> generators = new ArrayList<IStatementGenerator>();

  public StatementListGenerator() {
    AssignmentStatementGenerator _assignmentStatementGenerator = new AssignmentStatementGenerator();
    this.generators.add(_assignmentStatementGenerator);
    IfStatementGenerator _ifStatementGenerator = new IfStatementGenerator(this);
    this.generators.add(_ifStatementGenerator);
    CaseStatementGenerator _caseStatementGenerator = new CaseStatementGenerator(this);
    this.generators.add(_caseStatementGenerator);
    ForStatementGenerator _forStatementGenerator = new ForStatementGenerator(this);
    this.generators.add(_forStatementGenerator);
    WhileStatementGenerator _whileStatementGenerator = new WhileStatementGenerator(this);
    this.generators.add(_whileStatementGenerator);
    RepeatStatementGenerator _repeatStatementGenerator = new RepeatStatementGenerator(this);
    this.generators.add(_repeatStatementGenerator);
    RestartProcessStatementGenerator _restartProcessStatementGenerator = new RestartProcessStatementGenerator();
    this.generators.add(_restartProcessStatementGenerator);
    StartProcessStatementGenerator _startProcessStatementGenerator = new StartProcessStatementGenerator();
    this.generators.add(_startProcessStatementGenerator);
    StopProcessStatementGenerator _stopProcessStatementGenerator = new StopProcessStatementGenerator();
    this.generators.add(_stopProcessStatementGenerator);
    ErrorProcessStatementGenerator _errorProcessStatementGenerator = new ErrorProcessStatementGenerator();
    this.generators.add(_errorProcessStatementGenerator);
    SetStateStatementGenerator _setStateStatementGenerator = new SetStateStatementGenerator();
    this.generators.add(_setStateStatementGenerator);
    ResetTimerStatementGenerator _resetTimerStatementGenerator = new ResetTimerStatementGenerator();
    this.generators.add(_resetTimerStatementGenerator);
    TimeoutStatementGenerator _timeoutStatementGenerator = new TimeoutStatementGenerator(this);
    this.generators.add(_timeoutStatementGenerator);
    ExitStatementGenerator _exitStatementGenerator = new ExitStatementGenerator();
    this.generators.add(_exitStatementGenerator);
    ReturnStatementGenerator _returnStatementGenerator = new ReturnStatementGenerator();
    this.generators.add(_returnStatementGenerator);
  }

  public String generate(final StatementList list, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      if ((list == null)) {
        return "";
      }
      final StringBuilder builder = new StringBuilder();
      EList<Statement> _statements = list.getStatements();
      for (final Statement stmt : _statements) {
        {
          final String code = this.generateStatement(stmt, ctx, indent);
          if (((code != null) && (!code.trim().isEmpty()))) {
            builder.append(code);
            boolean _endsWith = code.endsWith("\n");
            boolean _not = (!_endsWith);
            if (_not) {
              builder.append("\n");
            }
          }
        }
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }

  private String generateStatement(final Statement stmt, final GenerationContext ctx, final String indent) {
    for (final IStatementGenerator g : this.generators) {
      boolean _supports = g.supports(stmt);
      if (_supports) {
        return g.generate(stmt, ctx, indent);
      }
    }
    String _name = stmt.eClass().getName();
    String _plus = ("Unsupported statement: " + _name);
    throw new IllegalStateException(_plus);
  }

  public static String indent(final String indent) {
    return (indent + "    ");
  }
}
