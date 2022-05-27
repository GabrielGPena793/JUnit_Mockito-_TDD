package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEstoqueExecption;
import br.ce.wcaquino.exceptions.LocadoraExecption;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LocacaoServiceTest {

    private LocacaoService service;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public  void setup(){
        service = new LocacaoService();
    }

    @Test
    public void deveAlugarFilmeComSucesso() throws Exception {
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenário
        Usuario usuario = new Usuario("Gabriel");
        List<Filme> filmes = Arrays.asList(new Filme("Jujutso", 2, 30.5),
                new Filme("Jujutso", 2, 30.5));

        //action
        Locacao locacao1 = service.alugarFilme(usuario,filmes);

        //assert
        Assert.assertEquals(61.0, locacao1.getValor(), 0.01);
        Assert.assertTrue(DataUtils.isMesmaData(locacao1.getDataLocacao(),new Date()));
        Assert.assertTrue(DataUtils.isMesmaData(locacao1.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
    }

    @Test(expected = FilmesSemEstoqueExecption.class)
    public void deveLançarExcecaoAoAlugarFilmeSemEstoque() throws Exception {
        //cenário
        Usuario usuario = new Usuario("Gabriel");
        List<Filme> filmes = Arrays.asList(new Filme("Jujutso", 0, 30.5),
                new Filme("Jujutso", 0, 30.5));

        //action
        Locacao locacao1 = service.alugarFilme(usuario,filmes);
    }

    @Test
    public void deveLançarExcecaoAoAlugarFilmeSemEstoque_Robusta(){
        //cenário
        Usuario usuario = new Usuario("Gabriel");
        List<Filme> filmes = Arrays.asList(new Filme("Jujutso", 0, 30.5),
                new Filme("Jujutso", 0, 30.5));


        //action
        try {
            Locacao locacao1 = service.alugarFilme(usuario,filmes);
            Assert.fail("Deveria ter lançado uma exceção");
        } catch (Exception e) {
            Assert.assertEquals("Filmes sem estoque", e.getMessage());
        }
    }

    @Test
    public void deveLançarExcecaoAoAlugarFilmeSemEstoque_FormaNova() throws Exception {
        //cenário
        Usuario usuario = new Usuario("Gabriel");
        List<Filme> filmes = Arrays.asList(new Filme("Jujutso", 0, 30.5),
                new Filme("Jujutso", 0, 30.5));

        expectedException.expect(Exception.class);
        expectedException.expectMessage("Filmes sem estoque");

        //action
        Locacao locacao1 = service.alugarFilme(usuario,filmes);
    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmesSemEstoqueExecption {
        //cenário
        List<Filme> filmes = Arrays.asList(new Filme("Jujutso", 2, 30.5),
                new Filme("Jujutso", 2, 30.5));


        //action
        try {
            Locacao locacao1 = service.alugarFilme(null,filmes);
            Assert.fail("Deveria ter lançado uma exceção");
        } catch (LocadoraExecption e) {
            Assert.assertEquals("Usuário vazio", e.getMessage());
        }
    }


    @Test
    public void naoDeveAlugarFilmeSemFilme() throws FilmesSemEstoqueExecption {
        //cenário
        Usuario usuario = new Usuario("Gabriel");

        //action
        try {
            Locacao locacao1 = service.alugarFilme(usuario,null);
            Assert.fail("Deveria ter lançado uma exceção");
        } catch (LocadoraExecption e) {
            Assert.assertEquals("Filme vazio", e.getMessage());
        }
    }


    @Test
    public void devePagar75PorcentoNoFilme3() throws FilmesSemEstoqueExecption, LocadoraExecption {
        //cenário
        Usuario usuario = new Usuario("Gabriel");
        List<Filme> filmes = Arrays.asList(new Filme("Narnia", 1, 4.0),
                new Filme("Narnia", 1, 4.0),new Filme("Narnia", 1, 4.0));
        //ação
        Locacao resultado = service.alugarFilme(usuario,filmes);

        //verificação
        Assert.assertEquals(11, resultado.getValor(), 0.01);
    }

    @Test
    public void devePagar50PorcentoNoFilme4() throws FilmesSemEstoqueExecption, LocadoraExecption {
        //cenário
        Usuario usuario = new Usuario("Gabriel");
        List<Filme> filmes = Arrays.asList(new Filme("Narnia", 1, 4.0),
                new Filme("Narnia", 1, 4.0),new Filme("Narnia", 1, 4.0),
                new Filme("Narnia", 1, 4.0));
        //ação
        Locacao resultado = service.alugarFilme(usuario,filmes);

        //verificação
        Assert.assertEquals(13, resultado.getValor(), 0.01);
    }

    @Test
    public void devePagar25PorcentoNoFilme5() throws FilmesSemEstoqueExecption, LocadoraExecption {
        //cenário
        Usuario usuario = new Usuario("Gabriel");
        List<Filme> filmes = Arrays.asList(new Filme("Narnia", 1, 4.0),
                new Filme("Narnia", 1, 4.0),new Filme("Narnia", 1, 4.0),
                new Filme("Narnia", 1, 4.0), new Filme("Narnia", 1, 4.0));
        //ação
        Locacao resultado = service.alugarFilme(usuario,filmes);

        //verificação
        Assert.assertEquals(14, resultado.getValor(), 0.01);
    }

    @Test
    public void devePagarZeroNoFilme6() throws FilmesSemEstoqueExecption, LocadoraExecption {
        //cenário
        Usuario usuario = new Usuario("Gabriel");
        List<Filme> filmes = Arrays.asList(new Filme("Narnia", 1, 4.0),
                new Filme("Narnia", 1, 4.0),new Filme("Narnia", 1, 4.0),
                new Filme("Narnia", 1, 4.0), new Filme("Narnia", 1, 4.0),
                new Filme("Narnia", 1, 4.0));
        //ação
        Locacao resultado = service.alugarFilme(usuario,filmes);

        //verificação
        Assert.assertEquals(14, resultado.getValor(), 0.01);
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmesSemEstoqueExecption, LocadoraExecption {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Gabriel");
        List<Filme> filmes = List.of(new Filme("Narnia", 1, 4.0));

        //ação
        Locacao retorno = service.alugarFilme(usuario,filmes);

        //verificação
        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataLocacao(), Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);
    }
}
