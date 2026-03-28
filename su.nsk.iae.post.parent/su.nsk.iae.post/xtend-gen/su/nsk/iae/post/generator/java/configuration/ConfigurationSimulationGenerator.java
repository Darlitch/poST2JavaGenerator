package su.nsk.iae.post.generator.java.configuration;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
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
import su.nsk.iae.post.poST.SingleResource;
import su.nsk.iae.post.poST.TempVarDeclaration;
import su.nsk.iae.post.poST.VarDeclaration;
import su.nsk.iae.post.poST.VarInitDeclaration;

@SuppressWarnings("all")
public class ConfigurationSimulationGenerator {
  private final ResourceGenerator resourceGen = new ResourceGenerator();

  private final SimulationClassGenerator simulationGen = new SimulationClassGenerator();

  public String generate(final Configuration conf, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      final String fields = this.generateFields(conf, "    ");
      final String constructorBody = this.generateConstructorBody(conf, ctx, "        ");
      final String programInstanceName = this.resolveProgramInstanceName(conf);
      _xblockexpression = this.simulationGen.generate(fields, constructorBody, programInstanceName);
    }
    return _xblockexpression;
  }

  private String generateFields(final Configuration conf, final String indent) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      List<ProgramConfiguration> _collectProgramConfigurations = this.collectProgramConfigurations(conf);
      for (final ProgramConfiguration pc : _collectProgramConfigurations) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(indent);
        _builder.append("private final ");
        String _name = pc.getProgram().getName();
        _builder.append(_name);
        _builder.append(" ");
        String _name_1 = pc.getName();
        _builder.append(_name_1);
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        builder.append(_builder);
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }

  private String generateConstructorBody(final Configuration conf, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      Iterable<GlobalVarDeclaration> _filter = Iterables.<GlobalVarDeclaration>filter(IteratorExtensions.<EObject>toIterable(conf.eAllContents()), GlobalVarDeclaration.class);
      for (final GlobalVarDeclaration g : _filter) {
        EObject _eContainer = g.eContainer();
        if ((_eContainer instanceof Configuration)) {
          builder.append(
            GlobalVarDeclarationGenerator.generate(g, ctx, indent));
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
                  builder.append(VarMemoryGenerator.generate(decl, ctx, indent));
                }
              }
              EList<OutputVarDeclaration> _progOutVars = program.getProgOutVars();
              for (final OutputVarDeclaration v_1 : _progOutVars) {
                EList<VarInitDeclaration> _vars_1 = v_1.getVars();
                for (final VarInitDeclaration decl_1 : _vars_1) {
                  builder.append(VarMemoryGenerator.generate(decl_1, ctx, indent));
                }
              }
              EList<VarDeclaration> _progVars = program.getProgVars();
              for (final VarDeclaration v_2 : _progVars) {
                EList<VarInitDeclaration> _vars_2 = v_2.getVars();
                for (final VarInitDeclaration decl_2 : _vars_2) {
                  builder.append(VarMemoryGenerator.generate(decl_2, ctx, indent));
                }
              }
              EList<InputOutputVarDeclaration> _progInOutVars = program.getProgInOutVars();
              for (final InputOutputVarDeclaration v_3 : _progInOutVars) {
                EList<VarInitDeclaration> _vars_3 = v_3.getVars();
                for (final VarInitDeclaration decl_3 : _vars_3) {
                  builder.append(VarMemoryGenerator.generate(decl_3, ctx, indent));
                }
              }
              EList<TempVarDeclaration> _progTempVars = program.getProgTempVars();
              for (final TempVarDeclaration v_4 : _progTempVars) {
                EList<VarInitDeclaration> _vars_4 = v_4.getVars();
                for (final VarInitDeclaration decl_4 : _vars_4) {
                  builder.append(VarMemoryGenerator.generate(decl_4, ctx, indent));
                }
              }
            }
          }
          builder.append(
            this.resourceGen.generate(r, ctx, indent));
        }
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }

  private List<ProgramConfiguration> collectProgramConfigurations(final Configuration conf) {
    ArrayList<ProgramConfiguration> _xblockexpression = null;
    {
      final ArrayList<ProgramConfiguration> result = new ArrayList<ProgramConfiguration>();
      EList<Resource> _resources = conf.getResources();
      for (final Resource r : _resources) {
        SingleResource _resStatement = r.getResStatement();
        boolean _tripleNotEquals = (_resStatement != null);
        if (_tripleNotEquals) {
          result.addAll(r.getResStatement().getProgramConfs());
        }
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }

  private String resolveProgramInstanceName(final Configuration conf) {
    String _xblockexpression = null;
    {
      final List<ProgramConfiguration> pcs = this.collectProgramConfigurations(conf);
      boolean _isEmpty = pcs.isEmpty();
      if (_isEmpty) {
        throw new IllegalStateException("No PROGRAM instance defined in CONFIGURATION");
      }
      _xblockexpression = IterableExtensions.<ProgramConfiguration>head(pcs).getName();
    }
    return _xblockexpression;
  }
}
