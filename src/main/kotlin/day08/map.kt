package day08

import java.io.File

fun main() {
    val file = File("src/main/resources/day08-map").readLines()
    val instructions = file[0]

    val map = file.stream().skip(2).map { line ->
        val (def, dirs) = line.split(" = ")
        val (l, r) = dirs.split(", ")
        def.trim() to Pair(l.removePrefix("("), r.removeSuffix(")"))
    }.toList().toMap()

    fun step(steps: Long, current: String): String {
        val direction = instructions[(steps % instructions.length).toInt()]
        return if (direction == 'L') map[current]!!.first else map[current]!!.second
    }

    var steps = 0L
    var current = "AAA"
    while (current != "ZZZ") {
        current = step(steps++, current)
    }
    println("Steps: $steps")

    val stepsList = map.keys
        .filter { it.endsWith("A") }
        .map {
            steps = 0
            current = it
            while (!current.endsWith("Z")) {
                current = step(steps++, current)
            }
            steps
        }

    val lcm = stepsList.reduce { acc, i -> lcm(acc, i) }
    println("Simultaneous Steps: $lcm")
}

fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
