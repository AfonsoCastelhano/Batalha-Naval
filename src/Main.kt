import java.io.File

var tabuleiroPalpitesDoComputador = arrayOf(arrayOf(null, null, 'd'), arrayOf('e', 'f', null))
var tabuleiroComputador = arrayOf(arrayOf('c', null, 'd'), arrayOf('e', 'f', null))
var tabuleiroHumano = arrayOf(arrayOf('c', null, 'd'), arrayOf('e', 'f', null))
var tabuleiroPalpitesDoHumano = arrayOf(arrayOf('c', null, 'd'), arrayOf('e', 'f', null))
var numColunas = 0
var numLinhas = 0

fun criaLegendaHorizontal(numColunas: Int): String {
    if (numColunas <= 0) {
        return ""
    }

    val letra = 'A'
    var coluna = 1
    var resultado = ""

    while (coluna <= numColunas) {
        resultado += "${letra + coluna - 1}"
        if ( coluna != numColunas) {
            resultado += " | "
        }
        coluna++
    }

    return resultado
}

fun tamanhoTabuleiroValido(numLinhas: Int,numColunas: Int): Boolean {
    if (numLinhas == 5) {
        if (numColunas == 5){
            return true
        }
    } else if (numLinhas == 7) {
        if(numColunas == 7) {
            return true
        }
    } else if (numLinhas == 8) {
        if (numColunas == 8) {
            return true
        }
    } else if (numLinhas == 10) {
        if(numColunas == 10) {
            return true
        }
    } else if (numLinhas == 4) {
        if (numColunas == 4) {
            return true
        }
    }
    return false
}

fun processaCoordenadas(coordenadas: String, numLinhas: Int, numColunas: Int): Pair<Int, Int>? {
    if (coordenadas.length < 3 || coordenadas.length > 4 || coordenadas[1] != ',' && coordenadas[2] != ','
            || !coordenadas[0].isDigit() || !coordenadas.last().isLetter()) {
        return null
    }

    val numero = coordenadas.substringBefore(',').toInt()

    val letraChar = coordenadas.last().toUpperCase()
    val letraValor = letraChar.toInt() - 'A'.toInt() + 1

    if (numero > numColunas || letraValor > numLinhas) {
        return null
    } else {
        return Pair(numero, letraValor )
    }
}

fun calculaNumNavios(numLinhas: Int, numColunas: Int): Array<Int> {
    return when {
        numLinhas == 4 && numColunas == 4 -> arrayOf(2, 0, 0, 0)
        numLinhas == 5 && numColunas == 5 -> arrayOf(1, 1, 1, 0)
        numLinhas == 7 && numColunas == 7 -> arrayOf(2, 1, 1, 1)
        numLinhas == 8 && numColunas == 8 -> arrayOf(2, 2, 1, 1)
        numLinhas == 10 && numColunas == 10 -> arrayOf(3, 2, 1, 1)
        else -> arrayOf(0, 0, 0, 0)
    }
}

fun criaTabuleiroVazio(numLinhas: Int,numColunas: Int): Array<Array<Char?>> {
    val tabuleiro = Array(numLinhas) { arrayOfNulls<Char?>(numColunas)}
    return tabuleiro
}

fun coordenadaContida(tabuleiroVazio: Array<Array<Char?>>, linha: Int,coluna: Int): Boolean {
    if (linha <= tabuleiroVazio.size && linha > 0 && coluna <= tabuleiroVazio[0].size && linha > 0 ) {
        return true
    } else {
        return false
    }
}

fun limparCoordenadasVazias(coordenadas: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    var tamanhoNovoArray = 0
    for (par in coordenadas) {
        if (par != Pair(0, 0)) {
            tamanhoNovoArray++
        }
    }
    val coordenadasNaoVazias = Array(tamanhoNovoArray) { Pair(0, 0) }
    var indiceNovoArray = 0
    for (par in coordenadas) {
        if (par != Pair(0, 0)) {
            coordenadasNaoVazias[indiceNovoArray] = par
            indiceNovoArray++
        }
    }

    return coordenadasNaoVazias
}

