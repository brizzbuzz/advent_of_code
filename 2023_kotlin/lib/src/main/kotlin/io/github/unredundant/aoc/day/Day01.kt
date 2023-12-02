package io.github.unredundant.aoc.day

object Day01 : Day<Int, Int> {
  override val calendarDate: Int = 1

  private val c = mapOf("one" to "o1e", "two" to "t2o", "three" to "t3e", "four" to "f4r", "five" to "f5e", "six" to "s6x", "seven" to "s7n", "eight" to "e8t", "nine" to "n9e")
  override fun silver(): Int = input.toResult()
  override fun gold(): Int = input.replaceCodes().toResult()
  private fun String.toResult() = replace(Regex("[a-zA-Z]"), "").lines().sumOf { "${it.first()}${it.last()}".toInt() }
  private fun String.replaceCodes() = c.entries.fold(this) { acc, (k, v) -> acc.replace(k, v) }
}
