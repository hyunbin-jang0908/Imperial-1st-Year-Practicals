package journeyplan

// Add your code for the route planner in this file.
class SubwayMap(val map: List<Segment>) {
  fun routesFrom(
    origin: Station,
    destination: Station,
    characteristic: (Route) -> Int = ::totalDuration
  ): List<Route> {
    val routes = mutableListOf<Route>()
    return routeHelper(origin, destination, emptyList(), routes).sortedBy { characteristic(it) }
  }

  private fun routeHelper(
    origin: Station,
    destination: Station,
    visited: List<String>,
    routes: MutableList<Route>
  ): List<Route> {
    if (origin.name == destination.name) return listOf(Route(emptyList()))
    var possibleSegs = map.filter { it.station1.name == origin.name }
    possibleSegs = possibleSegs.filter { it.station2.name !in visited }
    if (possibleSegs.isEmpty()) return emptyList()
    val newRoutes = mutableListOf<Route>()
    for (segment in possibleSegs) {
      newRoutes += routeHelper(segment.station2, destination, visited + origin.name, routes)
        .map { Route(listOf(segment) + it.segs) }
//      println(newRoutes)
    }
    return newRoutes
  }
}

// characteristic function for changes
fun totalChanges(r: Route): Int = r.getChanges()

// characteristic function for durations
fun totalDuration(r: Route): Int = r.getDuration()

class Route(var segs: List<Segment>) {
  override fun toString(): String {
    var output = "${segs.first().station1} to ${segs.last().station2}" +
      " - ${getDuration()} minutes, ${getChanges()} changes\n"
    for (segment in segs) output += "- ${segment.station1} to ${segment.station2} by ${segment.line}\n"
    return output
  }

  fun getDuration(): Int {
    var totalTime = 0
    segs.map { totalTime += it.time }
    return totalTime
  }

  fun getChanges(): Int {
    var totalChange = 0
    var prevLine = segs[0].line.name
    for (s in segs) {
      if (s.line.name != prevLine) totalChange += 1
      prevLine = s.line.name
    }
    return totalChange
  }
}

fun londonUnderground(): SubwayMap {
  val segments = piccadilly_eastBound +
    piccadilly_westBound +
    districtLine_eastBound +
    districtLine_westbound +
    victoria_northern +
    victoria_southern
  return SubwayMap(segments)
}

fun main() {
  val map = londonUnderground()
  println(map.routesFrom(southKensington, westminster, ::totalChanges))
}
