import java.util.Map;
import java.util.HashMap;

public class Traffic_lightsSimulation {

    public static void main(String[] args) throws Exception {

        Map<String,Object> memory = new HashMap<>();

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
        Controller traffic_lights_controller = new Controller(memory);

        while (true) {

            traffic_lights_controller.runIter(taskTimeMs);

            Thread.sleep(taskTimeMs);
        }
    }

}
