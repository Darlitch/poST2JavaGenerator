package su.nsk.iae.post.generator.java.common

import su.nsk.iae.post.poST.Program
import su.nsk.iae.post.poST.Process
import su.nsk.iae.post.poST.VarInitDeclaration

import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.vars.VarMemoryGenerator

class ProgramGenerator {

    static val INDENT = "    "

    val ProcessGenerator processGen = new ProcessGenerator

    def String generate(Program program, GenerationContext ctx) {

        registerAll(program, ctx)

        val builder = new StringBuilder
        val name = program.name

        builder.append(
'''
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class «name» {

«INDENT»private final Map<String,Object> memory;
«INDENT»private final List<IProcess> processes = new ArrayList<>();
«INDENT»private final Set<String> inputNames = new HashSet<>();
«INDENT»private final Set<String> outputNames = new HashSet<>();
«INDENT»private final Set<String> globalNames = new HashSet<>();
«INDENT»private final Set<String> varNames = new HashSet<>();

'''
        )

//        builder.append(generateProcessFields(program))
        builder.append(generateConstructor(program, ctx))
        builder.append(generateRunIter())
        builder.append("\n")
        builder.append(generateRegisterProcess())
        builder.append("\n")
        builder.append(generateDumpStates())
        builder.append("\n")
        builder.append(generateDumpTimers())
        builder.append("\n")
        builder.append(generateDumpInputs())
        builder.append("\n")
        builder.append(generateDumpOutputs())
        builder.append("\n")
        builder.append(generateDumpGlobals())
        builder.append("\n")
        builder.append(generateDumpVars())
        builder.append("\n")

        for (Process p : program.processes) {
            builder.append(processGen.generate(p, ctx, INDENT))
        }
        
        builder.append(
'''

«INDENT»private boolean isActive(IProcess p) {
«INDENT»    String s = p.getStateName();
«INDENT»    return !s.equals("Stop") && !s.equals("Error");
«INDENT»}

«INDENT»private boolean isInactive(IProcess p) {
«INDENT»    String s = p.getStateName();
«INDENT»    return s.equals("Stop") || s.equals("Error");
«INDENT»}

«INDENT»private boolean isStop(IProcess p) {
«INDENT»    return p.getStateName().equals("Stop");
«INDENT»}

«INDENT»private boolean isError(IProcess p) {
«INDENT»    return p.getStateName().equals("Error");
«INDENT»}
'''
		)
		
		builder.append(
'''

«INDENT»private boolean loopCond(String var, int end, int step) {
«INDENT»    int value = ((Number)memory.get(var)).intValue();
«INDENT»    return (step >= 0 && value <= end)
«INDENT»        || (step < 0 && value >= end);
«INDENT»}
'''
		)

		builder.append(
'''

«INDENT»private Object getArrayValue(String name, int index, int start) {
«INDENT»    List<String> list = (List<String>) memory.get(resolve(name));

«INDENT»    int offset = index - start;

«INDENT»    if (offset < 0 || offset >= list.size()) {
«INDENT»        throw new RuntimeException(
«INDENT»            "Array index out of bounds: " + name + "[" + index + "]"
«INDENT»        );
«INDENT»    }

«INDENT»    String cell = list.get(offset);
«INDENT»    return memory.get(cell);
«INDENT»}

«INDENT»private void setArrayValue(String name, int index, int start, Object value) {
«INDENT»    List<String> list = (List<String>) memory.get(resolve(name));

«INDENT»    int offset = index - start;

«INDENT»    if (offset < 0 || offset >= list.size()) {
«INDENT»        throw new RuntimeException(
«INDENT»            "Array index out of bounds: " + name + "[" + index + "]"
«INDENT»        );
«INDENT»    }

«INDENT»    String cell = list.get(offset);
«INDENT»    memory.put(cell, value);
«INDENT»}

}
'''
		)

        builder.toString
    }

