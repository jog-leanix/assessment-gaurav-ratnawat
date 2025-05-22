package com.gridgame

class Grid {

    var rows = 50
    var columns = 50
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

}
