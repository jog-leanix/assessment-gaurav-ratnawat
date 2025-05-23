package com.gridgame.service

import com.gridgame.model.Grid
import com.gridgame.model.GridRepository
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
@QuarkusTestResource(PostgresTestResource::class)
class GridServiceIntegrationTest {

    @Inject
    lateinit var gridService: GridService

    @Inject
    lateinit var gridRepository: GridRepository

    private lateinit var testGrid: Grid

    @BeforeEach
    @Transactional
    fun setUp() {
        gridRepository.listAll().forEach { grid ->
            gridRepository.delete(grid)
        }
        testGrid = gridService.initializeGrid(5, 5)
    }

    @Test
    @Transactional
    fun `should initialize grid with correct data`() {
        // When
        val grid = gridService.initializeGrid(5, 5)

        // Then
        val persistedGrid = gridRepository.findById(grid.id!!)
        assertNotNull(persistedGrid)
        assertEquals(5, persistedGrid?.rows)
        assertEquals(5, persistedGrid?.columns)
        assertEquals(25, persistedGrid?.cells?.size)
    }

    @Test
    @Transactional
    fun `should increment row and column values on cell click`() {
        // When
        val updatedGrid = gridService.handleCellClick(testGrid.id!!, 2, 3)

        // Then
        assertNotNull(updatedGrid)
        updatedGrid?.cells?.forEach { cell ->
            when {
                cell.row == 2 -> assertEquals(1, cell.value)
                cell.column == 3 -> assertEquals(1, cell.value)
                else -> assertEquals(0, cell.value)
            }
        }
    }

    @Test
    @Transactional
    fun `should clear Fibonacci sequence in row`() {
        // Given
        val grid = gridRepository.findById(testGrid.id!!)!!
        grid.cells.filter { it.row == 0 }.forEachIndexed { index, cell ->
            cell.value = when(index) {
                0 -> 0
                1 -> 0
                2 -> 1
                3 -> 2
                4 -> 4
                else -> 0
            }
        }
        gridRepository.persist(grid)

        // When
        val updatedGrid = gridService.handleCellClick(testGrid.id!!, 0, 0)

        // Then
        assertNotNull(updatedGrid)
        updatedGrid?.cells?.filter { it.row == 0 }?.forEach { cell ->
            assertEquals(0, cell.value)
        }
    }

    @Test
    @Transactional
    fun `should clear Fibonacci sequence in column`() {
        // Given
        val grid = gridRepository.findById(testGrid.id!!)!!
        grid.cells.filter { it.column == 0 }.forEachIndexed { index, cell ->
            cell.value = when(index) {
                0 -> 0
                1 -> 0
                2 -> 1
                3 -> 2
                4 -> 4
                else -> 0
            }
        }
        gridRepository.persistAndFlush(grid)

        // When
        val updatedGrid = gridService.handleCellClick(testGrid.id!!, 0, 0)

        // Then
        assertNotNull(updatedGrid)
        updatedGrid?.cells?.filter { it.column == 0 }?.forEach { cell ->
            assertEquals(0, cell.value)
        }
    }

    @Test
    @Transactional
    fun `should handle multiple cell clicks`() {
        // When
        gridService.handleCellClick(testGrid.id!!, 0, 0)
        val updatedGrid = gridService.handleCellClick(testGrid.id!!, 0, 1)

        // Then
        assertNotNull(updatedGrid)
        val cell = updatedGrid?.cells?.find { it.row == 0 && it.column == 0 }
        assertEquals(2, cell?.value)
    }

    @Test
    @Transactional
    fun `should return all persisted grids`() {
        // Given
        val grid1 = gridService.initializeGrid(3, 3)
        val grid2 = gridService.initializeGrid(4, 4)

        // When
        val result = gridService.getAllGrids()

        // Then
        assertEquals(3, result.size)
        assertTrue(result.any { it.id == grid1.id })
        assertTrue(result.any { it.id == grid2.id })
        assertTrue(result.any { it.rows == 3 && it.columns == 3 })
        assertTrue(result.any { it.rows == 4 && it.columns == 4 })
    }

    @Test
    @Transactional
    fun `should return empty list when no grids exist`() {
        // Given - ensure database is clean
        gridRepository.listAll().forEach { grid ->
            gridRepository.delete(grid)
        }

        // When
        val result = gridService.getAllGrids()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    @Transactional
    fun `should return grid by id when exists`() {
        // Given
        val grid = gridService.initializeGrid(3, 3)

        // When
        val result = gridService.getGrid(grid.id!!)

        // Then
        assertNotNull(result)
        assertEquals(grid.id, result?.id)
        assertEquals(3, result?.rows)
        assertEquals(3, result?.columns)
        assertEquals(9, result?.cells?.size)
    }

}
