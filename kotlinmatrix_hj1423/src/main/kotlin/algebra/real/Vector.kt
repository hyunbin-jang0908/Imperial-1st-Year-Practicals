package algebra.real

data class Vector(private val elements: List<Double>) {
    init {
        require(elements.isNotEmpty())
    }

    val length = elements.size

    operator fun get(index: Int): Double =
        when {
            0 <= index || index < length -> elements[index]
            else -> throw  IndexOutOfBoundsException()
        }

    operator fun plus(v2: Vector): Vector =
        when {
            this.length != v2.length -> throw UnsupportedOperationException()
            else -> Vector(this.elements.zip(v2.elements) { a, b -> a + b })
        }

    operator fun times(scalar: Double): Vector =
        Vector(elements.map { it * scalar })

    infix fun dot(v: Vector): Double =
        when {
            this.length != v.length -> throw UnsupportedOperationException()
            else -> this.elements.zip(v.elements) { a, b -> a * b }.reduce { acc, n -> acc + n }
        }

    override fun toString(): String =
        "(" + elements.map { it.toString() }.reduce { acc, n -> "$acc, $n" } + ")"
}

operator fun Double.times(v: Vector): Vector = v * this
