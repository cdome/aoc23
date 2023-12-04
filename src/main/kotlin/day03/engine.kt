package day03

import java.io.File

import kotlin.text.StringBuilder

fun main() {
    val lines = File("src/main/resources/day03-engine").readLines()
    println("Sum of part numbers: ${engineSum(lines)}")
    println("Sum of gear ratios: ${gears(lines)}")
}

fun gears(lines: List<String>): Int {
    val emptyLine = ".".repeat(lines[0].length)
    val extendedLines = listOf(emptyLine) + lines + emptyLine
    val numbers: List<List<Pair<IntRange, Int>>> = rangeNumbers(extendedLines)

    return extendedLines.mapIndexed { lineNo, line ->
        line.mapIndexed { charNo, char ->
            if (char == '*') {
                val gearedParts = getAdjacentNumbers(charNo, numbers[lineNo - 1]) +
                        getAdjacentNumbers(charNo, numbers[lineNo + 1]) +
                        getAdjacentNumbers(charNo, numbers[lineNo])
                if (gearedParts.size == 2) gearedParts[0] * gearedParts[1] else 0
            } else 0
        }
    }.flatten().sum()
}

fun getAdjacentNumbers(position: Int, line: List<Pair<IntRange, Int>>) = line
    .filter { (range, _) -> range.contains(position) || range.contains(position + 1) || range.contains(position - 1) }
    .map { (_, number) -> number }

private fun rangeNumbers(lines: List<String>) = lines.map { line ->
    var inNumber = false
    val rowRanges = mutableListOf<Pair<IntRange, Int>>()
    val actualNumber = StringBuilder()
    var beginning = -1
    line.forEachIndexed { index, c ->
        if (c.isDigit()) actualNumber.append(c)
        if (c.isDigit() && !inNumber) {
            beginning = index
            inNumber = true
        }
        if ((!c.isDigit() || index == line.length - 1) && inNumber) {
            rowRanges.add(beginning..<index to actualNumber.toString().toInt())
            actualNumber.clear()
            inNumber = false
        }
    }
    rowRanges
}

fun engineSum(lines: List<String>): Int {
    val emptyLine = ".".repeat(lines[0].length + 2)
    return lines
        .mapIndexed { id, line ->
            fun extendLine(line: String) = StringBuilder(".").append(line).append(".").toString()
            val above = if (id == 0) emptyLine else extendLine(lines[id - 1])
            val below = if (id == lines.size - 1) emptyLine else extendLine(lines[id + 1])
            processLine(extendLine(line), above, below)
        }
        .flatten()
        .sum()
}

fun processLine(line: String, above: String, below: String): List<Int> {
    val lineNumbers = mutableListOf<Int>()
    val actualNumber = StringBuilder()
    var wasSymbol = false
    line.forEachIndexed() { position, char ->
        val charAbove = above[position]
        val charBelow = below[position]
        fun isSymbol(char: Char) = !char.isDigit() && char != '.'
        fun hasSymbol() = isSymbol(char) || isSymbol(charAbove) || isSymbol(charBelow)
        if (!char.isDigit() && (actualNumber.isNotEmpty() && (wasSymbol || hasSymbol()))) lineNumbers.add(
            actualNumber.toString().toInt()
        )
        if (char.isDigit()) actualNumber.append(char)
        if (!char.isDigit()) actualNumber.clear()
        if (hasSymbol()) wasSymbol = true
        if (char == '.' && !isSymbol(charAbove) && !isSymbol(charBelow)) wasSymbol = false
    }
    return lineNumbers
}
