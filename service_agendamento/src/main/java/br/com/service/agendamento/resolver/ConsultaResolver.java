package br.com.service.agendamento.resolver;

import br.com.service.agendamento.dto.ConsultaInput;
import br.com.service.agendamento.dto.EventoNotificacaoDTO;
import br.com.service.agendamento.entity.Consulta;
import br.com.service.agendamento.entity.Usuario;
import br.com.service.agendamento.producer.ConsultaProducer;
import br.com.service.agendamento.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ConsultaResolver {

    private final ConsultaService consultaService;
    private final ConsultaProducer consultaProducer;

    //LISTAR TODAS AS CONSULTAS (PACIENTE VÊ SÓ AS DELE)
    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO', 'PACIENTE')")
    public List<Consulta> consultas() {
        Usuario usuario = getUsuarioLogado();
        return consultaService.listarConsultas(usuario);
    }

    //LISTAR CONSULTAS FUTURAS
    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO', 'PACIENTE')")
    public List<Consulta> consultasFuturas() {
        Usuario usuario = getUsuarioLogado();
        return consultaService.listarConsultasFuturas(usuario);
    }

    //BUSCAR UMA CONSULTA POR ID (PACIENTE só pode ver se for dele)
    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO', 'PACIENTE')")
    public Consulta consulta(@Argument Long id) {
        Usuario usuario = getUsuarioLogado();
        Consulta consulta = consultaService.buscarPorId(id);

        //Se for PACIENTE, verifica se a consulta é dele
        if (usuario.getRole() == Usuario.Role.PACIENTE) {
            if (!consulta.getPaciente().getId().equals(usuario.getId())) {
                throw new RuntimeException("Acesso negado: você só pode visualizar suas próprias consultas.");
            }
        }

        return consulta;
    }

    //CRIAR CONSULTA (MÉDICO OU ENFERMEIRO)
    @MutationMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public Consulta criarConsulta(@Argument ConsultaInput input) {
        Consulta consulta = consultaService.criarConsulta(input);

        //Publica evento no RabbitMQ
        EventoNotificacaoDTO evento = new EventoNotificacaoDTO(
                consulta.getId(),
                consulta.getPaciente().getEmail(),
                consulta.getPaciente().getNome(),
                consulta.getDataHora(),
                "CRIADA"
        );
        consultaProducer.enviarEventoCriacao(evento);

        return consulta;
    }

    //EDITAR CONSULTA (MÉDICO OU ENFERMEIRO)
    @MutationMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public Consulta editarConsulta(@Argument Long id, @Argument ConsultaInput input) {
        Consulta consulta = consultaService.editarConsulta(id, input);

        // Publica evento no RabbitMQ
        EventoNotificacaoDTO evento = new EventoNotificacaoDTO(
                consulta.getId(),
                consulta.getPaciente().getEmail(),
                consulta.getPaciente().getNome(),
                consulta.getDataHora(),
                "EDITADA"
        );
        consultaProducer.enviarEventoEdicao(evento);

        return consulta;
    }

    //CANCELAR CONSULTA (MÉDICO OU ENFERMEIRO)
    @MutationMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public Consulta cancelarConsulta(@Argument Long id) {
        Consulta consulta = consultaService.cancelarConsulta(id);

        EventoNotificacaoDTO evento = new EventoNotificacaoDTO(
                consulta.getId(),
                consulta.getPaciente().getEmail(),
                consulta.getPaciente().getNome(),
                consulta.getDataHora(),
                "CANCELADA"
        );
        consultaProducer.enviarEventoEdicao(evento);

        return consulta;
    }

    @MutationMapping
    @PreAuthorize("hasRole('MEDICO')")  //Apenas Médico pode realizar
    public Consulta realizarConsulta(@Argument Long id) {
        Consulta consulta = consultaService.realizarConsulta(id);

        // Publica evento no RabbitMQ
        EventoNotificacaoDTO evento = new EventoNotificacaoDTO(
                consulta.getId(),
                consulta.getPaciente().getEmail(),
                consulta.getPaciente().getNome(),
                consulta.getDataHora(),
                "REALIZADA"
        );
        consultaProducer.enviarEventoEdicao(evento);

        return consulta;
    }

    private Usuario getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) auth.getPrincipal();
    }
}