package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculadoraTest {

    private Calculadora calc;

    @Before
    public void setup(){
        calc = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores(){
        //Cenário
        int a = 5;
        int b = 5;

        //Ação
        int resultado = calc.sum(a, b);

        //Verificação
        Assert.assertEquals(10, resultado);
    }

    @Test
    public void deveSubtrairDoisValores(){
        //Cenário
        int a = 5;
        int b = 5;

        //Ação
        int resultado = calc.sub(a, b);

        //Verificação
        Assert.assertEquals(0, resultado);
    }

    @Test
    public void deveDividirDoisValores(){
        //Cenário
        int a = 6;
        int b = 3;

        //Ação
        int resultado = calc.div(a, b);

        //Verificação
        Assert.assertEquals(2, resultado);
    }

    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void deveLançarExceçãoAoDividirPorZero(){
        //Cenário
        int a = 6;
        int b = 0;

        //Ação
        int resultado = calc.div(a, b);
    }
}
