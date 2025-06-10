class LaserTurret extends Armadilha {
    private static final int DANO_PADAWAN = 15;
    private static final int DANO_CAVALEIRO_JEDI = 25;
    private static final int DANO_LORDE_SITH = 40;

    public LaserTurret(Dificuldade dificuldade) {
        super(
                dificuldade == Dificuldade.LORDE_SITH ? DANO_LORDE_SITH :
                        dificuldade == Dificuldade.CAVALEIRO_JEDI ? DANO_CAVALEIRO_JEDI : DANO_PADAWAN,
                "Torre Laser Automática",
                "Torre de defesa com blasters automáticos"
        );
    }

    @Override
    public void interagir(Jogador j) {
        int danoCausado = this.dano - (j.getDefesa() / 2);
        if (danoCausado < 0) danoCausado = 0;

        j.adicionarVida(-danoCausado); // Usando adicionarVida para exibir a mudança
        System.out.println("╔════════════════════════════╗");
        System.out.println("║ ARMADILHA ATIVADA: " + nome + "!");
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ " + descricao);
        System.out.println("║ Você sofre " + danoCausado + " de dano!");
        System.out.println("║ Vida atual: " + j.getVida());
        System.out.println("╚════════════════════════════╝");
    }
}