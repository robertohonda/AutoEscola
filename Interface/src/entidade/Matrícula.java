/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidade;

import com.sun.javafx.geom.Matrix3f;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import persistência.BD;
import util.Data;

/**
 *
 * @author Roberto Honda
 */
public class Matrícula {
    
    
    public static Vector<Matrícula> pesquisarMatrículas(ArrayList<Horário.DiasDaSemana> diasLetivos, Data dataInício, Data dataFim,
            String cidadeDoAluno, char sexoInstrutor, ArrayList<Matrícula.Categoria> categoriasMatriculadas) {
        Vector<Matrícula> matrículasSelecionadas = new Vector();
        
        String sql = "SELECT * FROM Matrículas";
        
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);            
            ResultSet result = comando.executeQuery();
            
            while(result.next()) {
                
                int     idSequencial = result.getInt("Sequencial");
                
                Matrícula matrícula = buscarMatrícula(idSequencial);
                if(diasLetivos.size()>0)
                {
                    int flag=0;
                    for(Horário.DiasDaSemana diaDaSemana : diasLetivos)
                    {
                        if(!matrícula.getInstrutor().getHorário().getDiasLetivos().contains(diaDaSemana))
                        {
                            flag = 1;
                            break;
                        }
                    }
                    if(flag==1)
                        continue;
                }
                if(dataInício!=null&&matrícula.getDataMatrícula().compareTo(dataInício)==-1)
                    continue;
                if(dataFim!=null&&matrícula.getDataMatrícula().compareTo(dataFim)==1)
                    continue;
                if((!cidadeDoAluno.trim().isEmpty())&&matrícula.getAluno().getEndereço().getCidade().compareToIgnoreCase(cidadeDoAluno)!=0)
                    continue;
                if(Character.toLowerCase(sexoInstrutor)!='x'&&Character.toLowerCase(matrícula.getInstrutor().getSexo()) != Character.toLowerCase(sexoInstrutor))
                    continue;
                if(categoriasMatriculadas.size()>0)
                {
                    int flag = 0;
                    for(Categoria categoria : categoriasMatriculadas)
                    {
                        if(!matrícula.getCategoriasMatriculadas().contains(categoria)){
                            flag = 1;
                            break;
                        }
                    }
                    if(flag==1)
                        continue;
                }
                matrículasSelecionadas.add(matrícula);
            }            
            result.close();
            comando.close();
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return matrículasSelecionadas;
    }
    
    public static boolean existeMatrícula(String cpfAluno, String cpfInstrutor) {
        
        String sql = "SELECT AlunoId, InstrutorId FROM Matrículas WHERE AlunoId = ? AND InstrutorId = ?";
        
        int index = 1;
        boolean result = true;
        
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            
            comando.setString(index++, cpfAluno);
            comando.setString(index, cpfInstrutor);
            
            result = comando.executeQuery().next();      
            comando.close();
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return result;
    }
    
    public static Matrícula buscarMatrícula(int sequencial) {

        String sql = "SELECT InstrutorId, CategoriaA, CategoriaB, CategoriaC, CategoriaD, CategoriaE, "
                + "DataMatrícula, AlunoId, AptoExamePsicotécnico, AptoExameMédico"
                + " FROM Matrículas"
                + " WHERE Sequencial = ?";
        ResultSet listaResultados = null;

        Matrícula matrícula = null;

        try {
            
            PreparedStatement comando = BD.conexão.prepareStatement(sql);

            comando.setInt(1, sequencial);
            listaResultados = comando.executeQuery();
            
            while (listaResultados.next()) {
                
                ArrayList<Categoria> categoriasMatriculadas = new ArrayList();
                String          instrutorId      = listaResultados.getString("InstrutorId");
                if(listaResultados.getBoolean("CategoriaA"))
                    categoriasMatriculadas.add(Categoria.CategoriaA);
                if(listaResultados.getBoolean("CategoriaB"))
                    categoriasMatriculadas.add(Categoria.CategoriaB);
                if(listaResultados.getBoolean("CategoriaC"))
                    categoriasMatriculadas.add(Categoria.CategoriaC);
                if(listaResultados.getBoolean("CategoriaD"))
                    categoriasMatriculadas.add(Categoria.CategoriaD);
                if(listaResultados.getBoolean("CategoriaE"))
                    categoriasMatriculadas.add(Categoria.CategoriaE);
                String          dataMatrículaTexto= listaResultados.getString("DataMatrícula");
                String            alunoId          = listaResultados.getString("AlunoId");
                boolean         aptoExamePsicotécnico = listaResultados.getBoolean("AptoExamePsicotécnico");
                boolean         aptoExameMédico  = listaResultados.getBoolean("AptoExameMédico");
                
                
                
                matrícula = new Matrícula(sequencial, Instrutor.buscarInstrutor(instrutorId), categoriasMatriculadas, 
                        new Data(dataMatrículaTexto), Aluno.buscarAluno(alunoId), aptoExamePsicotécnico, aptoExameMédico);
                matrícula.getDataMatrícula().trocaMêsAno();
                
            }
            listaResultados.close();
            comando.close();

        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            
            matrícula = null;
        }

        return matrícula;
    }
    
     public static String alterarMatrícula(Matrícula matrícula) {

        String sql = "UPDATE Matrículas SET InstrutorId = ?, CategoriaA = ?, CategoriaB = ?, CategoriaC = ?, "
                + "CategoriaD = ?, CategoriaE = ?, DataMatrícula = ?, AlunoId = ?, AptoExamePsicotécnico = ?, AptoExameMédico = ?"
                + " WHERE Sequencial = ?";

        int index = 1;

        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(index++, matrícula.getInstrutor().getCPF());
            comando.setBoolean(index++, matrícula.getCategoriasMatriculadas().contains(Categoria.CategoriaA));
            comando.setBoolean(index++, matrícula.getCategoriasMatriculadas().contains(Categoria.CategoriaB));
            comando.setBoolean(index++, matrícula.getCategoriasMatriculadas().contains(Categoria.CategoriaC));
            comando.setBoolean(index++, matrícula.getCategoriasMatriculadas().contains(Categoria.CategoriaD));
            comando.setBoolean(index++, matrícula.getCategoriasMatriculadas().contains(Categoria.CategoriaE));
            comando.setString(index++, matrícula.getDataMatrícula().toStringFormatoBD());
            comando.setString(index++, matrícula.getAluno().getCPF());
            comando.setBoolean(index++, matrícula.getAptoExamePsicotécnico());
            comando.setBoolean(index++, matrícula.getAptoExameMédico());
            comando.setInt(index, matrícula.getNúmeroDeMatrícula());

            comando.executeUpdate();
            comando.close();
            return null;
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            return "Erro na Alteração da Matrícula no BD";
        }
    }
     
     public static String removerMatrícula(int sequencial) {
        
        String sql = "DELETE FROM Matrículas WHERE Sequencial = ?";
        
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setInt(1, sequencial);
            comando.executeUpdate();
            comando.close();
            return null;
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            return "Erro na Remoção da Matrícula no BD";
        }
    }
     
     public static String inserirMatrícula(Matrícula matrícula) {

        String sql = "INSERT INTO Matrículas (Sequencial, InstrutorId, CategoriaA, CategoriaB, "
                + "CategoriaC, CategoriaD, CategoriaE, DataMatrícula, AlunoId, AptoExamePsicotécnico, AptoExameMédico)"
                + " VALUES (DEFAULT,?,?,?,?,?,?,?,?,?,?)";

        int index = 1;

        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);

            //comando.setInt(index++, filme.getIdSequencial());
            comando.setString(index++, matrícula.getInstrutor().getCPF());
            comando.setBoolean(index++, matrícula.getCategoriasMatriculadas().contains(Categoria.CategoriaA));
            comando.setBoolean(index++, matrícula.getCategoriasMatriculadas().contains(Categoria.CategoriaB));
            comando.setBoolean(index++, matrícula.getCategoriasMatriculadas().contains(Categoria.CategoriaC));
            comando.setBoolean(index++, matrícula.getCategoriasMatriculadas().contains(Categoria.CategoriaD));
            comando.setBoolean(index++, matrícula.getCategoriasMatriculadas().contains(Categoria.CategoriaE));
            comando.setString(index++, matrícula.getDataMatrícula().toStringFormatoBD());
            comando.setString(index++, matrícula.getAluno().getCPF());
            comando.setBoolean(index++, matrícula.getAptoExamePsicotécnico());
            comando.setBoolean(index, matrícula.getAptoExameMédico());

            comando.executeUpdate();
            comando.close();
            
            return null;
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            return "Erro na Inserção da Matrícula no BD";
        }
    }
     
      public static int últimoSequencial(){

        String sql = "SELECT MAX(Sequencial) FROM Matrículas";

        int result;

        ResultSet lista_resultados;

        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);

            lista_resultados = comando.executeQuery();

            if (lista_resultados.first()) {
                result = lista_resultados.getInt("MAX(Sequencial)");
            } else {
                result = -1;
            }

            lista_resultados.close();
            comando.close();
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            result = -1;
        }

        return result;
    }
    
    public static Vector<Visão<Integer>> getVisões() {

        String sql = "SELECT Sequencial, AlunoId FROM Matrículas";

        ResultSet lista_resultados = null;

        Vector<Visão<Integer>> visões = new Vector<Visão<Integer>>();
        int sequencial;

        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            lista_resultados = comando.executeQuery();
            while (lista_resultados.next()) {
                sequencial = lista_resultados.getInt("Sequencial");
                visões.addElement(new Visão<Integer>(sequencial,
                        sequencial + " - " + Aluno.buscarAluno(lista_resultados.getString("AlunoId")).getNome()));
            }
            lista_resultados.close();
            comando.close();
        } catch (SQLException exceção_sql) {
            System.out.println("Erro em obter visões matrícula!");
            exceção_sql.printStackTrace();
        }

        return visões;
    }
    
    
    
    public Visão<Integer> getVisão() {
        return new Visão<Integer>(númeroDeMatrícula, númeroDeMatrícula + " - " + aluno.getNome());
    }
    
    private int númeroDeMatrícula;
    private Instrutor instrutor;
    //private ArrayList<Categoria> categoriasMatriculadas;
    private Data dataMatrícula;
    private Aluno aluno;
    private boolean aptoExamePsicotécnico;
    private boolean aptoExameMédico;
    public enum Categoria{CategoriaA, CategoriaB, CategoriaC, CategoriaD, CategoriaE};
    ArrayList<Categoria> categoriasMatriculadas;

    public Matrícula(int númeroDeMatrícula, Instrutor instrutor, ArrayList<Categoria> categoriasMatriculadas, 
            Data dataMatrícula, Aluno aluno, boolean aptoExamePsicotécnico, boolean aptoExameMédico) {
        this.númeroDeMatrícula = númeroDeMatrícula;
        this.instrutor = instrutor;
        this.categoriasMatriculadas = categoriasMatriculadas;
        this.dataMatrícula = dataMatrícula;
        this.aluno = aluno;
        this.aptoExamePsicotécnico = aptoExamePsicotécnico;
        this.aptoExameMédico = aptoExameMédico;
    }

    public int getNúmeroDeMatrícula() {
        return númeroDeMatrícula;
    }

    public void setNúmeroDeMatrícula(int númeroDeMatrícula) {
        this.númeroDeMatrícula = númeroDeMatrícula;
    }

    public Instrutor getInstrutor() {
        return instrutor;
    }

    public void setInstrutor(Instrutor instrutor) {
        this.instrutor = instrutor;
    }

    public ArrayList<Categoria> getCategoriasMatriculadas() {
        return categoriasMatriculadas;
    }

    public void setCategoriasMatriculadas(ArrayList<Categoria> categoriasMatriculadas) {
        this.categoriasMatriculadas = categoriasMatriculadas;
    }

    public Data getDataMatrícula() {
        return dataMatrícula;
    }

    public void setDataMatrícula(Data dataMatrícula) {
        this.dataMatrícula = dataMatrícula;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
    
    public boolean inserirCategoriaMatriculada(Categoria categoria){
        if(categoriasMatriculadas.contains(categoria))
            return false;
        categoriasMatriculadas.add(categoria);
        return true;
    }
    
    public boolean removerCategoriaMatriculada(Categoria categoria){
        return categoriasMatriculadas.remove(categoria);
    }

    public boolean getAptoExamePsicotécnico() {
        return aptoExamePsicotécnico;
    }

    public void setAptoExamePsicotécnico(boolean aptoExamePsicotécnico) {
        this.aptoExamePsicotécnico = aptoExamePsicotécnico;
    }

    public boolean getAptoExameMédico() {
        return aptoExameMédico;
    }

    public void setAptoExameMédico(boolean aptoExameMédico) {
        this.aptoExameMédico = aptoExameMédico;
    }
    
    
    public String toString(){
        String saída = "";
        
        saída += "Nome: " + aluno.getNome() + " - CPF: "+ aluno.getCPF() + "\n";
        saída += "Número de Matrícula: " + númeroDeMatrícula + "\n";
        saída += "Instrutor: "+instrutor.getNome() + "\n";
        saída += "Categoria matriculadas: ";
        int tam = categoriasMatriculadas.size();
        for(int i=0;i<tam;i++){
            Categoria categoria = categoriasMatriculadas.get(i);
            if(categoria == Categoria.CategoriaA)
                saída += "A";
            else if(categoria == Categoria.CategoriaB)
                saída += "B";
            else if(categoria == Categoria.CategoriaC)
                saída += "C";
            else if(categoria == Categoria.CategoriaD)
                saída += "D";
            else if(categoria == Categoria.CategoriaE)
                saída += "E";
            if(i<tam-1)
                saída += ", ";
        }
        saída += "\nData de Matrícula: " + dataMatrícula.toString()+"\n";
        if(aptoExameMédico)
            saída += "Apto no exame Médico\n";
        else
            saída += "Não apto no exame Médico\n";
        if(aptoExamePsicotécnico)
            saída += "Apto no exame Psicotécnico\n";
        else
            saída += "Não apto no exame Psicotécnico\n";
        return saída;
    }
    
}
