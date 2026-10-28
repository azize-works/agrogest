package mz.isctem.agrogest.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import mz.isctem.agrogest.model.ResumoCliente;

/**
 * Relatorios de clientes para o administrador: por nome, por volume de compras,
 * por assiduidade (numero de compras) e clientes sem compras.
 */
public class RelatorioService {

    // Ordem alfabetica pelo nome do cliente (sem distinguir maiusculas)
    private static final Comparator<ResumoCliente> POR_NOME =
            Comparator.comparing(r -> r.getCliente().getNome(), String.CASE_INSENSITIVE_ORDER);

    /** Todos os clientes, por nome crescente. */
    public static List<ResumoCliente> clientesPorNome() {
        List<ResumoCliente> lista = ClienteService.resumos();
        lista.sort(POR_NOME);
        return lista;
    }

    /** So os clientes que ja compraram, por volume (total gasto) decrescente. */
    public static List<ResumoCliente> clientesQueCompraram() {
        List<ResumoCliente> lista = filtrar(true);
        lista.sort(Comparator.comparingDouble(ResumoCliente::getTotalGasto).reversed().thenComparing(POR_NOME));
        return lista;
    }

    /** Ranking por numero de compras (decrescente); desempate pelo total gasto e depois pelo nome. */
    public static List<ResumoCliente> clientesMaisAssiduos() {
        List<ResumoCliente> lista = filtrar(true);
        lista.sort(Comparator.comparingInt(ResumoCliente::getNumeroCompras).reversed()
                .thenComparing(Comparator.comparingDouble(ResumoCliente::getTotalGasto).reversed())
                .thenComparing(POR_NOME));
        return lista;
    }

    /** Clientes sem nenhuma compra, por nome. */
    public static List<ResumoCliente> clientesSemCompras() {
        List<ResumoCliente> lista = filtrar(false);
        lista.sort(POR_NOME);
        return lista;
    }

    /** Devolve os resumos dos clientes que compraram (comCompras = true) ou que nunca compraram (false). */
    private static List<ResumoCliente> filtrar(boolean comCompras) {
        List<ResumoCliente> resultado = new ArrayList<>();
        for (ResumoCliente r : ClienteService.resumos()) {
            if ((r.getNumeroCompras() > 0) == comCompras) {
                resultado.add(r);
            }
        }
        return resultado;
    }
}

