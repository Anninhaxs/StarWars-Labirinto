import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

enum Dificuldade {
    PADAWAN,
    CAVALEIRO_JEDI,
    LORDE_SITH
}

class Labirinto {
    private final int largura;
    private final int altura;
    private final Dificuldade dificuldade;
    private char[][] mapa;
    private Random random = new Random();
    private List<int[]> caminhoGerado = new ArrayList<>();
    private Map<Character, Perigo> perigosMap = new HashMap<>();

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    public Labirinto(int largura, int altura, Dificuldade dificuldade) {
        this.largura = largura;
        this.altura = altura;
        this.dificuldade = dificuldade;
        this.mapa = new char[altura][largura];
        perigosMap.put('S', new Stormtrooper(dificuldade));
        perigosMap.put('T', new LaserTurret(dificuldade));
        perigosMap.put('G', new GasVenenoso(dificuldade));
        generarLabirintoValido();
        atualizarSaida(false);
    }

    public void generarLabirintoValido() {
        boolean labirintoValido = false;
        int tentativas = 0;
        while (!labirintoValido && tentativas < 10) {
            gerarLabirintoBase();
            labirintoValido = verificarConectividade();
            tentativas++;
            if (tentativas == 10) {
                System.out.println("Não foi possível gerar um labirinto válido após 10 tentativas. Tentando novamente...");
                tentativas = 0;
            }
        }
        removerMaisParedes();
    }

    private void gerarLabirintoBase() {
        for (int i = 0; i < altura; i++) {
            Arrays.fill(mapa[i], '#');
        }
        caminhoGerado.clear();

        int startX = random.nextInt((largura - 2) / 2) * 2 + 1;
        int startY = random.nextInt((altura - 2) / 2) * 2 + 1;
        gerarCaminhoDFS(startX, startY);

        caminhoGerado.removeIf(p -> mapa[p[1]][p[0]] == 'P' || mapa[p[1]][p[0]] == 'K' || mapa[p[1]][p[0]] == 's');

        int[] posJogador = obterPosicaoLivreAleatoria();
        int[] posChave = obterPosicaoLivreAleatoria();
        int[] posSaida = obterPosicaoLivreAleatoria();

        if (posJogador != null) mapa[posJogador[1]][posJogador[0]] = 'P';
        if (posChave != null) mapa[posChave[1]][posChave[0]] = 'K';
        if (posSaida != null) mapa[posSaida[1]][posSaida[0]] = 's';

        colocarElementosAleatorios();
    }

