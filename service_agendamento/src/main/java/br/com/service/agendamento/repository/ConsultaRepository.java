package br.com.service.agendamento.repository;

import br.com.service.agendamento.entity.Consulta;
import br.com.service.agendamento.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    //Busca consultas por paciente
    List<Consulta> findByPaciente(Usuario paciente);
    //Busca consultas futuras por paciente
    List<Consulta> findByPacienteAndDataHoraAfter(Usuario paciente, LocalDateTime data);
    //Buscar consultas por médico
    List<Consulta> findByMedico(Usuario medico);
}