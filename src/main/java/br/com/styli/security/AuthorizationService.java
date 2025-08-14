package br.com.styli.security;

import br.com.styli.domain.exception.BadRequestException;
import br.com.styli.domain.exception.NotFoundException;
import br.com.styli.dto.request.FiltroAgendamentosRequest;
import br.com.styli.repository.AgendamentoRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationService {

    private final AgendamentoRepository agendamentoRepository;

    // ---- Cancelar: cliente só cancela o que é dele; funcionário só o dele; admin tudo
    public void ensurePodeCancelar(Long agendamentoId, HttpServletRequest req) {
        var a = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new NotFoundException("Agendamento não encontrado"));

        if (hasRole("ADMIN")) return;

        if (hasRole("CLIENTE")) {
            Long headerId = readLongHeader(req, "X-Cliente-Id");
            if (a.getCliente() == null || !a.getCliente().getId().equals(headerId)) {
                throw new AccessDeniedException("Cliente não pode cancelar agendamento de outro cliente");
            }
            return;
        }

        if (hasRole("FUNCIONARIO")) {
            if (a.getFuncionario() == null) {
                throw new AccessDeniedException("Agendamento sem funcionário não pode ser cancelado por funcionário");
            }
            Long headerId = readLongHeader(req, "X-Funcionario-Id");
            if (!a.getFuncionario().getId().equals(headerId)) {
                throw new AccessDeniedException("Funcionário não pode cancelar agendamento de outro funcionário");
            }
            return;
        }

        throw new AccessDeniedException("Perfil sem permissão");
    }

    // ---- Listar: força filtro pelo dono quando não é admin
    public void applyFilterByRole(FiltroAgendamentosRequest f, HttpServletRequest req) {
        if (hasRole("ADMIN")) return;

        if (hasRole("CLIENTE")) {
            Long headerId = readLongHeader(req, "X-Cliente-Id");
            if (f.getClienteId() == null) {
                f.setClienteId(headerId);
            } else if (!f.getClienteId().equals(headerId)) {
                throw new AccessDeniedException("Cliente só pode listar seus próprios agendamentos");
            }
            // bloqueia outros filtros conflitantes
            f.setFuncionarioId(null);
            f.setEmpresaId(null);
            return;
        }

        if (hasRole("FUNCIONARIO")) {
            Long headerId = readLongHeader(req, "X-Funcionario-Id");
            if (f.getFuncionarioId() == null) {
                f.setFuncionarioId(headerId);
            } else if (!f.getFuncionarioId().equals(headerId)) {
                throw new AccessDeniedException("Funcionário só pode listar seus próprios agendamentos");
            }
            f.setClienteId(null);
            // empresaId opcionalmente pode ficar, mas para evitar vazamento, zere:
            f.setEmpresaId(null);
            return;
        }

        throw new AccessDeniedException("Perfil sem permissão");
    }

    // ---------- helpers ----------
    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    private Long readLongHeader(HttpServletRequest req, String header) {
        String v = req.getHeader(header);
        if (v == null || v.isBlank()) {
            throw new BadRequestException("Header obrigatório: " + header);
        }
        try {
            return Long.parseLong(v);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Header " + header + " inválido (precisa ser um número)");
        }
    }
}
