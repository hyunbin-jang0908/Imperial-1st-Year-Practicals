package museumvisit

fun createArtGallery(): Museum{
    val museum = Museum("Art Gallery", MuseumRoom("Entrance hall", 10))
    val exhibition = MuseumRoom("Exhibition room", 5)
    museum.addRoom(exhibition)
    museum.connectRoomTo(museum.entrance, exhibition)
    museum.connectRoomToExit(exhibition)
    return museum
}

fun createAnimalSanctuary(): Museum {
    val museum = Museum("Animal sanctuary", MuseumRoom("Entrance hall", 20))
    val bats = MuseumRoom("Bats", 10)
    val lizards = MuseumRoom("Lizards", 10)
    val insects = MuseumRoom("Insects", 10)
    val snakes = MuseumRoom("Snakes", 10)
    val giftShop = MuseumRoom("Gift shop", 20)
    museum.addRoom(bats)
    museum.addRoom(lizards)
    museum.addRoom(insects)
    museum.addRoom(snakes)
    museum.addRoom(giftShop)
    museum.connectRoomTo(museum.entrance, bats)
    museum.connectRoomTo(bats, lizards)
    museum.connectRoomTo(lizards, insects)
    museum.connectRoomTo(lizards, giftShop)
    museum.connectRoomTo(insects, snakes)
    museum.connectRoomTo(insects, giftShop)
    museum.connectRoomTo(snakes, museum.entrance)
    museum.connectRoomToExit(giftShop)
    return museum
}

fun createAnimalSanctuaryWithUnreachableRooms(): Museum {
    val museum = Museum("Animal Sanctuary", MuseumRoom("Entrance hall", 20))
    val bats = MuseumRoom("Bats", 10)
    val lizards = MuseumRoom("Lizards", 10)
    val insects = MuseumRoom("Insects", 10)
    val snakes = MuseumRoom("Snakes", 10)
    val giftShop = MuseumRoom("Gift shop", 20)
    museum.addRoom(bats)
    museum.addRoom(lizards)
    museum.addRoom(insects)
    museum.addRoom(snakes)
    museum.addRoom(giftShop)
    museum.connectRoomTo(bats, lizards)
    museum.connectRoomTo(lizards, insects)
    museum.connectRoomTo(lizards, giftShop)
    museum.connectRoomTo(insects, snakes)
    museum.connectRoomTo(insects, giftShop)
    museum.connectRoomTo(snakes, museum.entrance)
    museum.connectRoomToExit(giftShop)
    return museum
}

fun createAnimalSanctuaryWithRoomsThatDoNotLeadToExit(): Museum {
    val museum = Museum("Animal Sanctuary", MuseumRoom("Entrance hall", 20))
    val bats = MuseumRoom("Bats", 10)
    val lizards = MuseumRoom("Lizards", 10)
    val insects = MuseumRoom("Insects", 10)
    val snakes = MuseumRoom("Snakes", 10)
    val giftShop = MuseumRoom("Gift shop", 20)
    museum.addRoom(bats)
    museum.addRoom(lizards)
    museum.addRoom(insects)
    museum.addRoom(snakes)
    museum.addRoom(giftShop)
    museum.connectRoomTo(museum.entrance, bats)
    museum.connectRoomTo(bats, lizards)
    museum.connectRoomTo(lizards, insects)
    museum.connectRoomTo(lizards, giftShop)
    museum.connectRoomTo(insects, snakes)
    museum.connectRoomToExit(giftShop)
    return museum
}
