package io.github.unredundant.aoc.day

object Day03 : Day<Int, Int> {

  override val calendarDate: Int = 3

  private val sanitizedInput = let {
    val lineLength = input.lines().first().length
    val padStr = ".".repeat(lineLength)
    padStr.plus("\n").plus(input).plus("\n").plus(padStr)
      .lines()
      .joinToString("\n") { ".$it." }
      .replace("-", "_")
  }

  private val windows = sanitizedInput.lines().windowed(3).map { it.toWindow() }

  override fun silver(): Int = windows.sumOf { w ->
    val initial = Accumulator(0, 0)
    val result = w.middle.fold(initial) { acc, _ ->
      foldWrapper(acc, w) { a, bw, n, nl ->
        if (bw.containsSymbol) {
          a.increment(nl, a.value + n)
        } else {
          a.increment(nl)
        }
      }
    }
    result.value
  }

  override fun gold(): Int = windows.mapIndexed { y, w ->
    val initial = Accumulator(0, emptyList<GearComponent>())
    val result = w.middle.fold(initial) { acc, _ ->
      foldWrapper(acc, w) { a, bw, n, nl ->
        if (bw.containsSymbol) {
          a.increment(nl, a.value + bw.gearCoordinates(a.index, y).map { GearComponent(it, n) })
        } else {
          a.increment(nl)
        }
      }
    }
    result.value
  }
    .flatten()
    .groupBy { it.coordinate }
    .mapValues { (_, v) -> v.map { it.score } }
    .filterValues { it.size == 2 }
    .mapValues { (_, v) -> v[0] * v[1] }
    .values
    .sum()

  private fun <A> foldWrapper(
    a: Accumulator<A>,
    w: Window,
    f: (Accumulator<A>, Window, Int, Int) -> Accumulator<A>
  ): Accumulator<A> {
    val (i, _) = a

    if (i >= w.middle.length) {
      return a
    }

    var nl = 0
    while (w.middle[i + nl] in '0'..'9') {
      nl++
    }

    if (nl == 0) {
      return a.increment()
    }

    val bw = w.toBorderWindow(i, nl)
    val n = w.middle.substring(i, i + nl).toInt()

    return f(a, bw, n, nl)
  }

  data class Accumulator<T>(val index: Int, val value: T) {
    fun increment(i: Int = 1, v: T = value) = Accumulator(index + i, v)
  }

  private val Window.containsSymbol: Boolean
    get() = listOf('#', '$', '%', '&', '*', '+', '-', '_', '/', '=', '@').any { s ->
      top.contains(s) || middle.contains(
        s
      ) || bottom.contains(s)
    }

  private fun Window.gearCoordinates(startX: Int, startY: Int): List<Coordinate> =
    top.identifyGearCoordinates(startX, startY) +
      middle.identifyGearCoordinates(startX, startY + 1) +
      bottom.identifyGearCoordinates(startX, startY + 2)

  private fun String.identifyGearCoordinates(startX: Int, startY: Int): List<Coordinate> =
    mapIndexedNotNull { index, c ->
      if (c == '*') {
        Coordinate(startX + index, startY)
      } else {
        null
      }
    }

  private fun Window.toBorderWindow(start: Int, length: Int): Window {
    val topSubstring = top.substring(start - 1, start + length + 1)
    val middleSubstring = middle.substring(start - 1, start + length + 1)
    val bottomSubstring = bottom.substring(start - 1, start + length + 1)
    return Window(
      top = topSubstring,
      middle = middleSubstring,
      bottom = bottomSubstring,
    )
  }

  private fun List<String>.toWindow() = Window(top = first(), middle = get(1), bottom = last())
  data class Window(val top: String, val middle: String, val bottom: String)
  data class Coordinate(val x: Int, val y: Int)
  data class GearComponent(val coordinate: Coordinate, val score: Int)
}
