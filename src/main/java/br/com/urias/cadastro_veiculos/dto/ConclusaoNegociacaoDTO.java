package br.com.urias.cadastro_veiculos.dto;

import java.util.ArrayList;
import java.util.List;

public class ConclusaoNegociacaoDTO {

    private List<VeiculoEntradaCadastroDTO> veiculosEntrada
            = new ArrayList<>();

    public List<VeiculoEntradaCadastroDTO> getVeiculosEntrada() {
        return veiculosEntrada;
    }

    public void setVeiculosEntrada(
            List<VeiculoEntradaCadastroDTO> veiculosEntrada) {

        this.veiculosEntrada = veiculosEntrada;
    }
}