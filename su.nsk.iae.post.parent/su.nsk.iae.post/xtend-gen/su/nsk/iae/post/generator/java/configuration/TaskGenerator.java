package su.nsk.iae.post.generator.java.configuration;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.util.MemoryUtil;
import su.nsk.iae.post.poST.Constant;
import su.nsk.iae.post.poST.Task;
import su.nsk.iae.post.poST.TaskInitialization;

@SuppressWarnings("all")
public class TaskGenerator {
  public String generate(final Task task, final String indent) {
    String _xblockexpression = null;
    {
      final TaskInitialization init = task.getInit();
      Constant _interval = init.getInterval();
      boolean _tripleEquals = (_interval == null);
      if (_tripleEquals) {
        throw new IllegalStateException(
          "Only INTERVAL tasks are supported in simulator");
      }
      final String intervalMs = MemoryUtil.parseTime(init.getInterval().getTime());
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append(indent);
      _builder.append("long taskTimeMs = ");
      _builder.append(intervalMs);
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
}
