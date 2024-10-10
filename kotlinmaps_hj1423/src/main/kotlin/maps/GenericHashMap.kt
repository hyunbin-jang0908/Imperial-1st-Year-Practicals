package maps

private const val LOAD_FACTOR = 0.75
private const val DEFAULT_SIZE = 16

typealias BucketFactory<K, V> = () -> CustomMutableMap<K, V>

abstract class GenericHashMap<K, V>(
    private val bucketFactory: BucketFactory<K, V>,
    private var size: Int,
    private val loadFactor: Double
) : CustomMutableMap<K, V> {
    private var buckets = Array(size) { bucketFactory() }
    private var currentSize = 0

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
            buckets += List(size) { bucketFactory() }
            size = buckets.size
        }
        return buckets[hash(key)].put(key, value)
    }

    override fun set(key: K, value: V): V? = put(key, value)

    override fun get(key: K): V? = buckets[hash(key)][key]

    private fun hash(key: K): Int = key.hashCode() and size
}

class HashMapBackedByLists<K, V> : GenericHashMap<K, V>(
    bucketFactory = { ListBasedMap() },
    size = DEFAULT_SIZE,
    loadFactor = LOAD_FACTOR
)
