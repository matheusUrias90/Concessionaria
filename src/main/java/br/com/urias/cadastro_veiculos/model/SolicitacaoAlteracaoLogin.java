package br.com.urias.cadastro_veiculos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class SolicitacaoAlteracaoLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String loginAtual;

    private String novoLogin;

    private LocalDateTime dataSolicitacao;

    @Enumerated(EnumType.STRING)
    private StatusSolicitacaoLogin status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getLoginAtual() {
        return loginAtual;
    }

    public void setLoginAtual(String loginAtual) {
        this.loginAtual = loginAtual;
    }

    public String getNovoLogin() {
        return novoLogin;
    }

    public void setNovoLogin(String novoLogin) {
        this.novoLogin = novoLogin;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public StatusSolicitacaoLogin getStatus() {
        return status;
    }

    public void setStatus(StatusSolicitacaoLogin status) {
        this.status = status;
    }
}