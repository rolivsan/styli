package br.com.styli.domain.exception;

public enum ErrorCode {
    CLIENTE_NOT_FOUND("Cliente nao encontrado"),
    SERVICO_NOT_FOUND("Servico nao encontrado"),
    FUNCIONARIO_NOT_FOUND("Cliente nao encontrado"),
    EMPRESA_NOT_FOUND("Empresa nao encontrada"),
    GENERIC("Erro genérico, contate o administrador"),
    HORARIO_UNAVAILABLE("Horario indisponivel, Nenhum funcionario disponível nesse horario");


    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}