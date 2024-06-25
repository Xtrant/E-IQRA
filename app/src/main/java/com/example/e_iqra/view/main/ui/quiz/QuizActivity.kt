package com.example.e_iqra.view.main.ui.quiz

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.e_iqra.R
import com.example.e_iqra.data.quiz.QuizData
import com.example.e_iqra.databinding.ActivityQuizBinding
import com.example.e_iqra.view.ViewModelFactory
import com.example.e_iqra.view.customview.CanvasView

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var canvasView: CanvasView

    private val quizViewModel: QuizViewModel by viewModels {
        ViewModelFactory(this)
    }

    private var currentQuestionIndex = 0
    private var score = 0

    private var questionIndices: List<Int> = emptyList()

    private val questions = QuizData.questions
    private val images = QuizData.images
    private val options = QuizData.options
    private val correctAnswers = QuizData.correctAnswers
    private val userAnswers = QuizData.userAnswers

    private var selectedOptionView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        canvasView = binding.canvasView

        setupViews()
        generateRandomQuestionIndices()
        loadQuestion()
    }

    private fun generateRandomQuestionIndices() {
        questionIndices = (0 until QuizData.questions.size).shuffled().take(5)
    }

    private fun setupViews() {
        binding.clearButton.setOnClickListener {
            canvasView.clearCanvas()
        }

        binding.nextButton.setOnClickListener {
            if (currentQuestionIndex < questionIndices.size - 1) {
                currentQuestionIndex++
                loadQuestion()
            } else {
                showResults()
            }
        }

        canvasView = binding.canvasView

        binding.clearButton.setOnClickListener {
            if (::canvasView.isInitialized) {
                canvasView.clearCanvas()
            }
        }

        binding.nextButtonCanvas.setOnClickListener {
            startCheckDrawingAnswer()
        }

        binding.option1.setOnClickListener { onOptionClicked(binding.option1, 0) }
        binding.option2.setOnClickListener { onOptionClicked(binding.option2, 1) }
        binding.option3.setOnClickListener { onOptionClicked(binding.option3, 2) }
        binding.option4.setOnClickListener { onOptionClicked(binding.option4, 3) }
    }

    private fun loadQuestion() {
        val questionIndex = questionIndices[currentQuestionIndex]

        val isCanvasQuestion = correctAnswers[questionIndex] == -1

        binding.multipleChoiceLayout.visibility = if (isCanvasQuestion) View.GONE else View.VISIBLE
        binding.canvasLayout.visibility = if (isCanvasQuestion) View.VISIBLE else View.GONE

        binding.nextButton.visibility = if (userAnswers[questionIndex] != null) View.VISIBLE else View.GONE

        if (!isCanvasQuestion) {
            binding.questionText.text = questions[questionIndex]
            if (images[questionIndex] != 0) {
                binding.questionImage.visibility = View.VISIBLE
                binding.questionImage.setImageResource(images[questionIndex])
            } else {
                binding.questionImage.visibility = View.GONE
            }

            binding.option1.text = options[questionIndex][0]
            binding.option2.text = options[questionIndex][1]
            binding.option3.text = options[questionIndex][2]
            binding.option4.text = options[questionIndex][3]

            resetOptionColors()
            enableOptions()

            userAnswers[questionIndex]?.let {
                highlightSelectedOption(it)
            }
        } else {
            binding.canvasQuestionText.text = questions[questionIndex]
            canvasView.clearCanvas()
        }
    }

    private fun highlightSelectedOption(selectedOption: Int) {
        when (selectedOption) {
            0 -> binding.option1.setBackgroundColor(Color.BLUE)
            1 -> binding.option2.setBackgroundColor(Color.BLUE)
            2 -> binding.option3.setBackgroundColor(Color.BLUE)
            3 -> binding.option4.setBackgroundColor(Color.BLUE)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun onOptionClicked(view: View, selectedOption: Int) {
        selectedOptionView?.setBackgroundColor(R.color.primary_color)
        selectedOptionView = view
        view.setBackgroundColor(Color.BLUE)
        userAnswers[questionIndices[currentQuestionIndex]] = selectedOption
        if (correctAnswers[questionIndices[currentQuestionIndex]] == selectedOption) {
            score++
        }

        binding.nextButton.visibility = View.VISIBLE
    }

    @SuppressLint("ResourceAsColor")
    private fun resetOptionColors() {
        binding.option1.setBackgroundColor(R.color.primary_color)
        binding.option2.setBackgroundColor(R.color.primary_color)
        binding.option3.setBackgroundColor(R.color.primary_color)
        binding.option4.setBackgroundColor(R.color.primary_color)
    }

    private fun enableOptions() {
        binding.option1.isEnabled = true
        binding.option2.isEnabled = true
        binding.option3.isEnabled = true
        binding.option4.isEnabled = true
    }

    private fun showResults() {
        val intent = Intent(this, QuizResultActivity::class.java).apply {
            putExtra("SCORE", score)
            putExtra("TOTAL_QUESTIONS", questionIndices.size)
            putExtra("USER_ANSWERS", userAnswers)
            putExtra("CORRECT_ANSWERS", correctAnswers)
            putExtra("QUESTIONS", questions)
            putExtra("OPTIONS", options)
        }
        startActivity(intent)
        finish()
    }

    private fun startCheckDrawingAnswer() {
        val bitmap = quizViewModel.getBitmapFromView(binding.canvasView)
        quizViewModel.checkDrawingWithMLModel(bitmap) { isDrawingCorrect ->
            if (isDrawingCorrect) {
                score++
            }
        }

        if (currentQuestionIndex < questionIndices.size - 1) {
            currentQuestionIndex++
            loadQuestion()
        } else {
            showResults()
        }
    }
}
