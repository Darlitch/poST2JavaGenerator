package su.nsk.iae.post.generator.java.configuration;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.vars.VarMemoryGenerator;
import su.nsk.iae.post.poST.InputOutputVarDeclaration;
import su.nsk.iae.post.poST.InputVarDeclaration;
import su.nsk.iae.post.poST.Model;
import su.nsk.iae.post.poST.OutputVarDeclaration;
import su.nsk.iae.post.poST.ProcessVarDeclaration;
import su.nsk.iae.post.poST.ProcessVarInitDeclaration;
import su.nsk.iae.post.poST.ProcessVariable;
import su.nsk.iae.post.poST.Program;
import su.nsk.iae.post.poST.TempVarDeclaration;
import su.nsk.iae.post.poST.VarDeclaration;
import su.nsk.iae.post.poST.VarInitDeclaration;

@SuppressWarnings("all")
public class DefaultSimulationGenerator {
  public String generate(final Model model, final GenerationContext ctx) {
    final String IND = "        ";
    final StringBuilder builder = new StringBuilder();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.Map;");
    _builder.newLine();
    _builder.append("import java.util.HashMap;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public class Simulation {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("public static void main(String[] args) throws Exception {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("Map<String,Object> memory = new HashMap<>();");
    _builder.newLine();
    _builder.append("        ");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("Map<String, IProcess> processMap = new HashMap<>();");
    _builder.newLine();
    _builder.append("        ");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("memory.put(\"_global_time\", 0L);");
    _builder.newLine();
    builder.append(_builder);
    EList<Program> _programs = model.getPrograms();
    for (final Program p : _programs) {
      {
        EList<InputVarDeclaration> _progInVars = p.getProgInVars();
        for (final InputVarDeclaration v : _progInVars) {
          EList<VarInitDeclaration> _vars = v.getVars();
          for (final VarInitDeclaration decl : _vars) {
            builder.append(VarMemoryGenerator.generate(decl, ctx, IND));
          }
        }
        EList<OutputVarDeclaration> _progOutVars = p.getProgOutVars();
        for (final OutputVarDeclaration v_1 : _progOutVars) {
          EList<VarInitDeclaration> _vars_1 = v_1.getVars();
          for (final VarInitDeclaration decl_1 : _vars_1) {
            builder.append(VarMemoryGenerator.generate(decl_1, ctx, IND));
          }
        }
        EList<VarDeclaration> _progVars = p.getProgVars();
        for (final VarDeclaration v_2 : _progVars) {
          EList<VarInitDeclaration> _vars_2 = v_2.getVars();
          for (final VarInitDeclaration decl_2 : _vars_2) {
            builder.append(VarMemoryGenerator.generate(decl_2, ctx, IND));
          }
        }
        EList<InputOutputVarDeclaration> _progInOutVars = p.getProgInOutVars();
        for (final InputOutputVarDeclaration v_3 : _progInOutVars) {
          EList<VarInitDeclaration> _vars_3 = v_3.getVars();
          for (final VarInitDeclaration decl_3 : _vars_3) {
            builder.append(VarMemoryGenerator.generate(decl_3, ctx, IND));
          }
        }
        EList<TempVarDeclaration> _progTempVars = p.getProgTempVars();
        for (final TempVarDeclaration v_4 : _progTempVars) {
          EList<VarInitDeclaration> _vars_4 = v_4.getVars();
          for (final VarInitDeclaration decl_4 : _vars_4) {
            builder.append(VarMemoryGenerator.generate(decl_4, ctx, IND));
          }
        }
      }
    }
    EList<Program> _programs_1 = model.getPrograms();
    for (final Program p_1 : _programs_1) {
      {
        final String name = p_1.getName();
        final String instance = StringExtensions.toFirstLower(name);
        builder.append("\n");
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append(IND);
        _builder_1.append(name);
        _builder_1.append(" ");
        _builder_1.append(instance);
        _builder_1.append(" = new ");
        _builder_1.append(name);
        _builder_1.append("(memory, processMap);");
        _builder_1.newLineIfNotEmpty();
        builder.append(_builder_1);
      }
    }
    EList<Program> _programs_2 = model.getPrograms();
    for (final Program p_2 : _programs_2) {
      EList<su.nsk.iae.post.poST.Process> _processes = p_2.getProcesses();
      for (final su.nsk.iae.post.poST.Process proc : _processes) {
        ctx.registerProcess(
          proc.getName(), 
          StringExtensions.toFirstLower(proc.getName()), 
          proc.getName());
      }
    }
    EList<Program> _programs_3 = model.getPrograms();
    for (final Program p_3 : _programs_3) {
      {
        final String programInstance = StringExtensions.toFirstLower(p_3.getName());
        EList<su.nsk.iae.post.poST.Process> _processes_1 = p_3.getProcesses();
        for (final su.nsk.iae.post.poST.Process proc_1 : _processes_1) {
          {
            final String procName = StringExtensions.toFirstLower(proc_1.getName());
            final String procType = proc_1.getName();
            builder.append("\n");
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append(IND);
            _builder_1.append("Map<String,String> ");
            _builder_1.append(procName);
            _builder_1.append("_aliases = new HashMap<>();");
            _builder_1.newLineIfNotEmpty();
            _builder_1.append(IND);
            _builder_1.append(procType);
            _builder_1.append(" ");
            _builder_1.append(procName);
            _builder_1.append(" = new ");
            _builder_1.append(procType);
            _builder_1.append("(\"");
            _builder_1.append(procName);
            _builder_1.append("\", memory, ");
            _builder_1.append(procName);
            _builder_1.append("_aliases, processMap);");
            _builder_1.newLineIfNotEmpty();
            _builder_1.append(IND);
            _builder_1.append(programInstance);
            _builder_1.append(".registerProcess(");
            _builder_1.append(procName);
            _builder_1.append(");");
            _builder_1.newLineIfNotEmpty();
            builder.append(_builder_1);
            boolean _equals = proc_1.getName().equals("Init");
            if (_equals) {
              StringConcatenation _builder_2 = new StringConcatenation();
              _builder_2.append(IND);
              _builder_2.append(procName);
              _builder_2.append(".start();");
              _builder_2.newLineIfNotEmpty();
              builder.append(_builder_2);
            }
          }
        }
        EList<su.nsk.iae.post.poST.Process> _processes_2 = p_3.getProcesses();
        for (final su.nsk.iae.post.poST.Process proc_2 : _processes_2) {
          {
            final String procName = StringExtensions.toFirstLower(proc_2.getName());
            EList<ProcessVarDeclaration> _procProcessVars = proc_2.getProcProcessVars();
            for (final ProcessVarDeclaration v : _procProcessVars) {
              EList<ProcessVarInitDeclaration> _vars = v.getVars();
              for (final ProcessVarInitDeclaration decl : _vars) {
                EList<ProcessVariable> _vars_1 = decl.getVarList().getVars();
                for (final ProcessVariable vname : _vars_1) {
                  {
                    final String target = vname.getName();
                    final String resolved = ctx.resolveProcess(target);
                    StringConcatenation _builder_1 = new StringConcatenation();
                    _builder_1.append(IND);
                    _builder_1.append(procName);
                    _builder_1.append(".setProcess(\"");
                    _builder_1.append(target);
                    _builder_1.append("\", ");
                    _builder_1.append(resolved);
                    _builder_1.append(");");
                    _builder_1.newLineIfNotEmpty();
                    builder.append(_builder_1);
                  }
                }
              }
            }
          }
        }
      }
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.newLine();
    _builder_1.append(IND);
    _builder_1.append("long taskTimeMs = 100L;");
    _builder_1.newLineIfNotEmpty();
    _builder_1.newLine();
    _builder_1.append(IND);
    _builder_1.append("while (true) {");
    _builder_1.newLineIfNotEmpty();
    builder.append(_builder_1);
    EList<Program> _programs_4 = model.getPrograms();
    for (final Program p_4 : _programs_4) {
      {
        final String instance = StringExtensions.toFirstLower(p_4.getName());
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append(IND);
        _builder_2.append("    ");
        _builder_2.append(instance);
        _builder_2.append(".runIter(taskTimeMs);");
        _builder_2.newLineIfNotEmpty();
        builder.append(_builder_2);
      }
    }
    StringConcatenation _builder_2 = new StringConcatenation();
    _builder_2.append(IND);
    _builder_2.append("    Thread.sleep(taskTimeMs);");
    _builder_2.newLineIfNotEmpty();
    _builder_2.append("        ");
    _builder_2.append("}");
    _builder_2.newLine();
    _builder_2.append("    ");
    _builder_2.append("}");
    _builder_2.newLine();
    _builder_2.append("}");
    _builder_2.newLine();
    builder.append(_builder_2);
    return builder.toString();
  }
}
