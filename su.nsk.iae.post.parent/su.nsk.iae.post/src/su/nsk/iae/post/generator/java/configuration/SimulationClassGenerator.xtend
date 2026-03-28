package su.nsk.iae.post.generator.java.configuration

class SimulationClassGenerator {

    def String generate(
        String fields,
        String constructorBody,
        String programInstanceName
    ) {
    	val IND = "    "

'''
import java.util.Map;
import java.util.HashMap;

public class Simulation implements ISimulationRuntime {

«IND»private final Map<String,Object> memory = new HashMap<>();
«IND»private final Map<String, IProcess> processMap = new HashMap<>();
«IND»private final long taskTimeMs;
«fields»

«generateConstructor(constructorBody, IND)»

«generateStep(programInstanceName, IND)»

«generateUpdateInputs(programInstanceName, IND)»

«generateDumpInputs(programInstanceName, IND)»

«generateDumpOutputs(programInstanceName, IND)»

«generateDumpGlobals(programInstanceName, IND)»

«generateDumpVars(programInstanceName, IND)»

«generateDumpStates(programInstanceName, IND)»

«generateDumpTimers(programInstanceName, IND)»

}
'''
    }
    
    private def String generateConstructor(String constructorBody, String indent) '''
«indent»public Simulation() {
«indent»	memory.put("_global_time", 0L);
«constructorBody»
«indent»}
'''
    
private def String generateStep(String programInstanceName, String indent) '''
«indent»@Override
«indent»public void step() {
«indent»	«programInstanceName».runIter(taskTimeMs);
«indent»}
'''

    private def String generateUpdateInputs(String programInstanceName, String indent) '''
«indent»@Override
«indent»public void updateInputs(Map<String, Object> values) {
«indent»	«programInstanceName».updateInputs(values);
«indent»}
'''

    private def String generateDumpInputs(String programInstanceName, String indent) '''
«indent»@Override
«indent»public Map<String,Object> dumpInputs() {
«indent»	Map<String,Object> res = new HashMap<>();
«indent»	res.putAll(«programInstanceName».dumpInputs());
«indent»	return res;
«indent»}
'''

    private def String generateDumpOutputs(String programInstanceName, String indent) '''
«indent»@Override
«indent»public Map<String,Object> dumpOutputs() {
«indent»	Map<String,Object> res = new HashMap<>();
«indent»	res.putAll(«programInstanceName».dumpOutputs());
«indent»	return res;
«indent»}
'''

    private def String generateDumpGlobals(String programInstanceName, String indent) '''
«indent»@Override
«indent»public Map<String,Object> dumpGlobals() {
«indent»	Map<String,Object> res = new HashMap<>();
«indent»	res.putAll(«programInstanceName».dumpGlobals());
«indent»	return res;
«indent»}
'''

    private def String generateDumpVars(String programInstanceName, String indent) '''
«indent»@Override
«indent»public Map<String,Object> dumpVars() {
«indent»	Map<String,Object> res = new HashMap<>();
«indent»	res.putAll(«programInstanceName».dumpVars());
«indent»	return res;
«indent»}
'''

    private def String generateDumpStates(String programInstanceName, String indent) '''
«indent»@Override
«indent»public Map<String,String> dumpProcessStates() {
«indent»	Map<String,String> res = new HashMap<>();
«indent»	res.putAll(«programInstanceName».dumpProcessStates());
«indent»	return res;
«indent»}
'''

    private def String generateDumpTimers(String programInstanceName, String indent) '''
«indent»@Override
«indent»public Map<String,Long> dumpProcessTimers() {
«indent»	Map<String,Long> res = new HashMap<>();
«indent»	res.putAll(«programInstanceName».dumpProcessTimers());
«indent»	return res;
«indent»}
'''
}

