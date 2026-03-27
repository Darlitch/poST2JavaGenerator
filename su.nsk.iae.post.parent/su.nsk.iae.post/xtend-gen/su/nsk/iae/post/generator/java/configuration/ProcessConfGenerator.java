package su.nsk.iae.post.generator.java.configuration;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.java.common.context.GenerationContext;
import su.nsk.iae.post.generator.java.common.vars.BindingGenerator;
import su.nsk.iae.post.poST.AttachVariableConfElement;
import su.nsk.iae.post.poST.TemplateProcessAttachVariableConfElement;
import su.nsk.iae.post.poST.TemplateProcessConfElement;
import su.nsk.iae.post.poST.TemplateProcessElements;

@SuppressWarnings("all")
public class ProcessConfGenerator {
  public String generate(final TemplateProcessConfElement proc, final GenerationContext ctx, final String programInstance, final String programType, final String indent) {
    final StringBuilder builder = new StringBuilder();
    final String procName = proc.getName();
    final String procType = proc.getProcess().getName();
    ctx.registerProcess(procName, procName, procType);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append(indent);
    _builder.append("Map<String,String> ");
    _builder.append(procName);
    _builder.append("_aliases = new HashMap<>();");
    _builder.newLineIfNotEmpty();
    builder.append(_builder);
    TemplateProcessElements _args = proc.getArgs();
    boolean _tripleNotEquals = (_args != null);
    if (_tripleNotEquals) {
      EList<TemplateProcessAttachVariableConfElement> _elements = proc.getArgs().getElements();
      for (final TemplateProcessAttachVariableConfElement p : _elements) {
        BindingGenerator.generate(p, ctx);
      }
      EList<TemplateProcessAttachVariableConfElement> _elements_1 = proc.getArgs().getElements();
      for (final TemplateProcessAttachVariableConfElement p_1 : _elements_1) {
        if (((p_1 instanceof AttachVariableConfElement) || 
          (p_1 instanceof TemplateProcessAttachVariableConfElement))) {
          builder.append(
            BindingGenerator.generateAlias(p_1, ctx, procName, indent));
        }
      }
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append(indent);
    _builder_1.append(programType);
    _builder_1.append(".");
    _builder_1.append(procType);
    _builder_1.append(" ");
    _builder_1.append(procName);
    _builder_1.append(" = new ");
    _builder_1.append(programType);
    _builder_1.append(".");
    _builder_1.append(procType);
    _builder_1.append("(\"");
    _builder_1.append(procName);
    _builder_1.append("\", memory, ");
    _builder_1.append(procName);
    _builder_1.append("_aliases, processMap);");
    _builder_1.newLineIfNotEmpty();
    _builder_1.append(indent);
    _builder_1.append(programInstance);
    _builder_1.append(".registerProcess(");
    _builder_1.append(procName);
    _builder_1.append(");");
    _builder_1.newLineIfNotEmpty();
    builder.append(_builder_1);
    TemplateProcessElements _args_1 = proc.getArgs();
    boolean _tripleNotEquals_1 = (_args_1 != null);
    if (_tripleNotEquals_1) {
      EList<TemplateProcessAttachVariableConfElement> _elements_2 = proc.getArgs().getElements();
      for (final TemplateProcessAttachVariableConfElement p_2 : _elements_2) {
        if (((p_2 instanceof AttachVariableConfElement) || 
          (p_2 instanceof TemplateProcessAttachVariableConfElement))) {
          builder.append(
            BindingGenerator.generateProcessBinding(p_2, ctx, procName, indent));
        }
      }
    }
    boolean _isActive = proc.isActive();
    if (_isActive) {
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append(indent);
      _builder_2.append(procName);
      _builder_2.append(".start();");
      _builder_2.newLineIfNotEmpty();
      builder.append(_builder_2);
    }
    return builder.toString();
  }
}
