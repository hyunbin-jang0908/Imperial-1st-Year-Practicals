package simulation

import java.util.PriorityQueue

interface Scheduler {
    fun schedule(event: Event, dt: Double)
}

data class EventTimePair(val event: Event, val time: Double) : Comparable<EventTimePair> {
    override fun compareTo(other: EventTimePair): Int {
        // Compare based on time
        return this.time.compareTo(other.time)
    }
}

abstract class Simulator : Clock, Scheduler {
    val eventList = PriorityQueue<EventTimePair>()

    private var currentTime = 0.0

    override fun currentTime(): Double {
        return currentTime
    }

    override fun schedule(event: Event, dt: Double) {
        eventList.add(EventTimePair(event, currentTime() + dt))
    }

    fun execute() {
        while (eventList.isNotEmpty()) {
            val (event, time) = eventList.poll()
            updateTime(time)
            if (shouldTerminate()) break
            event.invoke()
        }
    }

    private fun updateTime(time: Double) {
        currentTime = time
    }

    abstract fun shouldTerminate(): Boolean
}

class ToySimulator : Simulator() {
    override fun shouldTerminate(): Boolean = false
}

fun main() {
    val toySimulator = ToySimulator()
    for (i in 0 until 10) {
        toySimulator.schedule(ToyEvent(System.out), 1.0)
    }
    toySimulator.execute()
}
