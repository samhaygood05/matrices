import Matrix.Companion.i

fun main() {
    var matrix: Matrix = Matrix(arrayOf(
        doubleArrayOf(1.0, 2.0, 1.0),
        doubleArrayOf(2.0, 1.0, 2.0),
        doubleArrayOf(1.0, 2.0, 1.0)))
    println(matrix.det())
}