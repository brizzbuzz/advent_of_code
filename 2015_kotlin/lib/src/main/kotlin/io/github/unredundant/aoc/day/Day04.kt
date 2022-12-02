package io.github.unredundant.aoc.day

import io.github.unredundant.aoc.util.Util.getInput
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest

object Day04 : Day<Int, Int> {
  override val calendarDate: Int = 4
  private val crypt = MessageDigest.getInstance("MD5");

  override fun silver(): Int {
    val input = getInput(4)
    var i = 0
    do {
      val hash = md5("$input$i").toHex()
      if (hash.startsWith("00000")) {
        return i
      }
      i++
    } while (true)
  }

  override fun gold(): Int {
    val input = getInput(4)
    var i = 0
    do {
      val hash = md5("$input$i").toHex()
      if (hash.startsWith("000000")) {
        return i
      }
      i++
    } while (true)
  }

  private fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(UTF_8))
  private fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }
}
