package su.nsk.iae.post.generator.java.configuration;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class SimulationClassGenerator {
  public String generate(final String fields, final String constructorBody, final String programRunBody) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.Map;");
    _builder.newLine();
    _builder.append("import java.util.HashMap;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public class Simulation {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private final Map<String,Object> memory = new HashMap<>();");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private final Map<String, IProcess> processMap = new HashMap<>();");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private final long taskTimeMs;");
    _builder.newLine();
    _builder.append(fields);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("public Simulation() {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("memory.put(\"_global_time\", 0L);");
    _builder.newLine();
    _builder.append(constructorBody);
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("public void step() {");
    _builder.newLine();
    _builder.append(programRunBody);
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
