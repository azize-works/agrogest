<%-- compras.jsp: todas as compras em detalhe (um bloco por compra: factura, cliente, itens, subtotal, IVA e total). --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ordem" value="${empty param.ordem ? 'data' : param.ordem}" />
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Compras - AgroGest</title>
    <link rel="stylesheet" href="${ctx}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />
    <jsp:include page="/WEB-INF/views/admin/menu-admin.jsp">
        <jsp:param name="activo" value="compras" />
    </jsp:include>

    <main class="conteudo">
        <%-- Titulo e escolha da ordenacao (abas feitas com links) --%>
        <div class="cartao-topo">
            <h2>Compras dos clientes</h2>
            <nav class="abas">
                <span>Ordenar por:</span>
                <a href="${ctx}/admin/compras?ordem=data" class="${ordem == 'data' ? 'activa' : ''}">Data</a>
                <a href="${ctx}/admin/compras?ordem=valor" class="${ordem == 'valor' ? 'activa' : ''}">Valor</a>
                <a href="${ctx}/admin/compras?ordem=cliente" class="${ordem == 'cliente' ? 'activa' : ''}">Cliente</a>
            </nav>
        </div>

        <c:if test="${empty vendas}">
            <div class="cartao vazio">Ainda não há compras registadas.</div>
        </c:if>

        <%-- Um bloco por compra --%>
        <c:forEach var="v" items="${vendas}">
            <c:set var="cli" value="${clientes[v.clienteId]}" />
            <section class="cartao bloco-compra">
                <%-- Cabecalho da compra: factura, data e link --%>
                <div class="bloco-compra-topo">
                    <div>
                        <strong class="factura-numero"><c:out value="${v.numeroFactura}" /></strong>
                        <span class="subtitulo">${v.dataTexto}</span>
                    </div>
                    <a class="btn btn-sec btn-pequeno" href="${ctx}/factura?id=${v.id}">Ver factura</a>
                </div>

                <%-- Dados do cliente --%>
                <p class="bloco-compra-cliente">
                    <strong>Cliente:</strong> <c:out value="${cli.nome}" default="—" /> &middot;
                    <strong>Endereço:</strong> <c:out value="${cli.endereco}" default="—" /> &middot;
                    <strong>Telefone:</strong> <c:out value="${cli.telefone}" default="—" />
                </p>

                <%-- Itens da compra --%>
                <div class="tabela-scroll">
                    <table>
                        <tr>
                            <th>Produto</th>
                            <th>Quantidade</th>
                            <th>Valor unitário</th>
                            <th>Subtotal</th>
                        </tr>
                        <c:forEach var="item" items="${v.itens}">
                            <tr>
                                <td><c:out value="${item.nomeProduto}" /></td>
                                <td class="numero-celula"><fmt:formatNumber value="${item.quantidade}" maxFractionDigits="2" /></td>
                                <td class="numero-celula"><fmt:formatNumber value="${item.precoUnit}" minFractionDigits="2" maxFractionDigits="2" /> MT</td>
                                <td class="numero-celula"><fmt:formatNumber value="${item.subtotal}" minFractionDigits="2" maxFractionDigits="2" /> MT</td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>

                <%-- Totais da compra --%>
                <div class="resumo-totais">
                    <p>Subtotal: <strong><fmt:formatNumber value="${v.subtotal}" minFractionDigits="2" maxFractionDigits="2" /> MT</strong></p>
                    <p>IVA (16%): <strong><fmt:formatNumber value="${v.iva}" minFractionDigits="2" maxFractionDigits="2" /> MT</strong></p>
                    <p class="total-destaque">Total: <strong><fmt:formatNumber value="${v.total}" minFractionDigits="2" maxFractionDigits="2" /> MT</strong></p>
                </div>
            </section>
        </c:forEach>
    </main>
</body>
</html>

