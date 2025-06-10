class Stormtrooper extends Monstro {
    private static final int VIDA_PADAWAN = 20;
    private static final int VIDA_CAVALEIRO_JEDI = 30;
    private static final int VIDA_LORDE_SITH = 50;
    private static final int DEFESA_PADAWAN = 2;
    private static final int DEFESA_CAVALEIRO_JEDI = 8;
    private static final int DEFESA_LORDE_SITH = 20;
    private static final int DANO_PADAWAN = 5;
    private static final int DANO_CAVALEIRO_JEDI = 10;
    private static final int DANO_LORDE_SITH = 20;

    public Stormtrooper(Dificuldade dificuldade) {
        super(
                dificuldade == Dificuldade.LORDE_SITH ? VIDA_LORDE_SITH :
                        dificuldade == Dificuldade.CAVALEIRO_JEDI ? VIDA_CAVALEIRO_JEDI : VIDA_PADAWAN,
                dificuldade == Dificuldade.LORDE_SITH ? DEFESA_LORDE_SITH :
                        dificuldade == Dificuldade.CAVALEIRO_JEDI ? DEFESA_CAVALEIRO_JEDI : DEFESA_PADAWAN,
                2,
                dificuldade == Dificuldade.LORDE_SITH ? DANO_LORDE_SITH :
                        dificuldade == Dificuldade.CAVALEIRO_JEDI ? DANO_CAVALEIRO_JEDI : DANO_PADAWAN,
                "Stormtrooper",
                "Soldado de elite do Império com blaster E-11"
        );
    }
}
