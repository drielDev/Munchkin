package com.example.munchkin

data class Jogador(
    var nome: String,
    var level: Int,
    var equipamento: Int,
    var modificador: Int
) {
    fun poderDeAtaque(): Int {
        return level + equipamento + modificador
    }
}