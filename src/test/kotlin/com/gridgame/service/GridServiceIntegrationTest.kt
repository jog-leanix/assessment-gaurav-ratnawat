package com.gridgame.service

import com.gridgame.model.Grid
import com.gridgame.model.GridRepository
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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
        gridRepository.deleteAll()
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
}
