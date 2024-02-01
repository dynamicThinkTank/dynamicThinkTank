package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = ConcreteSquareBoard(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = ConcreteGameBoard(createSquareBoard(width))

private fun Cell.coordinate(direction: Direction) =
    when (direction) {
        UP -> Pair(i - 1, j)
        DOWN -> Pair(i + 1, j)
        RIGHT -> Pair(i, j + 1)
        LEFT -> Pair(i, j - 1)
    }

class ConcreteSquareBoard(override val width: Int): SquareBoard {
    private val cells: List<List<Cell>> = List(width) { row ->
        List(width) { col ->
            Cell(row + 1, col + 1)
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if (i > width || j > width || i < 1 || j < 1) return null
        return cells[i - 1][j - 1]
    }

    override fun getCell(i: Int, j: Int): Cell {
        if (i > width) throw IllegalArgumentException("{i} is greater than $width")
        if (j > width) throw IllegalArgumentException("{j} is greater than $width")
        return getCellOrNull(i, j)!!
    }

    override fun getAllCells(): Collection<Cell> {
        return cells.flatten()
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        if (i > width) throw IllegalArgumentException("$i is greater than the $width")

        return jRange.filter { j -> j - 1 in cells[i - 1].indices }.map { j -> cells[i - 1][j - 1] }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        if (j > width) throw IllegalArgumentException("$j is greater than the $width")

        return iRange.filter { i -> i - 1 in cells.indices }.map { i -> cells[i - 1][j - 1] }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        val (i, j) = this.coordinate(direction)
        return getCellOrNull(i, j)
    }
}

class ConcreteGameBoard<T>(private val squareBoard: SquareBoard): GameBoard<T>, SquareBoard by squareBoard {
    private val maps: HashMap<Cell, T?> = HashMap()

    init {
        squareBoard.getAllCells().forEach {
            maps[it] = null
        }
    }

    override fun get(cell: Cell): T? {
        return maps[cell]
    }

    override fun set(cell: Cell, value: T?) {
        maps[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return maps.filter { (_, value) -> predicate(value) }.keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return filter(predicate).first()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return maps.any { (_, value) ->  predicate(value)}
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return maps.all { (_, value) -> predicate(value) }
    }

}