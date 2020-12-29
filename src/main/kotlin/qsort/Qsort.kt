package qsort

import java.util.concurrent.ExecutorService
import java.util.concurrent.atomic.AtomicInteger

class Qsort() {
    private var executor: ExecutorService? = null
    private var count: AtomicInteger = AtomicInteger(1)
    private val sn = SyncNotifier()

    fun stSorting(inData: MutableList<Int>): Boolean {
        qSorting(inData, 0, inData.size-1)
        return true
    }

    fun mtSorting(inData: MutableList<Int>, exec: ExecutorService): Boolean {
        executor = exec
        //count = AtomicInteger(1)
        sn.inc1()
        qSortingPar(inData, 0, inData.size-1)
        sn.waitToFinish()
        //while (count.get() != 0) { /*waiting for the sorting process to finish*/ }
        executor!!.shutdownNow()
        return true
    }

    private fun qSorting(inData: MutableList<Int>, start: Int, end: Int) {
        if (start >= end) { return }
        if (end - start == 1) {
            if (inData[start] > inData[end]) {
                val t = inData[start]
                inData[start] = inData[end]
                inData[end] = t
            }
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
        qSorting(inData, start, startPointer - 1)
        qSorting(inData, startPointer, end)
    }

    private fun qSortingPar(inData: MutableList<Int>, start: Int, end: Int) {
        if (start >= end) {
            //count.updateAndGet { it - 1 }
            sn.dec1()
            return
        }
        if (end - start == 1) {
            if (inData[start] > inData[end]) {
                val t = inData[start]
                inData[start] = inData[end]
                inData[end] = t
            }
            //count.updateAndGet { it - 1 }
            sn.dec1()
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
        //count.updateAndGet { it + 1 }
        sn.inc1()
        executor!!.execute { qSortingPar(inData, start, startPointer - 1) }
        executor!!.execute {qSortingPar(inData, startPointer, end) }
    }

    private inner class SyncNotifier {
        private val count = AtomicInteger(0)
        private val lock = Object()

        fun inc1() { count.updateAndGet { it + 1 } }
        fun dec1() {
            count.updateAndGet { it - 1 }
            synchronized(lock) { if (count.get() == 0) lock.notifyAll() }
        }
        fun waitToFinish() { synchronized(lock) { while (count.get() > 0) lock.wait() } }
    }
}