abstract class Preciosidades implements Tesouro {
    protected int valor; // Alterado para protected
    protected String nome; // Alterado para protected
    protected String descricao; // Alterado para protected

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