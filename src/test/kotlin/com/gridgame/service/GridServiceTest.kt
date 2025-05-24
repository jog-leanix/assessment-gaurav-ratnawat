package com.gridgame.service

import com.gridgame.exception.GridNotFoundException
import com.gridgame.exception.InvalidCellClickException
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
import org.junit.jupiter.api.assertThrows

class GridServiceTest {

    private lateinit var gridService: GridService
    private lateinit var gridRepository: GridRepository
    private lateinit var grid: Grid

    @BeforeEach
    fun setUp() {
        gridRepository = mockk<GridRepository>()
        gridService = GridService(gridRepository)
        every { gridRepository.persistAndFlush(any()) } returns Unit
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
            rows = 5
            columns = 5
            initialize()
        }
        every { gridRepository.findById(1) } returns grid

        // When
        val result = gridService.handleCellClick(1, 1, 1)

        // Then
        assertNotNull(result)
        result.cells.forEach { cell ->
            if (cell.row == 1 || cell.column == 1) {
                assertEquals(1, cell.value)
            } else {
                assertEquals(0, cell.value)
            }
        }
        verify { gridRepository.findById(1) }
    }

    @Test
    fun `should throw GridNotFoundException  when grid not found`() {
        every { gridRepository.findById(999) } returns null

        assertThrows<GridNotFoundException> {
            gridService.handleCellClick(999, 1, 1)
        }
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
        result.cells.forEach { cell ->
            assertEquals(0, cell.value, "Cell with Fibonacci number should be cleared")
        }
    }

    @Test
    fun `should throw InvalidCellClickException for empty grid`() {
        // Given
        val grid = Grid().apply {
            rows = 0
            columns = 0
            cells = mutableListOf()
        }
        every { gridRepository.findById(1) } returns grid

        // When
        assertThrows<InvalidCellClickException> { gridService.handleCellClick(1, 0, 0) }
        verify { gridRepository.findById(1) }
    }

    @Test
    fun `should throw InvalidCellClickException for invalid cell click`() {
        // Given
        val grid = Grid().apply {
            rows = 50
            columns = 50
            initialize()
        }
        every { gridRepository.findById(1) } returns grid

        // When
        assertThrows<InvalidCellClickException> {
            gridService.handleCellClick(1, 51, 49)
        }
    }

    @Test
    fun `should handle large Fibonacci number`() {
        // Given
        val grid = Grid().apply {
            rows = 1
            columns = 5
            cells = mutableListOf(
                Cell(0, 0, 232, this),
                Cell(0, 1, 376, this),
                Cell(0, 2, 609, this),
                Cell(0, 3, 986, this),
                Cell(0, 4, 1596, this)
            )
        }
        every { gridRepository.findById(1) } returns grid

        // When
        val result = gridService.handleCellClick(1, 0, 0)

        // Then
        assertNotNull(result)
        result.cells.forEach { cell ->
            assertEquals(0, cell.value, "Cells should be cleared as they form a Fibonacci sequence")
        }
    }

    @Test
    fun `should not clear large Fibonacci number cell if not in sequence`() {
        // Given
        val grid = Grid().apply {
            rows = 1
            columns = 5
            cells = mutableListOf(
                Cell(0, 0, 232, this),
                Cell(0, 1, 376, this),
                Cell(0, 2, 610, this),
                Cell(0, 3, 986, this),
                Cell(0, 4, 1596, this)
            )
        }
        every { gridRepository.findById(1) } returns grid

        // When
        val result = gridService.handleCellClick(1, 0, 0)

        // Then
        assertNotNull(result)
        result.cells.forEach { cell ->
            val expectedValue = when (cell.column) {
                0 -> 233  // 232 + 1
                1 -> 377  // 376 + 1
                2 -> 611  // 610 + 1
                3 -> 987  // 986 + 1
                else -> 1597 // 1596 + 1
            }
            assertEquals(expectedValue, cell.value, "Cell values should be incremented by 1")
        }
    }

    @Test
    fun `should increment value on consecutive clicks over the same cell`() {
        // Given
        val grid = Grid().apply {
            rows = 3
            columns = 3
            initialize()
        }
        every { gridRepository.findById(1) } returns grid

        // When - click same cell twice
        gridService.handleCellClick(1, 1, 1)
        val result = gridService.handleCellClick(1, 1, 1)

        // Then
        assertNotNull(result)
        result.cells.forEach { cell ->
            when {
                cell.row == 1 && cell.column == 1 -> assertEquals(2, cell.value)
                cell.row == 1 || cell.column == 1 -> assertEquals(2, cell.value)
                else -> assertEquals(0, cell.value)
            }
        }
    }

    @Test
    fun `should clear first sequence incase of overlapping Fibonacci sequences`() {
        // Given
        val grid = Grid().apply {
            rows = 1
            columns = 7
            cells = mutableListOf(
                Cell(0, 0, 0, this),
                Cell(0, 1, 0, this),
                Cell(0, 2, 1, this),
                Cell(0, 3, 2, this),
                Cell(0, 4, 4, this),
                Cell(0, 5, 7, this),
                Cell(0, 6, 12, this)
            )
        }
        every { gridRepository.findById(1) } returns grid

        // When
        val result = gridService.handleCellClick(1, 0, 0)

        // Then
        assertNotNull(result)
        result.cells.take(5).forEach { cell ->
            assertEquals(0, cell.value, "First sequence should be cleared")
        }
        assertEquals(8, result.cells[5].value, "Cell at index 5 should increase its value")
        assertEquals(13, result.cells[6].value, "Cell at index 6 should increase its value")
    }

    @Test
    fun `should return all grids`() {
        // Given
        val grid1 = Grid().apply {
            rows = 3
            columns = 3
            initialize()
        }
        val grid2 = Grid().apply {
            rows = 4
            columns = 4
            initialize()
        }
        val expectedGrids = listOf(grid1, grid2)
        every { gridRepository.listAll() } returns expectedGrids

        // When
        val result = gridService.getAllGrids()

        // Then
        assertEquals(expectedGrids, result)
        assertEquals(2, result.size)
        verify(exactly = 1) { gridRepository.listAll() }
    }

    @Test
    fun `should return empty list when no grids exist`() {
        // Given
        every { gridRepository.listAll() } returns emptyList()

        // When
        val result = gridService.getAllGrids()

        // Then
        assertTrue(result.isEmpty())
        verify(exactly = 1) { gridRepository.listAll() }
    }

    @Test
    fun `should return grid by id when found`() {
        // Given
        val expectedGrid = Grid().apply {
            id = 1L
            rows = 3
            columns = 3
            initialize()
        }
        every { gridRepository.findById(1L) } returns expectedGrid

        // When
        val result = gridService.getGrid(1L)

        // Then
        assertNotNull(result)
        assertEquals(expectedGrid.id, result?.id)
        assertEquals(expectedGrid.rows, result?.rows)
        assertEquals(expectedGrid.columns, result?.columns)
        verify(exactly = 1) { gridRepository.findById(1L) }
    }

    @Test
    fun `should return null when grid not found`() {
        // Given
        every { gridRepository.findById(999L) } returns null

        // When
        val result = gridService.getGrid(999L)

        // Then
        assertNull(result)
        verify(exactly = 1) { gridRepository.findById(999L) }
    }
}
