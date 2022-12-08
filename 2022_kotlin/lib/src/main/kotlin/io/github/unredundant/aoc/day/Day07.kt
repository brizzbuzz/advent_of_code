package io.github.unredundant.aoc.day

object Day07 : Day<Int, Int> {
  private const val TOTAL_DISK_SPACE = 70000000
  private const val REQUIRED_SPACE = 30000000

  override val calendarDate: Int = 7

  override fun silver(): Int {
    val fileSystem = input.constructFileSystemFromInput()
    return fileSystem.directories
      .filterValues { it.totalSize() < 100000 }
      .map { it.value.totalSize() }
      .sum()
  }

  override fun gold(): Int {
    val fileSystem = input.constructFileSystemFromInput()
    val freeSpace = TOTAL_DISK_SPACE - fileSystem.directories.values.map { it.totalSize() }.max()
    val spaceToFree = REQUIRED_SPACE - freeSpace
    return fileSystem.directories.mapValues { it.value.totalSize() }.map { it.toPair() }
      .fold(Int.MAX_VALUE) { acc, pair ->
        if (pair.second - spaceToFree > 0 && pair.second < acc) {
          pair.second
        } else {
          acc
        }
      }
  }

  private data class FileSystem(val directories: MutableMap<String, Directory> = mutableMapOf())

  private data class File(val name: String, val size: Int)

  private data class Directory(
    val files: MutableList<File> = mutableListOf(),
    val directories: MutableList<Directory> = mutableListOf(),
  )

  // TODO This is disgusting
  private fun String.constructFileSystemFromInput(): FileSystem {
    val fileSystem = FileSystem()
    var currPath = ""
    lines().forEach { line ->
      when {
        line.startsWith("$") && line.contains("cd") -> {
          val (_, _, dir) = line.split(" ")
          val newDir = Directory()
          val parentPath = currPath
          currPath = when (dir) {
            "/" -> "/"
            ".." -> {
              currPath.substringBeforeLast("/").substringBeforeLast("/") + '/'
            }

            else -> "$currPath$dir/"
          }
          when (dir) {
            "/" -> {
              fileSystem.directories[currPath] = newDir
            }

            ".." -> {}
            else -> {
              fileSystem.directories[parentPath]!!.directories.add(newDir)
              fileSystem.directories[currPath] = newDir
            }
          }
        }

        else -> {
          when {
            line.matches(Regex("[0-9]* .*")) -> {
              val (size, name) = line.split(" ")
              val file = File(name, size.toInt())
              fileSystem.directories[currPath]?.files?.add(file)
            }
          }
        }
      }
    }
    return fileSystem
  }

  private fun Directory.totalSize(): Int = files.sumOf { it.size } + directories.sumOf { it.totalSize() }
}

fun main() {
  println(Day07.silver())
  println(Day07.gold())
}
