package io.github.unredundant.aoc.day

import io.github.unredundant.aoc.util.Util

object Day02 : Day<Int, Int> {
  override val calendarDate: Int = 2

  override fun silver() = Util.getInput(2)
    .convertSilverInput()
    .calculateScore()


  override fun gold() = Util.getInput(2)
    .convertGoldInput()
    .calculateScore()

  private enum class Play {
    ROCK,
    PAPER,
    SCISSORS;

    fun losesTo() = when (this) {
      ROCK -> PAPER
      PAPER -> SCISSORS
      SCISSORS -> ROCK
    }

    fun winsAgainst() = when (this) {
      ROCK -> SCISSORS
      PAPER -> ROCK
      SCISSORS -> PAPER
    }
  }

  private enum class Result {
    WIN,
    LOSS,
    DRAW
  }

  private data class Round(val elf: Play, val me: Play) {
    val result = when {
      elf == me -> Result.DRAW
      elf == Play.ROCK && me == Play.PAPER -> Result.WIN
      elf == Play.ROCK && me == Play.SCISSORS -> Result.LOSS
      elf == Play.PAPER && me == Play.ROCK -> Result.LOSS
      elf == Play.PAPER && me == Play.SCISSORS -> Result.WIN
      elf == Play.SCISSORS && me == Play.ROCK -> Result.WIN
      elf == Play.SCISSORS && me == Play.PAPER -> Result.LOSS
      else -> throw IllegalStateException("Invalid round")
    }
  }

  private fun String.convertSilverInput(): List<Round> = lines().map { line ->
    val (p1, p2) = line.split(" ")
    Round(
      elf = when (p1) {
        "A" -> Play.ROCK
        "B" -> Play.PAPER
        "C" -> Play.SCISSORS
        else -> throw IllegalArgumentException("Invalid play: $p1")
      },
      me = when (p2) {
        "X" -> Play.ROCK
        "Y" -> Play.PAPER
        "Z" -> Play.SCISSORS
        else -> throw IllegalArgumentException("Invalid play: $p2")
      }
    )
  }

  private fun String.convertGoldInput(): List<Round> = lines().map { line ->
    val (p1, p2) = line.split(" ")
    val elfPlay = when (p1) {
      "A" -> Play.ROCK
      "B" -> Play.PAPER
      "C" -> Play.SCISSORS
      else -> throw IllegalArgumentException("Invalid play: $p1")
    }
    Round(
      elf = elfPlay,
      me = when (p2) {
        "X" -> elfPlay.winsAgainst()
        "Y" -> elfPlay
        "Z" -> elfPlay.losesTo()
        else -> throw IllegalArgumentException("Invalid play: $p2")
      }
    )
  }

  private fun List<Round>.calculateScore() = map { round ->
    val outcomeBonus = when (round.result) {
      Result.WIN -> 6
      Result.LOSS -> 0
      Result.DRAW -> 3
    }
    val playBonus = when (round.me) {
      Play.ROCK -> 1
      Play.PAPER -> 2
      Play.SCISSORS -> 3
    }
    outcomeBonus + playBonus
  }.sum()
}
