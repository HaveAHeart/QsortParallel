package qsort

import java.util.concurrent.ExecutorService
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

class Qsort(items: Int) {
    private var executor: ExecutorService? = null
    private var itemsAmount = 0
    private var count: AtomicInteger = AtomicInteger(1)
    var a = mutableListOf<Int>()

    init {
        reset(items)
    }

    private fun reset(items: Int) {
        a = mutableListOf()
        itemsAmount = items
        for (j in 1..this.itemsAmount) a.add(Random.nextInt(-1000, 1000))
        count = AtomicInteger(1)
    }

    fun stSorting(inData: MutableList<Int>): Boolean {
        count = AtomicInteger(1)
        qSorting(inData, 0, a.size-1)
        while (count.get() != 0) { /*waiting for the sorting process to finish*/ }
        return true
    }

    fun mtSorting(inData: MutableList<Int>, exec: ExecutorService, items: Int): Boolean {
        executor = exec
        itemsAmount = items
        count = AtomicInteger(1)
        qSortingPar(inData, 0, a.size-1)
        while (count.get() != 0) { /*waiting for the sorting process to finish*/ }
        executor!!.shutdownNow()
        return true
    }

    private fun qSorting(inData: MutableList<Int>, start: Int, end: Int) {
        if (start >= end) {
            count.updateAndGet { it - 1 }
            return
        }
        if (end - start == 1) {
            if (inData[start] > inData[end]) {
                val t = inData[start]
                inData[start] = inData[end]
                inData[end] = t
            }
            count.updateAndGet { it - 1 }
            return
        }
        val sortValue = inData[(start+end)/2]
        var startPointer = start
        var endPointer = end


        while (startPointer != endPointer) {
            while (inData[startPointer] < sortValue && startPointer != endPointer) startPointer++
            while (inData[endPointer] > sortValue && startPointer != endPointer) endPointer--
            if (startPointer != endPointer) {
                val t = inData[startPointer]
                inData[startPointer] = inData[endPointer]
                inData[endPointer] = t
                startPointer++
            }
        }
        count.updateAndGet { it + 1 }
        qSorting(inData, start, startPointer - 1)
        qSorting(inData, startPointer, end)
    }

    private fun qSortingPar(inData: MutableList<Int>, start: Int, end: Int) {
        if (start >= end) {
            count.updateAndGet { it - 1 }
            return
        }
        if (end - start == 1) {
            if (inData[start] > inData[end]) {
                val t = inData[start]
                inData[start] = inData[end]
                inData[end] = t
            }
            count.updateAndGet { it - 1 }
            return
        }
        val sortValue = inData[(start+end)/2]
        var startPointer = start
        var endPointer = end


        while (startPointer != endPointer) {
            while (inData[startPointer] < sortValue && startPointer != endPointer) startPointer++
            while (inData[endPointer] > sortValue && startPointer != endPointer) endPointer--
            if (startPointer != endPointer) {
                val t = inData[startPointer]
                inData[startPointer] = inData[endPointer]
                inData[endPointer] = t
                startPointer++
            }
        }
        count.updateAndGet { it + 2 }
        executor!!.execute { qSortingPar(inData, start, startPointer - 1) }
        executor!!.execute {qSortingPar(inData, startPointer, end) }
        count.updateAndGet { it - 1 }
        //Thread.currentThread().interrupt()
    }
}