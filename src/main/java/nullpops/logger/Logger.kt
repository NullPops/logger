/*
 * Copyright (c) 2025 NullPops
 *
 * This file is part of logger.
 *
 * Licensed under the GNU Affero General Public License v3.0 (AGPLv3)
 * or a Commercial License.
 *
 * You may use this file under AGPLv3 if you release your project under
 * a compatible open source license. For closed source or commercial use,
 * you must obtain a commercial license from NullPops.
 *
 * See the LICENSE file for details.
 */

package nullpops.logger

import java.time.LocalTime
import java.time.format.DateTimeFormatter

object ConsoleColor {
    const val RESET = "\u001B[0m"
    const val DARK_GRAY = "\u001B[90m"
    const val GRAY = "\u001B[37m"
    const val GREEN = "\u001B[32m"
    const val YELLOW = "\u001B[33m"
    const val BRIGHT_RED = "\u001B[91m"
}

enum class LoggerLevel {
    TRACE,
    INFO,
    DEBUG,
    WARN,
    ERROR
}

class Logger(var scope: String = "main") {

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    }

    fun trace(text: String) {
        print(LoggerLevel.TRACE, text)
    }

    fun info(text: String) {
        print(LoggerLevel.INFO, text)
    }

    fun debug(text: String) {
        print(LoggerLevel.DEBUG, text)
    }

    fun warn(text: String) {
        print(LoggerLevel.WARN, text)
    }

    fun error(text: String) {
        print(LoggerLevel.ERROR, text)
    }

    fun error(e: Exception) {
        print(LoggerLevel.ERROR, e.toString())
        for (line in e.stackTrace)
            print(LoggerLevel.ERROR, line.toString())
    }

    fun print(level: LoggerLevel, text: String) {
        print(when (level) {
            LoggerLevel.TRACE -> ConsoleColor.DARK_GRAY
            LoggerLevel.INFO -> ConsoleColor.GRAY
            LoggerLevel.DEBUG -> ConsoleColor.GREEN
            LoggerLevel.WARN -> ConsoleColor.YELLOW
            LoggerLevel.ERROR -> ConsoleColor.BRIGHT_RED
        })
        val line = "[${getCurrentTime()}] [$scope]: $text${ConsoleColor.RESET}"
        println(line)
    }


    fun getCurrentTime(): String = LocalTime.now().format(formatter)
}