    // ================= PROCESS FIELDS =================

//    private def String generateProcessFields(Program program) {
//        val builder = new StringBuilder
//
//        for (p : program.processes) {
//            builder.append(
//'''
//«INDENT»private final «p.name» «p.name.toFirstLower»;
//'''
//            )
//        }
//
//        builder.toString
//    }

    // ================= CONSTRUCTOR =================

    private def String generateConstructor(
        Program program,
        GenerationContext ctx
    ) {

        val builder = new StringBuilder

        builder.append(
'''

«INDENT»public «program.name»(Map<String,Object> memory) {
«INDENT»    this.memory = memory;
'''
        )

        // ===== VAR INIT =====

        for (v : program.progInVars)
            for (decl : v.vars)
                builder.append(VarMemoryGenerator.generate(decl, ctx, INDENT))

        for (v : program.progOutVars)
            for (decl : v.vars)
                builder.append(VarMemoryGenerator.generate(decl, ctx, INDENT))

        for (v : program.progVars)
            for (decl : v.vars)
                builder.append(VarMemoryGenerator.generate(decl, ctx, INDENT))

        for (v : program.progInOutVars)
            for (decl : v.vars)
                builder.append(VarMemoryGenerator.generate(decl, ctx, INDENT))

        for (v : program.progTempVars)
            for (decl : v.vars)
                builder.append(VarMemoryGenerator.generate(decl, ctx, INDENT))
                
        // ===== PROCESS VAR INIT =====

		for (p : program.processes) {
		
		    for (v : p.procVars)
		        for (decl : v.vars)
		            builder.append(VarMemoryGenerator.generate(decl, ctx, INDENT+'    '))
		
		    for (v : p.procInVars)
		        for (decl : v.vars)
		            builder.append(VarMemoryGenerator.generate(decl, ctx, INDENT+'    '))
		
		    for (v : p.procOutVars)
		        for (decl : v.vars)
		            builder.append(VarMemoryGenerator.generate(decl, ctx, INDENT+'    '))
		}

        // ===== registry =====

        for (n : ctx.inputVars) {
            builder.append(
'''
«INDENT»    inputNames.add("«n»");
'''
            )
        }

        for (n : ctx.outputVars) {
            builder.append(
'''
«INDENT»    outputNames.add("«n»");
'''
            )
        }

        for (n : ctx.globalVars) {
            builder.append(
'''
«INDENT»    globalNames.add("«n»");
'''
            )
        }

        for (n : ctx.localVars) {
            builder.append(
'''
«INDENT»    varNames.add("«n»");
'''
            )
        }

        // ===== ïðîöåññû =====

//        for (p : program.processes) {
//            val field = p.name.toFirstLower
//
//            builder.append(
//'''
//«INDENT»    «field» = new «p.name»(memory);
//«INDENT»    processes.add(«field»);
//'''
//            )
//        }

        builder.append(
'''
«INDENT»}
'''
        )

        builder.toString
    }

    // ================= RUN ITER =================

    private def String generateRunIter() {

'''
«INDENT»public void runIter(long cycleTimeMs) {
	
«INDENT»    memory.put(
«INDENT»        "_global_time",
«INDENT»        ((Long)memory.get("_global_time")) + cycleTimeMs
«INDENT»    );

«INDENT»    for (IProcess p : processes)
«INDENT»        p.run();
«INDENT»}
'''
    }

    // ================= PROCESS DEBUG =================

    private def String generateDumpStates() {

'''
«INDENT»public Map<String,String> dumpProcessStates() {

«INDENT»    Map<String,String> res = new HashMap<>();

«INDENT»    for (IProcess p : processes)
«INDENT»        p.dumpStates(res);

«INDENT»    return res;
«INDENT»}
'''
    }

    private def String generateDumpTimers() {

'''
«INDENT»public Map<String,Long> dumpProcessTimers() {

«INDENT»    Map<String,Long> res = new HashMap<>();

«INDENT»    for (IProcess p : processes)
«INDENT»        p.dumpTimers(res);

«INDENT»    return res;
«INDENT»}
'''
    }

