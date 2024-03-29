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
  private fun List<String>.toGames() = map { it.toGame() }
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

  fun golf() {
    val f:(List<String>,String)->Int={r,a->r.filter{c->c.contains(a)}.maxOfOrNull{c->c.replace(Regex("[a-zA-Z; ]"),"").toInt()}?:0}
    val b=input.lines().map{it.split(":")}.map{(i,r)->i.replace("Game","").trim().toInt() to r.split(Regex("[,;]")).map{it.trim()}}.map{(i, r)->i to Triple(f(r,"red"),f(r,"green"),f(r, "blue"))}
    val s=b.filter{(_, c)->c.first<=12&&c.second<=13&&c.third<=14}.sumOf{(i, _)->i}
    val g=b.sumOf{(_, c)->c.first*c.second*c.third}
    println("s:$s|g:$g")
  }
}
