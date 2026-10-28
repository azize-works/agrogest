<%-- clientes.jsp: lista de clientes com nº de compras, total gasto e botao para activar/desactivar a conta. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ordem" value="${empty param.ordem ? 'nome' : param.ordem}" />
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Clientes - AgroGest</title>
    <link rel="stylesheet" href="${ctx}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />
    <jsp:include page="/WEB-INF/views/admin/menu-admin.jsp">
        <jsp:param name="activo" value="clientes" />
    </jsp:include>

    <main class="conteudo">
        <h2>Clientes</h2>

        <%-- Avisos: erro ou sucesso depois do redirect --%>
        <c:if test="${not empty erro}">
            <p class="alerta alerta-erro"><c:out value="${erro}" /></p>
        </c:if>
        <c:if test="${not empty param.ok}">
            <p class="alerta alerta-ok"><c:out value="${param.ok}" /></p>
        </c:if>

        <%-- Tabela de clientes: ordenavel por nome, email ou nº de compras --%>
        <section class="cartao">
            <div class="tabela-scroll">
                <table class="tabela-ordenavel">
                    <tr>
                        <th>ID</th>
                        <th class="${ordem == 'nome' ? 'ordenada' : ''}"><a href="${ctx}/admin/clientes?ordem=nome">Nome</a></th>
                        <th class="${ordem == 'email' ? 'ordenada' : ''}"><a href="${ctx}/admin/clientes?ordem=email">Email</a></th>
                        <th>Telefone</th>
                        <th>Endereço</th>
                        <th class="${ordem == 'compras' ? 'ordenada' : ''}"><a href="${ctx}/admin/clientes?ordem=compras">Compras</a></th>
                        <th>Total gasto</th>
                        <th>Estado</th>
                        <th></th>
                    </tr>
                    <c:forEach var="r" items="${clientes}">
                        <tr>
                            <td>${r.cliente.id}</td>
                            <td><c:out value="${r.cliente.nome}" /></td>
                            <td><c:out value="${r.cliente.email}" /></td>
                            <td><c:out value="${r.cliente.telefone}" /></td>
                            <td><c:out value="${r.cliente.endereco}" /></td>
                            <td class="numero-celula">${r.numeroCompras}</td>
                            <td class="numero-celula"><fmt:formatNumber value="${r.totalGasto}" minFractionDigits="2" maxFractionDigits="2" /> MT</td>
                            <td>
                                <span class="selo ${r.cliente.ativo ? 'selo-aberta' : 'selo-fechada'}">${r.cliente.ativo ? 'Activo' : 'Inactivo'}</span>
                            </td>
                            <td>
                                <form method="post" action="${ctx}/admin/clientes"
                                      onsubmit="return confirm('${r.cliente.ativo ? 'Desactivar' : 'Activar'} este cliente?');">
                                    <input type="hidden" name="accao" value="alternar">
                                    <input type="hidden" name="id" value="${r.cliente.id}">
                                    <button type="submit" class="btn btn-pequeno ${r.cliente.ativo ? 'btn-perigo' : ''}">${r.cliente.ativo ? 'Desactivar' : 'Activar'}</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty clientes}">
                        <tr><td colspan="9" class="vazio">Ainda não há clientes registados.</td></tr>
                    </c:if>
                </table>
            </div>
        </section>
    </main>
</body>
</html>

