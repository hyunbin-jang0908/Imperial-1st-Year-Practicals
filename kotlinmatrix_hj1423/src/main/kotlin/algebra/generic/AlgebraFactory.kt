package algebra.generic

data class AlgebraFactory<T>(
    val plus: (T, T) -> T,
    val times: (T, T) -> T
) {
    fun makeVector(elements: List<T>): Vector<T> =
        Vector(plus, times, elements)

    fun makeMatrix(elements: List<List<T>>): Matrix<T> =
        Matrix(plus, times, elements.map { Vector(plus, times, it) })
}
