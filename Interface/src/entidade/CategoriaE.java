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
public class CategoriaE extends Categoria{
    public CategoriaE(Categoria.Veiculo veiculo, boolean aprovado, Data dataAprovacao){
        super("Estar habilitado há pelo menos 1 ano na categoria C, ser aprovado no exame de aptidão física e mental, não ter sido multado por falta grave ou gravíssima, nem ser reincidente em multa por falta média nos últimos 12 meses, não estar cumprindo pena de suspensão de direito de dirigir ou cassação da CNH, decorrente de crime de trânsito, realizar curso prático de 15 horas/aulas e teste de direção veicular." , veiculo, 500.00, aprovado, dataAprovacao, "Permite conduzir todos os veículos das categorias B, C e D, trailers e veículos rebocando unidades com mais de 6.000 Kg (PBT) ou com lotação maior que 8 passageiros. É a única categoria que permite conduzir veículos com mais de um reboque. Não permite dirigir motos, triciclos e ciclomotores.");
    }
    
    public String toString(){
        String saida = "Categoria: E";
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
