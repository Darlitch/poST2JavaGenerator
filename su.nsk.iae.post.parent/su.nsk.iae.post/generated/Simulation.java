import java.util.Map;
import java.util.HashMap;

public class Simulation {

    public static void main(String[] args) throws Exception {

        Map<String,Object> memory = new HashMap<>();
        
        Map<String, IProcess> processMap = new HashMap<>();
        
        memory.put("_global_time", 0L);
        memory.put("onfloor0", false);
        memory.put("onfloor1", false);
        memory.put("onfloor2", false);
        memory.put("call0", false);
        memory.put("call1", false);
        memory.put("call2", false);
        memory.put("button0", false);
        memory.put("button1", false);
        memory.put("button2", false);
        memory.put("door0closed", false);
        memory.put("door1closed", false);
        memory.put("door2closed", false);
        memory.put("up", false);
        memory.put("down", false);
        memory.put("open0", false);
        memory.put("open1", false);
        memory.put("open2", false);
        memory.put("call0_LED", false);
        memory.put("call1_LED", false);
        memory.put("call2_LED", false);
        memory.put("button0_LED", false);
        memory.put("button1_LED", false);
        memory.put("button2_LED", false);
        memory.put("floor0_LED", false);
        memory.put("floor1_LED", false);
        memory.put("floor2_LED", false);
        memory.put("cur", 0);
        memory.put("target", 0);

        Controller controller = new Controller(memory, processMap);

        Map<String,String> init_aliases = new HashMap<>();
        Init init = new Init("init", memory, init_aliases, processMap);
        controller.registerProcess(init);
        init.start();

        Map<String,String> call0Latch_aliases = new HashMap<>();
        Call0Latch call0Latch = new Call0Latch("call0Latch", memory, call0Latch_aliases, processMap);
        controller.registerProcess(call0Latch);

        Map<String,String> call1Latch_aliases = new HashMap<>();
        Call1Latch call1Latch = new Call1Latch("call1Latch", memory, call1Latch_aliases, processMap);
        controller.registerProcess(call1Latch);

        Map<String,String> call2Latch_aliases = new HashMap<>();
        Call2Latch call2Latch = new Call2Latch("call2Latch", memory, call2Latch_aliases, processMap);
        controller.registerProcess(call2Latch);

        Map<String,String> button0Latch_aliases = new HashMap<>();
        Button0Latch button0Latch = new Button0Latch("button0Latch", memory, button0Latch_aliases, processMap);
        controller.registerProcess(button0Latch);

        Map<String,String> button1Latch_aliases = new HashMap<>();
        Button1Latch button1Latch = new Button1Latch("button1Latch", memory, button1Latch_aliases, processMap);
        controller.registerProcess(button1Latch);

        Map<String,String> button2Latch_aliases = new HashMap<>();
        Button2Latch button2Latch = new Button2Latch("button2Latch", memory, button2Latch_aliases, processMap);
        controller.registerProcess(button2Latch);

        Map<String,String> checkCurFloor_aliases = new HashMap<>();
        CheckCurFloor checkCurFloor = new CheckCurFloor("checkCurFloor", memory, checkCurFloor_aliases, processMap);
        controller.registerProcess(checkCurFloor);

        Map<String,String> upControl_aliases = new HashMap<>();
        UpControl upControl = new UpControl("upControl", memory, upControl_aliases, processMap);
        controller.registerProcess(upControl);

        Map<String,String> upMotion_aliases = new HashMap<>();
        UpMotion upMotion = new UpMotion("upMotion", memory, upMotion_aliases, processMap);
        controller.registerProcess(upMotion);

        Map<String,String> downControl_aliases = new HashMap<>();
        DownControl downControl = new DownControl("downControl", memory, downControl_aliases, processMap);
        controller.registerProcess(downControl);

        Map<String,String> downMotion_aliases = new HashMap<>();
        DownMotion downMotion = new DownMotion("downMotion", memory, downMotion_aliases, processMap);
        controller.registerProcess(downMotion);

        Map<String,String> doorCycle_aliases = new HashMap<>();
        DoorCycle doorCycle = new DoorCycle("doorCycle", memory, doorCycle_aliases, processMap);
        controller.registerProcess(doorCycle);

        long taskTimeMs = 100L;

        while (true) {
            controller.runIter(taskTimeMs);
            Thread.sleep(taskTimeMs);
        }
    }
}
