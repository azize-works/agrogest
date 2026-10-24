package mz.isctem.agrogest.model;

/** Perfil de um utilizador: define o que ele pode fazer no sistema. */
public enum Perfil {
    ADMIN,   // gere catalogo, campanhas, clientes e vendas
    CLIENTE  // consulta catalogo, compra e ve as suas facturas
}
