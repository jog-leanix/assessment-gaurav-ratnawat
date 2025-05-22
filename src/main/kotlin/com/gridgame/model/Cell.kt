package com.gridgame.model

class Cell {

    var row: Int = 0
    var column: Int = 0
    var value: Int = 0

    lateinit var grid: Grid

    constructor()

    constructor(row: Int, column: Int, value: Int, grid: Grid) {
        this.row = row
        this.column = column
        this.value = value
        this.grid = grid
    }
}
