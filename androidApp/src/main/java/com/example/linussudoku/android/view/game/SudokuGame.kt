package com.example.linussudoku.android.view.game

import androidx.lifecycle.MutableLiveData
import com.example.linussudoku.android.view.game.algorithms.BoardSolver

class SudokuGame {

    private val boardSolver = BoardSolver()

    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<List<List<Cell>>>()
    val isTakingNotesLiveData = MutableLiveData<Boolean>()
    val highlightedKeysLiveData = MutableLiveData<Set<Int>>()

    private var selectedRow = -1
    private var selectedColumn = -1
    private var isTakingNotes = false

    val board: Board

    private val boardSize = 9
    private val squareRootSize = 3

    init {
        val cells = getHardStartingInstance()

        board = Board(9, cells)

        selectedCellLiveData.postValue(Pair(selectedRow, selectedColumn))
        cellsLiveData.postValue(board.cells)
        isTakingNotesLiveData.postValue(isTakingNotes)
    }

    fun handleInput(number: Int) {
        if (selectedRow == -1 || selectedColumn == -1) {
            return
        }
        val cell = board.getCell(selectedRow, selectedColumn)

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

    fun delete() {
        if (selectedRow == -1 || selectedColumn == -1) {
            return
        }
        val cell = board.getCell(selectedRow, selectedColumn)
        if (isTakingNotes) {
            cell.notes.clear()
            highlightedKeysLiveData.postValue(setOf())
        } else {
            cell.value = 0
        }
        cellsLiveData.postValue(board.cells)
    }

    fun solveBoard() {
        println("Starting brute force solve...")
        boardSolver.solveBoardBruteForce(this)
        println("Finished brute force solve...")
    }

    fun newRandomBoard() {

    }

    private fun getValidStartingInstance(): List<List<Cell>> {
        val cells: MutableList<MutableList<Cell>> = mutableListOf()

        val row1: MutableList<Cell> = mutableListOf()
        val row2: MutableList<Cell> = mutableListOf()
        val row3: MutableList<Cell> = mutableListOf()
        val row4: MutableList<Cell> = mutableListOf()
        val row5: MutableList<Cell> = mutableListOf()
        val row6: MutableList<Cell> = mutableListOf()
        val row7: MutableList<Cell> = mutableListOf()
        val row8: MutableList<Cell> = mutableListOf()
        val row9: MutableList<Cell> = mutableListOf()

        row1.add(Cell(0, 0, 5, true))
        row1.add(Cell(0, 1, 3, true))
        row1.add(Cell(0, 2, 0, false))
        row1.add(Cell(0, 3, 0, false))
        row1.add(Cell(0, 4, 7, true))
        row1.add(Cell(0, 5, 0, false))
        row1.add(Cell(0, 6, 0, false))
        row1.add(Cell(0, 7, 0, false))
        row1.add(Cell(0, 8, 0, false))

        row2.add(Cell(1, 0, 6, true))
        row2.add(Cell(1, 1, 0, false))
        row2.add(Cell(1, 2, 0, false))
        row2.add(Cell(1, 3, 1, true))
        row2.add(Cell(1, 4, 9, true))
        row2.add(Cell(1, 5, 5, true))
        row2.add(Cell(1, 6, 0, false))
        row2.add(Cell(1, 7, 0, false))
        row2.add(Cell(1, 8, 0, false))

        row3.add(Cell(2, 0, 0, false))
        row3.add(Cell(2, 1, 9, true))
        row3.add(Cell(2, 2, 8, true))
        row3.add(Cell(2, 3, 0, false))
        row3.add(Cell(2, 4, 0, false))
        row3.add(Cell(2, 5, 0, false))
        row3.add(Cell(2, 6, 0, false))
        row3.add(Cell(2, 7, 6, true))
        row3.add(Cell(2, 8, 0, false))

        row4.add(Cell(3, 0, 8, true))
        row4.add(Cell(3, 1, 0, false))
        row4.add(Cell(3, 2, 0, false))
        row4.add(Cell(3, 3, 0, false))
        row4.add(Cell(3, 4, 6, true))
        row4.add(Cell(3, 5, 0, false))
        row4.add(Cell(3, 6, 0, false))
        row4.add(Cell(3, 7, 0, false))
        row4.add(Cell(3, 8, 3, true))

        row5.add(Cell(4, 0, 4, true))
        row5.add(Cell(4, 1, 0, false))
        row5.add(Cell(4, 2, 0, false))
        row5.add(Cell(4, 3, 8, true))
        row5.add(Cell(4, 4, 0, false))
        row5.add(Cell(4, 5, 3, true))
        row5.add(Cell(4, 6, 0, false))
        row5.add(Cell(4, 7, 0, false))
        row5.add(Cell(4, 8, 1, true))

        row6.add(Cell(5, 0, 7, true))
        row6.add(Cell(5, 1, 0, false))
        row6.add(Cell(5, 2, 0, false))
        row6.add(Cell(5, 3, 0, false))
        row6.add(Cell(5, 4, 2, true))
        row6.add(Cell(5, 5, 0, false))
        row6.add(Cell(5, 6, 0, false))
        row6.add(Cell(5, 7, 0, false))
        row6.add(Cell(5, 8, 6, true))

        row7.add(Cell(6, 0, 0, false))
        row7.add(Cell(6, 1, 6, true))
        row7.add(Cell(6, 2, 0, false))
        row7.add(Cell(6, 3, 0, false))
        row7.add(Cell(6, 4, 0, false))
        row7.add(Cell(6, 5, 0, false))
        row7.add(Cell(6, 6, 2, true))
        row7.add(Cell(6, 7, 8, true))
        row7.add(Cell(6, 8, 0, false))

        row8.add(Cell(7, 0, 0, false))
        row8.add(Cell(7, 1, 0, false))
        row8.add(Cell(7, 2, 0, false))
        row8.add(Cell(7, 3, 4, true))
        row8.add(Cell(7, 4, 1, true))
        row8.add(Cell(7, 5, 9, true))
        row8.add(Cell(7, 6, 0, false))
        row8.add(Cell(7, 7, 0, false))
        row8.add(Cell(7, 8, 5, true))

        row9.add(Cell(8, 0, 0, false))
        row9.add(Cell(8, 1, 0, false))
        row9.add(Cell(8, 2, 0, false))
        row9.add(Cell(8, 3, 0, false))
        row9.add(Cell(8, 4, 8, true))
        row9.add(Cell(8, 5, 0, false))
        row9.add(Cell(8, 6, 0, false))
        row9.add(Cell(8, 7, 7, true))
        row9.add(Cell(8, 8, 9, true))

        cells.add(row1)
        cells.add(row2)
        cells.add(row3)
        cells.add(row4)
        cells.add(row5)
        cells.add(row6)
        cells.add(row7)
        cells.add(row8)
        cells.add(row9)

        return cells
    }

    private fun getHardStartingInstance(): List<List<Cell>> {
        val cells: MutableList<MutableList<Cell>> = mutableListOf()

        val row1: MutableList<Cell> = mutableListOf()
        val row2: MutableList<Cell> = mutableListOf()
        val row3: MutableList<Cell> = mutableListOf()
        val row4: MutableList<Cell> = mutableListOf()
        val row5: MutableList<Cell> = mutableListOf()
        val row6: MutableList<Cell> = mutableListOf()
        val row7: MutableList<Cell> = mutableListOf()
        val row8: MutableList<Cell> = mutableListOf()
        val row9: MutableList<Cell> = mutableListOf()

        row1.add(Cell(0, 0, 5, true))
        row1.add(Cell(0, 1, 0, false))
        row1.add(Cell(0, 2, 0, false))
        row1.add(Cell(0, 3, 0, false))
        row1.add(Cell(0, 4, 0, false))
        row1.add(Cell(0, 5, 0, false))
        row1.add(Cell(0, 6, 0, false))
        row1.add(Cell(0, 7, 3, true))
        row1.add(Cell(0, 8, 0, false))

        row2.add(Cell(1, 0, 0, false))
        row2.add(Cell(1, 1, 3, true))
        row2.add(Cell(1, 2, 0, false))
        row2.add(Cell(1, 3, 4, true))
        row2.add(Cell(1, 4, 0, false))
        row2.add(Cell(1, 5, 2, true))
        row2.add(Cell(1, 6, 1, true))
        row2.add(Cell(1, 7, 0, false))
        row2.add(Cell(1, 8, 0, false))

        row3.add(Cell(2, 0, 0, false))
        row3.add(Cell(2, 1, 0, false))
        row3.add(Cell(2, 2, 0, false))
        row3.add(Cell(2, 3, 7, true))
        row3.add(Cell(2, 4, 0, false))
        row3.add(Cell(2, 5, 0, false))
        row3.add(Cell(2, 6, 0, false))
        row3.add(Cell(2, 7, 0, false))
        row3.add(Cell(2, 8, 0, false))

        row4.add(Cell(3, 0, 0, false))
        row4.add(Cell(3, 1, 2, true))
        row4.add(Cell(3, 2, 0, false))
        row4.add(Cell(3, 3, 0, false))
        row4.add(Cell(3, 4, 7, true))
        row4.add(Cell(3, 5, 0, false))
        row4.add(Cell(3, 6, 0, false))
        row4.add(Cell(3, 7, 0, false))
        row4.add(Cell(3, 8, 0, false))

        row5.add(Cell(4, 0, 0, false))
        row5.add(Cell(4, 1, 0, false))
        row5.add(Cell(4, 2, 0, false))
        row5.add(Cell(4, 3, 0, false))
        row5.add(Cell(4, 4, 5, true))
        row5.add(Cell(4, 5, 0, false))
        row5.add(Cell(4, 6, 6, true))
        row5.add(Cell(4, 7, 0, false))
        row5.add(Cell(4, 8, 0, false))

        row6.add(Cell(5, 0, 8, true))
        row6.add(Cell(5, 1, 0, false))
        row6.add(Cell(5, 2, 0, false))
        row6.add(Cell(5, 3, 2, true))
        row6.add(Cell(5, 4, 0, false))
        row6.add(Cell(5, 5, 6, true))
        row6.add(Cell(5, 6, 0, false))
        row6.add(Cell(5, 7, 0, false))
        row6.add(Cell(5, 8, 4, true))

        row7.add(Cell(6, 0, 0, false))
        row7.add(Cell(6, 1, 0, false))
        row7.add(Cell(6, 2, 9, true))
        row7.add(Cell(6, 3, 0, false))
        row7.add(Cell(6, 4, 0, false))
        row7.add(Cell(6, 5, 0, false))
        row7.add(Cell(6, 6, 0, false))
        row7.add(Cell(6, 7, 0, false))
        row7.add(Cell(6, 8, 8, true))

        row8.add(Cell(7, 0, 0, false))
        row8.add(Cell(7, 1, 5, true))
        row8.add(Cell(7, 2, 0, false))
        row8.add(Cell(7, 3, 3, true))
        row8.add(Cell(7, 4, 0, false))
        row8.add(Cell(7, 5, 1, true))
        row8.add(Cell(7, 6, 4, true))
        row8.add(Cell(7, 7, 0, false))
        row8.add(Cell(7, 8, 0, false))

        row9.add(Cell(8, 0, 0, false))
        row9.add(Cell(8, 1, 0, false))
        row9.add(Cell(8, 2, 0, false))
        row9.add(Cell(8, 3, 0, false))
        row9.add(Cell(8, 4, 6, true))
        row9.add(Cell(8, 5, 0, false))
        row9.add(Cell(8, 6, 0, false))
        row9.add(Cell(8, 7, 0, false))
        row9.add(Cell(8, 8, 0, false))

        cells.add(row1)
        cells.add(row2)
        cells.add(row3)
        cells.add(row4)
        cells.add(row5)
        cells.add(row6)
        cells.add(row7)
        cells.add(row8)
        cells.add(row9)

        return cells
    }

}