package orcamento.app

import orcamento.modelo.Sessao
import orcamento.modelo.Utilizador
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.UUID

class SistemaOrcamento {
    private val utilizadores = mutableMapOf<String, Utilizador>()
    private var utilizadorAtual: Utilizador? = null

    fun executar() {
        while (true) {
            val cmd = readlnOrNull() ?: break
            if (cmd.isBlank()) continue

            val partes = cmd.trim().split(Regex("\\s+"))
            when (partes[0]) {
                "u" -> iniciarSessao(partes)
                "exit" -> terminarSessao()
                else -> executarComando(cmd)
            }
        }
    }

    private fun iniciarSessao(partes: List<String>) {
        val email = partes[1]
        val password = partes[2]
        val existente = utilizadores[email]

        if (existente != null && existente.password != password) {
            println("Credenciais invalidas")
            return
        }

        val utilizador = existente ?: Utilizador(email, password).also {
            utilizadores[email] = it
        }

        utilizadorAtual?.sessaoAtiva = null
        val sessao = Sessao(criarToken(), LocalDateTime.now())
        utilizador.sessoes.add(sessao)
        utilizador.sessaoAtiva = sessao
        utilizadorAtual = utilizador
    }

    private fun criarToken(): String {
        val valorAleatorio = UUID.randomUUID().toString()
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(valorAleatorio.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun terminarSessao() {
        val utilizador = utilizadorAtual ?: return
        utilizador.sessaoAtiva = null
        utilizadorAtual = null
    }

    private fun executarComando(cmd: String) {
        val utilizador = utilizadorAtual ?: return
        when (cmd.trim().split(Regex("\\s+"))[0]) {
            "i" -> utilizador.orcamento.inserir(cmd)
            "l" -> utilizador.orcamento.listar(cmd)
            "r" -> utilizador.orcamento.remover(cmd)
            "b" -> utilizador.orcamento.calcularBalanco(cmd)
            "m" -> utilizador.orcamento.alterarSaldoMinimo(cmd)
            "v" -> utilizador.orcamento.validarOrcamento()
            "w" -> utilizador.orcamento.procurarDataPossivel(cmd)
        }
    }
}
