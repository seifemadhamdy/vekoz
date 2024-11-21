package seifemadhamdy.vekoz.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MoviesByQueryResponseDto(
    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var results: ArrayList<ResultsDto> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null,
)
