import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.pow

/** Multiplies this value by the other value. */
operator fun Double.times(n: Matrix) = n * this
/** Multiplies this value by the other value. */
operator fun Int.times(n: Matrix) = n * this

/** Divides this value by the other value. */
operator fun Double.div(n: Matrix) = this * n.inv()
/** Divides this value by the other value. */
operator fun Int.div(n: Matrix) = this * n.inv()

class Matrix {
    var matrix: Array<DoubleArray>
    var rows: Int
    var columns: Int

    constructor(i: Int, j: Int) {
        matrix = Array(i) { DoubleArray(j) }
        rows = i
        columns = j

    }
    constructor(matrix: Array<DoubleArray>) {
        val size: Int = matrix[0].size
        for (i in 1 until matrix.size) {
            require(matrix[i].size == size) { "Matrix has missing elements" }
        }
        this.matrix = matrix
        rows = matrix.size
        columns = matrix[0].size
    }

    /**
     * Returns the submatrix with row [i] and column [j] removed
     */
    fun subMatrix(i: Int, j: Int): Matrix {
        val subMatrix = Array(rows - 1) { DoubleArray(columns - 1) }
        var k = 0
        var l = 0
        while (k < rows) {
            if (k != i) {
                var m = 0
                var n = 0
                while (m < columns) {
                    if (m != j) {
                        subMatrix[l][n++] = matrix[k][m]
                    }
                    m++
                }
                l++
            }
            k++
        }
        return Matrix(subMatrix)
    }

    /** Adds the other value to this value. */
    operator fun plus(that: Matrix): Matrix {
        require(areSameSize(that)) { "A ${rows}x${columns} cannot be added with a ${that.rows}x${that.columns} matrix" }
        val temp = Matrix(rows, columns)
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                temp.matrix[i][j] = matrix[i][j] + that.matrix[i][j]
            }
        }
        return temp
    }

    /** Subtracts the other value from this value. */
    operator fun minus(that: Matrix): Matrix = this + -that

    /**
     * Returns the dot product of row [i] and column [j] between two matrices
     *
     * Note:
     * - the row length of the first matrix must be equal to the column length of the second matrix
     */
    fun dotProd(that: Matrix, i: Int, j: Int): Double {
        require(columns == that.rows) { "A ${rows}x${columns} cannot be multiplied by a ${that.rows}x${that.columns} matrix" }
        var temp = 0.0
        for (k in 0 until columns) {
            temp += matrix[i][k] * that.matrix[k][j]
        }
        return temp
    }

    /** Multiplies this value by the other value. */
    operator fun times(n: Double): Matrix {
        val temp = this
        for (i in matrix.indices) {
            for (j in 0 until columns) {
                temp.matrix[i][j] *= n
            }
        }
        return temp
    }
    /** Multiplies this value by the other value. */
    operator fun times(n: Int): Matrix {
        val temp = this
        for (i in matrix.indices) {
            for (j in 0 until columns) {
                temp.matrix[i][j] = n * temp.matrix[i][j]
            }
        }
        return temp
    }
    /** Multiplies this value by the other value. */
    operator fun times(that: Matrix): Matrix {
        val temp = Matrix(rows, that.columns)
        for (i in matrix.indices) {
            for (j in that.matrix[0].indices) {
                temp.matrix[i][j] = dotProd(that, i, j)
            }
        }
        return temp
    }

    /** Divides this value by the other value. */
    operator fun div(that: Double): Matrix = this * 1/that
    /** Divides this value by the other value. */
    operator fun div(that: Int): Matrix = this * 1.0/that
    /** Divides this value by the other value. */
    operator fun div(that: Matrix): Matrix = this * 1/that

    fun transpose(): Matrix {
        val temp = Matrix(columns, rows)
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                temp.matrix[j][i] = matrix[i][j]
            }
        }
        return temp
    }
    fun trace(): Double {
        var temp = 0.0
        require(isSquareMatrix) { "Matrix is not a square" }
        for (i in matrix.indices) {
            temp += matrix[i][i]
        }
        return temp
    }

    /** Returns this value. */
    operator fun unaryPlus(): Matrix = this
    /** Returns the negative of this value. */
    operator fun unaryMinus(): Matrix = -1 * this

    val isSquareMatrix: Boolean get() = rows == columns
    val isInvertible: Boolean get() = det() != 0.0
    fun areSameSize(that: Matrix): Boolean = rows == that.rows && columns == that.columns

    fun cofactor(i: Int,j: Int): Double {
        return (-1.0).pow(i + j) * subMatrix(i, j).det()
    }
    fun det(): Double {
        require(isSquareMatrix) { "Matrix is not a square" }
        return if (rows == 1) matrix[0][0]
        else if (rows == 2) matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1]
        else {
            var temp = 0.0
            for (i in matrix.indices) {
                temp += matrix[i][0] * cofactor(i,0)
            }
            temp
        }
    }
    fun inv(): Matrix {
        require(isInvertible) { "Matrix is non-invertible" }
        val temp = Matrix(rows, columns)
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                temp.matrix[i][j] = 1/det() * cofactor(i, j)
            }
        }
        return temp
    }


    override fun toString(): String {
        val temp = StringBuilder()
        val maxLengthPerColumn: MutableList<Int> = mutableListOf()
        for (j in matrix[0].indices) {
            maxLengthPerColumn.add(longestNumber(j))
        }
        for (i in matrix.indices) {
            if (i == 0) temp.append("┌ ") else if (i == rows - 1) temp.append("└ ") else temp.append("│ ")
            for (j in 0 until matrix[i].size) {
                if (matrix[i][j] == 0.0) temp.append("0.0")
                else temp.append(matrix[i][j])
                for (k in 1..maxLengthPerColumn[j] - matrix[i][j].toString().length) temp.append(" ")
                if (j != columns - 1) temp.append("   ")
            }
            if (i == 0) temp.append(" ┐\n") else if (i == rows - 1) temp.append(" ┘") else temp.append(" │\n")
        }
        return temp.toString()
    }

    private fun longestNumber(j: Int): Int {
        var temp = 0
        for (i in matrix.indices) {
            temp = max(temp, matrix[i][j].toString().length)
        }
        return temp
    }

    companion object {
        fun i(n: Int): Matrix {
            val temp = Matrix(n, n)
            for (i in temp.matrix.indices) {
                temp.matrix[i][i] = 1.0
            }
            return temp
        }
    }
}