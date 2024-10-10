package algebra.generic

data class Vector<T>(
    val addition: (T, T) -> T,
    val multiplication: (T, T) -> T,
    private val elements: List<T>
) {
    init {
        require(elements.isNotEmpty())
    }

    val length = elements.size

    operator fun get(index: Int): T =
        when {
            0 <= index || index < length -> elements[index]
            else -> throw  IndexOutOfBoundsException()
        }

    operator fun plus(v2: Vector<T>): Vector<T> =
        when {
            this.length != v2.length -> throw UnsupportedOperationException()
            else -> Vector(
                addition,
                multiplication,
                this.elements.zip(v2.elements) { a, b -> addition(a, b) }
            )
        }

    operator fun times(scalar: T): Vector<T> =
        Vector(addition, multiplication, elements.map { multiplication(it, scalar) })

    infix fun dot(v: Vector<T>): T =
        when {
            this.length != v.length -> throw UnsupportedOperationException()
            else -> this.elements.zip(v.elements) { a, b -> multiplication(a, b) }
                .reduce { acc, n -> addition(acc, n) }
        }

    override fun toString(): String =
        "(" + elements.map { it.toString() }.reduce { acc, n -> "$acc, $n" } + ")"
}

operator fun <T> T.times(v: Vector<T>): Vector<T> =
    Vector(v.addition, v.multiplication, (0 until v.length).map { i -> v.multiplication(this, v[i]) })
