package tunes

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ThreadSafeTune(private val target: Tune): Tune {
    private val lock = ReentrantLock()

    override val notes: List<Note>
        get() = lock.withLock {
                return target.notes
            }

    override fun addNote(note: Note) =
        lock.withLock {
            target.addNote(note)
        }
}
