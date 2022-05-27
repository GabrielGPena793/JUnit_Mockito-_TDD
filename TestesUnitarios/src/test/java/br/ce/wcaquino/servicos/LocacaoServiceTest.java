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

import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.FilmeBuilder.umFilmeComZeroDeEsotoque;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;

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
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().comPreco(30.5).agora(),
                umFilme().comPreco(30.5).agora());

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
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilmeComZeroDeEsotoque().agora(),
                umFilmeComZeroDeEsotoque().agora());

        //action
        Locacao locacao1 = service.alugarFilme(usuario,filmes);
    }

    @Test
    public void deveLançarExcecaoAoAlugarFilmeSemEstoque_Robusta(){
        //cenário
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilmeComZeroDeEsotoque().agora(),
                umFilmeComZeroDeEsotoque().agora());

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
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilmeComZeroDeEsotoque().agora(),
                umFilmeComZeroDeEsotoque().agora());

        expectedException.expect(Exception.class);
        expectedException.expectMessage("Filmes sem estoque");

        //action
        Locacao locacao1 = service.alugarFilme(usuario,filmes);
    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmesSemEstoqueExecption {
        //cenário
        List<Filme> filmes = Arrays.asList(umFilme().comPreco(30.5).agora(),
                umFilme().comPreco(30.5).agora());

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
        Usuario usuario = umUsuario().agora();

        //action
        try {
            Locacao locacao1 = service.alugarFilme(usuario,null);
            Assert.fail("Deveria ter lançado uma exceção");
        } catch (LocadoraExecption e) {
            Assert.assertEquals("Filme vazio", e.getMessage());
        }
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmesSemEstoqueExecption, LocadoraExecption {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = List.of(umFilme().agora());

        //ação
        Locacao retorno = service.alugarFilme(usuario,filmes);

        //verificação
        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataLocacao(), Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);
    }
}
