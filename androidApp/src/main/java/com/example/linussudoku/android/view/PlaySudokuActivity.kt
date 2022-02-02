package com.example.linussudoku.android.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.linussudoku.Greeting
import com.example.linussudoku.android.R
import com.example.linussudoku.android.databinding.ActivityMainBinding
import com.example.linussudoku.android.view.custom.SudokuBoardView
import com.example.linussudoku.android.view.viewmodel.PlaySudokuViewModel

fun greet(): String {
    return Greeting().greeting()
}

class PlaySudokuActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {
    private lateinit var viewModel: PlaySudokuViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        println("Hellos")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)


        binding.sudokuBoardView.registerListener(this)

        println("The current registered listener is: ${binding.sudokuBoardView.listener.toString()}")

        //What does the line below do?
        setContentView(binding.root)

        println("This is my current state: ${this.lifecycle.currentState}")
        viewModel = ViewModelProvider(this)[PlaySudokuViewModel::class.java]
        viewModel.sudokuGame.selectedCellLiveData.observe(
            this,
            Observer { updateSelectedCellUI(it) })

    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        binding.sudokuBoardView.updateSelectedCellUI(cell.first, cell.second)
    }

    override fun onCellTouched(row: Int, column: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, column)
    }


}
