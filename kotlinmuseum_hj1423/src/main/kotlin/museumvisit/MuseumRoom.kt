package museumvisit

import java.util.concurrent.locks.ReentrantLock

class MuseumRoom(
    val name: String,
    private val capacity: Int
) {
    private var _occupancy : Int = 0
    val occupancy : Int
        get() = _occupancy

    val lock = ReentrantLock()

    init {
        if (capacity < 0) throw IllegalArgumentException()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MuseumRoom) return false
        return name == other.name
    }

    fun hasCapacity(): Boolean = _occupancy < capacity

    fun enter() {
        if (!hasCapacity()) throw UnsupportedOperationException()
        _occupancy++
    }

    fun exit() {
        if (occupancy == 0 || name == "Outside") throw UnsupportedOperationException()
        _occupancy--
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + capacity
        result = 31 * result + _occupancy
        return result
    }
}