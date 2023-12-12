package day07

import java.io.File

fun main() {
    val file = File("src/main/resources/day07-cards").readLines()
    file
        .map { line ->
            val (cards, bid) = line.split(" ")
            Hand(cards.trim(), bid.trim().toInt())
        }
        .sorted()
        .mapIndexed { order, hand -> (order + 1) * hand.bid }
        .sum()
        .let { println("Total: $it") }

    file
        .map { line ->
            val (cards, bid) = line.split(" ")
            HandWithJokers(cards.trim(), bid.trim().toInt())
        }
        .sorted()
        .mapIndexed { order, hand -> (order + 1) * hand.bid }
        .sum()
        .let { println("Total with Js: $it") }
}

data class Hand(val cards: String, val bid: Int) : Comparable<Hand> {
    private val suits = mutableMapOf<Char, Int>()

    init {
        cards.forEach { suits[it] = suits.getOrDefault(it, 0) + 1 }
    }

    private fun handValue() = when {
        suits.values.contains(5) -> 10
        suits.values.contains(4) -> 9
        suits.values.contains(3) && suits.values.contains(2) -> 8
        suits.values.contains(3) -> 7
        suits.values.filter { it == 2 }.size == 2 -> 6
        suits.values.contains(2) -> 5
        else -> 0
    }

    private fun suitValue(suit: Char) = when (suit) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 11
        'T' -> 10
        else -> suit.digitToInt()
    }

    override fun compareTo(other: Hand) = when {
        this.handValue() < other.handValue() -> -1
        this.handValue() > other.handValue() -> 1
        else -> {
            cards.indices.first { index -> cards[index] != other.cards[index] }.let {
                suitValue(cards[it]).compareTo(suitValue(other.cards[it]))
            }
        }
    }
}

data class HandWithJokers(val cards: String, val bid: Int) : Comparable<HandWithJokers> {
    val suits = mutableMapOf<Char, Int>()

    init {
        cards.forEach { suits[it] = suits.getOrDefault(it, 0) + 1 }
        if (suits.contains('J') && !suits.values.contains(5)) {
            val noJokes = suits.filterKeys { it != 'J' }
            val bestCard = noJokes
                .filter { it.value == noJokes.maxOf { it.value } }
                .maxBy { suitValue(it.key) }.key
            suits[bestCard] = suits[bestCard]!! + suits['J']!!
            suits.remove('J')
        }
    }

    private fun handValue() = when {
        suits.values.contains(5) -> 10
        suits.values.contains(4) -> 9
        suits.values.contains(3) && suits.values.contains(2) -> 8
        suits.values.contains(3) -> 7
        suits.values.filter { it == 2 }.size == 2 -> 6
        suits.values.contains(2) -> 5
        else -> 0
    }

    private fun suitValue(suit: Char) = when (suit) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 1
        'T' -> 10
        else -> suit.digitToInt()
    }

    override fun compareTo(other: HandWithJokers) = when {
        this.handValue() < other.handValue() -> -1
        this.handValue() > other.handValue() -> 1
        else -> {
            cards.indices.first { index -> cards[index] != other.cards[index] }.let {
                suitValue(cards[it]).compareTo(suitValue(other.cards[it]))
            }
        }
    }
}
