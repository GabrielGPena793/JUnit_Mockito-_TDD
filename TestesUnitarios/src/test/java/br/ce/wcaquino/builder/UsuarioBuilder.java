package br.ce.wcaquino.builder;

import br.ce.wcaquino.entidades.Usuario;

import java.util.Objects;

public class UsuarioBuilder {

    private Usuario usuario;

    private UsuarioBuilder(){};


    public static UsuarioBuilder umUsuario(){
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.usuario = new Usuario();
        builder.usuario.setNome("Gabriel");
        return builder;
    }

    public UsuarioBuilder comNome(String nome){
        usuario.setNome(nome);
        return this;
    }

    public Usuario agora(){
        return usuario;
    }
}
