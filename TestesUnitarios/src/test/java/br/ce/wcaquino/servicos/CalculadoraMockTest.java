package br.ce.wcaquino.servicos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class CalculadoraMockTest {

    @Mock
    private Calculadora calcMock;
    @Spy
    private Calculadora calcSpy;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void devoMostrarDiferencaEntreMockSpy() {
        Mockito.when(calcMock.sum(1,2)).thenCallRealMethod();
        Mockito.when(calcSpy.sum(1,2)).thenReturn(8);

        System.out.println("Mock: " + calcMock.sum(1,2));
        System.out.println("Spy: " + calcSpy.sum(1,5));
    }

    @Test
    public void test(){
        Calculadora calc = Mockito.mock(Calculadora.class);
        Mockito.when(calc.sum(Mockito.anyInt(), Mockito.anyInt())).thenReturn(6);

        System.out.println(calc.sum(122,51111));
    }
}
