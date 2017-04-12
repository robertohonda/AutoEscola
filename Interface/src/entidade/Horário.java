
package entidade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import persistência.BD;
import util.Hora;

public class Horário {
    public static Horário buscarHorário(int sequencial) {

        String sql = "SELECT HoraDeInício, HoraDeTérmino, AulaSegunda, AulaTerça, AulaQuarta, AulaQuinta, "
                + "AulaSexta, AulaSábado, AulaDomingo"
                + " FROM Horários"
                + " WHERE Sequencial = ?";
        ResultSet listaResultados = null;

        Horário horário = null;

        try {
            
            PreparedStatement comando = BD.conexão.prepareStatement(sql);

            comando.setInt(1, sequencial);
            listaResultados = comando.executeQuery();
            
            while (listaResultados.next()) {
                Hora horaDeÍnicio = new Hora(listaResultados.getString("HoraDeInício"));
                Hora horaDeTérmino = new Hora(listaResultados.getString("HoraDeTérmino"));
                ArrayList<DiasDaSemana> diasLetivos = new ArrayList();
                if(listaResultados.getBoolean("AulaSegunda"))
                    diasLetivos.add(DiasDaSemana.segunda);
                if(listaResultados.getBoolean("AulaTerça"))
                    diasLetivos.add(DiasDaSemana.terça);
                if(listaResultados.getBoolean("AulaQuarta"))
                    diasLetivos.add(DiasDaSemana.quarta);
                if(listaResultados.getBoolean("AulaQuinta"))
                    diasLetivos.add(DiasDaSemana.quinta);
                if(listaResultados.getBoolean("AulaSexta"))
                    diasLetivos.add(DiasDaSemana.sexta);
                if(listaResultados.getBoolean("AulaSábado"))
                    diasLetivos.add(DiasDaSemana.sábado);
                if(listaResultados.getBoolean("AulaDomingo"))
                    diasLetivos.add(DiasDaSemana.domingo);
                
                horário = new Horário(sequencial, diasLetivos, horaDeÍnicio, horaDeTérmino);
                
            }
            listaResultados.close();
            comando.close();

        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            
           horário = null;
        }

        return horário;
    }
    
