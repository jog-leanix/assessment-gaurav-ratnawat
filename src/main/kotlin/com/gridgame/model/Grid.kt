package com.gridgame.model

import com.gridgame.exception.InvalidCellClickException
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany

@Entity
class Grid : PanacheEntity() {

    @Column(nullable = false)
    var rows = SIZE

    @Column(nullable = false)
    var columns = SIZE

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], mappedBy = "grid", orphanRemoval = true)
    var cells: MutableList<Cell> = mutableListOf()

    fun initialize() {
        cells.clear()
        for (row in 0 until rows) {
            for (col in 0 until columns) {
                val cell = Cell(row = row, column = col, value = 0, grid = this)
                cells.add(cell)
            }
        }
    }

    fun incrementRowAndColumn(row: Int, column: Int) {
        cells.forEach { cell ->
            if (cell.isInRowOrColumn(row, column)) {
                cell.increment()
            }
        }
    }

    fun detectAndClearFibonacciSequences() {
        for (row in 0 until rows) {
            val cells = getCellsInRow(row)
            checkAndClearSequence(cells)
        }

        for (col in 0 until columns) {
            val cells = getCellsInColumn(col)
            checkAndClearSequence(cells)
        }
    }

    fun validateCellClick(row: Int, column: Int) {
        if (cells.isEmpty()) {
            throw InvalidCellClickException("Grid is empty: grid = ${this.id}")
        }

        if (!isValidPosition(row, column)) {
            throw InvalidCellClickException(
                "Invalid cell position: row=$row, column=$column for grid = ${this.id}"
            )
        }
    }

    private fun getCellsInRow(row: Int) = cells.filter { it.row == row }
        .sortedBy { it.column }

    private fun getCellsInColumn(col: Int) = cells.filter { it.column == col }
        .sortedBy { it.row }

    private fun checkAndClearSequence(cells: List<Cell>) {
        for (i in 0..cells.size - FIBONACCI_SEQUENCE_LENGTH) {
            val sequence = cells.subList(i, i + FIBONACCI_SEQUENCE_LENGTH)
            if (isFibonacciSequence(sequence.map { it.value })) {
                sequence.forEach { cell -> cell.clear() }
            }
        }
    }

    private fun isFibonacciSequence(sequence: List<Int>): Boolean {
        if (sequence.size < FIBONACCI_SEQUENCE_LENGTH) return false
        return sequence.windowed(3).all { (a, b, c) ->
            if (a < 0 || b < 0 || c < 0) return false
            c == a + b
        }
    }

    private fun isValidPosition(row: Int, column: Int): Boolean {
        return row in 0 until rows && column in 0 until columns
    }

    companion object {

        private const val SIZE = 50
        private const val FIBONACCI_SEQUENCE_LENGTH = 5
    }
}
