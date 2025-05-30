# StarWars Labirinto
Trabalho realizado para POO

Grupo: Manuela Silveira da Silva (00000852571), Anna Beatriz dos Santos Silva (00000851757) e Daniel Silva Costa (00000852075)

Este código Java implementa um jogo de labirinto com temática de Star Wars, onde o objetivo principal é escapar da Estrela da Morte.

Como o Jogo Funciona

Criação do Labirinto:

O jogo gera um labirinto aleatório, ou seja, ele é diferente a cada vez que você joga.
O labirinto é preenchido com paredes (#) e espaços vazios () que formam os caminhos.
Elementos importantes como o jogador (P), a chave de acesso (K) e a saída (s ou F) são colocados aleatoriamente.

O Jogador:

Você controla um personagem Rebelde com atributos de vida, ataque e defesa.
Você pode mover-se usando as teclas W, A, S, D.
Seu objetivo é encontrar o código de acesso (K) e, com ele, alcançar a nave de fuga (F).

Exploração e Interações:

Ao se mover pelo labirinto, você encontrará diversos elementos:
Monstros (S, B, V): Stormtroopers, Boba Fett e até Darth Vader. Ao interagir com eles, você entra em um "combate" abstrato onde sua vida é reduzida e a do monstro também (eles são removidos após a interação).
Armadilhas (T, G): Torres a laser e gás venenoso. O gás venenoso tem um efeito contínuo por alguns turnos.
Tesouros (H, J, c): Holocrons, joias e créditos galácticos. Eles aumentam sua pontuação no jogo e são mais comuns.
Armas (E, L, D): Blasters, sabres de luz e vibroswords. Elas aumentam seu atributo de ataque.
Armaduras (A, C, M): Armadura Rebelde, Mandaloriana, Clone. Elas aumentam seu atributo de defesa.
Após interagir com monstros, armadilhas ou coletar itens, o espaço que eles ocupavam no mapa fica vazio.

Condições de Vitória e Derrota:

Você ganha o jogo ao encontrar o código de acesso e, em seguida, mover-se para a nave de fuga (F).
Você perde o jogo se sua vida chegar a zero.
Estrutura do Código (simplificada)
Dificuldade (Enum): Define os níveis de dificuldade (Padawan, Cavaleiro Jedi, Lorde Sith), que afetam a força dos inimigos e a quantidade de paredes.
Labirinto (Classe):
É o coração do jogo, responsável por gerar o mapa, colocar todos os elementos (jogador, chave, saída, inimigos, itens) e gerenciar as células do labirinto.
Jogador (Classe):
Representa o seu personagem, com seus atributos, inventário e a lógica de movimentação e interação com o que encontrar no labirinto.
Perigo (Interface): Um contrato para tudo que pode causar dano ao jogador (monstros e armadilhas).
Armadilha (Classe Abstrata) e Monstro (Classe Abstrata): Classes base para os perigos, com suas subclasses específicas (LaserTurret, GasVenenoso, Stormtrooper, BobaFett, DarthVader).
Tesouro (Interface): Tudo que o jogador pode coletar no labirinto.
Armadura (Classe Abstrata), Arma (Classe Abstrata) e Preciosidades (Classe Abstrata): Classes base para os tesouros, com suas subclasses (ArmaduraRebelde, SabreLuz, Holocron, etc.).
StarWarsLabirinto (Classe Principal):
Contém o método main, que é o ponto de entrada do jogo.
Gerencia o loop principal do jogo (mostrar mapa, receber comando, processar ação) e a interface do usuário no terminal.