fun juntarCoordenadas(primeiroArray: Array<Pair<Int, Int>>, segundoArray: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    val tamanhoNovoArray = primeiroArray.size + segundoArray.size
    val arrayJunto = Array<Pair<Int, Int>>(tamanhoNovoArray) { Pair(0, 0) }
    for (i in primeiroArray.indices) {
        arrayJunto[i] = primeiroArray[i]
    }
    for (j in segundoArray.indices) {
        arrayJunto[j + primeiroArray.size] = segundoArray[j]
    }

    return arrayJunto
}

fun gerarCoordenadasNavio(tabuleiro: Array<Array<Char?>>, linha: Int,coluna: Int,orientacao: String, dimensao: Int): Array<Pair<Int, Int>> {
    var count = 0
    var linhaposicao = linha
    var colunaposicao = coluna
    var coordsNavio : Array<Pair<Int, Int>>
    coordsNavio = Array(dimensao) { Pair(0, 0) }
    coordsNavio = Array(dimensao) { Pair(0, 0) }
    do {
        if (orientacao == "S") {
            if (tabuleiro.size < (linha + dimensao)) {
                return emptyArray()
            } else {
                coordsNavio[count] = Pair(linhaposicao, coluna)
                count++
                linhaposicao++
            }
        } else if (orientacao == "N") {
            if ( linha - dimensao < 0) {
                return emptyArray()
            } else {
                coordsNavio[count] = Pair(linhaposicao, coluna)
                count++
                linhaposicao--
            }
        } else if (orientacao == "E") {
            if (tabuleiro.size < (coluna + dimensao)) {
                return emptyArray()
            } else {
                coordsNavio[count] = Pair(linha, colunaposicao)
                count++
                colunaposicao++
            }
        } else if (orientacao == "O") {
            if (coluna - dimensao < 0) {
                return emptyArray()
            } else {
                coordsNavio[count] = Pair(linha, colunaposicao)
                count++
                colunaposicao--
            }
        }
    } while (count < dimensao)
    return coordsNavio
}

fun gerarCoordenadasFronteira(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Array<Pair<Int, Int>> {
    val coordenadas = mutableListOf<Pair<Int, Int>>()

    tabuleiro[linha - 1][coluna - 1] = '1'

    for (i in -1..1) {
        for (j in -1..1) {
            val l = linha + i
            val c = coluna + j
            if( linha - 1 > 0 && coluna - 1 > 0) {
                coordenadas.add(Pair(linha - 1, coluna - 1))
            }
            if( linha - 1 > 0 && coluna  > 0) {
                coordenadas.add(Pair(linha - 1, coluna ))
            }
            if( linha - 1 > 0 && coluna + 1 > 0 && coluna + 1 <= tabuleiro.size) {
                coordenadas.add(Pair(linha - 1, coluna + 1))
            }
            if( linha  > 0 && coluna - 1 > 0) {
                coordenadas.add(Pair(linha, coluna - 1))
            }
            if( linha  > 0 && coluna + 1 > 0 && coluna + 1 <= tabuleiro.size) {
                coordenadas.add(Pair(linha, coluna + 1))
            }
            if( linha + 1 <= tabuleiro.size && coluna - 1 > 0) {
                coordenadas.add(Pair(linha + 1, coluna - 1))
            }
            if( linha + 1 <= tabuleiro.size && coluna > 0) {
                coordenadas.add(Pair(linha + 1, coluna ))
            }
            if( linha + 1 <= tabuleiro.size && coluna + 1 > 0 && coluna + 1 <= tabuleiro.size) {
                coordenadas.add(Pair(linha + 1, coluna + 1))
            }
        }
    }

    return coordenadas.toTypedArray()
}

fun estaLivre(tabuleiro: Array<Array<Char?>>, coordenadas: Array<Pair<Int, Int>>): Boolean {
    for ((linha, coluna) in coordenadas) {
        val linhaNoTabuleiro = linha - 1
        val colunaNoTabuleiro = coluna - 1
        if (linhaNoTabuleiro >= 0 && linhaNoTabuleiro < tabuleiro.size &&
                colunaNoTabuleiro >= 0 && colunaNoTabuleiro < tabuleiro[linhaNoTabuleiro].size
        ) {
            if (tabuleiro[linhaNoTabuleiro][colunaNoTabuleiro] != null) {
                return false
            }
        } else {
            return false
        }
    }
    return true
}

fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, dimensao: Int): Boolean {
    val numeroLinhas = tabuleiro.size
    val numeroColunas = if (numeroLinhas > 0) tabuleiro[0].size else 0

    if (linha < 1 || coluna < 1 || linha > numeroLinhas || coluna > numeroColunas) {
        return false
    }

    val charEscolhido = tabuleiro[linha - 1][coluna - 1]

    if (charEscolhido != null) {
        return false
    }

    if (dimensao != 1) {
        return false
    }

    tabuleiro[linha - 1][coluna - 1] = '1'

    return true
}