     public static String alterarHorário(Horário horário, int sequencial) {

        String sql = "UPDATE Horários SET HoraDeInício = ?, HoraDeTérmino = ?, AulaSegunda = ?, AulaTerça = ?, "
                + "AulaQuarta = ?, AulaQuinta = ?, AulaSexta = ?, AulaSábado = ?, AulaDomingo = ?"
                + " WHERE Sequencial = ?";

        int index = 1;
        
        if(!horário.getHoraDeInício().éVálido())
            return "Hora de início Inválida!";
        if(!horário.getHoraDeTérmino().éVálido())
            return "Hora de término inválida!";

        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setString(index++, horário.getHoraDeInício().toStringFormatoBD());
            comando.setString(index++, horário.getHoraDeTérmino().toStringFormatoBD());
            comando.setBoolean(index++, horário.getDiasLetivos().contains(DiasDaSemana.segunda));
            comando.setBoolean(index++, horário.getDiasLetivos().contains(DiasDaSemana.terça));
            comando.setBoolean(index++, horário.getDiasLetivos().contains(DiasDaSemana.quarta));
            comando.setBoolean(index++, horário.getDiasLetivos().contains(DiasDaSemana.quinta));
            comando.setBoolean(index++, horário.getDiasLetivos().contains(DiasDaSemana.sexta));
            comando.setBoolean(index++, horário.getDiasLetivos().contains(DiasDaSemana.sábado));
            comando.setBoolean(index++, horário.getDiasLetivos().contains(DiasDaSemana.domingo));
            //comando.setInt(index++, (filme.hasOscarMelhorFilmeAno() ? 1 : 0));
            comando.setInt(index, sequencial);

            comando.executeUpdate();
            comando.close();
            return null;
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            return "Erro na Alteração do horário no BD";
        }
    }
     
     public static String removerHorário(int sequencial) {
        
        String sql = "DELETE FROM Horários WHERE Sequencial = ?";
        
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);
            comando.setInt(1, sequencial);
            comando.executeUpdate();
            comando.close();
            return null;
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            return "Erro na Remoção da Horário no BD";
        }
    }
     
     public static String inserirHorário(Horário horário) {

        String sql = "INSERT INTO Horários (Sequencial, HoraDeInício, HoraDeTérmino, AulaSegunda, AulaTerça, AulaQuarta, AulaQuinta, "
                + "AulaSexta, AulaSábado, AulaDomingo)"
                + " VALUES (DEFAULT,?,?,?,?,?,?,?,?,?)";

        int index = 1;
        
        if(!horário.getHoraDeInício().éVálido())
            return "Hora de início Inválida!";
        if(!horário.getHoraDeTérmino().éVálido())
            return "Hora de término inválida!";
        try {
            PreparedStatement comando = BD.conexão.prepareStatement(sql);

            comando.setString(index++, horário.getHoraDeInício().toStringFormatoBD());
            comando.setString(index++, horário.getHoraDeTérmino().toStringFormatoBD());
            comando.setBoolean(index++, horário.getDiasLetivos().contains(DiasDaSemana.segunda));
            comando.setBoolean(index++, horário.getDiasLetivos().contains(DiasDaSemana.terça));
            comando.setBoolean(index++, horário.getDiasLetivos().contains(DiasDaSemana.quarta));
            comando.setBoolean(index++, horário.getDiasLetivos().contains(DiasDaSemana.quinta));
            comando.setBoolean(index++, horário.getDiasLetivos().contains(DiasDaSemana.sexta));
            comando.setBoolean(index++, horário.getDiasLetivos().contains(DiasDaSemana.sábado));
            comando.setBoolean(index, horário.getDiasLetivos().contains(DiasDaSemana.domingo));

            comando.executeUpdate();
            comando.close();
            
            return null;
        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
            return "Erro na Inserção do horário no BD";
        }
    }
     
      public static int últimoSequencial(){

        String sql = "SELECT MAX(Sequencial) FROM Horários";

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
    public Visão<Integer> getVisão() {
        String visão = "";
        int tam = diasLetivos.size();
        for(int i=0;i<tam;i++){
            DiasDaSemana diaLetivo = diasLetivos.get(i);
            visão+=diaLetivo;
            if(diaLetivo!=DiasDaSemana.domingo && diaLetivo!=DiasDaSemana.sábado)
                visão += "-feira";
            if(i<tam-1)
                visão += ", ";
        }
        visão += " "+horaDeInício.toString()+"~"+horaDeTérmino.toString();
        return new Visão<Integer>(idSequencial,  visão);
    }
    
    public enum DiasDaSemana{segunda, terça, quarta, quinta, sexta, sábado, domingo};
    private int idSequencial;
    private ArrayList<DiasDaSemana> diasLetivos;
    private Hora horaDeInício;
    private Hora horaDeTérmino;

    public Horário(int idSequencial, ArrayList<DiasDaSemana> diasLetivos, Hora horaDeInício, Hora horaDeTérmino) {
        this.idSequencial = idSequencial;
        this.diasLetivos = diasLetivos;
        this.horaDeInício = horaDeInício;
        this.horaDeTérmino = horaDeTérmino;
    }

    public int getIdSequencial() {
        return idSequencial;
    }

    public void setIdSequencial(int idSequencial) {
        this.idSequencial = idSequencial;
    }

    public ArrayList<DiasDaSemana> getDiasLetivos() {
        return diasLetivos;
    }

    public void setDiasLetivos(ArrayList<DiasDaSemana> diasLetivos) {
        this.diasLetivos = diasLetivos;
    }

    public Hora getHoraDeInício() {
        return horaDeInício;
    }

    public void setHoraDeInício(Hora horaDeInicio) {
        this.horaDeInício = horaDeInicio;
    }

    public Hora getHoraDeTérmino() {
        return horaDeTérmino;
    }

    public void setHoraDeTérmino(Hora horaDeTermino) {
        this.horaDeTérmino = horaDeTermino;
    }
    
    public boolean inserirDiaLetivo(DiasDaSemana diaLetivo){
        if(diasLetivos.contains(diaLetivo))
            return false;
        diasLetivos.add(diaLetivo);
        return true;
    }
    
    public boolean removerDiaLetivo(DiasDaSemana diaLetivo){
        return diasLetivos.remove(diaLetivo);
    }
    
    public String toString(){
        String saída = "";
        saída += "Dias da semana letivos: ";
        int tam = diasLetivos.size();
        for(int i=0;i<tam;i++){
            DiasDaSemana diaLetivo = diasLetivos.get(i);
            saída+=diaLetivo;
            if(diaLetivo!=DiasDaSemana.domingo && diaLetivo!=DiasDaSemana.sábado)
                saída += "-feira";
            if(i<tam-1)
                saída += ", ";
        }
        saída += "\nHorário de início: " + horaDeInício + ":" + horaDeTérmino + "\n";
        return saída;
    }
    
}
