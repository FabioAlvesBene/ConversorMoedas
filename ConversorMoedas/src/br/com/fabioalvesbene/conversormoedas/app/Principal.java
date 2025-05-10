package br.com.fabioalvesbene.conversormoedas.app;

import br.com.fabioalvesbene.conversormoedas.modelo.Moeda;
import br.com.fabioalvesbene.conversormoedas.servicos.ConversorMoedas;

import java.io.IOException;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        ConversorMoedas converter = new ConversorMoedas();
        Moeda[] moedas = {
                new Moeda("USD", "Dólar dos EUA"),
                new Moeda("EUR", "Euro"),
                new Moeda("JPY", "Iene Japonês"),
                new Moeda("BOB", "Boliviano da Bolivia"),
                new Moeda("BRL", "Real Brasileiro"),
                new Moeda("ARS", "Peso Argentino")
        };

        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Exibir as opções de moedas
            System.out.println("Selecione a moeda de origem (ou digite 'sair' para encerrar):\n");
            for (int i = 0; i < moedas.length; i++) {
                System.out.printf("%d: %s (%s)%n", i + 1, moedas[i].getNome(), moedas[i].getCodigo());
            }

            // Lê a moeda de origem
            System.out.print("Digite o número da moeda de origem:\n ");

            String inputOrigem = scanner.next();
            if (inputOrigem.equalsIgnoreCase("sair")) break;

            int leMoedaOrigem;
            try {
                leMoedaOrigem = Integer.parseInt(inputOrigem) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite um número ou 'sair'.");
                continue;
            }

            // Exibir as opções de conversão
            System.out.println("Selecione a moeda de destino (ou digite 'sair' para encerrar):\n");
            for (int i = 0; i < moedas.length; i++) {
                System.out.printf("%d: %s (%s)%n", i + 1, moedas[i].getNome(), moedas[i].getCodigo());
            }

            // Lê a moeda de destino
            System.out.print("Digite o número da moeda de destino:\n ");
            String inputDestino = scanner.next();
            if (inputDestino.equalsIgnoreCase("sair")) break; // sair do loop se a opção "sair" for selecionada
            int leMoedaDestino = Integer.parseInt(inputDestino) - 1; // reduzir 1 para índice do array

            // Lê o valor a ser convertido
            System.out.print("Digite o valor a ser convertido (ou digite 'sair' para encerrar):\n ");
            String entradaValor = scanner.next();
            if (entradaValor.equalsIgnoreCase("sair")) break; // sair do loop se a opção "sair" for selecionada
            double valorConverter = Double.parseDouble(entradaValor);

            // Realiza a conversão
            try {
                double valorConvertido = converter.converterMoeda(moedas[leMoedaOrigem].getCodigo(), moedas[leMoedaDestino].getCodigo(), valorConverter);
                System.out.printf("Valor convertido: %.2f %s%n", valorConvertido, moedas[leMoedaDestino].getCodigo());
            } catch (IOException e) {
                System.out.println("Erro ao fazer a conversão: " + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Seleção de moeda inválida!");
            }
        }

        // Fechar o scanner apenas ao sair do loop
        scanner.close();
        System.out.println("Programa encerrado.");
    }
}