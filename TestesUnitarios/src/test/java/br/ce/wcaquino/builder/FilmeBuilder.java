package br.ce.wcaquino.builder;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {

    private Filme filme;

    private FilmeBuilder(){};

    public static FilmeBuilder umFilme(){
        FilmeBuilder filmeBuilder = new FilmeBuilder();
        filmeBuilder.filme =  new Filme("Narnia", 1, 4.0);
        return  filmeBuilder;
    }

    //Contructor no padrão Obeject Model
    public static FilmeBuilder umFilmeComZeroDeEsotoque(){
        FilmeBuilder filmeBuilder = new FilmeBuilder();
        filmeBuilder.filme =  new Filme("Narnia", 0, 4.0);
        return  filmeBuilder;
    }

    public Filme agora(){
        return filme;
    }

    public FilmeBuilder comPreco(Double valor){
        filme.setPrecoLocacao(valor);
        return this;
    }

    public FilmeBuilder comEstoque(Integer estoque){
        filme.setEstoque(estoque);
        return this;
    }
}
