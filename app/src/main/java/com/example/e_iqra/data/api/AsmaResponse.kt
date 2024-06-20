package com.example.e_iqra.data.api

import com.google.gson.annotations.SerializedName

data class AsmaResponse(

	@field:SerializedName("data")
	val data: List<DataItem> = emptyList()
)

data class DataItem(

	@field:SerializedName("indo")
	val indo: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("latin")
	val latin: String? = null,

	@field:SerializedName("arab")
	val arab: String? = null
)
