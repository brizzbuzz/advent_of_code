package io.github.unredundant.aoc.day

object Day06 : Day<Long, Long> {
  override val calendarDate: Int = 6

  private val baseParse: String.() -> List<String> =
    { split(": ").last().split(Regex("\\s+")).filterNot { it.isBlank() } }

  private val silverRaces: List<Race>
    get() {
      val times = input.lines().first().baseParse().map { it.toLong() }
      val records = input.lines().last().baseParse().map { it.toLong() }
      return times.zip(records).map { Race(it.first, it.second) }
    }

  private val goldRace: Race
    get() {
      val time = input.lines().first().baseParse().joinToString(separator = "").toLong()
      val record = input.lines().last().baseParse().joinToString(separator = "").toLong()
      return Race(time, record)
    }

  override fun silver(): Long = silverRaces.map { it.numberOfRecordBreakingScores() }.fold(1) { acc, i -> acc * i }
  override fun gold(): Long = goldRace.numberOfRecordBreakingScores()

  private fun Race.numberOfRecordBreakingScores(): Long {
    val range = LongRange(0, time)
    val a = findIntersection(range)
    val b = findIntersection(range.reversed())

    return b - a + 1
  }

  private fun Race.findIntersection(range: LongProgression): Long {
    return range.find { t ->
      val result = (t * time) - (t * t)
      result > record
    }!!
  }

  data class Race(val time: Long, val record: Long)
}
