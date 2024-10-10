package maps

class ListBasedMap<K, V>(
    ) : CustomMutableMap<K, V> {
        private val list = CustomLinkedList<Entry<K, V>>()

    override val entries: Iterable<Entry<K, V>>
        get() = list
    override val keys: Iterable<K>
        get() = list.map { it.key }
    override val values: Iterable<V>
        get() = list.map { it.value }

    override fun get(key: K): V? {
        return list.find { it.key == key }?.value
    }

    override fun set(key: K, value: V): V? {
        val oldEntry = list.find { it.key == key }
        if (oldEntry != null) list.removeSpecific(oldEntry)
        list.add(Entry(key, value))
        return oldEntry?.value
    }

    override fun put(key: K, value: V): V? = set(key, value)

    override fun put(entry: Entry<K, V>): V? = set(entry.key, entry.value)

    override fun remove(key: K): V? {
        val toBeRemoved = list.find { it.key == key }

        if (toBeRemoved != null) {
            list.removeSpecific(toBeRemoved)
        }
        return toBeRemoved?.value
    }

    override fun contains(key: K): Boolean {
        return list.any { it.key == key }
    }
}

fun main() {
    val map = ListBasedMap<Int, Char>()
    map[2] = 'h'
    map[3] = 'y'
    map[4] = 'u'
    println(map.values)
    println(map.get(3))
    println(map.put(4, 'n'))

}