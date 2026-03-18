package su.nsk.iae.post.generator.java.configuration;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.vars.BindingGenerator;
import su.nsk.iae.post.poST.AttachVariableConfElement;
import su.nsk.iae.post.poST.ProgramConfElement;
import su.nsk.iae.post.poST.ProgramConfElements;
import su.nsk.iae.post.poST.ProgramConfiguration;

@SuppressWarnings("all")
public class ProgramConfGenerator {
  public String generate(final ProgramConfiguration conf, final GenerationContext ctx, final String indent) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      final String instanceName = conf.getName();
      final String programType = conf.getProgram().getName();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(indent);
      _builder.append(programType);
      _builder.append(" ");
      _builder.append(instanceName);
      _builder.append(" = new ");
      _builder.append(programType);
      _builder.append("(memory);");
      _builder.newLineIfNotEmpty();
      builder.append(_builder);
      ProgramConfElements _args = conf.getArgs();
      boolean _tripleNotEquals = (_args != null);
      if (_tripleNotEquals) {
        EList<ProgramConfElement> _elements = conf.getArgs().getElements();
        for (final ProgramConfElement arg : _elements) {
          if ((arg instanceof AttachVariableConfElement)) {
            BindingGenerator.generate(((AttachVariableConfElement)arg), ctx);
          }
        }
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
}
