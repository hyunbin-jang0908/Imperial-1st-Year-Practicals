package tunes

class Composer(val notes: List<Note>, private val target: Tune): Runnable {
    override fun run() {
        for (note in notes) {
            target.addNote(note)
        }
    }
}