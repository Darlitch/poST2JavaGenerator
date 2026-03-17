public class Controller {

    private final java.util.Map<String,Object> memory =
        new java.util.HashMap<>();

    private final java.util.List<IProcess> processes =
        new java.util.ArrayList<>();

    private final java.util.Set<String> inputNames =
        new java.util.HashSet<>();

    private final java.util.Set<String> outputNames =
        new java.util.HashSet<>();

    private final java.util.Set<String> globalNames =
        new java.util.HashSet<>();

    private final java.util.Set<String> varNames =
        new java.util.HashSet<>();
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
    public java.util.Map<String,String> dumpProcessStates() {

        java.util.Map<String,String> res =
            new java.util.HashMap<>();

        for (IProcess p : processes)
            p.dumpStates(res);

        return res;
    }
    public java.util.Map<String,Long> dumpProcessTimers() {

        java.util.Map<String,Long> res =
            new java.util.HashMap<>();

        for (IProcess p : processes)
            p.dumpTimers(res);

        return res;
    }
    public java.util.Map<String,Object> dumpInputs() {

        java.util.Map<String,Object> res =
            new java.util.HashMap<>();

        for (String n : inputNames)
            res.put(n, memory.get(n));

        return res;
    }
    public java.util.Map<String,Object> dumpOutputs() {

        java.util.Map<String,Object> res =
            new java.util.HashMap<>();

        for (String n : outputNames)
            res.put(n, memory.get(n));

        return res;
    }
    public java.util.Map<String,Object> dumpGlobals() {

        java.util.Map<String,Object> res =
            new java.util.HashMap<>();

        for (String n : globalNames)
            res.put(n, memory.get(n));

        return res;
    }
    public java.util.Map<String,Object> dumpVars() {

        java.util.Map<String,Object> res =
            new java.util.HashMap<>();

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

        private final java.util.Map<String,Object> memory;

        public Light(java.util.Map<String,Object> memory) {
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
        public void dumpStates(java.util.Map<String,String> out) {
            out.put("Light_state", state.name());
        }

        @Override
        public void dumpTimers(java.util.Map<String,Long> out) {
            out.put("Light_time", timerBaseTime);
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

        private final java.util.Map<String,Object> memory;

        public Control(java.util.Map<String,Object> memory) {
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
                    else if ((((((pRed.getState() == Light.State.Stop || pRed.getState() == Light.State.Error)) && ((pGreen.getState() != Light.State.Stop && pGreen.getState() != Light.State.Error)))) || ((((pGreen.getState() == Light.State.Stop || pGreen.getState() == Light.State.Error)) && ((pRed.getState() != Light.State.Stop && pRed.getState() != Light.State.Error)))))) {
                        int __start = ((Number)(0)).intValue();
                        int __end   = ((Number)(3)).intValue();
                        int __step  = ((Number)(1)).intValue();

                        if (__step == 0)
                            throw new RuntimeException("FOR step cannot be zero");

                        memory.put("alight", __start);

                        while (
                               (__step >= 0 && ((Integer)memory.get("alight")) <= __end)
                            || (__step < 0  && ((Integer)memory.get("alight")) >= __end)
                        ) {
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

                        while (
                               (__step >= 0 && ((Integer)memory.get("alight")) <= __end)
                            || (__step < 0  && ((Integer)memory.get("alight")) >= __end)
                        ) {
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

                        while (
                               (__step >= 0 && ((Integer)memory.get("alight")) <= __end)
                            || (__step < 0  && ((Integer)memory.get("alight")) >= __end)
                        ) {
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
                    if ((((Boolean)memory.get("control_sensor")) && (pRed.getState() != Light.State.Stop && pRed.getState() != Light.State.Error))) {
                        memory.put("pressed", true);
                        setState(State.Work);
                    }
                }
                case Stop, Error -> { }
            }
        }

        @Override
        public void dumpStates(java.util.Map<String,String> out) {
            out.put("Control_state", state.name());
        }

        @Override
        public void dumpTimers(java.util.Map<String,Long> out) {
            out.put("Control_time", timerBaseTime);
        }

    }

    private Object getArrayValue(String name, int index, int start) {
        java.util.List<String> list =
            (java.util.List<String>) memory.get(name);

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
        java.util.List<String> list =
            (java.util.List<String>) memory.get(name);

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
