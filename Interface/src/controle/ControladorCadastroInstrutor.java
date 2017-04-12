/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import entidade.Instrutor;

/**
 *
 * @author Roberto Honda
 */
public class ControladorCadastroInstrutor {
    public ControladorCadastroInstrutor(){
    }
    
    public static String inserirInstrutor(Instrutor instrutor) {
        
        if(Instrutor.buscarInstrutor(instrutor.getCPF())==null) {
            return Instrutor.inserirInstrutor(instrutor);
        } else {
            return "Instrutor já cadastrado!";
        }
    }
    
    public String alterarInstrutor(Instrutor instrutor) {
        
        if (Instrutor.buscarInstrutor(instrutor.getCPF()) != null) {
            return Instrutor.alterarInstrutor(instrutor);
        } else {
            return "Instrutor não cadastrado";
        }
    }
    
    public static String removerInstrutor(String cpf) {
        
        if(Instrutor.buscarInstrutor(cpf)!=null) {
            return Instrutor.removerInstrutor(cpf);
        } else {
            return "Instrutor não cadastrado!";
        }
    }
}
