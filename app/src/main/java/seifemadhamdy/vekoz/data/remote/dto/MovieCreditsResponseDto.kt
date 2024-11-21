package seifemadhamdy.vekoz.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MovieCreditsResponseDto(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("cast") var movieCastDto: ArrayList<MovieCastDto> = arrayListOf(),
    @SerializedName("crew") var movieCreditsCrewDto: ArrayList<MovieCreditsCrewDto> = arrayListOf(),
)
