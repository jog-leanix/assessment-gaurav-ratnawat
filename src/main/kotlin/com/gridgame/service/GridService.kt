package com.gridgame.service

import com.gridgame.model.Grid
import com.gridgame.model.GridRepository
import jakarta.enterprise.context.ApplicationScoped

/**
 * GridService is responsible for managing the grid and its cells.
 * It initializes the grid and provides methods to manipulate it.
 */
@ApplicationScoped
class GridService(private val gridRepository: GridRepository) {

    // Fibonacci numbers for sequence detection
    private val fibonacciSequence = listOf(0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144)

    fun initializeGrid(rows: Int, columns: Int): Grid {
        val grid = Grid().apply {
            this.rows = rows
            this.columns = columns
        }
        grid.initialize()
        return grid
    }

    // need to handle the cell click event
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
        for (cell in grid.cells) {
            if (fibonacciSequence.contains(cell.value)) {
                cell.value = 0
            }
        }
    }
}
