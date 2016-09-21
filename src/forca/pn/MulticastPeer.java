package forca.pn;


import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;

import forca.Processo;

public class MulticastPeer extends Thread {

    private InetAddress group;//ip para o grupo multicast
    private MulticastSocket socket; // O mesmo socket para send e receive ...   
    private PeerReceive p;
    private Processo sessao;

    public MulticastPeer(Processo s) {

        this.socket = null;
        this.sessao = s;
        Properties props = System.getProperties();
        props.setProperty("java.net.preferIPv4Stack","true");
        System.setProperties(props);
        this.start();
    }

    public void run() {
        try {
            this.group = InetAddress.getByName("225.1.1.1");
            this.socket = new MulticastSocket(6789);
            this.socket.joinGroup(group);
            DatagramPacket messageOut;
            socket.setTimeToLive(5);
            this.p = new PeerReceive(this.socket, this.sessao); // Iniciando thread do receive (socket como parâmetro) ...

            Scanner scan = new Scanner(System.in);
            String message;
            message = "Multicast iniciado na porta 6789";
            messageOut = new DatagramPacket(message.getBytes(), message.length(), this.group, 6789);
            this.socket.send(messageOut);
            
            while (!message.equals("sair")) { // Enquanto não digitar 'sair' fica no loop ...

                message = scan.nextLine();
                messageOut = new DatagramPacket(message.getBytes(), message.length(), this.group, 6789);

                socket.send(messageOut);
            }

            this.socket.leaveGroup(this.group);

        } catch (SocketException e) {

            System.out.println("Socket: " + e.getMessage());

        } catch (IOException e) {

            System.out.println("IO: " + e.getMessage());

        } finally {

            if (socket != null) {
                socket.close();
            }
        }
    }

    /*Método que enviará mensagens pela rede p2p*/
    public void enviaMensagem(byte[] message) {

        try {
            DatagramPacket messageOut;

            messageOut = new DatagramPacket(message, message.length, this.group, 6789);

            this.socket.send(messageOut);//com o socket já instanciado, envamos o datagrampacket    

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}

//Classe que cria uma thread para receber as mensagens multicast
class PeerReceive extends Thread {

    private final MulticastSocket s;
    private Processo sessao;

    public PeerReceive(MulticastSocket s, Processo sessao) {

        this.sessao = sessao;
        this.s = s;
        this.start(); // Inicia thread ...
    }

    public void run() {

        try {
            while (true) { // Loop para ficar recebendo mensagens ...

                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                
                s.receive(messageIn);
                byte[] message = messageIn.getData();
                
                //System.out.println(""+ new String(message));
                
                /*tratamento de mensagens*/
                if (message[0] == '0') {
                    mensagem_novoUser(messageIn.getData());
                }
                else if (message[0] == '~'){
                    sessao.limpaLista_usuarios();
                    mensagem_novoBD("" + message);
                }
            }
        } catch (IOException ex) {
            System.out.println("Erro: "+ex.toString());
        }
    }
    
    private void mensagem_novoUser(byte mensagem[] ) {
        
        byte[] apelido = null;
        byte[] chave_publica = null;
        //System.out.println(mensagem.toString());
        
        System.out.print("Mensagem: ");
        for(int i = 0; i < 10; i++)
            System.out.print((char)mensagem[i]);        
        System.out.println("");
        
        
        for(int i=1; i<mensagem.length ; i++){
           apelido[i-1] = mensagem[i];
            if( '|' == (char)mensagem[i] ){
                System.arraycopy(s, i, s, i, i);
                break;
            }            
        }
        
        //System.out.println("apelido:" + apelido );
     
        
        //implementar dentro desse metodo a atribuição nas instancias do objeto Processo
        //sessao.adicionaUsuario(new String(apelido), chave_pub);

        
        System.out.println("");
        /*if (porta != sessao.getPortaUDP()) {
            sessao.adicionaUsuario(nome, moedas, preco, porta, 5);
            new UDPClient(sessao.lista_usuariosToString(), "localhost", porta);
        }*/
    }
    
    private void mensagem_novoBD(String m) {

        String nome = null;
        float moedas = 0, preco = 0;
        int porta = 0, versao_bd;

        StringTokenizer st = new StringTokenizer(m, "~");        
        String[] lista_dados;        
        String s = new String();
        
        while (st.hasMoreElements()) {
            s = st.nextToken();            
            lista_dados = s.split("\\|");
            
            nome = lista_dados[0];
            moedas = Float.parseFloat(lista_dados[1]);
            preco = Float.parseFloat(lista_dados[2]);
            porta = Integer.parseUnsignedInt(lista_dados[3].trim());           
            versao_bd = Integer.parseUnsignedInt(lista_dados[4].trim());
            
            //sessao.adicionaUsuario(nome, moedas, preco, porta, versao_bd);
        }
    }
}