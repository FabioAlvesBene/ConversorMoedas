package br.com.fabioalvesbene.conversormoedas.servicos;

import br.com.fabioalvesbene.conversormoedas.modelo.Transacao;
import com.google.gson.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConversorMoedas {
    private static final String API_KEY = "SUA_CHAVE_AQUI";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public double converterMoeda(String origem, String destino, double valor) throws IOException {
        String urlString = API_URL + origem;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Usa Gson para converter JSON diretamente em um objeto Java
        Gson gson = new Gson();
        try (InputStreamReader leitura = new InputStreamReader(conn.getInputStream())) {
            ExchangeRateResponse resposta = gson.fromJson(leitura, ExchangeRateResponse.class);

            if (resposta.conversion_rates != null && resposta.conversion_rates.containsKey(destino)) {
                double taxa = resposta.conversion_rates.get(destino);
                double valorConvertido = valor * taxa;
                registraTransacao(origem, destino, valor, valorConvertido);
                return valorConvertido;
            } else {
                throw new IOException("Moeda de destino não encontrada na resposta.");
            }
        }
    }

    private void registraTransacao(String origem, String destino, double valor, double valorConvertido) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).setPrettyPrinting().create();
        Transacao novaTransacao = new Transacao(origem, destino, valor, valorConvertido);
        List<Transacao> transacoes = new ArrayList<>();

        File arquivo = new File("conversao_log.json");

        // Se o arquivo existir, tenta carregar as transações existentes
        if (arquivo.exists()) {
            try (Reader reader = new FileReader(arquivo)) {
                JsonElement elemento = JsonParser.parseReader(reader);
                if (elemento.isJsonArray()) {
                    Transacao[] transacoesExistentes = gson.fromJson(elemento, Transacao[].class);
                    transacoes.addAll(Arrays.asList(transacoesExistentes));
                } else {
                    // O arquivo não está em formato de array, pode ignorar ou limpar
                    System.out.println("Formato inválido de JSON. Ignorando conteúdo antigo.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Adiciona a nova transação
        transacoes.add(novaTransacao);

        // Escreve o array atualizado no arquivo
        try (Writer writer = new FileWriter(arquivo)) {
            gson.toJson(transacoes, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Classe interna ou separada para mapear a resposta da API
    private static class ExchangeRateResponse {
        public java.util.Map<String, Double> conversion_rates;
    }
}