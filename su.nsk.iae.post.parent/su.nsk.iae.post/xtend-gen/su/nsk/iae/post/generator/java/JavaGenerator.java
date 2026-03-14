package su.nsk.iae.post.generator.java;

import com.google.common.collect.Iterables;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import su.nsk.iae.post.generator.IPoSTGenerator;
import su.nsk.iae.post.generator.java.common.IProcessGenerator;
import su.nsk.iae.post.generator.java.common.ProgramGenerator;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.configuration.ConfigurationGenerator;
import su.nsk.iae.post.poST.Configuration;
import su.nsk.iae.post.poST.Model;
import su.nsk.iae.post.poST.Program;

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
    fsa.generateFile(
      "IProcess.java", 
      IProcessGenerator.generate());
    EList<Program> _programs = model.getPrograms();
    for (final Program p : _programs) {
      {
        final String code = new ProgramGenerator().generate(p, ctx);
        String _name = p.getName();
        String _plus = (_name + ".java");
        fsa.generateFile(_plus, code);
      }
    }
    Configuration _conf = model.getConf();
    boolean _tripleNotEquals = (_conf != null);
    if (_tripleNotEquals) {
      final String code = new ConfigurationGenerator().generate(model.getConf(), ctx);
      String _name = model.getConf().getName();
      String _plus = (_name + "Simulation.java");
      fsa.generateFile(_plus, code);
    }
  }

  @Override
  public void afterGenerate(final Resource input, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
  }
}
