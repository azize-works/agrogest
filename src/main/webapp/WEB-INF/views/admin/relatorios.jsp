<%-- relatorios.jsp: relatorios de clientes em abas (por nome, compraram, assiduos, sem compras). --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Relatórios - AgroGest</title>
    <link rel="stylesheet" href="${ctx}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />
    <jsp:include page="/WEB-INF/views/admin/menu-admin.jsp">
        <jsp:param name="activo" value="relatorios" />
    </jsp:include>

    <main class="conteudo">
        <h2>Relatórios de clientes</h2>

        <%-- Abas (links) para escolher o relatorio --%>
        <nav class="abas abas-grandes">
            <a href="${ctx}/admin/relatorios?tipo=nome" class="${tipo == 'nome' ? 'activa' : ''}">Por nome</a>
            <a href="${ctx}/admin/relatorios?tipo=compraram" class="${tipo == 'compraram' ? 'activa' : ''}">Compraram (volume)</a>
            <a href="${ctx}/admin/relatorios?tipo=assiduos" class="${tipo == 'assiduos' ? 'activa' : ''}">Mais assíduos</a>
            <a href="${ctx}/admin/relatorios?tipo=semcompras" class="${tipo == 'semcompras' ? 'activa' : ''}">Sem compras</a>
        </nav>

        <%-- Tabela do relatorio escolhido (com posicao quando e ranking) --%>
        <section class="cartao">
            <h3><c:out value="${titulo}" /></h3>
            <div class="tabela-scroll">
                <table>
                    <tr>
                        <c:if test="${ranking}"><th>Posição</th></c:if>
                        <th>Nome</th>
                        <th>Email</th>
                        <th>Nº de compras</th>
                        <th>Total gasto</th>
                    </tr>
                    <c:forEach var="r" items="${linhas}" varStatus="estado">
                        <tr>
                            <c:if test="${ranking}"><td><strong>${estado.count}º</strong></td></c:if>
                            <td><c:out value="${r.cliente.nome}" /></td>
                            <td><c:out value="${r.cliente.email}" /></td>
                            <td class="numero-celula">${r.numeroCompras}</td>
                            <td class="numero-celula"><fmt:formatNumber value="${r.totalGasto}" minFractionDigits="2" maxFractionDigits="2" /> MT</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty linhas}">
                        <tr><td colspan="${ranking ? 5 : 4}" class="vazio">Não há clientes neste relatório.</td></tr>
                    </c:if>
                </table>
            </div>
        </section>
    </main>
</body>
</html>

