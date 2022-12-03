package io.github.unredundant.aoc.day

object Day03 : Day<Int, Int> {
  override val calendarDate: Int = 3

  override fun silver(): Int = input.lines()
    .splitEachInHalf()
    .findCommonChars()
    .sumPriority()

  override fun gold(): Int = input.lines()
    .chunked(3)
    .map { (a, b, c) -> findCommonChars(a, b, c) }
    .sumPriority()

  private fun List<String>.splitEachInHalf() = map { it.splitInHalf() }
  private fun String.splitInHalf() = this.chunked(this.length / 2)
  private fun findCommonChars(s1: String, s2: String) = s1.toSet().intersect(s2.toSet())

  private fun findCommonChars(s1: String, s2: String, s3: String) =
    s1.toSet().intersect(s2.toSet()).intersect(s3.toSet())

  private fun List<List<String>>.findCommonChars() = map { (first, second) -> findCommonChars(first, second) }

  private fun List<Set<Char>>.sumPriority() = sumOf {
    it.sumOf { c -> c.priority() }
  }

  private fun Char.priority() = when (isUpperCase()) {
    true -> code - 38
    false -> code - 96
  }
}
