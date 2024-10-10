package maps

private const val LOAD_FACTOR = 0.75
private const val DEFAULT_SIZE = 16

typealias BucketFactory<K, V> = () -> CustomMutableMap<K, V>

abstract class GenericHashMap<K, V>(
    protected val bucketFactory: BucketFactory<K, V>,
    protected var size: Int,
    protected val loadFactor: Double
) : CustomMutableMap<K, V> {
    protected var buckets = Array(size) { bucketFactory() }
    protected var currentSize = 0

    override val entries: Iterable<Entry<K, V>>
        get() = buckets.flatMap { it.entries }

    override val keys: Iterable<K>
        get() = buckets.flatMap { it.keys }

    override val values: Iterable<V>
        get() = buckets.flatMap { it.values }

    override fun contains(key: K): Boolean = buckets[hash(key)].contains(key)

    override fun remove(key: K): V? {
        currentSize--
        return buckets[hash(key)].remove(key)
    }

    override fun put(entry: Entry<K, V>): V? = put(entry.key, entry.value)

    override fun put(key: K, value: V): V? {
        currentSize++
        if (currentSize > size * loadFactor) {
            val oldBuckets = buckets
            size *= 2
            buckets = Array(size) { bucketFactory() }
            for (bucket in oldBuckets) {
                for (entry in bucket.entries) {
                    val bucketIndex = hash(entry.key)
                    buckets[bucketIndex].put(entry.key, entry.value)
                }
            }
        }
        return buckets[hash(key)].put(key, value)
    }

    override fun set(key: K, value: V): V? = put(key, value)

    override fun get(key: K): V? = buckets[hash(key)][key]

    protected fun hash(key: K): Int = key.hashCode() and (size-1)
}

class HashMapBackedByLists<K, V> : GenericHashMap<K, V>(
    bucketFactory = { ListBasedMap() },
    size = DEFAULT_SIZE,
    loadFactor = LOAD_FACTOR
)
