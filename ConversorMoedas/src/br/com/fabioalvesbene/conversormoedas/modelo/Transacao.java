package br.com.fabioalvesbene.conversormoedas.modelo;

public class Transacao {
    String origem;
    String destino;
    double valorOriginal;
    double valorConvertido;

    public Transacao(String origem, String destino, double valorOriginal, double valorConvertido) {
        this.origem = origem;
        this.destino = destino;
        this.valorOriginal = valorOriginal;
        this.valorConvertido = valorConvertido;
    }
}
