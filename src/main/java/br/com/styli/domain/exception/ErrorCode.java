package br.com.styli.domain.exception;

public enum ErrorCode {
    CLIENTE_NOT_FOUND("Cliente nao encontrado"),
    OS_NOT_FOUND("O.S nao encontrado"),
    PAGAMENTO_NOT_FOUND("Pagamento nao encontrado"),
    ADICIONAL_OS_NOT_FOUND("ADICIONAL NAO ENCONTRADO PARA ESSA OS"),
    ITEM_OS_NOT_FOUND("ITEM NAO ENCONTRADO PARA ESSA OS"),
    GENERIC("Erro genérico, contate o administrador"),
    ITEM_NOT_DELETED("Item nao removido, contate o administrador");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}