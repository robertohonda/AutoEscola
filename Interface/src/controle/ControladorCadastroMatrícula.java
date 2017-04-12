/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import entidade.Matrícula;

/**
 *
 * @author Roberto Honda
 */
public class ControladorCadastroMatrícula {
    public ControladorCadastroMatrícula(){
    }
    
    public static String inserirMatrícula(Matrícula matrícula) {
        
        if(!Matrícula.existeMatrícula(matrícula.getAluno().getCPF(), matrícula.getInstrutor().getCPF())) {
            return Matrícula.inserirMatrícula(matrícula);
        } else {
            return "Matrícula já realizada!";
        }
    }
    
    public String alterarMatrícula(Matrícula matrícula) {
        
        if (Matrícula.buscarMatrícula(matrícula.getNúmeroDeMatrícula()) != null) {
            return Matrícula.alterarMatrícula(matrícula);
        } else {
            return "Número de Matrícula não cadastrado!";
        }
    }
    
    public static String removerMatrícula(int sequencial) {
        
        if(Matrícula.buscarMatrícula(sequencial)!=null) {
            return Matrícula.removerMatrícula(sequencial);
        } else {
            return "A Matrícula selecionada não existe no BD!";
        }
    }
}
