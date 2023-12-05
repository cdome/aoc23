package day04

import java.io.File
import kotlin.math.pow

fun main() {
    val cards = parseFile()
    val cardsWorth = cards
        .sumOf { (winning, having) ->
            when (winning.size - (winning - having).size) {
                0 -> 0.0
                1 -> 1.0
                else -> 2.0.pow(winning.size - (winning - having).size - 1)
            }.toInt()
        }
    println("Cards worth: $cardsWorth")

    val cardsOwned = Array(cards.size) { 1 }
    cards.forEachIndexed { idx, (winning, having) ->
        val cardWorth = winning.size - (winning - having).size
        if (cardWorth > 0) (1..cardWorth).forEach { i -> cardsOwned[idx + i] += cardsOwned[idx] }
    }
    println("Cards owned: ${cardsOwned.sum()}")
}

private fun parseFile() = File("src/main/resources/day04-scratchcards").readLines()
    .map { line ->
        val (winning, having) = line.split(":")[1].split("|")
        winning.trim().split(" ").filter { it.isNotBlank() }.map { it.toDouble() }.toHashSet() to
                having.trim().split(" ").filter { it.isNotBlank() }.map { it.toDouble() }.toHashSet()
    }