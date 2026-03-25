import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Plant {

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

    public Plant(Map<String,Object> memory) {
        this.memory = memory;
        memory.put("DOOR_SPEED", 0.5);
        memory.put("DOOR_OPEN_COORD", -50);
        memory.put("coord", 0.0);
        memory.put("DOOR_SPEED", 0.5);
        memory.put("DOOR_OPEN_COORD", -50);
        memory.put("coord", 0.0);
        memory.put("DOOR_SPEED", 0.5);
        memory.put("DOOR_OPEN_COORD", -50);
        memory.put("coord", 0.0);
        memory.put("ELEV_ACCEL", 0.25);
        memory.put("ELEV_MAX_SPEED", 0.5);
        memory.put("ELEV_DOWN_COORD", 440.0);
        memory.put("v", 0.0);
        memory.put("coord", 0.0);

        registerTo(inputNames, "onfloor1");
        registerTo(inputNames, "onfloor2");
        registerTo(inputNames, "call0");
        registerTo(inputNames, "button2");
        registerTo(inputNames, "call1");
        registerTo(inputNames, "call2");
        registerTo(inputNames, "onfloor0");
        registerTo(inputNames, "door1closed");
        registerTo(inputNames, "button0");
        registerTo(inputNames, "button1");
        registerTo(inputNames, "door2closed");
        registerTo(inputNames, "door0closed");

        registerTo(outputNames, "floor0_LED");
        registerTo(outputNames, "cur");
        registerTo(outputNames, "call2_LED");
        registerTo(outputNames, "button1_LED");
        registerTo(outputNames, "call1_LED");
        registerTo(outputNames, "button0_LED");
        registerTo(outputNames, "down");
        registerTo(outputNames, "floor1_LED");
        registerTo(outputNames, "button2_LED");
        registerTo(outputNames, "open0");
        registerTo(outputNames, "open1");
        registerTo(outputNames, "open2");
        registerTo(outputNames, "call0_LED");
        registerTo(outputNames, "up");
        registerTo(outputNames, "floor2_LED");

        varNames.add("target");
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

    class Init extends BaseProcess {
	
        enum State {
            begin,
            Stop,
            Error
        }

        public Init(String instanceName, Map<String,Object> memory, Map<String,String> aliases) {
            super(instanceName, memory, aliases);
        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.begin;
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
                case begin -> state = State.begin;
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
                case begin -> {
                    writeVar("onfloor0", false);
                    writeVar("onfloor1", false);
                    writeVar("onfloor2", false);
                    writeVar("call0", false);
                    writeVar("call1", false);
                    writeVar("call2", false);
                    writeVar("button0", false);
                    writeVar("button1", false);
                    writeVar("button2", false);
                    writeVar("door0closed", false);
                    writeVar("door1closed", false);
                    writeVar("door2closed", false);
                    writeVar("up", false);
                    writeVar("down", false);
                    writeVar("open0", false);
                    writeVar("open1", false);
                    writeVar("open2", false);
                    writeVar("call0_LED", false);
                    writeVar("call1_LED", false);
                    writeVar("call2_LED", false);
                    writeVar("button0_LED", false);
                    writeVar("button1_LED", false);
                    writeVar("button2_LED", false);
                    writeVar("floor0_LED", false);
                    writeVar("floor1_LED", false);
                    writeVar("floor2_LED", false);
                    processRefs.get("Door0Sim").start();
                    processRefs.get("Door1Sim").start();
                    processRefs.get("Door2Sim").start();
                    processRefs.get("ElevatorSim").start();
                    this.stop();
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

    class Door0Sim extends BaseProcess {
	
        enum State {
            check_open_close,
            Stop,
            Error
        }

        public Door0Sim(String instanceName, Map<String,Object> memory, Map<String,String> aliases) {
            super(instanceName, memory, aliases);
        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.check_open_close;
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
                case check_open_close -> state = State.check_open_close;
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
                case check_open_close -> {
                    if (readBool("open0")) {
                        writeVar("coord", (readFloat("coord") - readFloat("DOOR_SPEED")));
                    }
                    else {
                        writeVar("coord", (readFloat("coord") + readFloat("DOOR_SPEED")));
                    }
                    if ((((double)(readFloat("coord"))) >= ((double)(0.0)))) {
                        writeVar("coord", ((float)(0.0)));
                    }
                    if ((((float)(readFloat("coord"))) <= ((float)(readFloat("DOOR_OPEN_COORD"))))) {
                        writeVar("coord", readFloat("DOOR_OPEN_COORD"));
                    }
                    if ((((double)(readFloat("coord"))) == ((double)(0.0)))) {
                        writeVar("door0closed", true);
                    }
                    else {
                        writeVar("door0closed", false);
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

    class Door1Sim extends BaseProcess {
	
        enum State {
            check_open_close,
            Stop,
            Error
        }

        public Door1Sim(String instanceName, Map<String,Object> memory, Map<String,String> aliases) {
            super(instanceName, memory, aliases);
        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.check_open_close;
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
                case check_open_close -> state = State.check_open_close;
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
                case check_open_close -> {
                    if (readBool("open1")) {
                        writeVar("coord", (readFloat("coord") - readFloat("DOOR_SPEED")));
                    }
                    else {
                        writeVar("coord", (readFloat("coord") + readFloat("DOOR_SPEED")));
                    }
                    if ((((double)(readFloat("coord"))) >= ((double)(0.0)))) {
                        writeVar("coord", ((float)(0.0)));
                    }
                    if ((((float)(readFloat("coord"))) <= ((float)(readFloat("DOOR_OPEN_COORD"))))) {
                        writeVar("coord", readFloat("DOOR_OPEN_COORD"));
                    }
                    if ((((double)(readFloat("coord"))) == ((double)(0.0)))) {
                        writeVar("door1closed", true);
                    }
                    else {
                        writeVar("door1closed", false);
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

    class Door2Sim extends BaseProcess {
	
        enum State {
            check_open_close,
            Stop,
            Error
        }

        public Door2Sim(String instanceName, Map<String,Object> memory, Map<String,String> aliases) {
            super(instanceName, memory, aliases);
        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.check_open_close;
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
                case check_open_close -> state = State.check_open_close;
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
                case check_open_close -> {
                    if (readBool("open2")) {
                        writeVar("coord", (readFloat("coord") - readFloat("DOOR_SPEED")));
                    }
                    else {
                        writeVar("coord", (readFloat("coord") + readFloat("DOOR_SPEED")));
                    }
                    if ((((double)(readFloat("coord"))) >= ((double)(0.0)))) {
                        writeVar("coord", ((float)(0.0)));
                    }
                    if ((((float)(readFloat("coord"))) <= ((float)(readFloat("DOOR_OPEN_COORD"))))) {
                        writeVar("coord", readFloat("DOOR_OPEN_COORD"));
                    }
                    if ((((double)(readFloat("coord"))) == ((double)(0.0)))) {
                        writeVar("door2closed", true);
                    }
                    else {
                        writeVar("door2closed", false);
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

    class ElevatorSim extends BaseProcess {
	
        enum State {
            up_down,
            Stop,
            Error
        }

        public ElevatorSim(String instanceName, Map<String,Object> memory, Map<String,String> aliases) {
            super(instanceName, memory, aliases);
        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.up_down;
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
                case up_down -> state = State.up_down;
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
                case up_down -> {
                    if (readBool("up")) {
                        writeVar("v", (readFloat("v") - readFloat("ELEV_ACCEL")));
                    }
                    else if (readBool("down")) {
                        writeVar("v", (readFloat("v") + readFloat("ELEV_ACCEL")));
                    }
                    else {
                        writeVar("v", ((float)(0.0)));
                    }
                    if ((((float)(readFloat("v"))) > ((float)(readFloat("ELEV_MAX_SPEED"))))) {
                        writeVar("v", readFloat("ELEV_MAX_SPEED"));
                    }
                    else if ((((float)(readFloat("v"))) < ((float)((0 - readFloat("ELEV_MAX_SPEED")))))) {
                        writeVar("v", (0 - readFloat("ELEV_MAX_SPEED")));
                    }
                    writeVar("coord", (readFloat("coord") + readFloat("v")));
                    if ((((double)(readFloat("coord"))) < ((double)(0.0)))) {
                        writeVar("coord", ((float)(0.0)));
                    }
                    else if ((((float)(readFloat("coord"))) > ((float)(readFloat("ELEV_DOWN_COORD"))))) {
                        writeVar("coord", readFloat("ELEV_DOWN_COORD"));
                    }
                    writeVar("onfloor0", false);
                    writeVar("onfloor1", false);
                    writeVar("onfloor2", false);
                    if ((((double)(readFloat("coord"))) < ((double)(1.5)))) {
                        writeVar("onfloor2", true);
                    }
                    else if ((((((double)(readFloat("coord"))) > ((double)(224.5)))) && ((((double)(readFloat("coord"))) < ((double)(225.5)))))) {
                        writeVar("onfloor1", true);
                    }
                    else if (((((double)(readFloat("coord"))) > ((double)((readFloat("ELEV_DOWN_COORD") - 20.0)))))) {
                        writeVar("onfloor0", true);
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
