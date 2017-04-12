/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import controle.ControladorCadastroAluno;
import controle.ControladorCadastroInstrutor;
import controle.ControladorCadastroMatrícula;
import entidade.Aluno;
import entidade.Endereço;
import entidade.Horário;
import entidade.Instrutor;
import entidade.Matrícula;
import entidade.Visão;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import persistência.BD;
import util.Data;
import util.Hora;

/**
 *
 * @author alexs
 */
public class JanelaPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form JanelaPrincipal
     */
    private DefaultListModel<Visão<Integer>> modelo_lista_matrículasCadastradas = new DefaultListModel();
    private DefaultListModel<Visão<Integer>> modelo_lista_horáriosCadastrados = new DefaultListModel();
    ControladorCadastroAluno controladorCadastroAluno = new ControladorCadastroAluno();
    ControladorCadastroMatrícula controladorCadastroMatrícula = new ControladorCadastroMatrícula();
    ControladorCadastroInstrutor controladorCadastroInstrutor = new ControladorCadastroInstrutor();
    BufferedImage imagem;

    Vector<Visão<String>> alunos_cadastrados;
    Vector<Visão<String>> instrutores_cadastrados;

    public JanelaPrincipal() {
        BD.criaConexão();
        alunos_cadastrados = Aluno.getVisões();
        instrutores_cadastrados = Instrutor.getVisões();
        inicializarListaMatrículas();
        initComponents();
        inicializaComponentes();
    }

    private void inicializaComponentes() {
        alunoCadastrado_ComboBox.setSelectedIndex(-1);
        instrutoresCadastrados_ComboBox.setSelectedIndex(-1);
        matrículaInstrutoresCadastrados_ComboBox.setSelectedIndex(-1);
        matrículaAlunosCadastrados_ComboBox.setSelectedIndex(-1);
    }

    private void inicializarListaMatrículas() {

        Vector<Visão<Integer>> visões = Matrícula.getVisões();

        for (Visão<Integer> visão : visões) {
            modelo_lista_matrículasCadastradas.addElement(visão);
        }
    }

    private void apendarPesquisaAreaTexto(Vector<Matrícula> matrículasSelecionadas, ArrayList<Horário.DiasDaSemana> diasLetivos, Data dataÍnicio, Data dataFim,
            String cidadeDoAluno, char sexoInstrutor, ArrayList<Matrícula.Categoria> categoriasMatriculadas) {

        String output = "Filtros utilizados = [";
        
        output += "A partir da data = " + (dataÍnicio == null ? "Qualquer" : dataÍnicio.toString()) + ", ";
        output += "Dias da semana letivos = ";
        int tam = diasLetivos.size();
        if(tam==0)
            output += "Qualquer ";
        else
        {
            for(int i=0;i<tam;i++){
                Horário.DiasDaSemana diaLetivo = diasLetivos.get(i);
                output += diaLetivo;
                if(diaLetivo!=Horário.DiasDaSemana.domingo && diaLetivo!=Horário.DiasDaSemana.sábado)
                    output += "-feira";
                if(i<tam-1)
                    output += ", ";
            }
        }
        output += ", Até a data = " + (dataFim == null ? "Qualquer" : dataFim.toString()) + ", ";
        output += " Cidade do Aluno = " + (cidadeDoAluno.isEmpty() ? "Qualquer" : cidadeDoAluno) + ", ";
        output += " Sexo do instrutor = " + (Character.toLowerCase(sexoInstrutor) == 'x' ? "Qualquer" : sexoInstrutor) + ", ";
        output += " Categorias matrículadas = "; 
        if (categoriasMatriculadas.size() == 0) {
            output += "Qualquer ";
        } else {
            tam = categoriasMatriculadas.size();
            for (int i = 0; i < tam; i++) {
                Matrícula.Categoria categoria = categoriasMatriculadas.get(i);
                if (categoria == Matrícula.Categoria.CategoriaA) {
                    output += "A";
                } else if (categoria == Matrícula.Categoria.CategoriaB) {
                    output += "B";
                } else if (categoria == Matrícula.Categoria.CategoriaC) {
                    output += "C";
                } else if (categoria == Matrícula.Categoria.CategoriaD) {
                    output += "D";
                } else if (categoria == Matrícula.Categoria.CategoriaE) {
                    output += "E";
                }
                if (i < tam - 1) {
                    output += ", ";
                }
            }
        }
        output += "]\n";

        if (!matrículasSelecionadas.isEmpty()) {
            for (Matrícula matrícula : matrículasSelecionadas) {
                output += " -- Dias letivos: ";
                tam = matrícula.getInstrutor().getHorário().getDiasLetivos().size();
                for(int i=0;i<tam;i++){
                    Horário.DiasDaSemana diaLetivo = matrícula.getInstrutor().getHorário().getDiasLetivos().get(i);
                    output +=diaLetivo;
                    if(diaLetivo!=Horário.DiasDaSemana.domingo && diaLetivo!=Horário.DiasDaSemana.sábado)
                        output += "-feira";
                    if(i<tam-1)
                        output += ", ";
                }
                output += "\n";
                output += " -- Data de Matrícula: " + matrícula.getDataMatrícula() + "\n";
                output += " -- Cidade do Aluno: " + matrícula.getAluno().getEndereço().getCidade() + "\n";
                output += " -- Sexo do instrutor: " + ((Character.toUpperCase(matrícula.getInstrutor().getSexo())== 'M') ? "Masculino": "Feminino") + "\n";
                output += " -- Categorias Matrículadas: ";
                tam = matrícula.getCategoriasMatriculadas().size();
                for (int i = 0; i < tam; i++) {
                    Matrícula.Categoria categoria = matrícula.getCategoriasMatriculadas().get(i);
                    if (categoria == Matrícula.Categoria.CategoriaA) {
                        output += "A";
                    } else if (categoria == Matrícula.Categoria.CategoriaB) {
                        output += "B";
                    } else if (categoria == Matrícula.Categoria.CategoriaC) {
                        output += "C";
                    } else if (categoria == Matrícula.Categoria.CategoriaD) {
                        output += "D";
                    } else if (categoria == Matrícula.Categoria.CategoriaE) {
                        output += "E";
                    }
                    if (i < tam - 1) {
                        output += ", ";
                    }
                }
                //output += "\n\n";
                output += "\n" + matrícula.toString() + "\n\n";
            }
        } else {
            output += "Não houveram resultados para a pesquisa!\n\n";
        }

        filtroResultadosTextArea.append(output);
    }

    private Instrutor obterInstrutorInformado() {

        String nome = instrutorNome_TextField.getText();
        if (nome.trim().isEmpty()) {
            return null;
        }

        String cpf = instrutorCpf_FormattedTextField.getText();
        if (cpf.compareTo("   .   .   -  ") == 0) {
            return null;
        }

        String rg = instrutorRg_FormattedTextField.getText();
        if (rg.compareTo("  .   .   - ") == 0) {
            return null;
        }

        char sexo;
        if (instrutorSexoMasculino_RadioButton.isSelected()) {
            sexo = 'M';
        } else if (instrutorSexoFeminino_RadioButton.isSelected()) {
            sexo = 'F';
        } else {
            return null;
        }
        if (instrutorDataDeNascimento_FormattedTextField.getText().compareTo("  /  /    ") == 0) {
            return null;
        }
        Data dataDeNascimento = new Data(instrutorDataDeNascimento_FormattedTextField.getText().trim());

        if (instrutorDataDeContratação_FormattedTextField.getText().compareTo("  /  /    ") == 0) {
            return null;
        }
        Data dataDeContratação = new Data(instrutorDataDeContratação_FormattedTextField.getText().trim());
        //Conferindo Contatos
        String telefone = instrutorTelefone_FormatedTextField.getText();
        if (telefone.trim().compareTo("(  )     -    ")==0) {
            return null;
        }

        String email = instrutorEmail_TextField.getText();
        if (email.trim().isEmpty()) {
            return null;
        }

        //Conferindo Endereço
        String logradouro = instrutorLogradouro_TextField.getText();
        if (logradouro.trim().isEmpty()) {
            return null;
        }

        String complemento = instrutorComplemento_TextField.getText();
        if (complemento.trim().isEmpty()) {
            return null;
        }

        String bairro = instrutorBairro_TextField.getText();
        if (bairro.trim().isEmpty()) {
            return null;
        }

        int número;
        if (instrutorNúmero_TextField.getText().trim().isEmpty()) {
            return null;
        }
        try {
            número = Integer.parseInt(instrutorNúmero_TextField.getText().trim());
        } catch (Exception e) {
            número = -1;
        }

        String cep = instrutorCep_TextField.getText();
        if (cep.trim().isEmpty()) {
            return null;
        }

        String cidade = instrutorCidade_TextField.getText();
        if (cidade.trim().isEmpty()) {
            return null;
        }

        int item_selecionado;
        item_selecionado = instrutorEstado_ComboBox.getSelectedIndex();
        if (item_selecionado == -1) {
            return null;
        }
        Endereço.UF estado = Endereço.estados[item_selecionado];

        ArrayList<Matrícula.Categoria> categoriasMinistradas = new ArrayList();
        if (instrutorCategoriaA_RadioButton.isSelected()) {
            categoriasMinistradas.add(Matrícula.Categoria.CategoriaA);
        }
        if (instrutorCategoriaB_RadioButton.isSelected()) {
            categoriasMinistradas.add(Matrícula.Categoria.CategoriaB);
        }
        if (instrutorCategoriaC_RadioButton.isSelected()) {
            categoriasMinistradas.add(Matrícula.Categoria.CategoriaC);
        }
        if (instrutorCategoriaD_RadioButton.isSelected()) {
            categoriasMinistradas.add(Matrícula.Categoria.CategoriaD);
        }
        if (instrutorCategoriaE_RadioButton.isSelected()) {
            categoriasMinistradas.add(Matrícula.Categoria.CategoriaE);
        }
        if (categoriasMinistradas.size() == 0) {
            return null;
        }

        ArrayList<Horário.DiasDaSemana> diasLetivos = new ArrayList();
        if (instrutorSegundaCheckBox.isSelected()) {
            diasLetivos.add(Horário.DiasDaSemana.segunda);
        }
        if (instrutorTerçaCheckBox.isSelected()) {
            diasLetivos.add(Horário.DiasDaSemana.terça);
        }
        if (instrutorQuartaCheckBox.isSelected()) {
            diasLetivos.add(Horário.DiasDaSemana.quarta);
        }
        if (instrutorQuintaCheckBox.isSelected()) {
            diasLetivos.add(Horário.DiasDaSemana.quinta);
        }
        if (instrutorSextaCheckBox.isSelected()) {
            diasLetivos.add(Horário.DiasDaSemana.sexta);
        }
        if (instrutorSábadoCheckBox.isSelected()) {
            diasLetivos.add(Horário.DiasDaSemana.sábado);
        }
        if (instrutorDomingoCheckBox.isSelected()) {
            diasLetivos.add(Horário.DiasDaSemana.domingo);
        }
        if (diasLetivos.size() == 0) {
            return null;
        }

        if (instrutorHoraInicio_FormattedTextField.getText().compareTo("  :  ") == 0) {
            return null;
        }
        Hora horaÍnicio = new Hora(instrutorHoraInicio_FormattedTextField.getText().trim());
        if (instrutorHoraFim_FormattedTextField.getText().compareTo("  :  ") == 0) {
            return null;
        }
        Hora horaFim = new Hora(instrutorHoraFim_FormattedTextField.getText().trim());

        return new Instrutor(nome, rg, cpf, dataDeNascimento, sexo, new Endereço(logradouro, número, complemento, bairro, cidade, cep, estado),
                 telefone, email, dataDeContratação, categoriasMinistradas, new Horário(-1, diasLetivos, horaÍnicio, horaFim));

    }

    private int buscaIndiceVisão(Vector<Visão<String>> visões, String chave) {
        int indice = 0;
        for (Visão<String> visão : visões) {
            if (visão.getChave().compareTo(chave) == 0) {
                return indice;
            }
            indice++;
        }
        return -1;
    }

    private Matrícula obterMatrículaInformada() {

        Visão<String> visãoAluno = (Visão<String>) matrículaAlunosCadastrados_ComboBox.getSelectedItem();
        if (visãoAluno == null) {
            return null;
        }
        Aluno aluno = Aluno.buscarAluno(visãoAluno.getChave());
        if (aluno == null) {
            return null;
        }

        Visão<String> visãoInstrutor = (Visão<String>) matrículaInstrutoresCadastrados_ComboBox.getSelectedItem();
        if (visãoInstrutor == null) {
            return null;
        }
        Instrutor instrutor = Instrutor.buscarInstrutor(visãoInstrutor.getChave());
        if (instrutor == null) {
            return null;
        }

        ArrayList<Matrícula.Categoria> categoriasMatrículadas = new ArrayList();
        if (matrículaCategoriaA_RadioButton.isSelected()) {
            categoriasMatrículadas.add(Matrícula.Categoria.CategoriaA);
        }
        if (matrículaCategoriaB_RadioButton.isSelected()) {
            categoriasMatrículadas.add(Matrícula.Categoria.CategoriaB);
        }
        if (matrículaCategoriaC_RadioButton.isSelected()) {
            categoriasMatrículadas.add(Matrícula.Categoria.CategoriaC);
        }
        if (matrículaCategoriaD_RadioButton.isSelected()) {
            categoriasMatrículadas.add(Matrícula.Categoria.CategoriaD);
        }
        if (matrículaCategoriaE_RadioButton.isSelected()) {
            categoriasMatrículadas.add(Matrícula.Categoria.CategoriaE);
        }
        if (categoriasMatrículadas.size() == 0) {
            return null;
        }

        boolean examePsicotécnico;
        if (!matrículaAptoPsicotécnico_RadioButton.isSelected() && !matrículaInaptoPisicotécnico_RadioButton.isSelected()) {
            return null;
        }
        if (matrículaAptoPsicotécnico_RadioButton.isSelected()) {
            examePsicotécnico = true;
        } else {
            examePsicotécnico = false;
        }

        boolean exameMédico;
        if (!matrículaAptoMédico_RadioButton.isSelected() && !matrículaInaptoMédico_RadioButton.isSelected()) {
            return null;
        }
        if (matrículaAptoMédico_RadioButton.isSelected()) {
            exameMédico = true;
        } else {
            exameMédico = false;
        }

        String data = matrículaDataMatrícula_FormattedTextField.getText();
        if (data.compareTo("  /  /    ") == 0) {
            return null;
        }
        Data dataMatrícula = new Data(data.trim());

        if (matrículaHoráriosList.getSelectedIndex() == -1) {
            return null;
        }
        Visão<Integer> visãoHorário = (Visão<Integer>) matrículaHoráriosList.getSelectedValue();
        Horário horário = Horário.buscarHorário(visãoHorário.getChave());

        return new Matrícula(-1, instrutor, categoriasMatrículadas, dataMatrícula, aluno, examePsicotécnico, exameMédico);
    }

    private Aluno obterAlunoInformado() {

        /*Falta a data de nascimento e os exames se for definido na classe Aluno*/
        //Conferindo dados pessoais
        String nome = alunoNome_TextField.getText();
        if (nome.trim().isEmpty()) {
            return null;
        }

        String cpf = alunoCpf_FormattedTextField.getText();
        if (cpf.compareTo("   .   .   -  ") == 0) {
            return null;
        }

        String rg = alunoRg_FormattedTextField.getText();
        if (rg.compareTo("  .   .   - ") == 0) {
            return null;
        }

        char sexo;
        if (alunoSexoMasculino_RadioButton.isSelected()) {
            sexo = 'M';
        } else if (alunoSexoFeminino_RadioButton.isSelected()) {
            sexo = 'F';
        } else {
            return null;
        }
        if (alunoDataDeNascimento_FormattedTextField.getText().compareTo("  /  /    ") == 0) {
            return null;
        }
        Data dataDeNascimento = new Data(alunoDataDeNascimento_FormattedTextField.getText().trim());

        //Conferindo Contatos
        String telefone = alunoTelefone_FormatedTextField.getText();
        if (telefone.compareTo("(  )     -    ")==0) {
            return null;
        }

        String email = alunoEmail_TextField.getText();
        if (email.trim().isEmpty()) {
            return null;
        }

        //Conferindo Endereço
        String logradouro = alunoLogradouro_TextField.getText();
        if (logradouro.trim().isEmpty()) {
            return null;
        }

        String complemento = alunoComplemento_TextField.getText();
        if (complemento.trim().isEmpty()) {
            return null;
        }

        String bairro = alunoBairro_TextField.getText();
        if (bairro.trim().isEmpty()) {
            return null;
        }

        int número;
        if (alunoNúmero_TextField.getText().trim().isEmpty()) {
            return null;
        }
        try {
            número = Integer.parseInt(alunoNúmero_TextField.getText().trim());
        } catch (Exception e) {
            número = -1;
        }

        String cep = alunoCep_TextField.getText();
        if (cep.trim().isEmpty()) {
            return null;
        }

        String cidade = alunoCidade_TextField.getText();
        if (cidade.trim().isEmpty()) {
            return null;
        }

        int item_selecionado;
        item_selecionado = alunoEstado_ComboBox.getSelectedIndex();
        if (item_selecionado == -1) {
            return null;
        }
        Endereço.UF estado = Endereço.estados[item_selecionado];

        return new Aluno(nome, rg, cpf, dataDeNascimento, sexo, new Endereço(logradouro, número, complemento, bairro, cidade, cep, estado), telefone, email);
    }

    private Visão<String> getVisãoAlunosCadastrados(String chave) {
        for (Visão<String> visão : alunos_cadastrados) {
            if (visão.getChave().equals(chave)) {
                return visão;
            }
        }
        return null;
    }

    private Visão<String> getVisãoInstrutoresCadastrados(String chave) {
        for (Visão<String> visão : instrutores_cadastrados) {
            if (visão.getChave().equals(chave)) {
                return visão;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        alunoSexobuttonGroup = new javax.swing.ButtonGroup();
        matrículaExamePsicotécnico_buttonGroup = new javax.swing.ButtonGroup();
        matrículaExameMédico_buttonGroup = new javax.swing.ButtonGroup();
        instrutorSexo_buttonGroup = new javax.swing.ButtonGroup();
        filtroSexoInstrutor = new javax.swing.ButtonGroup();
        abas_TabbedPane = new javax.swing.JTabbedPane();
        inicioPanel = new javax.swing.JPanel();
        logoAutoEscolaLabel = new javax.swing.JLabel();
        alunoPanel = new javax.swing.JPanel();
        alunoBotões_Panel = new javax.swing.JPanel();
        alunoInserir_Button = new javax.swing.JButton();
        alunoConsultar_Button = new javax.swing.JButton();
        alunoAlterar_Button = new javax.swing.JButton();
        alunoRemover_Button = new javax.swing.JButton();
        alunoLimpar_Button = new javax.swing.JButton();
        alunoDadosPessoais_Panel = new javax.swing.JPanel();
        alunoNome_Label = new javax.swing.JLabel();
        alunoNome_TextField = new javax.swing.JTextField();
        alunoCpf_Label = new javax.swing.JLabel();
        alunoSexo_Label = new javax.swing.JLabel();
        alunoRg_Label = new javax.swing.JLabel();
        alunoCpf_FormattedTextField = new javax.swing.JFormattedTextField();
        alunoRg_FormattedTextField = new javax.swing.JFormattedTextField();
        alunoDataDeNascimento_Label = new javax.swing.JLabel();
        alunoDataDeNascimento_FormattedTextField = new javax.swing.JFormattedTextField();
        alunoSexoMasculino_RadioButton = new javax.swing.JRadioButton();
        alunoSexoFeminino_RadioButton = new javax.swing.JRadioButton();
        alunoContato_Panel = new javax.swing.JPanel();
        alunoTelefone_Label = new javax.swing.JLabel();
        alunoEmail_Label = new javax.swing.JLabel();
        alunoEmail_TextField = new javax.swing.JTextField();
        alunoTelefone_FormatedTextField = new javax.swing.JFormattedTextField();
        alunoEndereço_Panel = new javax.swing.JPanel();
        alunoLogradouro_Label = new javax.swing.JLabel();
        alunoLogradouro_TextField = new javax.swing.JTextField();
        alunoNúmero_Label = new javax.swing.JLabel();
        alunoNúmero_TextField = new javax.swing.JTextField();
        alunoComplemento_Label = new javax.swing.JLabel();
        alunoComplemento_TextField = new javax.swing.JTextField();
        alunoBairro_Label = new javax.swing.JLabel();
        alunoBairro_TextField = new javax.swing.JTextField();
        alunoCep_Label = new javax.swing.JLabel();
        alunoCep_TextField = new javax.swing.JTextField();
        alunoCidade_Label = new javax.swing.JLabel();
        alunoCidade_TextField = new javax.swing.JTextField();
        alunoUf_Label = new javax.swing.JLabel();
        alunoEstado_ComboBox = new javax.swing.JComboBox();
        alunoCadastrado_ComboBox = new javax.swing.JComboBox<>();
        instrutorPanel = new javax.swing.JPanel();
        instrutoresDadosPessoais_Panel = new javax.swing.JPanel();
        instrutoresNome_Label = new javax.swing.JLabel();
        instrutorNome_TextField = new javax.swing.JTextField();
        instrutoresCpf_Label = new javax.swing.JLabel();
        instrutoresSexo_Label = new javax.swing.JLabel();
        instrutoresRg_Label = new javax.swing.JLabel();
        instrutorCpf_FormattedTextField = new javax.swing.JFormattedTextField();
        instrutorRg_FormattedTextField = new javax.swing.JFormattedTextField();
        instrutoresDataDeNascimento_Label = new javax.swing.JLabel();
        instrutorDataDeNascimento_FormattedTextField = new javax.swing.JFormattedTextField();
        instrutorSexoMasculino_RadioButton = new javax.swing.JRadioButton();
        instrutorSexoFeminino_RadioButton = new javax.swing.JRadioButton();
        instrutoresDataDeContratação_Label = new javax.swing.JLabel();
        instrutorDataDeContratação_FormattedTextField = new javax.swing.JFormattedTextField();
        instrutoresBotões_Panel = new javax.swing.JPanel();
        instrutoresInserir_Button = new javax.swing.JButton();
        instrutoresConsultar_Button = new javax.swing.JButton();
        instrutoresAlterar_Button = new javax.swing.JButton();
        instrutoresRemover_Button = new javax.swing.JButton();
        instrutoresLimpar_Button = new javax.swing.JButton();
        instrutoresContato_Panel = new javax.swing.JPanel();
        instrutoresTelefone_Label = new javax.swing.JLabel();
        instrutoresEmail_Label = new javax.swing.JLabel();
        instrutorEmail_TextField = new javax.swing.JTextField();
        instrutorTelefone_FormatedTextField = new javax.swing.JFormattedTextField();
        instrutoresEndereço_Panel = new javax.swing.JPanel();
        instrutoresLogradouro_Label = new javax.swing.JLabel();
        instrutorLogradouro_TextField = new javax.swing.JTextField();
        instrutoresNúmero_Label = new javax.swing.JLabel();
        instrutorNúmero_TextField = new javax.swing.JTextField();
        instrutoresComplemento_Label = new javax.swing.JLabel();
        instrutorComplemento_TextField = new javax.swing.JTextField();
        instrutoresBairro_Label = new javax.swing.JLabel();
        instrutorBairro_TextField = new javax.swing.JTextField();
        instrutoresCep_Label = new javax.swing.JLabel();
        instrutorCep_TextField = new javax.swing.JTextField();
        instrutoresCidade_Label = new javax.swing.JLabel();
        instrutorCidade_TextField = new javax.swing.JTextField();
        instrutoresUf_Label = new javax.swing.JLabel();
        instrutorEstado_ComboBox = new javax.swing.JComboBox();
        instrutorCategorias_Panel = new javax.swing.JPanel();
        instrutorCategoriaA_RadioButton = new javax.swing.JRadioButton();
        instrutorCategoriaC_RadioButton = new javax.swing.JRadioButton();
        instrutorCategoriaB_RadioButton = new javax.swing.JRadioButton();
        instrutorCategoriaD_RadioButton = new javax.swing.JRadioButton();
        instrutorCategoriaE_RadioButton = new javax.swing.JRadioButton();
        instrutorHorários_Panel = new javax.swing.JPanel();
        instrutorSegundaCheckBox = new javax.swing.JCheckBox();
        instrutorTerçaCheckBox = new javax.swing.JCheckBox();
        instrutorQuartaCheckBox = new javax.swing.JCheckBox();
        instrutorQuintaCheckBox = new javax.swing.JCheckBox();
        instrutorSextaCheckBox = new javax.swing.JCheckBox();
        instrutorSábadoCheckBox = new javax.swing.JCheckBox();
        instrutorDomingoCheckBox = new javax.swing.JCheckBox();
        instrutorHoraInicio_FormattedTextField = new javax.swing.JFormattedTextField();
        instrutorHoraFim_FormattedTextField = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        instrutoresCadastrados_ComboBox = new javax.swing.JComboBox<>();
        matrículaPanel = new javax.swing.JPanel();
        matrículaExames_Panel = new javax.swing.JPanel();
        matrículaAptoPsicotécnico_RadioButton = new javax.swing.JRadioButton();
        matrículaExamePsicotécnico_Label = new javax.swing.JLabel();
        matrículaExameMédico_Label = new javax.swing.JLabel();
        matrículaInaptoPisicotécnico_RadioButton = new javax.swing.JRadioButton();
        matrículaAptoMédico_RadioButton = new javax.swing.JRadioButton();
        matrículaInaptoMédico_RadioButton = new javax.swing.JRadioButton();
        matrículaResultadoExame_Label = new javax.swing.JLabel();
        matrículaId_Panel = new javax.swing.JPanel();
        matrículaId_TextField = new javax.swing.JTextField();
        matrículaAlunosCadastrados_Panel = new javax.swing.JPanel();
        matrículaAlunosCadastrados_ComboBox = new javax.swing.JComboBox<>();
        matrículaInstrutoresCadastrados_Panel = new javax.swing.JPanel();
        matrículaInstrutoresCadastrados_ComboBox = new javax.swing.JComboBox<>();
        matrículaCategorias_Panel = new javax.swing.JPanel();
        matrículaCategoriaA_RadioButton = new javax.swing.JRadioButton();
        matrículaCategoriaB_RadioButton = new javax.swing.JRadioButton();
        matrículaCategoriaE_RadioButton = new javax.swing.JRadioButton();
        matrículaCategoriaC_RadioButton = new javax.swing.JRadioButton();
        matrículaCategoriaD_RadioButton = new javax.swing.JRadioButton();
        matrículaBotões_Panel = new javax.swing.JPanel();
        matrículaInserir_Button = new javax.swing.JButton();
        matrículaConsultar_Button = new javax.swing.JButton();
        matrículaRemover_Button = new javax.swing.JButton();
        matrículaLimpar_Button = new javax.swing.JButton();
        matrículaAlterarButton = new javax.swing.JButton();
        matrículaDataMatrícula_Panel = new javax.swing.JPanel();
        matrículaDataMatrícula_FormattedTextField = new javax.swing.JFormattedTextField();
        horárioInstrutores_Panel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        matrículaHoráriosList = new javax.swing.JList<>();
        matrículaCadastrada_Panel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        matrículaCadastrada_List = new javax.swing.JList<>();
        filtroPanel = new javax.swing.JPanel();
        filtroHorário_Panel = new javax.swing.JPanel();
        filtroSegundaCheckBox = new javax.swing.JCheckBox();
        filtroTerçaCheckBox = new javax.swing.JCheckBox();
        filtroQuartaCheckBox = new javax.swing.JCheckBox();
        filtroQuintaCheckBox = new javax.swing.JCheckBox();
        filtroSextaCheckBox = new javax.swing.JCheckBox();
        filtroSábadoCheckBox = new javax.swing.JCheckBox();
        filtroDomingoCheckBox = new javax.swing.JCheckBox();
        filtroPerídoMatrícula_Panel = new javax.swing.JPanel();
        filtroDataMatrículaInício_FormattedTextField = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        filtroDataMatrículaFim_FormattedTextField = new javax.swing.JFormattedTextField();
        filtroAluno_Panel = new javax.swing.JPanel();
        filtroCidadeAlunoLabel = new javax.swing.JLabel();
        filtroCidadeAlunoTextField = new javax.swing.JTextField();
        filtroInstrutor_Panel = new javax.swing.JPanel();
        filtroCidadeAlunoLabel1 = new javax.swing.JLabel();
        filtroSexoMasculinoRadioButton = new javax.swing.JRadioButton();
        filtroSexoFemininoRadioButton = new javax.swing.JRadioButton();
        filtroCategorias_Panel = new javax.swing.JPanel();
        filtroCategoriaA_RadioButton = new javax.swing.JRadioButton();
        filtroCategoriaB_RadioButton = new javax.swing.JRadioButton();
        filtroCategoriaE_RadioButton = new javax.swing.JRadioButton();
        filtroCategoriaC_RadioButton = new javax.swing.JRadioButton();
        filtroCategoriaD_RadioButton = new javax.swing.JRadioButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        filtroResultadosTextArea = new javax.swing.JTextArea();
        filtroFiltrar_Button = new javax.swing.JButton();
        filtroLimpar_Button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Auto Escola");
        setMaximumSize(new java.awt.Dimension(800, 483));
        setMinimumSize(new java.awt.Dimension(800, 483));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                fecharJanela(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        abas_TabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        abas_TabbedPane.setMaximumSize(new java.awt.Dimension(800, 483));
        abas_TabbedPane.setMinimumSize(new java.awt.Dimension(800, 483));
        abas_TabbedPane.setPreferredSize(new java.awt.Dimension(800, 483));

        inicioPanel.setLayout(new java.awt.GridBagLayout());

        logoAutoEscolaLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/util/logo.png"))); // NOI18N
        inicioPanel.add(logoAutoEscolaLabel, new java.awt.GridBagConstraints());

        abas_TabbedPane.addTab("Inicio", inicioPanel);

        alunoPanel.setPreferredSize(new java.awt.Dimension(650, 445));

        alunoInserir_Button.setText("Inserir");
        alunoInserir_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inserirAluno(evt);
            }
        });

        alunoConsultar_Button.setText("Consultar");
        alunoConsultar_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultarAluno(evt);
            }
        });

        alunoAlterar_Button.setText("Alterar");
        alunoAlterar_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alterarAluno(evt);
            }
        });

        alunoRemover_Button.setText("Remover");
        alunoRemover_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removerAluno(evt);
            }
        });

        alunoLimpar_Button.setText("Limpar");
        alunoLimpar_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limparDadosAluno(evt);
            }
        });

        javax.swing.GroupLayout alunoBotões_PanelLayout = new javax.swing.GroupLayout(alunoBotões_Panel);
        alunoBotões_Panel.setLayout(alunoBotões_PanelLayout);
        alunoBotões_PanelLayout.setHorizontalGroup(
            alunoBotões_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alunoBotões_PanelLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(alunoInserir_Button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alunoConsultar_Button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alunoAlterar_Button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alunoRemover_Button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alunoLimpar_Button)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        alunoBotões_PanelLayout.setVerticalGroup(
            alunoBotões_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, alunoBotões_PanelLayout.createSequentialGroup()
                .addGroup(alunoBotões_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(alunoInserir_Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(alunoBotões_PanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(alunoBotões_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(alunoConsultar_Button)
                            .addComponent(alunoAlterar_Button)
                            .addComponent(alunoRemover_Button)
                            .addComponent(alunoLimpar_Button))))
                .addGap(0, 0, 0))
        );

        alunoDadosPessoais_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Dados Pessoais", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));
        alunoDadosPessoais_Panel.setMaximumSize(new java.awt.Dimension(600, 84));
        alunoDadosPessoais_Panel.setMinimumSize(new java.awt.Dimension(600, 84));
        alunoDadosPessoais_Panel.setPreferredSize(new java.awt.Dimension(600, 80));

        alunoNome_Label.setText("Nome:");

        alunoNome_TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alunoNome_TextFieldActionPerformed(evt);
            }
        });

        alunoCpf_Label.setText("CPF:");

        alunoSexo_Label.setText("Sexo:");

        alunoRg_Label.setText("RG:");

        try {
            alunoCpf_FormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        alunoCpf_FormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alunoCpf_FormattedTextFieldActionPerformed(evt);
            }
        });

        try {
            alunoRg_FormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        alunoRg_FormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alunoRg_FormattedTextFieldActionPerformed(evt);
            }
        });

        alunoDataDeNascimento_Label.setText("Data de Nascimento:");

        try {
            alunoDataDeNascimento_FormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        alunoSexobuttonGroup.add(alunoSexoMasculino_RadioButton);
        alunoSexoMasculino_RadioButton.setText("Masculino");
        alunoSexoMasculino_RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alunoSexoMasculino_RadioButtonActionPerformed(evt);
            }
        });

        alunoSexobuttonGroup.add(alunoSexoFeminino_RadioButton);
        alunoSexoFeminino_RadioButton.setText("Feminino");

        javax.swing.GroupLayout alunoDadosPessoais_PanelLayout = new javax.swing.GroupLayout(alunoDadosPessoais_Panel);
        alunoDadosPessoais_Panel.setLayout(alunoDadosPessoais_PanelLayout);
        alunoDadosPessoais_PanelLayout.setHorizontalGroup(
            alunoDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alunoDadosPessoais_PanelLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(alunoDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(alunoDadosPessoais_PanelLayout.createSequentialGroup()
                        .addComponent(alunoNome_Label)
                        .addGap(7, 7, 7)
                        .addComponent(alunoNome_TextField))
                    .addGroup(alunoDadosPessoais_PanelLayout.createSequentialGroup()
                        .addComponent(alunoCpf_Label)
                        .addGap(15, 15, 15)
                        .addComponent(alunoCpf_FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(alunoDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(alunoDadosPessoais_PanelLayout.createSequentialGroup()
                                .addComponent(alunoSexo_Label)
                                .addGap(8, 8, 8)
                                .addComponent(alunoSexoMasculino_RadioButton)
                                .addGap(8, 8, 8)
                                .addComponent(alunoSexoFeminino_RadioButton))
                            .addGroup(alunoDadosPessoais_PanelLayout.createSequentialGroup()
                                .addComponent(alunoRg_Label)
                                .addGap(6, 6, 6)
                                .addComponent(alunoRg_FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(alunoDataDeNascimento_Label)
                                .addGap(7, 7, 7)
                                .addComponent(alunoDataDeNascimento_FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        alunoDadosPessoais_PanelLayout.setVerticalGroup(
            alunoDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alunoDadosPessoais_PanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(alunoDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alunoDadosPessoais_PanelLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(alunoNome_Label))
                    .addComponent(alunoNome_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(alunoDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(alunoCpf_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(alunoDadosPessoais_PanelLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(alunoDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(alunoDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(alunoRg_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(alunoCpf_FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(alunoRg_FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(alunoDadosPessoais_PanelLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(alunoDataDeNascimento_Label))
                            .addComponent(alunoDataDeNascimento_FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 4, Short.MAX_VALUE)
                .addGroup(alunoDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alunoDadosPessoais_PanelLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(alunoSexo_Label))
                    .addComponent(alunoSexoMasculino_RadioButton)
                    .addComponent(alunoSexoFeminino_RadioButton))
                .addContainerGap())
        );

        alunoContato_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Contato", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));
        alunoContato_Panel.setMaximumSize(new java.awt.Dimension(600, 76));
        alunoContato_Panel.setMinimumSize(new java.awt.Dimension(600, 63));
        alunoContato_Panel.setPreferredSize(new java.awt.Dimension(600, 63));

        alunoTelefone_Label.setText("Tel./Cel.:");

        alunoEmail_Label.setText("E-mail:");

        try {
            alunoTelefone_FormatedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        alunoTelefone_FormatedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alunoTelefone_FormatedTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout alunoContato_PanelLayout = new javax.swing.GroupLayout(alunoContato_Panel);
        alunoContato_Panel.setLayout(alunoContato_PanelLayout);
        alunoContato_PanelLayout.setHorizontalGroup(
            alunoContato_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alunoContato_PanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(alunoTelefone_Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(alunoTelefone_FormatedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(alunoEmail_Label)
                .addGap(5, 5, 5)
                .addComponent(alunoEmail_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        alunoContato_PanelLayout.setVerticalGroup(
            alunoContato_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alunoContato_PanelLayout.createSequentialGroup()
                .addGroup(alunoContato_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alunoContato_PanelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(alunoContato_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(alunoTelefone_Label)
                            .addComponent(alunoTelefone_FormatedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(alunoContato_PanelLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(alunoEmail_Label))
                    .addGroup(alunoContato_PanelLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(alunoEmail_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17))
        );

        alunoEndereço_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Endereço", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));
        alunoEndereço_Panel.setMaximumSize(new java.awt.Dimension(600, 2147483647));
        alunoEndereço_Panel.setMinimumSize(new java.awt.Dimension(600, 114));
        alunoEndereço_Panel.setPreferredSize(new java.awt.Dimension(600, 114));

        alunoLogradouro_Label.setText("Logradouro:");

        alunoNúmero_Label.setText("Nº:");

        alunoComplemento_Label.setText("Complemento:");

        alunoBairro_Label.setText("Bairro:");

        alunoCep_Label.setText("CEP:");

        alunoCidade_Label.setText("Cidade:");

        alunoUf_Label.setText("UF:");

        alunoEstado_ComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));
        alunoEstado_ComboBox.setSelectedIndex(-1);
        alunoEstado_ComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alunoEstado_ComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout alunoEndereço_PanelLayout = new javax.swing.GroupLayout(alunoEndereço_Panel);
        alunoEndereço_Panel.setLayout(alunoEndereço_PanelLayout);
        alunoEndereço_PanelLayout.setHorizontalGroup(
            alunoEndereço_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alunoEndereço_PanelLayout.createSequentialGroup()
                .addGroup(alunoEndereço_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alunoEndereço_PanelLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(alunoLogradouro_Label)
                        .addGap(4, 4, 4)
                        .addComponent(alunoLogradouro_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(alunoEndereço_PanelLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(alunoComplemento_Label)
                        .addGap(4, 4, 4)
                        .addComponent(alunoComplemento_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(alunoBairro_Label)
                        .addGap(5, 5, 5)
                        .addComponent(alunoBairro_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(alunoNúmero_Label)
                        .addGap(4, 4, 4)
                        .addComponent(alunoNúmero_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(alunoEndereço_PanelLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(alunoCep_Label)
                        .addGap(4, 4, 4)
                        .addComponent(alunoCep_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(alunoCidade_Label)
                        .addGap(10, 10, 10)
                        .addComponent(alunoCidade_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(alunoUf_Label)
                        .addGap(7, 7, 7)
                        .addComponent(alunoEstado_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        alunoEndereço_PanelLayout.setVerticalGroup(
            alunoEndereço_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alunoEndereço_PanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(alunoEndereço_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alunoEndereço_PanelLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(alunoLogradouro_Label))
                    .addComponent(alunoLogradouro_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(alunoEndereço_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(alunoComplemento_Label)
                    .addComponent(alunoComplemento_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alunoBairro_Label)
                    .addComponent(alunoBairro_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alunoNúmero_Label)
                    .addComponent(alunoNúmero_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(alunoEndereço_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(alunoCep_Label)
                    .addComponent(alunoCep_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alunoCidade_Label)
                    .addComponent(alunoCidade_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alunoUf_Label)
                    .addComponent(alunoEstado_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        alunoCadastrado_ComboBox.setModel(new javax.swing.DefaultComboBoxModel(alunos_cadastrados));

        javax.swing.GroupLayout alunoPanelLayout = new javax.swing.GroupLayout(alunoPanel);
        alunoPanel.setLayout(alunoPanelLayout);
        alunoPanelLayout.setHorizontalGroup(
            alunoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alunoPanelLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(alunoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(alunoContato_Panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(alunoDadosPessoais_Panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(alunoPanelLayout.createSequentialGroup()
                        .addComponent(alunoCadastrado_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(alunoBotões_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(alunoEndereço_Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        alunoPanelLayout.setVerticalGroup(
            alunoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alunoPanelLayout.createSequentialGroup()
                .addGroup(alunoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alunoPanelLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(alunoBotões_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(alunoPanelLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(alunoCadastrado_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14)
                .addComponent(alunoDadosPessoais_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(alunoContato_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(alunoEndereço_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        abas_TabbedPane.addTab("Aluno", alunoPanel);

        instrutoresDadosPessoais_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Dados Pessoais", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));
        instrutoresDadosPessoais_Panel.setMaximumSize(new java.awt.Dimension(600, 84));
        instrutoresDadosPessoais_Panel.setMinimumSize(new java.awt.Dimension(600, 84));
        instrutoresDadosPessoais_Panel.setPreferredSize(new java.awt.Dimension(600, 80));

        instrutoresNome_Label.setText("Nome:");

        instrutorNome_TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instrutorNome_TextFieldActionPerformed(evt);
            }
        });

        instrutoresCpf_Label.setText("CPF:");

        instrutoresSexo_Label.setText("Sexo:");

        instrutoresRg_Label.setText("RG:");

        try {
            instrutorCpf_FormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        instrutorCpf_FormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instrutorCpf_FormattedTextFieldActionPerformed(evt);
            }
        });

        try {
            instrutorRg_FormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        instrutorRg_FormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instrutorRg_FormattedTextFieldActionPerformed(evt);
            }
        });

        instrutoresDataDeNascimento_Label.setText("Data de Nascimento:");

        try {
            instrutorDataDeNascimento_FormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        instrutorSexo_buttonGroup.add(instrutorSexoMasculino_RadioButton);
        instrutorSexoMasculino_RadioButton.setText("Masculino");
        instrutorSexoMasculino_RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instrutorSexoMasculino_RadioButtonActionPerformed(evt);
            }
        });

        instrutorSexo_buttonGroup.add(instrutorSexoFeminino_RadioButton);
        instrutorSexoFeminino_RadioButton.setText("Feminino");

        instrutoresDataDeContratação_Label.setText("Data de Contratação:");

        try {
            instrutorDataDeContratação_FormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        instrutorDataDeContratação_FormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instrutorDataDeContratação_FormattedTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout instrutoresDadosPessoais_PanelLayout = new javax.swing.GroupLayout(instrutoresDadosPessoais_Panel);
        instrutoresDadosPessoais_Panel.setLayout(instrutoresDadosPessoais_PanelLayout);
        instrutoresDadosPessoais_PanelLayout.setHorizontalGroup(
            instrutoresDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(instrutoresDadosPessoais_PanelLayout.createSequentialGroup()
                .addGroup(instrutoresDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(instrutoresDadosPessoais_PanelLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(instrutoresSexo_Label)
                        .addGap(8, 8, 8)
                        .addComponent(instrutorSexoMasculino_RadioButton)
                        .addGap(8, 8, 8)
                        .addComponent(instrutorSexoFeminino_RadioButton)
                        .addGap(48, 48, 48)
                        .addComponent(instrutoresDataDeNascimento_Label)
                        .addGap(7, 7, 7)
                        .addComponent(instrutorDataDeNascimento_FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(instrutoresDataDeContratação_Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(instrutorDataDeContratação_FormattedTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
                    .addGroup(instrutoresDadosPessoais_PanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(instrutoresCpf_Label)
                        .addGap(7, 7, 7)
                        .addComponent(instrutorCpf_FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82)
                        .addComponent(instrutoresRg_Label)
                        .addGap(6, 6, 6)
                        .addComponent(instrutorRg_FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(instrutoresDadosPessoais_PanelLayout.createSequentialGroup()
                        .addComponent(instrutoresNome_Label)
                        .addGap(7, 7, 7)
                        .addComponent(instrutorNome_TextField)))
                .addGap(24, 24, 24))
        );
        instrutoresDadosPessoais_PanelLayout.setVerticalGroup(
            instrutoresDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(instrutoresDadosPessoais_PanelLayout.createSequentialGroup()
                .addGroup(instrutoresDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(instrutoresDadosPessoais_PanelLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(instrutoresNome_Label))
                    .addGroup(instrutoresDadosPessoais_PanelLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(instrutorNome_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addGroup(instrutoresDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(instrutoresCpf_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(instrutoresDadosPessoais_PanelLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(instrutoresDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(instrutorCpf_FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(instrutoresRg_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(instrutorRg_FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(3, 3, 3)
                .addGroup(instrutoresDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(instrutoresDadosPessoais_PanelLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(instrutoresSexo_Label))
                    .addGroup(instrutoresDadosPessoais_PanelLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(instrutorSexoMasculino_RadioButton))
                    .addGroup(instrutoresDadosPessoais_PanelLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(instrutorSexoFeminino_RadioButton))
                    .addGroup(instrutoresDadosPessoais_PanelLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(instrutoresDataDeNascimento_Label))
                    .addGroup(instrutoresDadosPessoais_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(instrutorDataDeNascimento_FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(instrutoresDataDeContratação_Label)
                        .addComponent(instrutorDataDeContratação_FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        instrutoresInserir_Button.setText("Inserir");
        instrutoresInserir_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inserirInstrutor(evt);
            }
        });

        instrutoresConsultar_Button.setText("Consultar");
        instrutoresConsultar_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultarInstrutor(evt);
            }
        });

        instrutoresAlterar_Button.setText("Alterar");
        instrutoresAlterar_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alterarInstrutor(evt);
            }
        });

        instrutoresRemover_Button.setText("Remover");
        instrutoresRemover_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removerInstrutor(evt);
            }
        });

        instrutoresLimpar_Button.setText("Limpar");
        instrutoresLimpar_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limparDadosInstrutor(evt);
            }
        });

        javax.swing.GroupLayout instrutoresBotões_PanelLayout = new javax.swing.GroupLayout(instrutoresBotões_Panel);
        instrutoresBotões_Panel.setLayout(instrutoresBotões_PanelLayout);
        instrutoresBotões_PanelLayout.setHorizontalGroup(
            instrutoresBotões_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(instrutoresBotões_PanelLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(instrutoresInserir_Button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(instrutoresConsultar_Button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(instrutoresAlterar_Button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(instrutoresRemover_Button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(instrutoresLimpar_Button)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        instrutoresBotões_PanelLayout.setVerticalGroup(
            instrutoresBotões_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, instrutoresBotões_PanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(instrutoresBotões_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(instrutoresInserir_Button)
                    .addComponent(instrutoresConsultar_Button)
                    .addComponent(instrutoresAlterar_Button)
                    .addComponent(instrutoresRemover_Button)
                    .addComponent(instrutoresLimpar_Button))
                .addGap(1, 1, 1))
        );

        instrutoresContato_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Contato", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));
        instrutoresContato_Panel.setMaximumSize(new java.awt.Dimension(600, 76));
        instrutoresContato_Panel.setMinimumSize(new java.awt.Dimension(600, 63));
        instrutoresContato_Panel.setPreferredSize(new java.awt.Dimension(600, 63));

        instrutoresTelefone_Label.setText("Tel./Cel.:");

        instrutoresEmail_Label.setText("E-mail:");

        try {
            instrutorTelefone_FormatedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout instrutoresContato_PanelLayout = new javax.swing.GroupLayout(instrutoresContato_Panel);
        instrutoresContato_Panel.setLayout(instrutoresContato_PanelLayout);
        instrutoresContato_PanelLayout.setHorizontalGroup(
            instrutoresContato_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(instrutoresContato_PanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(instrutoresTelefone_Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(instrutorTelefone_FormatedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(instrutoresEmail_Label)
                .addGap(5, 5, 5)
                .addComponent(instrutorEmail_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        instrutoresContato_PanelLayout.setVerticalGroup(
            instrutoresContato_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(instrutoresContato_PanelLayout.createSequentialGroup()
                .addGroup(instrutoresContato_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(instrutoresContato_PanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(instrutoresContato_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(instrutoresTelefone_Label)
                            .addComponent(instrutorTelefone_FormatedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(instrutoresContato_PanelLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(instrutoresEmail_Label))
                    .addGroup(instrutoresContato_PanelLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(instrutorEmail_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11))
        );

        instrutoresEndereço_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Endereço", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));
        instrutoresEndereço_Panel.setMaximumSize(new java.awt.Dimension(600, 2147483647));
        instrutoresEndereço_Panel.setMinimumSize(new java.awt.Dimension(600, 114));
        instrutoresEndereço_Panel.setPreferredSize(new java.awt.Dimension(600, 114));
        instrutoresEndereço_Panel.setLayout(new java.awt.GridBagLayout());

        instrutoresLogradouro_Label.setText("Logradouro:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 20, 0, 0);
        instrutoresEndereço_Panel.add(instrutoresLogradouro_Label, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.ipadx = 492;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 4, 0, 15);
        instrutoresEndereço_Panel.add(instrutorLogradouro_TextField, gridBagConstraints);

        instrutoresNúmero_Label.setText("Nº:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 11, 7, 0);
        instrutoresEndereço_Panel.add(instrutoresNúmero_Label, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 44;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 7, 15);
        instrutoresEndereço_Panel.add(instrutorNúmero_TextField, gridBagConstraints);

        instrutoresComplemento_Label.setText("Complemento:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 10, 7, 0);
        instrutoresEndereço_Panel.add(instrutoresComplemento_Label, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 194;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 7, 0);
        instrutoresEndereço_Panel.add(instrutorComplemento_TextField, gridBagConstraints);

        instrutoresBairro_Label.setText("Bairro:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 10, 7, 0);
        instrutoresEndereço_Panel.add(instrutoresBairro_Label, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 164;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 7, 0);
        instrutoresEndereço_Panel.add(instrutorBairro_TextField, gridBagConstraints);

        instrutoresCep_Label.setText("CEP:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 56, 10, 0);
        instrutoresEndereço_Panel.add(instrutoresCep_Label, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 108;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 10, 0);
        instrutoresEndereço_Panel.add(instrutorCep_TextField, gridBagConstraints);

        instrutoresCidade_Label.setText("Cidade:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 0);
        instrutoresEndereço_Panel.add(instrutoresCidade_Label, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 240;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 0);
        instrutoresEndereço_Panel.add(instrutorCidade_TextField, gridBagConstraints);

        instrutoresUf_Label.setText("UF:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 10, 0);
        instrutoresEndereço_Panel.add(instrutoresUf_Label, gridBagConstraints);

        instrutorEstado_ComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));
        instrutorEstado_ComboBox.setSelectedIndex(-1);
        instrutorEstado_ComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instrutorEstado_ComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 10, 15);
        instrutoresEndereço_Panel.add(instrutorEstado_ComboBox, gridBagConstraints);

        instrutorCategorias_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Categorias"));
        instrutorCategorias_Panel.setLayout(new java.awt.GridBagLayout());

        instrutorCategoriaA_RadioButton.setText("A");
        instrutorCategoriaA_RadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instrutorCategoriaA_RadioButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 1, 0);
        instrutorCategorias_Panel.add(instrutorCategoriaA_RadioButton, gridBagConstraints);

        instrutorCategoriaC_RadioButton.setText("C");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 1, 0);
        instrutorCategorias_Panel.add(instrutorCategoriaC_RadioButton, gridBagConstraints);

        instrutorCategoriaB_RadioButton.setText("B");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 1, 0);
        instrutorCategorias_Panel.add(instrutorCategoriaB_RadioButton, gridBagConstraints);

        instrutorCategoriaD_RadioButton.setText("D");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 1, 0);
        instrutorCategorias_Panel.add(instrutorCategoriaD_RadioButton, gridBagConstraints);

        instrutorCategoriaE_RadioButton.setText("E");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 1, 10);
        instrutorCategorias_Panel.add(instrutorCategoriaE_RadioButton, gridBagConstraints);

        instrutorHorários_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Horários"));
        instrutorHorários_Panel.setLayout(new java.awt.GridBagLayout());

        instrutorSegundaCheckBox.setText("Segunda");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        instrutorHorários_Panel.add(instrutorSegundaCheckBox, gridBagConstraints);

        instrutorTerçaCheckBox.setText("Terça");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        instrutorHorários_Panel.add(instrutorTerçaCheckBox, gridBagConstraints);

        instrutorQuartaCheckBox.setText("Quarta");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        instrutorHorários_Panel.add(instrutorQuartaCheckBox, gridBagConstraints);

        instrutorQuintaCheckBox.setText("Quinta");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        instrutorHorários_Panel.add(instrutorQuintaCheckBox, gridBagConstraints);

        instrutorSextaCheckBox.setText("Sexta");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        instrutorHorários_Panel.add(instrutorSextaCheckBox, gridBagConstraints);

        instrutorSábadoCheckBox.setText("Sábado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        instrutorHorários_Panel.add(instrutorSábadoCheckBox, gridBagConstraints);

        instrutorDomingoCheckBox.setText("Domingo");
        instrutorDomingoCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instrutorDomingoCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        instrutorHorários_Panel.add(instrutorDomingoCheckBox, gridBagConstraints);

        try {
            instrutorHoraInicio_FormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        instrutorHoraInicio_FormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instrutorHoraInicio_FormattedTextFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 0, 7);
        instrutorHorários_Panel.add(instrutorHoraInicio_FormattedTextField, gridBagConstraints);

        try {
            instrutorHoraFim_FormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        instrutorHoraFim_FormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instrutorHoraFim_FormattedTextFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 7);
        instrutorHorários_Panel.add(instrutorHoraFim_FormattedTextField, gridBagConstraints);

        jLabel1.setText("Hora Início:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 6, 0, 0);
        instrutorHorários_Panel.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Hora Fim:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 17, 0, 0);
        instrutorHorários_Panel.add(jLabel2, gridBagConstraints);

        instrutoresCadastrados_ComboBox.setModel(new javax.swing.DefaultComboBoxModel(instrutores_cadastrados));

        javax.swing.GroupLayout instrutorPanelLayout = new javax.swing.GroupLayout(instrutorPanel);
        instrutorPanel.setLayout(instrutorPanelLayout);
        instrutorPanelLayout.setHorizontalGroup(
            instrutorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(instrutorPanelLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(instrutorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(instrutorPanelLayout.createSequentialGroup()
                        .addComponent(instrutoresCadastrados_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(instrutoresBotões_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(instrutoresDadosPessoais_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(instrutoresContato_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(instrutoresEndereço_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(instrutorPanelLayout.createSequentialGroup()
                        .addComponent(instrutorCategorias_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(instrutorHorários_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        instrutorPanelLayout.setVerticalGroup(
            instrutorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(instrutorPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(instrutorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(instrutoresCadastrados_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(instrutoresBotões_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(instrutoresDadosPessoais_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(instrutoresContato_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(instrutoresEndereço_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(instrutorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(instrutorCategorias_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(instrutorHorários_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        abas_TabbedPane.addTab("Instrutor", instrutorPanel);

        matrículaPanel.setPreferredSize(new java.awt.Dimension(800, 478));
        matrículaPanel.setLayout(new java.awt.GridBagLayout());

        matrículaExames_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Exames"));
        matrículaExames_Panel.setMaximumSize(new java.awt.Dimension(311, 87));
        matrículaExames_Panel.setMinimumSize(new java.awt.Dimension(311, 87));
        matrículaExames_Panel.setPreferredSize(new java.awt.Dimension(311, 87));
        matrículaExames_Panel.setLayout(new java.awt.GridBagLayout());

        matrículaExamePsicotécnico_buttonGroup.add(matrículaAptoPsicotécnico_RadioButton);
        matrículaAptoPsicotécnico_RadioButton.setText("Apto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        matrículaExames_Panel.add(matrículaAptoPsicotécnico_RadioButton, gridBagConstraints);

        matrículaExamePsicotécnico_Label.setText("Exame Psicotécinico:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 16, 0, 0);
        matrículaExames_Panel.add(matrículaExamePsicotécnico_Label, gridBagConstraints);

        matrículaExameMédico_Label.setText("Exame Médico:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        matrículaExames_Panel.add(matrículaExameMédico_Label, gridBagConstraints);

        matrículaExamePsicotécnico_buttonGroup.add(matrículaInaptoPisicotécnico_RadioButton);
        matrículaInaptoPisicotécnico_RadioButton.setText("Inapto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        matrículaExames_Panel.add(matrículaInaptoPisicotécnico_RadioButton, gridBagConstraints);

        matrículaExameMédico_buttonGroup.add(matrículaAptoMédico_RadioButton);
        matrículaAptoMédico_RadioButton.setText("Apto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        matrículaExames_Panel.add(matrículaAptoMédico_RadioButton, gridBagConstraints);

        matrículaExameMédico_buttonGroup.add(matrículaInaptoMédico_RadioButton);
        matrículaInaptoMédico_RadioButton.setText("Inapto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        matrículaExames_Panel.add(matrículaInaptoMédico_RadioButton, gridBagConstraints);

        matrículaResultadoExame_Label.setText("Resultado:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 16, 0, 0);
        matrículaExames_Panel.add(matrículaResultadoExame_Label, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 266;
        gridBagConstraints.ipady = -1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 52, 0, 0);
        matrículaPanel.add(matrículaExames_Panel, gridBagConstraints);

        matrículaId_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "ID"));
        matrículaId_Panel.setLayout(new java.awt.GridBagLayout());

        matrículaId_TextField.setEnabled(false);
        matrículaId_TextField.setPreferredSize(new java.awt.Dimension(20, 20));
        matrículaId_TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                matrículaId_TextFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 34;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 8, 9);
        matrículaId_Panel.add(matrículaId_TextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 0);
        matrículaPanel.add(matrículaId_Panel, gridBagConstraints);

        matrículaAlunosCadastrados_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Alunos Cadastrados"));
        matrículaAlunosCadastrados_Panel.setMaximumSize(new java.awt.Dimension(325, 35));
        matrículaAlunosCadastrados_Panel.setMinimumSize(new java.awt.Dimension(325, 35));
        matrículaAlunosCadastrados_Panel.setPreferredSize(new java.awt.Dimension(325, 35));

        matrículaAlunosCadastrados_ComboBox.setModel(new javax.swing.DefaultComboBoxModel(alunos_cadastrados));
        matrículaAlunosCadastrados_ComboBox.setMaximumSize(new java.awt.Dimension(28, 20));
        matrículaAlunosCadastrados_ComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                matrículaAlunosCadastrados_ComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout matrículaAlunosCadastrados_PanelLayout = new javax.swing.GroupLayout(matrículaAlunosCadastrados_Panel);
        matrículaAlunosCadastrados_Panel.setLayout(matrículaAlunosCadastrados_PanelLayout);
        matrículaAlunosCadastrados_PanelLayout.setHorizontalGroup(
            matrículaAlunosCadastrados_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(matrículaAlunosCadastrados_PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(matrículaAlunosCadastrados_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(116, Short.MAX_VALUE))
        );
        matrículaAlunosCadastrados_PanelLayout.setVerticalGroup(
            matrículaAlunosCadastrados_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(matrículaAlunosCadastrados_PanelLayout.createSequentialGroup()
                .addComponent(matrículaAlunosCadastrados_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = -109;
        gridBagConstraints.ipady = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 51, 0, 0);
        matrículaPanel.add(matrículaAlunosCadastrados_Panel, gridBagConstraints);

        matrículaInstrutoresCadastrados_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Instrutores Cadastrados"));
        matrículaInstrutoresCadastrados_Panel.setMaximumSize(new java.awt.Dimension(80, 35));
        matrículaInstrutoresCadastrados_Panel.setMinimumSize(new java.awt.Dimension(80, 35));
        matrículaInstrutoresCadastrados_Panel.setPreferredSize(new java.awt.Dimension(325, 35));

        matrículaInstrutoresCadastrados_ComboBox.setModel(new javax.swing.DefaultComboBoxModel(instrutores_cadastrados));
        matrículaInstrutoresCadastrados_ComboBox.setMaximumSize(new java.awt.Dimension(28, 20));
        matrículaInstrutoresCadastrados_ComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                atualizarHorários(evt);
            }
        });

        javax.swing.GroupLayout matrículaInstrutoresCadastrados_PanelLayout = new javax.swing.GroupLayout(matrículaInstrutoresCadastrados_Panel);
        matrículaInstrutoresCadastrados_Panel.setLayout(matrículaInstrutoresCadastrados_PanelLayout);
        matrículaInstrutoresCadastrados_PanelLayout.setHorizontalGroup(
            matrículaInstrutoresCadastrados_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, matrículaInstrutoresCadastrados_PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(matrículaInstrutoresCadastrados_ComboBox, 0, 186, Short.MAX_VALUE)
                .addContainerGap())
        );
        matrículaInstrutoresCadastrados_PanelLayout.setVerticalGroup(
            matrículaInstrutoresCadastrados_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(matrículaInstrutoresCadastrados_PanelLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(matrículaInstrutoresCadastrados_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 136;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 51, 0, 0);
        matrículaPanel.add(matrículaInstrutoresCadastrados_Panel, gridBagConstraints);

        matrículaCategorias_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Categorias"));
        matrículaCategorias_Panel.setLayout(new java.awt.GridBagLayout());

        matrículaCategoriaA_RadioButton.setText("Categoria A");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        matrículaCategorias_Panel.add(matrículaCategoriaA_RadioButton, gridBagConstraints);

        matrículaCategoriaB_RadioButton.setText("Categoria B");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        matrículaCategorias_Panel.add(matrículaCategoriaB_RadioButton, gridBagConstraints);

        matrículaCategoriaE_RadioButton.setText("Categoria E");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        matrículaCategorias_Panel.add(matrículaCategoriaE_RadioButton, gridBagConstraints);

        matrículaCategoriaC_RadioButton.setText("Categoria C");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        matrículaCategorias_Panel.add(matrículaCategoriaC_RadioButton, gridBagConstraints);

        matrículaCategoriaD_RadioButton.setText("Categoria D");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        matrículaCategorias_Panel.add(matrículaCategoriaD_RadioButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 156;
        gridBagConstraints.ipady = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 52, 75, 0);
        matrículaPanel.add(matrículaCategorias_Panel, gridBagConstraints);

        matrículaBotões_Panel.setLayout(new java.awt.GridBagLayout());

        matrículaInserir_Button.setText("Inserir");
        matrículaInserir_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inserirMatrícula(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        matrículaBotões_Panel.add(matrículaInserir_Button, gridBagConstraints);

        matrículaConsultar_Button.setText("Consultar");
        matrículaConsultar_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultarMatrícula(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        matrículaBotões_Panel.add(matrículaConsultar_Button, gridBagConstraints);

        matrículaRemover_Button.setText("Remover");
        matrículaRemover_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removerMatrícula(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        matrículaBotões_Panel.add(matrículaRemover_Button, gridBagConstraints);

        matrículaLimpar_Button.setText("Limpar");
        matrículaLimpar_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limparCamposMatrícula(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        matrículaBotões_Panel.add(matrículaLimpar_Button, gridBagConstraints);

        matrículaAlterarButton.setText("Alterar");
        matrículaAlterarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alterarMatrícula(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        matrículaBotões_Panel.add(matrículaAlterarButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipady = 33;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 115);
        matrículaPanel.add(matrículaBotões_Panel, gridBagConstraints);

        matrículaDataMatrícula_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Data Matrícula"));

        try {
            matrículaDataMatrícula_FormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        matrículaDataMatrícula_FormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                matrículaDataMatrícula_FormattedTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout matrículaDataMatrícula_PanelLayout = new javax.swing.GroupLayout(matrículaDataMatrícula_Panel);
        matrículaDataMatrícula_Panel.setLayout(matrículaDataMatrícula_PanelLayout);
        matrículaDataMatrícula_PanelLayout.setHorizontalGroup(
            matrículaDataMatrícula_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(matrículaDataMatrícula_PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(matrículaDataMatrícula_FormattedTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );
        matrículaDataMatrícula_PanelLayout.setVerticalGroup(
            matrículaDataMatrícula_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, matrículaDataMatrícula_PanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(matrículaDataMatrícula_FormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 44;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 18, 0, 0);
        matrículaPanel.add(matrículaDataMatrícula_Panel, gridBagConstraints);

        horárioInstrutores_Panel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Horários Disponíveis"));

        matrículaHoráriosList.setModel(modelo_lista_horáriosCadastrados);
        jScrollPane3.setViewportView(matrículaHoráriosList);

        javax.swing.GroupLayout horárioInstrutores_Panel1Layout = new javax.swing.GroupLayout(horárioInstrutores_Panel1);
        horárioInstrutores_Panel1.setLayout(horárioInstrutores_Panel1Layout);
        horárioInstrutores_Panel1Layout.setHorizontalGroup(
            horárioInstrutores_Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(horárioInstrutores_Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        horárioInstrutores_Panel1Layout.setVerticalGroup(
            horárioInstrutores_Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(horárioInstrutores_Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.ipady = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 6, 0, 115);
        matrículaPanel.add(horárioInstrutores_Panel1, gridBagConstraints);

        matrículaCadastrada_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Matrículas Cadastradas"));
        matrículaCadastrada_Panel.setMaximumSize(new java.awt.Dimension(115, 128));
        matrículaCadastrada_Panel.setPreferredSize(new java.awt.Dimension(115, 128));
        matrículaCadastrada_Panel.setLayout(new java.awt.GridBagLayout());

        matrículaCadastrada_List.setModel(modelo_lista_matrículasCadastradas);
        jScrollPane1.setViewportView(matrículaCadastrada_List);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 52;
        gridBagConstraints.ipady = 57;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(9, 15, 12, 15);
        matrículaCadastrada_Panel.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 68;
        gridBagConstraints.ipady = -6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 51, 0, 0);
        matrículaPanel.add(matrículaCadastrada_Panel, gridBagConstraints);

        abas_TabbedPane.addTab("Matrícula", matrículaPanel);

        filtroPanel.setLayout(new java.awt.GridBagLayout());

        filtroHorário_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Horário"));
        filtroHorário_Panel.setMaximumSize(new java.awt.Dimension(262, 75));
        filtroHorário_Panel.setPreferredSize(new java.awt.Dimension(262, 75));
        filtroHorário_Panel.setLayout(new java.awt.GridBagLayout());

        filtroSegundaCheckBox.setText("Segunda");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        filtroHorário_Panel.add(filtroSegundaCheckBox, gridBagConstraints);

        filtroTerçaCheckBox.setText("Terça");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        filtroHorário_Panel.add(filtroTerçaCheckBox, gridBagConstraints);

        filtroQuartaCheckBox.setText("Quarta");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        filtroHorário_Panel.add(filtroQuartaCheckBox, gridBagConstraints);

        filtroQuintaCheckBox.setText("Quinta");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        filtroHorário_Panel.add(filtroQuintaCheckBox, gridBagConstraints);

        filtroSextaCheckBox.setText("Sexta");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        filtroHorário_Panel.add(filtroSextaCheckBox, gridBagConstraints);

        filtroSábadoCheckBox.setText("Sábado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        filtroHorário_Panel.add(filtroSábadoCheckBox, gridBagConstraints);

        filtroDomingoCheckBox.setText("Domingo");
        filtroDomingoCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filtroDomingoCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        filtroHorário_Panel.add(filtroDomingoCheckBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 38;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 42, 0, 0);
        filtroPanel.add(filtroHorário_Panel, gridBagConstraints);

        filtroPerídoMatrícula_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Período da Matrícula"));
        filtroPerídoMatrícula_Panel.setMaximumSize(new java.awt.Dimension(392, 75));
        filtroPerídoMatrícula_Panel.setMinimumSize(new java.awt.Dimension(392, 75));
        filtroPerídoMatrícula_Panel.setPreferredSize(new java.awt.Dimension(392, 75));
        filtroPerídoMatrícula_Panel.setLayout(new java.awt.GridBagLayout());

        try {
            filtroDataMatrículaInício_FormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        filtroDataMatrículaInício_FormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filtroDataMatrículaInício_FormattedTextFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        filtroPerídoMatrícula_Panel.add(filtroDataMatrículaInício_FormattedTextField, gridBagConstraints);

        jLabel4.setText("Início:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        filtroPerídoMatrícula_Panel.add(jLabel4, gridBagConstraints);

        jLabel5.setText("Fim:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 18, 0, 5);
        filtroPerídoMatrícula_Panel.add(jLabel5, gridBagConstraints);

        try {
            filtroDataMatrículaFim_FormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        filtroDataMatrículaFim_FormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filtroDataMatrículaFim_FormattedTextFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
        filtroPerídoMatrícula_Panel.add(filtroDataMatrículaFim_FormattedTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = -86;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 34, 0, 0);
        filtroPanel.add(filtroPerídoMatrícula_Panel, gridBagConstraints);

        filtroAluno_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Aluno"));
        filtroAluno_Panel.setMaximumSize(new java.awt.Dimension(307, 75));
        filtroAluno_Panel.setMinimumSize(new java.awt.Dimension(307, 75));
        filtroAluno_Panel.setPreferredSize(new java.awt.Dimension(307, 75));
        filtroAluno_Panel.setLayout(new java.awt.GridBagLayout());

        filtroCidadeAlunoLabel.setText("Cidade:");
        filtroAluno_Panel.add(filtroCidadeAlunoLabel, new java.awt.GridBagConstraints());

        filtroCidadeAlunoTextField.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        filtroAluno_Panel.add(filtroCidadeAlunoTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 33, 0, 0);
        filtroPanel.add(filtroAluno_Panel, gridBagConstraints);

        filtroInstrutor_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Instrutor"));
        filtroInstrutor_Panel.setMaximumSize(new java.awt.Dimension(392, 75));
        filtroInstrutor_Panel.setMinimumSize(new java.awt.Dimension(392, 75));
        filtroInstrutor_Panel.setPreferredSize(new java.awt.Dimension(392, 75));
        filtroInstrutor_Panel.setLayout(new java.awt.GridBagLayout());

        filtroCidadeAlunoLabel1.setText("Sexo:");
        filtroInstrutor_Panel.add(filtroCidadeAlunoLabel1, new java.awt.GridBagConstraints());

        filtroSexoInstrutor.add(filtroSexoMasculinoRadioButton);
        filtroSexoMasculinoRadioButton.setText("Masculino");
        filtroInstrutor_Panel.add(filtroSexoMasculinoRadioButton, new java.awt.GridBagConstraints());

        filtroSexoInstrutor.add(filtroSexoFemininoRadioButton);
        filtroSexoFemininoRadioButton.setText("Feminino");
        filtroSexoFemininoRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filtroSexoFemininoRadioButtonActionPerformed(evt);
            }
        });
        filtroInstrutor_Panel.add(filtroSexoFemininoRadioButton, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = -92;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 42, 0, 0);
        filtroPanel.add(filtroInstrutor_Panel, gridBagConstraints);

        filtroCategorias_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Categorias"));
        filtroCategorias_Panel.setMaximumSize(new java.awt.Dimension(852, 75));
        filtroCategorias_Panel.setMinimumSize(new java.awt.Dimension(852, 75));
        filtroCategorias_Panel.setPreferredSize(new java.awt.Dimension(852, 75));
        filtroCategorias_Panel.setLayout(new java.awt.GridBagLayout());

        filtroCategoriaA_RadioButton.setText("Categoria A");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        filtroCategorias_Panel.add(filtroCategoriaA_RadioButton, gridBagConstraints);

        filtroCategoriaB_RadioButton.setText("Categoria B");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        filtroCategorias_Panel.add(filtroCategoriaB_RadioButton, gridBagConstraints);

        filtroCategoriaE_RadioButton.setText("Categoria E");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        filtroCategorias_Panel.add(filtroCategoriaE_RadioButton, gridBagConstraints);

        filtroCategoriaC_RadioButton.setText("Categoria C");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        filtroCategorias_Panel.add(filtroCategoriaC_RadioButton, gridBagConstraints);

        filtroCategoriaD_RadioButton.setText("Categoria D");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        filtroCategorias_Panel.add(filtroCategoriaD_RadioButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = -134;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 33, 0, 53);
        filtroPanel.add(filtroCategorias_Panel, gridBagConstraints);

        filtroResultadosTextArea.setEditable(false);
        filtroResultadosTextArea.setColumns(20);
        filtroResultadosTextArea.setRows(7);
        jScrollPane4.setViewportView(filtroResultadosTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 627;
        gridBagConstraints.ipady = 109;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 33, 69, 53);
        filtroPanel.add(jScrollPane4, gridBagConstraints);

        filtroFiltrar_Button.setText("Filtrar");
        filtroFiltrar_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filtrarMatrículas(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 41, 0, 0);
        filtroPanel.add(filtroFiltrar_Button, gridBagConstraints);

        filtroLimpar_Button.setText("Limpar");
        filtroLimpar_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limparDadosFiltro(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 18, 0, 0);
        filtroPanel.add(filtroLimpar_Button, gridBagConstraints);

        abas_TabbedPane.addTab("Filtro", filtroPanel);

        getContentPane().add(abas_TabbedPane, new java.awt.GridBagConstraints());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void instrutorHoraFim_FormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instrutorHoraFim_FormattedTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_instrutorHoraFim_FormattedTextFieldActionPerformed

    private void instrutorHoraInicio_FormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instrutorHoraInicio_FormattedTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_instrutorHoraInicio_FormattedTextFieldActionPerformed

    private void instrutorDomingoCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instrutorDomingoCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_instrutorDomingoCheckBoxActionPerformed

    private void instrutorCategoriaA_RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instrutorCategoriaA_RadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_instrutorCategoriaA_RadioButtonActionPerformed

    private void instrutorEstado_ComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instrutorEstado_ComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_instrutorEstado_ComboBoxActionPerformed

    private void limparDadosInstrutor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limparDadosInstrutor
        // TODO add your handling code here:
        instrutorNome_TextField.setText("");
        instrutorCpf_FormattedTextField.setText("");
        instrutorRg_FormattedTextField.setText("");
        instrutorSexo_buttonGroup.clearSelection();
        instrutorDataDeNascimento_FormattedTextField.setText("");
        instrutorDataDeContratação_FormattedTextField.setText("");
        instrutorTelefone_FormatedTextField.setText("");
        instrutorEmail_TextField.setText("");
        instrutorLogradouro_TextField.setText("");
        instrutorComplemento_TextField.setText("");
        instrutorBairro_TextField.setText("");
        instrutorNúmero_TextField.setText("");
        instrutorCep_TextField.setText("");
        instrutorCidade_TextField.setText("");
        instrutorEstado_ComboBox.setSelectedIndex(-1);
        for (Component componente : instrutorCategorias_Panel.getComponents()) {
            if (componente instanceof JRadioButton) {
                JRadioButton categoria = (JRadioButton) componente;
                categoria.setSelected(false);
            }
        }
        for (Component componente : instrutorHorários_Panel.getComponents()) {
            if (componente instanceof JCheckBox) {
                JCheckBox diaSemana = (JCheckBox) componente;
                diaSemana.setSelected(false);
            }
        }
        instrutorHoraInicio_FormattedTextField.setText("");
        instrutorHoraFim_FormattedTextField.setText("");
    }//GEN-LAST:event_limparDadosInstrutor

    private void removerInstrutor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removerInstrutor
        Visão<String> visão
                = (Visão<String>) instrutoresCadastrados_ComboBox.getSelectedItem();
        String mensagem_erro = null;
        if (visão != null) {
            //Remover no banco de dados
            mensagem_erro = controladorCadastroInstrutor.removerInstrutor(visão.getChave());

        } else {
            mensagem_erro = "Nenhum instrutor selecionado";
        }
        if (mensagem_erro == null) {
            instrutores_cadastrados.remove(visão);
            if (instrutores_cadastrados.size() >= 1) {
                instrutoresCadastrados_ComboBox.setSelectedIndex(0);
                matrículaInstrutoresCadastrados_ComboBox.setSelectedIndex(0);
            } else {
                instrutoresCadastrados_ComboBox.setSelectedIndex(-1);
                matrículaInstrutoresCadastrados_ComboBox.setSelectedIndex(-1);
            }
            modelo_lista_matrículasCadastradas.clear();
            inicializarListaMatrículas();
            limparDadosInstrutor(evt);
        } else {
            JOptionPane.showMessageDialog(this, mensagem_erro, "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_removerInstrutor

    private void alterarInstrutor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alterarInstrutor
        Visão<String> visão_selecionada
                = (Visão<String>) instrutoresCadastrados_ComboBox.getSelectedItem();

        Instrutor instrutor = obterInstrutorInformado();
        String mensagem_erro = null;
        if (instrutor != null) {
            if (instrutor.getEndereço().getNúmero() < 0) {
                JOptionPane.showMessageDialog(this, "Número de Endereço inválido", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!instrutor.getDataNascimento().verificarData())
            {
                JOptionPane.showMessageDialog(this, "Data de nascimento inválida!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!instrutor.getDataContratação().verificarData())
            {
                JOptionPane.showMessageDialog(this, "Data de contratação inválida!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!instrutor.getHorário().getHoraDeInício().éVálido())
            {
                JOptionPane.showMessageDialog(this, "Hora de início inválido!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!instrutor.getHorário().getHoraDeTérmino().éVálido())
            {
                JOptionPane.showMessageDialog(this, "Hora de Fim inválido!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //Alterar no banco de dados
            mensagem_erro = controladorCadastroInstrutor.alterarInstrutor(instrutor);

        } else {
            mensagem_erro = "Algum atributo do instrutor não foi informado";
        }
        if (mensagem_erro == null) {
            Visão<String> visão = getVisãoInstrutoresCadastrados(instrutor.getCPF());
            if (visão != null) {
                visão.setInfo(instrutor.getVisão().getInfo());
                instrutoresCadastrados_ComboBox.updateUI();
                matrículaInstrutoresCadastrados_ComboBox.updateUI();
                instrutoresCadastrados_ComboBox.setSelectedItem(visão);
            }
        } else {
            JOptionPane.showMessageDialog(this, mensagem_erro, "ERRO", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_alterarInstrutor

    private void consultarInstrutor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_consultarInstrutor
        // TODO add your handling code here:
        Visão<String> visão
                = (Visão<String>) instrutoresCadastrados_ComboBox.getSelectedItem();

        Instrutor instrutor = null;
        String mensagem_erro = null;
        if (visão != null) {
            instrutor = Instrutor.buscarInstrutor(visão.getChave());

            if (instrutor == null) {
                mensagem_erro = "Instrutor não cadastrado";
            }
        } else {
            mensagem_erro = "Nenhum instrutor selecionado";
        }
        if (mensagem_erro == null) {

            instrutorNome_TextField.setText(instrutor.getNome());
            instrutorCpf_FormattedTextField.setText(instrutor.getCPF());
            instrutorRg_FormattedTextField.setText(instrutor.getRG());
            if (instrutor.getSexo() == 'M') {
                instrutorSexoMasculino_RadioButton.setSelected(true);
            } else {
                instrutorSexoFeminino_RadioButton.setSelected(true);
            }
            instrutorDataDeNascimento_FormattedTextField.setText(instrutor.getDataNascimento().toString());
            instrutorDataDeContratação_FormattedTextField.setText(instrutor.getDataContratação().toString());
            instrutorTelefone_FormatedTextField.setText(instrutor.getTelefone());
            instrutorEmail_TextField.setText(instrutor.getEmail());
            instrutorLogradouro_TextField.setText(instrutor.getEndereço().getLogradouro());
            instrutorComplemento_TextField.setText(instrutor.getEndereço().getComplemento());
            instrutorBairro_TextField.setText(instrutor.getEndereço().getBairro());
            instrutorNúmero_TextField.setText("" + instrutor.getEndereço().getNúmero());
            instrutorCep_TextField.setText(instrutor.getEndereço().getCEP());
            instrutorCidade_TextField.setText(instrutor.getEndereço().getCidade());
            instrutorEstado_ComboBox.setSelectedIndex(instrutor.getEndereço().getUF().ordinal());
            
            if (instrutor.getCategoriasMinistradas().contains(Matrícula.Categoria.CategoriaA)) {
                instrutorCategoriaA_RadioButton.setSelected(true);
            }
            if (instrutor.getCategoriasMinistradas().contains(Matrícula.Categoria.CategoriaB)) {
                instrutorCategoriaB_RadioButton.setSelected(true);
            }
            if (instrutor.getCategoriasMinistradas().contains(Matrícula.Categoria.CategoriaC)) {
                instrutorCategoriaC_RadioButton.setSelected(true);
            }
            if (instrutor.getCategoriasMinistradas().contains(Matrícula.Categoria.CategoriaD)) {
                instrutorCategoriaD_RadioButton.setSelected(true);
            }
            if (instrutor.getCategoriasMinistradas().contains(Matrícula.Categoria.CategoriaE)) {
                instrutorCategoriaE_RadioButton.setSelected(true);
            }

            if (instrutor.getHorário().getDiasLetivos().contains(Horário.DiasDaSemana.segunda)) {
                instrutorSegundaCheckBox.setSelected(true);
            }
            if (instrutor.getHorário().getDiasLetivos().contains(Horário.DiasDaSemana.terça)) {
                instrutorTerçaCheckBox.setSelected(true);
            }
            if (instrutor.getHorário().getDiasLetivos().contains(Horário.DiasDaSemana.quarta)) {
                instrutorQuartaCheckBox.setSelected(true);
            }
            if (instrutor.getHorário().getDiasLetivos().contains(Horário.DiasDaSemana.quinta)) {
                instrutorQuintaCheckBox.setSelected(true);
            }
            if (instrutor.getHorário().getDiasLetivos().contains(Horário.DiasDaSemana.sexta)) {
                instrutorSextaCheckBox.setSelected(true);
            }
            if (instrutor.getHorário().getDiasLetivos().contains(Horário.DiasDaSemana.sábado)) {
                instrutorSábadoCheckBox.setSelected(true);
            }
            if (instrutor.getHorário().getDiasLetivos().contains(Horário.DiasDaSemana.domingo)) {
                instrutorDomingoCheckBox.setSelected(true);
            }

            instrutorHoraInicio_FormattedTextField.setText(instrutor.getHorário().getHoraDeInício().toStringFormatoBD());
            instrutorHoraFim_FormattedTextField.setText(instrutor.getHorário().getHoraDeTérmino().toStringFormatoBD());

        } else {
            JOptionPane.showMessageDialog(this, mensagem_erro, "ERRO", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_consultarInstrutor

    private void inserirInstrutor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inserirInstrutor
        // TODO add your handling code here:
        Instrutor instrutor = obterInstrutorInformado();

        String mensagem_erro = null;

        if (instrutor != null) {
            //Colocar o aluno no banco de dados
            if (instrutor.getEndereço().getNúmero() < 0) {
                JOptionPane.showMessageDialog(this, "Número de Endereço inválido!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!instrutor.getDataNascimento().verificarData())
            {
                JOptionPane.showMessageDialog(this, "Data de nascimento inválida!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!instrutor.getDataContratação().verificarData())
            {
                JOptionPane.showMessageDialog(this, "Data de contratação inválida!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!instrutor.getHorário().getHoraDeInício().éVálido())
            {
                JOptionPane.showMessageDialog(this, "Hora de início inválido!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!instrutor.getHorário().getHoraDeTérmino().éVálido())
            {
                JOptionPane.showMessageDialog(this, "Hora de Fim inválido!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            mensagem_erro = controladorCadastroInstrutor.inserirInstrutor(instrutor);

        } else {
            mensagem_erro = "Algum atributo do Instrutor não foi informado";
        }
        if (mensagem_erro == null) {

            Visão<String> visão = instrutor.getVisão();

            instrutores_cadastrados.add(visão);
            //clientes_cadastradosComboBox.addItem(visão.getInfo());
            instrutoresCadastrados_ComboBox.setSelectedItem(visão);
            matrículaInstrutoresCadastrados_ComboBox.updateUI();

        } else {
            JOptionPane.showMessageDialog(this, mensagem_erro, "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_inserirInstrutor

    private void instrutorDataDeContratação_FormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instrutorDataDeContratação_FormattedTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_instrutorDataDeContratação_FormattedTextFieldActionPerformed

    private void instrutorSexoMasculino_RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instrutorSexoMasculino_RadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_instrutorSexoMasculino_RadioButtonActionPerformed

    private void instrutorRg_FormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instrutorRg_FormattedTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_instrutorRg_FormattedTextFieldActionPerformed

    private void instrutorCpf_FormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instrutorCpf_FormattedTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_instrutorCpf_FormattedTextFieldActionPerformed

    private void instrutorNome_TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instrutorNome_TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_instrutorNome_TextFieldActionPerformed

    private void alunoEstado_ComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alunoEstado_ComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_alunoEstado_ComboBoxActionPerformed

    private void alunoSexoMasculino_RadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alunoSexoMasculino_RadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_alunoSexoMasculino_RadioButtonActionPerformed

    private void alunoRg_FormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alunoRg_FormattedTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_alunoRg_FormattedTextFieldActionPerformed

    private void alunoCpf_FormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alunoCpf_FormattedTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_alunoCpf_FormattedTextFieldActionPerformed

    private void alunoNome_TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alunoNome_TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_alunoNome_TextFieldActionPerformed

    private void limparDadosAluno(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limparDadosAluno
        alunoNome_TextField.setText("");
        alunoCpf_FormattedTextField.setText("");
        alunoRg_FormattedTextField.setText("");
        alunoSexobuttonGroup.clearSelection();
        alunoDataDeNascimento_FormattedTextField.setText("");
        alunoTelefone_FormatedTextField.setText("");
        alunoEmail_TextField.setText("");
        alunoLogradouro_TextField.setText("");
        alunoComplemento_TextField.setText("");
        alunoBairro_TextField.setText("");
        alunoNúmero_TextField.setText("");
        alunoCep_TextField.setText("");
        alunoCidade_TextField.setText("");
        alunoEstado_ComboBox.setSelectedIndex(-1);
    }//GEN-LAST:event_limparDadosAluno

    private void removerAluno(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removerAluno
        Visão<String> visão
                = (Visão<String>) alunoCadastrado_ComboBox.getSelectedItem();
        String mensagem_erro = null;
        if (visão != null) {
            //Remover no banco de dados
            mensagem_erro = controladorCadastroAluno.removerAluno(visão.getChave());

        } else {
            mensagem_erro = "Nenhum aluno selecionado";
        }
        if (mensagem_erro == null) {
            alunos_cadastrados.remove(visão);
            if (alunos_cadastrados.size() >= 1) {
                alunoCadastrado_ComboBox.setSelectedIndex(0);
                matrículaAlunosCadastrados_ComboBox.setSelectedIndex(0);
            } else {
                alunoCadastrado_ComboBox.setSelectedIndex(-1);
                matrículaAlunosCadastrados_ComboBox.setSelectedIndex(-1);
            }
            modelo_lista_matrículasCadastradas.clear();
            inicializarListaMatrículas();
            limparDadosAluno(null);
        } else {
            JOptionPane.showMessageDialog(this, mensagem_erro, "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_removerAluno

    private void alterarAluno(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alterarAluno
        Visão<String> visão_selecionada
                = (Visão<String>) alunoCadastrado_ComboBox.getSelectedItem();

        Aluno aluno = obterAlunoInformado();
        String mensagem_erro = null;

        if (aluno != null) {
            if (aluno.getEndereço().getNúmero() < 0) {
                JOptionPane.showMessageDialog(this, "Número de Endereço inválido", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!aluno.getDataNascimento().verificarData())
            {
                JOptionPane.showMessageDialog(this, "Data de nascimento inválida!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //Alterar no banco de dados
            mensagem_erro = controladorCadastroAluno.alterarAluno(aluno);

        } else {
            mensagem_erro = "Algum atributo do aluno não foi informado";
        }
        if (mensagem_erro == null) {
            Visão<String> visão = getVisãoAlunosCadastrados(aluno.getCPF());
            if (visão != null) {
                visão.setInfo(aluno.getVisão().getInfo());
                alunoCadastrado_ComboBox.updateUI();
                matrículaAlunosCadastrados_ComboBox.updateUI();
                alunoCadastrado_ComboBox.setSelectedItem(visão);
            }
            modelo_lista_matrículasCadastradas.clear();
            inicializarListaMatrículas();
        } else {
            JOptionPane.showMessageDialog(this, mensagem_erro, "ERRO", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_alterarAluno

    private void consultarAluno(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_consultarAluno
        Visão<String> visão
                = (Visão<String>) alunoCadastrado_ComboBox.getSelectedItem();

        Aluno aluno = null;
        String mensagem_erro = null;
        if (visão != null) {
            aluno = Aluno.buscarAluno(visão.getChave());

            if (aluno == null) {
                mensagem_erro = "Aluno não cadastrado";
            }
        } else {
            mensagem_erro = "Nenhum aluno selecionado";
        }
        if (mensagem_erro == null) {
            alunoNome_TextField.setText(aluno.getNome());
            alunoCpf_FormattedTextField.setText(aluno.getCPF());
            alunoRg_FormattedTextField.setText(aluno.getRG());
            if (aluno.getSexo() == 'M') {
                alunoSexoMasculino_RadioButton.setSelected(true);
            } else {
                alunoSexoFeminino_RadioButton.setSelected(true);
            }
            alunoDataDeNascimento_FormattedTextField.setText(aluno.getDataNascimento().toString());
            alunoTelefone_FormatedTextField.setText(aluno.getTelefone());
            alunoEmail_TextField.setText(aluno.getEmail());
            alunoLogradouro_TextField.setText(aluno.getEndereço().getLogradouro());
            alunoComplemento_TextField.setText(aluno.getEndereço().getComplemento());
            alunoBairro_TextField.setText(aluno.getEndereço().getBairro());
            alunoNúmero_TextField.setText("" + aluno.getEndereço().getNúmero());
            alunoCep_TextField.setText(aluno.getEndereço().getCEP());
            alunoCidade_TextField.setText(aluno.getEndereço().getCidade());
            alunoEstado_ComboBox.setSelectedIndex(aluno.getEndereço().getUF().ordinal());

        } else {
            JOptionPane.showMessageDialog(this, mensagem_erro, "ERRO", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_consultarAluno

    private void inserirAluno(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inserirAluno
        Aluno aluno = obterAlunoInformado();

        String mensagem_erro = null;

        if (aluno != null) {
            if (aluno.getEndereço().getNúmero() < 0) {
                JOptionPane.showMessageDialog(this, "Número de Endereço inválido", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!aluno.getDataNascimento().verificarData())
            {
                JOptionPane.showMessageDialog(this, "Data de nascimento inválida!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //Colocar o aluno no banco de dados
            mensagem_erro = controladorCadastroAluno.inserirAluno(aluno);

        } else {
            mensagem_erro = "Algum atributo do aluno não foi informado";
        }
        if (mensagem_erro == null) {

            Visão<String> visão = aluno.getVisão();

            alunos_cadastrados.add(visão);
            //clientes_cadastradosComboBox.addItem(visão.getInfo());
            alunoCadastrado_ComboBox.setSelectedItem(visão);
            matrículaAlunosCadastrados_ComboBox.updateUI();

        } else {
            JOptionPane.showMessageDialog(this, mensagem_erro, "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_inserirAluno

    private void filtroDataMatrículaInício_FormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filtroDataMatrículaInício_FormattedTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroDataMatrículaInício_FormattedTextFieldActionPerformed

    private void filtroDataMatrículaFim_FormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filtroDataMatrículaFim_FormattedTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroDataMatrículaFim_FormattedTextFieldActionPerformed

    private void filtroSexoFemininoRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filtroSexoFemininoRadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroSexoFemininoRadioButtonActionPerformed

    private void matrículaDataMatrícula_FormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_matrículaDataMatrícula_FormattedTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_matrículaDataMatrícula_FormattedTextFieldActionPerformed

    private void alterarMatrícula(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alterarMatrícula
        if (matrículaId_TextField.getText().isEmpty()) {
            consultarMatrícula(evt);
            return;
        }

        int númeroDeMatrícula = Integer.parseInt(matrículaId_TextField.getText());

        Matrícula matrícula = obterMatrículaInformada();

        String mensagem_erro = null;
        if (matrícula != null) {
            int flag = 0;
            for(Matrícula.Categoria categoria : matrícula.getCategoriasMatriculadas())
                if(!matrícula.getInstrutor().getCategoriasMinistradas().contains(categoria))
                {
                    flag = 1;
                    break;
                }
            if(flag == 1)
            {
                JOptionPane.showMessageDialog(this, "O Instrutor selecionado não é apto para lecionar as categoria selecionada!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            matrícula.setNúmeroDeMatrícula(númeroDeMatrícula);
            if(!matrícula.getDataMatrícula().verificarData())
            {
                JOptionPane.showMessageDialog(this, "Data de matrícula inválida!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //Alterar no banco de dados
            mensagem_erro = controladorCadastroMatrícula.alterarMatrícula(matrícula);
        } else {
            mensagem_erro = "Algum atributo da Matrícula não foi informado";
        }
        if (mensagem_erro == null) {
            for (int i = 0; i < modelo_lista_matrículasCadastradas.size(); i++) {
                Visão<Integer> visão = modelo_lista_matrículasCadastradas.get(i);

                if (visão.getChave() == matrícula.getNúmeroDeMatrícula()) {
                    modelo_lista_matrículasCadastradas.set(i, matrícula.getVisão());
                    break;
                }
            }
            modelo_lista_horáriosCadastrados.clear();
            modelo_lista_horáriosCadastrados.addElement(matrícula.getInstrutor().getHorário().getVisão());
            matrículaHoráriosList.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(this, mensagem_erro, "ERRO", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_alterarMatrícula

    private void limparCamposMatrícula(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limparCamposMatrícula
        matrículaHoráriosList.setSelectedIndex(-1);
        matrículaAlunosCadastrados_ComboBox.setSelectedIndex(-1);
        matrículaInstrutoresCadastrados_ComboBox.setSelectedIndex(-1);
        for (Component componente : matrículaCategorias_Panel.getComponents()) {
            if (componente instanceof JRadioButton) {
                JRadioButton botão = (JRadioButton) componente;
                botão.setSelected(false);
            }
        }
        matrículaExamePsicotécnico_buttonGroup.clearSelection();
        matrículaExameMédico_buttonGroup.clearSelection();
        matrículaId_TextField.setText("");
        matrículaDataMatrícula_FormattedTextField.setText("");
        modelo_lista_horáriosCadastrados.clear();
    }//GEN-LAST:event_limparCamposMatrícula

    private void removerMatrícula(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removerMatrícula
        Visão<Integer> visão = matrículaCadastrada_List.getSelectedValue();
        String mensagemErro = null;

        if (visão != null) {

            int index = matrículaCadastrada_List.getSelectedIndex();
            mensagemErro = ControladorCadastroMatrícula.removerMatrícula(visão.getChave());

            if (mensagemErro == null) {
                modelo_lista_matrículasCadastradas.remove(index);
                matrículaCadastrada_List.setSelectedIndex(modelo_lista_horáriosCadastrados.size() - 1);
            } else {
                JOptionPane.showMessageDialog(this, mensagemErro, "ERRO", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            matrículaCadastrada_List.setSelectedIndex(-1);
        }
    }//GEN-LAST:event_removerMatrícula

    private void consultarMatrícula(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_consultarMatrícula
        Visão<Integer> visão = matrículaCadastrada_List.getSelectedValue();

        if (visão != null) {

            Matrícula matrícula = Matrícula.buscarMatrícula(visão.getChave());

            if (matrícula != null) {
                
                matrículaId_TextField.setText("" + matrícula.getNúmeroDeMatrícula());
                int indice = buscaIndiceVisão(alunos_cadastrados, matrícula.getAluno().getCPF());
                matrículaAlunosCadastrados_ComboBox.setSelectedIndex(indice);
                indice = buscaIndiceVisão(instrutores_cadastrados, matrícula.getInstrutor().getCPF());
                matrículaInstrutoresCadastrados_ComboBox.setSelectedIndex(indice);

                if (matrícula.getCategoriasMatriculadas().contains(Matrícula.Categoria.CategoriaA)) {
                    matrículaCategoriaA_RadioButton.setSelected(true);
                }
                if (matrícula.getCategoriasMatriculadas().contains(Matrícula.Categoria.CategoriaB)) {
                    matrículaCategoriaB_RadioButton.setSelected(true);
                }
                if (matrícula.getCategoriasMatriculadas().contains(Matrícula.Categoria.CategoriaC)) {
                    matrículaCategoriaC_RadioButton.setSelected(true);
                }
                if (matrícula.getCategoriasMatriculadas().contains(Matrícula.Categoria.CategoriaD)) {
                    matrículaCategoriaD_RadioButton.setSelected(true);
                }
                if (matrícula.getCategoriasMatriculadas().contains(Matrícula.Categoria.CategoriaE)) {
                    matrículaCategoriaE_RadioButton.setSelected(true);
                }

                if (matrícula.getAptoExamePsicotécnico()) {
                    matrículaAptoPsicotécnico_RadioButton.setSelected(true);
                } else {
                    matrículaInaptoPisicotécnico_RadioButton.setSelected(true);
                }
                if (matrícula.getAptoExameMédico()) {
                    matrículaAptoMédico_RadioButton.setSelected(true);
                } else {
                    matrículaInaptoMédico_RadioButton.setSelected(true);
                }
                matrículaDataMatrícula_FormattedTextField.setText(matrícula.getDataMatrícula().toString());
                modelo_lista_horáriosCadastrados.clear();
                modelo_lista_horáriosCadastrados.addElement(matrícula.getInstrutor().getHorário().getVisão());
                matrículaHoráriosList.setSelectedIndex(0);
            }
        }
    }//GEN-LAST:event_consultarMatrícula

    private void inserirMatrícula(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inserirMatrícula
        Matrícula matrícula = obterMatrículaInformada();

        String mensagem_erro = null;
        if (matrícula != null) {
            int flag = 0;
            for(Matrícula.Categoria categoria : matrícula.getCategoriasMatriculadas())
            {
                if(!matrícula.getInstrutor().getCategoriasMinistradas().contains(categoria))
                {
                    flag = 1;
                    break;
                }
            }
            if(flag == 1)
            {
                JOptionPane.showMessageDialog(this, "O Instrutor selecionado não é apto para lecionar as categoria selecionada!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!matrícula.getDataMatrícula().verificarData())
            {
                JOptionPane.showMessageDialog(this, "Data de matrícula inválida!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            mensagem_erro = controladorCadastroMatrícula.inserirMatrícula(matrícula);
        } else {
            mensagem_erro = "Algum atributo da Matrícula não foi informada";
        }
        if (mensagem_erro == null) {
            matrícula.setNúmeroDeMatrícula(Matrícula.últimoSequencial());
            Visão<Integer> visão = matrícula.getVisão();
            modelo_lista_matrículasCadastradas.addElement(visão);
            matrículaCadastrada_List.setSelectedValue(visão, true);
            matrículaId_TextField.setText("" + matrícula.getNúmeroDeMatrícula());
        } else {
            JOptionPane.showMessageDialog(this, mensagem_erro, "ERRO", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_inserirMatrícula

    private void atualizarHorários(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_atualizarHorários
        // TODO add your handling code here:
        Visão<String> visão = (Visão<String>) matrículaInstrutoresCadastrados_ComboBox.getSelectedItem();
        if (visão == null) {
            return;
        }
        Instrutor instrutor = Instrutor.buscarInstrutor(visão.getChave());
        modelo_lista_horáriosCadastrados.clear();
        modelo_lista_horáriosCadastrados.addElement(instrutor.getHorário().getVisão());
    }//GEN-LAST:event_atualizarHorários

    private void matrículaAlunosCadastrados_ComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_matrículaAlunosCadastrados_ComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_matrículaAlunosCadastrados_ComboBoxActionPerformed

    private void matrículaId_TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_matrículaId_TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_matrículaId_TextFieldActionPerformed

    private void filtrarMatrículas(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filtrarMatrículas
        Data dataInício = null;
        if(filtroDataMatrículaInício_FormattedTextField.getText().compareTo("  /  /    ")==0)
            dataInício = null;
        else if(Data.verificaData(filtroDataMatrículaInício_FormattedTextField.getText().trim()))
            dataInício = new Data(filtroDataMatrículaInício_FormattedTextField.getText().trim());
        
        Data dataFim = null;
        if(filtroDataMatrículaFim_FormattedTextField.getText().compareTo("  /  /    ")==0)
            dataFim = null;
        else if(Data.verificaData(filtroDataMatrículaFim_FormattedTextField.getText().trim()))
            dataFim = new Data(filtroDataMatrículaFim_FormattedTextField.getText().trim());
        
        String cidadeAluno = filtroCidadeAlunoTextField.getText().trim();
        
        char sexoInstrutor;
        if(filtroSexoMasculinoRadioButton.isSelected())
            sexoInstrutor = 'M';
        else if(filtroSexoFemininoRadioButton.isSelected())
            sexoInstrutor = 'F';
        else
            sexoInstrutor = 'X';
        
        ArrayList<Matrícula.Categoria> categoriasMatriculadas = new ArrayList();
        if(filtroCategoriaA_RadioButton.isSelected())
            categoriasMatriculadas.add(Matrícula.Categoria.CategoriaA);
        if(filtroCategoriaB_RadioButton.isSelected())
            categoriasMatriculadas.add(Matrícula.Categoria.CategoriaB);
        if(filtroCategoriaC_RadioButton.isSelected())
            categoriasMatriculadas.add(Matrícula.Categoria.CategoriaC);
        if(filtroCategoriaD_RadioButton.isSelected())
            categoriasMatriculadas.add(Matrícula.Categoria.CategoriaD);
        if(filtroCategoriaE_RadioButton.isSelected())
            categoriasMatriculadas.add(Matrícula.Categoria.CategoriaE);
        
        ArrayList<Horário.DiasDaSemana> diasLetivos = new ArrayList();
        if(filtroSegundaCheckBox.isSelected())
            diasLetivos.add(Horário.DiasDaSemana.segunda);
        if(filtroTerçaCheckBox.isSelected())
            diasLetivos.add(Horário.DiasDaSemana.terça);
        if(filtroQuartaCheckBox.isSelected())
            diasLetivos.add(Horário.DiasDaSemana.quarta);
        if(filtroQuintaCheckBox.isSelected())
            diasLetivos.add(Horário.DiasDaSemana.quinta);
        if(filtroSextaCheckBox.isSelected())
            diasLetivos.add(Horário.DiasDaSemana.sexta);
        if(filtroSábadoCheckBox.isSelected())
            diasLetivos.add(Horário.DiasDaSemana.sábado);
        if(filtroDomingoCheckBox.isSelected())
            diasLetivos.add(Horário.DiasDaSemana.domingo);
        Vector<Matrícula> matrículasSelecionadas = Matrícula.pesquisarMatrículas(diasLetivos, dataInício, dataFim, cidadeAluno, sexoInstrutor, categoriasMatriculadas);
        apendarPesquisaAreaTexto(matrículasSelecionadas, diasLetivos, dataInício, dataFim, cidadeAluno, sexoInstrutor, categoriasMatriculadas);
    }//GEN-LAST:event_filtrarMatrículas

    private void limparDadosFiltro(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limparDadosFiltro
        
        filtroDataMatrículaInício_FormattedTextField.setText("");
        filtroDataMatrículaFim_FormattedTextField.setText("");
        filtroCidadeAlunoTextField.setText("");
        for(Component componente : filtroCategorias_Panel.getComponents())
        {
            if(componente instanceof JRadioButton)
                ((JRadioButton) componente).setSelected(false);
        }
        for(Component componente : filtroHorário_Panel.getComponents())
        {
            if(componente instanceof JCheckBox)
                ((JCheckBox) componente).setSelected(false);
        }
        filtroSexoInstrutor.clearSelection();
        filtroResultadosTextArea.setText("");
    }//GEN-LAST:event_limparDadosFiltro

    private void filtroDomingoCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filtroDomingoCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filtroDomingoCheckBoxActionPerformed

    private void alunoTelefone_FormatedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alunoTelefone_FormatedTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_alunoTelefone_FormatedTextFieldActionPerformed

    private void fecharJanela(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_fecharJanela

        BD.fechaConexão();
    }//GEN-LAST:event_fecharJanela

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JanelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JanelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JanelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JanelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JanelaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane abas_TabbedPane;
    private javax.swing.JButton alunoAlterar_Button;
    private javax.swing.JLabel alunoBairro_Label;
    private javax.swing.JTextField alunoBairro_TextField;
    private javax.swing.JPanel alunoBotões_Panel;
    private javax.swing.JComboBox<String> alunoCadastrado_ComboBox;
    private javax.swing.JLabel alunoCep_Label;
    private javax.swing.JTextField alunoCep_TextField;
    private javax.swing.JLabel alunoCidade_Label;
    private javax.swing.JTextField alunoCidade_TextField;
    private javax.swing.JLabel alunoComplemento_Label;
    private javax.swing.JTextField alunoComplemento_TextField;
    private javax.swing.JButton alunoConsultar_Button;
    private javax.swing.JPanel alunoContato_Panel;
    private javax.swing.JFormattedTextField alunoCpf_FormattedTextField;
    private javax.swing.JLabel alunoCpf_Label;
    private javax.swing.JPanel alunoDadosPessoais_Panel;
    private javax.swing.JFormattedTextField alunoDataDeNascimento_FormattedTextField;
    private javax.swing.JLabel alunoDataDeNascimento_Label;
    private javax.swing.JLabel alunoEmail_Label;
    private javax.swing.JTextField alunoEmail_TextField;
    private javax.swing.JPanel alunoEndereço_Panel;
    private javax.swing.JComboBox alunoEstado_ComboBox;
    private javax.swing.JButton alunoInserir_Button;
    private javax.swing.JButton alunoLimpar_Button;
    private javax.swing.JLabel alunoLogradouro_Label;
    private javax.swing.JTextField alunoLogradouro_TextField;
    private javax.swing.JLabel alunoNome_Label;
    private javax.swing.JTextField alunoNome_TextField;
    private javax.swing.JLabel alunoNúmero_Label;
    private javax.swing.JTextField alunoNúmero_TextField;
    private javax.swing.JPanel alunoPanel;
    private javax.swing.JButton alunoRemover_Button;
    private javax.swing.JFormattedTextField alunoRg_FormattedTextField;
    private javax.swing.JLabel alunoRg_Label;
    private javax.swing.JRadioButton alunoSexoFeminino_RadioButton;
    private javax.swing.JRadioButton alunoSexoMasculino_RadioButton;
    private javax.swing.JLabel alunoSexo_Label;
    private javax.swing.ButtonGroup alunoSexobuttonGroup;
    private javax.swing.JFormattedTextField alunoTelefone_FormatedTextField;
    private javax.swing.JLabel alunoTelefone_Label;
    private javax.swing.JLabel alunoUf_Label;
    private javax.swing.JPanel filtroAluno_Panel;
    private javax.swing.JRadioButton filtroCategoriaA_RadioButton;
    private javax.swing.JRadioButton filtroCategoriaB_RadioButton;
    private javax.swing.JRadioButton filtroCategoriaC_RadioButton;
    private javax.swing.JRadioButton filtroCategoriaD_RadioButton;
    private javax.swing.JRadioButton filtroCategoriaE_RadioButton;
    private javax.swing.JPanel filtroCategorias_Panel;
    private javax.swing.JLabel filtroCidadeAlunoLabel;
    private javax.swing.JLabel filtroCidadeAlunoLabel1;
    private javax.swing.JTextField filtroCidadeAlunoTextField;
    private javax.swing.JFormattedTextField filtroDataMatrículaFim_FormattedTextField;
    private javax.swing.JFormattedTextField filtroDataMatrículaInício_FormattedTextField;
    private javax.swing.JCheckBox filtroDomingoCheckBox;
    private javax.swing.JButton filtroFiltrar_Button;
    private javax.swing.JPanel filtroHorário_Panel;
    private javax.swing.JPanel filtroInstrutor_Panel;
    private javax.swing.JButton filtroLimpar_Button;
    private javax.swing.JPanel filtroPanel;
    private javax.swing.JPanel filtroPerídoMatrícula_Panel;
    private javax.swing.JCheckBox filtroQuartaCheckBox;
    private javax.swing.JCheckBox filtroQuintaCheckBox;
    private javax.swing.JTextArea filtroResultadosTextArea;
    private javax.swing.JCheckBox filtroSegundaCheckBox;
    private javax.swing.JRadioButton filtroSexoFemininoRadioButton;
    private javax.swing.ButtonGroup filtroSexoInstrutor;
    private javax.swing.JRadioButton filtroSexoMasculinoRadioButton;
    private javax.swing.JCheckBox filtroSextaCheckBox;
    private javax.swing.JCheckBox filtroSábadoCheckBox;
    private javax.swing.JCheckBox filtroTerçaCheckBox;
    private javax.swing.JPanel horárioInstrutores_Panel1;
    private javax.swing.JPanel inicioPanel;
    private javax.swing.JTextField instrutorBairro_TextField;
    private javax.swing.JRadioButton instrutorCategoriaA_RadioButton;
    private javax.swing.JRadioButton instrutorCategoriaB_RadioButton;
    private javax.swing.JRadioButton instrutorCategoriaC_RadioButton;
    private javax.swing.JRadioButton instrutorCategoriaD_RadioButton;
    private javax.swing.JRadioButton instrutorCategoriaE_RadioButton;
    private javax.swing.JPanel instrutorCategorias_Panel;
    private javax.swing.JTextField instrutorCep_TextField;
    private javax.swing.JTextField instrutorCidade_TextField;
    private javax.swing.JTextField instrutorComplemento_TextField;
    private javax.swing.JFormattedTextField instrutorCpf_FormattedTextField;
    private javax.swing.JFormattedTextField instrutorDataDeContratação_FormattedTextField;
    private javax.swing.JFormattedTextField instrutorDataDeNascimento_FormattedTextField;
    private javax.swing.JCheckBox instrutorDomingoCheckBox;
    private javax.swing.JTextField instrutorEmail_TextField;
    private javax.swing.JComboBox instrutorEstado_ComboBox;
    private javax.swing.JFormattedTextField instrutorHoraFim_FormattedTextField;
    private javax.swing.JFormattedTextField instrutorHoraInicio_FormattedTextField;
    private javax.swing.JPanel instrutorHorários_Panel;
    private javax.swing.JTextField instrutorLogradouro_TextField;
    private javax.swing.JTextField instrutorNome_TextField;
    private javax.swing.JTextField instrutorNúmero_TextField;
    private javax.swing.JPanel instrutorPanel;
    private javax.swing.JCheckBox instrutorQuartaCheckBox;
    private javax.swing.JCheckBox instrutorQuintaCheckBox;
    private javax.swing.JFormattedTextField instrutorRg_FormattedTextField;
    private javax.swing.JCheckBox instrutorSegundaCheckBox;
    private javax.swing.JRadioButton instrutorSexoFeminino_RadioButton;
    private javax.swing.JRadioButton instrutorSexoMasculino_RadioButton;
    private javax.swing.ButtonGroup instrutorSexo_buttonGroup;
    private javax.swing.JCheckBox instrutorSextaCheckBox;
    private javax.swing.JCheckBox instrutorSábadoCheckBox;
    private javax.swing.JFormattedTextField instrutorTelefone_FormatedTextField;
    private javax.swing.JCheckBox instrutorTerçaCheckBox;
    private javax.swing.JButton instrutoresAlterar_Button;
    private javax.swing.JLabel instrutoresBairro_Label;
    private javax.swing.JPanel instrutoresBotões_Panel;
    private javax.swing.JComboBox<Visão<String>> instrutoresCadastrados_ComboBox;
    private javax.swing.JLabel instrutoresCep_Label;
    private javax.swing.JLabel instrutoresCidade_Label;
    private javax.swing.JLabel instrutoresComplemento_Label;
    private javax.swing.JButton instrutoresConsultar_Button;
    private javax.swing.JPanel instrutoresContato_Panel;
    private javax.swing.JLabel instrutoresCpf_Label;
    private javax.swing.JPanel instrutoresDadosPessoais_Panel;
    private javax.swing.JLabel instrutoresDataDeContratação_Label;
    private javax.swing.JLabel instrutoresDataDeNascimento_Label;
    private javax.swing.JLabel instrutoresEmail_Label;
    private javax.swing.JPanel instrutoresEndereço_Panel;
    private javax.swing.JButton instrutoresInserir_Button;
    private javax.swing.JButton instrutoresLimpar_Button;
    private javax.swing.JLabel instrutoresLogradouro_Label;
    private javax.swing.JLabel instrutoresNome_Label;
    private javax.swing.JLabel instrutoresNúmero_Label;
    private javax.swing.JButton instrutoresRemover_Button;
    private javax.swing.JLabel instrutoresRg_Label;
    private javax.swing.JLabel instrutoresSexo_Label;
    private javax.swing.JLabel instrutoresTelefone_Label;
    private javax.swing.JLabel instrutoresUf_Label;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel logoAutoEscolaLabel;
    private javax.swing.JButton matrículaAlterarButton;
    private javax.swing.JComboBox<String> matrículaAlunosCadastrados_ComboBox;
    private javax.swing.JPanel matrículaAlunosCadastrados_Panel;
    private javax.swing.JRadioButton matrículaAptoMédico_RadioButton;
    private javax.swing.JRadioButton matrículaAptoPsicotécnico_RadioButton;
    private javax.swing.JPanel matrículaBotões_Panel;
    private javax.swing.JList<Visão<Integer>> matrículaCadastrada_List;
    private javax.swing.JPanel matrículaCadastrada_Panel;
    private javax.swing.JRadioButton matrículaCategoriaA_RadioButton;
    private javax.swing.JRadioButton matrículaCategoriaB_RadioButton;
    private javax.swing.JRadioButton matrículaCategoriaC_RadioButton;
    private javax.swing.JRadioButton matrículaCategoriaD_RadioButton;
    private javax.swing.JRadioButton matrículaCategoriaE_RadioButton;
    private javax.swing.JPanel matrículaCategorias_Panel;
    private javax.swing.JButton matrículaConsultar_Button;
    private javax.swing.JFormattedTextField matrículaDataMatrícula_FormattedTextField;
    private javax.swing.JPanel matrículaDataMatrícula_Panel;
    private javax.swing.JLabel matrículaExameMédico_Label;
    private javax.swing.ButtonGroup matrículaExameMédico_buttonGroup;
    private javax.swing.JLabel matrículaExamePsicotécnico_Label;
    private javax.swing.ButtonGroup matrículaExamePsicotécnico_buttonGroup;
    private javax.swing.JPanel matrículaExames_Panel;
    private javax.swing.JList<Visão<Integer>> matrículaHoráriosList;
    private javax.swing.JPanel matrículaId_Panel;
    private javax.swing.JTextField matrículaId_TextField;
    private javax.swing.JRadioButton matrículaInaptoMédico_RadioButton;
    private javax.swing.JRadioButton matrículaInaptoPisicotécnico_RadioButton;
    private javax.swing.JButton matrículaInserir_Button;
    private javax.swing.JComboBox<Visão<String>> matrículaInstrutoresCadastrados_ComboBox;
    private javax.swing.JPanel matrículaInstrutoresCadastrados_Panel;
    private javax.swing.JButton matrículaLimpar_Button;
    private javax.swing.JPanel matrículaPanel;
    private javax.swing.JButton matrículaRemover_Button;
    private javax.swing.JLabel matrículaResultadoExame_Label;
    // End of variables declaration//GEN-END:variables
}
