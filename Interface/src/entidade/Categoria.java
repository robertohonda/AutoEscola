package entidade;

import java.util.ArrayList;
import util.Data;

public class Categoria {
    public enum Veiculo{moto, triciclo, carro, caminhão, ônibus, treminhão};
    protected String  requisitos;
    protected Veiculo veiculo;
    protected double  preco;
    protected boolean aprovado;
    protected Data    dataAprovacao;
    protected String  informacao;
    
    public Categoria(String requisitos, Veiculo veiculo, double preco, boolean aprovado, Data dataAprovacao, String informacao){
        this.requisitos    = requisitos;
        this.veiculo       = veiculo;
        this.preco         = preco;
        this.aprovado      = aprovado;
        this.dataAprovacao = dataAprovacao;
        this.informacao    = informacao;
    }
    /*
    public static ArrayList<Categoria> verificarCategoriasVeiculo(Veiculo veiculo){
        ArrayList<Categoria> resultado = new ArrayList();
        if(veiculo.getRodas()<=3)
        {
             resultado.add(new CategoriaA(veiculo, false, null));
             return resultado;
        }
        if(veiculo.getLotacao()<=9 && veiculo.getPesoTotalBruto()<=3500)
            resultado.add(new CategoriaB(veiculo, false, null));
        if(veiculo.getLotacao()<=9 && veiculo.getPesoTotalBruto()<=6000)
            resultado.add(new CategoriaC(veiculo, false, null));
        if(veiculo.getPesoTotalBruto()<=6000)
            resultado.add(new CategoriaD(veiculo, false, null));
        resultado.add(new CategoriaE(veiculo, false, null));
        return resultado;
    }
    
    public static Categoria verificarCategoriaVeiculo(Veiculo veiculo)
    {
        if(veiculo.getRodas()<=3)
            return new CategoriaA(veiculo, false, null);
        if(veiculo.getLotacao()<=9 && veiculo.getPesoTotalBruto()<=3500)
            return new CategoriaB(veiculo, false, null);
        if(veiculo.getLotacao()<=9 && veiculo.getPesoTotalBruto()<=6000)
            return new CategoriaC(veiculo, false, null);
        if(veiculo.getLotacao()>9 && veiculo.getPesoTotalBruto()<=6000)
            return new CategoriaD(veiculo, false, null);
        return new CategoriaE(veiculo, false, null);
    }
*/

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public double getPreco() {
        return preco;
    }

    public boolean getAprovado() {
        return aprovado;
    }

    public Data getDataAprovacao() {
        return dataAprovacao;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }

    public void setDataAprovacao(Data dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }
    
    public String toString(){
        String saida = "";
        if(!requisitos.isEmpty())
            saida = "Requisitos: "+requisitos + "\n";
        saida += "Veículo: " + veiculo + "\nPreço: R$ "+ preco;
        if(aprovado)
            saida += "\nAprovado: Sim"+ "\nData Aprovação: "+ dataAprovacao.toString();
        else
            saida += "\nAprovado: Não";
        return saida + "\nInformação: " + informacao;
    }
    
    
}
