package su.nsk.iae.post.generator.java.common;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class IProcessGenerator {
  public static String generate() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public interface IProcess {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void run();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void dumpStates(java.util.Map<String,String> out);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void dumpTimers(java.util.Map<String,Long> out);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
