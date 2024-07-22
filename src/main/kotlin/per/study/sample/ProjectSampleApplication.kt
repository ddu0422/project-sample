package per.study.sample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

private const val MB = 1024 * 1024
private const val MEMORY_UNIT = "MB"

@SpringBootApplication
class ProjectSampleApplication

fun main(args: Array<String>) {
    val runtime = Runtime.getRuntime()

    val maxMemory = runtime.maxMemory()
    val freeMemory = runtime.freeMemory()
    val allocatedMemory = runtime.totalMemory()

    println("================= Memory Info =================")
    println("Free Memory: ${freeMemory.div(MB)} $MEMORY_UNIT")
    println("Allocated Memory: ${allocatedMemory.div(MB)} $MEMORY_UNIT")
    println("Max Memory: ${maxMemory.div(MB)} $MEMORY_UNIT")
    println("Total Memory: ${(freeMemory.plus(maxMemory).minus(allocatedMemory)).div(MB)} $MEMORY_UNIT")
    println("===============================================")

    runApplication<ProjectSampleApplication>(*args)
}
