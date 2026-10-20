# Laboratório #1 --- Introdução à Linguagem Kotlin

## Sistema de Orçamento Pessoal --- Fase 3

README de estudo e execução. Linguagem: Kotlin. Interface: consola. Os
dados são mantidos em memória e perdem-se quando a aplicação termina.

## Índice

1.  Descrição do laboratório
2.  Objetivos
3.  Requisitos e comandos
4.  Estrutura do projeto
5.  Como executar
6.  Testes prontos
7.  Como funciona o balanço acumulado
8.  Utilizadores, sessões e tokens
9. Princípios de POO
10. Tabela de sintaxe Kotlin
11. Dificuldades e soluções
12. Erros comuns
13. Complexidade
14. Melhorias possíveis
15. Checklist final

------------------------------------------------------------------------

## 1. Descrição do laboratório

O Laboratório #1 introduz a linguagem Kotlin através da implementação de
um sistema de orçamento pessoal executado na consola. O sistema regista
receitas e despesas em datas inteiras de `0` a `999`, permite consultar
e remover movimentos, calcular balanços acumulados, definir um saldo
mínimo e validar o orçamento.

A Fase 3 acrescenta utilizadores e sessões. O comando `u email password`
cria um utilizador quando o email ainda não existe ou autentica um
utilizador existente. Cada utilizador tem o seu próprio orçamento.
`exit` termina a sessão ativa, mas mantém a aplicação em execução.

**Persistência:** esta versão mantém tudo em memória. Ao reiniciar a
aplicação, os utilizadores, movimentos e sessões desaparecem.

## 2. Objetivos de aprendizagem

-   Sintaxe básica de Kotlin, funções e classes.
-   `val`, `var`, `data class`, propriedades e modificadores de acesso.
-   Listas, mapas e conjuntos mutáveis.
-   Ciclos `for` e `while`, condições `if` e `when`.
-   Vetores, funções com retorno e conversões numéricas.
-   Expressões regulares e processamento de comandos.
-   EOF e leitura de linhas do terminal.
-   Balanços acumulados.
-   Modelação de utilizadores e sessões.
-   UUID, SHA-256 e data/hora.

## 3. Requisitos e comandos

Um movimento contém montante, data, periodicidade, tipo e descrição. O
tipo `r` representa receita e `d` despesa. Periodicidade `0` significa
uma única ocorrência; um valor positivo repete o movimento em intervalos
dessa periodicidade até à data 999.

  ------------------------------------------------------------------------------------
  Comando                                          Função
  ------------------------------------------------ -----------------------------------
  `i montante data periodicidade r\|d descricao`   Inserir movimento

  `l data`                                         Listar movimentos dessa data

  `r data indice`                                  Remover uma ocorrência da listagem
                                                   dessa data

  `b data`                                         Imprimir balanço acumulado até à
                                                   data

  `m minimo`                                       Definir saldo mínimo

  `v`                                              Imprimir `Valido` ou `Invalido`

  `w data montante`                                Procurar a primeira data possível
                                                   para uma despesa adicional

  `u email password`                               Criar utilizador ou iniciar sessão

  `exit`                                           Terminar a sessão, sem terminar a
                                                   aplicação
  ------------------------------------------------------------------------------------

A listagem apresenta índices locais à data, começando em zero. A remoção
elimina uma ocorrência, não todas as ocorrências periódicas. A
implementação atual espera que a descrição seja uma única palavra, pois
usa `p[5]`.

## 4. Estrutura do projeto

``` text
orcamento-kotlin/
├── settings.gradle.kts
├── build.gradle.kts
└── src/main/kotlin/orcamento/
    ├── app/
    │   ├── Main.kt
    │   └── SistemaOrcamento.kt
    └── model/
        ├── Utilizador.kt
        └── Movimento.kt
```

