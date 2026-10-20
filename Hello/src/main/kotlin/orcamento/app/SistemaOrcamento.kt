package orcamento.app

import orcamento.model.Sessao
import orcamento.model.Utilizador
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.UUID

class SistemaOrcamento {
    private val utilizadores = mutableMapOf<String, Utilizador>()
    private val tokensEmitidos = mutableSetOf<String>()
    private var utilizadorAtual: Utilizador? = null

    fun executar() {
        while (true) {
            val linha = readlnOrNull() ?: break
            val cmd = linha.trim()
            if (cmd.isEmpty()) continue
            val partes = cmd.split(Regex("\\s+"))
            when (partes[0]) {
                "u" -> iniciarSessao(partes)
                "exit" -> terminarSessao()
                "sair" -> break
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
        while (true) {
            val valor = UUID.randomUUID().toString()
            val bytes = MessageDigest.getInstance("SHA-256")
                .digest(valor.toByteArray(Charsets.UTF_8))
            val token = bytes.joinToString("") {
                "%02x".format(it.toInt() and 0xff)
            }
            if (tokensEmitidos.add(token)) return token
        }
    }

    private fun terminarSessao() {
        val utilizador = utilizadorAtual ?: return
        utilizador.sessaoAtiva = null
        utilizadorAtual = null
    }

    private fun executarComando(cmd: String) {
        val utilizador = utilizadorAtual ?: return
        when (cmd.split(Regex("\\s+"))[0]) {
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
