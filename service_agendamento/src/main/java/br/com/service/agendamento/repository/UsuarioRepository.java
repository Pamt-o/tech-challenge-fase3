package br.com.service.agendamento.repository;

import br.com.service.agendamento.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    Optional<Object> findByEmail(java.lang.String email);
}