package su.nsk.iae.post.generator.java.configuration;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.vars.BindingGenerator;
import su.nsk.iae.post.poST.AttachVariableConfElement;
import su.nsk.iae.post.poST.ProgramConfElement;
import su.nsk.iae.post.poST.ProgramConfElements;
import su.nsk.iae.post.poST.ProgramConfiguration;
import su.nsk.iae.post.poST.TemplateProcessAttachVariableConfElement;
import su.nsk.iae.post.poST.TemplateProcessConfElement;
import su.nsk.iae.post.poST.TemplateProcessElements;

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
          if ((arg instanceof TemplateProcessConfElement)) {
            final TemplateProcessConfElement proc = ((TemplateProcessConfElement)arg);
            final String procName = proc.getName();
            final String procType = proc.getProcess().getName();
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.newLine();
            _builder_1.append(indent);
            _builder_1.append("Map<String,String> ");
            _builder_1.append(procName);
            _builder_1.append("_aliases = new HashMap<>();");
            _builder_1.newLineIfNotEmpty();
            builder.append(_builder_1);
            TemplateProcessElements _args_1 = proc.getArgs();
            boolean _tripleNotEquals_1 = (_args_1 != null);
            if (_tripleNotEquals_1) {
              EList<TemplateProcessAttachVariableConfElement> _elements_1 = proc.getArgs().getElements();
              for (final TemplateProcessAttachVariableConfElement p : _elements_1) {
                if (((p instanceof AttachVariableConfElement) || 
                  (p instanceof TemplateProcessAttachVariableConfElement))) {
                  builder.append(
                    BindingGenerator.generateAlias(p, ctx, procName, indent));
                }
              }
            }
            StringConcatenation _builder_2 = new StringConcatenation();
            _builder_2.append(indent);
            _builder_2.append(procType);
            _builder_2.append(" ");
            _builder_2.append(procName);
            _builder_2.append(" = new ");
            _builder_2.append(procType);
            _builder_2.append("(memory, ");
            _builder_2.append(procName);
            _builder_2.append("_aliases);");
            _builder_2.newLineIfNotEmpty();
            _builder_2.append(indent);
            _builder_2.append(instanceName);
            _builder_2.append(".registerProcess(");
            _builder_2.append(procName);
            _builder_2.append(");");
            _builder_2.newLineIfNotEmpty();
            builder.append(_builder_2);
            boolean _isActive = proc.isActive();
            if (_isActive) {
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
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
}