fun insereNavio(tabuleiro: Array<Array<Char?>>, linha: Int,coluna: Int, orientacao: String, dimensao: Int): Boolean {
    if (linha < 0 || linha >= tabuleiro.size || coluna < 0 || coluna >= tabuleiro[0].size) {
        return false
    }
    when (orientacao.toUpperCase()) {
        "E" -> {
            if (coluna + dimensao > tabuleiro[0].size) {
                return false
            }
        }
        "S" -> {
            if (linha + dimensao > tabuleiro.size) {
                return false
            }
        }
        "O" -> {
            if (coluna - dimensao + 1 < 0) {
                return false
            }
        }
        "N" -> {
            if (linha - dimensao + 1 < 0) {
                return false
            }
        }
        else -> return false
    }
    for (i in 0 until dimensao) {
        when (orientacao.toUpperCase()) {
            "E" -> {
                if (tabuleiro[linha][coluna + i] != null) {
                    return false
                }
            }
            "S" -> {
                if (tabuleiro[linha + i][coluna] != null) {
                    return false
                }
            }
            "O" -> {
                if (tabuleiro[linha][coluna - i] != null) {
                    return false
                }
            }
            "N" -> {
                if (tabuleiro[linha - i][coluna] != null) {
                    return false
                }
            }
        }
    }
    for (i in 0 until dimensao) {
        when (orientacao.toUpperCase()) {
            "E" -> tabuleiro[linha][coluna + i] = dimensao.toChar()
            "S" -> tabuleiro[linha + i][coluna] = dimensao.toChar()
            "O" -> tabuleiro[linha][coluna - i] = dimensao.toChar()
            "N" -> tabuleiro[linha - i][coluna] = dimensao.toChar()
        }
    }

    return true
}

