/*
 * Trabalho APS
 */
package APSServidor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author TW
 */
public class Conexao {

    private String url = "jdbc:sqlite:APS1.db";

    public Connection conectar() throws SQLException {
        Connection con = DriverManager.getConnection(url);
        String create = "CREATE TABLE IF NOT EXISTS mensagens(id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " 			      usuario_id int not null,"
                + "                                           ip varchar(20) not null,"
                + "                                           data_hora varchar(20),"
                + "                                           mensagem varchar(10000),"
                + "                                           privado int not null );";
        con.prepareStatement(create).execute();
        String create2 = "CREATE TABLE IF NOT EXISTS usuarios(id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "                                          nome varchar(100),"
                + "                                          email varchar(50),"
                + "                                          usuario varchar(20),"
                + "                                          senha varchar(20),"
                + "                                          cargo int)";
        con.prepareStatement(create2).execute();
        //Verifica se usuario já existe
        String verifica = "SELECT COUNT(*) FROM usuarios "
                + "         WHERE usuario = ?";

        PreparedStatement st = con.prepareStatement(verifica);
        st.setString(1, "admin");
        ResultSet rs = st.executeQuery();
        if (rs.getInt(1) == 0) {
            String insert = "INSERT INTO usuarios (nome, email, usuario, senha, cargo)"
                    + "     VALUES ('Administrador', '', 'admin', 'admin', 2)";
            con.prepareStatement(insert).executeUpdate();
        }
        return con;
    }
}
