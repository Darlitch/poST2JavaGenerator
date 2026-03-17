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
public class «name» {

«INDENT»private final java.util.Map<String,Object> memory =
«INDENT»    new java.util.HashMap<>();

«INDENT»private final java.util.List<IProcess> processes =
«INDENT»    new java.util.ArrayList<>();

«INDENT»private final java.util.Set<String> inputNames =
«INDENT»    new java.util.HashSet<>();

«INDENT»private final java.util.Set<String> outputNames =
«INDENT»    new java.util.HashSet<>();

«INDENT»private final java.util.Set<String> globalNames =
«INDENT»    new java.util.HashSet<>();

«INDENT»private final java.util.Set<String> varNames =
«INDENT»    new java.util.HashSet<>();
'''
        )

        builder.append(generateProcessFields(program))
        builder.append(generateConstructor(program, ctx))
        builder.append(generateRunIter())
        builder.append(generateDumpStates())
        builder.append(generateDumpTimers())
        builder.append(generateDumpInputs())
        builder.append(generateDumpOutputs())
        builder.append(generateDumpGlobals())
        builder.append(generateDumpVars())

        for (Process p : program.processes) {
            builder.append(processGen.generate(p, ctx, INDENT))
        }

builder.append(
'''

«INDENT»private Object getArrayValue(String name, int index, int start) {
«INDENT»    java.util.List<String> list =
«INDENT»        (java.util.List<String>) memory.get(name);

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
«INDENT»    java.util.List<String> list =
«INDENT»        (java.util.List<String>) memory.get(name);

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

    private def String generateProcessFields(Program program) {
        val builder = new StringBuilder

        for (p : program.processes) {
            builder.append(
'''
«INDENT»private final «p.name» «p.name.toFirstLower»;
'''
            )
        }

        builder.toString
    }

    // ================= CONSTRUCTOR =================

    private def String generateConstructor(
        Program program,
        GenerationContext ctx
    ) {

        val builder = new StringBuilder

        builder.append(
'''
«INDENT»public «program.name»() {

«INDENT»    memory.put("_global_time", 0L);
'''
        )

        // ===== VAR INIT =====

        for (v : program.progInVars)
            for (decl : v.vars)
                builder.append(VarMemoryGenerator.generate(decl, ctx))

        for (v : program.progOutVars)
            for (decl : v.vars)
                builder.append(VarMemoryGenerator.generate(decl, ctx))

        for (v : program.progVars)
            for (decl : v.vars)
                builder.append(VarMemoryGenerator.generate(decl, ctx))

        for (v : program.progInOutVars)
            for (decl : v.vars)
                builder.append(VarMemoryGenerator.generate(decl, ctx))

        for (v : program.progTempVars)
            for (decl : v.vars)
                builder.append(VarMemoryGenerator.generate(decl, ctx))

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

        for (p : program.processes) {
            val field = p.name.toFirstLower

            builder.append(
'''
«INDENT»    «field» = new «p.name»(memory);
«INDENT»    processes.add(«field»);
'''
            )
        }

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
«INDENT»public java.util.Map<String,String> dumpProcessStates() {

«INDENT»    java.util.Map<String,String> res =
«INDENT»        new java.util.HashMap<>();

«INDENT»    for (IProcess p : processes)
«INDENT»        p.dumpStates(res);

«INDENT»    return res;
«INDENT»}
'''
    }

    private def String generateDumpTimers() {

'''
«INDENT»public java.util.Map<String,Long> dumpProcessTimers() {

«INDENT»    java.util.Map<String,Long> res =
«INDENT»        new java.util.HashMap<>();

«INDENT»    for (IProcess p : processes)
«INDENT»        p.dumpTimers(res);

«INDENT»    return res;
«INDENT»}
'''
    }

    // ================= VARIABLE DEBUG =================

    private def String generateDumpInputs() {

'''
«INDENT»public java.util.Map<String,Object> dumpInputs() {

«INDENT»    java.util.Map<String,Object> res =
«INDENT»        new java.util.HashMap<>();

«INDENT»    for (String n : inputNames)
«INDENT»        res.put(n, memory.get(n));

«INDENT»    return res;
«INDENT»}
'''
    }

    private def String generateDumpOutputs() {

'''
«INDENT»public java.util.Map<String,Object> dumpOutputs() {

«INDENT»    java.util.Map<String,Object> res =
«INDENT»        new java.util.HashMap<>();

«INDENT»    for (String n : outputNames)
«INDENT»        res.put(n, memory.get(n));

«INDENT»    return res;
«INDENT»}
'''
    }

    private def String generateDumpGlobals() {

'''
«INDENT»public java.util.Map<String,Object> dumpGlobals() {

«INDENT»    java.util.Map<String,Object> res =
«INDENT»        new java.util.HashMap<>();

«INDENT»    for (String n : globalNames)
«INDENT»        res.put(n, memory.get(n));

«INDENT»    return res;
«INDENT»}
'''
    }

    private def String generateDumpVars() {

'''
«INDENT»public java.util.Map<String,Object> dumpVars() {

«INDENT»    java.util.Map<String,Object> res =
«INDENT»        new java.util.HashMap<>();

«INDENT»    for (String n : varNames)
«INDENT»        res.put(n, memory.get(n));

«INDENT»    return res;
«INDENT»}
'''
    }

    // ================= REGISTER =================

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
                ctx.registerArrayStart(vname.name, 0)
            }
        }
    }
}