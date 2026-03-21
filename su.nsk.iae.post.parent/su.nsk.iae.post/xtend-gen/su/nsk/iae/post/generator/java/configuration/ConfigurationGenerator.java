package su.nsk.iae.post.generator.java.configuration;

import com.google.common.collect.Iterables;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.vars.GlobalVarDeclarationGenerator;
import su.nsk.iae.post.generator.java.common.vars.VarMemoryGenerator;
import su.nsk.iae.post.poST.Configuration;
import su.nsk.iae.post.poST.GlobalVarDeclaration;
import su.nsk.iae.post.poST.InputOutputVarDeclaration;
import su.nsk.iae.post.poST.InputVarDeclaration;
import su.nsk.iae.post.poST.OutputVarDeclaration;
import su.nsk.iae.post.poST.Program;
import su.nsk.iae.post.poST.ProgramConfiguration;
import su.nsk.iae.post.poST.Resource;
import su.nsk.iae.post.poST.TempVarDeclaration;
import su.nsk.iae.post.poST.VarDeclaration;
import su.nsk.iae.post.poST.VarInitDeclaration;

@SuppressWarnings("all")
public class ConfigurationGenerator {
  private final ResourceGenerator resourceGen = new ResourceGenerator();

  public String generate(final Configuration conf, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      final String IND = "        ";
      final StringBuilder builder = new StringBuilder();
      final String name = conf.getName();
      String programInstance = null;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("import java.util.Map;");
      _builder.newLine();
      _builder.append("import java.util.HashMap;");
      _builder.newLine();
      _builder.newLine();
      _builder.append("public class ");
      _builder.append(name);
      _builder.append("Simulation {");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public static void main(String[] args) throws Exception {");
      _builder.newLine();
      _builder.newLine();
      builder.append(_builder);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(IND);
      _builder_1.append("Map<String,Object> memory = new HashMap<>();");
      _builder_1.newLineIfNotEmpty();
      _builder_1.newLine();
      _builder_1.append(IND);
      _builder_1.append("memory.put(\"_global_time\", 0L);");
      _builder_1.newLineIfNotEmpty();
      builder.append(_builder_1);
      Iterable<GlobalVarDeclaration> _filter = Iterables.<GlobalVarDeclaration>filter(IteratorExtensions.<EObject>toIterable(conf.eAllContents()), GlobalVarDeclaration.class);
      for (final GlobalVarDeclaration g : _filter) {
        EObject _eContainer = g.eContainer();
        if ((_eContainer instanceof Configuration)) {
          builder.append(
            GlobalVarDeclarationGenerator.generate(g, ctx, IND));
        }
      }
      EList<Resource> _resources = conf.getResources();
      for (final Resource r : _resources) {
        {
          EList<ProgramConfiguration> _programConfs = r.getResStatement().getProgramConfs();
          for (final ProgramConfiguration pc : _programConfs) {
            {
              final Program program = pc.getProgram();
              EList<InputVarDeclaration> _progInVars = program.getProgInVars();
              for (final InputVarDeclaration v : _progInVars) {
                EList<VarInitDeclaration> _vars = v.getVars();
                for (final VarInitDeclaration decl : _vars) {
                  builder.append(VarMemoryGenerator.generate(decl, ctx, IND));
                }
              }
              EList<OutputVarDeclaration> _progOutVars = program.getProgOutVars();
              for (final OutputVarDeclaration v_1 : _progOutVars) {
                EList<VarInitDeclaration> _vars_1 = v_1.getVars();
                for (final VarInitDeclaration decl_1 : _vars_1) {
                  builder.append(VarMemoryGenerator.generate(decl_1, ctx, IND));
                }
              }
              EList<VarDeclaration> _progVars = program.getProgVars();
              for (final VarDeclaration v_2 : _progVars) {
                EList<VarInitDeclaration> _vars_2 = v_2.getVars();
                for (final VarInitDeclaration decl_2 : _vars_2) {
                  builder.append(VarMemoryGenerator.generate(decl_2, ctx, IND));
                }
              }
              EList<InputOutputVarDeclaration> _progInOutVars = program.getProgInOutVars();
              for (final InputOutputVarDeclaration v_3 : _progInOutVars) {
                EList<VarInitDeclaration> _vars_3 = v_3.getVars();
                for (final VarInitDeclaration decl_3 : _vars_3) {
                  builder.append(VarMemoryGenerator.generate(decl_3, ctx, IND));
                }
              }
              EList<TempVarDeclaration> _progTempVars = program.getProgTempVars();
              for (final TempVarDeclaration v_4 : _progTempVars) {
                EList<VarInitDeclaration> _vars_4 = v_4.getVars();
                for (final VarInitDeclaration decl_4 : _vars_4) {
                  builder.append(VarMemoryGenerator.generate(decl_4, ctx, IND));
                }
              }
              programInstance = pc.getName();
            }
          }
          builder.append(
            this.resourceGen.generate(r, ctx, IND));
        }
      }
      if ((programInstance == null)) {
        throw new IllegalStateException("No PROGRAM instance defined in CONFIGURATION");
      }
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.newLine();
      _builder_2.append(IND);
      _builder_2.append("while (true) {");
      _builder_2.newLineIfNotEmpty();
      _builder_2.newLine();
      _builder_2.append(IND);
      _builder_2.append("    ");
      _builder_2.append(programInstance);
      _builder_2.append(".runIter(taskTimeMs);");
      _builder_2.newLineIfNotEmpty();
      _builder_2.newLine();
      _builder_2.append(IND);
      _builder_2.append("    Thread.sleep(taskTimeMs);");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append(IND);
      _builder_2.append("}");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append("    ");
      _builder_2.append("}");
      _builder_2.newLine();
      _builder_2.newLine();
      _builder_2.append("}");
      _builder_2.newLine();
      builder.append(_builder_2);
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
}
