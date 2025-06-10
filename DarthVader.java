class DarthVader extends Monstro {
    private static final int VIDA_DARTH_VADER = 100;
    private static final int DEFESA_DARTH_VADER = 25;
    private static final int VELOCIDADE_DARTH_VADER = 3;
    private static final int DANO_DARTH_VADER = 30;

    public DarthVader() {
        super(VIDA_DARTH_VADER, DEFESA_DARTH_VADER, VELOCIDADE_DARTH_VADER, DANO_DARTH_VADER,
                "Darth Vader",
                "O Lorde Sombrio dos Sith com seu sabre de luz vermelho");
    }
}
