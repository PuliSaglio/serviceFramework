package br.com.serviceframework.domain.entity;

import java.math.BigDecimal;

import br.com.serviceframework.domain.converter.CategoriaConverter;
import br.com.serviceframework.domain.interfaces.ICategoriaServicos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Id;

@Entity
public class Servico {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column(length = 300)
    private String descricao;

    @Column
    private BigDecimal precoBase;

    @Column
    private String imagemUrl;

    @Column
    @Convert(converter = CategoriaConverter.class)
    private ICategoriaServicos categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestador_id")
    @JsonIgnore
    private Prestador prestador;

    @ManyToOne
    @JoinColumn(name = "recomendacoes_cliente_id")
    private RecomendacoesCliente recomendacoesCliente;


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(BigDecimal precoBase) {
        this.precoBase = precoBase;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public Prestador getPrestador() {
        return prestador;
    }

    public void setPrestador(Prestador prestador) {
        this.prestador = prestador;
    }

    public ICategoriaServicos getCategoria() {
        return categoria;
    }

    public void setCategoria(ICategoriaServicos categoria) {
        this.categoria = categoria;
    }

    public RecomendacoesCliente getRecomendacoesCliente() {
        return recomendacoesCliente;
    }

    public void setRecomendacoesCliente(RecomendacoesCliente recomendacoesCliente) {
        this.recomendacoesCliente = recomendacoesCliente;
    }
}
