package su.nsk.iae.post.generator.java.configuration

import su.nsk.iae.post.poST.Model
import su.nsk.iae.post.poST.Program

import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.vars.VarMemoryGenerator

class DefaultSimulationGenerator {

    val SimulationClassGenerator simulationGen = new SimulationClassGenerator

    def String generate(Model model, GenerationContext ctx) {
        val fields = generateFields(model, "    ")
        val constructorBody = generateConstructorBody(model, ctx, "        ")
        val programInstanceName = resolveProgramInstanceName(model)

        simulationGen.generate(
            fields,
            constructorBody,
            programInstanceName
        )
    }

    private def String generateFields(Model model, String indent) {
        val builder = new StringBuilder

        for (Program p : model.programs) {
            val instance = p.name.toFirstLower
            builder.append(
'''
«indent»private final «p.name» «instance»;
'''
            )
        }

        builder.toString
    }

    private def String generateConstructorBody(Model model, GenerationContext ctx, String indent) {
        val builder = new StringBuilder

        // fixed default step for simulation without CONFIGURATION
        builder.append(
'''
«indent»this.taskTimeMs = 100L;
'''
        )

        // ===== PROGRAM VARS =====
        for (Program p : model.programs) {

            for (v : p.progInVars)
                for (decl : v.vars)
                    builder.append(VarMemoryGenerator.generate(decl, ctx, indent))

            for (v : p.progOutVars)
                for (decl : v.vars)
                    builder.append(VarMemoryGenerator.generate(decl, ctx, indent))

            for (v : p.progVars)
                for (decl : v.vars)
                    builder.append(VarMemoryGenerator.generate(decl, ctx, indent))

            for (v : p.progInOutVars)
                for (decl : v.vars)
                    builder.append(VarMemoryGenerator.generate(decl, ctx, indent))

            for (v : p.progTempVars)
                for (decl : v.vars)
                    builder.append(VarMemoryGenerator.generate(decl, ctx, indent))
        }

        // ===== PROGRAM INSTANCES =====
        for (Program p : model.programs) {
            val name = p.name
            val instance = name.toFirstLower

            builder.append(
'''

«indent»this.«instance» = new «name»(memory, processMap);
'''
            )
        }

        // ===== REGISTER PROCESSES =====
        for (Program p : model.programs) {
            for (proc : p.processes) {
                ctx.registerProcess(
                    proc.name,
                    proc.name.toFirstLower,
                    processJavaTypeName(proc.name)
                )
            }
        }

        // ===== PROCESSES =====
        for (Program p : model.programs) {

            val programInstance = p.name.toFirstLower

            // create
            for (proc : p.processes) {

                val procName = proc.name.toFirstLower
                val procType = processJavaTypeName(proc.name)
                val programType = p.name

                builder.append(
'''

«indent»Map<String,String> «procName»_aliases = new HashMap<>();
«indent»«programType».«procType» «procName» = new «programType».«procType»("«procName»", memory, «procName»_aliases, processMap);
«indent»«programInstance».registerProcess(«procName»);
'''
                )

                var boolean hasInit = false

                for (proc2 : p.processes) {
                    if (proc2.name.equals("Init")) {
                        hasInit = true
                    }
                }

                if (proc.name.equals("Init")) {
                    builder.append(
'''
«indent»«procName».start();
'''
                    )
                } else if (!hasInit && proc === p.processes.get(0)) {
                    builder.append(
'''
«indent»«procName».start();
'''
                    )
                }
            }

            // binding
            for (proc : p.processes) {

                val procName = proc.name.toFirstLower

                for (v : proc.procProcessVars) {
                    for (decl : v.vars) {
                        for (vname : decl.varList.vars) {

                            val target = vname.name
                            val resolved = ctx.resolveProcess(target)

                            builder.append(
'''
«indent»«procName».setProcess("«target»", «resolved»);
'''
                            )
                        }
                    }
                }
            }
        }

        builder.toString
    }
    
    private def String resolveProgramInstanceName(Model model) {
	    if (model.programs.empty)
	        throw new IllegalStateException("No PROGRAM defined in model")
	
	    model.programs.head.name.toFirstLower
	}
	
	private def String processJavaTypeName(String processName) {
	    processName + "Process"
	}
}