-   **`Main.kt`**: cria `SistemaOrcamento` e chama `executar()`.
-   **`SistemaOrcamento.kt`**: lê comandos, autentica utilizadores, gere
    sessões e encaminha comandos.
-   **`model/Utilizador.kt`**: define `Sessao` e `Utilizador`.
-   **`model/Movimento.kt`**: define `Movimento` e `GestorMovimentos`, com a
    lógica do orçamento.
-   **Gradle**: configura o projeto Kotlin e a compilação.

O ponto de entrada é:

``` kotlin
package orcamento.app

fun main() {
    SistemaOrcamento().executar()
}
```

## 5. Como executar

### IntelliJ IDEA

1.  Abre a pasta raiz que contém `settings.gradle.kts`.
2.  Aguarda a sincronização do Gradle.
3.  Confirma que o JDK configurado corresponde ao `jvmToolchain` do
    projeto.
4.  Abre `src/main/kotlin/orcamento/app/Main.kt`.
5.  Executa `main()` e escreve os comandos na consola.
6.  **Começa sempre por `u email password`** antes de executar comandos
    do orçamento.

### Terminal

Se o projeto tiver Gradle Wrapper e a configuração `application` com a
classe principal correta, executa:

Windows:

``` powershell
.\gradlew.bat run
```

Linux/macOS:

``` bash
./gradlew run
```

Se `run` não estiver configurado, executa `Main.kt` pelo IntelliJ.

### EOF versus `exit`

O ciclo principal usa `readlnOrNull() ?: break`. `readlnOrNull()`
devolve `null` quando a entrada termina; `?:` é o operador Elvis;
`break` termina o ciclo. Em muitos terminais usa-se `Ctrl+D`; na consola
clássica do Windows, normalmente `Ctrl+Z` e depois Enter. O botão Stop
do IntelliJ também interrompe o processo. `exit` apenas termina a
sessão.

## 6. Testes prontos

Executa cada teste numa execução nova, a menos que o teste diga o
contrário.

### Teste A --- Inserir, listar e calcular

Entrada:

``` text
u william teste123
i 150 0 0 r Salario
i 20 0 0 d Transporte
l 0
b 0
```

Saída:

``` text
0 150 r Salario
1 20 d Transporte
130
```

### Teste B --- Balanço acumulado

Entrada:

``` text
u ana senha
i 150 0 0 r Salario
i 30 1 0 d Alimentacao
i 20 2 0 d Transporte
b 0
b 1
b 2
```

Saída:

``` text
150
120
100
```

### Teste C --- Periodicidade

Entrada:

``` text
u periodicidade senha
i 10 0 2 d Transporte
l 0
l 1
l 2
l 4
```

Saída:

``` text
0 10 d Transporte
0 10 d Transporte
0 10 d Transporte
```

A data 1 não tem movimentos; por isso não imprime linhas.

### Teste D --- Remover uma ocorrência

Entrada:

``` text
u remover senha
i 100 0 0 r Salario
i 20 0 0 d Transporte
l 0
r 0 1
l 0
b 0
```

Saída:

``` text
0 100 r Salario
1 20 d Transporte
0 100 r Salario
100
```

### Teste E --- Validar saldo mínimo

Entrada:

``` text
u validacao senha
i 100 0 0 r Salario
i 80 1 0 d Renda
m 30
v
b 0
b 1
```

Saída:

``` text
Valido
100
20
```

Se mudares o mínimo para `50`, `v` deve imprimir `Invalido`.

### Teste F --- Isolamento de utilizadores

Entrada:

``` text
u ana senha1
i 100 0 0 r Salario
b 0
exit
u bruno senha2
b 0
i 40 0 0 r Trabalho
b 0
exit
u ana senha1
b 0
```

Saída:

``` text
100
0
40
100
```

### Teste G --- Credenciais incorretas

Entrada:

``` text
u ana senha1
i 100 0 0 r Salario
exit
u ana errada
u ana senha1
b 0
```

