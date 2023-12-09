package io.github.unredundant.aoc.day

object Day08 : Day<Long, Long> {
  override val calendarDate: Int = 8
  private val instructions = input.lines().first().split("").filterNot { it.isBlank() }.map {
    when (it) {
      "L" -> Direction.LEFT
      "R" -> Direction.RIGHT
      else -> error("Invalid Direction")
    }
  }

  private val nodes = input.split("\n\n").last().lines().map {
    val (id, lr) = it.split(" = ")
    val (l, r) = lr.replace(Regex("[()]"), "").split(", ")
    Node(id, l, r)
  }

  private val nodeMap: Map<String, Node> = nodes.associateBy { it.id }

  private enum class Direction {
    LEFT,
    RIGHT
  }

  private data class Node(val id: String, val leftId: String, val rightId: String)

  override fun silver(): Long {
    var currNode = nodeMap["AAA"]!!
    var steps = 0L
    while (currNode.id != "ZZZ") {
      val instruction = instructions[(steps % instructions.size).toInt()]
      steps += 1
      currNode = when (instruction) {
        Direction.LEFT -> nodeMap[currNode.leftId]!!
        Direction.RIGHT -> nodeMap[currNode.rightId]!!
      }
    }
    return steps
  }

  override fun gold(): Long {
    val currNodes = nodeMap.keys.filter { it.endsWith("A") }.map { nodeMap[it]!! }
    val idk = currNodes.map { it.stepToZs(instructions) }
    return lcm(*(idk.flatten().toLongArray()))
  }

  private fun Node.stepToZs(instructions: List<Direction>): List<Long> {
    val startPoints = mutableListOf<Node>()
    val zSteps = mutableListOf<Long>()
    var currNode = this
    var steps = 0L
    while (!startPoints.contains(currNode)) {
      startPoints.add(currNode)
      instructions.forEach { i ->
        steps++
        currNode = when (i) {
          Direction.LEFT -> nodeMap[currNode.leftId]!!
          Direction.RIGHT -> nodeMap[currNode.rightId]!!
        }
        if (currNode.id.endsWith("Z")) {
          zSteps.add(steps)
        }
      }
    }
    return zSteps
  }

  private fun gcd(a: Long, b: Long): Long {
    var num1 = a
    var num2 = b
    while (num2 != 0L) {
      val temp = num2
      num2 = num1 % num2
      num1 = temp
    }
    return num1
  }

  private fun lcm(vararg numbers: Long): Long {
    var result = numbers[0]
    for (i in 1 until numbers.size) {
      result = result * numbers[i] / gcd(result, numbers[i])
    }
    return result
  }
}
