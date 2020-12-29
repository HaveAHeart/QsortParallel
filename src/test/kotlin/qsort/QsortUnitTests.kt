package qsort

import org.junit.Test
import java.util.concurrent.Executors
import kotlin.random.Random

class QsortUnitTests {
    private val itemsAmount = 1000000
    private val iterAmount = 1

    @Test
    fun usualTest() {
        var delta: Long = 0

        for (i in 1..iterAmount) {
            val a = mutableListOf<Int>()
            for (j in 1..itemsAmount) a.add(Random.nextInt(-1000, 1000))
            val z = Qsort()
            val timeStart = System.currentTimeMillis()
            val res = z.stSorting(a)
            val timeFinish = System.currentTimeMillis()
            delta += (timeFinish - timeStart)
            assert(isSorted(a))
            assert(res)
        }

        val avgTime = delta/iterAmount
        println("1 thread: $avgTime")
    }

    @Test
    fun parallelTest() {
        doTests(2)
        doTests(4)
        doTests(5)
        doTests(6)
        doTests(8)
        doTests(16)
    }

    private fun isSorted(a: MutableList<Int>): Boolean {
        for (i in 1 until a.size) { if (a[i-1] > a[i]) return false }
        return true
    }

    private fun doTests(threadsAmount: Int) {
        var delta: Long = 0

        for (i in 1..iterAmount) {
            val a = mutableListOf<Int>()
            for (j in 1..itemsAmount) a.add(Random.nextInt(-1000, 1000))
            val z = Qsort()
            val timeStart = System.currentTimeMillis()
            val res = z.mtSorting(a, Executors.newFixedThreadPool(threadsAmount))
            val timeFinish = System.currentTimeMillis()
            delta += (timeFinish - timeStart)
            assert(isSorted(a))
            assert(res)
        }

        val avgTime = delta/iterAmount
        println("$threadsAmount threads: $avgTime from max ${Runtime.getRuntime().availableProcessors()}")
    }

}