package maps

import java.util.NoSuchElementException

interface Node<T> {
    var next: Node<T>?
}

class ValueNode<T>(var value: T) : Node<T> {
    override var next: Node<T>? = null
}

class RootNode<T> : Node<T> {
    override var next: Node<T>? = null
}

class CustomLinkedList<T> : MutableIterable<T> {
    val root = RootNode<T>()

    fun isEmpty(): Boolean = root.next == null

    fun head(): ValueNode<T>? = root.next as ValueNode<T>?

    fun add(item: T) {
        val newNode = ValueNode(item)
        if (isEmpty()) {
            root.next = newNode
        }
        else {
            newNode.next = root.next
            root.next = newNode
        }
    }

    fun remove(): Node<T>? {
        if (isEmpty()) return null
        val temp = root.next
        root.next = root.next?.next
        return temp
    }

    fun removeSpecific(item: T): T? {
        var current: Node<T>? = root
        var previous: Node<T>? = null
        while (current != null) {
            val valueNode = current as? ValueNode<T>
            if (valueNode != null && valueNode.value == item) {
                previous?.next = current.next
                return valueNode.value
            }
            previous = current
            current = current.next
        }
        return null
    }

    override fun iterator(): MutableIterator<T> {
        return object : MutableIterator<T> {
            var current: Node<T>? = root
            var previous: Node<T>? = null

            override fun hasNext(): Boolean {
                return current?.next != null
            }

            override fun next(): T {
                if (!hasNext()) throw NoSuchElementException()
                previous = current
                current = current?.next
                return (current as ValueNode<T>).value
            }

            override fun remove() {
                if (previous == null) throw UnsupportedOperationException()
                previous!!.next = current?.next
                current = previous
            }
        }
    }
}

fun main() {
    val linkedList = CustomLinkedList<Int>()
    println(linkedList.head())
    linkedList.add(6)
    println(linkedList.isEmpty())
    linkedList.add(7)
    println(linkedList.head()?.value)
    linkedList.remove()
    println(linkedList.isEmpty())
    println(linkedList.head()?.value)
}
