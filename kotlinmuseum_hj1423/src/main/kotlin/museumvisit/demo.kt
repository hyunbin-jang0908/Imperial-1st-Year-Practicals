package museumvisit

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Incrementer(
    private val counter: Counter,
    private val numIncrements: Int,
) : Runnable {

    private val _observedValues: MutableSet<Int> = mutableSetOf()

    val observedValues: Set<Int>
        get() = _observedValues

    override fun run() {
        for (i in 1..numIncrements) {
            _observedValues.add(counter.inc())
        }
    }
}

class Counter {
    private val lock: Lock = ReentrantLock()

    var value: Int = 0
        private set

    fun inc(): Int {
        lock.withLock {
            val result = value
            value++
            return result
        } // The lock is guaranteed to be unlocked, thanks to withLock
    }
}


fun main() {
    val counter = Counter()
    val incrementer1 = Incrementer(counter, 500)
    val incrementer2 = Incrementer(counter, 500)
    val thread1 = Thread(incrementer1)
    val thread2 = Thread(incrementer2)
    thread1.start()
    thread2.start()

    thread1.join()
    thread2.join()

    println(counter.value)
    println(incrementer1.observedValues intersect incrementer2.observedValues)

    // We still have race conditions - these will print nondeterministic values.
    // But we got rid of the data races!
    println(incrementer1.observedValues.sorted()[0])
    println(incrementer2.observedValues.sorted()[0])
}