fun preencheTabuleiroComputador(tabuleiroComputador: Array<Array<Char?>>, navios: Array<Int>) {
    for (i in 0 until navios[2] * 3 step 3) {
        var tentativas = 0

        do {
            val posicaoLinha = (Math.random() * (tabuleiroComputador.size - 2)).toInt()
            val posicaoColuna = (Math.random() * tabuleiroComputador[0].size).toInt()

            var posicaoValida = true

            for (j in 0 until 3) {
                val linha = posicaoLinha + j

                if (linha in 0 until tabuleiroComputador.size &&
                        (tabuleiroComputador[linha][posicaoColuna] == '1' || tabuleiroComputador[linha][posicaoColuna] == '2' || tabuleiroComputador[linha][posicaoColuna] == '3' || tabuleiroComputador[linha][posicaoColuna] == '4')
                ) {
                    posicaoValida = false
                    break
                }
            }

            if (posicaoValida) {
                for (j in 0 until 3) {
                    val linha = posicaoLinha + j
                    tabuleiroComputador[linha][posicaoColuna] = '3'
                }
            }

            tentativas++

        } while (!posicaoValida && tentativas < 100)
    }

    for (i in 0 until navios[1] * 2 step 2) {
        var tentativas = 0

        do {
            val posicaoLinha = (Math.random() * (tabuleiroComputador.size - 1)).toInt()
            val posicaoColuna = (Math.random() * tabuleiroComputador[0].size).toInt()

            var posicaoValida = true

            for (j in 0 until 2) {
                val linha = posicaoLinha + j

                if (linha in 0 until tabuleiroComputador.size &&
                        (tabuleiroComputador[linha][posicaoColuna] == '1' || tabuleiroComputador[linha][posicaoColuna] == '2' || tabuleiroComputador[linha][posicaoColuna] == '3' || tabuleiroComputador[linha][posicaoColuna] == '4')
                ) {
                    posicaoValida = false
                    break
                }
            }

            if (posicaoValida) {
                for (j in 0 until 2) {
                    val linha = posicaoLinha + j
                    tabuleiroComputador[linha][posicaoColuna] = '2'
                }
            }

            tentativas++

        } while (!posicaoValida && tentativas < 100)
    }

    for (i in 0 until navios[3] * 4 step 4) {
        var tentativas = 0

        do {
            val posicaoLinha = if (navios[2] == 0) {
                (Math.random() * (tabuleiroComputador.size - 3)).toInt()
            } else {
                (Math.random() * (tabuleiroComputador.size - 2)).toInt()
            }

            val posicaoColuna = (Math.random() * tabuleiroComputador[0].size).toInt()

            var posicaoValida = true

            for (j in 0 until 4) {
                val linha = posicaoLinha + j

                if (linha in 0 until tabuleiroComputador.size &&
                        (tabuleiroComputador[linha][posicaoColuna] == '1' || tabuleiroComputador[linha][posicaoColuna] == '2' || tabuleiroComputador[linha][posicaoColuna] == '3' || tabuleiroComputador[linha][posicaoColuna] == '4')
                ) {
                    posicaoValida = false
                    break
                }
            }

            if (posicaoValida) {
                for (j in 0 until 4) {
                    val linha = posicaoLinha + j
                    tabuleiroComputador[linha][posicaoColuna] = '4'
                }
            }

            tentativas++

        } while (!posicaoValida && tentativas < 100)
    }

    for (i in 0 until navios[0]) {
        var tentativas = 0

        do {
            val posicaoLinha = (Math.random() * tabuleiroComputador.size).toInt()
            val posicaoColuna = (Math.random() * tabuleiroComputador[0].size).toInt()

            var posicaoValida = true

            for (j in -1..1) {
                for (k in -1..1) {
                    val linha = posicaoLinha + j
                    val coluna = posicaoColuna + k

                    if (linha in 0 until tabuleiroComputador.size && coluna in 0 until tabuleiroComputador[0].size &&
                            (tabuleiroComputador[linha][coluna] == '1' || tabuleiroComputador[linha][coluna] == '2' || tabuleiroComputador[linha][coluna] == '3' || tabuleiroComputador[linha][coluna] == '4')
                    ) {
                        posicaoValida = false
                        break
                    }
                }
                if (!posicaoValida) {
                    break
                }
            }

            if (posicaoValida) {
                tabuleiroComputador[posicaoLinha][posicaoColuna] = '1'
            }

            tentativas++

        } while (!posicaoValida && tentativas < 100)
    }
}

fun navioCompleto(tabuleiroPalpites: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    if (linha < 1 || linha > tabuleiroPalpites.size || coluna < 1 || coluna > tabuleiroPalpites[0].size) {
        return false
    }
    if(tabuleiroPalpites[linha-1][coluna-1] == '1') {
        return true
    } else if (tabuleiroPalpites[linha-1][coluna-1] == '2') {
        if(tabuleiroPalpites[linha-2][coluna-1] == '2' || tabuleiroPalpites[linha-1][coluna-2] == '2' ||
                tabuleiroPalpites[linha-1][coluna+1] == '2' || tabuleiroPalpites[linha][coluna-1] == '2' ) {
            return true
        } else {
            return false
        }
    } else if(tabuleiroPalpites[linha-1][coluna-1] == '3') {
        if(tabuleiroPalpites[linha-2][coluna-1] == '3') {
            if(tabuleiroPalpites[linha-3][coluna-1] == '3') {
                return true
            } else {
                return false
            }
        } else if(tabuleiroPalpites[linha-1][coluna-2] == '3') {
            if(tabuleiroPalpites[linha-1][coluna-3] == '3') {
                return true
            } else {
                return false
            }
        } else if(tabuleiroPalpites[linha-1][coluna+1] == '3') {
            if(tabuleiroPalpites[linha-1][coluna+2] == '3') {
                return true
            } else {
                return false
            }
        } else if(tabuleiroPalpites[linha][coluna-1] == '3' ) {
            if(tabuleiroPalpites[linha+1][coluna-1] == '2' ) {
                return true
            } else {
                return false
            }
        } else {
            return false
        }
    } else {
        return false
    }
}

