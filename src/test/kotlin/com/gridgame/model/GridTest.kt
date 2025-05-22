package com.gridgame.model

import com.gridgame.Grid
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GridTest {

    private lateinit var grid: Grid

    @BeforeEach
    fun setUp() {
        grid = Grid()
        grid.initialize()
    }

    @Test
    fun `should initialize grid with zero value cells`() {
        assertEquals(5, grid.rows)
        assertEquals(5, grid.columns)
        assertEquals(25, grid.cells.size)

        for (cell in grid.cells) {
            assertEquals(0, cell.value)
        }
    }

}
