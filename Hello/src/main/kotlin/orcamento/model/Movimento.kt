package orcamento.model

data class Movimento(
    val montante: Long,
    val data: Int,
    val periodicidade: Int,
    val tipo: String,
    val descricao: String
)

class GestorMovimentos {
    private val movimentos = mutableListOf<Movimento>()
    var saldoMinimo: Long = 0L
        private set

    fun inserir(cmd: String) {
        val p = cmd.trim().split(Regex("\\s+"))
        val montante = p[1].toLong()
        val dataInicial = p[2].toInt()
        val periodicidade = p[3].toInt()
        val tipo = p[4]
        val descricao = p[5]
        var data = dataInicial
        while (data <= 999) {
            movimentos.add(Movimento(montante, data, periodicidade, tipo, descricao))
            if (periodicidade == 0) break
            data += periodicidade
        }
    }

    fun listar(cmd: String) {
        val data = cmd.trim().split(Regex("\\s+"))[1].toInt()
        val encontrados = movimentos.filter { it.data == data }
        for ((indice, m) in encontrados.withIndex()) {
            println("$indice ${m.montante} ${m.tipo} ${m.descricao}")
        }
    }

    fun remover(cmd: String) {
        val p = cmd.trim().split(Regex("\\s+"))
        val data = p[1].toInt()
        val indice = p[2].toInt()
        val encontrados = movimentos.filter { it.data == data }
        if (indice in encontrados.indices) {
            val alvo = encontrados[indice]
            val global = movimentos.indexOfFirst { it === alvo }
            if (global >= 0) movimentos.removeAt(global)
        }
    }

    private fun calcularBalancos(): LongArray {
        val b = LongArray(1000)
        for (m in movimentos) {
            val impacto = if (m.tipo == "r") m.montante else -m.montante
            b[m.data] += impacto
        }
        for (data in 1..999) b[data] += b[data - 1]
        return b
    }

    fun calcularBalanco(cmd: String) {
        val data = cmd.trim().split(Regex("\\s+"))[1].toInt()
        println(calcularBalancos()[data])
    }

    fun alterarSaldoMinimo(cmd: String) {
        saldoMinimo = cmd.trim().split(Regex("\\s+"))[1].toLong()
    }

    fun validarOrcamento() {
        for (b in calcularBalancos()) {
            if (b < saldoMinimo) {
                println("Invalido")
                return
            }
        }
        println("Valido")
    }

    fun procurarDataPossivel(cmd: String) {
        val p = cmd.trim().split(Regex("\\s+"))
        val inicio = p[1].toInt()
        val despesa = p[2].toLong()
        val balancos = calcularBalancos()
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
}
