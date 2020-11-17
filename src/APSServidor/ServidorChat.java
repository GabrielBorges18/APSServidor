/*
 * Trabalho APS
 */
package APSServidor;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import Mensagem.Mensagem;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import static org.bytedeco.opencv.global.opencv_core.CV_32SC1;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;

/**
 *
 * @author Gabriel Borges
 */
public class ServidorChat extends javax.swing.JFrame {

    //Lista com os Usuarios Conectados
    private ArrayList<Usuario> usuariosConectados = new ArrayList<Usuario>();
    //Socket do Servidor
    private ServerSocket servidor;
    //Conexão do Banco
    private Connection con;
    //Reconhecedor para a Autenticação Facial
    private FaceRecognizer reconhecedor;

    public ServidorChat() {
        initComponents();
    }

    public void start() {
        try {
            //Instancia Classe do ServerSocket
            servidor = new ServerSocket(1250);
            String ipServer = InetAddress.getLocalHost().getHostAddress();
            lblIpServer.setText(ipServer);
            //Executa treinamento do Reconhecimento facial, para ser usado posteriormente pelo algoritmo
            efetuaTreinamento();
            loading.setText("Servidor pronto para ser Usado!");
            while (true) {
                //Aguarda "Infinitamente" novas conexões
                Socket SocketCon;
                //Usuario de Servidor
                Usuario userServer = new Usuario("Servidor", null, ipServer);
                try {
                    //Thread ficará travada até linha abaixo ser executada, ou seja, novo usuario se conectar
                    SocketCon = servidor.accept();
                    
                    //Armezena variável responsável pela Saida (Servidor envia pro Usuario)
                    OutputStream streamS = SocketCon.getOutputStream();
                    ObjectOutputStream saida = new ObjectOutputStream(streamS);

                    //Armezena variável responsável pela Entrada (Usuario envia pro Servidor)
                    InputStream streamE = SocketCon.getInputStream();
                    ObjectInputStream entrada = new ObjectInputStream(streamE);
                    
                    //Instancia Thread responsável por gerenciar conexão com aquele novo usuario, para que a Thread principal possa aguardar nova conexão.
                    GerenciadorDeConexoes newGerenciador = new GerenciadorDeConexoes(userServer, SocketCon, entrada, saida, con, reconhecedor);
                    newGerenciador.start();

                } catch (IOException ex) {
                    Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Porta 1250 ou 1251 não está disponivel");
            this.dispose();
        }
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    /**
     * Função responsável por ler todo o diretório de fotos do Servidor e efetuar o treinamento
     * para que o algoritmo de reconhecimento consiga trabalhar
     */
    public void efetuaTreinamento() {
        //Pega todos os arquivos e lista eles
        File diretorio = new File("src\\fotos");
        FilenameFilter filtroImagem = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String nome) {
                return nome.endsWith(".jpg");
            }
        };
        File[] arquivos = diretorio.listFiles(filtroImagem);
        if (arquivos.length > 0) {
            //Efetua o treinamento
            MatVector fotos = new MatVector(arquivos.length);
            Mat rotulos = new Mat(arquivos.length, 1, CV_32SC1);
            IntBuffer rotulosBuffer = rotulos.createBuffer();
            int contador = 0;
            for (File imagem : arquivos) {
                //Le imagem em escalas de cinza
                Mat foto = imread(imagem.getAbsolutePath(), IMREAD_GRAYSCALE);
                //Pega id do usuario (O formato do arquivo esta como: pessoa.IDDOUSUARIO.SEQUENCIAL.jpg)
                int id = Integer.parseInt(imagem.getName().split("\\.")[1]);
                //Padroniza tamanho (Garantia)
                opencv_imgproc.resize(foto, foto, new Size(160, 160));
                //Adiciona no MatVector de Fotos
                fotos.put(contador, foto);
                //Adiciona no Buffer de Rotulos
                rotulosBuffer.put(contador, id);
                contador++;
            }
            //Efetua treinamento utilizando como parâmetro o MatVector de Fotos e o Buffer de Rotulos
            FaceRecognizer treinoLBPH = LBPHFaceRecognizer.create();
            treinoLBPH.train(fotos, rotulos);
            //Salva Classificador
            treinoLBPH.save("src\\recursos\\classificadorLBPH.yml");
        }
        //Instancia Reconhecedor e lê o Classificador recém criado
        reconhecedor = LBPHFaceRecognizer.create();
        reconhecedor.read("src\\recursos\\classificadorLBPH.yml");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Bg = new javax.swing.JPanel();
        titulo = new javax.swing.JLabel();
        Cont = new javax.swing.JPanel();
        ipServidorlbl = new javax.swing.JLabel();
        lblIpServer = new javax.swing.JLabel();
        lblUsers = new javax.swing.JLabel();
        Conectados = new javax.swing.JLabel();
        loading = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ambiente de Comunicação(Server)");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        Bg.setBackground(new java.awt.Color(0, 0, 153));

        titulo.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N

        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBackground(new java.awt.Color(0, 0, 153));
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        titulo.setText("Servidor");

        Cont.setBackground(new java.awt.Color(255, 255, 255));
        Cont.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        ipServidorlbl.setText("IP do Servidor:");

        lblUsers.setText("Usuarios Conectados(Chat):");

        Conectados.setText("0");

        loading.setText("Carregando Servidor, Aguarde...");

        javax.swing.GroupLayout ContLayout = new javax.swing.GroupLayout(Cont);
        Cont.setLayout(ContLayout);
        ContLayout.setHorizontalGroup(
            ContLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ContLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ContLayout.createSequentialGroup()
                        .addComponent(loading, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(ContLayout.createSequentialGroup()
                        .addGroup(ContLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ContLayout.createSequentialGroup()
                                .addComponent(ipServidorlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ContLayout.createSequentialGroup()
                                .addComponent(lblUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(ContLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ContLayout.createSequentialGroup()
                                .addComponent(Conectados, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(ContLayout.createSequentialGroup()
                                .addComponent(lblIpServer, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42))))))
        );
        ContLayout.setVerticalGroup(
            ContLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(ContLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ipServidorlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIpServer, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ContLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Conectados)
                    .addComponent(lblUsers))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loading)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout BgLayout = new javax.swing.GroupLayout(Bg);
        Bg.setLayout(BgLayout);
        BgLayout.setHorizontalGroup(
            BgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
            .addGroup(BgLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(Cont, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        BgLayout.setVerticalGroup(
            BgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BgLayout.createSequentialGroup()
                .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Cont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Bg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Metodo responsável para mandar a mensagem recebida para os demais usuarios
     */
    private void replicarMensagem(Usuario origem, String msg, int privado) {
        new Thread(() -> {
            try {
                //Pega data atual e formata
                DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String dataAtual = formatador.format(date);
                //Só salva no banco se mensagem enviada não foi enviada pelo Servidor
                if (!origem.getUsuario().equals("Servidor")) {
                    String sql_insert = "INSERT INTO mensagens(usuario_id,ip,data_hora,mensagem, privado)"
                            + "VALUES(?, ?, ?, ?, ?)";
                    PreparedStatement st = this.con.prepareStatement(sql_insert);
                    st.setInt(1, origem.getId());
                    st.setString(2, origem.getIp());
                    st.setString(3, dataAtual);
                    st.setString(4, msg);
                    st.setInt(5, privado);
                    if (st.executeUpdate() > 0) {
                        //System.out.println("Msg Gravada com Sucesso");
                    } else {
                        //System.out.println("Erro ao gravar MSG");
                    }
                }
                //Replica mensagem para todos os usuarios conectados
                for (Usuario user : usuariosConectados) {
                    ObjectOutputStream cliente = user.getSaida();
                    if (privado == 1) {
                        //Somente moderados e adms recebem mensagens privadas
                        if (user.getCargo() > 0) {
                            cliente.writeObject(new Mensagem("NovaMensagem", "*PRIVADO -  " + dataAtual + " - " + origem.getUsuario() + ": " + msg));
                        }
                    } else {
                        //Todos conectados recebem mensagem comum
                        cliente.writeObject(new Mensagem("NovaMensagem", dataAtual + " - " + origem.getUsuario() + ": " + msg));
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    /**
     * Função responsável por carregar histórico de mensagens
     */
    private void carregarMsgs(Usuario user) {
        ResultSet rs;
        try {
            rs = this.con.prepareStatement("Select m.data_hora as data_hora,u.usuario as usuario, m.mensagem as mensagem,m.privado from mensagens m "
                    + "                 join usuarios u on(u.id = m.usuario_id)"
                    + "").executeQuery();
            while (rs.next()) {
                int privado = rs.getInt("privado");
                if (privado == 1) {
                    if (user.getCargo() > 0) {
                        user.getSaida().writeObject(new Mensagem("MsgsAnteriores", "*PRIVADO -  " + rs.getString("data_hora") + " - " + rs.getString("usuario") + ": " + rs.getString("mensagem")));
                    }
                } else {
                    user.getSaida().writeObject(new Mensagem("MsgsAnteriores", rs.getString("data_hora") + " - " + rs.getString("usuario") + ": " + rs.getString("mensagem")));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Função responsável por buscar usuario, seu cargo e seu histórico (Função de Admin no client-side)
     */
    private void buscaUsuario(Usuario origem, Mensagem msg) {
        new Thread(() -> {
            try {
                //Verifica se usuario já existe
                String verifica = "SELECT COUNT(*), cargo FROM usuarios "
                        + "         WHERE usuario = ?";

                PreparedStatement st = this.con.prepareStatement(verifica);
                st.setString(1, msg.getMsg());
                ResultSet rs = st.executeQuery();
                //System.out.println(rs.getInt(1));
                if (rs.getInt(1) == 0) {
                    origem.getSaida().writeObject(new Mensagem("Erro", "Usuario não cadastrado"));
                } else {
                    String historico = "";
                    st = this.con.prepareStatement("Select m.data_hora as data_hora,u.usuario as usuario, m.mensagem as mensagem,m.privado from mensagens m "
                            + "                 join usuarios u on(u.id = m.usuario_id) "
                            + "                 where u.usuario = ?"
                            + "");
                    st.setString(1, msg.getMsg());
                    ResultSet rs2 = st.executeQuery();
                    while (rs2.next()) {
                        //System.out.println("Pegando historico");
                        int privado = rs2.getInt("privado");
                        if (privado == 1) {
                            historico += "*PRIVADO -  " + rs2.getString("data_hora") + " - " + rs2.getString("usuario") + ": " + rs2.getString("mensagem") + "\r\n";
                        } else {
                            historico += rs2.getString("data_hora") + " - " + rs2.getString("usuario") + ": " + rs2.getString("mensagem") + "\r\n";
                        }
                    }
                    //System.out.println("Chegou aqui");
                    //System.out.println(historico);
                    Mensagem retorno = new Mensagem("Sucesso", historico, msg.getMsg(), rs.getInt(2));
                    origem.getSaida().writeObject(retorno);
                }
            } catch (SQLException e) {
                Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, e);
            } catch (IOException ex) {
                Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
    /**
     * Função responsável por mudar cargo de usuario (Função de Admin no client-side)
     */
    private void mudarCargo(Usuario origem, Mensagem msg) {
        try {
            String upd = "UPDATE usuarios SET cargo = ? WHERE usuario = ?";
            PreparedStatement st = this.con.prepareStatement(upd);
            st.setInt(1, msg.getCargo());
            st.setString(2, msg.getNomeUsuario());
            if (st.executeUpdate() > 0) {
                origem.getSaida().writeObject(new Mensagem("retornoAlteraCargo", "Cargo alterado com sucesso!"));
            } else {
                origem.getSaida().writeObject(new Mensagem("retornoAlteraCargo", "Erro ao alterar cargo do Usuario!"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServidorChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServidorChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServidorChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServidorChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServidorChat().setVisible(true);
            }
        });
    }

    /**
     * Classe responsável por armazenar a conexão por usuario
     */
    public class Conexao extends Thread {

        private final ObjectInputStream entrada;
        private final Usuario usuario;
        private final Usuario server;
        private final Socket con;
        private boolean status = true;

        public Conexao(Socket con, ObjectInputStream entrada, Usuario usuario, Usuario server) {
            this.con = con;
            this.entrada = entrada;
            this.usuario = usuario;
            this.server = server;
        }

        /**
         * Thread responsável por processar mensagem recebida por usuario
         */
        private void gerenciaMsg(Mensagem msg) {
            new Thread(() -> {
                //Verifica se a mensagem é a de desconexão do ServidorChat
                if ("SairDoServidor".equals(msg.getTipo())) {
                    usuariosConectados.remove(usuario);
                    Conectados.setText("" + usuariosConectados.size());
                    this.interrupt();
                } else if ("CarregarMensagensAnteriores".equals(msg.getTipo())) {
                    carregarMsgs(this.usuario);
                } else if ("NovaMensagem".equals(msg.getTipo())) {
                    replicarMensagem(this.usuario, msg.getMsg(), msg.getPrivado());
                } else if ("BuscarHistoricoUsuario".equals(msg.getTipo())) {
                    //System.out.println("Entrou aqui" + msg.getMsg());
                    buscaUsuario(usuario, msg);
                } else if ("mudarCargoUsuario".equals(msg.getTipo())) {
                    mudarCargo(usuario, msg);
                }
            }).start();
        }

        //Thread
        @Override
        public void run() {

            //Loop "infinito"
            while (status) {
                try {
                    //Quando receber mensagem do cliente
                    if (con.getInputStream().available() > 0) {
                        //Chama uma THREAD nova para lidar com a mensagem, para deixar essa liberada para receber outra
                        Mensagem msg = (Mensagem) entrada.readObject();
                        gerenciaMsg(msg);
                    }
                } catch (IOException ex) {
                    status = false;
                    usuariosConectados.remove(usuario);
                    Conectados.setText("" + usuariosConectados.size());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            usuariosConectados.remove(usuario);
            Conectados.setText("" + usuariosConectados.size());
        }
    }

    public class GerenciadorDeConexoes extends Thread {

        private final ObjectInputStream entrada;
        private final ObjectOutputStream saida;
        private final Connection con;
        private final Socket SocketCon;
        private final Usuario userServer;
        private FaceRecognizer reconhecedor;

        public GerenciadorDeConexoes(Usuario userServer, Socket SocketCon, ObjectInputStream entrada, ObjectOutputStream saida, Connection con, FaceRecognizer reconhecedor) {
            this.entrada = entrada;
            this.saida = saida;
            this.con = con;
            this.SocketCon = SocketCon;
            this.userServer = userServer;
            this.reconhecedor = reconhecedor;
        }

        //Thread
        @Override
        public void run() {
            while (!SocketCon.isClosed()) {
                try {
                    while (!SocketCon.isClosed()) {
                        if (SocketCon.getInputStream().available() > 0) {
                            break;
                        }
                        this.sleep(10);
                    }
                    //Saiu do While, usuario entrou em alguma tela, descobre qual
                    Mensagem tela = (Mensagem) entrada.readObject();
                    if (tela.getTipo().equals("EntrouTelaCadastro")) {
                        //Tenta inserir no banco informações enviadas
                        while (!SocketCon.isClosed()) {
                            if (SocketCon.getInputStream().available() > 0) {
                                Mensagem msg = (Mensagem) entrada.readObject();
                                //System.out.println(msg);
                                //Verifica se usuario fechou a janela
                                if (msg.getTipo().equals("SairDoServidor")) {
                                    entrada.close();
                                    saida.close();
                                    SocketCon.close();
                                    //System.out.println("Janela Fechada");
                                    break;
                                } else if (msg.getTipo().equals("EfetuarCadastro")) {
                                    System.out.println(msg.getMsg());
                                    String Json = msg.getMsg();
                                    Gson g = new Gson();
                                    Usuario newUser = g.fromJson(Json, Usuario.class);
                                    //Verifica se usuario já existe
                                    String verifica = "SELECT COUNT(*) FROM usuarios "
                                            + "         WHERE usuario = ?";

                                    PreparedStatement st = this.con.prepareStatement(verifica);
                                    st.setString(1, newUser.getUsuario());
                                    ResultSet rs = st.executeQuery();
                                    if (rs.getInt(1) > 0) {
                                        saida.writeObject(new Mensagem("Erro", "Usuario já existe!"));
                                    } else {
                                        String insert = "INSERT INTO usuarios (nome, email, usuario, senha, cargo)"
                                                + "     VALUES (?, ?, ?, ?, 0)";
                                        st = this.con.prepareStatement(insert);
                                        st.setString(1, newUser.getNome());
                                        st.setString(2, newUser.getEmail());
                                        st.setString(3, newUser.getUsuario());
                                        st.setString(4, newUser.getSenha());
                                        if (st.executeUpdate() > 0) {
                                            String verifica2 = "SELECT count(*), id, nome FROM usuarios "
                                                    + "         WHERE usuario = ? AND"
                                                    + "               senha = ?";
                                            PreparedStatement st2 = this.con.prepareStatement(verifica2);
                                            st2.setString(1, newUser.getUsuario());
                                            st2.setString(2, newUser.getSenha());
                                            ResultSet rs2 = st2.executeQuery();
                                            saida.writeObject(new Mensagem("Retorno", "Usuário Cadastrado com Sucesso", rs2.getInt(2)));
                                        } else {
                                            saida.writeObject(new Mensagem("Erro", "Erro ao Criar usuario!"));
                                            System.out.println("Erro ao gravar");
                                            continue;
                                        }
                                    }
                                } else if (msg.getTipo().equals("EnvioImagemCadastro")) {
                                    //Mat imagemMat = new Mat(msg.getImagem());
                                    //System.out.println("Entrou aqui");
                                    imwrite("src\\fotos\\pessoa." + msg.getIdUsuario() + "." + msg.getOrdem() + ".jpg", msg.getImagem());
                                } else if (msg.getTipo().equals("FinalizoCadsatroImagem")) {
                                    new Thread(() -> {
                                        loading.setText("Efetuando treinamento, não sera possivel efetuar autenticação Biométrica");
                                        efetuaTreinamento();
                                        loading.setText("Servidor pronto para ser Usado!");
                                    }).start();
                                }
                            }
                        }
                    } else if (tela.getTipo().equals("TelaLogin")) {
                        while (!SocketCon.isClosed()) {
                            //Verifica se usuario fechou a janela
                            if (SocketCon.getInputStream().available() > 0) {
                                Mensagem msg = (Mensagem) entrada.readObject();
                                if (msg.getTipo().equals("SairDoServidor")) {
                                    entrada.close();
                                    saida.close();
                                    SocketCon.close();
                                    break;
                                } else if (msg.getTipo().equals("TentativaLoginJSON")) {
                                    String Json = msg.getMsg();
                                    Gson g = new Gson();
                                    Usuario user = g.fromJson(Json, Usuario.class);

                                    String verifica = "SELECT count(*), id, nome, cargo FROM usuarios "
                                            + "         WHERE usuario = ? AND"
                                            + "               senha = ?";
                                    PreparedStatement st = this.con.prepareStatement(verifica);
                                    st.setString(1, user.getUsuario());
                                    st.setString(2, user.getSenha());

                                    ResultSet rs = st.executeQuery();
                                    if (rs.getInt(1) == 0) {
                                        saida.writeObject(new Mensagem("Erro", "Usuario ou senha incorreto(s)"));
                                        continue;
                                    } else {
                                        boolean achou = false;
                                        for (Usuario u : usuariosConectados) {
                                            if (u.getUsuario().equals(user.getUsuario())) {
                                                //Usuario já conectado
                                                achou = true;
                                                break;
                                            }
                                        }
                                        if (achou) {
                                            saida.writeObject(new Mensagem("Erro", "Usuario ja esta logado"));
                                            continue;
                                        }

                                        user.setSaida(saida);
                                        user.setIp(SocketCon.getLocalAddress().getHostAddress());
                                        user.setId(rs.getInt(2));
                                        user.setNome(rs.getString(3));
                                        user.setCargo(rs.getInt(4));
                                        System.out.println(user.getCargo() + " " + user.getNome());
                                        saida.writeObject(new Mensagem("Sucesso", "Usuario Logado com Sucesso!", user.getNome(), user.getCargo()));
                                        usuariosConectados.add(user);
                                        //Inicia para esse usuario uma nova Thread que ficará ouvindo as mensagens que ele envia
                                        //OBS: Cada Thread é responsável por ouvir a mensagem recebida e enviar ao servidor 
                                        //Enquanto o ServidorChat é responsável por receber essas mensagens e enviar para todos os usuarios conectados
                                        replicarMensagem(userServer, "Usuario - " + user.getUsuario() + " Conectado ao chat", 0);
                                        Thread conexao = new Conexao(SocketCon, entrada, user, userServer);
                                        conexao.start();
                                        Conectados.setText("" + usuariosConectados.size());
                                        break;
                                    }
                                } else if (msg.getTipo().equals("TentativaLoginImagem")) {
                                    IntPointer rotulo = new IntPointer(1);
                                    DoublePointer confianca = new DoublePointer(1);
                                    Mat imagem = msg.getImagem();;
                                    reconhecedor.predict(imagem, rotulo, confianca);
                                    int predicao = rotulo.get(0);
                                    double nivelConfianca = confianca.get(0);
                                    //System.out.println(confianca.get(0));
                                    //imwrite("src\\fotos\\testeEnvio." + msg.getIdUsuario() + "." + msg.getOrdem() + ".jpg", imagem);
                                    if (predicao == -1 || nivelConfianca > 70) {
                                        saida.writeObject(new Mensagem("Erro", "Não identificado"));
                                        //System.out.println("Não identificou");
                                    } else {
                                        //RETORNA USUARIO
                                        Usuario user = new Usuario();
                                        String verifica = "SELECT count(*), id, nome, cargo, usuario FROM usuarios "
                                                + "         WHERE id = ?";
                                        PreparedStatement st = this.con.prepareStatement(verifica);
                                        st.setInt(1, predicao);
                                        ResultSet rs = st.executeQuery();
                                        if (rs.getInt(1) == 0) {
                                            //System.out.println("Não reconheceu");
                                            saida.writeObject(new Mensagem("Erro", "Não identificado"));
                                        } else {
                                            boolean achou = false;
                                            for (Usuario u : usuariosConectados) {
                                                if (u.getUsuario().equals(user.getUsuario())) {
                                                    //Usuario já conectado
                                                    achou = true;
                                                    break;
                                                }
                                            }
                                            if (achou) {
                                                saida.writeObject(new Mensagem("Erro2", "Usuario ja esta logado"));
                                                continue;
                                            }
                                            user.setSaida(saida);
                                            user.setIp(SocketCon.getLocalAddress().getHostAddress());
                                            user.setId(rs.getInt(2));
                                            user.setNome(rs.getString(3));
                                            user.setCargo(rs.getInt(4));
                                            user.setUsuario(rs.getString(5));
                                            System.out.println("Reconhecido rosto de " + user.getNome());
                                            saida.writeObject(new Mensagem("Sucesso", "Usuario Logado com Sucesso!", user.getNome(), user.getCargo()));
                                            usuariosConectados.add(user);
                                            //Inicia para esse usuario uma nova Thread que ficará ouvindo as mensagens que ele envia
                                            //OBS: Cada Thread é responsável por ouvir a mensagem recebida e enviar ao servidor 
                                            //Enquanto o ServidorChat é responsável por receber essas mensagens e enviar para todos os usuarios conectados
                                            replicarMensagem(userServer, "Usuario - " + user.getUsuario() + " Conectado ao chat", 0);
                                            Thread conexao = new Conexao(SocketCon, entrada, user, userServer);
                                            conexao.start();
                                            Conectados.setText("" + usuariosConectados.size());
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                    }
                } catch (IllegalStateException | SQLException ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("Usuario Fechou Tela ou mandou pro servidor algo inesperado");
                    try {
                        saida.writeObject(new Mensagem("Erro", "Erro ao Criar usuario!"));
                    } catch (IOException ex1) {
                        Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                } catch (IOException ex) {
                    System.out.println("Usuario Desconectou");
                    break;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            //System.out.println("Cliente desconectado");
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Bg;
    private javax.swing.JLabel Conectados;
    private javax.swing.JPanel Cont;
    private javax.swing.JLabel ipServidorlbl;
    private javax.swing.JLabel lblIpServer;
    private javax.swing.JLabel lblUsers;
    private javax.swing.JLabel loading;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}
