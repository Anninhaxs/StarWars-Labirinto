abstract class Arma implements Tesouro {
    protected int ataqueBonus; // Alterado para protected
    protected String nome; // Alterado para protected
    protected String descricao; // Alterado para protected

    public Arma(int ataqueBonus, String nome, String descricao) {
        this.ataqueBonus = ataqueBonus;
        this.nome = nome;
        this.descricao = descricao;
    }

    @Override
    public void aplicarEfeito(Jogador j) {
        j.armaEquipada = this;
        j.ataque += ataqueBonus; // Acesso direto ao campo do jogador para modificar status
        System.out.println("╔════════════════════════════╗");
        System.out.println("║ ARMA ENCONTRADA!");
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ " + nome);
        System.out.println("║ " + descricao);
        System.out.println("║ Bônus de Ataque: +" + ataqueBonus);
        System.out.println("║ Ataque total: " + j.getAtaque());
        j.adicionarPontos(40);
        System.out.println("╚════════════════════════════╝");
    }
}