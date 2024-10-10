package algebra.real

data class Matrix(private val rows: List<Vector>) {
    init {
        require(rows.isNotEmpty())
        require(rows.none { it.length != rows[0].length })
    }

    val numRows = rows.size
    val numColumns = rows[0].length

    operator fun get(index: Int): Vector =
        when {
            0 <= index || index < numRows -> rows[index]
            else -> throw  IndexOutOfBoundsException()
        }

    fun getRow(index: Int): Vector = this[index]

    fun getColumn(index: Int): Vector =
        when {
            0 <= index || index < numColumns -> Vector(rows.map { it[index] })
            else -> throw  IndexOutOfBoundsException()
        }

    operator fun get(i: Int, j: Int): Double = rows[i][j]

    operator fun plus(m: Matrix): Matrix =
        when {
            this.numColumns != m.numColumns || this.numRows != m.numRows ->
                throw UnsupportedOperationException()
            else -> Matrix(this.rows.zip(m.rows) { a, b -> a + b })
        }

    operator fun times(m: Matrix): Matrix =
        when {
            this.numColumns != m.numRows -> throw UnsupportedOperationException()
            else -> {
                val newRows = (0 until this.numRows).map { i ->
                    Vector(
                        (0 until m.numColumns).map { j ->
                            this.rows[i] dot m.getColumn(j)
                        }
                    )
                }
                Matrix(newRows)
            }
        }

    operator fun times(s: Double): Matrix =
        Matrix(rows.map { it * s })

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
            }.joinToString(" ") + " ]"
        }.joinToString("\n")
        // .reduce {acc, n -> "$acc\n$n"} works too
    }
}

operator fun Double.times(m: Matrix): Matrix = m * this
