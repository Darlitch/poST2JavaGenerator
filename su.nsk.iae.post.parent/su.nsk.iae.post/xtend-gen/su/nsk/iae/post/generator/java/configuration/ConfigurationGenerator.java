package su.nsk.iae.post.generator.java.configuration;

import com.google.common.collect.Iterables;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.vars.GlobalVarDeclarationGenerator;
import su.nsk.iae.post.poST.Configuration;
import su.nsk.iae.post.poST.GlobalVarDeclaration;
import su.nsk.iae.post.poST.ProgramConfiguration;
import su.nsk.iae.post.poST.Resource;

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
          builder.append(
            this.resourceGen.generate(r, ctx, IND));
          EList<ProgramConfiguration> _programConfs = r.getResStatement().getProgramConfs();
          for (final ProgramConfiguration pc : _programConfs) {
            programInstance = pc.getName();
          }
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
