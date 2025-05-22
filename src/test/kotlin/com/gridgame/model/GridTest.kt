package com.gridgame.model

import com.gridgame.Grid
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GridTest {

    private lateinit var grid: Grid

    @BeforeEach
    fun setup() {
        grid = Grid()
    }

    @Test
    fun `test grid initialization`() {
        assertEquals(0, grid.rows)
        assertEquals(0, grid.columns)
    }

    @Test
    fun testGetCell() {
        val cell = grid.getCell(2, 3)
        assertNotNull(cell)
        assertEquals(2, cell?.row)
        assertEquals(3, cell?.column)
        assertEquals(0, cell?.value)
    }
}
