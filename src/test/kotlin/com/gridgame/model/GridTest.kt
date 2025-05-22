package com.gridgame.model

import com.gridgame.Grid
import org.junit.jupiter.api.Assertions.assertEquals
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

}