    private int[] obterPosicaoLivreAleatoria() {
        List<int[]> espacosLivres = new ArrayList<>();
        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {
                if (mapa[y][x] == ' ') {
                    espacosLivres.add(new int[]{x, y});
                }
            }
        }
        if (espacosLivres.isEmpty()) return null;
        return espacosLivres.get(random.nextInt(espacosLivres.size()));
    }

    private void colocarElementosAleatorios() {
        int qtdStormtroopers, qtdArmadilhas, qtdTesouros;

        switch (dificuldade) {
            case PADAWAN:
                qtdStormtroopers = 2;
                qtdArmadilhas = 1;
                qtdTesouros = 8;
                break;
            case CAVALEIRO_JEDI:
                qtdStormtroopers = 4;
                qtdArmadilhas = 3;
                qtdTesouros = 6;
                break;
            case LORDE_SITH:
            default:
                qtdStormtroopers = 8;
                qtdArmadilhas = 6;
                qtdTesouros = 3;
                if (random.nextDouble() < 0.2) colocarElementoAleatorio('V');
                colocarElementoAleatorio('B');
                break;
        }

        colocarElementoAleatorio('E');
        colocarElementoAleatorio('A');
        if (dificuldade == Dificuldade.CAVALEIRO_JEDI || dificuldade == Dificuldade.LORDE_SITH) {
            colocarElementoAleatorio('L');
            colocarElementoAleatorio('C');
        }

        for (int i = 0; i < qtdStormtroopers; i++) colocarElementoAleatorio('S');
        for (int i = 0; i < qtdArmadilhas; i++) {
            if (random.nextBoolean()) {
                colocarElementoAleatorio('T');
            } else {
                colocarElementoAleatorio('G');
            }
        }
        for (int i = 0; i < qtdTesouros; i++) {
            char[] tesouros = {'H', 'J', 'c'};
            colocarElementoAleatorio(tesouros[random.nextInt(tesouros.length)]);
        }
    }

    private boolean verificarConectividade() {
        boolean[][] visitado = new boolean[altura][largura];
        int[] inicio = encontrarJogador();
        if (inicio == null) return false;

        Queue<int[]> fila = new LinkedList<>();
        fila.add(inicio);
        visitado[inicio[1]][inicio[0]] = true;

        int celulasAcessiveis = 0;
        int totalCelulasLivres = 0;

        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                if (mapa[y][x] != '#') totalCelulasLivres++;
            }
        }

        int[][] direcoes = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        while (!fila.isEmpty()) {
            int[] atual = fila.poll();
            celulasAcessiveis++;

            for (int[] dir : direcoes) {
                int nx = atual[0] + dir[0];
                int ny = atual[1] + dir[1];

                if (nx >= 0 && ny >= 0 && nx < largura && ny < altura &&
                        !visitado[ny][nx] && mapa[ny][nx] != '#') {
                    visitado[ny][nx] = true;
                    fila.add(new int[]{nx, ny});
                }
            }
        }

        return (double) celulasAcessiveis / totalCelulasLivres >= 0.90;
    }

    private void removerMaisParedes() {
        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {
                if (mapa[y][x] == '#' && contarVizinhosLivres(x, y) == 1) {
                    mapa[y][x] = ' ';
                }
            }
        }

        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {
                if (mapa[y][x] == '#') {
                    int vizinhosLivres = contarVizinhosLivres(x, y);
                    double chanceRemocao = 0.0;

                    switch (dificuldade) {
                        case PADAWAN:
                            if (vizinhosLivres >= 2) chanceRemocao = 0.6;
                            break;
                        case CAVALEIRO_JEDI:
                            if (vizinhosLivres >= 2) chanceRemocao = 0.4;
                            break;
                        case LORDE_SITH:
                            if (vizinhosLivres >= 2) chanceRemocao = 0.2;
                            break;
                    }

                    if (random.nextDouble() < chanceRemocao) {
                        mapa[y][x] = ' ';
                    }
                }
            }
        }
    }

    private int contarVizinhosLivres(int x, int y) {
        int count = 0;
        if (y > 0 && mapa[y-1][x] != '#') count++;
        if (y < altura - 1 && mapa[y+1][x] != '#') count++;
        if (x > 0 && mapa[y][x-1] != '#') count++;
        if (x < largura - 1 && mapa[y][x+1] != '#') count++;
        return count;
    }

    private void gerarCaminhoDFS(int x, int y) {
        mapa[y][x] = ' ';
        caminhoGerado.add(new int[]{x, y});

        int[][] direcoes = {{2, 0}, {-2, 0}, {0, 2}, {0, -2}};
        List<int[]> dirList = Arrays.asList(direcoes);
        Collections.shuffle(dirList);

        for (int[] dir : dirList) {
            int nx = x + dir[0];
            int ny = y + dir[1];

            if (nx > 0 && ny > 0 && nx < largura - 1 && ny < altura - 1 && mapa[ny][nx] == '#') {
                mapa[y + dir[1] / 2][x + dir[0] / 2] = ' ';
                gerarCaminhoDFS(nx, ny);
            }
        }
    }

    public void atualizarSaida(boolean temChave) {
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                if (mapa[y][x] == 's' || mapa[y][x] == 'F') {
                    mapa[y][x] = temChave ? 'F' : 's';
                    return;
                }
            }
        }
    }

    private void colocarElementoAleatorio(char simbolo) {
        int x, y;
        int tentativas = 0;
        do {
            x = random.nextInt(largura);
            y = random.nextInt(altura);
            tentativas++;
            if (tentativas > 200) return;
        } while (mapa[y][x] != ' ');
        mapa[y][x] = simbolo;
    }

    public void mostrarMapa() {
        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < largura; j++) {
                System.out.print(mapa[i][j]);
            }
            System.out.println();
        }
    }

    public char getCelula(int x, int y) {
        if (x < 0 || y < 0 || x >= largura || y >= altura) return '#';
        return mapa[y][x];
    }

    public void setCelula(int x, int y, char valor) {
        if (x >= 0 && y >= 0 && x < largura && y < altura) {
            mapa[y][x] = valor;
        }
    }

    public int[] encontrarJogador() {
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                if (mapa[y][x] == 'P') return new int[]{x, y};
            }
        }
        return null;
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }

    public void removerTodosInimigos() {
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                char celula = mapa[y][x];
                if (celula == 'S' || celula == 'B' || celula == 'V') {
                    mapa[y][x] = ' ';
                }
            }
        }
    }
}

