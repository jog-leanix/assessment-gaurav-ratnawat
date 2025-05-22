package com.gridgame.service

import com.gridgame.model.Grid
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GridServiceTest {
    private lateinit var gridService: GridService
    private lateinit var grid: Grid

    @BeforeEach
    fun setUp() {
        gridService = GridService()
        grid = Grid().apply {
            rows = 10
            columns = 10
            initialize()
        }
    }

    @Test
    fun `should initialize grid with correct size`() {
        val initializedGrid = gridService.initializeGrid(10, 10)
        assert(initializedGrid.rows == 10)
        assert(initializedGrid.columns == 10)
        assert(initializedGrid.cells.size == 100)
    }

}
