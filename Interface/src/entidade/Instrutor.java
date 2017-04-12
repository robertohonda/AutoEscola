package entidade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import persistência.BD;
import util.Data;

public class Instrutor {
    public static String inserirInstrutor(Instrutor instrutor) {
        String mensagemErro;
        mensagemErro = Horário.inserirHorário(instrutor.getHorário());
        if(mensagemErro != null)
            return mensagemErro;
        int horárioId = Horário.últimoSequencial();
        mensagemErro = Endereço.inserirEndereço(instrutor.getEndereço());
        if(mensagemErro != null)
            return mensagemErro;
        int endereçoId = Endereço.últimoSequencial();
        //falta dados
        String sql = "INSERT INTO Instrutores (CPF, Nome, RG, EndereçoId, DataNascimento, Sexo, Telefone, Email, DataContratação, "
                + "HorárioId, CategoriaA, CategoriaB, CategoriaC, CategoriaD, CategoriaE)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(1, instrutor.getCPF());
            comando.setString(2, instrutor.getNome());
            comando.setString(3, instrutor.getRG());
            comando.setInt(4, endereçoId);
            comando.setString(5, instrutor.getDataNascimento().toStringFormatoBD());
            comando.setString(6, ""+instrutor.getSexo());
            comando.setString(7, instrutor.getTelefone());
            comando.setString(8, instrutor.getEmail());
            comando.setString(9, instrutor.getDataContratação().toStringFormatoBD());
            comando.setInt(10, horárioId);
            comando.setBoolean(11, instrutor.getCategoriasMinistradas().contains(Matrícula.Categoria.CategoriaA));
            comando.setBoolean(12, instrutor.getCategoriasMinistradas().contains(Matrícula.Categoria.CategoriaB));
            comando.setBoolean(13, instrutor.getCategoriasMinistradas().contains(Matrícula.Categoria.CategoriaC));
            comando.setBoolean(14, instrutor.getCategoriasMinistradas().contains(Matrícula.Categoria.CategoriaD));
            comando.setBoolean(15, instrutor.getCategoriasMinistradas().contains(Matrícula.Categoria.CategoriaE));
            comando.executeUpdate();
            comando.close();
            return null;
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            return "Erro na Inserção do Instrutor no BD";
        }
    }
    
    public static int buscarEndereçoId(String cpf){
        String sql = "SELECT EndereçoId FROM Instrutores"
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
    
    public static int buscarHorárioId(String cpf){
        String sql = "SELECT HorárioId FROM Instrutores"
                + " WHERE CPF = ?";
        ResultSet lista_resultados = null;
        int horárioId = -1;
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(1, cpf);
            comando.executeQuery();
            lista_resultados = comando.getResultSet();
            while (lista_resultados.next()) {
                horárioId = lista_resultados.getInt("HorárioId");
            }
            lista_resultados.close();
            comando.close();
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            horárioId = -1;
        }
        return horárioId;
    }
    
    public static String removerInstrutor(String cpf) {
        int endereçoId = buscarEndereçoId(cpf);
        int horárioId = buscarHorárioId(cpf);
        Horário.removerHorário(horárioId);
        Endereço.removerEndereço(endereçoId);
        
        String sql = "DELETE FROM Instrutores WHERE CPF = ?";
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(1, cpf);
            comando.executeUpdate();
            comando.close();
            return null;
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            return "Erro na Remoção do Instrutor no BD";
        }
    }
    
    public static Instrutor buscarInstrutor(String cpf) {
        String sql = "SELECT Nome, RG, EndereçoId, Sexo, DataNascimento, Telefone, Email, DataContratação, HorárioId, CategoriaA,"
                + " CategoriaB, CategoriaC, CategoriaD, CategoriaE FROM Instrutores"
                + " WHERE CPF = ?";
        ResultSet lista_resultados = null;
        Instrutor instrutor = null;
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(1, cpf);
            lista_resultados = comando.executeQuery();
            while (lista_resultados.next()) {
                ArrayList<Matrícula.Categoria> categoriasMatriculadas = new ArrayList();
                if(lista_resultados.getBoolean("CategoriaA"))
                    categoriasMatriculadas.add(Matrícula.Categoria.CategoriaA);
                if(lista_resultados.getBoolean("CategoriaB"))
                    categoriasMatriculadas.add(Matrícula.Categoria.CategoriaB);
                if(lista_resultados.getBoolean("CategoriaC"))
                    categoriasMatriculadas.add(Matrícula.Categoria.CategoriaC);
                if(lista_resultados.getBoolean("CategoriaD"))
                    categoriasMatriculadas.add(Matrícula.Categoria.CategoriaD);
                if(lista_resultados.getBoolean("CategoriaE"))
                    categoriasMatriculadas.add(Matrícula.Categoria.CategoriaE);
                instrutor = new Instrutor(lista_resultados.getString("Nome"),
                        lista_resultados.getString("RG"),
                        cpf,
                        new Data(lista_resultados.getString("DataNascimento")),//data
                        lista_resultados.getString("Sexo").charAt(0),
                        Endereço.buscarEndereço(lista_resultados.getInt("EndereçoId")),
                        lista_resultados.getString("Telefone"),
                        lista_resultados.getString("Email"),
                        new Data(lista_resultados.getString("DataContratação")),
                        categoriasMatriculadas, Horário.buscarHorário(lista_resultados.getInt("HorárioId")));
                instrutor.getDataNascimento().trocaMêsAno();
                instrutor.getDataContratação().trocaMêsAno();
            }
            lista_resultados.close();
            comando.close();
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            instrutor = null;
        }
        return instrutor;
    }
   
    public static String alterarInstrutor(Instrutor instrutor) {
        int endereçoId = buscarEndereçoId(instrutor.getCPF());
        String mensagemErro;
        mensagemErro = Endereço.alterarEndereço(instrutor.getEndereço(), endereçoId);
        if(mensagemErro!=null)
            return mensagemErro;
        int horárioId = buscarHorárioId(instrutor.getCPF());
        mensagemErro = Horário.alterarHorário(instrutor.getHorário(), horárioId);
        if(mensagemErro!=null)
            return mensagemErro;
        String sql = "UPDATE Instrutores SET Nome = ?, RG = ?, EndereçoId = ?, DataNascimento = ?, Sexo = ?, Telefone = ?, Email = ?, "
                + "DataContratação = ?, HorárioId = ?, CategoriaA = ?, CategoriaB = ?, CategoriaC = ?, CategoriaD = ?, CategoriaE = ?"
                + " WHERE CPF = ?";
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(1, instrutor.getNome());
            comando.setString(2, instrutor.getRG());
            comando.setInt(3, endereçoId);
            comando.setString(4, instrutor.getDataNascimento().toStringFormatoBD());
            comando.setString(5, ""+instrutor.getSexo());
            comando.setString(6, instrutor.getTelefone());
            comando.setString(7, instrutor.getEmail());
            comando.setString(8, instrutor.getDataContratação().toStringFormatoBD());
            comando.setInt(9, horárioId);
            comando.setBoolean(10, instrutor.getCategoriasMinistradas().contains(Matrícula.Categoria.CategoriaA));
            comando.setBoolean(11, instrutor.getCategoriasMinistradas().contains(Matrícula.Categoria.CategoriaB));
            comando.setBoolean(12, instrutor.getCategoriasMinistradas().contains(Matrícula.Categoria.CategoriaC));
            comando.setBoolean(13, instrutor.getCategoriasMinistradas().contains(Matrícula.Categoria.CategoriaD));
            comando.setBoolean(14, instrutor.getCategoriasMinistradas().contains(Matrícula.Categoria.CategoriaE));
            comando.setString(15, instrutor.getCPF());
            
            comando.executeUpdate();
            comando.close();
            return null;
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            return "Erro na Alteração do Instrutor no BD";
        }
    }
    
    public static Vector<Visão<String>> getVisões() {
        String sql = "SELECT CPF, Nome FROM Instrutores";
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
    
    public Visão<String> getVisão() {
        return new Visão<String>(CPF, nome + " - " + CPF);
    }
    
    
    
    /*Dados Pessoais*/
    private String   nome, RG, CPF;
    private Data     dataNascimento;
    private char     sexo;
    
    /*Contato*/
    private Endereço endereço;
    private String   telefone, email;
    
    /*Outros*/
    private Data                 dataContratação;
    private ArrayList<Matrícula.Categoria> categoriasMinistradas;
    private Horário              horário;

    public Instrutor(String nome, String RG, String CPF, Data dataNascimento, 
            char sexo, Endereço endereço, String telefone, String email, 
            Data dataContratação, ArrayList<Matrícula.Categoria> categoriasMinistradas, 
            Horário horário) {
        this.nome                  = nome;
        this.RG                    = RG;
        this.CPF                   = CPF;
        this.dataNascimento        = dataNascimento;
        this.sexo                  = sexo;
        this.endereço              = endereço;
        this.telefone              = telefone;
        this.email                 = email;
        this.dataContratação       = dataContratação;
        this.categoriasMinistradas = categoriasMinistradas;
        this.horário = horário;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRG() {
        return RG;
    }

    public void setRG(String RG) {
        this.RG = RG;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public Data getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Data dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
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

    public Data getDataContratação() {
        return dataContratação;
    }

    public void setDataContratação(Data dataContratação) {
        this.dataContratação = dataContratação;
    }

    public ArrayList<Matrícula.Categoria> getCategoriasMinistradas() {
        return categoriasMinistradas;
    }

    public void setCategoriasMinistradas(ArrayList<Matrícula.Categoria> categoriasMinistradas) {
        this.categoriasMinistradas = categoriasMinistradas;
    }

    public Horário getHorário() {
        return horário;
    }

    public void setHorário(Horário horário) {
        this.horário = horário;
    }
    
    public boolean inserirCategoriaMinistrada(Matrícula.Categoria categoria){
        if(categoriasMinistradas.contains(categoria))
            return false;
        categoriasMinistradas.add(categoria);
        return true;
    }
    
    public boolean removerCategoriaMinistrada(Matrícula.Categoria categoria){
        return categoriasMinistradas.remove(categoria);
    }
    
    

    @Override
    public String toString() {
        String saída = "";
        saída += "\nInstrutor" + "\nNome: " + nome + "\nRG: " + RG + "\nCPF: " + CPF + 
                "\nData de Nascimento: " + dataNascimento;
        if(Character.toUpperCase(sexo) == 'F'){
            saída += "\nSexo: Feminino";
        } else {
            saída += "\nSexo: Masculino";
        }
        saída += "\nEndereço: " + endereço.toString() + "\nTelefone: " + telefone + 
                "\nEmail: " + email + "\nData de Contratação: " + dataContratação + 
                "\nCategoria(s) Ministrada(s): ";
        
        int tam = categoriasMinistradas.size();
        for(int i=0;i<tam;i++){
            Matrícula.Categoria categoria = categoriasMinistradas.get(i);
            if(categoria == Matrícula.Categoria.CategoriaA)
                saída += "A";
            else if(categoria == Matrícula.Categoria.CategoriaB)
                saída += "B";
            else if(categoria == Matrícula.Categoria.CategoriaC)
                saída += "C";
            else if(categoria == Matrícula.Categoria.CategoriaD)
                saída += "D";
            else if(categoria == Matrícula.Categoria.CategoriaE)
                saída += "E";
            if(i<tam-1)
                saída += ", ";
        }
        saída += "\n" + horário.toString() + "\n";
        return saída;
    }
    
}
