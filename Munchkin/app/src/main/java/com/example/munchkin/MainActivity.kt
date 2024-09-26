package com.example.munchkin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.munchkin.ui.theme.MunchkinTheme
import org.jetbrains.annotations.Contract

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Main()

        }
    }
}

@Composable
fun Main() {

    var jogadores by remember {
        mutableStateOf(
            List(6) { index -> Jogador(nome = "Jogador ${index + 1}", level = 1, equipamento = 0, modificador = 0) }
        )
    }

    ListaJogadores(jogadores = jogadores, onJogadorChange = { index, novoJogador ->
        jogadores = jogadores.toMutableList().apply { set(index, novoJogador) }
    })

}

@Composable
fun JogadorUI(jogador: Jogador, onJogadorChange: (Jogador) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Nome do Jogador
        TextField(
            value = jogador.nome,
            onValueChange = { novoNome ->
                onJogadorChange(jogador.copy(nome = novoNome))
            },
            label = { Text("Nome do Jogador") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Level do Jogador
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                if (jogador.level > 1) {
                    onJogadorChange(jogador.copy(level = jogador.level - 1))
                }
            }) {
                Text("-")
            }
            Text(text = "Level: ${jogador.level}", modifier = Modifier.padding(horizontal = 8.dp))
            Button(onClick = {
                onJogadorChange(jogador.copy(level = jogador.level + 1))
            }) {
                Text("+")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Bônus de Equipamento
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                onJogadorChange(jogador.copy(equipamento = jogador.equipamento - 1))
            }) {
                Text("-")
            }
            Text(text = "Bônus de Equipamento: ${jogador.equipamento}", modifier = Modifier.padding(horizontal = 8.dp))
            Button(onClick = {
                onJogadorChange(jogador.copy(equipamento = jogador.equipamento + 1))
            }) {
                Text("+")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Modificadores
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                onJogadorChange(jogador.copy(modificador = jogador.modificador - 1))
            }) {
                Text("-")
            }
            Text(text = "Modificadores: ${jogador.modificador}", modifier = Modifier.padding(horizontal = 8.dp))
            Button(onClick = {
                onJogadorChange(jogador.copy(modificador = jogador.modificador + 1))
            }) {
                Text("+")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Poder Total
        Text(text = "Poder Total: ${jogador.poderDeAtaque()}")
    }
}

@Composable
fun ListaJogadores(jogadores: List<Jogador>, onJogadorChange: (Int, Jogador) -> Unit) {
    Column {
        jogadores.forEachIndexed { index, jogador ->
            JogadorUI(jogador = jogador, onJogadorChange = { novoJogador ->
                onJogadorChange(index, novoJogador)
            })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}



@Composable
fun lutar(jogador: Jogador, monstro: Monstro): Boolean {
    return jogador.poderDeAtaque() > monstro.nivel
}


@Preview(showBackground = true)
@Composable
fun LayoutPreview() {
    Main()
}