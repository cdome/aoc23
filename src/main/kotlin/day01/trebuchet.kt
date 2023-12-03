package day01

import java.io.File

val numericNumbers = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
val wordNumbers = arrayOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
val numbers = wordNumbers + numericNumbers

fun main() {
    val instructions = File("src/main/resources/day01-trebuchet")
    println("Plain Numbers:" + instructions.readLines().sumOf { line ->
        line.first { it.isDigit() }.digitToInt() * 10 + line.last { it.isDigit() }.digitToInt()
    })

    println("Numbers and words:" + instructions
        .readLines()
        .map { firstLastNumber(it) }
        .sumOf { it.first * 10 + it.second }
    )
}

fun firstLastNumber(line: String): Pair<Int, Int> {
    val firstNumberIndex = numbers
        .mapIndexed { idx, value -> idx to line.indexOf(value) }
        .filter { (_, position) -> position > -1 }
        .minBy {(_, position) -> position }
        .first
    val lastNumberIndex = numbers
        .mapIndexed { idx, value -> idx to line.lastIndexOf(value) }
        .filter {(_, position) -> position > -1 }
        .maxBy { (_, position) -> position }
        .first

    fun numericValue(index: Int) = (if (index > 8) index - 9 else index) + 1
    return numericValue(firstNumberIndex) to numericValue(lastNumberIndex)
}
