package su.nsk.iae.post.generator.java.common.statement

import java.util.List
import java.util.ArrayList

import su.nsk.iae.post.poST.StatementList
import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.generator.java.common.context.GenerationContext

class StatementListGenerator {

	val List<IStatementGenerator> generators = new ArrayList

	new() {
		// ===== обычные операторы =====
		generators.add(new AssignmentStatementGenerator)
		generators.add(new IfStatementGenerator(this))
		generators.add(new CaseStatementGenerator(this))

		// ===== циклы =====
		generators.add(new ForStatementGenerator(this))
		generators.add(new WhileStatementGenerator(this))
		generators.add(new RepeatStatementGenerator(this))

		// ===== управление процессами =====
		generators.add(new RestartProcessStatementGenerator)
		generators.add(new StartProcessStatementGenerator)
		generators.add(new StopProcessStatementGenerator)
		generators.add(new ErrorProcessStatementGenerator)

		// ===== управление состо€ни€ми =====
		generators.add(new SetStateStatementGenerator)

		// ===== таймеры =====
		generators.add(new ResetTimerStatementGenerator)
		generators.add(new TimeoutStatementGenerator(this))

		// ===== управление потоком =====
		generators.add(new ExitStatementGenerator)
		generators.add(new ReturnStatementGenerator)
	}
	
	// √енерирует Java-код дл€ всего списка операторов.
	def String generate(StatementList list, GenerationContext ctx, String indent) {

		if (list === null)
			return ""

		val builder = new StringBuilder
	
		for (stmt : list.statements) {
            val code = generateStatement(stmt, ctx, indent)

            if (code !== null && !code.trim.empty) {
                builder.append(code)
                if (!code.endsWith("\n")) {
                    builder.append("\n")
                }
            }
        }

		builder.toString
	}
	
	//Ќаходит подход€щий генератор дл€ конкретного Statement и делегирует ему генерацию кода.
	private def String generateStatement(Statement stmt, GenerationContext ctx, String indent) {

		for (g : generators) {
			if (g.supports(stmt))
				return g.generate(stmt, ctx, indent)
		}

		throw new IllegalStateException(
			"Unsupported statement: " + stmt.eClass.name
		)
	}
	
	def static String indent(String indent) {
		indent + "    "
	}
}