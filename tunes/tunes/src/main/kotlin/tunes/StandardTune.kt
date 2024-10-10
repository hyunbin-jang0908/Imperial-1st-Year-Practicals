package tunes

class StandardTune() : Tune {

    private val _notes = mutableListOf<Note>()

    override val notes: List<Note>
        get() = _notes

    override fun addNote(note: Note) {
        _notes.add(note)
    }
}