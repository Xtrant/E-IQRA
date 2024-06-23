package com.example.e_iqra.view.main.ui.quiz

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.e_iqra.R
import com.example.e_iqra.databinding.ActivityQuizResultBinding
import com.example.e_iqra.view.ViewModelFactory

class QuizResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizResultBinding
    private val quizViewModel: QuizViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizResultBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val score = intent.getIntExtra("SCORE", 0)
        val totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0)
        val userAnswers = intent.getIntArrayExtra("USER_ANSWERS")
        val correctAnswers = intent.getIntArrayExtra("CORRECT_ANSWERS")
        val questions = intent.getStringArrayExtra("QUESTIONS")
        val options = intent.getSerializableExtra("OPTIONS") as Array<Array<String>>

        val percentageScore = (score.toDouble() / totalQuestions.toDouble()) * 100
        binding.scoreText.text = "Nilai: ${percentageScore.toInt()}"
        binding.correctText.text = "Jawaban benar: $score"
        binding.wrongText.text = "Jawaban salah: ${totalQuestions - score}"

        if (questions != null && options != null && userAnswers != null && correctAnswers != null) {
            displayQuestionSummary(quizViewModel.getQuestionSummary(totalQuestions, questions, options, userAnswers, correctAnswers))
        }

        binding.backButton.setOnClickListener {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.navigation_learn)
        }
    }

    private fun displayQuestionSummary(summaryList: List<String>) {
        for (summary in summaryList) {
            val questionSummary = TextView(this)
            questionSummary.text = summary
            questionSummary.setPadding(0, 16, 0, 0)
            binding.questionSummary.addView(questionSummary)
        }
    }
}
