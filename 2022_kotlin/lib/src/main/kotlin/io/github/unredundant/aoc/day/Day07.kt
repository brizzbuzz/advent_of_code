package io.github.unredundant.aoc.day

object Day07 : Day<Int, Int> {
  override val calendarDate: Int = 7

  override fun silver(): Int {
    val fileSystem = input.constructFileSystemFromInput()
    fileSystem.directories.forEach { t, u -> println("$t: ${u.totalSize()}") }
    return fileSystem.directories
      .filterValues { it.totalSize() < 100000 }
      .map { it.value.totalSize() }
      .sum()
  }

  override fun gold(): Int {
    TODO("Not yet implemented")
  }

  private data class FileSystem(val directories: MutableMap<String, Directory> = mutableMapOf())

  private data class File(val name: String, val size: Int)

  private data class Directory(
    val files: MutableList<File> = mutableListOf(),
    val directories: MutableList<Directory> = mutableListOf(),
  )

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
              println(currPath.substringBeforeLast("/") + '/')
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
              println("Adding file $line to $currPath")
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
  print(Day07.silver())
}
