package io.github.unredundant.aoc.day

private typealias History = List<Long>

object Day09 : Day<Long, Long> {
  override val calendarDate: Int = 9

  override fun silver(): Long = report.map { it.formulateDeltas(listOf(it)) }
    .sumOf { it.foldRight(0L) { h, acc -> (h.lastOrNull() ?: 0) + acc } }

  override fun gold(): Long = report.map { it.formulateDeltas(listOf(it)) }
    .sumOf { it.foldRight(0L) { h, acc -> (h.firstOrNull() ?: 0) - acc } }

  private val report: List<History> = input.lines().map {
    it.split(" ").filterNot { v -> v.isBlank() }.map { v -> v.toLong() }
  }

  private tailrec fun History.formulateDeltas(acc: List<History>): List<History> {
    val result: History = windowed(2).map { (a, b) ->
      val delta = maxOf(a, b) - minOf(a, b)
      if (a < b) delta else -delta
    }
    val newAcc = acc + listOf(result)
    if (result.all { it == 0L }) return newAcc
    return result.formulateDeltas(newAcc)
  }
}