class Jogador {
    int x, y;
    int vida = 150;
    int vidaMaxima = 150;
    int ataque = 15;
    int defesa = 10;
    int velocidade = 1;
    boolean temChave = false;
    List<String> inventario = new ArrayList<>();
    String nome;
    int pontuacao = 0;
    int passosEnvenenado = 0;
    boolean envenenado = false;
    int danoVeneno = 0;

    Arma armaEquipada = null;
    Armadura armaduraEquipada = null;

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

    public Jogador(int x, int y, String nome) {
        this.x = x;
        this.y = y;
        this.nome = nome;
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

        if (novoX < 0 || novoY < 0 || novoX >= labirinto.getLargura() || novoY >= labirinto.getAltura()) {
            System.out.println("Você bateu na parede do corredor!");
            aplicarVeneno();
            return false;
        }

        char destino = labirinto.getCelula(novoX, novoY);
        if (destino == '#') {
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
                adicionarPontos(50);
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
                    adicionarPontos(100);
                    System.out.println("╚════════════════════════════╝");
                    return true;
                }
                break;

            case 'S': case 'B': case 'V':
                Monstro monstro = criarMonstro(destino, labirinto.getDificuldade());
                if (monstro != null) {
                    monstro.interagir(this);
                    adicionarPontos(20);
                }
                labirinto.setCelula(novoX, novoY, ' ');
                break;

            case 'T': case 'G':
                Armadilha armadilha = criarArmadilha(destino, labirinto.getDificuldade());
                if (armadilha != null) {
                    armadilha.interagir(this);
                }
                labirinto.setCelula(novoX, novoY, ' ');
                break;

            case 'E': case 'L': case 'D':
                Arma arma = criarArma(destino);
                if (arma != null) {
                    arma.aplicarEfeito(this);
                    adicionarPontos(40);
                    labirinto.setCelula(novoX, novoY, ' ');
                }
                break;

            case 'A': case 'C': case 'M':
                Armadura armadura = criarArmadura(destino);
                if (armadura != null) {
                    armadura.aplicarEfeito(this);
                    labirinto.setCelula(novoX, novoY, ' ');
                }
                break;

            case 'H': case 'J': case 'c':
                Preciosidades tesouro = criarTesouro(destino);
                if (tesouro != null) {
                    tesouro.aplicarEfeito(this);
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
            if (passosEnvenenado % 2 == 0) {
                vida -= danoVeneno;
                System.out.println("╔════════════════════════════╗");
                System.out.println("║ EFEITO DE GÁS VENENOSO!");
                System.out.println("╠════════════════════════════╣");
                System.out.println("║ Você sofre " + danoVeneno + " de dano!");
                System.out.println("║ Vida atual: " + vida);
                System.out.println("╚════════════════════════════╝");
            }
            if (passosEnvenenado >= 10) {
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
        System.out.println("║ Itens: " + String.join(", ", inventario));

        if (armaEquipada != null) {
            System.out.println("║ Arma: " + armaEquipada.nome + " (+" + armaEquipada.ataqueBonus + " ataque)");
        } else {
            System.out.println("║ Arma: Nenhuma");
        }

        if (armaduraEquipada != null) {
            System.out.println("║ Armadura: " + armaduraEquipada.nome + " (+" + armaduraEquipada.defesaBonus + " defesa)");
        } else {
            System.out.println("║ Armadura: Nenhuma");
        }

        if (temChave) {
            System.out.println("║ Você possui o código de acesso da nave");
        }

        System.out.println("╚════════════════════════════╝");
    }

    public int getPontuacao() {
        return pontuacao;
    }
}

interface Perigo {
    void interagir(Jogador j);
}

abstract class Armadilha implements Perigo {
    int dano;
    String nome;
    String descricao;

    public Armadilha(int dano, String nome, String descricao) {
        this.dano = dano;
        this.nome = nome;
        this.descricao = descricao;
    }
}

class LaserTurret extends Armadilha {
    public LaserTurret(Dificuldade dificuldade) {
        super(
                dificuldade == Dificuldade.LORDE_SITH ? 40 :
                        dificuldade == Dificuldade.CAVALEIRO_JEDI ? 25 : 15,
                "Torre Laser Automática",
                "Torre de defesa com blasters automáticos"
        );
    }

    @Override
    public void interagir(Jogador j) {
        int danoCausado = this.dano - (j.defesa / 2);
        if (danoCausado < 0) danoCausado = 0;

        j.vida -= danoCausado;
        System.out.println("╔════════════════════════════╗");
        System.out.println("║ ARMADILHA ATIVADA: " + nome + "!");
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ " + descricao);
        System.out.println("║ Você sofre " + danoCausado + " de dano!");
        System.out.println("║ Vida atual: " + j.vida);
        System.out.println("╚════════════════════════════╝");
    }
}

class GasVenenoso extends Armadilha {
    private final int danoPorPassos;

    public GasVenenoso(Dificuldade dificuldade) {
        super(
                dificuldade == Dificuldade.LORDE_SITH ? 25 :
                        dificuldade == Dificuldade.CAVALEIRO_JEDI ? 15 : 8,
                "Gás Venenoso",
                "Nuvem de gás tóxico liberada pelos sistemas de segurança"
        );
        this.danoPorPassos = dificuldade == Dificuldade.LORDE_SITH ? 5 :
                dificuldade == Dificuldade.CAVALEIRO_JEDI ? 3 : 2;
    }

    @Override
    public void interagir(Jogador j) {
        System.out.println("╔════════════════════════════╗");
        System.out.println("║ ARMADILHA ATIVADA: " + nome + "!");
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ " + descricao);
        if (!j.envenenado) {
            j.envenenado = true;
            j.danoVeneno = this.danoPorPassos;
            j.passosEnvenenado = 0;
            System.out.println("║ Você foi envenenado! Sofrerá dano por alguns movimentos.");
        } else {
            System.out.println("║ O veneno está mais forte!");
            j.danoVeneno += this.danoPorPassos;
        }
        System.out.println("╚════════════════════════════╝");
    }
}

abstract class Monstro implements Perigo {
    int vida;
    int defesa;
    int velocidade;
    int dano;
    String nome;
    String descricao;

    public Monstro(int vida, int defesa, int velocidade, int dano, String nome, String descricao) {
        this.vida = vida;
        this.defesa = defesa;
        this.velocidade = velocidade;
        this.dano = dano;
        this.nome = nome;
        this.descricao = descricao;
    }

    @Override
    public void interagir(Jogador j) {
        System.out.println("╔════════════════════════════╗");
        System.out.println("║ INIMIGO ENCONTRADO: " + nome + "!");
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ " + descricao);
        System.out.println("║ Vida do " + nome + ": " + this.vida);
        System.out.println("║ Seu Ataque: " + j.ataque + " | Sua Defesa: " + j.defesa);
        System.out.println("╚════════════════════════════╝");

        int danoCausadoAoMonstro = j.ataque - this.defesa;
        if (danoCausadoAoMonstro < 1) danoCausadoAoMonstro = 1;

        int danoCausadoAoJogador = this.dano - (j.defesa / 2);
        if (danoCausadoAoJogador < 0) danoCausadoAoJogador = 0;

        this.vida -= danoCausadoAoMonstro;
        j.vida -= danoCausadoAoJogador;

        System.out.println("╔════════════════════════════╗");
        System.out.println("║ RESULTADO DO COMBATE!");
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ Você causou " + danoCausadoAoMonstro + " de dano ao " + nome + ".");
        System.out.println("║ " + nome + " causou " + danoCausadoAoJogador + " de dano a você.");
        System.out.println("║ Vida do " + nome + ": " + this.vida);
        System.out.println("║ Sua Vida: " + j.vida);
        System.out.println("╚════════════════════════════╝");

        if (this.vida <= 0) {
            System.out.println("╔════════════════════════════╗");
            System.out.println("║ " + nome + " DERROTADO!");
            System.out.println("╚════════════════════════════╝");
        } else if (j.vida > 0) {
            System.out.println("╔════════════════════════════╗");
            System.out.println("║ O " + nome + " fugiu para as sombras, ou você se esquivou!");
            System.out.println("║ Continue explorando, mas cuidado!");
            System.out.println("╚════════════════════════════╝");
        }
    }
}

class Stormtrooper extends Monstro {
    public Stormtrooper(Dificuldade dificuldade) {
        super(
                dificuldade == Dificuldade.LORDE_SITH ? 50 :
                        dificuldade == Dificuldade.CAVALEIRO_JEDI ? 30 : 20,
                dificuldade == Dificuldade.LORDE_SITH ? 20 :
                        dificuldade == Dificuldade.CAVALEIRO_JEDI ? 8 : 2,
                2,
                dificuldade == Dificuldade.LORDE_SITH ? 20 :
                        dificuldade == Dificuldade.CAVALEIRO_JEDI ? 10 : 5,
                "Stormtrooper",
                "Soldado de elite do Império com blaster E-11"
        );
    }
}

class BobaFett extends Monstro {
    public BobaFett() {
        super(60, 20, 4, 25,
                "Boba Fett",
                "O lendário caçador de recompensas com seu jetpack");
    }
}

class DarthVader extends Monstro {
    public DarthVader() {
        super(100, 25, 3, 30,
                "Darth Vader",
                "O Lorde Sombrio dos Sith com seu sabre de luz vermelho");
    }
}

interface Tesouro {
    void aplicarEfeito(Jogador j);
}

abstract class Armadura implements Tesouro {
    int defesaBonus;
    String nome;
    String descricao;

    public Armadura(int defesaBonus, String nome, String descricao) {
        this.defesaBonus = defesaBonus;
        this.nome = nome;
        this.descricao = descricao;
    }

    @Override
    public void aplicarEfeito(Jogador j) {
        j.armaduraEquipada = this;
        j.defesa += defesaBonus;
        System.out.println("╔════════════════════════════╗");
        System.out.println("║ ARMADURA ENCONTRADA!");
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ " + nome);
        System.out.println("║ " + descricao);
        System.out.println("║ Bônus de Defesa: +" + defesaBonus);
        System.out.println("║ Defesa total: " + j.defesa);
        j.adicionarPontos(35);
        System.out.println("╚════════════════════════════╝");
    }
}

class ArmaduraRebelde extends Armadura {
    public ArmaduraRebelde() {
        super(10, "Armadura Rebelde", "Armadura leve usada pelas tropas especiais rebeldes");
    }
}

class ArmaduraMandaloriana extends Armadura {
    public ArmaduraMandaloriana() {
        super(30, "Armadura Mandaloriana", "Armadura de beskar usada pelos guerreiros Mandalorianos");
    }
}

class ArmaduraClone extends Armadura {
    public ArmaduraClone() {
        super(15, "Armadura Clone", "Armadura usada pelos soldados clones da República");
    }
}

abstract class Arma implements Tesouro {
    int ataqueBonus;
    String nome;
    String descricao;

    public Arma(int ataqueBonus, String nome, String descricao) {
        this.ataqueBonus = ataqueBonus;
        this.nome = nome;
        this.descricao = descricao;
    }

    @Override
    public void aplicarEfeito(Jogador j) {
        j.armaEquipada = this;
        j.ataque += ataqueBonus;
        System.out.println("╔════════════════════════════╗");
        System.out.println("║ ARMA ENCONTRADA!");
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ " + nome);
        System.out.println("║ " + descricao);
        System.out.println("║ Bônus de Ataque: +" + ataqueBonus);
        System.out.println("║ Ataque total: " + j.ataque);
        j.adicionarPontos(40);
        System.out.println("╚════════════════════════════╝");
    }
}

class SabreLuz extends Arma {
    public SabreLuz() {
        super(40, "Sabre de Luz", "Arma Jedi tradicional com lâmina de plasma");
    }
}

class Blaster extends Arma {
    public Blaster() {
        super(20, "Blaster DH-17", "Pistola blaster padrão da Aliança Rebelde");
    }
}

class Vibrosword extends Arma {
    public Vibrosword() {
        super(15, "Vibrosword", "Espada com lâmina vibro para cortar quase qualquer material");
    }
}

abstract class Preciosidades implements Tesouro {
    int valor;
    String nome;
    String descricao;

    public Preciosidades(int valor, String nome, String descricao) {
        this.valor = valor;
        this.nome = nome;
        this.descricao = descricao;
    }

    @Override
    public void aplicarEfeito(Jogador j) {
        j.adicionarPontos(valor);
        System.out.println("╔════════════════════════════╗");
        System.out.println("║ TESOURO ENCONTRADO!");
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ " + nome);
        System.out.println("║ " + descricao);
        System.out.println("║ Valor: " + valor + " pontos");
        System.out.println("╚════════════════════════════╝");
    }
}

class Holocron extends Preciosidades {
    public Holocron() {
        super(400, "Holocron Jedi", "Dispositivo de armazenamento de conhecimento Jedi");
    }
}

class Joias extends Preciosidades {
    public Joias() {
        super(250, "Joias de Alderaan", "Joias raras do planeta destruído Alderaan");
    }
}

class Creditos extends Preciosidades {
    public Creditos() {
        super(150, "Saco de Creditos", "Moeda galáctica padrão");
    }
}

public class StarWarsLabirinto {
    private static Dificuldade Dificade;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║      STAR WARS: FUGA DA ESTRELA DA MORTE     ║");
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
        int escolha = scanner.nextInt();

        Dificuldade dificuldade;
        switch (escolha) {
            case 1: dificuldade = Dificuldade.PADAWAN; break;
            case 2: dificuldade = Dificade.CAVALEIRO_JEDI; break;
            case 3: dificuldade = Dificuldade.LORDE_SITH; break;
            default:
                System.out.println("Escolha inválida. Usando dificuldade Padawan.");
                dificuldade = Dificuldade.PADAWAN;
        }

        Labirinto labirinto = new Labirinto(25, 15, dificuldade);

        int[] posJogador = labirinto.encontrarJogador();
        Jogador jogador = new Jogador(posJogador[0], posJogador[1], nome);

        System.out.println("\n╔════════════════════════════╗");
        System.out.println("║ INÍCIO DO JOGO!");
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ Jogador: " + jogador.nome);
        System.out.println("║ Dificuldade: " + dificuldade);
        System.out.println("║ Controles: W (cima), A (esquerda)");
        System.out.println("║            S (baixo), D (direita)");
        System.out.println("║            I (inventário)");
        System.out.println("║            Q (sair)");
        System.out.println("╚════════════════════════════╝");

        boolean jogoAtivo = true;

        String vidaExtraEasterEgg = "wwssadad";
        String inimigosDerrotadosEasterEgg = "frota";
        StringBuilder comandosDigitados = new StringBuilder();
        final int MAX_LEN_EASTER_EGG = Math.max(vidaExtraEasterEgg.length(), inimigosDerrotadosEasterEgg.length());


        while (jogoAtivo && jogador.vida > 0) {
            labirinto.mostrarMapa();
            System.out.println("\nUse W/A/S/D para mover, I para inventário, Q para sair");
            System.out.print("Sua ação: ");
            char comando = scanner.next().toLowerCase().charAt(0);

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
                System.out.println("║ EASTER EGG DESCOBERTO!     ║");
                System.out.println("╠════════════════════════════╣");
                System.out.println("║ A Força está com você, jovem Padawan!    ║");
                System.out.println("║ Você ganhou 50 de vida extra!              ║");
                System.out.println("║ \"Isso não é a Estrela da Morte... é uma armadilha!\" ║");
                System.out.println("╚════════════════════════════╝");
                jogador.adicionarVida(50);
                comandosDigitados.setLength(0);
            } else if (comandosDigitados.toString().equals(inimigosDerrotadosEasterEgg)) {
                System.out.println("╔════════════════════════════╗");
                System.out.println("║ ATAQUE DA FROTA REBELDE!   ║");
                System.out.println("╠════════════════════════════╣");
                System.out.println("║ Uma frota rebelde de emergência chegou! ║");
                System.out.println("║ Todos os inimigos no labirinto foram eliminados! ║");
                System.out.println("║ Pontos de Coragem: +100!                 ║");
                System.out.println("╚════════════════════════════╝");
                labirinto.removerTodosInimigos();
                jogador.adicionarPontos(100);
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

            boolean venceu = jogador.mover(comando, labirinto);
            if (venceu) {
                System.out.println("\n╔════════════════════════════╗");
                System.out.println("║ MISSÃO CUMPRIDA!");
                System.out.println("╠════════════════════════════╣");
                System.out.println("║ Parabéns, " + jogador.nome + "!");
                System.out.println("║ Você escapou da Estrela da Morte!");
                System.out.println("║ Pontuação final: " + jogador.getPontuacao());
                System.out.println("║ Que a Força esteja com você!");
                System.out.println("╚════════════════════════════╝");
                jogoAtivo = false;
            }

            if (jogador.vida <= 0) {
                System.out.println("\n╔════════════════════════════╗");
                System.out.println("║ VOCÊ FOI DERROTADO!");
                System.out.println("╠════════════════════════════╣");
                System.out.println("║ " + jogador.nome + ", a Estrela da Morte te venceu.");
                System.out.println("║ A Força não estava com você hoje.");
                System.out.println("║ Pontuação final: " + jogador.getPontuacao());
                System.out.println("╚════════════════════════════╝");
                jogoAtivo = false;
            }
        }
        scanner.close();
    }
}
