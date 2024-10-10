package simulation

import java.io.PrintStream
import java.util.Random
import kotlin.math.ln

interface TimeDelay {
    fun nextSample(): Double
}

class UniformDelay(val a: Double, val b: Double) : TimeDelay {
    override fun nextSample(): Double = (b - a) * Random().nextDouble() + a
}

class ExponentialDelay(val m: Double) : TimeDelay {
    override fun nextSample(): Double = -ln(Random().nextDouble()) * m
}

class RandomTickSimulator(
    val printStream: PrintStream,
    private val stoppingTime: Double,
    val boundary: Pair<Double, Double>
) : Simulator() {
    override fun shouldTerminate(): Boolean = currentTime() >= stoppingTime

    inner class TickEvent() : Event {
        override fun invoke() {
            printStream.print("Tick at ${currentTime()}\n")
            schedule(TickEvent(), UniformDelay(boundary.first, boundary.second).nextSample())
        }
    }
}

fun main() {
    val randomTickSimulator = RandomTickSimulator(System.out, 10.0, Pair(1.0, 2.0))
    randomTickSimulator.schedule(randomTickSimulator.TickEvent(), 0.5)
    randomTickSimulator.execute()
}
