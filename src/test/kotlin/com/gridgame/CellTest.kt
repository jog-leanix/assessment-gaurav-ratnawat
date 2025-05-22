package com.gridgame

import com.gridgame.model.Cell
import com.gridgame.model.Grid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CellTest {

    private lateinit var grid: Grid
    private lateinit var cell: Cell

    @BeforeEach
    fun setUp() {
        grid = Grid()
        cell = Cell(row = 1, column = 2, value = 0, grid = grid)
    }

    @Test
    fun `should initialize the cells `() {
        assertEquals(1, cell.row)
        assertEquals(2, cell.column)
        assertEquals(0, cell.value)
        assertSame(grid, cell.grid)
    }
}
