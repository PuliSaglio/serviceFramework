package br.com.serviceframework.chatbot.model;

import java.time.LocalDateTime;

public class UserState {

    private EtapaConversa etapaAtual;
    private String chatId;
    private LocalDateTime lastUpdated;
    private String categoriaServico;
    private String nomeServico;
    private String dadosConversa;

    public UserState(EtapaConversa etapaAtual) {
        this.etapaAtual = etapaAtual;
        this.lastUpdated = LocalDateTime.now();
    }


    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCategoriaServico() {
        return categoriaServico;
    }

    public void setCategoriaServico(String categoriaServico) {
        this.categoriaServico = categoriaServico;
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }

    public EtapaConversa getEtapaAtual() {
        return etapaAtual;
    }

    public void setEtapaAtual(EtapaConversa etapaAtual) {
        this.etapaAtual = etapaAtual;
    }

    public String getDadosConversa() {
        return dadosConversa;
    }

    public void setDadosConversa(String dadosConversa) {
        this.dadosConversa = dadosConversa;
    }
}