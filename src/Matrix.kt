import java.lang.StringBuilder
import kotlin.math.pow

operator fun Double.times(n: Matrix) = n * this
operator fun Int.times(n: Matrix) = n * this

class Matrix {
    var matrix: Array<DoubleArray>

    constructor(i: Int, j: Int) {
        matrix = Array(i) { DoubleArray(j) }
    }
    constructor(matrix: Array<DoubleArray>) {
        val size: Int = matrix[0].size
        for (i in 1 until matrix.size) {
            require(matrix[i].size == size) { "Matrix has missing elements" }
        }
        this.matrix = matrix
    }

    fun subMatrix(i: Int, j: Int): Matrix {
        val minorMatrix = Array(matrix.size - 1) { DoubleArray(matrix[0].size - 1) }
        var k = 0
        var l = 0
        while (k < matrix.size) {
            if (k != i) {
                var m = 0
                var n = 0
                while (m < matrix[0].size) {
                    if (m != j) {
                        minorMatrix[l][n++] = matrix[k][m]
                    }
                    m++
                }
                l++
            }
            k++
        }
        return Matrix(minorMatrix)
    }

    operator fun plus(that: Matrix): Matrix {
        require(areSameSize(that)) { "A ${matrix.size}x${matrix[0].size} cannot be added with a ${that.matrix.size}x${that.matrix[0].size} matrix" }
        val temp = Matrix(matrix.size, matrix[0].size)
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                temp.matrix[i][j] = matrix[i][j] + that.matrix[i][j]
            }
        }
        return temp
    }
    operator fun minus(that: Matrix): Matrix = this + -that

    fun dotProd(that: Matrix, i: Int, j: Int): Double {
        require(matrix[0].size == that.matrix.size) { "A ${matrix.size}x${matrix[0].size} cannot be multiplied by a ${that.matrix.size}x${that.matrix[0].size} matrix" }
        var temp = 0.0
        for (k in 0 until matrix[0].size) {
            temp += matrix[i][k] * that.matrix[k][j]
        }
        return temp
    }

    operator fun times(n: Double): Matrix {
        val temp = this
        for (i in matrix.indices) {
            for (j in 0 until matrix[0].size) {
                temp.matrix[i][j] *= n
            }
        }
        return temp
    }
    operator fun times(n: Int): Matrix {
        val temp = this
        for (i in matrix.indices) {
            for (j in 0 until matrix[0].size) {
                temp.matrix[i][j] = n * temp.matrix[i][j]
            }
        }
        return temp
    }
    operator fun times(that: Matrix): Matrix {
        val temp = Matrix(matrix.size, that.matrix[0].size)
        for (i in matrix.indices) {
            for (j in that.matrix[0].indices) {
                temp.matrix[i][j] = dotProd(that, i, j)
            }
        }
        return temp
    }

    fun transpose(): Matrix {
        val temp = Matrix(matrix[0].size, matrix.size)
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                temp.matrix[j][i] = matrix[i][j]
            }
        }
        return temp
    }
    fun trace(): Double {
        var temp = 0.0;
        require(isSquareMatrix) { "Matrix is not a square" }
        for (i in matrix.indices) {
            temp += matrix[i][i]
        }
        return temp
    }

    operator fun unaryPlus(): Matrix = this
    operator fun unaryMinus(): Matrix = -1 * this

    val isSquareMatrix: Boolean get() = matrix.size == matrix[0].size
    fun areSameSize(that: Matrix): Boolean = matrix.size == that.matrix.size&& matrix[0].size == that.matrix[0].size


    fun cofactor(i: Int,j: Int): Double {
        return (-1.0).pow(i + j) * subMatrix(i, j).det()
    }
    fun det(): Double {
        require(isSquareMatrix) { "Matrix is not a square" }
        return if (matrix.size == 1) matrix[0][0]
        else if (matrix.size == 2) matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1]
        else {
            var temp = 0.0
            for (i in matrix.indices) {
                temp += matrix[i][0] * cofactor(i,0)
            }
            temp
        }
    }
    fun inv(): Matrix {
        require(isSquareMatrix) { "Matrix is not a square" }
        val temp = Matrix(matrix.size, matrix[0].size)
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                temp.matrix[i][j] = 1/det() * cofactor(i, j)
            }
        }
        return temp
    }


    override fun toString(): String {
        val temp = StringBuilder()
        for (i in matrix.indices) {
            if (i == 0) temp.append("｢ ") else if (i == matrix.size - 1) temp.append("  ") else temp.append("| ")
            for (j in 0 until matrix[i].size) {
                if (matrix[i][j] == 0.0) temp.append("0.0")
                else temp.append(matrix[i][j])
                if (j != matrix[0].size - 1) temp.append("  ")
            }
            if (i == 0) temp.append(" \n") else if (i == matrix.size - 1) temp.append(" ｣") else temp.append(" |\n")
        }
        return temp.toString()
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