Saída:

``` text
Credenciais invalidas
100
```

### Teste H --- `exit` não termina a aplicação

Entrada:

``` text
u william teste123
i 100 0 0 r Salario
exit
u ana senha
i 20 0 0 r Trabalho
b 0
exit
```

Saída:

``` text
20
```

Depois, envia EOF ou usa Stop para terminar o processo.

**Nota:** as saídas acima são resultados esperados para os comandos
apresentados. Compara sempre os testes com o código final guardado no
teu projeto e com o enunciado oficial.

## 7. Como funciona o balanço acumulado

Código:

``` kotlin
private fun calcularBalancos(): LongArray {
    val b = LongArray(1000)
    for (m in movimentos) {
        val impacto = if (m.tipo == "r") m.montante else -m.montante
        b[m.data] += impacto
    }
    for (data in 1..999) b[data] += b[data - 1]
    return b
}
```

1.  `LongArray(1000)` cria 1000 posições, inicialmente a zero.
2.  O primeiro ciclo percorre `movimentos`. Receitas têm impacto
    positivo; despesas têm impacto negativo. `b[m.data]` acumula o
    impacto líquido que ocorre exatamente nessa data.
3.  O segundo ciclo transforma os impactos em balanços acumulados:
    `b[data] = b[data] + b[data - 1]`.
4.  `return b` devolve o vetor completo.

Exemplo: impacto diário `130, -30, 50, 0` transforma-se em balanços
`130, 100, 150, 150`.

`calcularBalancos()` não recebe `movimentos` como parâmetro porque é um
método de `GestorMovimentos` e acede à propriedade da própria instância.
`calcularBalanco(cmd)` extrai a data e seleciona o elemento
correspondente:

``` kotlin
val data = cmd.trim().split(Regex("\\s+"))[1].toInt()
val balancos = calcularBalancos()
println(balancos[data])
```

O vetor local é recalculado em cada chamada. A complexidade é
`O(M + 1000)`, em que `M` é o número de movimentos guardados.

## 8. Utilizadores, sessões e tokens

O sistema mantém um mapa de utilizadores indexado por email. Cada
utilizador possui um gestor de movimentos, histórico de sessões e uma
referência à sessão ativa.

Ao receber `u email password`, o sistema: 1. Extrai as credenciais. 2.
Procura o email. 3. Cria o utilizador se não existir. 4. Se existir,
compara a palavra-passe. 5. Cria uma sessão, gera um token e regista a
sessão. 6. Define o utilizador atual.

O token é gerado a partir de um UUID aleatório, cujo conteúdo é
processado por SHA-256 e convertido para hexadecimal. O conjunto
`tokensEmitidos` impede reutilizações durante a execução atual.

**Nota de segurança:** esta versão académica guarda palavras-passe em
texto simples em memória. Um sistema real deve usar armazenamento seguro
de credenciais e hashing apropriado para palavras-passe. SHA-256 no
token não protege automaticamente a palavra-passe.

## 9. Princípios de Programação Orientada a Objetos (POO)

O projeto usa alguns princípios de POO de forma prática. É importante distinguir os princípios que estão efetivamente presentes daqueles que não foram implementados explicitamente.

### 9.1. Encapsulamento

Encapsulamento significa reunir estado e operações relacionadas numa classe e controlar como esse estado pode ser acedido ou alterado.

**Onde aparece:**
- Em `GestorMovimentos`, a lista `movimentos` é `private`, pelo que outras classes não a alteram diretamente. Em vez disso, usam operações como `inserir`, `listar` e `remover`.
- `saldoMinimo` tem `private set`: pode ser lido de fora da classe, mas só pode ser alterado através do próprio `GestorMovimentos`, usando `alterarSaldoMinimo`.
- Em `SistemaOrcamento`, os mapas e o utilizador atual são privados, protegendo o estado interno do sistema.

