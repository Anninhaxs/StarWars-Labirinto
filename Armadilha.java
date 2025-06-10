abstract class Armadilha implements Perigo {
    protected int dano; // Alterado para protected
    protected String nome; // Alterado para protected
    protected String descricao; // Alterado para protected

    public Armadilha(int dano, String nome, String descricao) {
        this.dano = dano;
        this.nome = nome;
        this.descricao = descricao;
    }
}