fun obtemMapa(tabuleiroReal: Array<Array<Char?>>, realOuPalpites: Boolean): Array<String> {
    val tamanho = tabuleiroReal.size
    val letras = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J')
    val resultado = mutableListOf("| " + letras.subList(0, tamanho).joinToString(" | ") + " |")

    for (i in 0 until tamanho) {
        val linha = mutableListOf("|")

        for (j in 0 until tamanho) {
            val elemento = if (realOuPalpites) {
                if (tabuleiroReal[i][j] == 'X') 'X' else tabuleiroReal[i][j]?.toString() ?: "~"
            } else {
                if (tabuleiroReal[i][j] == 'X') 'X' else if (tabuleiroReal[i][j] == '1') '1' else "?"
            }

            linha.add("$elemento |")
        }

        linha.add("${i + 1}")
        resultado.add(linha.joinToString(" "))
    }

    return resultado.toTypedArray()
}

fun lancarTiro(tabuleiroRealComputador: Array<Array<Char?>>, tabuleiroPalpitesHumano: Array<Array<Char?>>, coordenadasTiro: Pair<Int, Int>): String {
    val (linha, coluna) = coordenadasTiro

    if (linha < 1 || linha > tabuleiroRealComputador.size || coluna < 1 || coluna > tabuleiroRealComputador[0].size) {
        return "Coordenadas inválidas."
    }

    val elemento = tabuleiroRealComputador[linha - 1][coluna - 1]

    if (elemento == null) {
        tabuleiroPalpitesHumano[linha -1][coluna -1] = 'X'
        return "Agua."
    }

    val tipoNavio = when (elemento) {
        1.toChar() -> "submarino"
        2.toChar() -> "contra-torpedeiro"
        3.toChar() -> "navio-tanque"
        4.toChar() -> "porta-aviões"
        else -> "submarino"
    }
    tabuleiroPalpitesHumano[linha - 1][coluna - 1] = elemento
    return "Tiro num $tipoNavio."
}

fun geraTiroComputador(tabuleiroPalpitesComputador: Array<Array<Char?>>): Pair<Int, Int> {
    val linhas = tabuleiroPalpitesComputador.size
    val colunas = tabuleiroPalpitesComputador[0].size

    var linha: Int
    var coluna: Int
    linha = (Math.random() * linhas).toInt() + 1
    coluna = (Math.random() * colunas).toInt() + 1
    while (tabuleiroPalpitesComputador[linha - 1][coluna - 1] != null) {
        linha = (Math.random() * linhas).toInt() + 1
        coluna = (Math.random() * colunas).toInt() + 1
    }

    return Pair(linha, coluna)
}

fun contarNaviosDeDimensao(tabuleiroPalpites: Array<Array<Char?>>, dimensao: Int): Int {
    var contador = 0

    if (dimensao == 1) {
        for (linha in tabuleiroPalpites) {
            for (navio in linha) {
                if (navio == '1') {
                    contador++
                }
            }
        }
    } else if (dimensao == 2) {
        for (i in 0 until tabuleiroPalpites.size) {
            for (j in 0 until tabuleiroPalpites[0].size) {
                if (tabuleiroPalpites[i][j] == '2') {
                    if ((j - 1 >= 0 && tabuleiroPalpites[i][j - 1] == '2') ||
                            (j + 1 < tabuleiroPalpites[0].size && tabuleiroPalpites[i][j + 1] == '2') ||
                            (i - 1 >= 0 && tabuleiroPalpites[i - 1][j] == '2') ||
                            (i + 1 < tabuleiroPalpites.size && tabuleiroPalpites[i + 1][j] == '2') ||
                            (i - 1 >= 0 && j - 1 >= 0 && tabuleiroPalpites[i - 1][j - 1] == '2') ||
                            (i - 1 >= 0 && j + 1 < tabuleiroPalpites[0].size && tabuleiroPalpites[i - 1][j + 1] == '2') ||
                            (i + 1 < tabuleiroPalpites.size && j - 1 >= 0 && tabuleiroPalpites[i + 1][j - 1] == '2') ||
                            (i + 1 < tabuleiroPalpites.size && j + 1 < tabuleiroPalpites[0].size && tabuleiroPalpites[i + 1][j + 1] == '2')
                    ) {
                        contador++
                    }
                }
            }
        }
    }

    return contador
}

