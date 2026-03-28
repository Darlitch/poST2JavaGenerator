package su.nsk.iae.post.generator.java;

import com.google.common.collect.Iterables;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import su.nsk.iae.post.generator.IPoSTGenerator;
import su.nsk.iae.post.generator.java.common.BaseProcessGenerator;
import su.nsk.iae.post.generator.java.common.IProcessGenerator;
import su.nsk.iae.post.generator.java.common.ProgramGenerator;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.util.CompileTimeEvaluator;
import su.nsk.iae.post.generator.java.configuration.ConfigurationSimulationGenerator;
import su.nsk.iae.post.generator.java.configuration.DefaultSimulationGenerator;
import su.nsk.iae.post.poST.Configuration;
import su.nsk.iae.post.poST.GlobalVarDeclaration;
import su.nsk.iae.post.poST.Model;
import su.nsk.iae.post.poST.Program;
import su.nsk.iae.post.poST.SimpleSpecificationInit;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.VarInitDeclaration;

@SuppressWarnings("all")
public class JavaGenerator implements IPoSTGenerator {
  @Override
  public void setModel(final Model model) {
  }

  @Override
  public void beforeGenerate(final Resource input, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
  }

  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    final Model model = IterableExtensions.<Model>head(Iterables.<Model>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), Model.class));
    final GenerationContext ctx = new GenerationContext();
    InputOutput.<String>println("Generating IProcess.java");
    fsa.generateFile(
      "IProcess.java", 
      IProcessGenerator.generate());
    InputOutput.<String>println("Generating BaseProcess.java");
    fsa.generateFile(
      "BaseProcess.java", 
      BaseProcessGenerator.generate());
    this.registerGlobals(model, ctx);
    EList<Program> _programs = model.getPrograms();
    for (final Program p : _programs) {
      new ProgramGenerator().registerAll(p, ctx);
    }
    Configuration _conf = model.getConf();
    boolean _tripleNotEquals = (_conf != null);
    if (_tripleNotEquals) {
      final String code = new ConfigurationSimulationGenerator().generate(model.getConf(), ctx);
      InputOutput.<String>println("Generating Simulation.java");
      fsa.generateFile(
        "Simulation.java", code);
    } else {
      final String code_1 = new DefaultSimulationGenerator().generate(model, ctx);
      InputOutput.<String>println("Generating Simulation.java");
      fsa.generateFile(
        "Simulation.java", code_1);
    }
    EList<Program> _programs_1 = model.getPrograms();
    for (final Program p_1 : _programs_1) {
      {
        final String code_2 = new ProgramGenerator().generate(p_1, ctx);
        String _name = p_1.getName();
        String _plus = ("Generating program: " + _name);
        String _plus_1 = (_plus + ".java");
        InputOutput.<String>println(_plus_1);
        String _name_1 = p_1.getName();
        String _plus_2 = (_name_1 + ".java");
        fsa.generateFile(_plus_2, code_2);
      }
    }
  }

  public void registerGlobals(final Model model, final GenerationContext ctx) {
    Configuration _conf = model.getConf();
    boolean _tripleEquals = (_conf == null);
    if (_tripleEquals) {
      return;
    }
    Iterable<GlobalVarDeclaration> _filter = Iterables.<GlobalVarDeclaration>filter(model.getConf().eContents(), GlobalVarDeclaration.class);
    for (final GlobalVarDeclaration g : _filter) {
      EList<VarInitDeclaration> _varsSimple = g.getVarsSimple();
      for (final VarInitDeclaration decl : _varsSimple) {
        boolean _isConst = g.isConst();
        if (_isConst) {
          EList<SymbolicVariable> _vars = decl.getVarList().getVars();
          for (final SymbolicVariable vname : _vars) {
            {
              final Object value = CompileTimeEvaluator.evalExpression(decl.getSpec().getValue(), ctx);
              ctx.registerConst(vname.getName(), value);
              ctx.registerVar(vname.getName(), decl.getSpec().getType());
            }
          }
        } else {
          EList<SymbolicVariable> _vars_1 = decl.getVarList().getVars();
          for (final SymbolicVariable vname_1 : _vars_1) {
            {
              String _xifexpression = null;
              SimpleSpecificationInit _spec = decl.getSpec();
              boolean _tripleNotEquals = (_spec != null);
              if (_tripleNotEquals) {
                _xifexpression = decl.getSpec().getType();
              } else {
                _xifexpression = decl.getArrSpec().getInit().getType();
              }
              final String type = _xifexpression;
              ctx.registerVar(vname_1.getName(), type);
              ctx.registerGlobalVar(vname_1.getName());
            }
          }
        }
      }
    }
    EList<su.nsk.iae.post.poST.Resource> _resources = model.getConf().getResources();
    for (final su.nsk.iae.post.poST.Resource res : _resources) {
      EList<GlobalVarDeclaration> _resGlobVars = res.getResGlobVars();
      for (final GlobalVarDeclaration g_1 : _resGlobVars) {
        EList<VarInitDeclaration> _varsSimple_1 = g_1.getVarsSimple();
        for (final VarInitDeclaration decl_1 : _varsSimple_1) {
          boolean _isConst_1 = g_1.isConst();
          if (_isConst_1) {
            EList<SymbolicVariable> _vars_2 = decl_1.getVarList().getVars();
            for (final SymbolicVariable vname_2 : _vars_2) {
              {
                final Object value = CompileTimeEvaluator.evalExpression(decl_1.getSpec().getValue(), ctx);
                ctx.registerConst(vname_2.getName(), value);
                ctx.registerVar(vname_2.getName(), decl_1.getSpec().getType());
                String _name = vname_2.getName();
                String _plus = ("CONST REGISTERED: " + _name);
                InputOutput.<String>println(_plus);
              }
            }
          } else {
            EList<SymbolicVariable> _vars_3 = decl_1.getVarList().getVars();
            for (final SymbolicVariable vname_3 : _vars_3) {
              {
                String _xifexpression = null;
                SimpleSpecificationInit _spec = decl_1.getSpec();
                boolean _tripleNotEquals = (_spec != null);
                if (_tripleNotEquals) {
                  _xifexpression = decl_1.getSpec().getType();
                } else {
                  _xifexpression = decl_1.getArrSpec().getInit().getType();
                }
                final String type = _xifexpression;
                ctx.registerVar(vname_3.getName(), type);
                ctx.registerGlobalVar(vname_3.getName());
              }
            }
          }
        }
      }
    }
  }

  @Override
  public void afterGenerate(final Resource input, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
  }
}