Exemplo:
```kotlin
private val movimentos = mutableListOf<Movimento>()
var saldoMinimo: Long = 0L
    private set
```

### 9.2. Abstração

Abstração consiste em expor operações úteis e esconder detalhes de implementação que quem chama a operação não precisa de conhecer.

**Onde aparece:**
- `calcularBalanco(cmd)` permite pedir o balanço de uma data sem obrigar o chamador a conhecer a construção do vetor de balanços.
- `SistemaOrcamento` encaminha comandos para o gestor de movimentos sem ter de implementar novamente os cálculos do orçamento.
- `iniciarSessao` concentra a lógica de autenticação e criação da sessão numa operação.

Por exemplo, o chamador pode executar `utilizador.orcamento.calcularBalanco(cmd)` sem saber como `calcularBalancos()` percorre os movimentos e calcula as somas acumuladas.

### 9.3. Separação de responsabilidades

É uma decisão de desenho relacionada com o princípio da responsabilidade única: cada parte deve ter uma responsabilidade clara.

- `Main.kt`: inicia a aplicação.
- `SistemaOrcamento.kt`: lê e encaminha comandos, gere utilizadores e sessões.
- `Utilizador.kt`: representa um utilizador e o seu estado de sessão.
- `Movimento.kt`: representa movimentos e reúne as operações de orçamento em `GestorMovimentos`.

Esta divisão torna o projeto mais fácil de compreender, testar e modificar. Não significa, por si só, que exista uma implementação formal de todos os princípios SOLID.

### 9.4. Composição / relação “tem um”

A composição aparece quando um objeto mantém referências a outros objetos que colaboram com ele.

- Um `Utilizador` **tem um** `GestorMovimentos` próprio.
- Um `Utilizador` **tem uma lista de** `Sessao` e pode ter uma `sessaoAtiva`.
- `SistemaOrcamento` **tem um mapa de** utilizadores.

A consequência importante é o isolamento dos orçamentos: cada utilizador tem a sua própria instância de `GestorMovimentos` e, portanto, a sua própria lista de movimentos e saldo mínimo.

### 9.5. Classes e objetos

`Movimento`, `Utilizador`, `Sessao`, `GestorMovimentos` e `SistemaOrcamento` são classes. Quando o código executa `Utilizador(email, password)` ou `SistemaOrcamento()`, cria instâncias (objetos) dessas classes.

`data class Movimento(...)` é usada para representar dados de uma ocorrência. `Utilizador` é uma classe normal porque gere estado e mantém relações com outros objetos.

### 9.6. Herança e polimorfismo

Na implementação atual, **não há uso explícito de herança nem de polimorfismo por subtipagem**. Não existe uma hierarquia de classes com `open`, `abstract`, interfaces implementadas ou métodos sobrescritos para diferentes tipos de utilizador/movimento. Isso não é um problema: não é necessário forçar todos os princípios de POO num projeto simples.

### Resumo dos princípios

| Princípio/conceito | Usado? | Onde e como |
|---|---|---|
| Encapsulamento | Sim | Propriedades privadas, `private set` e operações públicas do gestor. |
| Abstração | Sim | Métodos como `calcularBalanco` escondem os detalhes do cálculo. |
| Separação de responsabilidades | Sim | `Main`, sistema, utilizador/sessão e gestor de movimentos têm papéis distintos. |
| Composição | Sim | Utilizador tem orçamento e sessões; sistema tem utilizadores. |
| Herança | Não explicitamente | Não há subclasses na versão atual. |
| Polimorfismo por subtipagem | Não explicitamente | Não há interfaces ou métodos sobrescritos para variantes de objetos. |

