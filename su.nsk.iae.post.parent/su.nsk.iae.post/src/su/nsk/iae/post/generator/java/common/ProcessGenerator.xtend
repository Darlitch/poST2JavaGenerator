package su.nsk.iae.post.generator.java.common

import su.nsk.iae.post.poST.Process
import su.nsk.iae.post.poST.State
import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.statement.StatementListGenerator
import su.nsk.iae.post.generator.java.common.util.MemoryUtil

class ProcessGenerator {

    val StatementListGenerator stmtGen
    val StateGenerator stateGen

    new() {
        stmtGen = new StatementListGenerator
        stateGen = new StateGenerator(stmtGen)
    }

    def String generate(Process p, GenerationContext ctx, String indent) {
    	
    	if (p.states.empty)
	        throw new IllegalStateException(
	            "Process must contain at least one STATE: " + p.name
	        )

        val builder = new StringBuilder

        val name = p.name
        val nextIndent = indent + "    "

        builder.append(
'''
«indent»class «name» implements IProcess {

«generateStateEnum(p, nextIndent)»

«nextIndent»private final java.util.Map<String,Object> memory;

«generateConstructor(name, nextIndent)»

«nextIndent»private State state = State.Stop;

«nextIndent»private long timerBaseTime;

«generateControlMethods(p, nextIndent)»

«generateRunMethod(p, ctx, nextIndent)»

«generateDumpStates(name, nextIndent)»

«generateDumpTimers(name, nextIndent)»

«indent»}
'''
        )

        builder.toString
    }
    
    // ================= CONSTRUCTOR =================

	private def String generateConstructor(String name, String indent) {

    '''
«indent»public «name»(java.util.Map<String,Object> memory) {
«indent»    this.memory = memory;
«indent»}
'''
	}

    // ================= ENUM STATE =================

    private def String generateStateEnum(Process p, String indent) {

        val states = p.states.map[it.name]

        '''
«indent»enum State {
«indent»    «FOR s : states SEPARATOR ",\n"»«s»«ENDFOR»,
«indent»    Stop,
«indent»    Error
«indent»}
'''
    }

    // ================= CONTROL METHODS =================

    private def String generateControlMethods(Process p, String indent) {

        val firstState = p.states.head.name
        val globalTime = MemoryUtil.globalTime()

        '''
«indent»public void start() {
«indent»    state = State.«firstState»;
«indent»    timerBaseTime = ((Long)memory.get("«globalTime»"));
«indent»}

«indent»public void stop() {
«indent»    state = State.Stop;
«indent»    timerBaseTime = ((Long)memory.get("«globalTime»"));
«indent»}

«indent»public void error() {
«indent»    state = State.Error;
«indent»    timerBaseTime = ((Long)memory.get("«globalTime»"));
«indent»}

«indent»public void setState(State s) {
«indent»    state = s;
«indent»    timerBaseTime = ((Long)memory.get("«globalTime»"));
«indent»}

«generateSetNext(p, indent)»

«indent»public State getState() {
«indent»    return state;
«indent»}
'''
    }

    // ================= SET NEXT =================

    private def String generateSetNext(Process p, String indent) {

        val states = p.states.map[it.name]

        val builder = new StringBuilder

        builder.append(
'''
«indent»public void setNext() {

«indent»    switch(state) {
'''
        )

        for (i : 0 ..< states.size) {

            val current = states.get(i)
            val next =
                if (i < states.size - 1)
                    states.get(i + 1)
                else
                    states.get(0)

            builder.append(
'''
«indent»        case «current» -> state = State.«next»;
'''
            )
        }

        builder.append(
'''
«indent»        default -> { }
«indent»    }

«indent»    timerBaseTime = ((Long)memory.get("«MemoryUtil.globalTime()»"));
«indent»}
'''
        )

        builder.toString
    }

    // ================= RUN =================

    private def String generateRunMethod(Process p, GenerationContext ctx, String indent) {

        val builder = new StringBuilder

        builder.append(
'''
«indent»@Override
«indent»public void run() {

«indent»    switch(state) {
'''
        )

        for (State s : p.states) {
            builder.append(
                stateGen.generate(s, ctx, indent + "        ")
            )
        }

        builder.append(
'''
«indent»        case Stop, Error -> { }
«indent»    }
«indent»}
'''
        )

        builder.toString
    }

    // ================= DEBUG =================

    private def String generateDumpStates(String name, String indent) {

        '''
«indent»@Override
«indent»public void dumpStates(java.util.Map<String,String> out) {
«indent»    out.put("«name»_state", state.name());
«indent»}
'''
    }

    private def String generateDumpTimers(String name, String indent) {

        '''
«indent»@Override
«indent»public void dumpTimers(java.util.Map<String,Long> out) {
«indent»    out.put("«name»_time", timerBaseTime);
«indent»}
'''
    }

}