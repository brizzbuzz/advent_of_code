package io.github.unredundant.aoc.day

import io.github.unredundant.aoc.util.Util

object Day02 : Day<Int, Unit> {
  override val calendarDate: Int = 2

  override fun silver() = Util.getInput(2)
    .convertInput()
    .calculateScore()


  override fun gold() {
    TODO("Not yet implemented")
  }

  private enum class Play {
    ROCK,
    PAPER,
    SCISSORS
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

  private fun String.convertInput(): List<Round> = lines().map { line ->
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
