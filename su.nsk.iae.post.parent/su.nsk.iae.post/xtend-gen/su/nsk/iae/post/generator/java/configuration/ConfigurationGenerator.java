package su.nsk.iae.post.generator.java.configuration;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.poST.Configuration;
import su.nsk.iae.post.poST.ProgramConfiguration;
import su.nsk.iae.post.poST.Resource;

@SuppressWarnings("all")
public class ConfigurationGenerator {
  private final ResourceGenerator resourceGen = new ResourceGenerator();

  public String generate(final Configuration conf, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      final String name = conf.getName();
      String programInstance = null;
      StringConcatenation _builder = new StringConcatenation();
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
      EList<Resource> _resources = conf.getResources();
      for (final Resource r : _resources) {
        {
          builder.append(
            this.resourceGen.generate(r, ctx));
          EList<ProgramConfiguration> _programConfs = r.getResStatement().getProgramConfs();
          for (final ProgramConfiguration pc : _programConfs) {
            programInstance = pc.getName();
          }
        }
      }
      if ((programInstance == null)) {
        throw new IllegalStateException("No PROGRAM instance defined in CONFIGURATION");
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("while (true) {");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("    ");
      _builder_1.append(programInstance, "    ");
      _builder_1.append(".runIter(taskTimeMs);");
      _builder_1.newLineIfNotEmpty();
      _builder_1.newLine();
      _builder_1.append("    ");
      _builder_1.append("Thread.sleep(taskTimeMs);");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("    ");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      builder.append(_builder_1);
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
}
