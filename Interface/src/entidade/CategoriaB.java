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
public class CategoriaB extends Categoria{
    private int lotacaoMaxima = 9;
    private double pesoBrutoTotalMaximo = 3500;
    
    public CategoriaB(Veiculo veiculo, boolean aprovado, Data dataAprovacao){
        super("Ser maior de 18 anos, ser aprovado em exame de aptidão física e mental, ser aprovado nos exames teórico e prático" , veiculo, 200.0, aprovado, dataAprovacao, "Pode conduzir veículos com ou sem reboque, com peso bruto total (PBT) de 3.500 kg e lotação máxima de 8 pessoas, fora o condutor. Não permite dirigir motos, triciclos, veículos com mais de 3.500 kg de peso bruto ou com mais de nove lugares, incluindo o do condutor.");
    }

    public int getLotacaoMaxima(){
        return lotacaoMaxima;
    }

    public void setLotacaoMaxima(int lotacaoMaxima) {
        this.lotacaoMaxima = lotacaoMaxima;
    }

    public double getPesoBrutoTotalMaximo() {
        return pesoBrutoTotalMaximo;
    }

    public void setPesoBrutoTotalMaximo(double pesoBrutoTotalMaximo) {
        this.pesoBrutoTotalMaximo = pesoBrutoTotalMaximo;
    }
    
    public String toString(){
        String saida = "Categoria: B";
        if(!requisitos.isEmpty())
            saida += "\nRequisitos: " + requisitos;
        saida += "\nVeículo: " + veiculo+"\nPreço: R$ "+ preco;
        if(aprovado)
            saida += "\nAprovado: Sim"+ "\nData Aprovação: "+ dataAprovacao.toString(); 
        else
            saida += "\nAprovado: Não";
        return saida += "\nInformação: " + informacao;
    }
    
}
