<%-- factura.jsp: documento de factura com numero, data/hora, cliente, itens, subtotal, IVA (16%) e total. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Factura <c:out value="${venda.numeroFactura}" /> - AgroGest</title>
    <link rel="stylesheet" href="${ctx}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />

    <main class="conteudo">
        <%-- Documento da factura --%>
        <section class="cartao factura">
            <div class="factura-cabecalho">
                <div>
                    <h2>AgroGest</h2>
                    <p class="subtitulo">Factura de venda</p>
                </div>
                <div class="factura-meta">
                    <p><strong>Factura nº:</strong> <c:out value="${venda.numeroFactura}" /></p>
                    <p><strong>Data:</strong> ${venda.dataTexto}</p>
                </div>
            </div>

            <%-- Dados do cliente da venda --%>
            <div class="factura-cliente">
                <h3>Cliente</h3>
                <p>
                    <strong>Nome:</strong> <c:out value="${cliente.nome}" default="—" /><br>
                    <strong>Email:</strong> <c:out value="${cliente.email}" default="—" /><br>
                    <strong>Endereço:</strong> <c:out value="${cliente.endereco}" default="—" /><br>
                    <strong>Telefone:</strong> <c:out value="${cliente.telefone}" default="—" />
                </p>
            </div>

            <%-- Tabela de itens da factura --%>
            <div class="tabela-scroll">
                <table>
                    <tr>
                        <th>Produto</th>
                        <th>Quantidade</th>
                        <th>Preço unit.</th>
                        <th>Subtotal</th>
                    </tr>
                    <c:forEach var="item" items="${venda.itens}">
                        <tr>
                            <td><c:out value="${item.nomeProduto}" /></td>
                            <td class="numero-celula"><fmt:formatNumber value="${item.quantidade}" maxFractionDigits="2" /></td>
                            <td class="numero-celula"><fmt:formatNumber value="${item.precoUnit}" minFractionDigits="2" maxFractionDigits="2" /> MT</td>
                            <td class="numero-celula"><fmt:formatNumber value="${item.subtotal}" minFractionDigits="2" maxFractionDigits="2" /> MT</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>

            <%-- Totais da factura: subtotal, IVA (16%) e total --%>
            <div class="resumo-totais">
                <p>Subtotal: <strong><fmt:formatNumber value="${venda.subtotal}" minFractionDigits="2" maxFractionDigits="2" /> MT</strong></p>
                <p>IVA (16%): <strong><fmt:formatNumber value="${venda.iva}" minFractionDigits="2" maxFractionDigits="2" /> MT</strong></p>
                <p class="total-destaque">Total: <strong><fmt:formatNumber value="${venda.total}" minFractionDigits="2" maxFractionDigits="2" /> MT</strong></p>
            </div>

            <%-- Botoes (escondidos ao imprimir) --%>
            <div class="carrinho-accoes nao-imprimir">
                <c:choose>
                    <c:when test="${sessionScope.utilizador.perfil == 'ADMIN'}">
                        <a class="btn btn-sec" href="${ctx}/admin">Voltar ao painel</a>
                    </c:when>
                    <c:otherwise>
                        <a class="btn btn-sec" href="${ctx}/minhas-compras">Minhas compras</a>
                    </c:otherwise>
                </c:choose>
                <button type="button" class="btn" onclick="window.print()">Imprimir</button>
            </div>
        </section>
    </main>
</body>
</html>

