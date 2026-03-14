package su.nsk.iae.post.generator.java.configuration;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.vars.BindingGenerator;
import su.nsk.iae.post.poST.TemplateProcessAttachVariableConfElement;
import su.nsk.iae.post.poST.TemplateProcessConfElement;
import su.nsk.iae.post.poST.TemplateProcessElements;

@SuppressWarnings("all")
public class ProcessConfGenerator {
  public String generate(final TemplateProcessConfElement conf, final GenerationContext ctx) {
    String _xblockexpression = null;
    {
      final StringBuilder builder = new StringBuilder();
      final String name = conf.getName();
      final String type = conf.getProcess().getName();
      final String fieldName = name;
      ctx.registerProcess(name, fieldName, type);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(type);
      _builder.append(" ");
      _builder.append(fieldName);
      _builder.append(" = new ");
      _builder.append(type);
      _builder.append("(memory);");
      _builder.newLineIfNotEmpty();
      _builder.append("processes.add(");
      _builder.append(fieldName);
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      builder.append(_builder);
      TemplateProcessElements _args = conf.getArgs();
      boolean _tripleNotEquals = (_args != null);
      if (_tripleNotEquals) {
        EList<TemplateProcessAttachVariableConfElement> _elements = conf.getArgs().getElements();
        for (final TemplateProcessAttachVariableConfElement arg : _elements) {
          BindingGenerator.generate(arg, ctx);
        }
      }
      boolean _isActive = conf.isActive();
      if (_isActive) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append(fieldName);
        _builder_1.append(".start();");
        _builder_1.newLineIfNotEmpty();
        builder.append(_builder_1);
      }
      _xblockexpression = builder.toString();
    }
    return _xblockexpression;
  }
}
