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

    public Controller(Map<String,Object> memory, Map<String, IProcess> processMap) {
        this.memory = memory;
        this.processMap = processMap;

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

    class Init extends BaseProcess {
	
        enum State {
            begin,
            Stop,
            Error
        }

        public Init(String instanceName, Map<String,Object> memory, Map<String,String> aliases, Map<String, IProcess> globalProcesses) {
            super(instanceName, memory, aliases, globalProcesses);

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
                    getProcess("call0Latch").start();
                    getProcess("call1Latch").start();
                    getProcess("call2Latch").start();
                    getProcess("button0Latch").start();
                    getProcess("button1Latch").start();
                    getProcess("button2Latch").start();
                    getProcess("checkCurFloor").start();
                    getProcess("upControl").start();
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

    class Call0Latch extends BaseProcess {
	
        enum State {
            init,
            check_ON_OFF,
            Stop,
            Error
        }

        public Call0Latch(String instanceName, Map<String,Object> memory, Map<String,String> aliases, Map<String, IProcess> globalProcesses) {
            super(instanceName, memory, aliases, globalProcesses);

            localMemory.put("prev_in", false);
            localMemory.put("prev_out", false);
        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.init;
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
                case init -> state = State.check_ON_OFF;
                case check_ON_OFF -> state = State.init;
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
                case init -> {
                    writeVar("prev_in", !(readBool("call0")));
                    writeVar("prev_out", !(readBool("open0")));
                    setNext();
                }
                case check_ON_OFF -> {
                    if ((readBool("call0") && !(readBool("prev_in")))) {
                        writeVar("call0_LED", true);
                    }
                    if ((readBool("open0") && !(readBool("prev_out")))) {
                        writeVar("call0_LED", false);
                    }
                    writeVar("prev_in", readBool("call0"));
                    writeVar("prev_out", readBool("open0"));
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

    class Call1Latch extends BaseProcess {
	
        enum State {
            init,
            check_ON_OFF,
            Stop,
            Error
        }

        public Call1Latch(String instanceName, Map<String,Object> memory, Map<String,String> aliases, Map<String, IProcess> globalProcesses) {
            super(instanceName, memory, aliases, globalProcesses);

            localMemory.put("prev_in", false);
            localMemory.put("prev_out", false);
        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.init;
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
                case init -> state = State.check_ON_OFF;
                case check_ON_OFF -> state = State.init;
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
                case init -> {
                    writeVar("prev_in", !(readBool("call1")));
                    writeVar("prev_out", !(readBool("open1")));
                    setNext();
                }
                case check_ON_OFF -> {
                    if ((readBool("call1") && !(readBool("prev_in")))) {
                        writeVar("call1_LED", true);
                    }
                    if ((readBool("open1") && !(readBool("prev_out")))) {
                        writeVar("call1_LED", false);
                    }
                    writeVar("prev_in", readBool("call1"));
                    writeVar("prev_out", readBool("open1"));
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

    class Call2Latch extends BaseProcess {
	
        enum State {
            init,
            check_ON_OFF,
            Stop,
            Error
        }

        public Call2Latch(String instanceName, Map<String,Object> memory, Map<String,String> aliases, Map<String, IProcess> globalProcesses) {
            super(instanceName, memory, aliases, globalProcesses);

            localMemory.put("prev_in", false);
            localMemory.put("prev_out", false);
        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.init;
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
                case init -> state = State.check_ON_OFF;
                case check_ON_OFF -> state = State.init;
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
                case init -> {
                    writeVar("prev_in", !(readBool("call2")));
                    writeVar("prev_out", !(readBool("open2")));
                    setNext();
                }
                case check_ON_OFF -> {
                    if ((readBool("call2") && !(readBool("prev_in")))) {
                        writeVar("call2_LED", true);
                    }
                    if ((readBool("open2") && !(readBool("prev_out")))) {
                        writeVar("call2_LED", false);
                    }
                    writeVar("prev_in", readBool("call2"));
                    writeVar("prev_out", readBool("open2"));
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

    class Button0Latch extends BaseProcess {
	
        enum State {
            init,
            check_ON_OFF,
            Stop,
            Error
        }

        public Button0Latch(String instanceName, Map<String,Object> memory, Map<String,String> aliases, Map<String, IProcess> globalProcesses) {
            super(instanceName, memory, aliases, globalProcesses);

            localMemory.put("prev_in", false);
            localMemory.put("prev_out", false);
        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.init;
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
                case init -> state = State.check_ON_OFF;
                case check_ON_OFF -> state = State.init;
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
                case init -> {
                    writeVar("prev_in", !(readBool("button0")));
                    writeVar("prev_out", !(readBool("open0")));
                    setNext();
                }
                case check_ON_OFF -> {
                    if ((readBool("button0") && !(readBool("prev_in")))) {
                        writeVar("button0_LED", true);
                    }
                    if ((readBool("open0") && !(readBool("prev_out")))) {
                        writeVar("button0_LED", false);
                    }
                    writeVar("prev_in", readBool("button0"));
                    writeVar("prev_out", readBool("open0"));
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

    class Button1Latch extends BaseProcess {
	
        enum State {
            init,
            check_ON_OFF,
            Stop,
            Error
        }

        public Button1Latch(String instanceName, Map<String,Object> memory, Map<String,String> aliases, Map<String, IProcess> globalProcesses) {
            super(instanceName, memory, aliases, globalProcesses);

            localMemory.put("prev_in", false);
            localMemory.put("prev_out", false);
        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.init;
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
                case init -> state = State.check_ON_OFF;
                case check_ON_OFF -> state = State.init;
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
                case init -> {
                    writeVar("prev_in", !(readBool("button1")));
                    writeVar("prev_out", !(readBool("open1")));
                    setNext();
                }
                case check_ON_OFF -> {
                    if ((readBool("button1") && !(readBool("prev_in")))) {
                        writeVar("button1_LED", true);
                    }
                    if ((readBool("open1") && !(readBool("prev_out")))) {
                        writeVar("button1_LED", false);
                    }
                    writeVar("prev_in", readBool("button1"));
                    writeVar("prev_out", readBool("open1"));
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

    class Button2Latch extends BaseProcess {
	
        enum State {
            init,
            check_ON_OFF,
            Stop,
            Error
        }

        public Button2Latch(String instanceName, Map<String,Object> memory, Map<String,String> aliases, Map<String, IProcess> globalProcesses) {
            super(instanceName, memory, aliases, globalProcesses);

            localMemory.put("prev_in", false);
            localMemory.put("prev_out", false);
        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.init;
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
                case init -> state = State.check_ON_OFF;
                case check_ON_OFF -> state = State.init;
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
                case init -> {
                    writeVar("prev_in", !(readBool("button2")));
                    writeVar("prev_out", !(readBool("open2")));
                    setNext();
                }
                case check_ON_OFF -> {
                    if ((readBool("button2") && !(readBool("prev_in")))) {
                        writeVar("button2_LED", true);
                    }
                    if ((readBool("open2") && !(readBool("prev_out")))) {
                        writeVar("button2_LED", false);
                    }
                    writeVar("prev_in", readBool("button2"));
                    writeVar("prev_out", readBool("open2"));
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

    class CheckCurFloor extends BaseProcess {
	
        enum State {
            check_floor,
            Stop,
            Error
        }

        public CheckCurFloor(String instanceName, Map<String,Object> memory, Map<String,String> aliases, Map<String, IProcess> globalProcesses) {
            super(instanceName, memory, aliases, globalProcesses);

        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.check_floor;
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
                case check_floor -> state = State.check_floor;
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
                case check_floor -> {
                    if (readBool("onfloor0")) {
                        writeVar("cur", 0);
                        writeVar("floor0_LED", true);
                        writeVar("floor1_LED", false);
                        writeVar("floor2_LED", false);
                    }
                    else if (readBool("onfloor1")) {
                        writeVar("cur", 1);
                        writeVar("floor0_LED", false);
                        writeVar("floor1_LED", true);
                        writeVar("floor2_LED", false);
                    }
                    else if (readBool("onfloor2")) {
                        writeVar("cur", 2);
                        writeVar("floor0_LED", false);
                        writeVar("floor1_LED", false);
                        writeVar("floor2_LED", true);
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

    class UpControl extends BaseProcess {
	
        enum State {
            check_calls,
            check_stop,
            door_cycle,
            Stop,
            Error
        }

        public UpControl(String instanceName, Map<String,Object> memory, Map<String,String> aliases, Map<String, IProcess> globalProcesses) {
            super(instanceName, memory, aliases, globalProcesses);

        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.check_calls;
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
                case check_calls -> state = State.check_stop;
                case check_stop -> state = State.door_cycle;
                case door_cycle -> state = State.check_calls;
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
                case check_calls -> {
                    if ((((((readInt("cur") == 0) && ((readBool("call0_LED") || readBool("button0_LED"))))) || (((readInt("cur") == 1) && ((readBool("call1_LED") || readBool("button1_LED")))))) || (((readInt("cur") == 2) && ((readBool("call2_LED") || readBool("button2_LED"))))))) {
                        getProcess("doorCycle").start();
                        setState(State.door_cycle);
                    }
                    else {
                        final Object __caseVal = (readInt("cur"));
                        if (((Number)(__caseVal)).longValue() == ((Number)(0)).longValue()) {
                            if (((((readBool("call1_LED") || readBool("button1_LED"))) || ((readBool("call2_LED") || readBool("button2_LED")))))) {
                                getProcess("upMotion").start();
                                setNext();
                            }
                        }
                        else if (((Number)(__caseVal)).longValue() == ((Number)(1)).longValue()) {
                            if (((readBool("call2_LED") || readBool("button2_LED")))) {
                                getProcess("upMotion").start();
                                setNext();
                            }
                            else if (((readBool("call0_LED") || readBool("button0_LED")))) {
                                getProcess("downControl").start();
                                this.stop();
                            }
                        }
                        else if (((Number)(__caseVal)).longValue() == ((Number)(2)).longValue()) {
                            getProcess("downControl").start();
                            this.stop();
                        }
                    }
                }
                case check_stop -> {
                    if ((isInactive(getProcess("upMotion")))) {
                        getProcess("doorCycle").start();
                        setNext();
                    }
                }
                case door_cycle -> {
                    if ((isInactive(getProcess("doorCycle")))) {
                        this.start();
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

    class UpMotion extends BaseProcess {
	
        enum State {
            start,
            check_target,
            Stop,
            Error
        }

        public UpMotion(String instanceName, Map<String,Object> memory, Map<String,String> aliases, Map<String, IProcess> globalProcesses) {
            super(instanceName, memory, aliases, globalProcesses);

        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.start;
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
                case start -> state = State.check_target;
                case check_target -> state = State.start;
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
                case start -> {
                    writeVar("up", true);
                    final Object __caseVal = (readInt("cur"));
                    if (((Number)(__caseVal)).longValue() == ((Number)(0)).longValue()) {
                        if (((readBool("call1_LED") || readBool("button1_LED")))) {
                            writeVar("target", 1);
                            setNext();
                        }
                    }
                    else if (((Number)(__caseVal)).longValue() == ((Number)(1)).longValue()) {
                        if (((readBool("call2_LED") || readBool("button2_LED")))) {
                            writeVar("target", 2);
                            setNext();
                        }
                    }
                    else if (((Number)(__caseVal)).longValue() == ((Number)(2)).longValue()) {
                        writeVar("target", 2);
                        setNext();
                    }
                }
                case check_target -> {
                    if (((readInt("cur") == readInt("target")))) {
                        writeVar("up", false);
                        this.stop();
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

    class DownControl extends BaseProcess {
	
        enum State {
            check_calls,
            check_stop,
            door_cycle,
            Stop,
            Error
        }

        public DownControl(String instanceName, Map<String,Object> memory, Map<String,String> aliases, Map<String, IProcess> globalProcesses) {
            super(instanceName, memory, aliases, globalProcesses);

        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.check_calls;
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
                case check_calls -> state = State.check_stop;
                case check_stop -> state = State.door_cycle;
                case door_cycle -> state = State.check_calls;
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
                case check_calls -> {
                    if ((((((readInt("cur") == 0) && ((readBool("call0") || readBool("button0"))))) || (((readInt("cur") == 1) && ((readBool("call1") || readBool("button1")))))) || (((readInt("cur") == 2) && ((readBool("call2") || readBool("button2"))))))) {
                        getProcess("doorCycle").start();
                        setState(State.door_cycle);
                    }
                    else {
                        final Object __caseVal = (readInt("cur"));
                        if (((Number)(__caseVal)).longValue() == ((Number)(0)).longValue()) {
                            getProcess("upControl").start();
                            this.stop();
                        }
                        else if (((Number)(__caseVal)).longValue() == ((Number)(1)).longValue()) {
                            if (((readBool("call0_LED") || readBool("button0_LED")))) {
                                getProcess("downMotion").start();
                                setNext();
                            }
                            else if (((readBool("call2_LED") || readBool("button2_LED")))) {
                                getProcess("upControl").start();
                                this.stop();
                            }
                        }
                        else if (((Number)(__caseVal)).longValue() == ((Number)(2)).longValue()) {
                            if (((((readBool("call1_LED") || readBool("button1_LED"))) || ((readBool("call0_LED") || readBool("button0_LED")))))) {
                                getProcess("downMotion").start();
                                setNext();
                            }
                        }
                    }
                }
                case check_stop -> {
                    if ((isInactive(getProcess("downMotion")))) {
                        getProcess("doorCycle").start();
                        setNext();
                    }
                }
                case door_cycle -> {
                    if ((isInactive(getProcess("doorCycle")))) {
                        this.start();
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

    class DownMotion extends BaseProcess {
	
        enum State {
            start,
            chech_next,
            Stop,
            Error
        }

        public DownMotion(String instanceName, Map<String,Object> memory, Map<String,String> aliases, Map<String, IProcess> globalProcesses) {
            super(instanceName, memory, aliases, globalProcesses);

        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.start;
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
                case start -> state = State.chech_next;
                case chech_next -> state = State.start;
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
                case start -> {
                    writeVar("down", true);
                    final Object __caseVal = (readInt("cur"));
                    if (((Number)(__caseVal)).longValue() == ((Number)(0)).longValue()) {
                        writeVar("target", 0);
                        setNext();
                    }
                    else if (((Number)(__caseVal)).longValue() == ((Number)(1)).longValue()) {
                        if (((readBool("call0_LED") || readBool("button0_LED")))) {
                            writeVar("target", 0);
                            setNext();
                        }
                    }
                    else if (((Number)(__caseVal)).longValue() == ((Number)(2)).longValue()) {
                        if (((readBool("call1_LED") || readBool("button1_LED")))) {
                            writeVar("target", 1);
                            setNext();
                        }
                    }
                }
                case chech_next -> {
                    if (((readInt("cur") == readInt("target")))) {
                        writeVar("down", false);
                        this.stop();
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

    class DoorCycle extends BaseProcess {
	
        enum State {
            choose_door_to_open,
            delay3s,
            check_closed,
            Stop,
            Error
        }

        public DoorCycle(String instanceName, Map<String,Object> memory, Map<String,String> aliases, Map<String, IProcess> globalProcesses) {
            super(instanceName, memory, aliases, globalProcesses);

        }

        private State state = State.Stop;

        private long timerBaseTime;

        public void start() {
            state = State.choose_door_to_open;
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
                case choose_door_to_open -> state = State.delay3s;
                case delay3s -> state = State.check_closed;
                case check_closed -> state = State.choose_door_to_open;
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
                case choose_door_to_open -> {
                    final Object __caseVal = (readInt("cur"));
                    if (((Number)(__caseVal)).longValue() == ((Number)(0)).longValue()) {
                        writeVar("open0", true);
                    }
                    else if (((Number)(__caseVal)).longValue() == ((Number)(1)).longValue()) {
                        writeVar("open1", true);
                    }
                    else if (((Number)(__caseVal)).longValue() == ((Number)(2)).longValue()) {
                        writeVar("open2", true);
                    }
                    setNext();
                }
                case delay3s -> {
                    if (((Long)memory.get("_global_time")) - this.timerBaseTime >= 3000L) {
                        writeVar("open0", false);
                        writeVar("open1", false);
                        writeVar("open2", false);
                        setNext();
                    }
                }
                case check_closed -> {
                    if ((((readBool("door0closed") && readBool("door1closed")) && readBool("door2closed")))) {
                        this.stop();
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
