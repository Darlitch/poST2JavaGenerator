import java.util.Map;
import java.util.HashMap;

public class Simulation {

    public static void main(String[] args) throws Exception {

        Map<String,Object> memory = new HashMap<>();

        Map<String, IProcess> processMap = new HashMap<>();

        memory.put("_global_time", 0L);
        memory.put("red1", false);
        memory.put("yellow1", false);
        memory.put("green1", false);
        memory.put("red2", false);
        memory.put("yellow2", false);
        memory.put("green2", false);
        memory.put("sensor", false);
        memory.put("NUMBER_OF_LIGHTS", 3);
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

        long taskTimeMs = 1000L;
        Controller traffic_lights_controller = new Controller(memory, processMap);

        Map<String,String> red_light1_aliases = new HashMap<>();
        red_light1_aliases.put("b_light", "red1");
        Light red_light1 = new Light("red_light1", memory, red_light1_aliases, processMap);
        traffic_lights_controller.registerProcess(red_light1);
        red_light1.start();

        Map<String,String> yellow_light1_aliases = new HashMap<>();
        yellow_light1_aliases.put("b_light", "yellow1");
        Light yellow_light1 = new Light("yellow_light1", memory, yellow_light1_aliases, processMap);
        traffic_lights_controller.registerProcess(yellow_light1);

        Map<String,String> green_light1_aliases = new HashMap<>();
        green_light1_aliases.put("b_light", "green1");
        Light green_light1 = new Light("green_light1", memory, green_light1_aliases, processMap);
        traffic_lights_controller.registerProcess(green_light1);

        Map<String,String> red_light2_aliases = new HashMap<>();
        red_light2_aliases.put("b_light", "red2");
        Light red_light2 = new Light("red_light2", memory, red_light2_aliases, processMap);
        traffic_lights_controller.registerProcess(red_light2);

        Map<String,String> yellow_light2_aliases = new HashMap<>();
        yellow_light2_aliases.put("b_light", "yellow2");
        Light yellow_light2 = new Light("yellow_light2", memory, yellow_light2_aliases, processMap);
        traffic_lights_controller.registerProcess(yellow_light2);

        Map<String,String> green_light2_aliases = new HashMap<>();
        green_light2_aliases.put("b_light", "green2");
        Light green_light2 = new Light("green_light2", memory, green_light2_aliases, processMap);
        traffic_lights_controller.registerProcess(green_light2);
        green_light2.start();

        Map<String,String> control1_aliases = new HashMap<>();
        control1_aliases.put("control_sensor", "sensor");
        control1_aliases.put("rLightsArray", "lightsArray1");
        Control control1 = new Control("control1", memory, control1_aliases, processMap);
        traffic_lights_controller.registerProcess(control1);
        control1.setProcess("pRed", red_light1);
        control1.setProcess("pYellow", yellow_light1);
        control1.setProcess("pGreen", green_light1);
        control1.start();

        Map<String,String> control2_aliases = new HashMap<>();
        control2_aliases.put("control_sensor", "sensor");
        control2_aliases.put("rLightsArray", "lightsArray2");
        Control control2 = new Control("control2", memory, control2_aliases, processMap);
        traffic_lights_controller.registerProcess(control2);
        control2.setProcess("pRed", green_light2);
        control2.setProcess("pYellow", yellow_light2);
        control2.setProcess("pGreen", red_light2);
        control2.start();

        while (true) {

            traffic_lights_controller.runIter(taskTimeMs);

            Thread.sleep(taskTimeMs);
        }
    }

}
