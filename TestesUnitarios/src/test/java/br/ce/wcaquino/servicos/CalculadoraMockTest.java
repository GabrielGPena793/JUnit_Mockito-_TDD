package br.ce.wcaquino.servicos;

import org.junit.Test;
import org.mockito.Mockito;

public class CalculadoraMockTest {

    @Test
    public void test(){
        Calculadora calc = Mockito.mock(Calculadora.class);
        Mockito.when(calc.sum(Mockito.anyInt(), Mockito.anyInt())).thenReturn(6);

        System.out.println(calc.sum(122,51111));
    }
}
