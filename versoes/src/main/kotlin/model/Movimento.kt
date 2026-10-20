package model

data class Movimento(
    val montante: Long,
    val data: Int,
    val periodicidade: Int,
    val tipo: String,
    val descricao: String
)