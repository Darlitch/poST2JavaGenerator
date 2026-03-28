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
  private final SimulationClassGenerator simulationGen = new SimulationClassGenerator();

  public String generate(final Model model, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      final String fields = this.generateFields(model, "    ");
      final String constructorBody = this.generateConstructorBody(model, ctx, "        ");
      final String programRunBody = this.generateProgramRunBody(model, "        ");
      _xblockexpression = this.simulationGen.generate(fields, constructorBody, programRunBody);
    }
    return _xblockexpression;
  }

  private String generateFields(final Model model, final String indent) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      EList<Program> _programs = model.getPrograms();
      for (final Program p : _programs) {
        {
          final String instance = StringExtensions.toFirstLower(p.getName());
          StringConcatenation _builder = new StringConcatenation();
          _builder.append(indent);
          _builder.append("private final ");
          String _name = p.getName();
          _builder.append(_name);
          _builder.append(" ");
          _builder.append(instance);
          _builder.append(";");
          _builder.newLineIfNotEmpty();
          builder.append(_builder);
        }
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }

  private String generateConstructorBody(final Model model, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(indent);
      _builder.append("this.taskTimeMs = 100L;");
      _builder.newLineIfNotEmpty();
      builder.append(_builder);
      EList<Program> _programs = model.getPrograms();
      for (final Program p : _programs) {
        {
          EList<InputVarDeclaration> _progInVars = p.getProgInVars();
          for (final InputVarDeclaration v : _progInVars) {
            EList<VarInitDeclaration> _vars = v.getVars();
            for (final VarInitDeclaration decl : _vars) {
              builder.append(VarMemoryGenerator.generate(decl, ctx, indent));
            }
          }
          EList<OutputVarDeclaration> _progOutVars = p.getProgOutVars();
          for (final OutputVarDeclaration v_1 : _progOutVars) {
            EList<VarInitDeclaration> _vars_1 = v_1.getVars();
            for (final VarInitDeclaration decl_1 : _vars_1) {
              builder.append(VarMemoryGenerator.generate(decl_1, ctx, indent));
            }
          }
          EList<VarDeclaration> _progVars = p.getProgVars();
          for (final VarDeclaration v_2 : _progVars) {
            EList<VarInitDeclaration> _vars_2 = v_2.getVars();
            for (final VarInitDeclaration decl_2 : _vars_2) {
              builder.append(VarMemoryGenerator.generate(decl_2, ctx, indent));
            }
          }
          EList<InputOutputVarDeclaration> _progInOutVars = p.getProgInOutVars();
          for (final InputOutputVarDeclaration v_3 : _progInOutVars) {
            EList<VarInitDeclaration> _vars_3 = v_3.getVars();
            for (final VarInitDeclaration decl_3 : _vars_3) {
              builder.append(VarMemoryGenerator.generate(decl_3, ctx, indent));
            }
          }
          EList<TempVarDeclaration> _progTempVars = p.getProgTempVars();
          for (final TempVarDeclaration v_4 : _progTempVars) {
            EList<VarInitDeclaration> _vars_4 = v_4.getVars();
            for (final VarInitDeclaration decl_4 : _vars_4) {
              builder.append(VarMemoryGenerator.generate(decl_4, ctx, indent));
            }
          }
        }
      }
      EList<Program> _programs_1 = model.getPrograms();
      for (final Program p_1 : _programs_1) {
        {
          final String name = p_1.getName();
          final String instance = StringExtensions.toFirstLower(name);
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.newLine();
          _builder_1.append(indent);
          _builder_1.append("this.");
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
              final String programType = p_3.getName();
              StringConcatenation _builder_1 = new StringConcatenation();
              _builder_1.newLine();
              _builder_1.append(indent);
              _builder_1.append("Map<String,String> ");
              _builder_1.append(procName);
              _builder_1.append("_aliases = new HashMap<>();");
              _builder_1.newLineIfNotEmpty();
              _builder_1.append(indent);
              _builder_1.append(programType);
              _builder_1.append(".");
              _builder_1.append(procType);
              _builder_1.append(" ");
              _builder_1.append(procName);
              _builder_1.append(" = new ");
              _builder_1.append(programType);
              _builder_1.append(".");
              _builder_1.append(procType);
              _builder_1.append("(\"");
              _builder_1.append(procName);
              _builder_1.append("\", memory, ");
              _builder_1.append(procName);
              _builder_1.append("_aliases, processMap);");
              _builder_1.newLineIfNotEmpty();
              _builder_1.append(indent);
              _builder_1.append(programInstance);
              _builder_1.append(".registerProcess(");
              _builder_1.append(procName);
              _builder_1.append(");");
              _builder_1.newLineIfNotEmpty();
              builder.append(_builder_1);
              boolean hasInit = false;
              EList<su.nsk.iae.post.poST.Process> _processes_2 = p_3.getProcesses();
              for (final su.nsk.iae.post.poST.Process proc2 : _processes_2) {
                boolean _equals = proc2.getName().equals("Init");
                if (_equals) {
                  hasInit = true;
                }
              }
              boolean _equals_1 = proc_1.getName().equals("Init");
              if (_equals_1) {
                StringConcatenation _builder_2 = new StringConcatenation();
                _builder_2.append(indent);
                _builder_2.append(procName);
                _builder_2.append(".start();");
                _builder_2.newLineIfNotEmpty();
                builder.append(_builder_2);
              } else {
                if (((!hasInit) && (proc_1 == p_3.getProcesses().get(0)))) {
                  StringConcatenation _builder_3 = new StringConcatenation();
                  _builder_3.append(indent);
                  _builder_3.append(procName);
                  _builder_3.append(".start();");
                  _builder_3.newLineIfNotEmpty();
                  builder.append(_builder_3);
                }
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
                      _builder_1.append(indent);
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
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }

  private String generateProgramRunBody(final Model model, final String indent) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      EList<Program> _programs = model.getPrograms();
      for (final Program p : _programs) {
        {
          final String instance = StringExtensions.toFirstLower(p.getName());
          StringConcatenation _builder = new StringConcatenation();
          _builder.append(indent);
          _builder.append(instance);
          _builder.append(".runIter(taskTimeMs);");
          _builder.newLineIfNotEmpty();
          builder.append(_builder);
        }
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
}
