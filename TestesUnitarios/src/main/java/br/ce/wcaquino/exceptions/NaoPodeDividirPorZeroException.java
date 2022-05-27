package br.ce.wcaquino.exceptions;

public class NaoPodeDividirPorZeroException extends RuntimeException {

    public NaoPodeDividirPorZeroException(String message) {
        super(message);
    }
}
