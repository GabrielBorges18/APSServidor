/*
 * Trabalho APS
 */
package APSServidor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gabriel
 */
public class APSServidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Cria nova instancia de conexão com o Banco
        Conexao conexao = new Conexao();
        try {
            Connection con = conexao.conectar();
            //Inicia Servidor
            ServidorChat servidorChat = new ServidorChat();
            servidorChat.setCon(con);
            servidorChat.setVisible(true);
            servidorChat.start();
        } catch (SQLException e) {
            Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
