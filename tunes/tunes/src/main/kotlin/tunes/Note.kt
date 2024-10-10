package tunes

const val MAX_PITCH = 200
const val MAX_DURATION = 64.0

data class Note(val pitch: Int, val duration: Double) {
    init {
        if (pitch < 0 || pitch > MAX_PITCH) {
            throw IllegalArgumentException()
        }
        if (duration <= 0 || duration > MAX_DURATION) {
            throw IllegalArgumentException()
        }
    }

    override fun toString(): String {
        return "${getName(pitch)}${pitch / 12}($duration)"
    }

    private fun getName(pitch: Int): String = when (pitch % 12) {
            0 -> "C"
            1 -> "C#"
            2 -> "D"
            3 -> "D#"
            4 -> "E"
            5 -> "F"
            6 -> "F#"
            7 -> "G"
            8 -> "G#"
            9 -> "A"
            10 -> "A#"
            else -> "B"
        }

    fun hasNoteAbove(): Boolean = pitch < MAX_PITCH

    fun hasNoteBelow(): Boolean = pitch > 0

    fun noteAbove(): Note = Note(pitch + 1, duration)

    fun noteBelow(): Note = Note(pitch - 1, duration)
}
