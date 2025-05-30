Trabalho realizado para POO

Grupo:
Manuela Silveira da Silva (00000852571)
Anna Beatriz dos Santos Silva (00000851757)
Daniel Silva Costa (00000852075)
Descrição Geral

Este código Java implementa um jogo de labirinto com temática de Star Wars, onde o objetivo principal é escapar da Estrela da Morte.
Como o Jogo Funciona
Criação do Labirinto:

    O jogo gera um labirinto aleatório, diferente a cada partida.

    O labirinto é composto por paredes (#) e caminhos ( ).

    Elementos como o jogador (P), chave (K) e saída (F) são posicionados aleatoriamente.

O Jogador:

    Controla um personagem Rebelde com atributos de vida, ataque e defesa.

    Movimentos são feitos com as teclas W, A, S, D.

    O objetivo é encontrar a chave de acesso (K) e alcançar a nave de fuga (F).

Exploração e Interações:

Durante a exploração, o jogador pode encontrar:

    Monstros (S, B, V): Stormtroopers, Boba Fett e Darth Vader.

    Armadilhas (T, G): Torres de laser e gás venenoso (com efeito contínuo).

    Tesouros (H, J, c): Holocrons, joias e créditos galácticos (aumentam a pontuação).

    Armas (E, L, D): Blasters, sabres de luz e vibroswords (aumentam o ataque).

    Armaduras (A, C, M): Rebelde, Mandaloriana e Clone (aumentam a defesa).

    Após a interação, o espaço ocupado pelo item, armadilha ou monstro se torna vazio.

Condições de Vitória e Derrota:

    Vitória: Obter a chave e alcançar a nave de fuga (F).

    Derrota: Vida do jogador chega a zero.

Estrutura do Código (simplificada):

    Dificuldade (Enum): Define o nível (Padawan, Cavaleiro Jedi, Lorde Sith).

    Labirinto (Classe): Gera o mapa e posiciona os elementos.

    Jogador (Classe): Representa o personagem com atributos e lógica de movimentação.

    Perigo (Interface): Interface para monstros e armadilhas.

    Armadilha e Monstro (Abstratas): Classes base para inimigos.

    Tesouro (Interface): Representa itens coletáveis.

    Armadura, Arma, Preciosidades (Abstratas): Bases para os itens de melhoria.

    StarWarsLabirinto (Classe Principal):

        Contém o main.

        Controla o loop principal do jogo.

        Mostra o mapa, recebe comandos e processa ações.

Easter Eggs

O jogo conta com dois Easter Eggs secretos que podem ser ativados via sequência de teclas durante a jogabilidade:
1. "É uma armadilha!"

    Sequência: W W S S A D A D

    Efeito: Exibe uma mensagem secreta do universo Star Wars:

        "Isso não é a Estrela da Morte... é uma armadilha!"

2. Ataque da Frota Rebelde

    Sequência: f r o t a

    Efeito:

        Todos os monstros do mapa são eliminados.

        O jogador recebe uma mensagem de vitória temática e pontos bônus.

        "A Frota Rebelde chegou a tempo! Todos os inimigos foram eliminados. A vitória é sua, herói da Aliança!"
