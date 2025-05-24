package com.gridgame.service

import com.gridgame.exception.GridNotFoundException
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
        gridRepository.persistAndFlush(grid)
        return grid
    }

    @Transactional
    fun handleCellClick(gridId: Long, row: Int, column: Int): Grid {
        val grid = gridRepository.findById(gridId) ?: throw GridNotFoundException("Grid not found: id = $gridId")
        grid.validateCellClick(row, column)
        grid.incrementRowAndColumn(row, column)
        grid.detectAndClearFibonacciSequences()

        gridRepository.persistAndFlush(grid)
        return grid
    }

    @Transactional
    fun getAllGrids(): List<Grid> {
        return gridRepository.listAll()
    }

    @Transactional
    fun getGrid(id: Long): Grid? {
        return gridRepository.findById(id)
    }
}
