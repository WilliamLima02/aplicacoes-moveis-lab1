package orcamento.model

import java.math.BigInteger

data class Movimento(
    val montante: BigInteger,
    val data: Int,
    val periodicidade: Int,
    val tipo: Char,
    val descricao: String
)

class GestorMovimentos {
    private val movimentos = mutableListOf<Movimento>()

    var saldoMinimo: BigInteger = BigInteger.ZERO
        private set

    fun inserir(cmd: String) {
        val p = tokens(cmd) ?: return
        if (p.size != 6 || p[0] != "i") return

        val montante = p[1].toBigIntegerOrNull() ?: return
        val dataInicial = p[2].toIntOrNull() ?: return
        val periodicidade = p[3].toIntOrNull() ?: return
        val tipo = p[4].singleOrNull() ?: return
        val descricao = p[5]

        if (montante < BigInteger.ZERO ||
            dataInicial !in 0..999 ||
            periodicidade !in 0..999 ||
            tipo !in listOf('r', 'd') ||
            descricao.isEmpty() ||
            descricao.length > 60 ||
            descricao.any { it.isWhitespace() }
        ) return

        val novasOcorrencias = mutableListOf<Movimento>()
        var data = dataInicial
        while (data <= 999) {
            novasOcorrencias.add(
                Movimento(montante, data, periodicidade, tipo, descricao)
            )
            if (periodicidade == 0) break
            data += periodicidade
        }

        val countsByDate = movimentos.groupingBy { it.data }.eachCount().toMutableMap()
        for (m in novasOcorrencias) {
            val count = (countsByDate[m.data] ?: 0) + 1
            if (count > MAX_MOVEMENTS_PER_DATE) return
            countsByDate[m.data] = count
        }

        movimentos.addAll(novasOcorrencias)
    }

    fun listar(cmd: String) {
        val p = tokens(cmd) ?: return
        if (p.size != 2 || p[0] != "l") return
        val data = p[1].toIntOrNull() ?: return
        if (data !in 0..999) return

        movimentos.asSequence()
            .filter { it.data == data }
            .forEachIndexed { index, m ->
                println("$index ${m.montante} ${m.tipo} ${m.descricao}")
            }
    }

    fun remover(cmd: String) {
        val p = tokens(cmd) ?: return
        if (p.size != 3 || p[0] != "r") return
        val data = p[1].toIntOrNull() ?: return
        val indice = p[2].toIntOrNull() ?: return
        if (data !in 0..999 || indice < 0) return

        val globalIndex = movimentos.withIndex()
            .filter { it.value.data == data }
            .getOrNull(indice)
            ?.index ?: return
        movimentos.removeAt(globalIndex)
    }

    fun calcularBalanco(cmd: String) {
        val p = tokens(cmd) ?: return
        if (p.size != 2 || p[0] != "b") return
        val data = p[1].toIntOrNull() ?: return
        if (data !in 0..999) return
        println(calcularBalancos()[data])
    }

    fun alterarSaldoMinimo(cmd: String) {
        val p = tokens(cmd) ?: return
        if (p.size != 2 || p[0] != "m") return
        val novoMinimo = p[1].toBigIntegerOrNull() ?: return
        saldoMinimo = novoMinimo
    }

    fun validarOrcamento() {
        val balancos = calcularBalancos()
        if (balancos.any { it < saldoMinimo }) {
            println("Invalido")
        } else {
            println("Valido")
        }
    }

    fun procurarDataPossivel(cmd: String) {
        val p = tokens(cmd) ?: return
        if (p.size != 3 || p[0] != "w") return
        val inicio = p[1].toIntOrNull() ?: return
        val despesa = p[2].toBigIntegerOrNull() ?: return
        if (inicio !in 0..999 || despesa < BigInteger.ZERO) return

        val balancos = calcularBalancos()

        // An expense cannot repair an already-invalid budget.
        if (balancos.any { it < saldoMinimo }) {
            println("Incomportavel")
            return
        }

        for (data in inicio..999) {
            var possivel = true
            for (dia in data..999) {
                if (balancos[dia] - despesa < saldoMinimo) {
                    possivel = false
                    break
                }
            }
            if (possivel) {
                println(data)
                return
            }
        }
        println("Incomportavel")
    }

    private fun calcularBalancos(): Array<BigInteger> {
        val balancos = Array(DAYS) { BigInteger.ZERO }
        for (m in movimentos) {
            val impacto = if (m.tipo == 'r') m.montante else m.montante.negate()
            balancos[m.data] = balancos[m.data] + impacto
        }
        for (data in 1..999) {
            balancos[data] = balancos[data] + balancos[data - 1]
        }
        return balancos
    }

    private fun tokens(cmd: String): List<String>? {
        val trimmed = cmd.trim()
        if (trimmed.isEmpty()) return null
        return trimmed.split(Regex("\\s+"))
    }

    private companion object {
        const val DAYS = 1000
        const val MAX_MOVEMENTS_PER_DATE = 100
    }
}
