## Resumo do código

O projeto implementa uma versão do **Jogo da Velha 4x4** em Java, com interface gráfica, permitindo que um jogador humano enfrente uma IA. O jogador humano utiliza o símbolo **X** e a IA utiliza **O**. A regra de vitória consiste em formar **4 símbolos em linha**, seja na horizontal, vertical ou diagonal.

A lógica do jogo foi organizada em classes separadas. A classe `Board` representa o tabuleiro e controla regras como jogadas válidas, verificação de vitória, empate e avaliação heurística. A classe `Move` representa uma jogada possível. A classe `MinimaxAI` implementa o algoritmo **Minimax simples, sem poda**, responsável por escolher a melhor jogada da IA. As classes `SearchResult` e `AIResult` armazenam informações sobre a busca, como pontuação, número de nós avaliados, tempo de execução e melhor jogada encontrada. Já a classe `GameUI` é responsável pela interface visual, exibindo o tabuleiro, permitindo as jogadas do usuário e mostrando indicadores de desempenho da IA.

Durante a execução, a IA registra os seguintes indicadores:
- **Número de nós avaliados**
- **Tempo de execução**
- **Melhor jogada encontrada**

Esses dados ajudam a observar o custo computacional da busca competitiva no jogo.

## Sobre a poda alfa-beta

A poda alfa-beta não altera a decisão final do Minimax porque ela não muda o critério usado para escolher a melhor jogada. O algoritmo continua buscando a melhor decisão possível para a IA e a pior para o adversário, exatamente como no Minimax tradicional.

A diferença é que a poda alfa-beta elimina ramos da árvore de busca que já não podem influenciar o resultado final. Em outras palavras, quando o algoritmo percebe que determinado caminho será pior do que outro já analisado, ele interrompe a exploração desse ramo. Assim, evita cálculos desnecessários.

Por isso, a melhor jogada escolhida no final continua sendo a mesma que seria encontrada pelo Minimax simples. O ganho está no desempenho: a poda alfa-beta reduz a quantidade de nós avaliados e o tempo de execução, tornando a busca mais eficiente.
