package su.nsk.iae.post.generator.java.common;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class IProcessGenerator {
  public static String generate() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.Map;");
    _builder.newLine();
    _builder.newLine();
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
    _builder.append("    ");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void dumpLocalVars(Map<String,Object> out);");
    _builder.newLine();
    _builder.append("    ");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("String getStateName();");
    _builder.newLine();
    _builder.append("    ");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void start();");
    _builder.newLine();
    _builder.append("    ");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void stop();");
    _builder.newLine();
    _builder.append("    ");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void error();");
    _builder.newLine();
    _builder.append("    ");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void setNext();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
