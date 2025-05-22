package com.gridgame.model

class Grid {

    var rows = SIZE
    var columns = SIZE
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
