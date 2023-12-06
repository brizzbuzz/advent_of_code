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

  override fun silver(): Long =
    silverRaces.map { it.calculateRecordBreakingScores() }.map { it.count() }.fold(1) { acc, i -> acc * i }

  override fun gold(): Long {
    val a = goldRace.let { r ->
      LongRange(0, r.time).find { t ->
        val result = (t * r.time) - (t * t)
        result > r.record
      }
    }!!

    val b = goldRace.let { r ->
      LongRange(0, r.time).reversed().find { t ->
        val result = (t * r.time) - (t * t)
        result > r.record
      }
    }!!

    return b - a + 1
  }

  private fun Race.calculateRecordBreakingScores(): List<Long> = (0..time).fold(emptyList()) { acc, t ->
    val speed = time - t
    val travelTime = time - speed
    val distance = speed * travelTime

    if (distance > record) {
      acc + distance
    } else {
      acc
    }
  }

  data class Race(val time: Long, val record: Long)
}
