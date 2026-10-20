
import model.Movimento

fun main() {
    val lista = mutableListOf<Movimento>()

    while (true) {
        val cmd = readlnOrNull() ?: break
        if (cmd.isBlank()) continue

        when (cmd.trim().split(Regex("\\s+"))[0]) {
            "i" -> inserir(cmd, lista)
            "l" -> listar(cmd, lista)
            "r" -> remover(cmd, lista)
            else -> println("Comando ainda nao implementado")
        }
    }
}

fun inserir(cmd: String, lista: MutableList<Movimento>) {
    val p = cmd.trim().split(Regex("\\s+"))

    if (p.size != 6) return

    val montante = p[1].toLongOrNull() ?: return
    val data = p[2].toIntOrNull() ?: return
    val periodicidade = p[3].toIntOrNull() ?: return
    val tipo = p[4]
    val descricao = p[5]

    if (montante < 0 || data !in 0..999 ||
        periodicidade < 0 || tipo !in listOf("r", "d") ||
        descricao.length > 60
    ) return

    lista.add(
        Movimento(montante, data, periodicidade, tipo, descricao)
    )
}

fun listar(cmd: String, lista: List<Movimento>) {
    val p = cmd.trim().split(Regex("\\s+"))
    if (p.size != 2) return

    val data = p[1].toIntOrNull() ?: return
    if (data !in 0..999) return

    val movimentosDoDia = lista.filter {
        data >= it.data &&
                (if (it.periodicidade == 0) {
                    data == it.data
                } else {
                    (data - it.data) % it.periodicidade == 0
                })
    }

    movimentosDoDia.forEachIndexed { index, movimento ->
        println("$index $movimento")
    }
}

fun remover(cmd: String, lista: MutableList<Movimento>) {
    val p = cmd.trim().split(Regex("\\s+"))
    if (p.size != 3) return

    val data = p[1].toIntOrNull() ?: return
    val indice = p[2].toIntOrNull() ?: return

    if (data !in 0..999 || indice < 0) return

    val movimentosDoDia = lista.filter {
        data >= it.data &&
                (if (it.periodicidade == 0) {
                    data == it.data
                } else {
                    (data - it.data) % it.periodicidade == 0
                })
    }

    val movimento = movimentosDoDia.getOrNull(indice) ?: return
    lista.remove(movimento)
}
