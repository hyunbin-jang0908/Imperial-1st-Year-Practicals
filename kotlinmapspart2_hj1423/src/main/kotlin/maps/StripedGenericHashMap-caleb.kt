package maps

import java.util.Collections
import java.util.concurrent.locks.ReentrantLock
import kotlin.Comparator
import kotlin.concurrent.withLock

abstract class StripedGenericHashMapC<K, V>(
    bucketFactory: BucketFactory<K, V>,
    size: Int,
    loadFactor: Double,
) : GenericHashMap<K, V>(bucketFactory, size, loadFactor) {
    private val initialSize: Int = size
    private val lockList: Array<ReentrantLock> = Array(initialSize) { ReentrantLock() }

    private fun lockHash(key: K) = key.hashCode() % initialSize

    private fun acquireAll() {
        lockList.forEach { lock ->
            lock.lock()
        }
    }

    private fun releaseAll() {
        lockList.forEach { lock ->
            lock.unlock()
        }
    }

    override val entries: Iterable<Entry<K, V>>
        get() {
            acquireAll()
            try {
                val bucketsCopy: List<CustomMutableMap<K, V>> =
                    Collections.unmodifiableList(
                        super.buckets.toList(),
                    )
                return bucketsCopy.flatMap { bucket -> bucket.entries }
            } finally {
                releaseAll()
            }
        }

    override val keys: Iterable<K>
        get () {
            acquireAll()
            try {
                val bucketsCopy: List<CustomMutableMap<K, V>> =
                    Collections.unmodifiableList(
                        super.buckets.toList(),
                    )
                return bucketsCopy.flatMap { bucket -> bucket.keys }
            } finally {
                releaseAll()
            }
        }

    override val values: Iterable<V>
        get() {
            acquireAll()
            try {
                val bucketsCopy: List<CustomMutableMap<K, V>> =
                    Collections.unmodifiableList(
                        super.buckets.toList(),
                    )
                return bucketsCopy.flatMap { bucket -> bucket.values }
            } finally {
                releaseAll()
            }
        }

    override fun get(key: K): V? {
        lockList[lockHash(key)].withLock {
            return super.get(key)
        }
    }

    override fun set(
        key: K,
        value: V,
    ): V? = this.put(key, value)

    override fun put(key: K, value: V, ): V? {
        lockList[lockHash(key)].withLock {
            val prevValue: V? = super.buckets[super.hash(key)].put(key, value)
            if (prevValue == null) {
                super.currentSize++
            }
            if (super.currentSize > loadFactor * size) {
                this.resize()
            }
            return prevValue
        }
    }

    private fun resize() {
        try {
            val oldSize: Int = size
            acquireAll()
            if (oldSize != size) {  // still needs resizing?
                return
            }
            size *= 2
            val oldBuckets: Array<CustomMutableMap<K, V>> = super.buckets
            super.buckets = Array(size) { bucketFactory() }
            oldBuckets.forEach { bucket ->
                bucket.entries.forEach { entry ->
                    this.put(entry)
                }
            }
        } finally {
            releaseAll()
        }
    }

    override fun put(entry: Entry<K, V>): V? =
        this.put(entry.key, entry.value)

    override fun remove(key: K): V? {
        lockList[lockHash(key)].withLock {
            return super.remove(key)
        }
    }

    override fun contains(key: K): Boolean {
        lockList[lockHash(key)].withLock {
            return super.contains(key)
        }
    }
}

class StripedHashMapBackedByListsC<K, V>: StripedGenericHashMap<K, V>(
    { ListBasedMap() }
)

class StripedHashMapBackedByTreesC<K, V>(
    private val keyComparator: Comparator<K>
) : StripedGenericHashMap<K, V>(
    { TreeBasedMap(keyComparator) }
)
