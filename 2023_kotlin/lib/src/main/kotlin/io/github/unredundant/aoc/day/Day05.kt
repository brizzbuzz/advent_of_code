package io.github.unredundant.aoc.day

object Day05 : Day<Long, Long> {
  override val calendarDate: Int = 5

  private val serializedSources = input.lines().drop(2).joinToString(separator = "\n").split("\n\n")
  private val sources = serializedSources.map { it.toSource() }

  private val serializedSeeds = input.lines().first().split(": ").last().split(" ")
  private val silverSeeds: Sequence<Long> = serializedSeeds.map { it.toLong() }.asSequence()
  private val goldSeeds: Sequence<LongRange> = silverSeeds.chunked(2).map { (a, b) -> LongRange(a, a + b) }

  override fun silver(): Long = sources.fold(silverSeeds) { ids, s -> ids.map { id -> s.translate(id) } }.min()

  override fun gold(): Long = reverseSearch()

  private fun Source.reverseSearch(location: Long): Long =
    mappings.firstOrNull { it.destinationStart <= location && location <= it.destinationStart + it.rangeLength }?.let {
      location - it.delta
    } ?: location

  private tailrec fun reverseSearch(location: Long = 1): Long {
    val seed = sources.reversed().fold(location) { acc, source -> source.reverseSearch(acc) }

    if (goldSeeds.any { it.contains(seed) }) return location

    return reverseSearch(location + 1)
  }

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
