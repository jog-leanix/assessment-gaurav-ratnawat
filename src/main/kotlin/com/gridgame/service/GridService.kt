package com.gridgame.service

import com.gridgame.model.Cell
import com.gridgame.model.Grid
import com.gridgame.model.GridRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

/**
 * GridService is responsible for managing the grid and its cells.
 * It initializes the grid and provides methods to manipulate it.
 */
@ApplicationScoped
class GridService(private val gridRepository: GridRepository) {

    @Transactional
    fun initializeGrid(rows: Int, columns: Int): Grid {
        val grid = Grid().apply {
            this.rows = rows
            this.columns = columns
        }
        grid.initialize()
        return grid
    }

    @Transactional
    fun handleCellClick(gridId: Long, row: Int, column: Int): Grid? {
        val grid = gridRepository.findById(gridId) ?: return null
        if (grid.cells.isEmpty()) {
            return null
        }
        if (!isValidPosition(row, column, grid)) {
            throw InvalidCellClickException(
                "Client clicked an invalid cell position: row=$row, column=$column for " +
                    "grid  = $gridId"
            )
        }
        incrementRowAndColumn(grid, row, column)
        detectAndClearFibonacciSequences(grid)
        return grid
    }

    private fun isValidPosition(row: Int, column: Int, grid: Grid): Boolean {
        return row in 0 until grid.rows && column in 0 until grid.columns
    }

    private fun incrementRowAndColumn(grid: Grid, row: Int, column: Int) {
        for (cell in grid.cells) {
            if (cell.row == row || cell.column == column) {
                cell.value++
            }
        }
    }

    private fun detectAndClearFibonacciSequences(grid: Grid) {
        for (row in 0 until grid.rows) {
            val cells = getCellsInRow(grid, row)
            checkAndClearSequence(cells)
        }

        for (col in 0 until grid.columns) {
            val cells = getCellsInColumn(grid, col)
            checkAndClearSequence(cells)
        }
    }

    private fun getCellsInRow(grid: Grid, row: Int) = grid.cells.filter { it.row == row }
        .sortedBy { it.column }

    private fun getCellsInColumn(grid: Grid, col: Int) = grid.cells.filter { it.column == col }
        .sortedBy { it.row }

    private fun checkAndClearSequence(cells: List<Cell>) {
        for (i in 0..cells.size - FIBONACCI_SEQUENCE_LENGTH) {
            val sequence = cells.subList(i, i + FIBONACCI_SEQUENCE_LENGTH)
            if (isFibonacciSequence(sequence.map { it.value })) {
                sequence.forEach { cell ->
                    cell.value = 0
                }
            }
        }
    }

    private fun isFibonacciSequence(sequence: List<Int>): Boolean {
        if (sequence.size < FIBONACCI_SEQUENCE_LENGTH) return false
        return sequence.windowed(3).all { (a, b, c) ->
            if (a < 0 || b < 0 || c < 0) return false
            c == a + b
        }
    }

    companion object {

        private const val FIBONACCI_SEQUENCE_LENGTH = 5
        private val fibonacciSequence = listOf(0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144)
    }
}
