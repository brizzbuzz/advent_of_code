package io.github.unredundant.aoc.day

object Day04 : Day<Int, Int> {
  override val calendarDate: Int = 4

  override fun silver(): Int = input.parseInput().count { (a, b) -> a.contains(b) || b.contains(a) }

  override fun gold(): Int = input.parseInput().count { (a, b) -> a.overlaps(b) || b.overlaps(a) }

  private data class Assignment(val lower: Int, val upper: Int) {
    fun contains(other: Assignment): Boolean = other.lower in lower..upper && other.upper in lower..upper
    fun overlaps(other: Assignment): Boolean = other.lower in lower..upper || other.upper in lower..upper
  }

  private fun String.parseInput() = lines().map { it.split(",") }
    .map { (a, b) -> listOf(a.toAssignment(), b.toAssignment()) }

  private fun String.toAssignment() = split("-").let { (a, b) -> Assignment(a.toInt(), b.toInt()) }
}
