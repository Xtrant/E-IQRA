package com.example.e_iqra.data.quiz

import com.example.e_iqra.R

object QuizData {
    val questions = arrayOf(
        "Gambar dibawah ini merupakan huruf...",
        "Gambar dibawah ini merupakan huruf...",
        "Gambarkan huruf alif",
        "Gambar dibawah ini merupakan huruf...",
        "Gambar huruf sin",
        "Gambar huruf ya",
        "Gambar dibawah ini merupakan huruf...",
        "Gambar huruf wau",
        "Gambar dibawah ini merupakan huruf...",
        "Gambar dibawah ini merupakan huruf...",
        "Gambar huruf dal",
        "Gambar dibawah ini merupakan huruf...",
        "Gambar dibawah ini merupakan huruf...",
        "Gambar dibawah ini merupakan huruf...",
        "Gambar dibawah ini merupakan huruf..."
    )

    val images = arrayOf(
        R.drawable.nun,
        R.drawable.wau,
        0,
        R.drawable.jim,
        0,
        0,
        R.drawable.alif,
        0,
        R.drawable.lam_alif,
        R.drawable.dal,
        0,
        R.drawable.dho,
        R.drawable.ghoin,
        R.drawable.sin,
        R.drawable.mim
    )

    val options = arrayOf(
        arrayOf("Ba", "Dzal", "Nun", "Lam Alif"),
        arrayOf("Wau", "Tsa", "Syin", "Ain"),
        emptyArray(),
        arrayOf("Jim", "Ra'", "Dhod", "Qaf"),
        emptyArray(),
        emptyArray(),
        arrayOf("Wau", "Dzal", "Tsa", "Alif"),
        emptyArray(),
        arrayOf("Syin", "Shod", "Lam Alif", "Ba"),
        arrayOf("Dal", "Ta", "Dzal", "Hamzah"),
        emptyArray(),
        arrayOf("Alif", "Dho", "Wau", "Nun"),
        arrayOf("Ain", "Kaf", "Ghoin", "Lam"),
        arrayOf("Sin", "Mim", "Syin", "Alif"),
        arrayOf("Ba", "Lam Alif", "Wau", "Mim")
    )

    val correctAnswers = arrayOf(2, 0, -1, 0, -1, -1, 3, -1, 2, 0, -1, 1, 2, 0, 3)
    val userAnswers = arrayOfNulls<Int>(questions.size)
}
