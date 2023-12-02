package io.github.unredundant.aoc.day

object Day02 : Day<Int, Int> {
  override val calendarDate: Int = 2

  override fun silver(): Int = games.filter { g ->
    val (mr, mg, mb) = g.maxCubeByColor()
    mr <= 12 && mg <= 13 && mb <= 14
  }.sumOf { it.id }

  override fun gold(): Int = games.sumOf { g ->
    val (mr, mg, mb) = g.maxCubeByColor()
    mr * mg * mb
  }

  private val games: List<Game> = input.lines().toGames()
  private fun List<String>.toGames() = map { it.toGame() }.map { (id, rounds) -> Game(id, rounds) }
  private fun String.toGame() = Game(getGamePrefix(), dropGamePrefix().toRounds())

  private fun Game.maxCubeByColor() = Triple(
    rounds.maxOf { it.red },
    rounds.maxOf { it.green },
    rounds.maxOf { it.blue }
  )

  private fun String.dropGamePrefix() = split(":").last()
  private fun String.getGamePrefix() = split(":").first().replace("Game", "").trim().toInt()
  private fun String.toRounds() = split(";").map { it.split(",").toRound() }
  private fun List<String>.toRound() = Round(
    find { it.contains("red") }?.findCount()?.trim()?.toInt() ?: 0,
    find { it.contains("green") }?.findCount()?.trim()?.toInt() ?: 0,
    find { it.contains("blue") }?.findCount()?.trim()?.toInt() ?: 0
  )

  private fun String.findCount() = split(":").last().replace(Regex("[a-zA-Z]"), "")
  data class Game(val id: Int, val rounds: List<Round>)
  data class Round(val red: Int, val green: Int, val blue: Int)
}
