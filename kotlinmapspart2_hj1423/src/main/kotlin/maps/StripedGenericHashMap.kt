package maps

import java.util.Collections
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

const val INITIAL_BUCKETS_NUM = 16

abstract class StripedGenericHashMap<K, V>(
    bucketFactory: BucketFactory<K, V>
) : GenericHashMap<K, V>(
    bucketFactory,
    size = INITIAL_BUCKETS_NUM,
    loadFactor = 0.75
) {
    private val locks: Array<ReentrantLock> =
        Array(INITIAL_BUCKETS_NUM) { ReentrantLock() }

    private fun lockHash(key: K) = key.hashCode() % INITIAL_BUCKETS_NUM

    private fun lockAll() {
        locks.forEach { lock -> lock.lock() }
    }

    private fun releaseAll() {
        locks.forEach { lock -> lock.unlock() }
    }

    override val entries: Iterable<Entry<K, V>>
        get() = try {
                lockAll()
                val bucketsCopy: List<CustomMutableMap<K, V>> =
                    Collections.unmodifiableList(
                        super.buckets.toList()
                    )
                bucketsCopy.flatMap { bucket -> bucket.entries }
            } finally {
                releaseAll()
            }

    override val values: Iterable<V>
        get() {
            try {
                lockAll()
                val bucketsCopy: List<CustomMutableMap<K, V>> =
                    Collections.unmodifiableList(
                        super.buckets.toList()
                    )
                return bucketsCopy.flatMap { bucket -> bucket.values }
            } finally {
                releaseAll()
            }
        }

    override val keys: Iterable<K>
        get() {
            try {
                lockAll()
                val bucketsCopy: List<CustomMutableMap<K, V>> =
                    Collections.unmodifiableList(
                        super.buckets.toList()
                    )
                return bucketsCopy.flatMap { bucket -> bucket.keys }
            } finally {
                releaseAll()
            }
        }

    override fun contains(key: K): Boolean {
        locks[hash(key) % INITIAL_BUCKETS_NUM].withLock {
            return super.contains(key)
        }
    }

    override fun remove(key: K): V? {
        locks[hash(key) % INITIAL_BUCKETS_NUM].withLock {
            return super.remove(key)
        }
    }

    override fun put(entry: Entry<K, V>): V? {
        return this.put(entry.key, entry.value)
    }

    override fun put(key: K, value: V): V? {
        locks[lockHash(key)].withLock {
            val prevVal: V? = super.buckets[super.hash(key)].put(key, value)
            if (prevVal == null) super.currentSize++

            if (super.currentSize > loadFactor * size) {
                println("------resize, from $size")
                try {
                    lockAll()  // acquire all the locks
                    if (currentSize > size * loadFactor) {  // still needs resizing?
                        println("real resize, from $size")
                        size *= 2
                        val oldBuckets = super.buckets
                        super.buckets = Array(size) { bucketFactory() }
                        oldBuckets.forEach { bucket ->
                            bucket.entries.forEach { entry ->
                                this.put(entry)
//                                buckets[hash(entry.key)].put(entry.key, entry.value)
                            }
                        }
                    }
                } finally {
                    releaseAll()  // release all the locks
                }
            }
            return prevVal
        }
    }

    override fun set(key: K, value: V): V? = this.put(key, value)

    override fun get(key: K): V? {
        locks[lockHash(key)].withLock {
            return super.get(key)
        }
    }
}

class StripedHashMapBackedByLists<K, V>: StripedGenericHashMap<K, V>(
    { ListBasedMap() }
)

class StripedHashMapBackedByTrees<K, V>(comparator: Comparator<K>): StripedGenericHashMap<K, V>(
    { TreeBasedMap(comparator) }
)
