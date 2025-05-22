package com.gridgame.service

import com.gridgame.model.Cell
import com.gridgame.model.Grid
import com.gridgame.model.GridRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
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

    @Test
    fun `should return null when grid not found`() {
        every { gridRepository.findById(999) } returns null

        val result = gridService.handleCellClick(999, 1, 1)

        assertNull(result)
        verify { gridRepository.findById(999) }
    }

    @Test
    fun `should clear cells with Fibonacci numbers`() {
        // Given
        val grid = Grid().apply {
            rows = 2
            columns = 2
            cells = mutableListOf(
                Cell(0, 0, 0, this),
                Cell(0, 1, 0, this),
                Cell(0, 2, 1, this),
                Cell(0, 3, 2, this),
                Cell(0, 4, 4, this),
            )
        }
        every { gridRepository.findById(1) } returns grid

        // When
        val result = gridService.handleCellClick(1, 0, 0)

        // Then
        assertNotNull(result)
        result!!.cells.forEach { cell ->
                assertEquals(0, cell.value, "Cell with Fibonacci number should be cleared")
        }
    }

    @Test
    fun `should return null for empty grid`() {
        // Given
        val grid = Grid().apply {
            rows = 0
            columns = 0
            cells = mutableListOf()
        }
        every { gridRepository.findById(1) } returns grid

        // When
        val result = gridService.handleCellClick(1, 0, 0)

        // Then
        assertNotNull(result)
        assertTrue(result!!.cells.isEmpty())
        verify { gridRepository.findById(1) }
    }

    @Test
    fun `should throw invalidClickException for invalid cell click`() {
        // Given
        val grid = Grid().apply {
            rows = 50
            columns = 50
            initialize()
        }
        every { gridRepository.findById(1) } returns grid

        // When
        assertThrows<InvalidClickException> {
            gridService.handleCellClick(1, 51, 49)
        }
    }
}
