package com.example.linussudoku.android.view

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.linussudoku.Greeting
import com.example.linussudoku.android.R
import com.example.linussudoku.android.databinding.ActivityMainBinding
import com.example.linussudoku.android.view.custom.SudokuBoardView
import com.example.linussudoku.android.view.game.Cell
import com.example.linussudoku.android.view.viewmodel.PlaySudokuViewModel

fun greet(): String {
    return Greeting().greeting()
}

class PlaySudokuActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {
    private lateinit var viewModel: PlaySudokuViewModel
    private lateinit var numberButtons: List<Button>

    private lateinit var binding: ActivityMainBinding

    //Set in onCreate()
    private var primaryColor = -1
    private var secondaryColor = -1

    override fun onCreate(savedInstanceState: Bundle?) {

        println("Hellos")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        primaryColor = ContextCompat.getColor(
            this, R.color.colorPrimary
        )
        secondaryColor = Color.LTGRAY

        binding = ActivityMainBinding.inflate(layoutInflater)


        binding.sudokuBoardView.registerListener(this)

        println("The current registered listener is: ${binding.sudokuBoardView.listener.toString()}")

        //What does the line below do?
        // This? https://developer.android.com/reference/androidx/appcompat/app/AppCompatActivity
        // Simply, this seems to enable newer features on older devices, "compat"="compatibility".
        // Since we use this method we have to extend "AppCompatActivity()", which we do above.
        setContentView(binding.root)

        println("This is my current state: ${this.lifecycle.currentState}")
        viewModel = ViewModelProvider(this)[PlaySudokuViewModel::class.java]
        viewModel.sudokuGame.selectedCellLiveData.observe(this,
            Observer { updateSelectedCellUI(it) })
        viewModel.sudokuGame.cellsLiveData.observe(this, { updateCells(it) })
        viewModel.sudokuGame.isTakingNotesLiveData.observe(this, Observer {
            updateNoteTakingUi(it)
        })
        viewModel.sudokuGame.highlightedKeysLiveData.observe(this, Observer {
            updateHighLightedKeys(it)
        })


        numberButtons = listOf(
            binding.oneButton,
            binding.twoButton,
            binding.threeButton,
            binding.fourButton,
            binding.fiveButton,
            binding.sixButton,
            binding.sevenButton,
            binding.eightButton,
            binding.nineButton
        )

        numberButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.sudokuGame.handleInput(index + 1)
            }
        }

        binding.notesButton.setOnClickListener {
            viewModel.sudokuGame.changeNoteTakingState()
        }

        binding.deleteButton.setOnClickListener {
            viewModel.sudokuGame.delete()
        }

        /*
         This method changes the background color ever so slightly compared to the default
         value as a result we call the method at the start so that we use the changed value
         as the default value.
         */
        updateHighLightedKeys(setOf())

        val deleteDrawable = DrawableCompat.wrap(binding.deleteButton.background)
        deleteDrawable.setTint(secondaryColor)
        binding.deleteButton.background = deleteDrawable
    }

    private fun updateCells(cells: List<Cell>) = cells?.let {
        binding.sudokuBoardView.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        binding.sudokuBoardView.updateSelectedCellUI(cell.first, cell.second)
    }

    private fun updateNoteTakingUi(isNoteTaking: Boolean?) = isNoteTaking?.let {
        val notesButton = binding.notesButton
        val drawable = DrawableCompat.wrap(notesButton.background)

        if (it) {
            drawable.setTint(primaryColor)
            notesButton.background = drawable
            notesButton.setColorFilter(primaryColor, PorterDuff.Mode.MULTIPLY)
        } else {
            drawable.setTint(secondaryColor)
            notesButton.background = drawable
            notesButton.setColorFilter(primaryColor, PorterDuff.Mode.MULTIPLY)
        }
    }

    private fun updateHighLightedKeys(set: Set<Int>?) = set?.let {
        numberButtons.forEachIndexed { index, button ->
            val drawable = DrawableCompat.wrap(button.background)

            val color = if (set.contains(index + 1)) primaryColor else secondaryColor
            drawable.setTint(color)
            button.background = drawable
        }
    }

    override fun onCellTouched(row: Int, column: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, column)
    }


}
