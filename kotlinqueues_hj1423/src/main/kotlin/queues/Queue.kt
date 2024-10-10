package queues

import java.util.PriorityQueue

data class Point(val coordX: Int, val coordY: Int)

interface Queue<T> {
    fun enqueue(item: T)

    fun peek(): T?

    fun dequeue(): T?

    fun isEmpty(): Boolean

    fun size(): Int
}

class FifoQueue<T> : Queue<T> {

    private data class Node<T>(val value: T, var next: Node<T>? = null)

    private var front: Node<T>? = null
    private var rear: Node<T>? = null

    override fun enqueue(item: T) {
        val newNode = Node(item)
        if (isEmpty()) {
            front = newNode
            rear = newNode
        } else {
            rear?.next = newNode
            rear = newNode
        }
    }

    override fun peek(): T? {
        return front?.value
    }

    override fun dequeue(): T? {
        val returnNode = front
        front = returnNode?.next
        if (front == null) {
            rear = null
        }
        return returnNode?.value
    }

    override fun isEmpty(): Boolean {
        return front == null
    }

    override fun size(): Int {
        var count = 0
        var currentNode = front
        while (currentNode != null) {
            count += 1
            currentNode = currentNode.next
        }
        return count
    }
}

class LifoQueue<T> : Queue<T> {
    private data class Node<T>(val value: T, var next: Node<T>? = null)

    private var top: Node<T>? = null

    override fun enqueue(item: T) {
        val newNode = Node(item)
        newNode.next = top
        top = newNode
    }

    override fun peek(): T? {
        return top?.value
    }

    override fun dequeue(): T? {
        val returnNode = top
        top = top?.next
        return returnNode?.value
    }

    override fun isEmpty(): Boolean {
        return top == null
    }

    override fun size(): Int {
        var count = 0
        var current = top
        while (current != null) {
            count++
            current = current.next
        }
        return count
    }
}

class PrQueue<T>(comparator: Comparator<T>? = null) : Queue<T> {
    private val priorityQueue: PriorityQueue<T>

    init {
        // Initialize the priority queue with the provided comparator or use natural ordering
        priorityQueue = if (comparator != null) {
            PriorityQueue(comparator)
        } else {
            PriorityQueue()
        }
    }

    override fun enqueue(item: T) {
        priorityQueue.offer(item)
    }

    override fun dequeue(): T? {
        return priorityQueue.poll()
    }

    override fun peek(): T? {
        return priorityQueue.peek()
    }

    override fun isEmpty(): Boolean {
        return priorityQueue.isEmpty()
    }

    override fun size(): Int {
        return priorityQueue.size
    }
}

class PointComparator : Comparator<Point> {
    override fun compare(o1: Point, o2: Point): Int {
        // Compare points first by their x coordinates
        val xComparison = o1.coordX - o2.coordX

        // If x coordinates are different, return the result of the x comparison
        if (xComparison != 0) {
            return xComparison
        }

        // If x coordinates are equal, compare by y coordinates
        return o1.coordY - o2.coordY
    }
}
