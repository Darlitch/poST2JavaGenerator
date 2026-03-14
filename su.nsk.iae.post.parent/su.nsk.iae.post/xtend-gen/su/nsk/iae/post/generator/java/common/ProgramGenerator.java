package su.nsk.iae.post.generator.java.common;

import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.vars.VarMemoryGenerator;
import su.nsk.iae.post.poST.InputOutputVarDeclaration;
import su.nsk.iae.post.poST.InputVarDeclaration;
import su.nsk.iae.post.poST.OutputVarDeclaration;
import su.nsk.iae.post.poST.Program;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.TempVarDeclaration;
import su.nsk.iae.post.poST.VarDeclaration;
import su.nsk.iae.post.poST.VarInitDeclaration;

@SuppressWarnings("all")
public class ProgramGenerator {
  private final ProcessGenerator processGen = new ProcessGenerator();

  public String generate(final Program program, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      final String name = program.getName();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("public class ");
      _builder.append(name);
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("private final java.util.Map<String,Object> memory =");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("new java.util.HashMap<>();");
      _builder.newLine();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("private final java.util.List<IProcess> processes =");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("new java.util.ArrayList<>();");
      _builder.newLine();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("private final java.util.Set<String> inputNames =");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("new java.util.HashSet<>();");
      _builder.newLine();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("private final java.util.Set<String> outputNames =");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("new java.util.HashSet<>();");
      _builder.newLine();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("private final java.util.Set<String> globalNames =");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("new java.util.HashSet<>();");
      _builder.newLine();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("private final java.util.Set<String> varNames =");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("new java.util.HashSet<>();");
      _builder.newLine();
      builder.append(_builder);
      builder.append(this.generateProcessFields(program));
      builder.append(this.generateConstructor(program, ctx));
      builder.append(this.generateRunIter());
      builder.append(this.generateDumpStates());
      builder.append(this.generateDumpTimers());
      builder.append(this.generateDumpInputs());
      builder.append(this.generateDumpOutputs());
      builder.append(this.generateDumpGlobals());
      builder.append(this.generateDumpVars());
      EList<su.nsk.iae.post.poST.Process> _processes = program.getProcesses();
      for (final su.nsk.iae.post.poST.Process p : _processes) {
        builder.append(
          this.processGen.generate(p, ctx, "    "));
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("}");
      _builder_1.newLine();
      builder.append(_builder_1);
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }

  private String generateProcessFields(final Program program) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      EList<su.nsk.iae.post.poST.Process> _processes = program.getProcesses();
      for (final su.nsk.iae.post.poST.Process p : _processes) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("private final ");
        String _name = p.getName();
        _builder.append(_name);
        _builder.append(" ");
        String _firstLower = StringExtensions.toFirstLower(p.getName());
        _builder.append(_firstLower);
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        builder.append(_builder);
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }

