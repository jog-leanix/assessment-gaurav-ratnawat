package com.gridgame.service

import com.gridgame.model.GridRepository
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@QuarkusTest
@QuarkusTestResource(PostgresTestResource::class)
class GridServiceIntegrationTest {

    @Inject
    lateinit var gridService: GridService

    @Inject
    lateinit var gridRepository: GridRepository

    @Test
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
}
