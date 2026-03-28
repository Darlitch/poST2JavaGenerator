package su.nsk.iae.post.generator.java.configuration;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class SimulationClassGenerator {
  public String generate(final String fields, final String constructorBody, final String programInstanceName) {
    String _xblockexpression = null;
    {
      final String IND = "    ";
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("import java.util.Map;");
      _builder.newLine();
      _builder.append("import java.util.HashMap;");
      _builder.newLine();
      _builder.newLine();
      _builder.append("public class Simulation implements ISimulationRuntime {");
      _builder.newLine();
      _builder.newLine();
      _builder.append(IND);
      _builder.append("private final Map<String,Object> memory = new HashMap<>();");
      _builder.newLineIfNotEmpty();
      _builder.append(IND);
      _builder.append("private final Map<String, IProcess> processMap = new HashMap<>();");
      _builder.newLineIfNotEmpty();
      _builder.append(IND);
      _builder.append("private final long taskTimeMs;");
      _builder.newLineIfNotEmpty();
      _builder.append(fields);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateConstructor = this.generateConstructor(constructorBody, IND);
      _builder.append(_generateConstructor);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateStep = this.generateStep(programInstanceName, IND);
      _builder.append(_generateStep);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateUpdateInputs = this.generateUpdateInputs(programInstanceName, IND);
      _builder.append(_generateUpdateInputs);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateDumpInputs = this.generateDumpInputs(programInstanceName, IND);
      _builder.append(_generateDumpInputs);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateDumpOutputs = this.generateDumpOutputs(programInstanceName, IND);
      _builder.append(_generateDumpOutputs);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateDumpGlobals = this.generateDumpGlobals(programInstanceName, IND);
      _builder.append(_generateDumpGlobals);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateDumpVars = this.generateDumpVars(programInstanceName, IND);
      _builder.append(_generateDumpVars);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateDumpStates = this.generateDumpStates(programInstanceName, IND);
      _builder.append(_generateDumpStates);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _generateDumpTimers = this.generateDumpTimers(programInstanceName, IND);
      _builder.append(_generateDumpTimers);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }

  private String generateConstructor(final String constructorBody, final String indent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("public Simulation() {");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\tmemory.put(\"_global_time\", 0L);");
    _builder.newLineIfNotEmpty();
    _builder.append(constructorBody);
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateStep(final String programInstanceName, final String indent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("@Override");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("public void step() {");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\t");
    _builder.append(programInstanceName);
    _builder.append(".runIter(taskTimeMs);");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateUpdateInputs(final String programInstanceName, final String indent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("@Override");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("public void updateInputs(Map<String, Object> values) {");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\t");
    _builder.append(programInstanceName);
    _builder.append(".updateInputs(values);");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateDumpInputs(final String programInstanceName, final String indent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("@Override");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("public Map<String,Object> dumpInputs() {");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\tMap<String,Object> res = new HashMap<>();");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\tres.putAll(");
    _builder.append(programInstanceName);
    _builder.append(".dumpInputs());");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\treturn res;");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateDumpOutputs(final String programInstanceName, final String indent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("@Override");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("public Map<String,Object> dumpOutputs() {");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\tMap<String,Object> res = new HashMap<>();");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\tres.putAll(");
    _builder.append(programInstanceName);
    _builder.append(".dumpOutputs());");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\treturn res;");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateDumpGlobals(final String programInstanceName, final String indent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("@Override");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("public Map<String,Object> dumpGlobals() {");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\tMap<String,Object> res = new HashMap<>();");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\tres.putAll(");
    _builder.append(programInstanceName);
    _builder.append(".dumpGlobals());");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\treturn res;");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateDumpVars(final String programInstanceName, final String indent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("@Override");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("public Map<String,Object> dumpVars() {");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\tMap<String,Object> res = new HashMap<>();");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\tres.putAll(");
    _builder.append(programInstanceName);
    _builder.append(".dumpVars());");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\treturn res;");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateDumpStates(final String programInstanceName, final String indent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("@Override");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("public Map<String,String> dumpProcessStates() {");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\tMap<String,String> res = new HashMap<>();");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\tres.putAll(");
    _builder.append(programInstanceName);
    _builder.append(".dumpProcessStates());");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\treturn res;");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  private String generateDumpTimers(final String programInstanceName, final String indent) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(indent);
    _builder.append("@Override");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("public Map<String,Long> dumpProcessTimers() {");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\tMap<String,Long> res = new HashMap<>();");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\tres.putAll(");
    _builder.append(programInstanceName);
    _builder.append(".dumpProcessTimers());");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("\treturn res;");
    _builder.newLineIfNotEmpty();
    _builder.append(indent);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
}
