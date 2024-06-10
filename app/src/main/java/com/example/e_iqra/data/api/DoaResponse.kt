package com.example.e_iqra.data.api

import com.google.gson.annotations.SerializedName

data class DoaResponse(

	@field:SerializedName("DoaResponse")
	val doaResponse: List<DoaResponseItem?>? = null
)

data class DoaResponseItem(

	@field:SerializedName("ayat")
	val ayat: String? = null,

	@field:SerializedName("doa")
	val doa: String? = null,

	@field:SerializedName("artinya")
	val artinya: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("latin")
	val latin: String? = null
)
