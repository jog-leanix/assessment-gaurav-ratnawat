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

        // Increment values in the clicked row and column
        incrementRowAndColumn(grid, row, column)

        // Check for Fibonacci sequences and clear if found
        detectAndClearFibonacciSequences(grid)

        return grid
    }

    private fun incrementRowAndColumn(grid: Grid, row: Int, column: Int) {
        for (cell in grid.cells) {
            if (cell.row == row || cell.column == column) {
                cell.value++
            }
        }
    }

    private fun detectAndClearFibonacciSequences(grid: Grid) {
        // Check horizontal sequences
        for (row in 0 until grid.rows) {
            val cells = grid.cells.filter { it.row == row }
                .sortedBy { it.column }
            checkAndClearSequence(cells)
        }

        // Check vertical sequences
        for (col in 0 until grid.columns) {
            val cells = grid.cells.filter { it.column == col }
                .sortedBy { it.row }
            checkAndClearSequence(cells)
        }
    }

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

        val startIndex = fibonacciSequence.indexOf(sequence.first())
        if (startIndex == -1) return false

        return sequence == fibonacciSequence.subList(
            startIndex,
            startIndex + FIBONACCI_SEQUENCE_LENGTH
        )
    }

    companion object {

        private const val GRID_SIZE = 50
        private const val FIBONACCI_SEQUENCE_LENGTH = 5
        private val fibonacciSequence = listOf(0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144)
    }
}
