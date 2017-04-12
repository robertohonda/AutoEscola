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
public class CategoriaA extends Categoria{
    public CategoriaA(Veiculo veiculo, boolean aprovado, Data dataAprovacao){
        super("Ser maior de 18 anos, ser aprovado em exame de aptidão física e mental, ser aprovado nos exames teórico e prático.", veiculo, 100.00, aprovado, dataAprovacao, "Pode conduzir veículos de 2 ou 3 rodas, com ou sem carro lateral como motocicletas, ciclomotores, motonetas, triciclos. Não permite dirigir nenhum outro tipo de veículo.");
    }
    
    public String toString(){
        String saida = "Categoria: A";
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
