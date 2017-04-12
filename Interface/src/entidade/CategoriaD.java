/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidade;

import util.Data;

/**
 *
 * @author Roberto Honda
 */
public class CategoriaD extends Categoria{
    private double pesoBrutoTotalMaximo = 6000;
    
    public CategoriaD(Veiculo veiculo, boolean aprovado, Data dataAprovacao){
        super("Ser habilitado na categoria C por pelo menos 1 ano, ou no mínimo 2 anos na categoria B, ter mais de 21 anos, ser aprovado em exame de aptidão física e mental, Não ter sido multado por falta grave ou gravíssima nos últimos 12 meses, realizar curso prático de 15h/aulas e teste de direção veicular." , veiculo, 400.00, aprovado, dataAprovacao, "Permite dirigir todos das categorias B e C, e veículos de passageiros sem reboque com lotação maior que 8 pessoas. Não permite dirigir motos, triciclos, e veículos cujo reboque pese mais de 6.000 Kg (PBT), ou possua reboque com lotação maior que 8 pessoas.");
    }

    public double getPesoBrutoTotalMaximo() {
        return pesoBrutoTotalMaximo;
    }

    public void setPesoBrutoTotalMaximo(double pesoBrutoTotalMaximo) {
        this.pesoBrutoTotalMaximo = pesoBrutoTotalMaximo;
    }
    
    public String toString(){
        String saida = "Categoria: D";
        if(!requisitos.isEmpty())
            saida += "\nRequisitos: " + requisitos;
        saida += "\nVeículo: " + veiculo + "\nPreço: R$ "+ preco;
        if(aprovado)
            saida += "\nAprovado: Sim"+ "\nData Aprovação: "+ dataAprovacao.toString(); 
        else
            saida += "\nAprovado: Não";
        return saida += "\nInformação: " + informacao;
    }
    
}
