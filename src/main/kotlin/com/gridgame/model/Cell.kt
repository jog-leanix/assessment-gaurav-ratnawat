package com.gridgame.model

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Cell : PanacheEntity {

    @Column(nullable = false)
    var row: Int = 0

    @Column(name = "\"column\"", nullable = false)
    var column: Int = 0

    @Column(nullable = false)
    var value: Int = 0
        set(newValue) {
            if (newValue < 0) {
                throw IllegalArgumentException("Value must be non-negative")
            }
            field = newValue
        }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grid_id")
    @JsonIgnore
    lateinit var grid: Grid

    constructor()

    constructor(row: Int, column: Int, value: Int, grid: Grid) {
        this.row = row
        this.column = column
        this.value = value
        this.grid = grid
    }


    fun increment() {
        value++
    }

    fun clear() {
        value = 0
    }

    fun isInRowOrColumn(targetRow: Int, targetColumn: Int): Boolean {
        return row == targetRow || column == targetColumn
    }
}
