package entidade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import persistência.BD;
import util.Data;

public class Aluno {
    
    public static String inserirAluno(Aluno aluno) {
        if(Endereço.inserirEndereço(aluno.getEndereço())!=null)
            return "Erro na Inserção do Endereço do Aluno no BD";
        int endereçoId = Endereço.últimoSequencial();

        String sql = "INSERT INTO Alunos (CPF, Nome, RG, EndereçoId, DataNascimento, Sexo, Telefone, Email)"
                + " VALUES (?,?,?,?,?,?,?,?)";
        
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(1, aluno.getCPF());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getRG());
            comando.setInt(4, endereçoId);
            comando.setString(5, aluno.getDataNascimento().toStringFormatoBD());
            comando.setString(6, ""+aluno.getSexo());
            comando.setString(7, aluno.getTelefone());
            comando.setString(8, aluno.getEmail());
            comando.executeUpdate();
            comando.close();
            return null;
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            return "Erro na Inserção do Aluno no BD";
        }
    }
    
    public static int buscarEndereçoId(String cpf){
        String sql = "SELECT EndereçoId FROM Alunos"
                + " WHERE CPF = ?";
        ResultSet lista_resultados = null;
        int endereçoId = -1;
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(1, cpf);
            comando.executeQuery();
            lista_resultados = comando.getResultSet();
            while (lista_resultados.next()) {
                endereçoId = lista_resultados.getInt("EndereçoId");
            }
            lista_resultados.close();
            comando.close();
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            endereçoId = -1;
        }
        return endereçoId;
    }
    
    public static String removerAluno(String cpf) {
        int endereçoId = buscarEndereçoId(cpf);
        Endereço.removerEndereço(endereçoId);
        
        String sql = "DELETE FROM Alunos WHERE CPF = ?";
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(1, cpf);
            comando.executeUpdate();
            comando.close();
            return null;
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            return "Erro na Remoção do Aluno no BD";
        }
    }
    
    public static Aluno buscarAluno(String cpf) {
        //falta dados
        String sql = "SELECT Nome, RG, EndereçoId, Sexo, DataNascimento, Telefone, Email FROM Alunos"
                + " WHERE CPF = ?";
        ResultSet lista_resultados = null;
        Aluno aluno = null;
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(1, cpf);
            lista_resultados = comando.executeQuery();
            while (lista_resultados.next()) {
                aluno = new Aluno(lista_resultados.getString("Nome"),
                        lista_resultados.getString("RG"),
                        cpf,
                        new Data(lista_resultados.getString("DataNascimento")),//data
                        lista_resultados.getString("Sexo").charAt(0),
                        Endereço.buscarEndereço(lista_resultados.getInt("EndereçoId")),
                        lista_resultados.getString("Telefone"),
                        lista_resultados.getString("Email"));
                aluno.getDataNascimento().trocaMêsAno();
            }
            lista_resultados.close();
            comando.close();
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            aluno = null;
        }
        return aluno;
    }
   
    public static String alterarAluno(Aluno aluno) {
        int endereçoId = buscarEndereçoId(aluno.getCPF());
        String mensagemErro;
        mensagemErro = Endereço.alterarEndereço(aluno.getEndereço(), endereçoId);
        if(mensagemErro !=null)
            return mensagemErro;
        String sql = "UPDATE Alunos SET Nome = ?, RG = ?, EndereçoId = ?, DataNascimento = ?, Sexo = ?, Telefone = ?, Email = ?"
                + " WHERE CPF = ?";
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(1, aluno.getNome());
            comando.setString(2, aluno.getRG());
            comando.setInt(3, endereçoId);
            comando.setString(4, aluno.getDataNascimento().toStringFormatoBD());
            comando.setString(5, ""+aluno.getSexo());
            comando.setString(6, aluno.getTelefone());
            comando.setString(7, aluno.getEmail());
            comando.setString(8, aluno.getCPF());
            comando.executeUpdate();
            comando.close();
            return null;
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            return "Erro na Alteração do Aluno no BD";
        }
    }
    
    public static Vector<Visão<String>> getVisões() {
        String sql = "SELECT CPF, Nome FROM Alunos";
        ResultSet lista_resultados = null;
        Vector<Visão<String>> visões = new Vector<Visão<String>>();
        String cpf;
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            lista_resultados = comando.executeQuery();
            while (lista_resultados.next()) {
                cpf = lista_resultados.getString("CPF");
                visões.addElement(new Visão<String>(cpf,
                        lista_resultados.getString("Nome") + " - " + cpf));
            }
            lista_resultados.close();
            comando.close();
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
        }
        return visões;
    }
    
    
    
    /*Dados Pessoais*/
    private String   nome, RG, CPF;
    private Data     dataNascimento;
    private char     sexo;
    /*Contato*/
    private String   telefone, email;
    /*Endereço*/
    private Endereço endereço;

    public Aluno(String nome, String RG, String CPF, Data dataNascimento, char sexo,
            Endereço endereço, String telefone, String email) {
        
        this.nome           = nome;
        this.RG             = RG;
        this.CPF            = CPF;
        this.dataNascimento = dataNascimento;
        this.sexo           = sexo;
        
        this.endereço       = endereço;
        this.telefone       = telefone;
        this.email          = email;
    }

    public String getNome() {
        return nome;
    }

    public String getRG() {
        return RG;
    }

    public String getCPF() {
        return CPF;
    }

    public Data getDataNascimento() {
        return dataNascimento;
    }

    public char getSexo() {
        return sexo;
    }

    public Endereço getEndereço() {
        return endereço;
    }

    public void setEndereço(Endereço endereço) {
        this.endereço = endereço;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public Visão<String> getVisão() {
        return new Visão<String>(CPF, nome + " - " + CPF);
    }
    
    @Override
    public String toString() {
        String saída = "";
        saída += "\nAluno " + "\nNome: " + nome + "\nRG: " + RG + "\nCPF: " + CPF 
                + "Data Nascimento: " + dataNascimento;
        
        if(Character.toUpperCase(sexo) == 'F'){
            saída += "\nSexo: Feminino";
        } else {
            saída += "\nSexo: Masculino";
        }
        saída += "\nEndereço: " + endereço.toString() + "\nTelefone: " + telefone + 
                "\nEmail: " + email + "\nExame Psicotécnico:";
        
        return saída;
    }
    
}
