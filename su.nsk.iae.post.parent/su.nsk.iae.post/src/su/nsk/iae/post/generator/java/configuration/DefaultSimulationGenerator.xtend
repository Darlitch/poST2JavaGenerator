package su.nsk.iae.post.generator.java.configuration

import su.nsk.iae.post.poST.Model
import su.nsk.iae.post.poST.Program

import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.vars.VarMemoryGenerator

class DefaultSimulationGenerator {

    def String generate(Model model, GenerationContext ctx) {

        val IND = "        "
        val builder = new StringBuilder

        builder.append(
'''
import java.util.Map;
import java.util.HashMap;

public class Simulation {

    public static void main(String[] args) throws Exception {

        Map<String,Object> memory = new HashMap<>();
        
        Map<String, IProcess> processMap = new HashMap<>();
        
        memory.put("_global_time", 0L);
'''
        )

        // ================= PROGRAM VARS =================
        for (Program p : model.programs) {

            for (v : p.progInVars)
                for (decl : v.vars)
                    builder.append(VarMemoryGenerator.generate(decl, ctx, IND))

            for (v : p.progOutVars)
                for (decl : v.vars)
                    builder.append(VarMemoryGenerator.generate(decl, ctx, IND))

            for (v : p.progVars)
                for (decl : v.vars)
                    builder.append(VarMemoryGenerator.generate(decl, ctx, IND))

            for (v : p.progInOutVars)
                for (decl : v.vars)
                    builder.append(VarMemoryGenerator.generate(decl, ctx, IND))

            for (v : p.progTempVars)
                for (decl : v.vars)
                    builder.append(VarMemoryGenerator.generate(decl, ctx, IND))
        }

        // ================= PROGRAM INSTANCES =================

        for (Program p : model.programs) {

            val name = p.name
            val instance = name.toFirstLower
			
			builder.append("\n")
            builder.append(
'''
«IND»«name» «instance» = new «name»(memory, processMap);
'''
            )
        }
        
        // ================= REGISTER PROCESSES =================

		for (Program p : model.programs) {
		    for (proc : p.processes) {
		        ctx.registerProcess(
		            proc.name,
		            proc.name.toFirstLower,
		            proc.name
		        )
		    }
		}
        
        // ================= PROCESSES =================

		for (Program p : model.programs) {

		    val programInstance = p.name.toFirstLower
		
		    // ===== PHASE 1: CREATE =====
		    for (proc : p.processes) {
		
		        val procName = proc.name.toFirstLower
		        val procType = proc.name
		
		        builder.append("\n")
		
		        builder.append(
'''
«IND»Map<String,String> «procName»_aliases = new HashMap<>();
«IND»«procType» «procName» = new «procType»("«procName»", memory, «procName»_aliases, processMap);
«IND»«programInstance».registerProcess(«procName»);
'''
		        )
		
		        // ===== AUTOSTART =====
				var boolean hasInit = false
				
				// сначала проверяем есть ли Init
				for (proc2 : p.processes) {
				    if (proc2.name.equals("Init")) {
				        hasInit = true
				    }
				}
				
				// если текущий процесс Init → стартуем
				if (proc.name.equals("Init")) {
				    builder.append(
				'''
				«IND»«procName».start();
				'''
				    )
				}
				
				// если Init нет → стартуем ПЕРВЫЙ процесс
				else if (!hasInit && proc === p.processes.get(0)) {
				    builder.append(
				'''
				«IND»«procName».start();
				'''
				    )
				}
		    }
		
		    // ===== PHASE 2: BINDING =====
		    for (proc : p.processes) {
		
		        val procName = proc.name.toFirstLower
		
		        for (v : proc.procProcessVars) {
		            for (decl : v.vars) {
		                for (vname : decl.varList.vars) {
		
		                    val target = vname.name
		                    val resolved = ctx.resolveProcess(target)
							
	                    	builder.append(
'''
«IND»«procName».setProcess("«target»", «resolved»);
'''
		                    )
		                }
		            }
		        }
		    }
		}

        // ================= LOOP =================

        builder.append(
'''
        
«IND»long taskTimeMs = 100L;

«IND»while (true) {
'''
        )

        for (Program p : model.programs) {
            val instance = p.name.toFirstLower

            builder.append(
'''
«IND»    «instance».runIter(taskTimeMs);
'''
            )
        }

        builder.append(
'''
«IND»    Thread.sleep(taskTimeMs);
        }
    }
}
'''
        )

        return builder.toString
    }
}