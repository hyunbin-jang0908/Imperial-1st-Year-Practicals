package museumvisit

import java.io.PrintStream
import kotlin.random.Random


class ImpatientVisitor(val name: String, val string: PrintStream, val museum: Museum): Runnable {
    override fun run() {
        var currentRoom = museum.enterIfPossible()
        while (currentRoom == null) {
            string.println("$name could not get into ${museum.name} but will try again soon.")
            currentRoom = museum.enterIfPossible()
            Thread.sleep(10)
            string.println("$name is ready to try again.")
        }
        string.println("$name has entered ${museum.name}.")

        while (true) {
            string.println("$name has entered ${currentRoom!!.name}.")
            val time = Random.nextLong(1, 201)
            Thread.sleep(time)
            string.println("$name wants to leave ${currentRoom.name}.")

            val connectedRooms = museum.getConnectedRooms(currentRoom)
//            val randomIndex = Random.nextInt(connectedRooms.size)
            var destination = connectedRooms.random()
            var succeeded = museum.moveWithLock(currentRoom, destination)
            while (!succeeded) {
                string.println("$name failed to leave ${currentRoom.name} but will try again soon.")
                Thread.sleep(10)
                string.println("$name is ready to try leaving ${currentRoom.name} again.")
                destination = connectedRooms.random()
                succeeded = museum.moveWithLock(currentRoom, destination)
            }
            string.println("$name has left ${currentRoom.name}.")
            currentRoom = destination
            if (currentRoom.name == "Outside") {
                string.println("$name has left ${museum.name}.")
                return
            }


        }
    }
}
