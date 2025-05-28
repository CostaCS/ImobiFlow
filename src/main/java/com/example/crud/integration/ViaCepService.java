package com.example.crud.integration;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ViaCepService {

    private final WebClient client;

    public ViaCepService(WebClient.Builder builder) {
        this.client = builder
                .baseUrl("https://viacep.com.br")
                .build();
    }

    public com.example.crud.integration.ViaCepResponse buscarPorCep(String cep) {
        String somenteDigitos = cep.replaceAll("\\D", "");
        com.example.crud.integration.ViaCepResponse resp = client.get()
                .uri("/ws/{cep}/json/", somenteDigitos)
                .retrieve()
                .bodyToMono(com.example.crud.integration.ViaCepResponse.class)
                .block();

        if (resp == null || Boolean.TRUE.equals(resp.getErro())) {
            return null;
        }
        return resp;
    }
}
