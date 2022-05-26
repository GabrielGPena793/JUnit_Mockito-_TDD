package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import  org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;

public class LocacaoServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void test() throws Exception {
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

    @Test(expected = Exception.class)
    public void testLocacao_filmeSemEstoque_Elegante() throws Exception {
        //cenário
        LocacaoService locacao = new LocacaoService();
        Usuario usuario = new Usuario("Gabriel");
        Filme filme = new Filme("Jujutso", 0, 30.5);

        //action
        Locacao locacao1 = locacao.alugarFilme(usuario,filme);
    }

    @Test
    public void testLocacao_filmeSem_EstoqueRobusta(){
        //cenário
        LocacaoService locacao = new LocacaoService();
        Usuario usuario = new Usuario("Gabriel");
        Filme filme = new Filme("Jujutso", 0, 30.5);

        //action
        try {
            Locacao locacao1 = locacao.alugarFilme(usuario,filme);
            Assert.fail("Deveria ter lançado uma exceção");
        } catch (Exception e) {
            Assert.assertEquals("Filmes sem estoque", e.getMessage());
        }
    }

    @Test
    public void   testLocacao_filmeSemEstoque_FormaNova() throws Exception {
        //cenário
        LocacaoService locacao = new LocacaoService();
        Usuario usuario = new Usuario("Gabriel");
        Filme filme = new Filme("Jujutso", 0, 30.5);

        expectedException.expect(Exception.class);
        expectedException.expectMessage("Filmes sem estoque");

        //action
        Locacao locacao1 = locacao.alugarFilme(usuario,filme);
    }

}
