package com.auth.auth.services;
import java.util.Set;

import com.auth.auth.dto.UsuarioRequest;
import com.auth.auth.dto.UsuarioResponse;

public interface UsuarioService {

    Set<UsuarioResponse> listar();

    UsuarioResponse registrar(UsuarioRequest request);

    UsuarioResponse eliminar(String username);
}