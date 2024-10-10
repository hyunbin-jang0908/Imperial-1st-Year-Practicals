package journeyplan

// Add your code for modelling public transport networks in this file.
class Station(val name: String) {
  override fun toString(): String = name
}

class Line(val name: String) {
  override fun toString(): String = "$name Line"
}

class Segment(val station1: Station, val station2: Station, val line: Line, val time: Int) {
  override fun toString(): String =
    "${station1.name} --> ${station2.name} via $line, (duration: ${time}mins).\n"
}
