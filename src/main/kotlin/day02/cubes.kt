package day02

import java.io.File

data class Game(
    val number: Int,
    val hands: List<Hand>
) {
    fun maxRed() = hands.maxOfOrNull { it.red } ?: 0
    fun maxBlue() = hands.maxOfOrNull { it.blue } ?: 0
    fun maxGreen() = hands.maxOfOrNull { it.green } ?: 0
}

data class Hand(val red: Int, val blue: Int, val green: Int)

fun main() {
    val games = gamesFromFile("src/main/resources/day02-cubes")
    println("Playable games: " + games.filter { isPlayable(it, 12, 14, 13) }.sumOf { it.number })
    println("Total power: " + games.sumOf { game -> game.hands.maxOf { it.red } * game.hands.maxOf { it.blue } * game.hands.maxOf { it.green } })
}

fun isPlayable(game: Game, reds: Int, blues: Int, greens: Int) =
    reds >= game.maxRed() && blues >= game.maxBlue() && greens >= game.maxGreen()

private fun gamesFromFile(fileName: String) = File(fileName).readLines().map { line ->
    val (number, hands) = line.split(":").map { it.trim() }
    Game(
        number = number.split(" ")[1].toInt(),
        hands = hands.split(";").map { it.trim() }.map { hand ->
            hand.split(", ").associate { cube -> cube.split(" ").run { this[1] to this[0].toInt() } }.let { cubesMap ->
                Hand(cubesMap["red"] ?: 0, cubesMap["blue"] ?: 0, cubesMap["green"] ?: 0)
            }
        }
    )
}
