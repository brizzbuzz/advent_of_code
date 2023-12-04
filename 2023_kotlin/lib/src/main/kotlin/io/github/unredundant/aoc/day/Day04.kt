package io.github.unredundant.aoc.day

import kotlin.math.pow

object Day04 : Day<Int, Int> {
  override val calendarDate: Int = 4

  private val cards: List<Card> = input.lines().map { it.toCard() }

  override fun silver(): Int = cards.sumOf { it.score }

  override fun gold(): Int = cards.fold(emptyMap<Int, Int>()) { acc, card ->
    val currentCard = acc.getOrDefault(card.id, 0) + 1
    val cardClones = (1..card.winnerCount).map { i -> acc.getOrDefault(card.id + i, 0) + currentCard }
    acc + (card.id to currentCard) + cardClones.mapIndexed { i, v -> card.id + i + 1 to v }
  }.values.sum()

  private fun String.toCard(): Card {
    val (idStr, numbers) = split(": ")
    val id = idStr.split(" ").last().toInt()
    val (winStr, drawStr) = numbers.split(" | ")
    val winningNumbers = winStr.trim().split(" ").filterNot { it.isBlank() }.map { it.toInt() }
    val drawnNumbers = drawStr.trim().split(" ").filterNot { it.isBlank() }.map { it.toInt() }
    return Card(id, winningNumbers, drawnNumbers)
  }

  data class Card(val id: Int, val winningNumbers: List<Int>, val drawnNumbers: List<Int>) {
    val winnerCount = drawnNumbers.count { it in winningNumbers }
    val score: Int = when (winnerCount) {
      0 -> 0
      1 -> 1
      else -> 1.doubleNTimes(winnerCount - 1)
    }
  }

  private fun Int.doubleNTimes(n: Int): Int = this * 2.0.pow(n).toInt()
}
