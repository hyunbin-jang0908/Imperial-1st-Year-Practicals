package algebra.generic

data class Matrix<T>(
    val addition: (T, T) -> T,
    val multiplication: (T, T) -> T,
    private val rows: List<Vector<T>>
) {
    init {
        require(rows.isNotEmpty())
        require(rows.none { it.length != rows[0].length })
    }

    val numRows = rows.size
    val numColumns = rows[0].length

    operator fun get(index: Int): Vector<T> =
        when {
            0 <= index || index < numRows -> rows[index]
            else -> throw  IndexOutOfBoundsException()
        }

    fun getRow(index: Int): Vector<T> = this[index]

    fun getColumn(index: Int): Vector<T> =
        when {
            0 <= index || index < numColumns ->
                Vector(addition, multiplication, rows.map { it[index] })
            else -> throw  IndexOutOfBoundsException()
        }

    operator fun get(i: Int, j: Int): T = rows[i][j]

    operator fun plus(m: Matrix<T>): Matrix<T> =
        when {
            this.numColumns != m.numColumns || this.numRows != m.numRows ->
                throw UnsupportedOperationException()
            else -> Matrix(addition, multiplication, this.rows.zip(m.rows) { a, b -> a + b })
        }

    operator fun times(m: Matrix<T>): Matrix<T> =
        when {
            this.numColumns != m.numRows -> throw UnsupportedOperationException()
            else -> {
                val newRows = (0 until this.numRows).map { i ->
                    Vector(
                        addition,
                        multiplication,
                        (0 until m.numColumns).map { j -> this.rows[i] dot m.getColumn(j) }
                    )
                }
                Matrix(addition, multiplication, newRows)
            }
        }

    operator fun times(s: T): Matrix<T> =
        Matrix(addition, multiplication, rows.map { it * s })

    override fun toString(): String {
        // Calculate the maximum character lengths for each column
        val maxChar = (0 until numColumns).map { col ->
            (0 until numRows).maxOfOrNull { row ->
                rows[row][col].toString().length
            }
        }

        return (0 until numRows).map { i ->
            "[ " + (0 until numColumns).map { j ->
                val elementString = rows[i][j].toString()
                " ".repeat(maxChar[j]?.minus(elementString.length) ?: 0) + elementString
            }
                .joinToString(" ") + " ]"
        }.joinToString("\n")
        // .reduce {acc, n -> "$acc\n$n"}   works too
    }
}

operator fun <T> T.times(m: Matrix<T>): Matrix<T> =
    Matrix(m.addition, m.multiplication, (0 until m.numRows).map { i -> this * m.getRow(i) })
