package su.nsk.iae.post.generator.java.configuration;

import org.eclipse.emf.common.util.EList;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.vars.GlobalVarDeclarationGenerator;
import su.nsk.iae.post.poST.GlobalVarDeclaration;
import su.nsk.iae.post.poST.ProgramConfiguration;
import su.nsk.iae.post.poST.Resource;
import su.nsk.iae.post.poST.SingleResource;
import su.nsk.iae.post.poST.Task;

@SuppressWarnings("all")
public class ResourceGenerator {
  private final TaskGenerator taskGen = new TaskGenerator();

  private final ProgramConfGenerator programGen = new ProgramConfGenerator();

  public String generate(final Resource resource, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      EList<GlobalVarDeclaration> _resGlobVars = resource.getResGlobVars();
      for (final GlobalVarDeclaration g : _resGlobVars) {
        GlobalVarDeclarationGenerator.generate(g, ctx);
      }
      final SingleResource single = resource.getResStatement();
      EList<Task> _tasks = single.getTasks();
      for (final Task t : _tasks) {
        builder.append(
          this.taskGen.generate(t));
      }
      EList<ProgramConfiguration> _programConfs = single.getProgramConfs();
      for (final ProgramConfiguration pc : _programConfs) {
        builder.append(
          this.programGen.generate(pc, ctx));
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
}