fun venceu(tabuleiroPalpites: Array<Array<Char?>>): Boolean {
    val totalArrays = tabuleiroPalpites.size

    when (totalArrays) {
        4 -> {
            val totalNumeros1 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '1' } }
            return totalNumeros1 == 2
        }
        5 -> {
            val totalNumeros1 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '1' } }
            val totalNumeros2 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '2' } }
            val totalNumeros3 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '3' } }

            return totalNumeros1 == 1 && totalNumeros2 == 2 && totalNumeros3 == 3
        }
        7 -> {
            val totalNumeros1 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '1' } }
            val totalNumeros2 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '2' } }
            val totalNumeros3 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '3' } }
            val totalNumeros4 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '4' } }

            return totalNumeros1 == 2 && totalNumeros2 == 2 && totalNumeros3 == 3 && totalNumeros4 == 4
        }
        8 -> {
            val totalNumeros1 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '1' } }
            val totalNumeros2 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '2' } }
            val totalNumeros3 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '3' } }
            val totalNumeros4 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '4' } }

            return totalNumeros1 == 2 && totalNumeros2 == 4 && totalNumeros3 == 3 && totalNumeros4 == 4
        }
        10 -> {
            val totalNumeros1 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '1' } }
            val totalNumeros2 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '2' } }
            val totalNumeros3 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '3' } }
            val totalNumeros4 = tabuleiroPalpites.sumBy { linha -> linha.count { it == '4' } }

            return totalNumeros1 == 3 && totalNumeros2 == 4 && totalNumeros3 == 3 && totalNumeros4 == 4
        }
        else -> return false
    }
}

fun lerJogo(nomeDoFicheiro: String, tipoDeTabuleiro: Int): List<String> {
    val file = File(nomeDoFicheiro)
    return file.readLines()
}

fun gravarJogo(nomeDoFicheiro: String, tabuleiroRealHumano: Array<Array<Char?>>, tabuleiroPalpitesHumano: Array<Array<Char?>>,
               tabuleiroRealComputador: Array<Array<Char?>>, tabuleiroPalpitesComputador: Array<Array<Char?>>) {
    if (!nomeDoFicheiro.endsWith(".txt")) {
        throw IllegalArgumentException("O nome do ficheiro deve terminar com \".txt\".")
    }

    val arquivo = File(nomeDoFicheiro)

    arquivo.printWriter().use { writer ->
        writer.println("Tabuleiro Real Humano:")
        for (linha in tabuleiroRealHumano) {
            for (celula in linha) {
                val valor = celula ?: ' '
                writer.print("$valor ")
            }
            writer.println()
        }

        writer.println("\nTabuleiro Palpites Humano:")
        for (linha in tabuleiroPalpitesHumano) {
            for (celula in linha) {
                val valor = celula ?: ' '
                writer.print("$valor ")
            }
            writer.println()
        }

        writer.println("\nTabuleiro Real Computador:")
        for (linha in tabuleiroRealComputador) {
            for (celula in linha) {
                val valor = celula ?: ' '
                writer.print("$valor ")
            }
            writer.println()
        }

        writer.println("\nTabuleiro Palpites Computador:")
        for (linha in tabuleiroPalpitesComputador) {
            for (celula in linha) {
                val valor = celula ?: ' '
                writer.print("$valor ")
            }
            writer.println()
        }
    }

    val tamanhoTabuleiro = tabuleiroRealHumano.size
    println("Tabuleiro ${tamanhoTabuleiro}x$tamanhoTabuleiro gravado com sucesso.")
}

