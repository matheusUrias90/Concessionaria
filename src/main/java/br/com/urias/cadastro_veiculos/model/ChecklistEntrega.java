package br.com.urias.cadastro_veiculos.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ChecklistEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Veiculo veiculo;

    @OneToMany(mappedBy = "checklist", 
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ItemChecklistEntrega> itens = new ArrayList<>();
    
    private boolean entregaConcluida = false;
    
    private LocalDate dataEntrega;
    
   public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public List<ItemChecklistEntrega> getItens() {
        return itens;
    }

    public void setItens(List<ItemChecklistEntrega> itens) {
        this.itens = itens;
    }
    
    public boolean isEntregaConcluida() {
        return entregaConcluida;
    }

    public void setEntregaConcluida(boolean entregaConcluida) {
        this.entregaConcluida = entregaConcluida;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }
    
    
}