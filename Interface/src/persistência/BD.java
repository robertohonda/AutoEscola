/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistência;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Roberto Honda
 */
public class BD {
    static final  String URL_BD = "jdbc:mysql://localhost:3306/autoescola?useSSL=true";
    static final  String USUÁRIO = "root";
    static final  String SENHA = "admin";
    public static Connection conexão = null;
    private static Statement comando = null;
    
    public static void criaConexão() {
        try {
            conexão = DriverManager.getConnection(URL_BD, USUÁRIO, SENHA);
            comando = conexão.createStatement();
        } catch (SQLException exceção_sql){
            exceção_sql.printStackTrace();
        }
    }

    public static void fechaConexão() {
        try {
            comando.close();
            conexão.close();
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
        }
    }
   
    /*
    public static void main(String[] args) {
        criaConexão();
        
        String sql;
        sql = "DELETE FROM Alunos";
        try {
            comando.executeUpdate(sql);
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
        }
        sql = "DELETE FROM Endereços;";
        try {
            comando.executeUpdate(sql);
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
        }
        sql = "DELETE FROM Horários;";   
        try {
            comando.executeUpdate(sql);
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
        }
        
        sql = "INSERT INTO Endereços (Logradouro, Complemento, Bairro, Número, CEP, Cidade, UF) VALUES ('Weimar', 'apto 4', 'Centro', 1020, 123123, 'Dourados', 'MS')";
        try {
            comando.executeUpdate(sql);
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
        }
        
        sql = "SELECT Logradouro FROM Endereços WHERE Sequencial = 6";
        ResultSet lista_resultados = null;
        try {
            lista_resultados = comando.executeQuery(sql);
            while (lista_resultados.next()) {
                System.out.println("Logradouro: " + lista_resultados.getString("Logradouro"));
            }
            lista_resultados.close();
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
        }
        
        /*
        sql = "INSERT INTO Alunos (CPF, Nome) VALUES ('111.111.111-11', 'Ana Julia')";
        try {
            comando.executeUpdate(sql);
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
        }
        sql = "SELECT Nome FROM Pessoas WHERE CPF = '111.111.111-11'";
        ResultSet lista_resultados = null;
        try {
            lista_resultados = comando.executeQuery(sql);
            while (lista_resultados.next()) {
                System.out.println("Nome: " + lista_resultados.getString("Nome"));
            }
            lista_resultados.close();
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
        }
        * /
        
        fechaConexão();
    }
    */

}
