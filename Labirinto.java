import java.util.*;

public class Labirinto {

    private final int largura;
    private final int altura;
    final Dificuldade dificuldade;
    private char[][] mapa;
    private final Random random = new Random();
    private final List<int[]> caminhoGerado = new ArrayList<>();
    private final Map<Character, Perigo> perigosMap = new HashMap<>();

    private static final int MAX_TENTATIVAS_GERACAO = 100;
    private static final int MAX_TENTATIVAS_ELEMENTO = 200;
    private static final double CONECTIVIDADE_MINIMA = 0.90;

    public Labirinto(int largura, int altura, Dificuldade dificuldade) {
        if (largura <= 0 || altura <= 0) {
            throw new IllegalArgumentException("Largura e altura devem ser maiores que zero.");
        }
        this.largura = (largura % 2 == 0) ? largura + 1 : largura;
        this.altura = (altura % 2 == 0) ? altura + 1 : altura;
        this.dificuldade = dificuldade;
        this.mapa = new char[this.altura][this.largura];

        try {
            perigosMap.put('S', new Stormtrooper(dificuldade));
            perigosMap.put('T', new LaserTurret(dificuldade));
            perigosMap.put('G', new GasVenenoso(dificuldade));
        } catch (Exception e) {
            System.err.println("Erro ao inicializar o mapa de perigos: " + e.getMessage());
            throw new RuntimeException("Falha na inicialização do Labirinto", e);
        }

        gerarLabirintoValido();
        atualizarSaida(false);
    }

    private void gerarLabirintoValido() {
        boolean labirintoValido = false;
        int tentativas = 0;

        while (!labirintoValido && tentativas < MAX_TENTATIVAS_GERACAO) {
            try {
                gerarLabirintoBase();
                labirintoValido = verificarConectividade();
            } catch (Exception e) {
                System.err.println("Erro durante a geração base do labirinto: " + e.getMessage());
                labirintoValido = false;
            }
            tentativas++;

            if (!labirintoValido && tentativas >= MAX_TENTATIVAS_GERACAO) {
                System.out.println("Não foi possível gerar um labirinto válido após " + MAX_TENTATIVAS_GERACAO + " tentativas. Abortando...");
                throw new RuntimeException("Falha ao gerar um labirinto conectado.");
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

        try {
            if (posJogador != null) mapa[posJogador[1]][posJogador[0]] = 'P';
            else System.err.println("Não foi possível encontrar uma posição livre para o jogador.");

            if (posChave != null) mapa[posChave[1]][posChave[0]] = 'K';
            else System.err.println("Não foi possível encontrar uma posição livre para a chave.");

            if (posSaida != null) mapa[posSaida[1]][posSaida[0]] = 's';
            else System.err.println("Não foi possível encontrar uma posição livre para a saída.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Erro ao definir posições iniciais: " + e.getMessage());
            throw new RuntimeException("Erro ao posicionar elementos iniciais", e);
        }

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
        if (inicio == null) {
            System.err.println("Jogador não encontrado para verificar conectividade.");
            return false;
        }

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
        return (double) celulasAcessiveis / totalCelulasLivres >= CONECTIVIDADE_MINIMA;
    }

    private void removerMaisParedes() {
        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {
                try {
                    if (mapa[y][x] == '#' && contarVizinhosLivres(x, y) == 1) {
                        mapa[y][x] = ' ';
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Erro ao remover paredes (fase 1): " + e.getMessage());
                }
            }
        }

        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {
                try {
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
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Erro ao remover paredes (fase 2): " + e.getMessage());
                }
            }
        }
    }

    private int contarVizinhosLivres(int x, int y) {
        int count = 0;
        try {
            if (y > 0 && mapa[y - 1][x] != '#') count++;
            if (y < altura - 1 && mapa[y + 1][x] != '#') count++;
            if (x > 0 && mapa[y][x - 1] != '#') count++;
            if (x < largura - 1 && mapa[y][x + 1] != '#') count++;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Erro de índice ao contar vizinhos livres em (" + x + "," + y + "): " + e.getMessage());
        }
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

            try {
                if (nx > 0 && ny > 0 && nx < largura - 1 && ny < altura - 1 && mapa[ny][nx] == '#') {
                    mapa[y + dir[1] / 2][x + dir[0] / 2] = ' ';
                    gerarCaminhoDFS(nx, ny);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Erro de índice durante a geração DFS em (" + x + "," + y + "): " + e.getMessage());
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
            if (tentativas > MAX_TENTATIVAS_ELEMENTO) {
                System.err.println("Não foi possível colocar o elemento '" + simbolo + "' após " + MAX_TENTATIVAS_ELEMENTO + " tentativas.");
                return;
            }
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
        if (x < 0 || y < 0 || x >= largura || y >= altura) {
            return '#';
        }
        return mapa[y][x];
    }

    public void setCelula(int x, int y, char valor) {
        if (x >= 0 && y >= 0 && x < largura && y < altura) {
            mapa[y][x] = valor;
        } else {
            System.err.println("Tentativa de definir célula fora dos limites: (" + x + "," + y + ")");
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
