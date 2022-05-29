package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class Calculadora {

    public Calculadora() {
    }

    public int sum(int a, int b) {
        return a + b;
    }

    public int sub(int a, int b) {
        return a - b;
    }

    public int div(int a, int b) {

        if (b == 0){
            throw new NaoPodeDividirPorZeroException("Não pode dividir por zero");
        }
        return a / b ;
    }
}
