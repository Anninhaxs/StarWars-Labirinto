import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

enum Dificuldade {
    PADAWAN,
    CAVALEIRO_JEDI,
    LORDE_SITH
}

interface Perigo {
    void interagir(Jogador j);
}

interface Tesouro {
    void aplicarEfeito(Jogador j);
}

public class StarWarsLabirinto {
    private static final int LARGURA_PADRAO = 25;
    private static final int ALTURA_PADRAO = 15;
    private static final int VIDA_EASTER_EGG = 50;
    private static final int PONTOS_EASTER_EGG_FROTA = 100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("╔════════════════════════════════════════════╗");
            System.out.println("║    STAR WARS: FUGA DA ESTRELA DA MORTE     ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.println("\nÉ o ano 0 ABY (Após a Batalha de Yavin).");
            System.out.println("Você é um herói da Aliança Rebelde capturado");
            System.out.println("a bordo da Estrela da Morte II. Enquanto a");
            System.out.println("frota Rebelde ataca a estação espacial, você");
            System.out.println("deve escapar dos blocos de detenção e chegar");
            System.out.println("ao hangar para roubar uma nave.");
            System.out.println("\nSeu objetivo é encontrar o código de acesso");
            System.out.println("e chegar à nave TIE Fighter no hangar.");

            System.out.print("\nDigite o nome do seu personagem: ");
            String nome = scanner.nextLine();

            System.out.println("\nEscolha sua dificuldade:");
            System.out.println("1 - Padawan (Fácil)");
            System.out.println("2 - Cavaleiro Jedi (Médio)");
            System.out.println("3 - Lorde Sith (Difícil)");
            System.out.print("Sua escolha: ");

            int escolhaDificuldade;
            try {
                escolhaDificuldade = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida para dificuldade. Usando dificuldade Padawan.");
                escolhaDificuldade = 1;
                scanner.nextLine();
            }

            Dificuldade dificuldade;
            switch (escolhaDificuldade) {
                case 1: dificuldade = Dificuldade.PADAWAN; break;
                case 2: dificuldade = Dificuldade.CAVALEIRO_JEDI; break;
                case 3: dificuldade = Dificuldade.LORDE_SITH; break;
                default:
                    System.out.println("Escolha inválida. Usando dificuldade Padawan.");
                    dificuldade = Dificuldade.PADAWAN;
            }

            Labirinto labirinto;
            int[] posJogador = null;

            try {
                labirinto = new Labirinto(LARGURA_PADRAO, ALTURA_PADRAO, dificuldade);
                posJogador = labirinto.encontrarJogador();

                if (posJogador == null) {
                    System.err.println("Erro: Não foi possível encontrar a posição inicial do jogador no labirinto.");
                    return;
                }
            } catch (Exception e) {
                System.err.println("Erro fatal ao criar o labirinto: " + e.getMessage());
                return;
            }

            Jogador jogador = new Jogador(posJogador[0], posJogador[1], nome);

            System.out.println("\n╔════════════════════════════╗");
            System.out.println("║ INÍCIO DO JOGO!");
            System.out.println("╠════════════════════════════╣");
            System.out.println("║ Jogador: " + jogador.getNome());
            System.out.println("║ Dificuldade: " + dificuldade);
            System.out.println("║ Controles: W (cima), A (esquerda)");
            System.out.println("║ S (baixo), D (direita)");
            System.out.println("║ I (inventário)");
            System.out.println("║ Q (sair)");
            System.out.println("╚════════════════════════════╝");

            boolean jogoAtivo = true;

            String vidaExtraEasterEgg = "wwssadad";
            String inimigosDerrotadosEasterEgg = "frota";
            StringBuilder comandosDigitados = new StringBuilder();
            final int MAX_LEN_EASTER_EGG = Math.max(vidaExtraEasterEgg.length(), inimigosDerrotadosEasterEgg.length());


            while (jogoAtivo && jogador.getVida() > 0) {
                labirinto.mostrarMapa();
                System.out.println("\nUse W/A/S/D para mover, I para inventário, Q para sair");
                System.out.print("Sua ação: ");
                char comando;
                try {
                    comando = scanner.next().toLowerCase().charAt(0);
                } catch (NoSuchElementException e) {
                    System.err.println("Erro de entrada: Nenhum comando fornecido. Encerrando jogo.");
                    jogoAtivo = false;
                    continue;
                } catch (StringIndexOutOfBoundsException e) {
                    System.err.println("Erro de entrada: Comando vazio. Tente novamente.");
                    continue;
                }

                if (comando == 'w' || comando == 'a' || comando == 's' || comando == 'd' ||
                        comando == 'f' || comando == 'r' || comando == 'o' || comando == 't') {

                    if (comandosDigitados.length() >= MAX_LEN_EASTER_EGG) {
                        comandosDigitados.deleteCharAt(0);
                    }
                    comandosDigitados.append(comando);
                } else {
                    comandosDigitados.setLength(0);
                }

                if (comandosDigitados.toString().equals(vidaExtraEasterEgg)) {
                    System.out.println("╔════════════════════════════╗");
                    System.out.println("║ EASTER EGG DESCOBERTO! ║");
                    System.out.println("╠════════════════════════════╣");
                    System.out.println("║ A Força está com você, jovem Padawan! ║");
                    System.out.println("║ Você ganhou 50 de vida extra! ║");
                    System.out.println("║ \"Isso não é a Estrela da Morte... é uma armadilha!\" ║");
                    System.out.println("╚════════════════════════════╝");
                    jogador.adicionarVida(VIDA_EASTER_EGG);
                    comandosDigitados.setLength(0);
                } else if (comandosDigitados.toString().equals(inimigosDerrotadosEasterEgg)) {
                    System.out.println("╔════════════════════════════╗");
                    System.out.println("║ ATAQUE DA FROTA REBELDE! ║");
                    System.out.println("╠════════════════════════════╣");
                    System.out.println("║ Uma frota rebelde de emergência chegou! ║");
                    System.out.println("║ Todos os inimigos no labirinto foram eliminados! ║");
                    System.out.println("║ Pontos de Coragem: +100! ║");
                    System.out.println("╚════════════════════════════╝");
                    labirinto.removerTodosInimigos();
                    jogador.adicionarPontos(PONTOS_EASTER_EGG_FROTA);
                    comandosDigitados.setLength(0);
                }

                if (comando == 'q') {
                    System.out.println("Você saiu do jogo.");
                    jogoAtivo = false;
                    continue;
                } else if (comando == 'i') {
                    jogador.mostrarInventario();
                    continue;
                }

                boolean venceu = false;
                try {
                    venceu = jogador.mover(comando, labirinto);
                } catch (Exception e) {
                    System.err.println("Ocorreu um erro durante o movimento do jogador: " + e.getMessage());
                }


                if (venceu) {
                    System.out.println("\n╔════════════════════════════╗");
                    System.out.println("║ MISSÃO CUMPRIDA!");
                    System.out.println("╠════════════════════════════╣");
                    System.out.println("║ Parabéns, " + jogador.getNome() + "!");
                    System.out.println("║ Você escapou da Estrela da Morte!");
                    System.out.println("║ Pontuação final: " + jogador.getPontuacao());
                    System.out.println("║ Que a Força esteja com você!");
                    System.out.println("╚════════════════════════════╝");
                    jogoAtivo = false;
                }

                if (jogador.getVida() <= 0) {
                    System.out.println("\n╔════════════════════════════╗");
                    System.out.println("║ VOCÊ FOI DERROTADO!");
                    System.out.println("╠════════════════════════════╣");
                    System.out.println("║ " + jogador.getNome() + ", a Estrela da Morte te venceu.");
                    System.out.println("║ A Força não estava com você hoje.");
                    System.out.println("║ Pontuação final: " + jogador.getPontuacao());
                    System.out.println("╚════════════════════════════╝");
                    jogoAtivo = false;
                }
            }
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado no jogo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
}