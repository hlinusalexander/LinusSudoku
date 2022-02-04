package com.example.linussudoku.android.view.game

import androidx.lifecycle.MutableLiveData

class SudokuGame {

    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<List<Cell>>()
    val isTakingNotesLiveData = MutableLiveData<Boolean>()
    val highlightedKeysLiveData = MutableLiveData<Set<Int>>()

    private var selectedRow = -1
    private var selectedColumn = -1
    private var isTakingNotes = false

    private val board: Board


    init {
        val cells = List(9 * 9) { i -> Cell(i / 9, i % 9, i % 9) }

        cells[0].notes = mutableSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        cells[11].isStartingCell = true
        cells[21].isStartingCell = true
        

        board = Board(9, cells)

        selectedCellLiveData.postValue(Pair(selectedRow, selectedColumn))
        cellsLiveData.postValue(board.cells)
        isTakingNotesLiveData.postValue(isTakingNotes)
    }

    fun handleInput(number: Int) {
        val cell = board.getCell(selectedRow, selectedColumn)
        if (selectedRow == -1 || selectedColumn == -1) {
            return
        }
        if (isTakingNotes) {
            if (cell.notes.contains(number)) {
                cell.notes.remove(number)
            } else {
                cell.notes.add(number)
            }
            highlightedKeysLiveData.postValue(cell.notes)
        } else {
            cell.value = number
        }
        cellsLiveData.postValue(board.cells)
    }

    fun updateSelectedCell(row: Int, column: Int) {
        val cell = board.getCell(row, column)
        if (!cell.isStartingCell) {
            selectedRow = row
            selectedColumn = column
            selectedCellLiveData.postValue(Pair(selectedRow, selectedColumn))

            if (isTakingNotes) {
                highlightedKeysLiveData.postValue(cell.notes)
            }
        } else {
            //Do nothing.
        }
    }

    fun changeNoteTakingState() {
        isTakingNotes = !isTakingNotes

        isTakingNotesLiveData.postValue(isTakingNotes)

        val currentNotes = if (isTakingNotes) {
            board.getCell(selectedRow, selectedColumn).notes
        } else {
            setOf<Int>()
        }

        highlightedKeysLiveData.postValue(currentNotes)
    }

}