package com.example.e_iqra.view.main.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.e_iqra.R
import com.example.e_iqra.databinding.ActivityQuizResultBinding
import com.example.e_iqra.view.main.MainActivity

class QuizResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizResultBinding
    private lateinit var textResultTitle: TextView
    private lateinit var textCorrectAnswers: TextView
    private lateinit var textWrongAnswers: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizResultBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupAction()
    }

    private fun setupAction() {
        textResultTitle = findViewById(R.id.text_result_title)
        textCorrectAnswers = findViewById(R.id.text_correct_answers)
        textWrongAnswers = findViewById(R.id.text_wrong_answers)

        val totalCorrectAnswers = intent.getIntExtra("TOTAL_CORRECT_ANSWERS", 0)
        val totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0)
        val totalWrongAnswers = totalQuestions - totalCorrectAnswers

        textCorrectAnswers.text = "Correct Answers: $totalCorrectAnswers"
        textWrongAnswers.text = "Wrong Answers: $totalWrongAnswers"

        binding.buttonBackToMenu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
