package io.github.unredundant.aoc.day

typealias CardGroups = Map<Day07.Card, Int>

object Day07 : Day<Int, Int> {
  override val calendarDate: Int = 7

  private val silverHands = input.lines().map { it.toHand() }
  private val goldHands = input.replace('J', 'W').lines().map { it.toHand() }

  override fun silver(): Int = silverHands.sorted().mapIndexed { i, h -> (i + 1) * h.bid }.sum()

  override fun gold(): Int = goldHands.sorted().mapIndexed { i, h -> (i + 1) * h.bid }.sum()

  private fun String.toHand(): Hand {
    val (cardStr, bidStr) = split(" ")
    val bid = bidStr.toInt()
    val cards = cardStr.map { Card.fromCode(it) }
    return Hand(cards, bid)
  }

  data class Hand(val cards: List<Card>, val bid: Int) : Comparable<Hand> {
    private val handType: HandType = HandType.determineType(cards)

    override fun compareTo(other: Hand): Int {
      if (handType.strength < other.handType.strength) {
        return -1
      }
      if (handType.strength > other.handType.strength) {
        return 1
      }

      val zippedCards = cards.zip(other.cards)

      val firstDifferent = zippedCards.firstOrNull { (a, b) -> a != b }

      if (firstDifferent == null) return 0

      val (r, l) = firstDifferent

      return if (r.strength > l.strength) 1 else -1
    }
  }

  sealed interface HandType {
    val strength: Int

    companion object {
      fun determineType(cards: List<Card>): HandType {
        val cardGroups = cards.group()
        return when {
          cardGroups.canBe(5) -> FiveOfAKind(cardGroups.highestCount())
          cardGroups.canBe(4) -> FourOfAKind(cardGroups.highestCount())
          cardGroups.canBeFullHouse() -> {
            val (triple, pair) = cardGroups.fullHouse()
            FullHouse(triple, pair)
          }

          cardGroups.canBe(3) -> ThreeOfAKind(cardGroups.highestCount())
          cardGroups.canBeTwoPair() -> {
            val (first, second) = cardGroups.twoPair()
            TwoPair(first, second)
          }

          cardGroups.canBe(2) -> OnePair(cardGroups.highestCount())
          else -> HighCard(cardGroups.highCard())
        }
      }
    }
  }

  data class FiveOfAKind(val kind: Card) : HandType {
    override val strength: Int = 7
  }

  data class FourOfAKind(val kind: Card) : HandType {
    override val strength: Int = 6
  }

  data class FullHouse(val triple: Card, val pair: Card) : HandType {
    override val strength: Int = 5
  }

  data class ThreeOfAKind(val kind: Card) : HandType {
    override val strength: Int = 4
  }

  data class TwoPair(val first: Card, val second: Card) : HandType {
    override val strength: Int = 3
  }

  data class OnePair(val kind: Card) : HandType {
    override val strength: Int = 2
  }

  data class HighCard(val kind: Card) : HandType {
    override val strength: Int = 1
  }

  private fun List<Card>.group(): CardGroups = groupBy { it }.mapValues { (_, v) -> v.count() }
  private fun CardGroups.highestCount(): Card = when (wildcardCount) {
    0 -> maxBy { (_, v) -> v }.key
    else -> {
      val filteredAndOrdered = filterNot { it.key == Card.WILDCARD }.toSortedMap()
      val maxInFiltered = filteredAndOrdered.maxByOrNull { it.value }?.value ?: 0
      val filteredAgain = filteredAndOrdered.filter { it.value == maxInFiltered }
      filteredAgain.keys.firstOrNull() ?: Card.ACE
    }
  }

  private val CardGroups.wildcardCount: Int
    get() = getOrDefault(Card.WILDCARD, 0)

  private fun CardGroups.fullHouse(): Pair<Card, Card> = when (wildcardCount) {
    0 -> {
      val triple = firstNotNullOf { (k, v) -> if (v == 3) k else null }
      val pair = firstNotNullOf { (k, v) -> if (v == 2) k else null }
      Pair(triple, pair)
    }

    else -> {
      val filteredAndOrdered = filterNot { it.key == Card.WILDCARD }.toSortedMap()
      Pair(filteredAndOrdered.keys.toList()[0]!!, filteredAndOrdered.keys.toList()[1]!!)
    }
  }

  private fun CardGroups.canBe(n: Int): Boolean = any { (k, v) ->
    if (k == Card.WILDCARD) v == n
    else wildcardCount + v == n
  }

  private fun CardGroups.canBeFullHouse(): Boolean {
    if (wildcardCount == 0 && size == 2 && 2 in values && 3 in values) return true
    val (cards, _) = partitionToMap { (k, _) -> k != Card.WILDCARD }
    if (cards.count() == 2 && cards.all { (_, v) -> v == 2 } && wildcardCount == 1) return true
    if (cards.count() == 2 && cards.any { (_, v) -> v == 3 } && wildcardCount == 1) return true
    if (cards.canBe(2) && wildcardCount == 2) return true
    return false
  }

  private fun <K, V> Map<K, V>.partitionToMap(predicate: (Pair<K, V>) -> Boolean): Pair<Map<K, V>, Map<K, V>> =
    toList().partition(predicate).let { (c, w) -> Pair(c.toMap(), w.toMap()) }

  private fun CardGroups.canBeTwoPair(): Boolean {
    if (wildcardCount == 0 && size == 3 && values.count { it == 2 } == 2) return true
    val (cards, wildcards) = partitionToMap { (k, _) -> k != Card.WILDCARD }
    if (cards.size >= 4 || wildcards.isEmpty()) return false
    if (cards.canBe(2) && wildcardCount >= 1) return true
    if (wildcardCount >= 2) return true
    return false
  }

  private fun CardGroups.twoPair(): Pair<Card, Card> {
    val (first, second) = filterValues { it == 2 }.keys.toList()
    return Pair(first, second)
  }

  private fun CardGroups.highCard(): Card = keys.maxBy { it.strength }

  enum class Card(val strength: Int, private val code: Char) {
    ACE(14, 'A'),
    KING(13, 'K'),
    QUEEN(12, 'Q'),
    JACK(11, 'J'),
    TEN(10, 'T'),
    NINE(9, '9'),
    EIGHT(8, '8'),
    SEVEN(7, '7'),
    SIX(6, '6'),
    FIVE(5, '5'),
    FOUR(4, '4'),
    THREE(3, '3'),
    TWO(2, '2'),
    WILDCARD(1, 'W');

    companion object {
      fun fromCode(code: Char) = Card.entries.find { it.code == code } ?: error("Illegal Card Code: $code")
    }
  }
}
