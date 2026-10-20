
fun main() {
    while (true) {
        print("> ")

        val cmd = readln()
        val partes = cmd.trim().split(Regex("\\s+"))
        val operacao = partes[0]

        // Identifica o comando e chama a função correspondente.
        val resultado = when (operacao) {
            "i" -> inserirMovimento(cmd)
            "l" -> listarMovimentos(cmd)
            "r" -> removerMovimento(cmd)
            "b" -> consultarSaldo(cmd)
            "m" -> alterarSaldoMinimo(cmd)
            "v" -> validarOrcamento(cmd)
            "w" -> procurarDataPossivel(cmd)
            "x" -> "Programa terminado."
            else -> "Comando desconhecido: $operacao"
        }

        println(resultado)

        // Termina o programa quando o utilizador escreve "x".
        if (operacao == "x") {
            break
        }
    }
}

// --------------------------------------------------
// 1. Inserir um movimento
// --------------------------------------------------
fun inserirMovimento(cmd: String): String {
    // TODO: interpretar os dados do comando.
    // TODO: guardar o movimento.
    // TODO: devolver uma mensagem de confirmação.

    return "Função de inserir movimento chamada."
}

// --------------------------------------------------
// 2. Listar movimentos numa data
// --------------------------------------------------
fun listarMovimentos(cmd: String): String {
    // TODO: extrair a data do comando.
    // TODO: procurar os movimentos dessa data.
    // TODO: devolver a lista encontrada.

    return "Função de listar movimentos chamada."
}

// --------------------------------------------------
// 3. Remover um movimento
// --------------------------------------------------
fun removerMovimento(cmd: String): String {
    // TODO: extrair a data e o índice do movimento.
    // TODO: remover o movimento indicado.

    return "Função de remover movimento chamada."
}

// --------------------------------------------------
// 4. Consultar o saldo numa data
// --------------------------------------------------
fun consultarSaldo(cmd: String): String {
    // TODO: calcular o saldo acumulado até à data indicada.

    return "Função de consultar saldo chamada."
}

// --------------------------------------------------
// 5. Alterar o saldo mínimo
// --------------------------------------------------
fun alterarSaldoMinimo(cmd: String): String {
    // TODO: extrair o novo saldo mínimo.
    // TODO: guardar esse valor.

    return "Função de alterar saldo mínimo chamada."
}

// --------------------------------------------------
// 6. Validar o orçamento
// --------------------------------------------------
fun validarOrcamento(cmd: String): String {
    // TODO: verificar se o saldo respeita o mínimo definido.

    return "Função de validar orçamento chamada."
}

// --------------------------------------------------
// 7. Procurar uma data em que uma despesa seja possível
// --------------------------------------------------
fun procurarDataPossivel(cmd: String): String {
    // TODO: ler a data e o valor da despesa.
    // TODO: procurar a primeira data em que a despesa
    // possa ser suportada sem violar o saldo mínimo.

    return "Função de procurar data chamada."
}
