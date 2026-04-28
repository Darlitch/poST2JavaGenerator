package su.nsk.iae.post.generator.java.common;

import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.statement.StatementListGenerator;
import su.nsk.iae.post.generator.java.common.util.MemoryUtil;
import su.nsk.iae.post.generator.java.common.vars.VarMemoryGenerator;
import su.nsk.iae.post.poST.State;
import su.nsk.iae.post.poST.VarDeclaration;
import su.nsk.iae.post.poST.VarInitDeclaration;

@SuppressWarnings("all")
public class ProcessGenerator {
  private final StatementListGenerator stmtGen;

  private final StateGenerator stateGen;

  public ProcessGenerator() {
    StatementListGenerator _statementListGenerator = new StatementListGenerator();
    this.stmtGen = _statementListGenerator;
    StateGenerator _stateGenerator = new StateGenerator(this.stmtGen);
    this.stateGen = _stateGenerator;
  }

  public String generate(final su.nsk.iae.post.poST.Process p, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      boolean _isEmpty = p.getStates().isEmpty();
      if (_isEmpty) {
        String _name = p.getName();
        String _plus = ("Process must contain at least one STATE: " + _name);
        throw new IllegalStateException(_plus);
      }
      final StringBuilder builder = new StringBuilder();
      String _name_1 = p.getName();
      final String name = (_name_1 + "Process");
      final String nextIndent = (indent + "    ");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(indent);
      _builder.append("static class ");
      _builder.append(name);
      _builder.append(" extends BaseProcess {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.newLine();
      String _generateStateEnum = this.generateStateEnum(p, nextIndent);
      _builder.append(_generateStateEnum);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateConstructor = this.generateConstructor(name, nextIndent, p, ctx);
      _builder.append(_generateConstructor);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(nextIndent);
      _builder.append("private State state = State.Stop;");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(nextIndent);
      _builder.append("private long timerBaseTime;");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateControlMethods = this.generateControlMethods(p, nextIndent);
      _builder.append(_generateControlMethods);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateRunMethod = this.generateRunMethod(p, ctx, nextIndent);
      _builder.append(_generateRunMethod);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateDumpStates = this.generateDumpStates(name, nextIndent);
      _builder.append(_generateDumpStates);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateDumpTimers = this.generateDumpTimers(name, nextIndent);
      _builder.append(_generateDumpTimers);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateGetStateName = this.generateGetStateName(nextIndent);
      _builder.append(_generateGetStateName);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(indent);
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      builder.append(_builder);
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }

  private String generateConstructor(final String name, final String indent, final su.nsk.iae.post.poST.Process p, final GenerationContext ctx) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("public ");
    _builder.append(name);
    _builder.append("(String instanceName, Map<String,Object> memory, Map<String,String> aliases, Map<String, IProcess> globalProcesses) {");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("    super(instanceName, memory, aliases, globalProcesses);");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    String _generateLocalVarInit = this.generateLocalVarInit(p, (indent + "    "), ctx);
    _builder.append(_generateLocalVarInit);
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateLocalVarInit(final su.nsk.iae.post.poST.Process p, final String indent, final GenerationContext ctx) {
    final StringBuilder builder = new StringBuilder();
    EList<VarDeclaration> _procVars = p.getProcVars();
    for (final VarDeclaration v : _procVars) {
      EList<VarInitDeclaration> _vars = v.getVars();
      for (final VarInitDeclaration decl : _vars) {
        builder.append(
          VarMemoryGenerator.generateLocal(decl, ctx, indent));
      }
    }
    return builder.toString();
  }

  private String generateStateEnum(final su.nsk.iae.post.poST.Process p, final String indent) {
    final StringBuilder builder = new StringBuilder();
    final Function1<State, String> _function = (State it) -> {
      return it.getName();
    };
    final List<String> states = ListExtensions.<State, String>map(p.getStates(), _function);
    builder.append((indent + "enum State {\n"));
    int _size = states.size();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final String s = states.get((i).intValue());
        builder.append((((indent + "    ") + s) + ",\n"));
      }
    }
    builder.append((indent + "    Stop,\n"));
    builder.append((indent + "    Error\n"));
    builder.append((indent + "}\n"));
    return builder.toString();
  }

  private String generateControlMethods(final su.nsk.iae.post.poST.Process p, final String indent) {
    String _xblockexpression = null;
    {
      final String firstState = IterableExtensions.<State>head(p.getStates()).getName();
      final String globalTime = MemoryUtil.globalTime();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(indent);
      _builder.append("public void start() {");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("    state = State.");
      _builder.append(firstState);
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("    timerBaseTime = ((Long)memory.get(\"");
      _builder.append(globalTime);
      _builder.append("\"));");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(indent);
      _builder.append("public void stop() {");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("    state = State.Stop;");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("    timerBaseTime = ((Long)memory.get(\"");
      _builder.append(globalTime);
      _builder.append("\"));");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(indent);
      _builder.append("public void error() {");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("    state = State.Error;");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("    timerBaseTime = ((Long)memory.get(\"");
      _builder.append(globalTime);
      _builder.append("\"));");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(indent);
      _builder.append("public void setState(State s) {");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("    state = s;");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("    timerBaseTime = ((Long)memory.get(\"");
      _builder.append(globalTime);
      _builder.append("\"));");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateSetNext = this.generateSetNext(p, indent);
      _builder.append(_generateSetNext);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(indent);
      _builder.append("public State getState() {");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("    return state;");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }

  private String generateSetNext(final su.nsk.iae.post.poST.Process p, final String indent) {
    String _xblockexpression = null;
    {
      final Function1<State, String> _function = (State it) -> {
        return it.getName();
      };
      final List<String> states = ListExtensions.<State, String>map(p.getStates(), _function);
      final StringBuilder builder = new StringBuilder();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(indent);
      _builder.append("public void setNext() {");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(indent);
      _builder.append("    switch(state) {");
      _builder.newLineIfNotEmpty();
      builder.append(_builder);
      int _size = states.size();
      ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
      for (final Integer i : _doubleDotLessThan) {
        {
          final String current = states.get((i).intValue());
          String _xifexpression = null;
          int _size_1 = states.size();
          int _minus = (_size_1 - 1);
          boolean _lessThan = ((i).intValue() < _minus);
          if (_lessThan) {
            _xifexpression = states.get(((i).intValue() + 1));
          } else {
            _xifexpression = states.get(0);
          }
          final String next = _xifexpression;
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append(indent);
          _builder_1.append("        case ");
          _builder_1.append(current);
          _builder_1.append(" -> state = State.");
          _builder_1.append(next);
          _builder_1.append(";");
          _builder_1.newLineIfNotEmpty();
          builder.append(_builder_1);
        }
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(indent);
      _builder_1.append("        default -> { }");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append(indent);
      _builder_1.append("    }");
      _builder_1.newLineIfNotEmpty();
      _builder_1.newLine();
      _builder_1.append(indent);
      _builder_1.append("    timerBaseTime = ((Long)memory.get(\"");
      String _globalTime = MemoryUtil.globalTime();
      _builder_1.append(_globalTime);
      _builder_1.append("\"));");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append(indent);
      _builder_1.append("}");
      _builder_1.newLineIfNotEmpty();
      builder.append(_builder_1);
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }

  private String generateRunMethod(final su.nsk.iae.post.poST.Process p, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(indent);
      _builder.append("@Override");
      _builder.newLineIfNotEmpty();
      _builder.append(indent);
      _builder.append("public void run() {");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(indent);
      _builder.append("    switch(state) {");
      _builder.newLineIfNotEmpty();
      builder.append(_builder);
      EList<State> _states = p.getStates();
      for (final State s : _states) {
        builder.append(
          this.stateGen.generate(s, ctx, (indent + "        ")));
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(indent);
      _builder_1.append("        case Stop, Error -> { }");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append(indent);
      _builder_1.append("    }");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append(indent);
      _builder_1.append("}");
      _builder_1.newLineIfNotEmpty();
      builder.append(_builder_1);
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }

  private String generateDumpStates(final String name, final String indent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("@Override");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("public void dumpStates(Map<String,String> out) {");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("    out.put(instanceName + \"_state\", state.name());");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateDumpTimers(final String name, final String indent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("@Override");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("public void dumpTimers(Map<String,Long> out) {");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("    out.put(instanceName + \"_time\", timerBaseTime);");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateGetStateName(final String indent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("@Override");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("public String getStateName() {");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("    return state.name();");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
}
