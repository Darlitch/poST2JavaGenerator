public class Traffic_lightsSimulation {

    public static void main(String[] args) throws Exception {

long taskTimeMs = 1000L;
Controller traffic_lights_controller = new Controller();
while (true) {

    traffic_lights_controller.runIter(taskTimeMs);

    Thread.sleep(taskTimeMs);
}
    }

}
