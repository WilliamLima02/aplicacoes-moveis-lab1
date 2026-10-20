
fun main() {
    while (true) {
        val cmd: String = readln()
        val partes = cmd.trim().split(Regex("\\s+"))

        if (partes.isEmpty()) {
            continue
        }

        val comando = partes[0]

        when (comando) {
            "i" -> inserir(cmd)
            "l" -> listar(cmd)
            "r" -> remover(cmd)
            "b" -> calcularBalanco(cmd)
            "m" -> definirMinimo(cmd)
            "v" -> validar(cmd)
            "w" -> procurarData(cmd)
            else -> println("Comando invalido")
        }
    }
}

fun inserir(cmd: String) {
    println("Comando: i")
}

fun listar(cmd: String) {
    println("Comando: l")
}

fun remover(cmd: String) {
    println("Comando: r")
}

fun calcularBalanco(cmd: String) {
    println("Comando: b")
}

fun definirMinimo(cmd: String) {
    println("Comando: m")
}

fun validar(cmd: String) {
    println("Comando: v")
}

fun procurarData(cmd: String) {
    println("Comando: w")
}
