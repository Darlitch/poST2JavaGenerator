import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Controller {

    private final Map<String,Object> memory;
    private final List<IProcess> processes = new ArrayList<>();
    private final Set<String> inputNames = new HashSet<>();
    private final Set<String> outputNames = new HashSet<>();
    private final Set<String> globalNames = new HashSet<>();
    private final Set<String> varNames = new HashSet<>();

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


    public Controller(Map<String,Object> memory) {
        this.memory = memory;
        memory.put("prev_light", 0);
        memory.put("alight", 0);
        memory.put("pressed", false);

        registerTo(inputNames, "lightsArray1");
        registerTo(inputNames, "lightsArray2");
        registerTo(inputNames, "sensor");

        registerTo(outputNames, "red2");
        registerTo(outputNames, "red1");
        registerTo(outputNames, "green2");
        registerTo(outputNames, "green1");
        registerTo(outputNames, "yellow1");
        registerTo(outputNames, "yellow2");

        registerTo(globalNames, "lightsArray1");
        registerTo(globalNames, "lightsArray2");
        registerTo(globalNames, "red2");
        registerTo(globalNames, "red1");
        registerTo(globalNames, "green2");
        registerTo(globalNames, "green1");
        registerTo(globalNames, "sensor");
        registerTo(globalNames, "NUMBER_OF_LIGHTS");
        registerTo(globalNames, "yellow1");
        registerTo(globalNames, "yellow2");
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

        for (String n : varNames)
            res.put(n, memory.get(n));

        return res;
    }

    class Light implements IProcess {

        enum State {
            Light,
            Stop,
            Error
        }

        private final Map<String,Object> memory;

        private final Map<String,IProcess> processRefs = new HashMap<>();

        private final Map<String,String> aliases;

        public Light(Map<String,Object> memory, Map<String,String> aliases) {
            this.memory = memory;
        this.aliases = aliases;
        }

        public void setProcess(String name, IProcess p) {
            processRefs.put(name, p);
        }

        private String resolve(String name) {
            String current = name;
            if (aliases != null) {
                while (aliases.containsKey(current)) {
                    current = aliases.get(current);
                }
            }
            return current;
        }

        private Object read(String name) {
            return memory.get(resolve(name));
        }

        private void write(String name, Object value) {
            memory.put(resolve(name), value);
        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.Light;
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
                case Light -> state = State.Light;
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
                case Light -> {
                    write("b_light", true);
                }
                case Stop, Error -> { }
            }
        }

        @Override
        public void dumpStates(Map<String,String> out) {
            out.put("Light_state", state.name());
        }

        @Override
        public void dumpTimers(Map<String,Long> out) {
            out.put("Light_time", timerBaseTime);
        }

        @Override
        public String getStateName() {
            return state.name();
        }

    }
    class Control implements IProcess {

        enum State {
            Work,
            delay10,
            delay30,
            Stop,
            Error
        }

        private final Map<String,Object> memory;

        private final Map<String,IProcess> processRefs = new HashMap<>();

        private final Map<String,String> aliases;

        public Control(Map<String,Object> memory, Map<String,String> aliases) {
            this.memory = memory;
        this.aliases = aliases;
        }

        public void setProcess(String name, IProcess p) {
            processRefs.put(name, p);
        }

        private String resolve(String name) {
            String current = name;
            if (aliases != null) {
                while (aliases.containsKey(current)) {
                    current = aliases.get(current);
                }
            }
            return current;
        }

        private Object read(String name) {
            return memory.get(resolve(name));
        }

        private void write(String name, Object value) {
            memory.put(resolve(name), value);
        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.Work;
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
                case Work -> state = State.delay10;
                case delay10 -> state = State.delay30;
                case delay30 -> state = State.Work;
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
                case Work -> {
                    if (((Boolean)read("pressed"))) {
                        write("prev_light", 0);
                        write("pressed", false);
                    }
                    else if (((((isInactive(processRefs.get("pRed"))) && (isActive(processRefs.get("pGreen"))))) || (((isInactive(processRefs.get("pGreen"))) && (isActive(processRefs.get("pRed"))))))) {
                        int __start = ((Number)(1)).intValue();
                        int __end   = ((Number)(3)).intValue();
                        int __step  = ((Number)(1)).intValue();

                        if (__step == 0)
                            throw new RuntimeException("FOR step cannot be zero");

                        memory.put("alight", __start);

                        while (loopCond("alight", __end, __step)) {
                            if (((Boolean) getArrayValue(resolve("rLightsArray"), ((Integer)read("alight")), 1))) {
                                write("prev_light", ((Integer)read("alight")));
                            }
                            setArrayValue(resolve("rLightsArray"), ((Integer)read("alight")), 1, false);
                            memory.put(
                                "alight",
                                ((Integer)read("alight")) + __step
                            );
                        }
                        processRefs.get("pRed").stop();
                        processRefs.get("pYellow").start();
                        processRefs.get("pGreen").stop();
                        setState(State.delay10);
                    }
                    else if ((((double)(((Integer)read("prev_light")))) == ((double)(0)))) {
                        int __start = ((Number)(1)).intValue();
                        int __end   = ((Number)(3)).intValue();
                        int __step  = ((Number)(1)).intValue();

                        if (__step == 0)
                            throw new RuntimeException("FOR step cannot be zero");

                        memory.put("alight", __start);

                        while (loopCond("alight", __end, __step)) {
                            setArrayValue(resolve("rLightsArray"), ((Integer)read("alight")), 1, false);
                            memory.put(
                                "alight",
                                ((Integer)read("alight")) + __step
                            );
                        }
                        processRefs.get("pRed").stop();
                        processRefs.get("pYellow").stop();
                        processRefs.get("pGreen").start();
                        setState(State.delay30);
                    }
                    else if ((((double)(((Integer)read("prev_light")))) == ((double)(2)))) {
                        int __start = ((Number)(1)).intValue();
                        int __end   = ((Number)(3)).intValue();
                        int __step  = ((Number)(1)).intValue();

                        if (__step == 0)
                            throw new RuntimeException("FOR step cannot be zero");

                        memory.put("alight", __start);

                        while (loopCond("alight", __end, __step)) {
                            setArrayValue(resolve("rLightsArray"), ((Integer)read("alight")), 1, false);
                            memory.put(
                                "alight",
                                ((Integer)read("alight")) + __step
                            );
                        }
                        processRefs.get("pRed").start();
                        processRefs.get("pYellow").stop();
                        processRefs.get("pGreen").stop();
                        setState(State.delay30);
                    }
                }
                case delay10 -> {
                }
                case delay30 -> {
                    if ((((Boolean)read("control_sensor")) && isActive(processRefs.get("pRed")))) {
                        write("pressed", true);
                        setState(State.Work);
                    }
                }
                case Stop, Error -> { }
            }
        }

        @Override
        public void dumpStates(Map<String,String> out) {
            out.put("Control_state", state.name());
        }

        @Override
        public void dumpTimers(Map<String,Long> out) {
            out.put("Control_time", timerBaseTime);
        }

        @Override
        public String getStateName() {
            return state.name();
        }

    }

    private boolean isActive(IProcess p) {
        String s = p.getStateName();
        return !s.equals("Stop") && !s.equals("Error");
    }

    private boolean isInactive(IProcess p) {
        String s = p.getStateName();
        return s.equals("Stop") || s.equals("Error");
    }

    private boolean isStop(IProcess p) {
        return p.getStateName().equals("Stop");
    }

    private boolean isError(IProcess p) {
        return p.getStateName().equals("Error");
    }

    private boolean loopCond(String var, int end, int step) {
        int value = ((Number)memory.get(var)).intValue();
        return (step >= 0 && value <= end)
            || (step < 0 && value >= end);
    }

    private Object getArrayValue(String name, int index, int start) {
        List<String> list = (List<String>) memory.get(name);

        int offset = index - start;

        if (offset < 0 || offset >= list.size()) {
            throw new RuntimeException(
                "Array index out of bounds: " + name + "[" + index + "]"
            );
        }

        String cell = list.get(offset);
        return memory.get(cell);
    }

    private void setArrayValue(String name, int index, int start, Object value) {
        List<String> list = (List<String>) memory.get(name);

        int offset = index - start;

        if (offset < 0 || offset >= list.size()) {
            throw new RuntimeException(
                "Array index out of bounds: " + name + "[" + index + "]"
            );
        }

        String cell = list.get(offset);
        memory.put(cell, value);
    }

}
