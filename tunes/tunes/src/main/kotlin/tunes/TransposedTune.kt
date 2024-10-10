package tunes

class TransposedTune(private val target: Tune, private val pitchOffset: Int): Tune {

    override val notes: List<Note>
        get() {
            val newNotes = mutableListOf<Note>()
            for (note in target) {
                var shiftedPitch = note.pitch + pitchOffset
                if (shiftedPitch < 0) shiftedPitch = 0
                else if (shiftedPitch > MAX_PITCH) shiftedPitch = MAX_PITCH
                newNotes.add(Note(shiftedPitch, note.duration))
            }
            return newNotes
        }

    override fun addNote(note: Note) {
        var m = note.pitch - pitchOffset
        if (m < 0) m = 0
        else if (m > MAX_PITCH) m = MAX_PITCH
        target.addNote(Note(m, note.duration))
    }
}