    // ================= VARIABLE DEBUG =================

    private def String generateDumpInputs() {

'''
«INDENT»public Map<String,Object> dumpInputs() {

«INDENT»    Map<String,Object> res = new HashMap<>();

«INDENT»    for (String n : inputNames)
«INDENT»        res.put(n, memory.get(n));

«INDENT»    return res;
«INDENT»}
'''
    }

    private def String generateDumpOutputs() {

'''
«INDENT»public Map<String,Object> dumpOutputs() {

«INDENT»    Map<String,Object> res = new HashMap<>();

«INDENT»    for (String n : outputNames)
«INDENT»        res.put(n, memory.get(n));

«INDENT»    return res;
«INDENT»}
'''
    }

    private def String generateDumpGlobals() {

'''
«INDENT»public Map<String,Object> dumpGlobals() {

«INDENT»    Map<String,Object> res = new HashMap<>();

«INDENT»    for (String n : globalNames)
«INDENT»        res.put(n, memory.get(n));

«INDENT»    return res;
«INDENT»}
'''
    }

    private def String generateDumpVars() {

'''
«INDENT»public Map<String,Object> dumpVars() {

«INDENT»    Map<String,Object> res = new HashMap<>();

«INDENT»    for (String n : varNames)
«INDENT»        res.put(n, memory.get(n));

«INDENT»    return res;
«INDENT»}
'''
    }

    // ================= REGISTER =================
    
    private def String generateRegisterProcess() '''
«INDENT»public void registerProcess(IProcess p) {
«INDENT»    processes.add(p);
«INDENT»}
'''

    def void registerAll(Program program, GenerationContext ctx) {

        for (v : program.progInVars)
            for (decl : v.vars)
                registerVarDecl(decl, ctx, [name | ctx.registerInputVar(name)])

        for (v : program.progOutVars)
            for (decl : v.vars)
                registerVarDecl(decl, ctx, [name | ctx.registerOutputVar(name)])

        for (v : program.progVars)
            for (decl : v.vars)
                registerVarDecl(decl, ctx, [name | ctx.registerLocalVar(name)])

        for (v : program.progInOutVars)
            for (decl : v.vars)
                registerVarDecl(decl, ctx, [name |
                    ctx.registerInputVar(name)
                    ctx.registerOutputVar(name)
                ])

        for (v : program.progTempVars)
            for (decl : v.vars)
                registerVarDecl(decl, ctx, [name | ctx.registerLocalVar(name)])

        for (p : program.processes) {
            val field = p.name.toFirstLower
            ctx.registerProcess(p.name, field, p.name)
        }

        for (p : program.processes) {

            for (v : p.procInVars)
                for (decl : v.vars)
                    registerVarDecl(decl, ctx, [name | ])

            for (v : p.procOutVars)
                for (decl : v.vars)
                    registerVarDecl(decl, ctx, [name | ])

            for (v : p.procVars)
                for (decl : v.vars)
                    registerVarDecl(decl, ctx, [name | ])

            for (v : p.procProcessVars)
                for (decl : v.vars)
                    for (vname : decl.varList.vars) {
                        val procType = decl.process.name
                        val field = vname.name
                        ctx.registerProcess(field, field, procType)
                    }
        }
    }

    private def String resolveType(VarInitDeclaration decl) {
        if (decl.spec !== null)
            return decl.spec.type

        if (decl.arrSpec !== null)
            return decl.arrSpec.init.type

        throw new IllegalStateException("Unknown declaration type: " + decl)
    }

    private def void registerVarDecl(
        VarInitDeclaration decl,
        GenerationContext ctx,
        (String)=>void registry
    ) {
        val type = resolveType(decl)

        for (vname : decl.varList.vars) {
            ctx.registerVar(vname.name, type)
            registry.apply(vname.name)

            if (decl.arrSpec !== null) {
                ctx.registerArrayType(vname.name, type)
//                ctx.registerArrayStart(vname.name, 0)
            }
        }
    }
}