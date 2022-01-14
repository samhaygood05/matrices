fun main() {
    var matrix: Matrix = Matrix(arrayOf(doubleArrayOf(1.0, 2.0),
        doubleArrayOf(3.0, 4.0),
        doubleArrayOf(1.0, 2.0)))
    println(matrix)
    println(matrix.minor(1,1))
}