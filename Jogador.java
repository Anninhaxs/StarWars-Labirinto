import java.util.ArrayList;
import java.util.List;

public class Jogador {
    private int x, y;
    private int vida = 150;
    private final int vidaMaxima = 150;
    int ataque = 15;
    int defesa = 10;
    private final int velocidade = 1; // Não usada, mas mantida
    private boolean temChave = false;
    private final List<String> inventario = new ArrayList<>();
    private final String nome;
    private int pontuacao = 0;
    int passosEnvenenado = 0;
    boolean envenenado = false;
    int danoVeneno = 0;

    Arma armaEquipada = null;
    Armadura armaduraEquipada = null;

    private static final int PONTOS_CHAVE = 50;
    private static final int PONTOS_ESCAPAR = 100;
    private static final int PONTOS_MONSTRO = 20;
    private static final int PONTOS_ARMA = 40;
    private static final int PONTOS_ARMADURA = 35;
    private static final int DANO_ENVENENADO_PASSOS = 2;
    private static final int DURACAO_ENVENENADO_PASSOS = 10;

    public Jogador(int x, int y, String nome) {
        this.x = x;
        this.y = y;
        this.nome = nome;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getVida() { return vida; }
    public int getAtaque() { return ataque; }
    public int getDefesa() { return defesa; }
    public boolean temChave() { return temChave; }
    public String getNome() { return nome; }
    public int getPontuacao() { return pontuacao; }
    public Arma getArmaEquipada() { return armaEquipada; }
    public Armadura getArmaduraEquipada() { return armaduraEquipada; }

    public void adicionarPontos(int valor) {
        this.pontuacao += valor;
        System.out.println("║ Pontos: +" + valor + " (Total: " + this.pontuacao + ")");
    }

    public void adicionarVida(int valor) {
        this.vida += valor;
        if (this.vida > this.vidaMaxima) {
            this.vida = this.vidaMaxima;
        }
        System.out.println("║ Vida: +" + valor + " (Total: " + this.vida + ")");
    }

    public boolean mover(char direcao, Labirinto labirinto) {
        int novoX = x, novoY = y;
        switch (direcao) {
            case 'w': novoY--; break;
            case 's': novoY++; break;
            case 'a': novoX--; break;
            case 'd': novoX++; break;
            default:
                System.out.println("Direção inválida! Use W, A, S ou D.");
                return false;
        }

        char destino;
        try {
            destino = labirinto.getCelula(novoX, novoY);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Você bateu na parede do corredor ou saiu dos limites!");
            aplicarVeneno();
            return false;
        }

        if (novoX < 0 || novoY < 0 || novoX >= labirinto.getLargura() || novoY >= labirinto.getAltura() || destino == '#') {
            System.out.println("Você bateu em uma parede de metal!");
            aplicarVeneno();
            return false;
        }

        switch (destino) {
            case 'K':
                temChave = true;
                inventario.add("Código de Acesso");
                System.out.println("╔════════════════════════════╗");
                System.out.println("║ CÓDIGO ENCONTRADO!");
                System.out.println("╠════════════════════════════╣");
                System.out.println("║ Você encontrou o código de acesso!");
                System.out.println("║ Agora pode pilotar a nave TIE Fighter.");
                adicionarPontos(PONTOS_CHAVE);
                System.out.println("╚════════════════════════════╝");
                labirinto.atualizarSaida(true);
                break;

            case 's':
                System.out.println("╔════════════════════════════╗");
                System.out.println("║ NAVE TRANCADA!");
                System.out.println("╠════════════════════════════╣");
                System.out.println("║ A nave TIE Fighter está trancada!");
                System.out.println("║ Você precisa encontrar o código de acesso primeiro.");
                System.out.println("╚════════════════════════════╝");
                return false;

            case 'F':
                if (temChave) {
                    System.out.println("╔════════════════════════════╗");
                    System.out.println("║ VOCÊ ESCAPOU!");
                    System.out.println("╠════════════════════════════╣");
                    System.out.println("║ Parabéns, " + nome + "!");
                    System.out.println("║ Você usou o código para acessar a nave");
                    System.out.println("║ e escapou da Estrela da Morte!");
                    adicionarPontos(PONTOS_ESCAPAR);
                    System.out.println("╚════════════════════════════╝");
                    return true;
                }
                break;

            case 'S': case 'B': case 'V':
                try {
                    Monstro monstro = criarMonstro(destino, labirinto.dificuldade);
                    if (monstro != null) {
                        monstro.interagir(this);
                        adicionarPontos(PONTOS_MONSTRO);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao interagir com monstro: " + e.getMessage());
                } finally {
                    labirinto.setCelula(novoX, novoY, ' ');
                }
                break;

            case 'T': case 'G':
                try {
                    Armadilha armadilha = criarArmadilha(destino, labirinto.dificuldade);
                    if (armadilha != null) {
                        armadilha.interagir(this);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao interagir com armadilha: " + e.getMessage());
                } finally {
                    labirinto.setCelula(novoX, novoY, ' ');
                }
                break;

            case 'E': case 'L': case 'D':
                try {
                    Arma arma = criarArma(destino);
                    if (arma != null) {
                        arma.aplicarEfeito(this);
                        adicionarPontos(PONTOS_ARMA);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao interagir com arma: " + e.getMessage());
                } finally {
                    labirinto.setCelula(novoX, novoY, ' ');
                }
                break;

            case 'A': case 'C': case 'M':
                try {
                    Armadura armadura = criarArmadura(destino);
                    if (armadura != null) {
                        armadura.aplicarEfeito(this);
                        adicionarPontos(PONTOS_ARMADURA); // Adiciona pontos pela armadura
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao interagir com armadura: " + e.getMessage());
                } finally {
                    labirinto.setCelula(novoX, novoY, ' ');
                }
                break;

            case 'H': case 'J': case 'c':
                try {
                    Preciosidades tesouro = criarTesouro(destino);
                    if (tesouro != null) {
                        tesouro.aplicarEfeito(this);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao interagir com tesouro: " + e.getMessage());
                } finally {
                    labirinto.setCelula(novoX, novoY, ' ');
                }
                break;
        }

        labirinto.setCelula(x, y, ' ');
        x = novoX;
        y = novoY;
        labirinto.setCelula(x, y, 'P');

        aplicarVeneno();
        return false;
    }

    private Monstro criarMonstro(char tipo, Dificuldade dificuldade) {
        switch (tipo) {
            case 'S': return new Stormtrooper(dificuldade);
            case 'B': return new BobaFett();
            case 'V': return new DarthVader();
            default: return null;
        }
    }

    private Armadilha criarArmadilha(char tipo, Dificuldade dificuldade) {
        switch (tipo) {
            case 'T': return new LaserTurret(dificuldade);
            case 'G': return new GasVenenoso(dificuldade);
            default: return null;
        }
    }

    private Arma criarArma(char tipo) {
        switch (tipo) {
            case 'E': return new Blaster();
            case 'L': return new SabreLuz();
            case 'D': return new Vibrosword();
            default: return null;
        }
    }

    private Armadura criarArmadura(char tipo) {
        switch (tipo) {
            case 'A': return new ArmaduraRebelde();
            case 'C': return new ArmaduraMandaloriana();
            case 'M': return new ArmaduraClone();
            default: return null;
        }
    }

    private Preciosidades criarTesouro(char tipo) {
        switch (tipo) {
            case 'H': return new Holocron();
            case 'J': return new Joias();
            case 'c': return new Creditos();
            default: return null;
        }
    }

    private void aplicarVeneno() {
        if (envenenado) {
            passosEnvenenado++;
            if (passosEnvenenado % DANO_ENVENENADO_PASSOS == 0) {
                vida -= danoVeneno;
                System.out.println("╔════════════════════════════╗");
                System.out.println("║ EFEITO DE GÁS VENENOSO!");
                System.out.println("╠════════════════════════════╣");
                System.out.println("║ Você sofre " + danoVeneno + " de dano!");
                System.out.println("║ Vida atual: " + vida);
                System.out.println("╚════════════════════════════╝");
            }
            if (passosEnvenenado >= DURACAO_ENVENENADO_PASSOS) {
                envenenado = false;
                passosEnvenenado = 0;
                System.out.println("╔════════════════════════════╗");
                System.out.println("║ VENENO NEUTRALIZADO!");
                System.out.println("╠════════════════════════════╣");
                System.out.println("║ Você se recuperou do efeito do gás venenoso.");
                System.out.println("╚════════════════════════════╝");
            }
        }
    }

    public void mostrarInventario() {
        System.out.println("╔════════════════════════════╗");
        System.out.println("║ INVENTÁRIO DE " + nome.toUpperCase());
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ Vida: " + vida);
        System.out.println("║ Pontuação: " + pontuacao);
        System.out.println("║ Itens: " + (inventario.isEmpty() ? "Nenhum" : String.join(", ", inventario)));

        try {
            if (armaEquipada != null) {
                System.out.println("║ Arma: " + armaEquipada.nome + " (+" + armaEquipada.ataqueBonus + " ataque)");
            } else {
                System.out.println("║ Arma: Nenhuma");
            }
        } catch (NullPointerException e) {
            System.err.println("Erro ao exibir arma equipada: " + e.getMessage());
            System.out.println("║ Arma: Erro ao carregar");
        }

        try {
            if (armaduraEquipada != null) {
                System.out.println("║ Armadura: " + armaduraEquipada.nome + " (+" + armaduraEquipada.defesaBonus + " defesa)");
            } else {
                System.out.println("║ Armadura: Nenhuma");
            }
        } catch (NullPointerException e) {
            System.err.println("Erro ao exibir armadura equipada: " + e.getMessage());
            System.out.println("║ Armadura: Erro ao carregar");
        }

        if (temChave) {
            System.out.println("║ Você possui o código de acesso da nave");
        }

        System.out.println("╚════════════════════════════╝");
    }
}

