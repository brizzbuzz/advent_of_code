package io.github.unredundant.aoc.day

import io.github.unredundant.aoc.util.Util.getInput

object Day05 : Day<Int, Int> {
  override val calendarDate: Int = 5

  override fun silver(): Int = getInput(5).lines().filter { it.isNiceSilver() }.count()

  override fun gold(): Int = getInput(5).lines().filter { it.isNiceGold() }.count()

  private fun String.containsAtLeastThreeVowels(): Boolean = count { it in "aeiou" } >= 3
  private fun String.containsAtLeastOneDoubleLetter(): Boolean = windowed(2).any { it[0] == it[1] }
  private fun String.doesNotContainBadWords(): Boolean = !contains("ab|cd|pq|xy".toRegex())
  private fun String.isNiceSilver() =
    containsAtLeastThreeVowels() && containsAtLeastOneDoubleLetter() && doesNotContainBadWords()

  fun String.twoCharsOccurAtLeastTwice(): Boolean = windowed(2).mapIndexed { i, s ->
    substring(i + 2).windowed(2).contains(s)
  }.any { it }

  fun String.containsAtLeastOneLetterSandwich() = windowed(3).any { it[0] == it[2] }

  fun String.isNiceGold() = twoCharsOccurAtLeastTwice() && containsAtLeastOneLetterSandwich()
}