  private String generateConstructor(final Program program, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("public ");
      String _name = program.getName();
      _builder.append(_name);
      _builder.append("() {");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("memory.put(\"_global_time\", 0L);");
      _builder.newLine();
      builder.append(_builder);
      EList<InputVarDeclaration> _progInVars = program.getProgInVars();
      for (final InputVarDeclaration v : _progInVars) {
        EList<VarInitDeclaration> _vars = v.getVars();
        for (final VarInitDeclaration decl : _vars) {
          {
            EList<SymbolicVariable> _vars_1 = decl.getVarList().getVars();
            for (final SymbolicVariable vname : _vars_1) {
              ctx.registerInputVar(vname.getName());
            }
            builder.append(
              VarMemoryGenerator.generate(decl, ctx));
          }
        }
      }
      EList<OutputVarDeclaration> _progOutVars = program.getProgOutVars();
      for (final OutputVarDeclaration v_1 : _progOutVars) {
        EList<VarInitDeclaration> _vars_1 = v_1.getVars();
        for (final VarInitDeclaration decl_1 : _vars_1) {
          {
            EList<SymbolicVariable> _vars_2 = decl_1.getVarList().getVars();
            for (final SymbolicVariable vname : _vars_2) {
              ctx.registerOutputVar(vname.getName());
            }
            builder.append(
              VarMemoryGenerator.generate(decl_1, ctx));
          }
        }
      }
      EList<VarDeclaration> _progVars = program.getProgVars();
      for (final VarDeclaration v_2 : _progVars) {
        EList<VarInitDeclaration> _vars_2 = v_2.getVars();
        for (final VarInitDeclaration decl_2 : _vars_2) {
          {
            EList<SymbolicVariable> _vars_3 = decl_2.getVarList().getVars();
            for (final SymbolicVariable vname : _vars_3) {
              ctx.registerLocalVar(vname.getName());
            }
            builder.append(
              VarMemoryGenerator.generate(decl_2, ctx));
          }
        }
      }
      EList<InputOutputVarDeclaration> _progInOutVars = program.getProgInOutVars();
      for (final InputOutputVarDeclaration v_3 : _progInOutVars) {
        EList<VarInitDeclaration> _vars_3 = v_3.getVars();
        for (final VarInitDeclaration decl_3 : _vars_3) {
          {
            EList<SymbolicVariable> _vars_4 = decl_3.getVarList().getVars();
            for (final SymbolicVariable vname : _vars_4) {
              {
                ctx.registerInputVar(vname.getName());
                ctx.registerOutputVar(vname.getName());
              }
            }
            builder.append(
              VarMemoryGenerator.generate(decl_3, ctx));
          }
        }
      }
      EList<TempVarDeclaration> _progTempVars = program.getProgTempVars();
      for (final TempVarDeclaration v_4 : _progTempVars) {
        EList<VarInitDeclaration> _vars_4 = v_4.getVars();
        for (final VarInitDeclaration decl_4 : _vars_4) {
          {
            EList<SymbolicVariable> _vars_5 = decl_4.getVarList().getVars();
            for (final SymbolicVariable vname : _vars_5) {
              ctx.registerLocalVar(vname.getName());
            }
            builder.append(
              VarMemoryGenerator.generate(decl_4, ctx));
          }
        }
      }
      Set<String> _inputVars = ctx.getInputVars();
      for (final String n : _inputVars) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("inputNames.add(\"");
        _builder_1.append(n);
        _builder_1.append("\");");
        _builder_1.newLineIfNotEmpty();
        builder.append(_builder_1);
      }
      Set<String> _outputVars = ctx.getOutputVars();
      for (final String n_1 : _outputVars) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("outputNames.add(\"");
        _builder_2.append(n_1);
        _builder_2.append("\");");
        _builder_2.newLineIfNotEmpty();
        builder.append(_builder_2);
      }
      Set<String> _globalVars = ctx.getGlobalVars();
      for (final String n_2 : _globalVars) {
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("globalNames.add(\"");
        _builder_3.append(n_2);
        _builder_3.append("\");");
        _builder_3.newLineIfNotEmpty();
        builder.append(_builder_3);
      }
      Set<String> _localVars = ctx.getLocalVars();
      for (final String n_3 : _localVars) {
        StringConcatenation _builder_4 = new StringConcatenation();
        _builder_4.append("varNames.add(\"");
        _builder_4.append(n_3);
        _builder_4.append("\");");
        _builder_4.newLineIfNotEmpty();
        builder.append(_builder_4);
      }
      EList<su.nsk.iae.post.poST.Process> _processes = program.getProcesses();
      for (final su.nsk.iae.post.poST.Process p : _processes) {
        {
          final String field = StringExtensions.toFirstLower(p.getName());
          StringConcatenation _builder_5 = new StringConcatenation();
          _builder_5.append(field);
          _builder_5.append(" = new ");
          String _name_1 = p.getName();
          _builder_5.append(_name_1);
          _builder_5.append("(memory);");
          _builder_5.newLineIfNotEmpty();
          _builder_5.append("processes.add(");
          _builder_5.append(field);
          _builder_5.append(");");
          _builder_5.newLineIfNotEmpty();
          builder.append(_builder_5);
        }
      }
      StringConcatenation _builder_5 = new StringConcatenation();
      _builder_5.append("}");
      _builder_5.newLine();
      builder.append(_builder_5);
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }

  private String generateRunIter() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void runIter(long cycleTimeMs) {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("memory.put(");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("\"_global_time\",");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("((Long)memory.get(\"_global_time\")) + cycleTimeMs");
    _builder.newLine();
    _builder.append("    ");
    _builder.append(");");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("for (IProcess p : processes)");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("p.run();");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }

  private String generateDumpStates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public java.util.Map<String,String> dumpProcessStates() {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("java.util.Map<String,String> res =");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("new java.util.HashMap<>();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("for (IProcess p : processes)");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("p.dumpStates(res);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("return res;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }

  private String generateDumpTimers() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public java.util.Map<String,Long> dumpProcessTimers() {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("java.util.Map<String,Long> res =");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("new java.util.HashMap<>();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("for (IProcess p : processes)");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("p.dumpTimers(res);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("return res;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }

  private String generateDumpInputs() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public java.util.Map<String,Object> dumpInputs() {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("java.util.Map<String,Object> res =");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("new java.util.HashMap<>();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("for (String n : inputNames)");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("res.put(n, memory.get(n));");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("return res;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }

  private String generateDumpOutputs() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public java.util.Map<String,Object> dumpOutputs() {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("java.util.Map<String,Object> res =");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("new java.util.HashMap<>();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("for (String n : outputNames)");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("res.put(n, memory.get(n));");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("return res;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }

  private String generateDumpGlobals() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public java.util.Map<String,Object> dumpGlobals() {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("java.util.Map<String,Object> res =");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("new java.util.HashMap<>();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("for (String n : globalNames)");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("res.put(n, memory.get(n));");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("return res;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }

  private String generateDumpVars() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public java.util.Map<String,Object> dumpVars() {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("java.util.Map<String,Object> res =");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("new java.util.HashMap<>();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("for (String n : varNames)");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("res.put(n, memory.get(n));");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("return res;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
