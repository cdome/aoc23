package day05

import java.io.File

val seeds = mutableListOf<Long>()
val seedToSoil = mutableListOf<Pair<Long, LongRange>>()
val soilToFertilizer = mutableListOf<Pair<Long, LongRange>>()
val fertilizerToWater = mutableListOf<Pair<Long, LongRange>>()
val waterToLight = mutableListOf<Pair<Long, LongRange>>()
val lightToTemperature = mutableListOf<Pair<Long, LongRange>>()
val temperatureToHumidity = mutableListOf<Pair<Long, LongRange>>()
val humidityToLocation = mutableListOf<Pair<Long, LongRange>>()

val maps = listOf(
    seedToSoil,
    soilToFertilizer,
    fertilizerToWater,
    waterToLight,
    lightToTemperature,
    temperatureToHumidity,
    humidityToLocation
)

fun main() {
    val file = File("src/main/resources/day05-seedmap").readLines()
    var state = ""
    file.filter { it.isNotBlank() }.forEach { line ->
        when {
            line.startsWith("seeds") -> seeds.addAll(line.split(":")[1].trim().split(" ").map { it.toLong() })
            line.startsWith("seed-to-soil map") -> state = "seed-to-soil"
            line.startsWith("soil-to-fertilizer map") -> state = "soil-to-fertilizer"
            line.startsWith("fertilizer-to-water map") -> state = "fertilizer-to-water"
            line.startsWith("water-to-light map") -> state = "water-to-light"
            line.startsWith("light-to-temperature map") -> state = "light-to-temperature"
            line.startsWith("temperature-to-humidity map") -> state = "temperature-to-humidity"
            line.startsWith("humidity-to-location map") -> state = "humidity-to-location"
            else -> {
                val map = line.trim().split(" ").map { it.trim().toLong() }
                val row = Pair(map[0], LongRange(map[1], map[1] + map[2] - 1))
                when (state) {
                    "seed-to-soil" -> seedToSoil.add(row)
                    "soil-to-fertilizer" -> soilToFertilizer.add(row)
                    "fertilizer-to-water" -> fertilizerToWater.add(row)
                    "water-to-light" -> waterToLight.add(row)
                    "light-to-temperature" -> lightToTemperature.add(row)
                    "temperature-to-humidity" -> temperatureToHumidity.add(row)
                    "humidity-to-location" -> humidityToLocation.add(row)
                }
            }
        }
    }
    seeds.minOfOrNull { seed ->
        var currentPosition = seed
        maps.indices.forEach { index -> currentPosition = dest(currentPosition, maps[index]) }
        currentPosition
    }.let { println("Closest seed [Single]: $it") }


    val seedRanges = (0..<seeds.size / 2).map { seeds[it * 2]..<seeds[it * 2] + seeds[1 + it * 2] }
    var processed = 0
    val start = System.currentTimeMillis()
    var tickStart = start
    var tickProcessed = 0
    println("Seed ranges: $seedRanges")
    val totalSeeds = seedRanges.sumOf { it.count() }
    println("All seeds: $totalSeeds")
    seedRanges.minOf { range ->
        range.minOf { seed ->

            var currentPosition = seed
            maps.indices.forEach { index -> currentPosition = dest(currentPosition, maps[index]) }
            processed++
                val curTIme = System.currentTimeMillis()
            if (curTIme - tickStart > 10000) {
                val perf = (processed - tickProcessed) / 10
                val remain = totalSeeds - processed
                val est = remain / perf
                val minutes = est / 60
                val secs = est % 60
                println("Performance: ${perf}/s, remaining ${remain} seeds, ETA: $minutes:$secs...")
                tickStart = curTIme
                tickProcessed = processed
            }
            currentPosition
        }
    }.let { println("Closest seed [Range]: $it") }
    println("Time: ${(System.currentTimeMillis() - start) / 1000}s")
}

fun dest(number: Long, map: List<Pair<Long, LongRange>>) = map
    .firstOrNull { (_, source) -> source.contains(number) }
    ?.let { (destination, source) -> destination + number - source.first }
    ?: number