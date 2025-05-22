package com.gridgame.model

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

    companion object {

        private const val SIZE = 50
    }
}
