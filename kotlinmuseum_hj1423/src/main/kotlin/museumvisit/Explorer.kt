package museumvisit

class Explorer {
    fun explore(museums: List<Museum>) {
        println("Welcome")
        while (true) {
            println("Input the number for the museum to explore")
            for (i in 1..museums.size) {
                println("$i. ${museums[i - 1].name}")
            }

            val choice = readlnOrNull()?.toIntOrNull() ?: continue
            if (choice == 0) break
            if (choice !in 1..museums.size) {
                println("Invalid choice. Please try again.")
                continue
            }

            val selectedMuseum = museums[choice - 1]

            exploreMuseum(selectedMuseum)
        }
        println("Goodbye!")
    }

    private fun exploreMuseum(museum: Museum) {
        var currentRoom: MuseumRoom = museum.entrance

        while (true) {
            if (currentRoom.name == "Outside") {
                println("Now you exited the museum. Goodbye.")
                break
            }

            println("You are in the ${currentRoom.name}.")
            println("Choose the room to move:")

            val connectedRooms = museum.getConnectedRooms(currentRoom).toList()
            for (i in 1..connectedRooms.size) {
                println("$i. ${connectedRooms[i - 1].name}")
            }

            val choice = readlnOrNull() ?: break
            if (choice.toIntOrNull() == null || choice.toIntOrNull() !in 1..connectedRooms.size) {
                println("Invalid Input. Try again")
                continue
            }
            currentRoom = connectedRooms[choice.toInt() - 1]
        }
        println("Back to selecting museum.")
    }
}

fun main() {
    val explore = Explorer()
    explore.explore(listOf(createArtGallery(), createAnimalSanctuary()))
}
