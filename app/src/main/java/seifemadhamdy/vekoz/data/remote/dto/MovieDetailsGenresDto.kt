package seifemadhamdy.vekoz.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MovieDetailsGenresDto(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
)
