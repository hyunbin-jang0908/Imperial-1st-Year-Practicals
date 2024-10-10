package simulation

import java.io.PrintStream

interface Event {
    fun invoke()
}

interface Clock {
    fun currentTime(): Double
}

class ToyEvent(private val printStream: PrintStream) : Event {
    override fun invoke() {
        printStream.print("A toy event occurred.\n")
    }
}

class TickEvent(private val printStream: PrintStream, private val simulator: Simulator) : Event {
    override fun invoke() {
        printStream.print("Tick at ${simulator.currentTime()}\n")
        simulator.schedule(TickEvent(printStream, simulator), 1.0)
    }
}
