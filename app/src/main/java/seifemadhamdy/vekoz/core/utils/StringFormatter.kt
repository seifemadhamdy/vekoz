package seifemadhamdy.vekoz.core.utils

import android.os.Build
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object StringFormatter {
    fun formatReleaseDate(releaseDate: String?): String? =
        when {
            releaseDate.isNullOrBlank() -> null
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                LocalDate.parse(releaseDate)
                    .format(DateTimeFormatter.ofPattern("d MMM, yyyy", Locale.ENGLISH))
            }
            else -> releaseDate
        }

    fun formatVoteAverage(voteAverage: Double?): String? =
        if (voteAverage == null || voteAverage == 0.0) null
        else "%.1f".format((voteAverage / 10) * 5)

    fun formatAsCurrency(value: Int?): String? =
        value?.let {
            NumberFormat.getCurrencyInstance(Locale.US)
                .apply { maximumFractionDigits = 0 }
                .format(it)
        }
}
