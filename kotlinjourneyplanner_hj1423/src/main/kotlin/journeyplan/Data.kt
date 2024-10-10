package journeyplan

// Piccadilly line
val southKensington = Station("South Kensington")
val knightsbridge = Station("Knightsbridge")
val hydeParkCorner = Station("Hyde Park Corner")
val greenPark = Station("Green Park")
val leicesterSq = Station("Leicester Square")
val piccadillyCircus = Station("Piccadilly Circus")
val coventGarden = Station("Covent Garden")
val holborn = Station("Holborn")
val russellSquare = Station("Russell Square")
val kingsCross = Station("King's Cross St. Pancras")

// District line
val highStreetKensington = Station("High Street Kensington")
val gloucesterRoad = Station("Gloucester Road")
val sloaneSquare = Station("Sloane Square")
val victoria = Station("Victoria")
val stJamesPark = Station("St. James's Park")
val westminster = Station("Westminster")
val embankment = Station("Embankment")
val temple = Station("Temple")
val mansionHouse = Station("Mansion House")
val cannonStreet = Station("Cannon Street")
val monument = Station("Monument")
val towerHill = Station("Tower Hill")
val aldgateEast = Station("Aldgate East")
val whitechapel = Station("Whitechapel")

// Victoria Line
val oxfordCircus = Station("Oxford Circus")

val victoria_northern = listOf(
  Segment(victoria, greenPark, Line("Victoria"), 3),
  Segment(greenPark, oxfordCircus, Line("Victoria"), 3)
)

val victoria_southern = listOf(
  Segment(oxfordCircus, greenPark, Line("Victoria"), 3),
  Segment(greenPark, victoria, Line("Victoria"), 3)
)

val piccadilly_eastBound = listOf(
  Segment(southKensington, knightsbridge, Line("Piccadilly"), 3),
  Segment(knightsbridge, hydeParkCorner, Line("Piccadilly"), 3),
  Segment(hydeParkCorner, greenPark, Line("Piccadilly"), 4),
  Segment(greenPark, piccadillyCircus, Line("Piccadilly"), 4),
  Segment(piccadillyCircus, leicesterSq, Line("Piccadilly"), 4),
  Segment(leicesterSq, coventGarden, Line("Piccadilly"), 4),
  Segment(coventGarden, holborn, Line("Piccadilly"), 2),
  Segment(holborn, russellSquare, Line("Piccadilly"), 2),
  Segment(russellSquare, kingsCross, Line("Piccadilly"), 3)
)

val piccadilly_westBound = listOf(
  Segment(kingsCross, russellSquare, Line("Piccadilly"), 3),
  Segment(russellSquare, holborn, Line("Piccadilly"), 2),
  Segment(holborn, coventGarden, Line("Piccadilly"), 2),
  Segment(coventGarden, leicesterSq, Line("Piccadilly"), 4),
  Segment(leicesterSq, piccadillyCircus, Line("Piccadilly"), 4),
  Segment(piccadillyCircus, greenPark, Line("Piccadilly"), 4),
  Segment(greenPark, hydeParkCorner, Line("Piccadilly"), 4),
  Segment(hydeParkCorner, knightsbridge, Line("Piccadilly"), 3),
  Segment(knightsbridge, southKensington, Line("Piccadilly"), 3)
)

val districtLine_eastBound = listOf(
  Segment(highStreetKensington, gloucesterRoad, Line("District"), 2),
  Segment(gloucesterRoad, southKensington, Line("District"), 2),
  Segment(southKensington, sloaneSquare, Line("District"), 2),
  Segment(sloaneSquare, victoria, Line("District"), 3),
  Segment(victoria, stJamesPark, Line("District"), 3),
  Segment(stJamesPark, westminster, Line("District"), 2),
  Segment(westminster, embankment, Line("District"), 3),
  Segment(embankment, temple, Line("District"), 2),
  Segment(temple, mansionHouse, Line("District"), 3),
  Segment(mansionHouse, cannonStreet, Line("District"), 2),
  Segment(cannonStreet, monument, Line("District"), 2),
  Segment(monument, towerHill, Line("District"), 4),
  Segment(towerHill, aldgateEast, Line("District"), 2),
  Segment(aldgateEast, whitechapel, Line("District"), 3)
)

val districtLine_westbound = listOf(
  Segment(whitechapel, aldgateEast, Line("District"), 3),
  Segment(aldgateEast, towerHill, Line("District"), 2),
  Segment(towerHill, monument, Line("District"), 4),
  Segment(monument, cannonStreet, Line("District"), 2),
  Segment(cannonStreet, mansionHouse, Line("District"), 3),
  Segment(mansionHouse, temple, Line("District"), 2),
  Segment(temple, embankment, Line("District"), 3),
  Segment(embankment, westminster, Line("District"), 2),
  Segment(westminster, stJamesPark, Line("District"), 2),
  Segment(stJamesPark, victoria, Line("District"), 2),
  Segment(victoria, sloaneSquare, Line("District"), 3),
  Segment(sloaneSquare, southKensington, Line("District"), 3),
  Segment(southKensington, gloucesterRoad, Line("District"), 2),
  Segment(gloucesterRoad, highStreetKensington, Line("District"), 2)
)
