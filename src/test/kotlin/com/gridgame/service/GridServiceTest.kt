package com.gridgame.service

import com.gridgame.model.Grid
import com.gridgame.model.GridRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GridServiceTest {

    private lateinit var gridService: GridService
    private lateinit var gridRepository: GridRepository
    private lateinit var grid: Grid

    @BeforeEach
    fun setUp() {
        gridRepository = mockk<GridRepository>()
        gridService = GridService(gridRepository)
    }

    @Test
    fun `should initialize grid with correct size`() {
        grid = Grid().apply {
            rows = 10
            columns = 10
            initialize()
        }
        val initializedGrid = gridService.initializeGrid(10, 10)
        assert(initializedGrid.rows == 10)
        assert(initializedGrid.columns == 10)
        assert(initializedGrid.cells.size == 100)
    }

    @Test
    fun `should handle cell click and increment values`() {
        // Given
        val grid = Grid().apply {
            rows = 0
            columns = 0
            initialize()
        }
        every { gridRepository.findById(1) } returns grid

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
        verify { gridRepository.findById(1) }
    }
}