## 10. Tabela de sintaxe Kotlin

  ---------------------------------------------------------------------------------------------
  Sintaxe                   Significado             Exemplo
  ------------------------- ----------------------- -------------------------------------------
  `package`                 Declara o pacote do     `package orcamento.app`
                            ficheiro                

  `import`                  Importa um tipo         `import java.time.LocalDateTime`

  `fun`                     Declara uma função      `fun executar()`

  `private`                 Restringe acesso ao     `private fun calcularBalancos()`
                            âmbito permitido        

  `val`                     Referência não pode ser `val data = 2`
                            reatribuída             

  `var`                     Variável pode ser       `var utilizadorAtual: Utilizador? = null`
                            reatribuída             

  `class`                   Declara uma classe      `class GestorMovimentos`

  `data class`              Classe orientada a      `data class Movimento(...)`
                            dados; gera `equals`,   
                            `hashCode`, `toString`, 
                            `copy`                  

  `Tipo?`                   Tipo anulável, pode ser `Utilizador?`
                            `null`                  

  `null`                    Ausência de             `utilizadorAtual = null`
                            objeto/valor            

  `MutableList<T>`          Lista cujo conteúdo     `mutableListOf<Movimento>()`
                            pode mudar              

  `mutableListOf<T>()`      Cria uma lista mutável  `mutableListOf<Sessao>()`

  `mutableMapOf<K,V>()`     Cria mapa mutável de    `mutableMapOf<String, Utilizador>()`
                            chaves para valores     

  `mutableSetOf<T>()`       Cria conjunto mutável   `mutableSetOf<String>()`
                            sem duplicados          

  `Int`                     Inteiro de 32 bits      Data

  `Long`                    Inteiro de 64 bits      Montante

  `LongArray(n)`            Vetor de `Long` com n   `LongArray(1000)`
                            posições a zero         

  `Unit`                    Retorno sem valor útil, `fun listar(cmd: String)`
                            semelhante a `void`     

  `if / else`               Condição; também pode   `if (m.tipo == "r") ... else ...`
                            produzir um valor       

  `when`                    Seleção entre           `when (partes[0]) { ... }`
                            alternativas            

  `for (x in ...)`          Percorre coleção ou     `for (m in movimentos)`
                            intervalo               

  `while`                   Repete enquanto a       `while (true)`
                            condição for verdadeira 

  `0..999`                  Intervalo inclusivo     `for (data in 1..999)`

  `break`                   Termina o ciclo mais    `?: break`
                            próximo                 

  `return`                  Termina função e pode   `return b`
                            devolver valor          

  `+=`                      Soma e atribui          `b[m.data] += impacto`

  `==`                      Compara igualdade de    `m.tipo == "r"`
                            valores                 

  `===`                     Compara identidade de   `it === alvo`
                            objetos                 

  `in`                      Verifica pertença ou    `indice in encontrados.indices`
                            percorre coleção        

  `?.`                      Chamada segura se o     `utilizadorAtual?.sessaoAtiva = null`
                            recetor não for `null`  

  `?:`                      Operador Elvis:         `readlnOrNull() ?: break`
                            alternativa se o valor  
                            for `null`              

  `!!`                      Afirma que não é nulo;  Evitar quando possível
                            pode lançar erro        

  `.add(x)`                 Adiciona elemento       `movimentos.add(...)`

  `.removeAt(i)`            Remove por índice       `movimentos.removeAt(global)`

  `.filter { ... }`         Filtra elementos por    `movimentos.filter { it.data == data }`
                            condição                

  `.withIndex()`            Itera elementos com     `encontrados.withIndex()`
                            índices                 

  `.indices`                Intervalo dos índices   `encontrados.indices`
                            válidos                 

  `.indexOfFirst { ... }`   Índice do primeiro      `indexOfFirst { it === alvo }`
                            elemento correspondente 
                            ou `-1`                 

  `it`                      Parâmetro implícito de  `{ it.data == data }`
                            uma lambda com um       
                            argumento               

  `{ ... }`                 Bloco ou lambda         `filter { ... }`

  `toInt()`                 Converte para inteiro;  `p[2].toInt()`
                            pode falhar             

  `toLong()`                Converte para `Long`;   `p[1].toLong()`
                            pode falhar             

  `trim()`                  Remove espaços nas      `cmd.trim()`
                            extremidades            

  `split(...)`              Divide string           `.split(Regex("\\s+"))`

  `Regex(...)`              Cria expressão regular  `Regex("\\s+")`

  `println(...)`            Imprime e muda de linha `println(balancos[data])`

  `readlnOrNull()`          Lê linha ou devolve     `readlnOrNull() ?: break`
                            `null` no fim da        
                            entrada                 

  `.also { ... }`           Executa bloco e devolve `Utilizador(...).also { ... }`
                            o objeto original       

  `UUID.randomUUID()`       Gera UUID aleatório     Token de sessão

  `MessageDigest`           API Java para funções   SHA-256
                            de hash                 

  `LocalDateTime.now()`     Data/hora local atual   Registo da sessão

  `objeto.propriedade`      Acede a uma propriedade `utilizador.orcamento`

  `objeto.metodo()`         Chama método            `orcamento.listar(cmd)`

  `[indice]`                Acede a elemento de     `balancos[data]`
                            lista/vetor             
  ---------------------------------------------------------------------------------------------

