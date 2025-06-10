class GasVenenoso extends Armadilha {
    private final int danoPorPassos;

    private static final int DANO_BASE_PADAWAN = 8;
    private static final int DANO_BASE_CAVALEIRO_JEDI = 15;
    private static final int DANO_BASE_LORDE_SITH = 25;

    private static final int DANO_POR_PASSOS_PADAWAN = 2;
    private static final int DANO_POR_PASSOS_CAVALEIRO_JEDI = 3;
    private static final int DANO_POR_PASSOS_LORDE_SITH = 5;

    public GasVenenoso(Dificuldade dificuldade) {
        super(
                dificuldade == Dificuldade.LORDE_SITH ? DANO_BASE_LORDE_SITH :
                        dificuldade == Dificuldade.CAVALEIRO_JEDI ? DANO_BASE_CAVALEIRO_JEDI : DANO_BASE_PADAWAN,
                "Gás Venenoso",
                "Nuvem de gás tóxico liberada pelos sistemas de segurança"
        );
        this.danoPorPassos = dificuldade == Dificuldade.LORDE_SITH ? DANO_POR_PASSOS_LORDE_SITH :
                dificuldade == Dificuldade.CAVALEIRO_JEDI ? DANO_POR_PASSOS_CAVALEIRO_JEDI : DANO_POR_PASSOS_PADAWAN;
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