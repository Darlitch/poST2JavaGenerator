import java.util.Map;
import java.util.HashMap;

import runtime.ISimulationRuntime;

public class Simulation implements ISimulationRuntime {

    private final Map<String,Object> memory = new HashMap<>();
    private final Map<String, IProcess> processMap = new HashMap<>();
    private final long taskTimeMs;
    private final Controller traffic_lights_controller;

    public Simulation() {
    	memory.put("_global_time", 0L);
        memory.put("red1", false);
        memory.put("yellow1", false);
        memory.put("green1", false);
        memory.put("red2", false);
        memory.put("yellow2", false);
        memory.put("green2", false);
        memory.put("sensor", false);
        memory.put("NUMBER_OF_LIGHTS", 2);
        memory.put(
            "lightsArray1",
            new java.util.ArrayList<String>(
                java.util.List.of(
                        "red1", 
                        "yellow1", 
                        "green1"
                )
            )
        );
        memory.put(
            "lightsArray2",
            new java.util.ArrayList<String>(
                java.util.List.of(
                        "green2", 
                        "yellow2", 
                        "red2"
                )
            )
        );

        this.taskTimeMs = 1000L;
        this.traffic_lights_controller = new Controller(memory, processMap);

        Map<String,String> proc_red_light1_aliases = new HashMap<>();
        proc_red_light1_aliases.put("b_light", "red1");
        Controller.LightProcess proc_red_light1 = new Controller.LightProcess("proc_red_light1", memory, proc_red_light1_aliases, processMap);
        traffic_lights_controller.registerProcess(proc_red_light1);
        proc_red_light1.start();

        Map<String,String> proc_yellow_light1_aliases = new HashMap<>();
        proc_yellow_light1_aliases.put("b_light", "yellow1");
        Controller.LightProcess proc_yellow_light1 = new Controller.LightProcess("proc_yellow_light1", memory, proc_yellow_light1_aliases, processMap);
        traffic_lights_controller.registerProcess(proc_yellow_light1);

        Map<String,String> proc_green_light1_aliases = new HashMap<>();
        proc_green_light1_aliases.put("b_light", "green1");
        Controller.LightProcess proc_green_light1 = new Controller.LightProcess("proc_green_light1", memory, proc_green_light1_aliases, processMap);
        traffic_lights_controller.registerProcess(proc_green_light1);

        Map<String,String> proc_red_light2_aliases = new HashMap<>();
        proc_red_light2_aliases.put("b_light", "red2");
        Controller.LightProcess proc_red_light2 = new Controller.LightProcess("proc_red_light2", memory, proc_red_light2_aliases, processMap);
        traffic_lights_controller.registerProcess(proc_red_light2);

        Map<String,String> proc_yellow_light2_aliases = new HashMap<>();
        proc_yellow_light2_aliases.put("b_light", "yellow2");
        Controller.LightProcess proc_yellow_light2 = new Controller.LightProcess("proc_yellow_light2", memory, proc_yellow_light2_aliases, processMap);
        traffic_lights_controller.registerProcess(proc_yellow_light2);

        Map<String,String> proc_green_light2_aliases = new HashMap<>();
        proc_green_light2_aliases.put("b_light", "green2");
        Controller.LightProcess proc_green_light2 = new Controller.LightProcess("proc_green_light2", memory, proc_green_light2_aliases, processMap);
        traffic_lights_controller.registerProcess(proc_green_light2);
        proc_green_light2.start();

        Map<String,String> proc_control1_aliases = new HashMap<>();
        proc_control1_aliases.put("control_sensor", "sensor");
        proc_control1_aliases.put("rLightsArray", "lightsArray1");
        Controller.ControlProcess proc_control1 = new Controller.ControlProcess("proc_control1", memory, proc_control1_aliases, processMap);
        traffic_lights_controller.registerProcess(proc_control1);
        proc_control1.setProcess("pRed", proc_red_light1);
        proc_control1.setProcess("pYellow", proc_yellow_light1);
        proc_control1.setProcess("pGreen", proc_green_light1);
        proc_control1.start();

        Map<String,String> proc_control2_aliases = new HashMap<>();
        proc_control2_aliases.put("control_sensor", "sensor");
        proc_control2_aliases.put("rLightsArray", "lightsArray2");
        Controller.ControlProcess proc_control2 = new Controller.ControlProcess("proc_control2", memory, proc_control2_aliases, processMap);
        traffic_lights_controller.registerProcess(proc_control2);
        proc_control2.setProcess("pRed", proc_green_light2);
        proc_control2.setProcess("pYellow", proc_yellow_light2);
        proc_control2.setProcess("pGreen", proc_red_light2);
        proc_control2.start();
    }

    @Override
    public void step() {
    	traffic_lights_controller.runIter(taskTimeMs);
    }

    @Override
    public void updateInputs(Map<String, Object> values) {
    	traffic_lights_controller.updateInputs(values);
    }

    @Override
    public Map<String,Object> dumpInputs() {
    	Map<String,Object> res = new HashMap<>();
    	res.putAll(traffic_lights_controller.dumpInputs());
    	return res;
    }

    @Override
    public Map<String,Object> dumpOutputs() {
    	Map<String,Object> res = new HashMap<>();
    	res.putAll(traffic_lights_controller.dumpOutputs());
    	return res;
    }

    @Override
    public Map<String,Object> dumpGlobals() {
    	Map<String,Object> res = new HashMap<>();
    	res.putAll(traffic_lights_controller.dumpGlobals());
    	return res;
    }

    @Override
    public Map<String,Object> dumpVars() {
    	Map<String,Object> res = new HashMap<>();
    	res.putAll(traffic_lights_controller.dumpVars());
    	return res;
    }

    @Override
    public Map<String,String> dumpProcessStates() {
    	Map<String,String> res = new HashMap<>();
    	res.putAll(traffic_lights_controller.dumpProcessStates());
    	return res;
    }

    @Override
    public Map<String,Long> dumpProcessTimers() {
    	Map<String,Long> res = new HashMap<>();
    	res.putAll(traffic_lights_controller.dumpProcessTimers());
    	return res;
    }

}
