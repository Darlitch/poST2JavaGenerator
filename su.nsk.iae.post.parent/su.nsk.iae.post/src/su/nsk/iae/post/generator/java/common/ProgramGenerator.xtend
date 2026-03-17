package su.nsk.iae.post.generator.java.common

import su.nsk.iae.post.poST.Program
import su.nsk.iae.post.poST.Process
import su.nsk.iae.post.poST.VarInitDeclaration
import su.nsk.iae.post.poST.Model

import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.vars.VarMemoryGenerator

class ProgramGenerator {

    val ProcessGenerator processGen = new ProcessGenerator

    def String generate(Program program, GenerationContext ctx) {

		registerAll(program, ctx)
        val builder = new StringBuilder
        val name = program.name

        builder.append(
'''
public class «name» {

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
            builder.append(
                processGen.generate(p, ctx, "    ")
            )
        }

        builder.append(
'''
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
    private final «p.name» «p.name.toFirstLower»;
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
    public «program.name»() {

        memory.put("_global_time", 0L);
'''
        )

        // ===== INPUT =====
        for (v : program.progInVars) {
            for (decl : v.vars) {

                for (vname : decl.varList.vars) {
                    ctx.registerInputVar(vname.name)
                }

                builder.append(
                    VarMemoryGenerator.generate(decl, ctx)
                )
            }
        }

        // ===== OUTPUT =====
        for (v : program.progOutVars) {
            for (decl : v.vars) {

                for (vname : decl.varList.vars) {
                    ctx.registerOutputVar(vname.name)
                }

                builder.append(
                    VarMemoryGenerator.generate(decl, ctx)
                )
            }
        }

        // ===== VAR =====
        for (v : program.progVars) {
            for (decl : v.vars) {

                for (vname : decl.varList.vars) {
                    ctx.registerLocalVar(vname.name)
                }

                builder.append(
                    VarMemoryGenerator.generate(decl, ctx)
                )
            }
        }

        // ===== IN_OUT =====
        for (v : program.progInOutVars) {
            for (decl : v.vars) {

                for (vname : decl.varList.vars) {
                    ctx.registerInputVar(vname.name)
                    ctx.registerOutputVar(vname.name)
                }

                builder.append(
                    VarMemoryGenerator.generate(decl, ctx)
                )
            }
        }

        // ===== TEMP =====
        for (v : program.progTempVars) {
            for (decl : v.vars) {

                for (vname : decl.varList.vars) {
                    ctx.registerLocalVar(vname.name)
                }

                builder.append(
                    VarMemoryGenerator.generate(decl, ctx)
                )
            }
        }

        // ===== runtime registry =====

        for (n : ctx.inputVars) {
            builder.append(
'''
        inputNames.add("«n»");
'''
            )
        }

        for (n : ctx.outputVars) {
            builder.append(
'''
        outputNames.add("«n»");
'''
            )
        }

        for (n : ctx.globalVars) {
            builder.append(
'''
        globalNames.add("«n»");
'''
            )
        }

        for (n : ctx.localVars) {
            builder.append(
'''
        varNames.add("«n»");
'''
            )
        }

        // ===== ïðîöåññû =====

        for (p : program.processes) {

            val field = p.name.toFirstLower

            builder.append(
'''
        «field» = new «p.name»(memory);
        processes.add(«field»);
'''
            )
        }

        builder.append(
'''
    }
'''
        )

        builder.toString
    }

    // ================= RUN ITER =================

    private def String generateRunIter() {

'''
    public void runIter(long cycleTimeMs) {

        memory.put(
            "_global_time",
            ((Long)memory.get("_global_time")) + cycleTimeMs
        );

        for (IProcess p : processes)
            p.run();
    }
'''
    }

    // ================= PROCESS DEBUG =================

    private def String generateDumpStates() {

'''
    public java.util.Map<String,String> dumpProcessStates() {

        java.util.Map<String,String> res =
            new java.util.HashMap<>();

        for (IProcess p : processes)
            p.dumpStates(res);

        return res;
    }
'''
    }

    private def String generateDumpTimers() {

'''
    public java.util.Map<String,Long> dumpProcessTimers() {

        java.util.Map<String,Long> res =
            new java.util.HashMap<>();

        for (IProcess p : processes)
            p.dumpTimers(res);

        return res;
    }
'''
    }

    // ================= VARIABLE DEBUG =================

    private def String generateDumpInputs() {

'''
    public java.util.Map<String,Object> dumpInputs() {

        java.util.Map<String,Object> res =
            new java.util.HashMap<>();

        for (String n : inputNames)
            res.put(n, memory.get(n));

        return res;
    }
'''
    }

    private def String generateDumpOutputs() {

'''
    public java.util.Map<String,Object> dumpOutputs() {

        java.util.Map<String,Object> res =
            new java.util.HashMap<>();

        for (String n : outputNames)
            res.put(n, memory.get(n));

        return res;
    }
'''
    }

    private def String generateDumpGlobals() {

'''
    public java.util.Map<String,Object> dumpGlobals() {

        java.util.Map<String,Object> res =
            new java.util.HashMap<>();

        for (String n : globalNames)
            res.put(n, memory.get(n));

        return res;
    }
'''
    }

    private def String generateDumpVars() {

'''
    public java.util.Map<String,Object> dumpVars() {

        java.util.Map<String,Object> res =
            new java.util.HashMap<>();

        for (String n : varNames)
            res.put(n, memory.get(n));

        return res;
    }
'''
    }
    
    def void registerAll(Program program, GenerationContext ctx) {

	    // ===== INPUT =====
	    for (v : program.progInVars)
	        for (decl : v.vars)
	            registerVarDecl(decl, ctx, [name |
	                ctx.registerInputVar(name)
	            ])
	
	    // ===== OUTPUT =====
	    for (v : program.progOutVars)
	        for (decl : v.vars)
	            registerVarDecl(decl, ctx, [name |
	                ctx.registerOutputVar(name)
	            ])
	
	    // ===== VAR =====
	    for (v : program.progVars)
	        for (decl : v.vars)
	            registerVarDecl(decl, ctx, [name |
	                ctx.registerLocalVar(name)
	            ])
	
	    // ===== IN_OUT =====
	    for (v : program.progInOutVars)
	        for (decl : v.vars)
	            registerVarDecl(decl, ctx, [name |
	                ctx.registerInputVar(name)
	                ctx.registerOutputVar(name)
	            ])
	
	    // ===== TEMP =====
	    for (v : program.progTempVars)
	        for (decl : v.vars)
	            registerVarDecl(decl, ctx, [name |
	                ctx.registerLocalVar(name)
	            ])
	
	    // ===== PROCESSES =====
	    for (p : program.processes) {
	        val field = p.name.toFirstLower
	        ctx.registerProcess(p.name, field, p.name)
	    }
	
	    // ===== PROCESS VARIABLES =====
	    for (p : program.processes) {
	
	        // VAR_INPUT
	        for (v : p.procInVars)
	            for (decl : v.vars)
	                registerVarDecl(decl, ctx, [name | ])
	
	        // VAR_OUTPUT
	        for (v : p.procOutVars)
	            for (decl : v.vars)
	                registerVarDecl(decl, ctx, [name | ])
	
	        // VAR
	        for (v : p.procVars)
	            for (decl : v.vars)
	                registerVarDecl(decl, ctx, [name | ])
	
	        // VAR_PROCESS
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
	
	    throw new IllegalStateException(
	        "Unknown declaration type: " + decl
	    )
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
	
	        // åñëè ìàññèâ — ðåãèñòðèðóåì êàê ìàññèâ
	        if (decl.arrSpec !== null) {
	            ctx.registerArrayType(vname.name, type)
	
	            // ñòàðò ìîæíî âðåìåííî 0 (ïîòîì óëó÷øèì)
	            ctx.registerArrayStart(vname.name, 0)
	        }
	    }
	}

}