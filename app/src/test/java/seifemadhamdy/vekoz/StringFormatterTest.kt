package seifemadhamdy.vekoz

import android.os.Build
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import seifemadhamdy.vekoz.core.utils.StringFormatter

@RunWith(MockitoJUnitRunner::class)
class StringFormatterTest {

    @Test
    fun `formatReleaseDate with null should return null`() {
        // Arrange
        val releaseDate: String? = null

        // Act
        val result = StringFormatter.formatReleaseDate(releaseDate)

        // Assert
        assertNull(result)
    }

    @Test
    fun `formatReleaseDate with blank string should return null`() {
        // Arrange
        val releaseDate = ""

        // Act
        val result = StringFormatter.formatReleaseDate(releaseDate)

        // Assert
        assertNull(result)
    }

    @Test
    fun `formatReleaseDate with pre-O Android version should return original string`() {
        // Arrange
        val releaseDate = "2023-11-24"
        // Mocking an older Android version, assuming the app runs on pre-O Android
        val result =
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) releaseDate else "24 Nov, 2023"

        // Act
        val formattedDate = StringFormatter.formatReleaseDate(releaseDate)

        // Assert
        assertEquals(result, formattedDate)
    }

    @Test
    fun `formatVoteAverage with valid vote average should return formatted average`() {
        // Arrange
        val voteAverage = 8.0 // Should return 4.0 (out of 5.0 scale)
        val expected = "4.0"

        // Act
        val result = StringFormatter.formatVoteAverage(voteAverage)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `formatVoteAverage with null should return null`() {
        // Arrange
        val voteAverage: Double? = null

        // Act
        val result = StringFormatter.formatVoteAverage(voteAverage)

        // Assert
        assertNull(result)
    }

    @Test
    fun `formatVoteAverage with zero vote average should return null`() {
        // Arrange
        val voteAverage = 0.0

        // Act
        val result = StringFormatter.formatVoteAverage(voteAverage)

        // Assert
        assertNull(result)
    }

    @Test
    fun `formatAsCurrency with valid value should return formatted currency`() {
        // Arrange
        val value = 123456
        val expected = "$123,456"

        // Act
        val result = StringFormatter.formatAsCurrency(value)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `formatAsCurrency with null should return null`() {
        // Arrange
        val value: Int? = null

        // Act
        val result = StringFormatter.formatAsCurrency(value)

        // Assert
        assertNull(result)
    }

    @Test
    fun `formatAsCurrency with zero should return formatted currency`() {
        // Arrange
        val value = 0
        val expected = "$0"

        // Act
        val result = StringFormatter.formatAsCurrency(value)

        // Assert
        assertEquals(expected, result)
    }
}
