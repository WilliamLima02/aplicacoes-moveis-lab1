package orcamento.movimentos

data class Movimento(
    val montante: Long,
    val data: Int,
    val periodicidade: Int,
    val tipo: String,
    val descricao: String
)

class GestorMovimentos {
    private val movimentos = mutableListOf<Movimento>()
    private var saldoMinimo: Long = 0L

    fun inserir(cmd: String) {
        val partes = cmd.trim().split(Regex("\\s+"))
        val montante = partes[1].toLong()
        val dataInicial = partes[2].toInt()
        val periodicidade = partes[3].toInt()
        val tipo = partes[4]
        val descricao = partes[5]
        var data = dataInicial

        while (data <= 999) {
            movimentos.add(Movimento(montante, data, periodicidade, tipo, descricao))
            if (periodicidade == 0) break
            data += periodicidade
        }
    }

    fun listar(cmd: String) {
        val partes = cmd.trim().split(Regex("\\s+"))
        val data = partes[1].toInt()
        val encontrados = movimentos.filter { it.data == data }

        for ((indice, movimento) in encontrados.withIndex()) {
            println("$indice ${movimento.montante} ${movimento.tipo} ${movimento.descricao}")
        }
    }

    fun remover(cmd: String) {
        val partes = cmd.trim().split(Regex("\\s+"))
        val data = partes[1].toInt()
        val indice = partes[2].toInt()
        val encontrados = movimentos.filter { it.data == data }

        if (indice in encontrados.indices) {
            val escolhido = encontrados[indice]
            val indiceGlobal = movimentos.indexOfFirst { it === escolhido }
            if (indiceGlobal != -1) movimentos.removeAt(indiceGlobal)
        }
    }

    private fun calcularBalancos(): LongArray {
        val balancos = LongArray(1000)

        for (movimento in movimentos) {
            val impacto = if (movimento.tipo == "r") movimento.montante else -movimento.montante
            balancos[movimento.data] += impacto
        }

        for (data in 1..999) {
            balancos[data] += balancos[data - 1]
        }
        return balancos
    }

    fun calcularBalanco(cmd: String) {
        val data = cmd.trim().split(Regex("\\s+"))[1].toInt()
        println(calcularBalancos()[data])
    }

    fun alterarSaldoMinimo(cmd: String) {
        saldoMinimo = cmd.trim().split(Regex("\\s+"))[1].toLong()
    }

    fun validarOrcamento() {
        for (balanco in calcularBalancos()) {
            if (balanco < saldoMinimo) {
                println("Invalido")
                return
            }
        }
        println("Valido")
    }

    fun procurarDataPossivel(cmd: String) {
        val partes = cmd.trim().split(Regex("\\s+"))
        val dataInicial = partes[1].toInt()
        val despesa = partes[2].toLong()
        val balancos = calcularBalancos()

        if (balancos.any { it < saldoMinimo }) {
            println("Incomportavel")
            return
        }

        for (data in dataInicial..999) {
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