### Pontos a compreender bem

-   `val` não torna o conteúdo de uma lista imutável. Impede reatribuir
    a referência, mas `MutableList` continua a permitir `add` e
    `removeAt`.
-   `private` esconde detalhes internos.
-   `LongArray(1000)` cria um vetor de 1000 valores `Long`.
-   `it` representa o argumento implícito de uma lambda;
    `{ movimento -> ... }` é uma alternativa explícita.
-   `==` compara valores; `===` verifica se as referências apontam para
    o mesmo objeto.
-   `Regex("\\s+")` recebe a expressão regular `\s+`. A barra invertida
    está duplicada porque a string Kotlin precisa de a escapar.

## 11. Dificuldades e soluções encontradas

### `l` não listava movimentos

O comando era processado sem utilizador autenticado. Solução: iniciar
com `u email password` e inserir movimentos na mesma execução.

### A aplicação não parava depois de `exit`

Era o comportamento previsto: `exit` termina a sessão, não o processo.
Solução: enviar EOF ou usar Stop.

### `calcularBalancos()` parecia não receber os dados

O método acede à propriedade `movimentos` da instância de
`GestorMovimentos`. Calcula todos os balanços e devolve um vetor;
`calcularBalanco(cmd)` seleciona a data pedida.

### Impactos diários versus balanços acumulados

O primeiro ciclo soma os impactos na data exata; o segundo acumula os
valores com o dia anterior.

### Índices locais da listagem

`l data` filtra primeiro os movimentos dessa data e enumera apenas os
resultados. A remoção converte o índice local na ocorrência
correspondente na lista geral.

### Testes da Fase 2 na aplicação da Fase 3

Os exemplos da Fase 2 não incluem login. Acrescenta `u email password`
no início e executa todos os comandos na mesma execução.

## 12. Erros comuns

  -----------------------------------------------------------------------------
  Sintoma                       Causa provável          Solução
  ----------------------------- ----------------------- -----------------------
  `l 0` não imprime nada        Sem sessão ou sem       Iniciar sessão e
                                movimentos nessa data   inserir movimentos

  Programa continua após `exit` Comportamento esperado  EOF ou Stop

  `IndexOutOfBoundsException`   Argumentos ou índice    Validar quantidade de
                                inválidos               argumentos e índice

  `NumberFormatException`       Número inválido         Validar antes de
                                                        `toInt()`/`toLong()`

  Credenciais recusadas         Palavra-passe errada    Confirmar credenciais
                                para email existente    

  Dados desapareceram           Aplicação foi           Dados só existem em
                                reiniciada              memória

  Descrição incompleta          Parser usa `p[5]`       Adaptar parser para
                                                        aceitar espaços

  `gradlew run` falha           Configuração            Executar no IntelliJ ou
                                `application` ausente   configurar classe
                                                        principal
  -----------------------------------------------------------------------------