fun main() {
    var opcao : Int
    do {
        println()
        println("> > Batalha Naval < <\n")
        println("1 - Definir Tabuleiro e Navios")
        println("2 - Jogar")
        println("3 - Gravar")
        println("4 - Ler")
        println("0 - Sair")
        println("")
        val userInput = readLine()

        opcao = if (userInput != null && userInput.toIntOrNull() != null) {
            userInput.toInt()
        } else {
            println("!!! Opcao invalida, tente novamente")
            -1
        }
        if (opcao < -1 || opcao > 4){
            println("!!! Opcao invalida, tente novamente")
            -1
        }
        if (opcao == 2 || opcao == 3 || opcao == 4) {
            println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
        } else if (opcao == 1) {
            println()
            println("> > Batalha Naval < <\n")
            println("Defina o tamanho do tabuleiro:\nQuantas linhas?")
            numLinhas = readLine()?.toInt() ?: 0
            println("Quantas colunas?")
            numColunas = readLine()?.toInt() ?: 0

            var tabuleiroPalpitesDoComputador = Array(numLinhas) { arrayOfNulls<Char?>(numColunas) }
            var tabuleiroComputador = Array(numLinhas) { arrayOfNulls<Char?>(numColunas) }
            var tabuleiroHumano = Array(numLinhas) { arrayOfNulls<Char?>(numColunas) }
            var tabuleiroPalpitesDoHumano = Array(numLinhas) { arrayOfNulls<Char?>(numColunas) }

            var mapaRealHumano = obtemMapa(tabuleiroHumano, true)
            for (linha in mapaRealHumano) {
                println(linha)
            }
            var navio1 = "1,A"
            var navio1Num = navio1.split(",")[0].toInt()
            var letraNavio1 = navio1.split(",")[1].trim()
            var letrasValidas = arrayOf("A", "B", "C", "D")
            var letraNavio1Coords = letrasValidas.indexOf(letraNavio1) + 1
            do {
                println("Insira as coordenadas de um submarino:\nCoordenadas? (ex: 6,G)")
                 navio1 = readLine().toString()
                 navio1Num = navio1.split(",")[0].toInt()
                 letraNavio1 = navio1.split(",")[1].trim()
                 letrasValidas = arrayOf("A", "B", "C", "D")
                 letraNavio1Coords = letrasValidas.indexOf(letraNavio1) + 1
                if ( navio1Num < 1 || navio1Num > 4 || letraNavio1 !in letrasValidas) {
                    println("!!! Posicionamente invalido, tente novamente")
                }
            } while( navio1Num < 1 || navio1Num > 4 || letraNavio1 !in letrasValidas)

            if (navio1Num in 1..numLinhas) {
                insereNavioSimples(tabuleiroHumano, navio1Num, letraNavio1Coords, 1)
            }

            mapaRealHumano = obtemMapa(tabuleiroHumano, true)
            for (linha in mapaRealHumano) {
                println(linha)
            }
            var navio2 = "1,A"
            var navio2Num = navio2.split(",")[0].toInt()
            var letraNavio2 = navio2.split(",")[1].trim()
            var letraNavio2Coords = letrasValidas.indexOf(letraNavio2) + 1
            do {
                println("Insira as coordenadas de um submarino:\nCoordenadas? (ex: 6,G)")
                 navio2 = readLine().toString()
                 navio2Num = navio2.split(",")[0].toInt()
                 letraNavio2 = navio2.split(",")[1].trim()
                 letraNavio2Coords = letrasValidas.indexOf(letraNavio2) + 1
                if ( navio2Num !in 1..4 || letraNavio2 !in letrasValidas || navio2 == navio1) {
                    println("!!! Posicionamente invalido, tente novamente")
                }
                if(navio2Num == navio1Num && letraNavio2Coords == letraNavio1Coords - 1 || navio2Num == navio1Num && letraNavio2Coords == letraNavio1Coords + 1){
                    println("!!! Posicionamente invalido, tente novamente")
                }
                if( navio2Num == navio1Num -1 && letraNavio2Coords == letraNavio1Coords){
                    println("!!! Posicionamente invalido, tente novamente")
                }
                if( navio2Num == navio1Num -1 && letraNavio2Coords == letraNavio1Coords -1){
                    println("!!! Posicionamente invalido, tente novamente")
                }
                if( navio2Num == navio1Num -1 && letraNavio2Coords == letraNavio1Coords +1){
                    println("!!! Posicionamente invalido, tente novamente")
                }
                if( navio2Num == navio1Num + 1 && letraNavio2Coords == letraNavio1Coords) {
                    println("!!! Posicionamente invalido, tente novamente")
                }
                if( navio2Num == navio1Num + 1 && letraNavio2Coords == letraNavio1Coords -1) {
                    println("!!! Posicionamente invalido, tente novamente")
                }
                if( navio2Num == navio1Num + 1 && letraNavio2Coords == letraNavio1Coords +1) {
                    println("!!! Posicionamente invalido, tente novamente")
                }
            } while ( navio2Num !in 1..4 || letraNavio2 !in letrasValidas || navio2 == navio1 ||
                    navio2Num == navio1Num + 1 && letraNavio2Coords == letraNavio1Coords +1 ||
                    navio2Num == navio1Num + 1 && letraNavio2Coords == letraNavio1Coords -1 ||
                    navio2Num == navio1Num + 1 && letraNavio2Coords == letraNavio1Coords ||
                    navio2Num == navio1Num -1 && letraNavio2Coords == letraNavio1Coords +1 ||
                    navio2Num == navio1Num -1 && letraNavio2Coords == letraNavio1Coords -1 ||
                    navio2Num == navio1Num -1 && letraNavio2Coords == letraNavio1Coords ||
                    navio2Num == navio1Num && letraNavio2Coords == letraNavio1Coords - 1 ||
                    navio2Num == navio1Num && letraNavio2Coords == letraNavio1Coords + 1)

            if (navio2Num in 1..numLinhas) {
                insereNavioSimples(tabuleiroHumano, navio2Num, letraNavio2Coords, 1)
            }

            mapaRealHumano = obtemMapa(tabuleiroHumano, true)
            for (linha in mapaRealHumano) {
                println(linha)
            }
            var mapaRealComputador : Array<String>
            var tabuleiroPC = preencheTabuleiroComputador(tabuleiroComputador,arrayOf(2,0,0,0))
            println("Pretende ver o mapa gerado para o Computador? (S/N)")
            var verMapaPC = readln().toString()
            if (verMapaPC == "S") {
                mapaRealComputador = obtemMapa(tabuleiroComputador, true)
                for (linha in mapaRealComputador) {
                    println(linha)
                }
            }

            println()
            println("> > Batalha Naval < <\n")
            println("1 - Definir Tabuleiro e Navios")
            println("2 - Jogar")
            println("3 - Gravar")
            println("4 - Ler")
            println("0 - Sair")
            println("")
            var opcao = readLine()?.toInt() ?: 0
            var mapaPalpitesHumano = obtemMapa(tabuleiroPalpitesDoHumano,false)
            var humanoVenceu = venceu(tabuleiroPalpitesDoHumano)
            var computadorVenceu = venceu(tabuleiroPalpitesDoComputador)
            var tiroHumano = "1,A"
            var tiroHumanoNum = tiroHumano.split(",")[0].toInt()
            var letraTiroHumano =tiroHumano.split(",")[1].trim()
            var letraTiroHumanoCoords = letrasValidas.indexOf(letraTiroHumano) + 1
            var parTiro : Pair<Int,Int>
            var TiroComputador : Pair<Int, Int>
            if (opcao == 2) {
                do {
                    mapaPalpitesHumano = obtemMapa(tabuleiroPalpitesDoHumano,false)
                    for (linha in mapaPalpitesHumano) {
                        println(linha)
                    }
                    println("Indique a posicao que pretende atingir")
                    println("Coordenadas? (ex: 6,G)")
                     tiroHumano = readln().toString()
                    tiroHumanoNum = tiroHumano.split(",")[0].toInt()
                    letraTiroHumano = tiroHumano.split(",")[1].trim()
                    letraTiroHumanoCoords = letrasValidas.indexOf(letraTiroHumano) + 1
                    parTiro = Pair(tiroHumanoNum,letraTiroHumanoCoords)
                    var posicaoTiroHumano = lancarTiro(tabuleiroComputador,tabuleiroPalpitesDoHumano,parTiro)
                    println(">>> Humano >>>$posicaoTiroHumano")
                    humanoVenceu = venceu(tabuleiroPalpitesDoHumano)
                    TiroComputador = geraTiroComputador(tabuleiroPalpitesDoComputador)
                    var posicaoTiroComputador = lancarTiro(tabuleiroHumano,tabuleiroPalpitesDoComputador,TiroComputador)
                    println("Computador lancou tiro para a posicao $TiroComputador")
                    println(">>> Computador >>>$posicaoTiroComputador")
                    computadorVenceu = venceu(tabuleiroPalpitesDoComputador)
                } while (humanoVenceu == false && computadorVenceu == false)
            }
        } else {
        }
    } while (opcao != 1 && opcao != 0)
}