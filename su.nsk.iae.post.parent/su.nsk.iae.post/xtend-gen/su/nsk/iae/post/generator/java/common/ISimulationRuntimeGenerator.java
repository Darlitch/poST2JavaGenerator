package su.nsk.iae.post.generator.java.common;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class ISimulationRuntimeGenerator {
  public static String generate() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.Map;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public interface ISimulationRuntime {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void step();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("   \t");
    _builder.append("void updateInputs(Map<String, Object> values);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("Map<String,Object> dumpInputs();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("Map<String,Object> dumpOutputs();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("Map<String,Object> dumpGlobals();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("Map<String,Object> dumpVars();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("Map<String,String> dumpProcessStates();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("Map<String,Long> dumpProcessTimers();");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
