package seifemadhamdy.vekoz.presentation.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import seifemadhamdy.vekoz.core.constants.TmdbUrls
import seifemadhamdy.vekoz.data.remote.dto.MovieCastDto
import seifemadhamdy.vekoz.data.remote.dto.MovieCreditsCrewDto

sealed class Person {
  data class Actor(val actor: MovieCastDto) : Person()

  data class Director(val director: MovieCreditsCrewDto) : Person()
}

@Composable
fun PersonCard(person: Person) {
  Card(
      modifier =
          Modifier.width((LocalConfiguration.current.screenWidthDp.dp / 3) - 26.dp).aspectRatio(1f),
      shape = CircleShape) {
        val profilePath =
            when (person) {
              is Person.Actor -> person.actor.profilePath
              is Person.Director -> person.director.profilePath
            }

        AsyncImage(
            model = "${TmdbUrls.BASE_IMAGE_URL}/original${profilePath}",
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop)
      }
}
