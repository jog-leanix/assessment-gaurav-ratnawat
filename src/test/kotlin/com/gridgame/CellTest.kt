package com.gridgame

import com.gridgame.model.Cell
import com.gridgame.model.Grid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

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

    @ParameterizedTest
    @CsvSource(
        "0,0,0",
        "1,2,3",
        "100,200,300",
        "${Int.MAX_VALUE},${Int.MAX_VALUE},${Int.MAX_VALUE}"
    )
    fun `should initialize cell with valid parameters`(row: Int, column: Int, value: Int) {
        val testCell = Cell(row, column, value, grid)
        assertEquals(row, testCell.row)
        assertEquals(column, testCell.column)
        assertEquals(value, testCell.value)
        assertSame(grid, testCell.grid)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 42, 100, Int.MAX_VALUE])
    fun `should allow setting valid values`(validValue: Int) {
        cell.value = validValue
        assertEquals(validValue, cell.value)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, -42, Int.MIN_VALUE])
    fun `should throw exception for negative values`(invalidValue: Int) {
        val exception = assertThrows<IllegalArgumentException> {
            cell.value = invalidValue
        }
        assertEquals("Value must be non-negative", exception.message)
    }
}
