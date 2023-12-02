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
    red = findByColor("red"),
    green = findByColor("green"),
    blue = findByColor("blue")
  )

  private fun List<String>.findByColor(color: String) = find { it.contains(color) }?.findCount()?.trim()?.toInt() ?: 0

  private fun String.findCount() = split(":").last().replace(Regex("[a-zA-Z]"), "")
  data class Game(val id: Int, val rounds: List<Round>)
  data class Round(val red: Int, val green: Int, val blue: Int)

  private fun golf() {
    val re = Regex("[a-zA-Z; ]")
    val b = input.lines()
      .asSequence()
      .map { it.split(":") }
      .map { (i, r) -> i.replace("Game", "").trim().toInt() to r.split(Regex("[,;]")).map { it.trim() } }
      .map { (i, r) ->
        i to Triple(
          r.filter { c -> c.contains("red") }.maxOfOrNull { c -> c.replace(re, "").toInt() } ?: 0,
          r.filter { c -> c.contains("green") }.maxOfOrNull { c -> c.replace(re, "").toInt() } ?: 0,
          r.filter { c -> c.contains("blue") }.maxOfOrNull { c -> c.replace(re, "").toInt() } ?: 0
        )
      }
    val s = b.filter { (_, c) -> c.first <= 12 && c.second <= 13 && c.third <= 14 }.sumOf { (i, _) -> i }
    val g = b.sumOf { (_, c) -> c.first * c.second * c.third }
    println("s: $s")
    println("g: $g")
  }
}
