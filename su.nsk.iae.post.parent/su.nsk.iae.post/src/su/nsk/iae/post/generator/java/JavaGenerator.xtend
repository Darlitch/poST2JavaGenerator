package su.nsk.iae.post.generator.java

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

import su.nsk.iae.post.poST.Model
import su.nsk.iae.post.poST.Program

import su.nsk.iae.post.generator.IPoSTGenerator
import su.nsk.iae.post.generator.java.common.ProgramGenerator
import su.nsk.iae.post.generator.java.common.IProcessGenerator
import su.nsk.iae.post.generator.java.configuration.ConfigurationGenerator
import su.nsk.iae.post.generator.java.common.context.GenerationContext

class JavaGenerator implements IPoSTGenerator {

    override setModel(Model model) {
        // не требуется
    }

    override beforeGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {}

    override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {

        val model =
            input.allContents
                 .toIterable
                 .filter(Model)
                 .head

        val ctx = new GenerationContext

        fsa.generateFile(
            "IProcess.java",
            IProcessGenerator.generate()
        )

        for (Program p : model.programs) {

            val code = new ProgramGenerator().generate(p, ctx)

            fsa.generateFile(
                p.name + ".java",
                code
            )
        }

        if (model.conf !== null) {

            val code =
                new ConfigurationGenerator()
                    .generate(model.conf, ctx)

            fsa.generateFile(
                model.conf.name + "Simulation.java",
                code
            )
        }
    }

    override afterGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {}
}