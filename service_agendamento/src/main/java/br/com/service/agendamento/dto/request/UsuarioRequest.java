package br.com.service.agendamento.dto.request;

import jakarta.validation.constraints.*;

public record UsuarioRequest(
        @NotBlank(message = "O nome de usuário é obrigatório")
        String username,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String password,

        @NotBlank
        String nome,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email deve ter um formato válido")
        String email,

        @NotBlank(message = "O perfil é obrigatório")
        @Pattern(regexp = "MEDICO|ENFERMEIRO|PACIENTE",
                message = "O perfil deve ser MEDICO, ENFERMEIRO ou PACIENTE")
        String role
) {}
