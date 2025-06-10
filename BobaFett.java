class BobaFett extends Monstro {
    private static final int VIDA_BOBA_FETT = 60;
    private static final int DEFESA_BOBA_FETT = 20;
    private static final int VELOCIDADE_BOBA_FETT = 4;
    private static final int DANO_BOBA_FETT = 25;

    public BobaFett() {
        super(VIDA_BOBA_FETT, DEFESA_BOBA_FETT, VELOCIDADE_BOBA_FETT, DANO_BOBA_FETT,
                "Boba Fett",
                "O lendário caçador de recompensas com seu jetpack");
    }
}