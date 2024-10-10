package maps

private const val LOAD_FACTOR = 0.75
private const val DEFAULT_SIZE = 16

class HashMapBackedByTrees<K, V>(comparator: Comparator<K>) : GenericHashMap<K, V>(
    bucketFactory = { TreeBasedMap(comparator) },
    size = DEFAULT_SIZE,
    loadFactor = LOAD_FACTOR
)
