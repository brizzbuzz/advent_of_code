package io.github.unredundant.aoc.day

object Day06 : Day<Int, Int> {
  private const val START_PACKET_SIZE = 4
  private const val START_MESSAGE_SIZE = 14
  override val calendarDate: Int = 6

  override fun silver(): Int = input.firstUniqueSubstringOfSize(START_PACKET_SIZE)

  override fun gold(): Int = input.firstUniqueSubstringOfSize(START_MESSAGE_SIZE)

  private fun String.firstUniqueSubstringOfSize(size: Int) = windowed(size)
    .mapIndexed { i, s -> Pair(i, s.toSet()) }
    .first { it.second.size == size }
    .let { it.first + size }
}
