package forca.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Forca extends JFrame{
	String Lista[] = {" " ,"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};
    private JToolBar Barra_Ferramenta = new JToolBar();
    private JLabel MSN = new JLabel(" Escolha uma Letra: ");
    private JButton Novo_Jogo = new JButton("Novo Jogo");
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private JComboBox Opcao = new JComboBox(Lista);
    private JLabel Ultima_Letra_Escolhida = new JLabel(" Utima Letra Escolhida: ");
    private JTextField Text_Letra = new JTextField();
    private JButton Palavra = new JButton("Ja sei a palavra certa!");

    private GridLayout Layout = new GridLayout(2,1);
    private JPanel Geral = new JPanel();
    private JPanel Panel_de_Controle = new JPanel();
    private JLabel Letra[] = new JLabel[12];
    private Enforcado Panel_da_Animacao = new Enforcado();
    private JLabel Barra_de_Status = new JLabel("  Numero de Letra(s) errada(s): 0");

    public Forca(){
        super("Jogo da Forca");
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,520);
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        // Adicionando os componentes a Barra de Ferramenta
        Barra_Ferramenta.add(Novo_Jogo);
        Barra_Ferramenta.add(MSN);
        Barra_Ferramenta.add(Opcao);
        Barra_Ferramenta.add(Ultima_Letra_Escolhida);
        Barra_Ferramenta.add(Text_Letra);
        Barra_Ferramenta.add(Palavra);
        add(Barra_Ferramenta, BorderLayout.NORTH);
        // Configuracoes Iniciais
        for (int i=0; i<12; ++i){
            Letra[i] = new JLabel("__");
            Panel_de_Controle.add(Letra[i]);
            Letra[i].setFont(new Font("Serif" , Font.BOLD, 48));  
            Letra[i].setVisible(false);
        }


        Geral.setLayout(Layout);
        Geral.add(Panel_de_Controle);
        Geral.add(Panel_da_Animacao);
        add(Geral);

        Barra_de_Status.setFont(new Font("Serif", Font.BOLD, 14));
        Barra_de_Status.setHorizontalAlignment(SwingConstants.LEFT);
        add(Barra_de_Status, BorderLayout.SOUTH);


            Opcao.setEnabled(false);
            Text_Letra.setEnabled(false);
            Palavra.setEnabled(false);


        Eventos_JogoDaForca Eventos = new Eventos_JogoDaForca();
        Novo_Jogo.addActionListener(Eventos);
        Palavra.addActionListener(Eventos);
        Opcao.addItemListener(Eventos);
}
    
    private class Eventos_JogoDaForca implements ActionListener, ItemListener{

        private String Palavra_Misteriosa, Palavra_Certa;
        int Tamanho_da_Palavra_Misteriosa, Letras_Encontradas, Numeros_de_Erros, Campos_Nao_Preenchidos;
        int Exibir_Messagem=0;

        public void actionPerformed (ActionEvent event){

            // Inicializando um novo Jogo
            if (event.getSource() == Novo_Jogo){

                Exibir_Messagem=0;
                Numeros_de_Erros = 0;

                Habilitando_Desabilitando_Componentes(false);

                for (int i=0; i<Tamanho_da_Palavra_Misteriosa; ++i)
                    Letra[i].setVisible(false);

                Palavra_Misteriosa = JOptionPane.showInputDialog(Forca.this, "Qual nome deve ser advinhado?");

                if(Palavra_Misteriosa != null){

                    if ((Palavra_Misteriosa.length() > 12) || (Palavra_Misteriosa.length() <= 0)) 
                        JOptionPane.showMessageDialog(
                                Forca.this, "A palavra escolhida devera ter no mimimo 1 e no maximo 12 caracteres!", 
                            "Menssagem", JOptionPane.PLAIN_MESSAGE);
                    else{

                        Habilitando_Desabilitando_Componentes(true);
                        Tamanho_da_Palavra_Misteriosa = Palavra_Misteriosa.length();

                        for (int i=0; i<Tamanho_da_Palavra_Misteriosa; ++i){
                            Letra[i].setVisible(true);
                            Letra[i].setText("__");
                        }
                    }
                }
            }
            // Caso ja se saiba a palavra a ser adivinhada
            if (event.getSource() == Palavra){

                Palavra_Certa = JOptionPane.showInputDialog(Forca.this, "Qual eh a palavra?");

                if (Palavra_Certa != null){
                    Exibir_Messagem=1;
                    // Caso a palavra digitada estiver correta...
                    if ((Palavra_Misteriosa.toUpperCase()).equals(Palavra_Certa.toUpperCase())){

                        // ... sera atualizado o campo de exibicao (palavra certa ira ser mostrada na tela)
                        for (int i=0; i<Tamanho_da_Palavra_Misteriosa; ++i)
                            Letra[i].setText("" + Palavra_Misteriosa.toUpperCase().charAt(i));

                        Vencir();

                    } else Perdir();

                    Habilitando_Desabilitando_Componentes(false);
                }
            }
        }

        public void itemStateChanged(ItemEvent event){
            if(event.getStateChange() == ItemEvent.SELECTED){

                // Exibindo a ultima letra selecionada
                Text_Letra.setText("" + Opcao.getSelectedItem());


                Letras_Encontradas = 0;
                // Verificando se a letra selecionada eh existente na palavra misteriosa
                for (int i=0; i<Tamanho_da_Palavra_Misteriosa; ++i){
                    if (Text_Letra.getText().charAt(0) == Palavra_Misteriosa.toUpperCase().charAt(i)){
                       Letra[i].setText("" + Text_Letra.getText().charAt(0));
                       Letras_Encontradas++;
                    }
                } 


                Campos_Nao_Preenchidos = 0;
                for (int i=0; i<Tamanho_da_Palavra_Misteriosa; ++i)
                    if (Letra[i].getText() != "__") Campos_Nao_Preenchidos++;

                if ((Campos_Nao_Preenchidos == Tamanho_da_Palavra_Misteriosa) && (Exibir_Messagem==0)){

                        Habilitando_Desabilitando_Componentes(false);
                        Exibir_Messagem = 1;
                        Vencir();

                } else{

                    /* Caso a letra selecionada pelo Jogador nao faca parte da 
                    palavra misteriosa sera contabilizado erro*/
                    if ((Letras_Encontradas == 0) && (Exibir_Messagem==0)){

                        Numeros_de_Erros++; // Incrementando variavel
                        // Desenhando animacao
                        Panel_da_Animacao.setErro(Numeros_de_Erros);
                        // Atualizando o numero de erros
                        Barra_de_Status.setText(" Numero de Letra(s) errada(s): " + Numeros_de_Erros);

                        if (Numeros_de_Erros == 7){
                            Habilitando_Desabilitando_Componentes(false);
                            Perdir();
                        }
                    }
                }
            }
        }


        public void Habilitando_Desabilitando_Componentes(boolean parametro){
            /* Quando parametro for verdadeiro os componetes abaixo 
            estaram ativados, ao contrario desativados*/
            Opcao.setEnabled(parametro);
            Text_Letra.setEnabled(parametro);
            Palavra.setEnabled(parametro);
        }

        public void Vencir(){      
            JOptionPane.showMessageDialog(Forca.this, "Parabens!!!");  
            Atualizar();
        }


        public void Perdir(){
            JOptionPane.showMessageDialog(Forca.this, "Voce falhou! \n Tente Novamete!");
            Atualizar();
        }

        public void Atualizar(){
            Panel_da_Animacao.setErro(7);
            Text_Letra.setText("");
            Opcao.setSelectedIndex(0);
            Barra_de_Status.setText(" Numero de Letra(s) errada(s): 0");
        }
 }

//desenhando o boneco
  class Enforcado extends JPanel{ 

    private int Erros;

        void setErro(int Numeros_de_Erros) {
            Erros = Numeros_de_Erros;
        }  

        public void paintComponent(Graphics g){
            super.paintComponents(g);

            if (Erros == 1){
                g.fillOval(5,5,205,205);
                repaint();
            }

            if (Erros == 2){
                g.fillOval(5,5,205,205);
                g.setColor(Color.WHITE);
                g.fillOval(55,65,30,30);  
                repaint();
            }

            if (Erros == 3){
                g.setColor(Color.BLACK);
                g.fillOval(5,5,205,205);
                g.setColor(Color.WHITE);
                g.fillOval(55,65,30,30);
                g.fillOval(135,65,30,30);
                repaint();
            }

            if (Erros == 4){
                g.setColor(Color.BLACK);
                g.fillOval(5,5,205,205);
                g.setColor(Color.WHITE);
                g.fillOval(55,65,30,30);
                g.fillOval(135,65,30,30);
                g.fillOval(50,110,120,60); 
                repaint();
            }

            if (Erros == 5){
                g.setColor(Color.BLACK);
                g.fillOval(5,5,205,205);
                g.setColor(Color.WHITE);
                g.fillRect(45,65,30,8);
                g.fillOval(55,65,30,30);
                g.fillOval(135,65,30,30);
                g.fillOval(50,110,120,60);  
                repaint();
            }

            if (Erros == 6){
                g.setColor(Color.BLACK);
                // Desenhando a cabeca
                g.fillOval(5,5,205,205);
                g.setColor(Color.WHITE);
                // Desenhando a primeira sobrancelha
                g.fillRect(45,65,30,8);
                // Desenhando o primeiro olho
                g.fillOval(55,65,30,30);
                // Desenhando a segunda sobrancelha
                g.fillRect(125,65,30,8);
                // Desenhando o segundo olho
                g.fillOval(135,65,30,30);
                // Desenhando a boca
                g.fillOval(50,110,120,60); 
                repaint();
            }

            // Limpa o Panel
            if (Erros == 7){
                g.clearRect(0,0,250,250);
                repaint();
            }
        }
    } 
}
