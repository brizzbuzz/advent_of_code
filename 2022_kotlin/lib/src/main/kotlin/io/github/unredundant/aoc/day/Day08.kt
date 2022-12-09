package io.github.unredundant.aoc.day

object Day08 : Day<Int, Int> {
  override val calendarDate: Int = 8

  override fun silver(): Int = input.toGrid().countVisible()

  override fun gold(): Int = input.toGrid().highestScenicScore()

  private data class SightLines(
    val top: List<Int>,
    val bottom: List<Int>,
    val left: List<Int>,
    val right: List<Int>
  )

  private fun String.toGrid(): List<List<Int>> = lines().map { it.toCharArray().map { c -> c.toString().toInt() } }

  private fun SightLines.isEdge() = top.isEmpty() || bottom.isEmpty() || left.isEmpty() || right.isEmpty()

  private fun List<List<Int>>.toSightLines(x: Int, y: Int): SightLines {
    val row = this[y]
    val col = this.map { it[x] }
    val left = row.subList(0, x).reversed()
    val right = row.subList(x + 1, row.size)
    val top = col.subList(0, y).reversed()
    val bottom = col.subList(y + 1, col.size)
    return SightLines(top, bottom, left, right)
  }

  private fun List<List<Int>>.isVisible(x: Int, y: Int): Boolean {
    val height = this[y][x]
    val sightLines = toSightLines(x, y)
    return if (sightLines.isEdge()) {
      true
    } else {
      height > sightLines.top.max()
        || height > sightLines.bottom.max()
        || height > sightLines.left.max()
        || height > sightLines.right.max()
    }
  }

  private fun List<List<Int>>.calculateScenicScore(x: Int, y: Int): Int {
    val height = this[y][x]
    val sightLines = toSightLines(x, y)
    val left = sightLines.left.sightDistance(height)
    val right = sightLines.right.sightDistance(height)
    val top = sightLines.top.sightDistance(height)
    val bottom = sightLines.bottom.sightDistance(height)
    return left * right * top * bottom
  }

  private fun List<Int>.sightDistance(height: Int) = indexOfFirst { it >= height }
    .let { if (it != -1) it + 1 else size }

  private fun List<List<Int>>.countVisible(): Int = this.mapIndexed { y, row ->
    List(row.size) { x -> if (isVisible(x, y)) 1 else 0 }.sum()
  }.sum()

  private fun List<List<Int>>.highestScenicScore(): Int = this.mapIndexed { y, row ->
    List(row.size) { x -> calculateScenicScore(x, y) }.max()
  }.max()
}
