package br.com.service.agendamento.service;

import br.com.service.agendamento.entity.Usuario;
import br.com.service.agendamento.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public boolean autenticar(String username, String senha) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario == null) {
            System.out.println("❌ Usuário não encontrado: " + username);
            return false;
        }

        // ✅ CORRETO - Usando matches() do bcrypt
        boolean senhaCorreta = passwordEncoder.matches(senha, usuario.getPassword());

        System.out.println("🔐 Usuário: " + username);
        System.out.println("🔐 Senha informada: " + senha);
        System.out.println("🔐 Hash armazenado: " + usuario.getPassword());
        System.out.println("🔐 Autenticado: " + senhaCorreta);

        return senhaCorreta;
    }

}
