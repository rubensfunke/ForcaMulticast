package forca;

import forca.gui.Forca;
import forca.gui.NovoUsuario;

import java.security.Provider;
import java.security.Security;

import javax.swing.JFrame;

import forca.pn.EncriptaDecriptaRSA;
import forca.pn.MulticastPeer;
import sun.security.jca.Providers;
//classe principal que instancia a interface grafica e a conexao multicast
public class MainClass {
    
    
    public static void main(String[] args) {
        
        final Processo sessao = new Processo();//objeto que irá guardar as informações do processo
        sessao.cripto = new EncriptaDecriptaRSA(sessao);
        sessao.cripto.geraChave();       
        sessao.setChave_publica(sessao.cripto.chavePublica.getEncoded());
        //sessao.cripto.teste();
        
        //iniciamos uma conexao multicast e salvamos os dados da conexao no objeto sessao
        sessao.setConexaoMulti(new MulticastPeer(sessao));    
        
        NovoUsuario user = new NovoUsuario(sessao);        
    }
}
