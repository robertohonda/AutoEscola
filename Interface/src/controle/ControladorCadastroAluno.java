/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import entidade.Aluno;

/**
 *
 * @author Roberto Honda
 */
public class ControladorCadastroAluno {
    public ControladorCadastroAluno() {
        //new JanelaCadastroAluno(this).setVisible(true);
    }
    
    public String inserirAluno(Aluno aluno) {
        Aluno aluno1 = Aluno.buscarAluno(aluno.getCPF());
        if (aluno1 == null) {
            return aluno.inserirAluno(aluno);
        } else {
            return "CPF do aluno já cadastrado";
        }
    }
    public String alterarAluno(Aluno aluno) {
        Aluno aluno1 = Aluno.buscarAluno(aluno.getCPF());
        if (aluno1 != null) {
            return Aluno.alterarAluno(aluno);
        } else {
            return "CPF do aluno não cadastrado";
        }
    }

    public String removerAluno(String cpf) {
        Aluno aluno1 = Aluno.buscarAluno(cpf);
        if (aluno1 != null) {
            return Aluno.removerAluno(cpf);
        } else {
            return "CPF do aluno não cadastrado";
        }
    }
}
