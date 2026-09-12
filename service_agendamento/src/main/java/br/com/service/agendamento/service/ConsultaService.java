package br.com.service.agendamento.service;

import br.com.service.agendamento.dto.ConsultaInput;
import br.com.service.agendamento.entity.Consulta;
import br.com.service.agendamento.entity.Usuario;
import br.com.service.agendamento.exception.BusinessException;
import br.com.service.agendamento.exception.ResourceNotFoundException;
import br.com.service.agendamento.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final UsuarioService usuarioService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // PACIENTE vê só suas consultas. MEDICO/ENFERMEIRO veem todas.
    public List<Consulta> listarConsultas(Usuario usuarioLogado) {
        // Retorna apenas as consultas do paciente logado
        if (usuarioLogado.getRole() == Usuario.Role.PACIENTE) {
            return consultaRepository.findByPaciente(usuarioLogado);
        }
        // Médico ou Enfermeiro veem todas
        return consultaRepository.findAll();
    }

    // PACIENTE vê só suas consultas futuras. MEDICO/ENFERMEIRO veem todas.
    public List<Consulta> listarConsultasFuturas(Usuario usuarioLogado) {
        LocalDateTime agora = LocalDateTime.now();
        if (usuarioLogado.getRole() == Usuario.Role.PACIENTE) {
            return consultaRepository.findByPacienteAndDataHoraAfter(usuarioLogado, agora);
        }
        return consultaRepository.findAll().stream()
                .filter(c -> c.getDataHora().isAfter(agora))
                .toList();
    }

    // Buscar por ID (com validação no Resolver)
    public Consulta buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada com ID: " + id));
    }

    @Transactional
    public Consulta criarConsulta(ConsultaInput input) {
        log.info("Criando nova consulta para pacienteId={}, medicoId={}", input.pacienteId(), input.medicoId());

        Usuario paciente = usuarioService.buscarPorId(input.pacienteId());
        validarPaciente(paciente);

        Usuario medico = usuarioService.buscarPorId(input.medicoId());
        validarMedico(medico);

        LocalDateTime dataHora = LocalDateTime.parse(input.dataHora(), FORMATTER);

        Consulta consulta = Consulta.builder()
                .paciente(paciente)
                .medico(medico)
                .dataHora(dataHora)
                .status(Consulta.StatusConsulta.AGENDADA)
                .observacoes(input.observacoes())
                .build();

        return consultaRepository.save(consulta);
    }

    @Transactional
    public Consulta editarConsulta(Long id, ConsultaInput input) {
        log.info("Editando consulta ID={}", id);

        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada com ID: " + id));

        if (consulta.getStatus() == Consulta.StatusConsulta.REALIZADA) {
            throw new BusinessException("Não é possível editar uma consulta já realizada.");
        }
        if (consulta.getStatus() == Consulta.StatusConsulta.CANCELADA) {
            throw new BusinessException("Não é possível editar uma consulta cancelada.");
        }

        Usuario paciente = usuarioService.buscarPorId(input.pacienteId());
        validarPaciente(paciente);

        Usuario medico = usuarioService.buscarPorId(input.medicoId());
        validarMedico(medico);

        LocalDateTime dataHora = LocalDateTime.parse(input.dataHora(), FORMATTER);

        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHora(dataHora);
        consulta.setObservacoes(input.observacoes());

        return consultaRepository.save(consulta);
    }

    @Transactional
    public Consulta cancelarConsulta(Long id) {
        Consulta consulta = buscarPorId(id);

        if (consulta.getStatus() == Consulta.StatusConsulta.REALIZADA) {
            throw new BusinessException("Não é possível cancelar uma consulta já realizada.");
        }
        if (consulta.getStatus() == Consulta.StatusConsulta.CANCELADA) {
            throw new BusinessException("Esta consulta já está cancelada.");
        }

        consulta.setStatus(Consulta.StatusConsulta.CANCELADA);
        return consultaRepository.save(consulta);
    }

    @Transactional
    public Consulta realizarConsulta(Long id) {
        log.info("Realizando consulta ID={}", id);

        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada com ID: " + id));

        // Só pode realizar se estiver AGENDADA
        if (consulta.getStatus() != Consulta.StatusConsulta.AGENDADA) {
            throw new BusinessException("Apenas consultas com status AGENDADA podem ser realizadas.");
        }

        consulta.setStatus(Consulta.StatusConsulta.REALIZADA);
        return consultaRepository.save(consulta);
    }


    private void validarPaciente(Usuario usuario) {
        if (usuario.getRole() != Usuario.Role.PACIENTE) {
            throw new BusinessException("O ID informado não corresponde a um paciente válido.");
        }
    }

    private void validarMedico(Usuario usuario) {
        if (usuario.getRole() != Usuario.Role.MEDICO) {
            throw new BusinessException("O ID informado não corresponde a um médico válido.");
        }
    }
}