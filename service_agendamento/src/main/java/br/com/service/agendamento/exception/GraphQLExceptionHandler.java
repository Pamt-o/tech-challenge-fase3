package br.com.service.agendamento.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        log.warn("⚠️ Exceção capturada no GraphQL: {} — {}", ex.getClass().getSimpleName(), ex.getMessage());

        //Erros de negócio (ex: "Username já em uso")
        if (ex instanceof BusinessException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.BAD_REQUEST)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }

        //Recurso não encontrado (ex: "Consulta não encontrada")
        if (ex instanceof ResourceNotFoundException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.NOT_FOUND)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }

        //Erros de acesso negado (Spring Security)
        if (ex instanceof org.springframework.security.access.AccessDeniedException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.FORBIDDEN)
                    .message("Acesso negado. Você não tem permissão para executar esta ação.")
                    .path(env.getExecutionStepInfo().getPath())
                    .build();
        }

        //Fallback genérico — não expõe detalhes internos
        log.error("❌ Erro interno não tratado no GraphQL", ex);
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.INTERNAL_ERROR)
                .message("Erro interno no servidor. Contate o administrador.")
                .path(env.getExecutionStepInfo().getPath())
                .build();
    }
}
