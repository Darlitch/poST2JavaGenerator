package su.nsk.iae.post.generator.java.configuration

import su.nsk.iae.post.poST.TemplateProcessConfElement
import su.nsk.iae.post.generator.java.common.context.GenerationContext
import su.nsk.iae.post.generator.java.common.vars.BindingGenerator

class ProcessConfGenerator {

    def String generate(TemplateProcessConfElement conf, GenerationContext ctx) {

        val builder = new StringBuilder

        val name = conf.name
        val type = conf.process.name

        // ===== имя поля в Java =====
        val fieldName = name

        // ===== регистрация процесса в контексте =====
        ctx.registerProcess(name, fieldName, type)

        // ===== создание процесса =====
        builder.append(
'''
«type» «fieldName» = new «type»(memory);
processes.add(«fieldName»);
'''
        )

        // ===== binding параметров =====
        if (conf.args !== null) {

            for (arg : conf.args.elements) {
                BindingGenerator.generate(arg, ctx)
            }
        }

        // ===== ACTIVE =====
        if (conf.active) {

            builder.append(
'''
«fieldName».start();
'''
            )
        }

        builder.toString
    }

}