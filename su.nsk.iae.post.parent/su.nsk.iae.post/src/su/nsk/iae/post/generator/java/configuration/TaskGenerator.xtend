package su.nsk.iae.post.generator.java.configuration

import su.nsk.iae.post.poST.Task
import static extension su.nsk.iae.post.generator.java.common.util.MemoryUtil.*

class TaskGenerator {

    def String generate(Task task) {

        val init = task.init

        if (init.interval === null)
            throw new IllegalStateException(
                "Only INTERVAL tasks are supported in simulator"
            )

        val intervalMs = parseTime(init.interval.time)

        '''
long taskTimeMs = «intervalMs»;
'''
    }

}