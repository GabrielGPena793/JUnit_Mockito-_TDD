package br.ce.wcaquino.dao;

import br.ce.wcaquino.entidades.Locacao;

import java.util.List;

public interface LocacaoDao {

    void salvar(Locacao locacao);

    List<Locacao> obterLocacoesPendentes();
}
