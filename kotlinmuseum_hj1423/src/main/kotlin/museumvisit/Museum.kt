package museumvisit

import java.util.ArrayDeque
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Museum(val name: String, val entrance: MuseumRoom) {
    private var _admitted = 0
    val admitted : Int
        get() = _admitted

    val outside: MuseumRoom = MuseumRoom("Outside", Int.MAX_VALUE)

    fun entranceHasCapacity(): Boolean = entrance.hasCapacity()

    fun enter() {
        if (!entranceHasCapacity()) throw UnsupportedOperationException()
        entrance.enter()
        _admitted++
    }

    private val museumRooms : MutableSet<MuseumRoom> = mutableSetOf(entrance)

    fun addRoom(museumRoom: MuseumRoom) {
        for (room in museumRooms) {
            if (room == museumRoom) throw IllegalArgumentException()
        }
        museumRooms.add(museumRoom)
    }

    private val connections: MutableList<Pair<MuseumRoom, MuseumRoom>> = mutableListOf()

    fun connectRoomTo(room1: MuseumRoom, room2: MuseumRoom) {
        if (room1 !in museumRooms || (room2 !in museumRooms && room2.name != "Outside"))
            throw IllegalArgumentException()
        if (room1.name == room2.name) throw IllegalArgumentException()
        if (connections.contains(room1 to room2)) throw IllegalArgumentException()
        connections.add(room1 to room2)
    }

    fun connectRoomToExit(exit: MuseumRoom) {
        connectRoomTo(exit, outside)
    }

    fun checkWellFormed() {
        val reachableRooms = mutableSetOf<MuseumRoom>()

        fun dfs(room: MuseumRoom) {
            if (room in reachableRooms) return
            reachableRooms.add(room)
            for (nextRoom in getConnectedRooms(room)) {
                dfs(nextRoom)
            }
        }

        dfs(entrance)

        val unreachableRooms = museumRooms - reachableRooms
        if (unreachableRooms.isNotEmpty()) {
            throw UnreachableRoomsException(unreachableRooms)
        }

        val exitableRooms = mutableSetOf<MuseumRoom>()

        fun getConnectedRoomsReverse(room: MuseumRoom): Set<MuseumRoom> {
            return connections.filter { it.second == room }.map { it.first }.toSet()
        }

        fun dfsRev(room: MuseumRoom) {
            if (room in exitableRooms) return
            exitableRooms.add(room)
            for (nextRoom in getConnectedRoomsReverse(room)) {
                dfsRev(nextRoom)
            }
        }

        dfsRev(outside)

        val exitBlockedRooms = museumRooms - exitableRooms

        if (exitBlockedRooms.isNotEmpty()) {
            throw CannotExitMuseumException(exitBlockedRooms)
        }
    }

    fun getConnectedRooms(room: MuseumRoom): Set<MuseumRoom> {
        return connections.filter { it.first == room }.map { it.second }.toSet()
    }

    override fun toString(): String {
        val visitedRooms = mutableSetOf<MuseumRoom>()
        val queue = ArrayDeque<MuseumRoom>()
        val result = StringBuilder()

        queue.offer(entrance)
        visitedRooms.add(entrance)

        result.appendLine(name)

        while (queue.isNotEmpty()) {
            val currentRoom = queue.poll()
            if (currentRoom == outside) {
                queue.poll()
                continue
            }

            // Append room name and its connections to the result
            result.append("${currentRoom.name} leads to: ")
            result.append(getConnectedRooms(currentRoom).joinToString { it.name })
            result.appendLine()

            // Enqueue connected rooms for further traversal
            getConnectedRooms(currentRoom).forEach { connectedRoom ->
                if (connectedRoom !in visitedRooms) {
                    queue.offer(connectedRoom)
                    visitedRooms.add(connectedRoom)
                }
            }
        }

        return result.toString()
    }

    fun enterIfPossible(): MuseumRoom? {
        entrance.lock.withLock {
            if (entranceHasCapacity()) {
                enter()
                return entrance
            } else {
                return null
            }
        }
    }

    fun moveWithLock(from: MuseumRoom, to: MuseumRoom): Boolean {
        var first = from
        var second = to
        if (from.name > to.name) {
            first = to
            second = from
        }
        first.lock.withLock{
            second.lock.withLock {
                if (to.hasCapacity()) {
                    from.exit()
                    to.enter()
                    return true
                } else return false
            }
        }
    }

}

class UnreachableRoomsException(private val unreachableRooms: Set<MuseumRoom>) : Exception() {
    override fun toString(): String {
        val sortedRooms = unreachableRooms.map { it.name }.sorted().joinToString(", ")
        return "Unreachable rooms: $sortedRooms"
    }
}

class CannotExitMuseumException(private val exitBlockedRooms: Set<MuseumRoom>) : Exception() {
    override fun toString(): String {
        val sortedRooms = exitBlockedRooms.map { it.name }.sorted().joinToString(", ")
        return "Impossible to leave museum from: $sortedRooms"
    }
}

