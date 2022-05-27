package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEstoqueExecption;
import br.ce.wcaquino.exceptions.LocadoraExecption;
import br.ce.wcaquino.utils.DataUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws LocadoraExecption, FilmesSemEstoqueExecption {

		if (usuario == null){
			throw new LocadoraExecption("Usuário vazio");
		}

		if (filmes == null || filmes.isEmpty()){
			throw new LocadoraExecption("Filme vazio");
		}

		if( filmes.stream().anyMatch(filme -> filme.getEstoque() == 0)){
			throw new FilmesSemEstoqueExecption("Filmes sem estoque");
		}

		Locacao locacao = new Locacao();
		locacao.setFilme(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		Double valorTotal = 0.0;
		for (int i = 0; i < filmes.size(); i++){
			switch (i){
				case 2: valorTotal += filmes.get(i).getPrecoLocacao() * 0.75; break;
				case 3: valorTotal += filmes.get(i).getPrecoLocacao() * 0.50; break;
				case 4: valorTotal += filmes.get(i).getPrecoLocacao() * 0.25; break;
				case 5: valorTotal += filmes.get(i).getPrecoLocacao() * 0.0; break;
				default: valorTotal += filmes.get(i).getPrecoLocacao();
			}
		}
		locacao.setValor(valorTotal);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)){
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}


}