package maps

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

abstract class LockedMapC<K, V>(
    private val map: CustomMutableMap<K, V>
) : CustomMutableMap<K, V> {
    private val lock: ReentrantLock = ReentrantLock()

    override val entries: Iterable<Entry<K, V>>
        get (){
            lock.withLock {
                val mapCopy = this.map
                return mapCopy.entries
            }
        }

    override val keys: Iterable<K>
        get() {
            lock.withLock {
                val mapCopy = this.map
                return mapCopy.keys
            }
        }

    override val values: Iterable<V>
        get() {
            lock.withLock {
                val mapCopy = this.map
                return mapCopy.values
            }
        }

    override fun get(key: K): V? {
        lock.withLock {
            return this.map.get(key)
        }
    }

    override fun set(
        key: K,
        value: V,
    ): V? {
        lock.withLock {
            return this.map.set(key, value)
        }
    }

    override fun put(
        key: K,
        value: V,
    ): V? {
        lock.withLock {
            return this.map.put(key, value)
        }
    }

    override fun put(entry: Entry<K, V>): V? {
        lock.withLock {
            return this.map.put(entry)
        }
    }

    override fun remove(key: K): V? {
        lock.withLock {
            return this.map.remove(key)
        }
    }

    override fun contains(key: K): Boolean {
        lock.withLock {
            return this.map.contains(key)
        }
    }
}

class LockedListBasedMapC<K, V>() : LockedMap<K, V>(ListBasedMap())

class LockedHashMapBackedByListsC<K, V>(
    size: Int,
    loadFactor: Double
) : GenericHashMap<K, V>(
    { LockedListBasedMap() },
    size,
    loadFactor
)
