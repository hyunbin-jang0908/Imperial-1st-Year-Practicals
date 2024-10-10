package queues

interface Acceptor<T> {
    fun accept(item: T)
}

interface Forwarder {
    fun forward()
}

class QueueNode<T>(var queue: Queue<T>, val successor: Acceptor<T>) : Acceptor<T>, Forwarder {
    override fun accept(item: T) {
        queue.enqueue(item)
    }

    override fun forward() {
        val dequeued = queue.dequeue()
        if (dequeued != null) {
            successor.accept(dequeued)
        }
    }
}

class Sink<T> : Acceptor<T> {
    private val sinked: MutableList<T> = mutableListOf()

    override fun accept(item: T) {
        sinked.add(item)
    }

    fun getAccepted(): List<T> {
        return sinked
    }
}
