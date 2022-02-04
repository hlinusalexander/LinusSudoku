package com.example.linussudoku.android.view.game.algorithms

import com.example.linussudoku.android.view.game.Cell
import com.example.linussudoku.android.view.game.SudokuGame

class BoardSolver {

    fun solveBoardBruteForce(sudokuGame: SudokuGame): Boolean {

        val cells = sudokuGame.board.cells
        var rowWeTested = -1
        var columnWeTested = -1
        rowLoop@ for (rowToTest in 0..8) {
            for (columnToTest in 0..8) {
                val cellToTest = cells[rowToTest][columnToTest]

                if (cellToTest.value == 0) {
                    val columnCells = getCellsInSameColumn(cells, columnToTest)
                    val currentRowValues = getValuesInCells(cells[rowToTest])
                    val currentColumnValues = getValuesInCells(columnCells)
                    val cellsInMiniSquare =
                        getCellsInTheSameMiniSquare(cells, rowToTest, columnToTest)
                    val currentMiniSquareValues = getValuesInCells(cellsInMiniSquare)

                    for (valueToTest in 1..9) {
                        if (!currentRowValues.contains(valueToTest)) {
                            if (!currentColumnValues.contains(valueToTest)) {
                                if (!currentMiniSquareValues.contains(valueToTest)) {
                                    //We will actually test adding the value.
                                    println("Will test at: row: $rowToTest, column: $columnToTest, value: $valueToTest")
                                    cellToTest.value = valueToTest
                                    sudokuGame.cellsLiveData.postValue(cells)



                                    rowWeTested = rowToTest
                                    columnWeTested = columnToTest
                                    if (sudokuGame.board.areAllCellsFilled()) {
                                        println("Found a solution!")
                                        return true
                                    } else {
                                        if (solveBoardBruteForce(sudokuGame)) {
                                            return true
                                        }
                                    }
                                } else {
                                    //If the mini square already contained the number then it's not a
                                    // legal move to add the same number again to the mini square.
                                }
                            } else {
                                //If the column already contained the number then it's not a legal move
                                //to add the same number again to the column.
                            }
                        } else {
                            //If the row already contained the number then it's not a legal move
                            //to add the same number again to the row.
                        }
                    }
                    //At this point we've gone through all possible values and none will fit.
                    //If this is the case we break and try a different route in our depth-first-search
                    println("Unsolvable path identified.")
                    println("rowToTest:$rowToTest, columnToTest:$columnToTest")
//                    rowWeTested = rowToTest
//                    columnWeTested = columnToTest
                    break@rowLoop
                } else {
                    //The cell already has a value, so we do not test anything here.
                }
            }
        }
        if (rowWeTested != -1 && columnWeTested != -1) {
            println("Deleting this cell. row: $rowWeTested, column: $columnWeTested")
            cells[rowWeTested][columnWeTested].value = 0
        }
        return false
    }

    private fun getCellsInTheSameMiniSquare(
        cells: List<List<Cell>>, rowIndex: Int, columnIndex: Int
    ): List<Cell> {
        val cellsInMiniSquare = mutableListOf<Cell>()

        if (rowIndex in 0..2) {
            if (columnIndex in 0..2) {
                for (i in 0..2) {
                    for (j in 0..2) {
                        cellsInMiniSquare.add(cells[i][j])
                    }
                }
            } else if (columnIndex in 3..5) {
                for (i in 0..2) {
                    for (j in 3..5) {
                        cellsInMiniSquare.add(cells[i][j])
                    }
                }
            } else if (columnIndex in 6..8) {
                for (i in 0..2) {
                    for (j in 6..8) {
                        cellsInMiniSquare.add(cells[i][j])
                    }
                }
            } else {
                Exception(
                    "Error, trying to get a Sudoku cell that doesn't exist. rowIndex: $rowIndex , columnIndex: $columnIndex"
                )
            }
        } else if (rowIndex in 3..5) {
            if (columnIndex in 0..2) {
                for (i in 3..5) {
                    for (j in 0..2) {
                        cellsInMiniSquare.add(cells[i][j])
                    }
                }
            } else if (columnIndex in 3..5) {
                for (i in 3..5) {
                    for (j in 3..5) {
                        cellsInMiniSquare.add(cells[i][j])
                    }
                }
            } else if (columnIndex in 6..8) {
                for (i in 3..5) {
                    for (j in 6..8) {
                        cellsInMiniSquare.add(cells[i][j])
                    }
                }
            } else {
                Exception(
                    "Error, trying to get a Sudoku cell that doesn't exist. rowIndex: $rowIndex , columnIndex: $columnIndex"
                )
            }
        } else if (rowIndex in 6..8) {
            if (columnIndex in 0..2) {
                for (i in 6..8) {
                    for (j in 0..2) {
                        cellsInMiniSquare.add(cells[i][j])
                    }
                }
            } else if (columnIndex in 3..5) {
                for (i in 6..8) {
                    for (j in 3..5) {
                        cellsInMiniSquare.add(cells[i][j])
                    }
                }
            } else if (columnIndex in 6..8) {
                for (i in 6..8) {
                    for (j in 6..8) {
                        cellsInMiniSquare.add(cells[i][j])
                    }
                }
            } else {
                Exception(
                    "Error, trying to get a Sudoku cell that doesn't exist. rowIndex: $rowIndex , columnIndex: $columnIndex"
                )
            }
        } else {
            Exception(
                "Error, trying to get a Sudoku cell that doesn't exist. rowIndex: $rowIndex , columnIndex: $columnIndex"
            )
        }

        return cellsInMiniSquare
    }

    private fun getCellsInSameColumn(cells: List<List<Cell>>, columnIndex: Int): List<Cell> {
        val columnCells = mutableListOf<Cell>()
        cells.forEach {
            columnCells.add(it[columnIndex])
        }
        return columnCells
    }

    private fun getValuesInCells(cells: List<Cell>): Set<Int> {
        val values = mutableSetOf<Int>()
        cells.forEach {
            values.add(it.value)
        }
        return values
    }

    private fun getCellsOnSameRowAsIndex(cellIndex: Int) {
        val row = cellIndex / 9
    }

}