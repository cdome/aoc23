package day06

import java.io.File
import kotlin.math.ceil
import kotlin.math.sqrt

fun main() {
    races()
    singleRace()
}

private fun races() {
    val file = File("src/main/resources/day06-boats").readLines()
    val times = file[0].split(":")[1].trim().split(" ").filter { it.isNotBlank() }.map { it.toLong() }
    val distances = file[1].split(":")[1].trim().split(" ").filter { it.isNotBlank() }.map { it.toLong() }

    times.indices.map { i ->
        val t = times[i]
        val d = distances[i]
        possibilities(t, d)
    }.reduce(Int::times).let { println("Multiple races possibilities: $it") }
}

private fun singleRace() {
    val file = File("src/main/resources/day06-boats").readLines()
    val time = file[0].split(":")[1].replace(" ", "").toLong()
    val distance = file[1].split(":")[1].replace(" ", "").toLong()

    println("Single race possibilities: ${possibilities(time, distance)}")
}

private fun possibilities(t: Long, d: Long): Int {
    val base = (t - sqrt(t * t - 4 * d.toDouble())) / 2
    val lowerLimit = if (base == ceil(base)) base + 1 else ceil(base)
    return (t - 2 * lowerLimit + 1).toInt()
}