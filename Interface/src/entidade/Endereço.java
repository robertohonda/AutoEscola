package entidade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import persistência.BD;

public class Endereço {
    
    
    public static String inserirEndereço(Endereço endereço){
        String sql = "INSERT INTO Endereços (Sequencial, Logradouro, Complemento, Bairro, Número, CEP, Cidade, UF)"
                + " VALUES (DEFAULT,?,?,?,?,?,?,?)";
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(1, endereço.getLogradouro());
            comando.setString(2, endereço.getComplemento());
            comando.setString(3, endereço.getBairro());
            comando.setInt(4, endereço.getNúmero());
            comando.setString(5, endereço.getCEP());
            comando.setString(6, endereço.getCidade());
            comando.setInt(7, endereço.getUF().ordinal());
            comando.executeUpdate();
            comando.close();
            return null;
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            return "Erro na Inserção do Endereço no BD";
        }
    }
    
    public static String removerEndereço(int sequencial){
        String sql = "DELETE FROM Endereços WHERE Sequencial = ?";
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(1, ""+sequencial);
            comando.executeUpdate();
            comando.close();
            return null;
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            return "Erro na Remoção do Endereço no BD";
        }
    }
    
    public static String alterarEndereço(Endereço endereço, int endereçoId) {
        String sql = "UPDATE Endereços SET Logradouro = ?, Complemento = ?, Bairro = ?, Número = ?, CEP = ?, Cidade = ?, UF = ?"
                + " WHERE Sequencial = ?";
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(1, endereço.getLogradouro());
            comando.setString(2, endereço.getComplemento());
            comando.setString(3, endereço.getBairro());
            comando.setInt(4, endereço.getNúmero());
            comando.setString(5, endereço.getCEP());
            comando.setString(6, endereço.getCidade());
            comando.setInt(7, endereço.getUF().ordinal());
            comando.setInt(8, endereçoId);
            comando.executeUpdate();
            comando.close();
            return null;
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            return "Erro na Alteração do Endereço no BD";
        }
    }
    
    public static Endereço buscarEndereço(int endereçoId) {
        String sql = "SELECT Logradouro, Complemento, Bairro, Número, CEP, Cidade, UF FROM Endereços"
                + " WHERE Sequencial = ?";
        ResultSet lista_resultados = null;
        Endereço endereço = null;
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(1, ""+endereçoId);
            lista_resultados = comando.executeQuery();
            while (lista_resultados.next()) {
                endereço = new Endereço(lista_resultados.getString("Logradouro"),
                        lista_resultados.getInt("Número"),
                        lista_resultados.getString("Complemento"),
                        lista_resultados.getString("Bairro"),
                        lista_resultados.getString("Cidade"),
                        lista_resultados.getString("CEP"),
                        estados[lista_resultados.getInt("UF")]);
            }
            lista_resultados.close();
            comando.close();
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            endereço = null;
        }
        return endereço;
    }
    
    public static int últimoSequencial() {
        
        String sql = "SELECT MAX(Sequencial) FROM Endereços";

        int result;

        ResultSet listaResultados;

        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);

            listaResultados = comando.executeQuery();

            if (listaResultados.first()) {
                result = listaResultados.getInt("MAX(Sequencial)");
            } else {
                result = -1;
            }

            listaResultados.close();
            comando.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            result = -1;
        }

        return result;
    }

    public enum UF{AC, AL, AP, AM, BA, CE, DF, ES, GO, MA, MT, MS, MG, PA, PB, 
    PR, PE, PI, RJ, RN, RS, RO, RR, SC, SP, SE, TO};
    
    private String logradouro, complemento, bairro, cidade, CEP;
    private UF estado;
    
    public static UF[] estados = UF.values();
    
    private int número;
    
    

    public Endereço(String logradouro, int número, String complemento,
            String bairro, String cidade, String CEP, UF estado) {
        this.logradouro  = logradouro;
        this.número      = número;
        this.complemento = complemento;
        this.bairro      = bairro;
        this.cidade      = cidade;
        this.CEP         = CEP;
        this.estado      = estado;
    }

    public UF getUF() {
        return estado;
    }

    public void setUF(UF estado) {
        this.estado = estado;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public int getNúmero() {
        return número;
    }

    public void setNúmero(int número) {
        this.número = número;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }
    
    public String toString(){
        String saida = "Endereço:";
        saida += "\nCEP: "+ CEP;
        saida += "\nUF: "+ estado;
        saida += "\nCidade: "+ cidade;
        saida += "\nBairro: "+ bairro;
        saida += "\nLogradouro: "+ logradouro;
        saida += "\nNúmero: "+ número;
        if(!complemento.isEmpty())
            saida += "\nComplemento: "+ complemento;
        return saida + "\n";
    }

}
