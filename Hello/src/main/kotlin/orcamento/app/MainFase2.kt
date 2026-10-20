package orcamento.app

import orcamento.movimentos.GestorMovimentos

fun main() {
    val gestor = GestorMovimentos()

    while (true) {
        val cmd = readlnOrNull() ?: break
        if (cmd.isBlank()) continue

        when (cmd.trim().split(Regex("\\s+"))[0]) {
            "i" -> gestor.inserir(cmd)
            "l" -> gestor.listar(cmd)
            "r" -> gestor.remover(cmd)
            "b" -> gestor.calcularBalanco(cmd)
            "m" -> gestor.alterarSaldoMinimo(cmd)
            "v" -> gestor.validarOrcamento()
            "w" -> gestor.procurarDataPossivel(cmd)
        }
    }
}
