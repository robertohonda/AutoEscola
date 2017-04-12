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
public class CategoriaC extends Categoria{
    private int lotacaoMaxima = 9;
    private double pesoBrutoTotalMaximo = 6000;
    
    public CategoriaC(Veiculo veiculo, boolean aprovado, Data dataAprovacao){
        super("É necessário pelo menos 1 ano de habilitação na categoria B, não ter sido multado por falta grave ou gravíssima, nem ser reincidente em uma multa por falta média nos últimos 12 meses, ser aprovado em exame de aptidão física e mental, realizar curso prático de 15 horas/aulas e teste de direção veicular." , veiculo, 300.00, aprovado, dataAprovacao, "Permite dirigir todos da categoria B, tratores, máquinas agrícolas e veículos de carga com mais de 3.500 Kg (PBT) com ou sem reboque, desde que o reboque pese menos de 6.000 Kg (PBT). Não permite dirigir motos, triciclos e veículos de passageiros com mais de nove lugares, incluindo o condutor.");
    }

    public int getLotacaoMaxima() {
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
        String saida = "Categoria: C";
        if(!requisitos.isEmpty())
            saida += "\nRequisitos: " + requisitos;
        saida += "\nVeículo: " + veiculo+ "\nPreço: R$ "+ preco;
        if(aprovado)
            saida += "\nAprovado: Sim"+ "\nData Aprovação: "+ dataAprovacao.toString(); 
        else
            saida += "\nAprovado: Não";
        return saida += "\nInformação: " + informacao;
    }
    
}
