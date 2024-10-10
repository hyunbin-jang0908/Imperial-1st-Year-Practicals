package simulation

import java.io.PrintStream

class BetterTickSimulator(val printStream: PrintStream, private val stoppingTime: Double) : Simulator() {

    inner class TickEvent() : Event {
        override fun invoke() {
            printStream.print("Tick at ${currentTime()}\n")
            schedule(TickEvent(), 1.0)
        }
    }

    override fun shouldTerminate(): Boolean = currentTime() >= stoppingTime
}

fun main() {
    val betterTickSimulator = BetterTickSimulator(System.out, 10.0)
    betterTickSimulator.schedule(betterTickSimulator.TickEvent(), 0.5)
    betterTickSimulator.execute()
}
