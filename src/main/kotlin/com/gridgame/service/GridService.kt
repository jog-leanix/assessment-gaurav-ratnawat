package com.gridgame.service

import com.gridgame.model.Grid

/**
 * GridService is responsible for managing the grid and its cells.
 * It initializes the grid and provides methods to manipulate it.
 */
class GridService {

    fun initializeGrid(rows: Int, columns: Int): Grid {
        val grid = Grid().apply {
            this.rows = rows
            this.columns = columns
        }
        grid.initialize()
        return grid
    }
}
