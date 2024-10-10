package maps

class ArrayBasedMapTest : CustomMutableMapTest() {
    override fun <K, V> emptyMap(): CustomMutableMap<K, V> =
        ListBasedMap() // ArrayBasedMap()
}
