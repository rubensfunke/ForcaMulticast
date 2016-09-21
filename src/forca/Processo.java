package forca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Queue;

import forca.pn.EncriptaDecriptaRSA;
import forca.pn.MulticastPeer;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class Processo implements Serializable{
	
    private int processoID; //Identificação do processo
    private String apelido; //Apelido do processo
    private MulticastPeer conexaoMulti; //Objeto da conexao multicast
    private byte[] chave_publica;//vetor de bytes para armazenar a chave publica em bytes de um usu�rio.
    public EncriptaDecriptaRSA cripto;//objeto para manipular a cripotgrafia        
    private Queue<Processo> lista_usuarios;//lista de todos os processos do sistema
    private Queue<Processo> lista_jogadores;//lista de todos os jogadores do sistema
    private int erros; //Conta a quantidade de erros de um processo
    private int pontuacao; //Guarda a pontuação do processo
    
    public Processo(){
        this.apelido = new String();
        this.processoID = 0;
        this.pontuacao = 0;
        this.erros = 0;
        
        lista_usuarios = new LinkedList<Processo>();
        lista_jogadores = new LinkedList<Processo>();
    }
    
    public int getProcessoID() {
		return processoID;
	}
	public void setProcessoID(int processoID) {
		this.processoID = processoID;
	}
	public String getApelido() {
		return apelido;
	}
	public void setApelido(String apelido) {
		this.apelido = apelido;
	}
	public MulticastPeer getConexaoMulti() {
		return conexaoMulti;
	}
	public void setConexaoMulti(MulticastPeer conexaoMulti) {
		this.conexaoMulti = conexaoMulti;
	}
	public byte[] getChave_publica() {
		return chave_publica;
	}
	public void setChave_publica(byte[] chave_publica) {
		this.chave_publica = chave_publica;
	}
	public EncriptaDecriptaRSA getCripto() {
		return cripto;
	}
	public void setCripto(EncriptaDecriptaRSA cripto) {
		this.cripto = cripto;
	}    
	public Queue<Processo> getLista_usuarios() {
		return lista_usuarios;
	}
	public void setLista_usuarios(Queue<Processo> lista_usuarios) {
		this.lista_usuarios = lista_usuarios;
	}
	public Queue<Processo> getLista_jogadores() {
		return lista_jogadores;
	}
	public void setLista_jogadores(Queue<Processo> lista_jogadores) {
		this.lista_jogadores = lista_jogadores;
	} 
	public int getErros() {
		return erros;
	}
	public void setErros(int erros) {
		this.erros = erros;
	}
	public int getPontuacao() {
		return pontuacao;
	}
	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}
	
	
	/*esse método pode ser utilizado quando o usuário atual entra no sistema e recebe
    no parametro lista o bando de dados de todo o sistema*/
    public void limpaLista_usuarios() {
        this.lista_usuarios.clear();
    }
    
    
    public byte[] lista_usuariosToString(){        
        byte[] dados_usuarios = null;
               
        for (Processo b : getLista_usuarios())
        {    
        	//colocar tudo num vetor de bytes
           //dados_usuarios += "~"+b.apelido+"|"+b.processoID/*+"|"+b.chave_publica+*/;           
        }
        
        return dados_usuarios;
    }
    
  //método para adicionar no BD um novo usuário que venha a entrar na rede P2P
    public void adicionaUsuario(String apelido) { 
              
        Processo novo_usuario = new Processo();
        
        if(lista_usuarios == null)
            novo_usuario.processoID = 0;
        else
            novo_usuario.processoID = lista_usuarios.size() + 1;
        
        novo_usuario.apelido = apelido;
        
        this.lista_usuarios.add(novo_usuario);//adicionamos um usuário e suas infos no banco de dados       
    }
    
    public void adicionaUsuario(Processo novo_usuario) {      
               
        this.lista_usuarios.add(novo_usuario);//adicionamos um usuário e suas infos no banco de dados
    }
    
     //metodo para remover processo da lista
     public void removeUsuarioBD(Processo user){
         
         Queue<Processo> lista = lista_usuarios;
         
         int i = 0;
         for(Processo b : lista){
             
             if(b.processoID == user.processoID){
                 lista.remove(i);
                 break;
             }            
             i++;        
         }
         getConexaoMulti().enviaMensagem(this.lista_usuariosToString());        
    }
}
