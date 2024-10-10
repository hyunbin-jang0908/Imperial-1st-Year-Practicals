package simulation

class TickSimulator(private val stoppingTime: Double) : Simulator() {
    override fun shouldTerminate(): Boolean = currentTime() >= stoppingTime
}

fun main() {
    val tickSimulator: Simulator = TickSimulator(10.0)
    tickSimulator.schedule(TickEvent(System.out, tickSimulator), 0.5)
    tickSimulator.execute()
}
