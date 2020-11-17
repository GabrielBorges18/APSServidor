/*
 * Trabalho APS
 */
package APSServidor;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ObjectOutputStream;

/**
 *
 * @author TW
 */
public class Usuario {

    private Integer id;
    private String nome;
    private ObjectOutputStream saida;
    private String ip;
    private String usuario;
    private String email;
    private int cargo;
    private ObjectOutputStream saidaVoz;

    public Usuario() {

    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    //Construtor Chat
    public Usuario(String usuario, ObjectOutputStream saida, String ip) {
        this.usuario = usuario;
        this.saida = saida;
        this.ip = ip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //Construtor para apenas voz
    public Usuario(ObjectOutputStream saidaVoz) {
        this.saidaVoz = saidaVoz;
    }

    public ObjectOutputStream getSaidaVoz() {
        return saidaVoz;
    }

    public void setSaidaVoz(ObjectOutputStream saidaVoz) {
        this.saidaVoz = saidaVoz;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    private String senha;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ObjectOutputStream getSaida() {
        return saida;
    }

    public void setSaida(ObjectOutputStream saida) {
        this.saida = saida;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