## 13. Complexidade

Se `M` é o número de ocorrências de movimentos:

  Operação                                       Complexidade aproximada
  ----------------------------- ----------------------------------------
  Inserir uma ocorrência                               `O(1)` amortizado
  Inserir movimento periódico              `O(K)`, K ocorrências geradas
  Listar                                                          `O(M)`
  Remover                                                         `O(M)`
  Calcular balanços                                        `O(M + 1000)`
  Consultar `b`                                            `O(M + 1000)`
  Validar `v`                                              `O(M + 1000)`
  Procurar `w`                    Até `O(M + 1000²)` nesta implementação
  Procurar utilizador no mapa                            `O(1)` esperado

O método `w` testa datas candidatas e percorre datas futuras para cada
uma. É uma implementação simples, mas pode ser otimizada se necessário.

## 14. Melhorias possíveis

1.  Validar o número e o conteúdo dos argumentos antes de os usar.
2.  Permitir descrições com espaços.
3.  Mostrar mensagens para comandos desconhecidos.
4.  Adicionar testes automatizados.
5.  Implementar persistência, se for exigida.
6.  Guardar palavras-passe com hashing apropriado.
7.  Separar parsing e lógica de negócio.
8.  Otimizar `w`.
9.  Confirmar no enunciado a regra para `w` quando o orçamento já é
    inválido.
10. Tratar erros sem encerrar inesperadamente a aplicação.

Estas são melhorias possíveis, não necessariamente requisitos da
entrega.

## 15. Checklist final

### Execução

-   [ ] Projeto abre no IntelliJ.
-   [ ] Gradle sincroniza.
-   [ ] `Main.kt` inicia o sistema.
-   [ ] Comandos são lidos pela consola.
-   [ ] EOF termina o ciclo de leitura.

### Fase 2

-   [ ] Inserir movimentos.
-   [ ] Expandir periodicidade até à data 999.
-   [ ] Listar por data.
-   [ ] Remover uma ocorrência.
-   [ ] Consultar balanço.
-   [ ] Alterar saldo mínimo.
-   [ ] Validar orçamento.
-   [ ] Procurar data possível.

### Fase 3

-   [ ] Criar utilizador.
-   [ ] Autenticar credenciais corretas.
-   [ ] Recusar palavra-passe errada.
-   [ ] Gerar token e guardar sessão.
-   [ ] Terminar sessão com `exit`.
-   [ ] Continuar a aplicação depois de `exit`.
-   [ ] Isolar o orçamento de cada utilizador.

### Compreensão

-   [ ] Explicar `val`, `var`, `data class`.
-   [ ] Explicar listas, mapas e conjuntos mutáveis.
-   [ ] Explicar `LongArray`.
-   [ ] Explicar `Regex`, `trim` e `split`.
-   [ ] Explicar `readlnOrNull() ?: break`.
-   [ ] Explicar `==` versus `===`.
-   [ ] Explicar o cálculo acumulado.
-   [ ] Explicar por que `calcularBalancos()` não recebe a lista como
    parâmetro.
-   [ ] Explicar por que `exit` não encerra a aplicação.

------------------------------------------------------------------------

## Conclusão

O projeto reúne processamento de comandos, estruturas de dados, funções,
classes, cálculo de balanços acumulados e gestão de sessões. O objetivo
é compreender não só como executar os comandos, mas também como os dados
passam entre as classes, como cada utilizador mantém o seu estado e como
os movimentos se transformam em balanços.

**Antes da entrega, compara este README com o enunciado oficial e os
ficheiros finais do projeto.** Se alteraste código depois desta
documentação, atualiza os exemplos e as descrições para refletirem
exatamente a versão entregue.
