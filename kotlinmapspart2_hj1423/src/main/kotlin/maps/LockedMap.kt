package maps

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

abstract class LockedMap<K, V>(
    private val targetMap: CustomMutableMap<K, V>
) : CustomMutableMap<K, V> {
    private val lock = ReentrantLock()

    override val entries: Iterable<Entry<K, V>>
        get() = lock.withLock {
                return ArrayList(targetMap.entries.map { it.copy() })
            }

    override val keys: Iterable<K>
        get() = lock.withLock {
                return ArrayList(targetMap.keys.toList())
            }

    override val values: Iterable<V>
        get() {
            lock.withLock {
                return ArrayList(targetMap.values.toList())
            }
        }

    override fun get(key: K): V? {
        lock.withLock { return targetMap[key] }
    }

    override fun set(key: K, value: V): V? {
        lock.withLock { return targetMap.set(key, value) }
    }

    override fun put(key: K, value: V): V? {
        lock.withLock { return targetMap.put(key, value) }
    }

    override fun put(entry: Entry<K, V>): V? {
        lock.withLock { return targetMap.put(entry) }
    }

    override fun remove(key: K): V? {
        lock.withLock {
            return targetMap.remove(key)
        }
    }

    override fun contains(key: K): Boolean {
        lock.withLock {
            return targetMap.contains(key)
        }
    }
}

class LockedListBasedMap<K, V>() : LockedMap<K, V>(ListBasedMap())

class LockedHashMapBackedByLists<K, V>() : LockedMap<K, V>(HashMapBackedByLists())
