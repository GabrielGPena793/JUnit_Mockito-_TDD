package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import  org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class LocacaoServiceTest {

    @Test
    public void test() {
        //cenário
        LocacaoService locacao = new LocacaoService();
        Usuario usuario = new Usuario("Gabriel");
        Filme filme = new Filme("Jujutso", 2, 30.5);

        //action
        Locacao locacao1 = locacao.alugarFilme(usuario,filme);

        //assert
        Assert.assertEquals(30.5, locacao1.getValor(), 0.01);
        Assert.assertTrue(DataUtils.isMesmaData(locacao1.getDataLocacao(),new Date()));
        Assert.assertTrue(DataUtils.isMesmaData(locacao1.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
    }
}
