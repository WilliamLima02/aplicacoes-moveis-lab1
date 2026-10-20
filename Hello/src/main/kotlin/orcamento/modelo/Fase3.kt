package orcamento.modelo

import orcamento.movimentos.GestorMovimentos
import java.time.LocalDateTime

data class Sessao(
    val token: String,
    val dataHora: LocalDateTime
)

class Utilizador(
    val email: String,
    val password: String
) {
    val orcamento = GestorMovimentos()
    val sessoes = mutableListOf<Sessao>()
    var sessaoAtiva: Sessao? = null
}
