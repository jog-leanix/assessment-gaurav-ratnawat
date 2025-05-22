package com.gridgame.service

import com.gridgame.model.Grid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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


    @Test
    fun `should handle cell click and increment values`() {
        // Given
        val grid = Grid().apply {
            rows = 3
            columns = 3
            initialize()
        }

        // When
        val result = gridService.handleCellClick(1, 1, 1)

        // Then
        assertNotNull(result)
        result!!.cells.forEach { cell ->
            if (cell.row == 1 || cell.column == 1) {
                assertEquals(1, cell.value)
            } else {
                assertEquals(0, cell.value)
            }
        }
    }

}
