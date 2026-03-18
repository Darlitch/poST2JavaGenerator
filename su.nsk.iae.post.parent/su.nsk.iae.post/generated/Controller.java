import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Controller {

    private final Map<String,Object> memory = new HashMap<>();
    private final List<IProcess> processes = new ArrayList<>();
    private final Set<String> inputNames = new HashSet<>();
    private final Set<String> outputNames = new HashSet<>();
    private final Set<String> globalNames = new HashSet<>();
    private final Set<String> varNames = new HashSet<>();

    private final Light light;
    private final Control control;

    public Controller() {
        memory.put("_global_time", 0L);
        globalNames.add("lightsArray1");
        globalNames.add("lightsArray2");
        globalNames.add("red2");
        globalNames.add("red1");
        globalNames.add("green2");
        globalNames.add("green1");
        globalNames.add("sensor");
        globalNames.add("yellow1");
        globalNames.add("yellow2");
        light = new Light(memory);
        processes.add(light);
        control = new Control(memory);
        processes.add(control);
    }
    public void runIter(long cycleTimeMs) {
	
        memory.put(
            "_global_time",
            ((Long)memory.get("_global_time")) + cycleTimeMs
        );

        for (IProcess p : processes)
            p.run();
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

        public Light(Map<String,Object> memory) {
            this.memory = memory;
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
                    memory.put("b_light", true);
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

        public Control(Map<String,Object> memory) {
            this.memory = memory;
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
                    if (((Boolean)memory.get("pressed"))) {
                        memory.put("prev_light", 0);
                        memory.put("pressed", false);
                    }
                    else if (((((isInactive(pRed)) && (isActive(pGreen)))) || (((isInactive(pGreen)) && (isActive(pRed)))))) {
                        int __start = ((Number)(0)).intValue();
                        int __end   = ((Number)(3)).intValue();
                        int __step  = ((Number)(1)).intValue();

                        if (__step == 0)
                            throw new RuntimeException("FOR step cannot be zero");

                        memory.put("alight", __start);

                        while (loopCond("alight", __end, __step)) {
                            if (((Boolean) getArrayValue("rLightsArray", ((Integer)memory.get("alight")), 0))) {
                                memory.put("prev_light", ((Integer)memory.get("alight")));
                            }
                            setArrayValue("rLightsArray", ((Integer)memory.get("alight")), 0, false);
                            memory.put(
                                "alight",
                                ((Integer)memory.get("alight")) + __step
                            );
                        }
                        pRed.stop();
                        pYellow.start();
                        pGreen.stop();
                        setState(State.delay10);
                    }
                    else if ((((double)(((Integer)memory.get("prev_light")))) == ((double)(0)))) {
                        int __start = ((Number)(0)).intValue();
                        int __end   = ((Number)(3)).intValue();
                        int __step  = ((Number)(1)).intValue();

                        if (__step == 0)
                            throw new RuntimeException("FOR step cannot be zero");

                        memory.put("alight", __start);

                        while (loopCond("alight", __end, __step)) {
                            setArrayValue("rLightsArray", ((Integer)memory.get("alight")), 0, false);
                            memory.put(
                                "alight",
                                ((Integer)memory.get("alight")) + __step
                            );
                        }
                        pRed.stop();
                        pYellow.stop();
                        pGreen.start();
                        setState(State.delay30);
                    }
                    else if ((((double)(((Integer)memory.get("prev_light")))) == ((double)(2)))) {
                        int __start = ((Number)(0)).intValue();
                        int __end   = ((Number)(3)).intValue();
                        int __step  = ((Number)(1)).intValue();

                        if (__step == 0)
                            throw new RuntimeException("FOR step cannot be zero");

                        memory.put("alight", __start);

                        while (loopCond("alight", __end, __step)) {
                            setArrayValue("rLightsArray", ((Integer)memory.get("alight")), 0, false);
                            memory.put(
                                "alight",
                                ((Integer)memory.get("alight")) + __step
                            );
                        }
                        pRed.start();
                        pYellow.stop();
                        pGreen.stop();
                        setState(State.delay30);
                    }
                }
                case delay10 -> {
                }
                case delay30 -> {
                    if ((((Boolean)memory.get("control_sensor")) && isActive(pRed))) {
                        memory.put("pressed", true);
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
