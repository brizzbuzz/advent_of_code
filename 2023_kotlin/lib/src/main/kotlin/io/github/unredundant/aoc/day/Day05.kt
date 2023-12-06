package io.github.unredundant.aoc.day

object Day05 : Day<Long, Long> {
  override val calendarDate: Int = 5

  private val serializedSources = input.lines().drop(2).joinToString(separator = "\n").split("\n\n")
  private val sources = serializedSources.map { it.toSource() }

  private val serializedSeeds = input.lines().first().split(": ").last().split(" ")
  private val silverSeeds = serializedSeeds.map { it.toLong() }
  private val goldSeeds = silverSeeds.chunked(2).map { (a, b) -> LongRange(a, a + b) }

  override fun silver(): Long = sources.fold(silverSeeds) { ids, s -> ids.map { id -> s.translate(id) } }.min()

  override fun gold(): Long {
    var step = 100000L
    var location = 1L

    do {
      location = reverseSearch(location - step, step)
      step /= 10
    } while (step != 1L)

    return reverseSearch(location, step)
  }

  private tailrec fun reverseSearch(location: Long, step: Long): Long {
    val seed = sources.reversed().fold(location) { acc, source -> source.searchLocation(acc) }

    if (goldSeeds.any { it.contains(seed) }) return location

    return reverseSearch(location + step, step)
  }

  private fun Source.searchLocation(location: Long): Long =
    mappings.firstOrNull { it.destinationStart <= location && location <= it.destinationStart + it.rangeLength }?.let {
      location - it.delta
    } ?: location

  data class Source(val mappings: List<Mapping>) {
    fun translate(id: Long): Long = mappings.find {
      it.sourceStart <= id && id <= it.sourceStart + it.rangeLength
    }?.let { id + it.delta } ?: id
  }

  data class Mapping(val destinationStart: Long, val sourceStart: Long, val rangeLength: Long) {
    val delta: Long = destinationStart - sourceStart
  }

  private fun String.toSource(): Source {
    val mappings = lines().drop(1).map { line ->
      val (drs, srs, rl) = line.split(" ").map { it.toLong() }
      Mapping(drs, srs, rl)
    }
    return Source(mappings)
  }
}
