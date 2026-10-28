package mz.isctem.agrogest.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mz.isctem.agrogest.model.Perfil;
import mz.isctem.agrogest.model.ResumoCliente;
import mz.isctem.agrogest.model.Utilizador;
import mz.isctem.agrogest.model.Venda;
import mz.isctem.agrogest.repo.DataStore;

/**
 * Regras de negocio dos clientes vistas pelo administrador: listar (com compras e total gasto)
 * e activar/desactivar contas. Os Servlets so chamam este servico.
 */
public class ClienteService {

    /** Devolve os clientes (so perfil CLIENTE) com o seu resumo, ordenados por "nome" (omissao), "email" ou "compras". */
    public static List<ResumoCliente> listar(String ordem) {
        List<ResumoCliente> lista = resumos();
        if ("email".equals(ordem)) {
            lista.sort(Comparator.comparing((ResumoCliente r) -> r.getCliente().getEmail(), String.CASE_INSENSITIVE_ORDER));
        } else if ("compras".equals(ordem)) {
            lista.sort(Comparator.comparingInt(ResumoCliente::getNumeroCompras).reversed()
                    .thenComparing(r -> r.getCliente().getNome(), String.CASE_INSENSITIVE_ORDER));
        } else {
            lista.sort(Comparator.comparing((ResumoCliente r) -> r.getCliente().getNome(), String.CASE_INSENSITIVE_ORDER));
        }
        return lista;
    }

    /** Conta quantos utilizadores tem o perfil CLIENTE (para o painel). */
    public static int contar() {
        return copiaDosClientes().size();
    }

    /** Activa um cliente inactivo ou desactiva um cliente activo. Erro se o id nao for de um cliente. */
    public static synchronized void alternarActivo(int id) {
        List<Utilizador> utilizadores = DataStore.getInstance().getUtilizadores();
        synchronized (utilizadores) {
            for (Utilizador u : utilizadores) {
                if (u.getId() == id && u.getPerfil() == Perfil.CLIENTE) {
                    u.setAtivo(!u.isAtivo());
                    return;
                }
            }
        }
        throw new IllegalArgumentException("O cliente não existe.");
    }

    /** Calcula o resumo (numero de compras e total gasto) de todos os clientes, sem ordem definida. */
    public static List<ResumoCliente> resumos() {
        // 1. Somar compras e totais por cliente, percorrendo uma copia das vendas
        Map<Integer, Integer> compras = new HashMap<>();
        Map<Integer, Double> totais = new HashMap<>();
        List<Venda> vendas = DataStore.getInstance().getVendas();
        synchronized (vendas) {
            for (Venda v : vendas) {
                compras.merge(v.getClienteId(), 1, Integer::sum);
                totais.merge(v.getClienteId(), v.getTotal(), Double::sum);
            }
        }

        // 2. Criar um resumo para cada cliente (0 compras se nunca comprou)
        List<ResumoCliente> resultado = new ArrayList<>();
        for (Utilizador u : copiaDosClientes()) {
            resultado.add(new ResumoCliente(u,
                    compras.getOrDefault(u.getId(), 0),
                    totais.getOrDefault(u.getId(), 0.0)));
        }
        return resultado;
    }

    /** Copia segura so dos utilizadores com perfil CLIENTE. */
    private static List<Utilizador> copiaDosClientes() {
        List<Utilizador> lista = DataStore.getInstance().getUtilizadores();
        List<Utilizador> clientes = new ArrayList<>();
        synchronized (lista) {
            for (Utilizador u : lista) {
                if (u.getPerfil() == Perfil.CLIENTE) {
                    clientes.add(u);
                }
            }
        }
        return clientes;
    }
}

