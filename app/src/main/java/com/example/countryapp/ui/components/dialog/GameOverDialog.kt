package com.example.countryapp.ui.components.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.countryapp.R
import com.example.countryapp.ui.theme.CountryAppTheme

@Composable
fun GameOverDialog(
    resetGame: () -> Unit,
    wordChosen: String?,
    hitsCount: Int,
    exitGame: () -> Unit
) {

    AlertDialog(
        icon = {
            Image(
                painter = painterResource(id = R.drawable.ic_wrong_quiz),
                contentDescription = null
            )
        },
        title = { GameOverText() },
        text = {
            if (wordChosen != null) {
                DialogContentColumn(wordChosen = wordChosen, hitsCount)
            }
        },
        onDismissRequest = { resetGame() },
        confirmButton = {
            Button(
                onClick = { resetGame() },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Play again")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { exitGame() },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Exit game")
            }
        },
    )
}

@Composable
private fun GameOverText() {
    Text(
        text = "Oh no..",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun DialogContentColumn(
    wordChosen: String,
    hitsCount: Int
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        RevealedWordRow(wordChosen)
        Divider(
            modifier = Modifier
                .alpha(0.36f)
                .fillMaxWidth(0.5f)
                .padding(vertical = 12.dp),
            color = MaterialTheme.colorScheme.primary
        )
        ShowHowManyHitsUserGot(hitsCount = hitsCount)
    }
}


@Composable
private fun RevealedWordRow(
    wordChosen: String,
) {
    Row {
        Text(
            text = stringResource(
                id = R.string.the_word_was_n_s,
                wordChosen
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ShowHowManyHitsUserGot(hitsCount: Int) {
    Row(
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = stringResource(
                id = R.string.show_how_many_hints,
                hitsCount
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun GameOverDialogPreview() {
    CountryAppTheme {
        GameOverDialog(
            resetGame = { /*TODO*/ },
            wordChosen = "BUCHAREST",
            hitsCount = 3,
            exitGame = {}
        )
    }
}