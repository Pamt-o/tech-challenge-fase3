package br.com.service.agendamento.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        log.warn("⚠️ Exceção capturada no GraphQL: {} — {}", ex.getClass().getSimpleName(), ex.getMessage());

        // 🔹 Erros de negócio (ex: "Username já em uso")
        if (ex instanceof BusinessException) {
            return buildError(ex.getMessage(), ErrorType.BAD_REQUEST, env);
        }

        // 🔹 Recurso não encontrado (ex: "Consulta não encontrada")
        if (ex instanceof ResourceNotFoundException) {
            return buildError(ex.getMessage(), ErrorType.NOT_FOUND, env);
        }

        // 🔹 Erros de validação (@Valid)
        if (ex instanceof ConstraintViolationException validationEx) {
            String mensagens = validationEx.getConstraintViolations().stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));
            return buildError(mensagens, ErrorType.BAD_REQUEST, env);
        }

        // 🔹 Argumentos inválidos (ex: enum inválido)
        if (ex instanceof IllegalArgumentException) {
            return buildError(ex.getMessage(), ErrorType.BAD_REQUEST, env);
        }

        // 🔹 Acesso negado (Spring Security)
        if (ex instanceof AccessDeniedException) {
            return buildError("Acesso negado. Você não tem permissão para executar esta ação.",
                    ErrorType.FORBIDDEN, env);
        }

        // 🔹 Fallback genérico — não expõe detalhes internos
        log.error("❌ Erro interno não tratado no GraphQL", ex);
        return buildError("Erro interno no servidor. Contate o administrador.",
                ErrorType.INTERNAL_ERROR, env);
    }

    /**
     * Método utilitário para construir erros GraphQL de forma padronizada.
     */
    private GraphQLError buildError(String message, ErrorType type, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError()
                .errorType(type)
                .message(message)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }
}