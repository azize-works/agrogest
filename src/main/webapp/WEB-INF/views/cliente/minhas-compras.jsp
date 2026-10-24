<%-- minhas-compras.jsp: historico de compras do cliente autenticado, da mais recente para a mais antiga. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Minhas compras - AgroGest</title>
    <link rel="stylesheet" href="${ctx}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />

    <main class="conteudo">
        <h2>Minhas compras</h2>

        <c:choose>
            <c:when test="${empty vendas}">
                <section class="cartao vazio">
                    <p>Ainda não realizou nenhuma compra.</p>
                    <a class="btn" href="${ctx}/catalogo">Explorar catálogo</a>
                </section>
            </c:when>
            <c:otherwise>
                <section class="cartao">
                    <div class="tabela-scroll">
                        <table>
                            <tr>
                                <th>Nº da factura</th>
                                <th>Data</th>
                                <th>Nº de itens</th>
                                <th>Total</th>
                                <th></th>
                            </tr>
                            <c:forEach var="v" items="${vendas}">
                                <tr>
                                    <td><strong><c:out value="${v.numeroFactura}" /></strong></td>
                                    <td>${v.dataTexto}</td>
                                    <td>${v.itens.size()}</td>
                                    <td class="numero-celula">
                                        <fmt:formatNumber value="${v.total}" minFractionDigits="2" maxFractionDigits="2" /> MT
                                    </td>
                                    <td>
                                        <a class="btn btn-pequeno" href="${ctx}/factura?id=${v.id}">Ver factura</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </section>
            </c:otherwise>
        </c:choose>
    </main>
</body>
</html>

