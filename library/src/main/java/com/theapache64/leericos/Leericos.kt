package com.theapache64.leericos

import android.content.Context
import com.theapache64.leericos.models.LRCLine
import java.io.InputStream
import java.util.ArrayList
import java.util.regex.Pattern
import kotlin.Comparator

class Leericos private constructor(
    private val data: String
) {
    lateinit var lrcs: List<LRCLine>

    init {
        setLrcs()
    }

    /**
     * Sets LRC list
     */
    private fun setLrcs() {
        val lrcs = mutableListOf<LRCLine>()
        data.split(Regex("[\n\r]")).forEach { line ->
            val lrcList = parseLrc(line)
            if (lrcList != null && lrcList.isNotEmpty()) {
                lrcs.addAll(lrcList)
            }
        }
        sortLrcs(lrcs)
        this.lrcs = lrcs
    }

    /**
     * Returns LRC for the time passed
     */
    fun get(time: Long): LRCLine {
        return lrcs[getPosition(time)]
    }

    /**
     * Returns LRC position for the time passed
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun getPosition(time: Long): Int {
        var linePos = 0
        for (i in 0 until lrcs.size) {
            val lrc = lrcs[i]
            if (time >= lrc.time) {
                if (i == lrcs.size - 1) {
                    linePos = lrcs.size - 1
                } else if (time < lrcs[i + 1].time) {
                    linePos = i
                    break
                }
            }
        }
        return linePos
    }

    companion object {

        private const val LINE_REGEX = "((\\[\\d{2}:\\d{2}\\.\\d{2}])+)(.*)"
        private const val TIME_REGEX = "\\[(\\d{2}):(\\d{2})\\.(\\d{2})]"

        /**
         * Parses LRC line from passed string
         */
        private fun parseLrc(lrcLine: String): List<LRCLine>? {

            if (lrcLine.trim { it <= ' ' }.isEmpty()) {
                return null
            }

            val lrcs = ArrayList<LRCLine>()
            val matcher = Pattern.compile(LINE_REGEX).matcher(lrcLine)
            if (!matcher.matches()) {
                return null
            }

            val time = matcher.group(1)
            val content = matcher.group(3)
            val timeMatcher = Pattern.compile(TIME_REGEX).matcher(time)

            while (timeMatcher.find()) {

                val min = timeMatcher.group(1)
                val sec = timeMatcher.group(2)
                val mil = timeMatcher.group(3)
                val lrc = LRCLine()
                lrc.time = (java.lang.Long.parseLong(min) * 60 * 1000 + java.lang.Long.parseLong(sec) * 1000
                        + java.lang.Long.parseLong(mil) * 10)
                if (content != null && content.isNotEmpty()) {
                    lrc.text = content
                } else {
                    lrc.text = "(music)"
                }

                lrcs.add(lrc)
            }
            return lrcs
        }

        /**
         * Sort LRCS based on the order they show.
         */
        private fun sortLrcs(lrcs: MutableList<LRCLine>) {
            lrcs.sortWith(Comparator { o1, o2 -> (o1.time - o2.time).toInt() })
        }

        /**
         * Get Leericos instance from String
         */
        @Suppress("MemberVisibilityCanBePrivate")
        fun fromString(data: String): Leericos {
            return Leericos(data)
        }

        /**
         * Get Leericos instance from InputStream
         */
        @Suppress("MemberVisibilityCanBePrivate")
        fun fromInputStream(inputStream: InputStream): Leericos {
            return fromString(inputStream.bufferedReader().use { it.readText() })
        }


        /**
         * Get Leericos instance from Assets
         */
        fun fromAssets(context: Context, path: String): Leericos {
            return fromInputStream(context.assets.open(path))
        }


    }
}