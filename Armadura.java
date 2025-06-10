abstract class Armadura implements Tesouro {
    protected int defesaBonus; // Alterado para protected
    protected String nome; // Alterado para protected
    protected String descricao; // Alterado para protected

    public Armadura(int defesaBonus, String nome, String descricao) {
        this.defesaBonus = defesaBonus;
        this.nome = nome;
        this.descricao = descricao;
    }

    @Override
    public void aplicarEfeito(Jogador j) {
        j.armaduraEquipada = this;
        j.defesa += defesaBonus; // Acesso direto ao campo do jogador para modificar status
        System.out.println("╔════════════════════════════╗");
        System.out.println("║ ARMADURA ENCONTRADA!");
        System.out.println("╠════════════════════════════╣");
        System.out.println("║ " + nome);
        System.out.println("║ " + descricao);
        System.out.println("║ Bônus de Defesa: +" + defesaBonus);
        System.out.println("║ Defesa total: " + j.getDefesa());
        j.adicionarPontos(35);
        System.out.println("╚════════════════════════════╝");
    }
}
