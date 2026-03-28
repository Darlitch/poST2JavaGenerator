package su.nsk.iae.post.generator.java.configuration

class SimulationClassGenerator {

    def String generate(
        String fields,
        String constructorBody,
        String programRunBody
    ) {

'''
import java.util.Map;
import java.util.HashMap;

public class Simulation {

    private final Map<String,Object> memory = new HashMap<>();
    private final Map<String, IProcess> processMap = new HashMap<>();
    private final long taskTimeMs;
«fields»

    public Simulation() {
        memory.put("_global_time", 0L);
«constructorBody»
    }

    public void step() {
«programRunBody»
    }

««««extraMethods»
}
'''
    }

}
