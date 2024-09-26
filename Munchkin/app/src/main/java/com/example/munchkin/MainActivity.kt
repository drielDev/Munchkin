package com.example.munchkin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            // Estado compartilhado dos jogadores
            var jogadores by remember {
                mutableStateOf(
                    List(6) { index -> Jogador(nome = "Jogador ${index + 1}", level = 1, equipamento = 0, modificador = 0) }
                )
            }

            NavHost(navController = navController, startDestination = "listaJogadores") {
                composable("listaJogadores") {
                    MainScreen(navController = navController, jogadores = jogadores)
                }
                composable("jogador/{jogadorIndex}") { backStackEntry ->
                    val jogadorIndex = backStackEntry.arguments?.getString("jogadorIndex")?.toInt() ?: 0
                    JogadorScreen(
                        jogadorIndex = jogadorIndex,
                        navController = navController,
                        jogadores = jogadores,
                        onJogadorChange = { index, jogadorAtualizado ->
                            jogadores = jogadores.toMutableList().apply {
                                set(index, jogadorAtualizado)
                            }
                        }
                    )
                }
            }
        }
    }
}




@Composable
fun MainScreen(
    navController: NavController,
    jogadores: List<Jogador>
) {
    Column {
        jogadores.forEachIndexed { index, jogador ->
            // Botão que navega para a tela individual do jogador
            Button(onClick = {
                navController.navigate("jogador/$index")
            }) {
                Text(text = jogador.nome)  // Exibe o nome atualizado na lista
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
fun JogadorScreen(jogadorIndex: Int, navController: NavController, jogadores: List<Jogador>, onJogadorChange: (Int, Jogador) -> Unit) {
    val context = LocalContext.current

    // Pegando o jogador atual pelo índice
    val jogador = jogadores[jogadorIndex]

    Column(modifier = Modifier.padding(16.dp)) {
        JogadorUI(jogador = jogador, onJogadorChange = { novoJogador ->
            // Atualiza o jogador na lista ao clicar no botão "Atualizar nome"
            onJogadorChange(jogadorIndex, novoJogador)
        })

        Spacer(modifier = Modifier.height(16.dp))

        // Parte de Lutar contra um Monstro
        val monstro = Monstro(nome = "Dragão", nivel = 5)

        Text(text = "Monstro: ${monstro.nome} (Nível: ${monstro.nivel})")

        Button(onClick = {
            val resultado = lutar(jogador, monstro)
            if (resultado) {
                Toast.makeText(context, "Vitória! Você derrotou o ${monstro.nome}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Derrota! O ${monstro.nome} foi mais forte.", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Lutar contra o Monstro")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigateUp()  // Voltar para a tela anterior
        }) {
            Text("Voltar")
        }
    }
}

@Composable
fun JogadorUI(jogador: Jogador, onJogadorChange: (Jogador) -> Unit) {
    var nomeTemp by remember { mutableStateOf(jogador.nome) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo de texto para editar o nome
        TextField(
            value = nomeTemp,
            onValueChange = { novoNome ->
                nomeTemp = novoNome  // Atualiza o nome temporariamente enquanto o usuário digita
            },
            label = { Text("Nome do Jogador") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botão para confirmar a atualização do nome
        Button(onClick = {
            // Atualiza permanentemente o nome do jogador
            onJogadorChange(jogador.copy(nome = nomeTemp))
            Toast.makeText(context, "Nome atualizado!!", Toast.LENGTH_SHORT).show()
        }) {
            Text(text = "Atualizar nome")
        }


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
                if (jogador.level < 10) {
                    onJogadorChange(jogador.copy(level = jogador.level + 1))
                }
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

        // Poder Total (Calculado dinamicamente)
        Text(text = "Poder Total: ${jogador.poderDeAtaque()}")
    }
}







fun lutar(jogador: Jogador, monstro: Monstro): Boolean {
    return jogador.poderDeAtaque() > monstro.nivel
}



@Preview(showBackground = true)
@Composable
fun LayoutPreview() {

}