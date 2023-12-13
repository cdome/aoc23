package day09

import java.io.File

fun main() {
    val tree = File("src/main/resources/day09-oasis").readLines().map { line ->
        var values = line.split(" ").map { it.toInt() }
        val valTree = mutableListOf(values)
        while (!values.all { it == 0 }) {
            values = (0..values.size - 2).map { i -> values[i + 1] - values[i] }
            valTree.add(values)
        }
        valTree
    }

    println("Sum of predictions: ${tree.flatMap { it.map { it.last() } }.sum()}")
    println("Sum of past values: ${tree.sumOf { it.map { it.first() }.reduceRight { i, acc -> i - acc } }}")
}