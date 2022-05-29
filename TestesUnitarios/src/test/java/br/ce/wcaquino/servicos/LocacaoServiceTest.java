package br.ce.wcaquino.servicos;

import br.ce.wcaquino.dao.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEstoqueExecption;
import br.ce.wcaquino.exceptions.LocadoraExecption;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.FilmeBuilder.umFilmeComZeroDeEsotoque;
import static br.ce.wcaquino.builder.LocacaoBuilder.umaLocacao;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;


public class LocacaoServiceTest {

    @InjectMocks @Spy
    private LocacaoService service;
    @Mock
    private SPCService spcService;
    @Mock
    private LocacaoDao dao;
    @Mock
    private EmailService emailService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public  void setup(){
        MockitoAnnotations.openMocks(this);
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
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = List.of(umFilme().agora());

        Mockito.doReturn(DataUtils.obterData(28,05,2022)).when(service).obterData();
        //ação
        Locacao retorno = service.alugarFilme(usuario,filmes);

        //verificação
        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);
    }

    @Test
    public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
        //Cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = List.of(umFilme().agora());

        Mockito.when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

        //ação
        try {
            service.alugarFilme(usuario, filmes);
            //verificacao
            Assert.fail("Deveria lançar uma exceção");
        } catch (LocadoraExecption e) {
            Assert.assertEquals("Usuário Negativado",e.getMessage());
        }

        Mockito.verify(spcService).possuiNegativacao(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas(){
        //cenário
        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().comNome("GG").agora();
        Usuario usuario3 = umUsuario().comNome("GGWP").agora();
        List<Locacao> locacoes = Arrays.asList(
                umaLocacao().atrasada().comUsuario(usuario).agora(),
                umaLocacao().comUsuario(usuario2).agora(),
                umaLocacao().atrasada().comUsuario(usuario3).agora(),
                umaLocacao().atrasada().comUsuario(usuario3).agora());

        Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

        //ação
        service.notificarAtrasos();

        //verificacao
        Mockito.verify(emailService, Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));
        Mockito.verify(emailService).notificarAtraso(usuario);
        Mockito.verify(emailService, Mockito.atLeastOnce()).notificarAtraso(usuario3);
        Mockito.verify(emailService, Mockito.never()).notificarAtraso(usuario2);
        Mockito.verifyNoMoreInteractions(emailService); // verifica se não houve mais nenhuma chamada com o mock além das que foram especificadas a cima
        Mockito.verifyNoInteractions(spcService); // verifica se o mock nunca foi chamado
    }

    @Test
    public void deveTratarErroNoSpc() throws Exception {
        //cenário
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());
        Mockito.when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Problema com SPC, tente novamente"));


        //verificação
        expectedException.expect(LocadoraExecption.class);
        expectedException.expectMessage("Problema com SPC, tente novamente");

        //ação
        service.alugarFilme(usuario,filmes);
    }


}
