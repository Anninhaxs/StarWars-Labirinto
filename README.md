# StarWars-LABIRINTO
> Trabalho realizado para POO

![Status](https://img.shields.io/badge/status-concluido-green)


**Grupo:**
- Manuela Silveira da Silva (00000852571)
- Anna Beatriz dos Santos Silva (00000851757)
- Daniel Silva Costa (00000852075)
  
---
## Descrição Geral

O Jogo StarWars-Labirinto em Java, onde o jogador assume o papel de um prisioneiro rebelde tentando escapar da Estrela da Morte.
O objetivo é navegar pelo labirinto, coletar o código de acesso e chegar à nave TIE Fighter para fugir, enfrentando diversos perigos pelo caminho.

---

### Funcionalidades
> Sistema de Aventureiro (Jogador)
- Personagem personalizável com nome escolhido pelo jogador
- Atributos de vida, ataque e defesa
- Sistema de inventário para armazenar itens coletados
- Progresso registrado durante a exploração

> Sistema de Itens
- Armas: Blaster, Sabre de Luz, Vibrosword - aumentam o ataque
- Armaduras: Rebelde, Mandaloriana, Clone - aumentam a defesa
- Tesouros: Holocrons, Joias, Creditos - concedem pontos extras

> Perigos do Labirinto
- Armadilhas: Torres laser e gás venenoso que causam dano
- Inimigos: Stormtroopers, Boba Fett e Darth Vader para enfrentar
- Efeitos de status: Envenenamento que causa dano contínuo

> Sistema de Labirinto
- Geração procedural de mapas com diferentes layouts
- Diferentes níveis de dificuldade (Padawan, Cavaleiro Jedi, Lorde Sith)
- Verificação de conectividade para garantir jogabilidade
- Easter eggs e segredos escondidos (W W S S A D A D)
- Pontuação baseada em conquistas e itens coletados
--- 

### Conceitos Aplicados
- Coleções: ArrayList para gerenciar caminhos e itens
- Polimorfismo: Hierarquia de classes para diferentes tipos de tesouros e perigos
- Tratamento de exceções: Prevenção de erros comuns de movimentação
- Encapsulamento: Atributos protegidos e métodos bem definidos
- Abstração: Interfaces para tipos de itens e perigos
---

### Estrutura do Projeto
> O código está organizado em:
- Classes principais (Jogador, Labirinto)
- Hierarquia de itens (Armas, Armaduras, Tesouros)
- Tipos de perigos (Armadilhas, Inimigos)
- Sistema de jogo principal
---

### ANEXOS 

- TELA INICIO DO JOGO : 
![Image](https://github.com/user-attachments/assets/23393817-631f-418f-87c0-623458071ee7)

- TELA LABIRINTO E COMANDOS: 
![Image](https://github.com/user-attachments/assets/6d25f8c3-3c86-46eb-888c-03f618adf5b7)

- TELA QUANDO ENCONTRA UM INIMIGO:
![Image](https://github.com/user-attachments/assets/0117e336-fef5-4313-a457-a033ae7e42ad)

- TELA QUANDO ENCONTRA UM EASTER EGG:
![Image](https://github.com/user-attachments/assets/86b09cb4-c823-4161-994c-0384d6e9d050)

- TELA QUANDO O JOGADOR GANHA:
![Image](https://github.com/user-attachments/assets/855eeddf-815a-484d-a2fe-cc5956f9941d)
