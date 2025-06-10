abstract class Monstro implements Perigo {
    protected int vida; // Alterado para protected
    protected int defesa; // Alterado para protected
    protected int velocidade; // Alterado para protected
    protected int dano; // Alterado para protected
    protected String nome; // Alterado para protected
    protected String descricao; // Alterado para protected

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
        System.out.println("║ Seu Ataque: " + j.getAtaque() + " | Sua Defesa: " + j.getDefesa());
        System.out.println("╚════════════════════════════╝");

        int danoCausadoAoMonstro = j.getAtaque() - this.defesa;
        if (danoCausadoAoMonstro < 1) danoCausadoAoMonstro = 1;

        int danoCausadoAoJogador = this.dano - (j.getDefesa() / 2);
        if (danoCausadoAoJogador < 0) danoCausadoAoJogador = 0;

        this.vida -= danoCausadoAoMonstro;
        j.adicionarVida(-danoCausadoAoJogador); // Usando adicionarVida para exibir a mudança

        System.out.println("╔════════════════════════════╗");
        System.out.println("║ RESULTADO DO COMBATE!");
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ Você causou " + danoCausadoAoMonstro + " de dano ao " + nome + ".");
        System.out.println("║ " + nome + " causou " + danoCausadoAoJogador + " de dano a você.");
        System.out.println("║ Vida do " + nome + ": " + this.vida);
        System.out.println("║ Sua Vida: " + j.getVida());
        System.out.println("╚════════════════════════════╝");

        if (this.vida <= 0) {
            System.out.println("╔════════════════════════════╗");
            System.out.println("║ " + nome + " DERROTADO!");
            System.out.println("╚════════════════════════════╝");
        } else if (j.getVida() > 0) {
            System.out.println("╔════════════════════════════╗");
            System.out.println("║ O " + nome + " fugiu para as sombras, ou você se esquivou!");
            System.out.println("║ Continue explorando, mas cuidado!");
            System.out.println("╚════════════════════════════╝");
        }
    }
}
