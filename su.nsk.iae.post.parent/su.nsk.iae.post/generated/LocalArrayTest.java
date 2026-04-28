import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;

public class LocalArrayTest {

    private final Map<String,Object> memory;
    private final List<IProcess> processes = new ArrayList<>();
    private final Map<String, IProcess> processMap;
    private final Set<String> inputNames = new HashSet<>();
    private final Set<String> outputNames = new HashSet<>();
    private final Set<String> globalNames = new HashSet<>();

    private void registerTo(Set<String> target, String name) {
        Object value = memory.get(name);

        if (value instanceof List) {
            List<String> list = (List<String>) value;
            for (String cell : list) {
                target.add(cell);
            }
        } else {
            target.add(name);
        }
    }

    public LocalArrayTest(Map<String,Object> memory, Map<String, IProcess> processMap) {
        this.memory = memory;
        this.processMap = processMap;



    }

    public void runIter(long cycleTimeMs) {
	
        memory.put(
            "_global_time",
            ((Long)memory.get("_global_time")) + cycleTimeMs
        );

        for (IProcess p : processes)
            p.run();
    }

    public void registerProcess(IProcess p) {
        processes.add(p);
        processMap.put(((BaseProcess)p).instanceName, p);
    }

    public Map<String,String> dumpProcessStates() {

        Map<String,String> res = new HashMap<>();

        for (IProcess p : processes)
            p.dumpStates(res);

        return res;
    }

    public Map<String,Long> dumpProcessTimers() {

        Map<String,Long> res = new HashMap<>();

        for (IProcess p : processes)
            p.dumpTimers(res);

        return res;
    }

    public Map<String,Object> dumpInputs() {

        Map<String,Object> res = new HashMap<>();

        for (String n : inputNames)
            res.put(n, memory.get(n));

        return res;
    }

    public void updateInputs(Map<String,Object> values) {

        if (values == null || values.isEmpty())
            return;

        for (Map.Entry<String,Object> e : values.entrySet()) {
            String n = e.getKey();

            if (inputNames.contains(n))
                memory.put(n, e.getValue());
        }
    }

    public Map<String,Object> dumpOutputs() {

        Map<String,Object> res = new HashMap<>();

        for (String n : outputNames)
            res.put(n, memory.get(n));

        return res;
    }

    public Map<String,Object> dumpGlobals() {

        Map<String,Object> res = new HashMap<>();

        for (String n : globalNames)
            res.put(n, memory.get(n));

        return res;
    }

    public Map<String,Object> dumpVars() {

        Map<String,Object> res = new HashMap<>();

        for (IProcess p : processes)
            p.dumpLocalVars(res);

        return res;
    }

    static class WorkerProcess extends BaseProcess {
	
        enum State {
            Init,
            Work,
            Stop,
            Error
        }

        public WorkerProcess(String instanceName, Map<String,Object> memory, Map<String,String> aliases, Map<String, IProcess> globalProcesses) {
            super(instanceName, memory, aliases, globalProcesses);

            localMemory.put("i", 0);
            localMemory.put("flags_1", false);
            localMemory.put("flags_2", false);
            localMemory.put("flags_3", false);
            localMemory.put(
                "flags",
                new java.util.ArrayList<String>(
                    java.util.List.of("flags_1", "flags_2", "flags_3")
                )
            );
            localMemory.put("nums_1", 0);
            localMemory.put("nums_2", 0);
            localMemory.put("nums_3", 0);
            localMemory.put(
                "nums",
                new java.util.ArrayList<String>(
                    java.util.List.of("nums_1", "nums_2", "nums_3")
                )
            );
        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.Init;
            timerBaseTime = ((Long)memory.get("_global_time"));
        }

        public void stop() {
            state = State.Stop;
            timerBaseTime = ((Long)memory.get("_global_time"));
        }

        public void error() {
            state = State.Error;
            timerBaseTime = ((Long)memory.get("_global_time"));
        }

        public void setState(State s) {
            state = s;
            timerBaseTime = ((Long)memory.get("_global_time"));
        }

        public void setNext() {

            switch(state) {
                case Init -> state = State.Work;
                case Work -> state = State.Init;
                default -> { }
            }

            timerBaseTime = ((Long)memory.get("_global_time"));
        }

        public State getState() {
            return state;
        }

        @Override
        public void run() {

            switch(state) {
                case Init -> {
                    int __start = ((Number)(1)).intValue();
                    int __end   = ((Number)(3)).intValue();
                    int __step  = ((Number)(1)).intValue();

                    if (__step == 0)
                        throw new RuntimeException("FOR step cannot be zero");

                    writeVar("i", __start);

                    while (loopCond("i", __end, __step)) {
                        setArrayValue("flags", readInt("i"), 1, false);
                        setArrayValue("nums", readInt("i"), 1, readInt("i"));
                        writeVar("i", readInt("i") + __step);
                    }
                    setState(State.Work);
                }
                case Work -> {
                    setArrayValue("flags", 2, 1, true);
                    if (getArrayBool("flags", 2, 1)) {
                        setArrayValue("nums", 3, 1, (getArrayInt("nums", 1, 1) + getArrayInt("nums", 2, 1)));
                    }
                }
                case Stop, Error -> { }
            }
        }

        @Override
        public void dumpStates(Map<String,String> out) {
            out.put(instanceName + "_state", state.name());
        }

        @Override
        public void dumpTimers(Map<String,Long> out) {
            out.put(instanceName + "_time", timerBaseTime);
        }

        @Override
        public String getStateName() {
            return state.name();
        }

    }

}
