package su.nsk.iae.post.generator.java.common;

import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.vars.VarMemoryGenerator;
import su.nsk.iae.post.poST.ArraySpecificationInit;
import su.nsk.iae.post.poST.InputOutputVarDeclaration;
import su.nsk.iae.post.poST.InputVarDeclaration;
import su.nsk.iae.post.poST.OutputVarDeclaration;
import su.nsk.iae.post.poST.ProcessVarDeclaration;
import su.nsk.iae.post.poST.ProcessVarInitDeclaration;
import su.nsk.iae.post.poST.ProcessVariable;
import su.nsk.iae.post.poST.Program;
import su.nsk.iae.post.poST.SimpleSpecificationInit;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.TempVarDeclaration;
import su.nsk.iae.post.poST.VarDeclaration;
import su.nsk.iae.post.poST.VarInitDeclaration;

@SuppressWarnings("all")
public class ProgramGenerator {
  private static final String INDENT = "    ";

  private final ProcessGenerator processGen = new ProcessGenerator();

  public String generate(final Program program, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      this.registerAll(program, ctx);
      final StringBuilder builder = new StringBuilder();
      final String name = program.getName();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("import java.util.Map;");
      _builder.newLine();
      _builder.append("import java.util.List;");
      _builder.newLine();
      _builder.append("import java.util.Set;");
      _builder.newLine();
      _builder.append("import java.util.HashMap;");
      _builder.newLine();
      _builder.append("import java.util.ArrayList;");
      _builder.newLine();
      _builder.append("import java.util.HashSet;");
      _builder.newLine();
      _builder.append("import java.util.Objects;");
      _builder.newLine();
      _builder.newLine();
      _builder.append("public class ");
      _builder.append(name);
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(ProgramGenerator.INDENT);
      _builder.append("private final Map<String,Object> memory;");
      _builder.newLineIfNotEmpty();
      _builder.append(ProgramGenerator.INDENT);
      _builder.append("private final List<IProcess> processes = new ArrayList<>();");
      _builder.newLineIfNotEmpty();
      _builder.append(ProgramGenerator.INDENT);
      _builder.append("private final Set<String> inputNames = new HashSet<>();");
      _builder.newLineIfNotEmpty();
      _builder.append(ProgramGenerator.INDENT);
      _builder.append("private final Set<String> outputNames = new HashSet<>();");
      _builder.newLineIfNotEmpty();
      _builder.append(ProgramGenerator.INDENT);
      _builder.append("private final Set<String> globalNames = new HashSet<>();");
      _builder.newLineIfNotEmpty();
      _builder.append(ProgramGenerator.INDENT);
      _builder.append("private final Set<String> varNames = new HashSet<>();");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      builder.append(_builder);
      builder.append(this.generateConstructor(program, ctx));
      builder.append(this.generateRunIter());
      builder.append("\n");
      builder.append(this.generateRegisterProcess());
      builder.append("\n");
      builder.append(this.generateDumpStates());
      builder.append("\n");
      builder.append(this.generateDumpTimers());
      builder.append("\n");
      builder.append(this.generateDumpInputs());
      builder.append("\n");
      builder.append(this.generateDumpOutputs());
      builder.append("\n");
      builder.append(this.generateDumpGlobals());
      builder.append("\n");
      builder.append(this.generateDumpVars());
      builder.append("\n");
      EList<su.nsk.iae.post.poST.Process> _processes = program.getProcesses();
      for (final su.nsk.iae.post.poST.Process p : _processes) {
        builder.append(this.processGen.generate(p, ctx, ProgramGenerator.INDENT));
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.newLine();
      _builder_1.append(ProgramGenerator.INDENT);
      _builder_1.append("private boolean isActive(IProcess p) {");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append(ProgramGenerator.INDENT);
      _builder_1.append("    String s = p.getStateName();");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append(ProgramGenerator.INDENT);
      _builder_1.append("    return !s.equals(\"Stop\") && !s.equals(\"Error\");");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append(ProgramGenerator.INDENT);
      _builder_1.append("}");
      _builder_1.newLineIfNotEmpty();
      _builder_1.newLine();
      _builder_1.append(ProgramGenerator.INDENT);
      _builder_1.append("private boolean isInactive(IProcess p) {");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append(ProgramGenerator.INDENT);
      _builder_1.append("    String s = p.getStateName();");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append(ProgramGenerator.INDENT);
      _builder_1.append("    return s.equals(\"Stop\") || s.equals(\"Error\");");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append(ProgramGenerator.INDENT);
      _builder_1.append("}");
      _builder_1.newLineIfNotEmpty();
      _builder_1.newLine();
      _builder_1.append(ProgramGenerator.INDENT);
      _builder_1.append("private boolean isStop(IProcess p) {");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append(ProgramGenerator.INDENT);
      _builder_1.append("    return p.getStateName().equals(\"Stop\");");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append(ProgramGenerator.INDENT);
      _builder_1.append("}");
      _builder_1.newLineIfNotEmpty();
      _builder_1.newLine();
      _builder_1.append(ProgramGenerator.INDENT);
      _builder_1.append("private boolean isError(IProcess p) {");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append(ProgramGenerator.INDENT);
      _builder_1.append("    return p.getStateName().equals(\"Error\");");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append(ProgramGenerator.INDENT);
      _builder_1.append("}");
      _builder_1.newLineIfNotEmpty();
      builder.append(_builder_1);
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.newLine();
      _builder_2.append(ProgramGenerator.INDENT);
      _builder_2.append("private boolean loopCond(String var, int end, int step) {");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append(ProgramGenerator.INDENT);
      _builder_2.append("    int value = ((Number)memory.get(var)).intValue();");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append(ProgramGenerator.INDENT);
      _builder_2.append("    return (step >= 0 && value <= end)");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append(ProgramGenerator.INDENT);
      _builder_2.append("        || (step < 0 && value >= end);");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append(ProgramGenerator.INDENT);
      _builder_2.append("}");
      _builder_2.newLineIfNotEmpty();
      builder.append(_builder_2);
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.newLine();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("private Object getArrayValue(String name, int index, int start) {");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("    List<String> list = (List<String>) memory.get(resolve(name));");
      _builder_3.newLineIfNotEmpty();
      _builder_3.newLine();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("    int offset = index - start;");
      _builder_3.newLineIfNotEmpty();
      _builder_3.newLine();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("    if (offset < 0 || offset >= list.size()) {");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("        throw new RuntimeException(");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("            \"Array index out of bounds: \" + name + \"[\" + index + \"]\"");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("        );");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("    }");
      _builder_3.newLineIfNotEmpty();
      _builder_3.newLine();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("    String cell = list.get(offset);");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("    return memory.get(cell);");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("}");
      _builder_3.newLineIfNotEmpty();
      _builder_3.newLine();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("private void setArrayValue(String name, int index, int start, Object value) {");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("    List<String> list = (List<String>) memory.get(resolve(name));");
      _builder_3.newLineIfNotEmpty();
      _builder_3.newLine();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("    int offset = index - start;");
      _builder_3.newLineIfNotEmpty();
      _builder_3.newLine();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("    if (offset < 0 || offset >= list.size()) {");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("        throw new RuntimeException(");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("            \"Array index out of bounds: \" + name + \"[\" + index + \"]\"");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("        );");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("    }");
      _builder_3.newLineIfNotEmpty();
      _builder_3.newLine();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("    String cell = list.get(offset);");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("    memory.put(cell, value);");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append(ProgramGenerator.INDENT);
      _builder_3.append("}");
      _builder_3.newLineIfNotEmpty();
      _builder_3.newLine();
      _builder_3.append("}");
      _builder_3.newLine();
      builder.append(_builder_3);
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }

  private String generateConstructor(final Program program, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append(ProgramGenerator.INDENT);
      _builder.append("public ");
      String _name = program.getName();
      _builder.append(_name);
      _builder.append("(Map<String,Object> memory) {");
      _builder.newLineIfNotEmpty();
      _builder.append(ProgramGenerator.INDENT);
      _builder.append("    this.memory = memory;");
      _builder.newLineIfNotEmpty();
      builder.append(_builder);
      EList<su.nsk.iae.post.poST.Process> _processes = program.getProcesses();
      for (final su.nsk.iae.post.poST.Process p : _processes) {
        {
          EList<VarDeclaration> _procVars = p.getProcVars();
          for (final VarDeclaration v : _procVars) {
            EList<VarInitDeclaration> _vars = v.getVars();
            for (final VarInitDeclaration decl : _vars) {
              builder.append(VarMemoryGenerator.generate(decl, ctx, (ProgramGenerator.INDENT + "    ")));
            }
          }
          EList<InputVarDeclaration> _procInVars = p.getProcInVars();
          for (final InputVarDeclaration v_1 : _procInVars) {
            EList<VarInitDeclaration> _vars_1 = v_1.getVars();
            for (final VarInitDeclaration decl_1 : _vars_1) {
              builder.append(VarMemoryGenerator.generate(decl_1, ctx, (ProgramGenerator.INDENT + "    ")));
            }
          }
          EList<OutputVarDeclaration> _procOutVars = p.getProcOutVars();
          for (final OutputVarDeclaration v_2 : _procOutVars) {
            EList<VarInitDeclaration> _vars_2 = v_2.getVars();
            for (final VarInitDeclaration decl_2 : _vars_2) {
              builder.append(VarMemoryGenerator.generate(decl_2, ctx, (ProgramGenerator.INDENT + "    ")));
            }
          }
        }
      }
      Set<String> _inputVars = ctx.getInputVars();
      for (final String n : _inputVars) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append(ProgramGenerator.INDENT);
        _builder_1.append("    inputNames.add(\"");
        _builder_1.append(n);
        _builder_1.append("\");");
        _builder_1.newLineIfNotEmpty();
        builder.append(_builder_1);
      }
      Set<String> _outputVars = ctx.getOutputVars();
      for (final String n_1 : _outputVars) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append(ProgramGenerator.INDENT);
        _builder_2.append("    outputNames.add(\"");
        _builder_2.append(n_1);
        _builder_2.append("\");");
        _builder_2.newLineIfNotEmpty();
        builder.append(_builder_2);
      }
      Set<String> _globalVars = ctx.getGlobalVars();
      for (final String n_2 : _globalVars) {
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append(ProgramGenerator.INDENT);
        _builder_3.append("    globalNames.add(\"");
        _builder_3.append(n_2);
        _builder_3.append("\");");
        _builder_3.newLineIfNotEmpty();
        builder.append(_builder_3);
      }
      Set<String> _localVars = ctx.getLocalVars();
      for (final String n_3 : _localVars) {
        StringConcatenation _builder_4 = new StringConcatenation();
        _builder_4.append(ProgramGenerator.INDENT);
        _builder_4.append("    varNames.add(\"");
        _builder_4.append(n_3);
        _builder_4.append("\");");
        _builder_4.newLineIfNotEmpty();
        builder.append(_builder_4);
      }
      StringConcatenation _builder_5 = new StringConcatenation();
      _builder_5.append(ProgramGenerator.INDENT);
      _builder_5.append("}");
      _builder_5.newLineIfNotEmpty();
      builder.append(_builder_5);
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }

  private String generateRunIter() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("public void runIter(long cycleTimeMs) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    memory.put(");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("        \"_global_time\",");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("        ((Long)memory.get(\"_global_time\")) + cycleTimeMs");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    );");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    for (IProcess p : processes)");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("        p.run();");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateDumpStates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("public Map<String,String> dumpProcessStates() {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    Map<String,String> res = new HashMap<>();");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    for (IProcess p : processes)");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("        p.dumpStates(res);");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    return res;");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateDumpTimers() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("public Map<String,Long> dumpProcessTimers() {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    Map<String,Long> res = new HashMap<>();");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    for (IProcess p : processes)");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("        p.dumpTimers(res);");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    return res;");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateDumpInputs() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("public Map<String,Object> dumpInputs() {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    Map<String,Object> res = new HashMap<>();");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    for (String n : inputNames)");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("        res.put(n, memory.get(n));");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    return res;");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateDumpOutputs() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("public Map<String,Object> dumpOutputs() {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    Map<String,Object> res = new HashMap<>();");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    for (String n : outputNames)");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("        res.put(n, memory.get(n));");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    return res;");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateDumpGlobals() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("public Map<String,Object> dumpGlobals() {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    Map<String,Object> res = new HashMap<>();");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    for (String n : globalNames)");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("        res.put(n, memory.get(n));");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    return res;");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateDumpVars() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("public Map<String,Object> dumpVars() {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    Map<String,Object> res = new HashMap<>();");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    for (String n : varNames)");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("        res.put(n, memory.get(n));");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    return res;");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateRegisterProcess() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("public void registerProcess(IProcess p) {");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("    processes.add(p);");
    _builder.newLineIfNotEmpty();
    _builder.append(ProgramGenerator.INDENT);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  public void registerAll(final Program program, final GenerationContext ctx) {
    EList<InputVarDeclaration> _progInVars = program.getProgInVars();
    for (final InputVarDeclaration v : _progInVars) {
      EList<VarInitDeclaration> _vars = v.getVars();
      for (final VarInitDeclaration decl : _vars) {
        final Procedure1<String> _function = (String name) -> {
          ctx.registerInputVar(name);
        };
        this.registerVarDecl(decl, ctx, _function);
      }
    }
    EList<OutputVarDeclaration> _progOutVars = program.getProgOutVars();
    for (final OutputVarDeclaration v_1 : _progOutVars) {
      EList<VarInitDeclaration> _vars_1 = v_1.getVars();
      for (final VarInitDeclaration decl_1 : _vars_1) {
        final Procedure1<String> _function_1 = (String name) -> {
          ctx.registerOutputVar(name);
        };
        this.registerVarDecl(decl_1, ctx, _function_1);
      }
    }
    EList<VarDeclaration> _progVars = program.getProgVars();
    for (final VarDeclaration v_2 : _progVars) {
      EList<VarInitDeclaration> _vars_2 = v_2.getVars();
      for (final VarInitDeclaration decl_2 : _vars_2) {
        final Procedure1<String> _function_2 = (String name) -> {
          ctx.registerLocalVar(name);
        };
        this.registerVarDecl(decl_2, ctx, _function_2);
      }
    }
    EList<InputOutputVarDeclaration> _progInOutVars = program.getProgInOutVars();
    for (final InputOutputVarDeclaration v_3 : _progInOutVars) {
      EList<VarInitDeclaration> _vars_3 = v_3.getVars();
      for (final VarInitDeclaration decl_3 : _vars_3) {
        final Procedure1<String> _function_3 = (String name) -> {
          ctx.registerInputVar(name);
          ctx.registerOutputVar(name);
        };
        this.registerVarDecl(decl_3, ctx, _function_3);
      }
    }
    EList<TempVarDeclaration> _progTempVars = program.getProgTempVars();
    for (final TempVarDeclaration v_4 : _progTempVars) {
      EList<VarInitDeclaration> _vars_4 = v_4.getVars();
      for (final VarInitDeclaration decl_4 : _vars_4) {
        final Procedure1<String> _function_4 = (String name) -> {
          ctx.registerLocalVar(name);
        };
        this.registerVarDecl(decl_4, ctx, _function_4);
      }
    }
    EList<su.nsk.iae.post.poST.Process> _processes = program.getProcesses();
    for (final su.nsk.iae.post.poST.Process p : _processes) {
      {
        final String field = StringExtensions.toFirstLower(p.getName());
        ctx.registerProcess(p.getName(), field, p.getName());
      }
    }
    EList<su.nsk.iae.post.poST.Process> _processes_1 = program.getProcesses();
    for (final su.nsk.iae.post.poST.Process p_1 : _processes_1) {
      {
        EList<InputVarDeclaration> _procInVars = p_1.getProcInVars();
        for (final InputVarDeclaration v_5 : _procInVars) {
          EList<VarInitDeclaration> _vars_5 = v_5.getVars();
          for (final VarInitDeclaration decl_5 : _vars_5) {
            final Procedure1<String> _function_5 = (String name) -> {
            };
            this.registerVarDecl(decl_5, ctx, _function_5);
          }
        }
        EList<OutputVarDeclaration> _procOutVars = p_1.getProcOutVars();
        for (final OutputVarDeclaration v_6 : _procOutVars) {
          EList<VarInitDeclaration> _vars_6 = v_6.getVars();
          for (final VarInitDeclaration decl_6 : _vars_6) {
            final Procedure1<String> _function_6 = (String name) -> {
            };
            this.registerVarDecl(decl_6, ctx, _function_6);
          }
        }
        EList<VarDeclaration> _procVars = p_1.getProcVars();
        for (final VarDeclaration v_7 : _procVars) {
          EList<VarInitDeclaration> _vars_7 = v_7.getVars();
          for (final VarInitDeclaration decl_7 : _vars_7) {
            final Procedure1<String> _function_7 = (String name) -> {
            };
            this.registerVarDecl(decl_7, ctx, _function_7);
          }
        }
        EList<InputOutputVarDeclaration> _procInOutVars = p_1.getProcInOutVars();
        for (final InputOutputVarDeclaration v_8 : _procInOutVars) {
          EList<VarInitDeclaration> _vars_8 = v_8.getVars();
          for (final VarInitDeclaration decl_8 : _vars_8) {
            final Procedure1<String> _function_8 = (String name) -> {
            };
            this.registerVarDecl(decl_8, ctx, _function_8);
          }
        }
        EList<ProcessVarDeclaration> _procProcessVars = p_1.getProcProcessVars();
        for (final ProcessVarDeclaration v_9 : _procProcessVars) {
          EList<ProcessVarInitDeclaration> _vars_9 = v_9.getVars();
          for (final ProcessVarInitDeclaration decl_9 : _vars_9) {
            EList<ProcessVariable> _vars_10 = decl_9.getVarList().getVars();
            for (final ProcessVariable vname : _vars_10) {
              {
                final String procType = decl_9.getProcess().getName();
                final String field = vname.getName();
                ctx.registerProcess(field, field, procType);
              }
            }
          }
        }
        EList<InputVarDeclaration> _procInVars_1 = p_1.getProcInVars();
        for (final InputVarDeclaration v_10 : _procInVars_1) {
          EList<VarInitDeclaration> _vars_11 = v_10.getVars();
          for (final VarInitDeclaration decl_10 : _vars_11) {
            EList<SymbolicVariable> _vars_12 = decl_10.getVarList().getVars();
            for (final SymbolicVariable vname_1 : _vars_12) {
              ctx.registerProcessInput(vname_1.getName());
            }
          }
        }
        EList<OutputVarDeclaration> _procOutVars_1 = p_1.getProcOutVars();
        for (final OutputVarDeclaration v_11 : _procOutVars_1) {
          EList<VarInitDeclaration> _vars_13 = v_11.getVars();
          for (final VarInitDeclaration decl_11 : _vars_13) {
            EList<SymbolicVariable> _vars_14 = decl_11.getVarList().getVars();
            for (final SymbolicVariable vname_2 : _vars_14) {
              ctx.registerProcessOutput(vname_2.getName());
            }
          }
        }
      }
    }
  }

  private String resolveType(final VarInitDeclaration decl) {
    SimpleSpecificationInit _spec = decl.getSpec();
    boolean _tripleNotEquals = (_spec != null);
    if (_tripleNotEquals) {
      return decl.getSpec().getType();
    }
    ArraySpecificationInit _arrSpec = decl.getArrSpec();
    boolean _tripleNotEquals_1 = (_arrSpec != null);
    if (_tripleNotEquals_1) {
      return decl.getArrSpec().getInit().getType();
    }
    throw new IllegalStateException(("Unknown declaration type: " + decl));
  }

  private void registerVarDecl(final VarInitDeclaration decl, final GenerationContext ctx, final Procedure1<? super String> registry) {
    final String type = this.resolveType(decl);
    EList<SymbolicVariable> _vars = decl.getVarList().getVars();
    for (final SymbolicVariable vname : _vars) {
      {
        ctx.registerVar(vname.getName(), type);
        registry.apply(vname.getName());
        if (((decl.getArrSpec() != null) && (!ctx.hasArrayElementType(vname.getName())))) {
          ctx.registerArrayType(vname.getName(), type);
        }
      }
    }
  }
}
