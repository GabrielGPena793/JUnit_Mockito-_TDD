package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;

import br.ce.wcaquino.dao.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEstoqueExecption;
import br.ce.wcaquino.exceptions.LocadoraExecption;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTeste {

    @InjectMocks
    private LocacaoService service;
    @Mock
    private LocacaoDao dao;
    @Mock
    private SPCService spcService;
    @Parameterized.Parameter            //paramentros que vamos usar precisamos definir
    public List<Filme> filmes;
    @Parameterized.Parameter(value = 1) //paramentros que vamos usar precisamos definir
    public Double valorLocacao;

    @Parameterized.Parameter(value = 2) //paramentros que vamos usar precisamos definir
    public String cenario;

    @Before
    public  void setup(){
        MockitoAnnotations.initMocks(this);
    }

    private static Filme filme1 =  umFilme().agora();
    private static Filme filme2 = umFilme().agora();
    private static Filme filme3 = umFilme().agora();
    private static Filme filme4 = umFilme().agora();
    private static Filme filme5 = umFilme().agora();
    private static Filme filme6 = new Filme("Narnia", 1, 4.0);

    @Parameterized.Parameters(name = "{2}") // collection que vai rodar a quatidade de vezes que for o tamanho da collection
    public static Collection<Object[]> getParamentros(){
        return Arrays.asList(new Object[][]{
                {Arrays.asList(filme1, filme2, filme3), 11.0, "3 filmes 25%"},
                {Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 filmes 50%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 filmes 75%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0,"3 filmes 100%"}
        });
    }

    @Test
    public void deveCalcularOvalorDaLocacaoConsiderandoDescontos() throws FilmesSemEstoqueExecption, LocadoraExecption {
        //cenário
        Usuario usuario = umUsuario().agora();

        //ação
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificação
        Assert.assertEquals(valorLocacao, resultado.getValor(), 0.01);
    }
}
