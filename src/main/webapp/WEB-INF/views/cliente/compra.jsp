<%-- compra.jsp: resumo da compra antes de confirmar (dados do cliente, itens, subtotal, IVA 16% e total). --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="cli" value="${sessionScope.utilizador}" />
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Confirmar compra - AgroGest</title>
    <link rel="stylesheet" href="${ctx}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />

    <main class="conteudo">
        <h2>Resumo da compra</h2>

        <%-- Dados do cliente --%>
        <section class="cartao">
            <h3>Dados do cliente</h3>
            <p>
                <strong>Nome:</strong> <c:out value="${cli.nome}" /><br>
                <strong>Endereço:</strong> <c:out value="${cli.endereco}" /><br>
                <strong>Telefone:</strong> <c:out value="${cli.telefone}" />
            </p>
        </section>

        <%-- Itens e valores a confirmar --%>
        <section class="cartao">
            <h3>Itens da encomenda</h3>
            <div class="tabela-scroll">
                <table>
                    <tr>
                        <th>Produto</th>
                        <th>Quantidade</th>
                        <th>Preço unit.</th>
                        <th>Subtotal</th>
                    </tr>
                    <c:forEach var="item" items="${linhas}">
                        <tr>
                            <td>
                                <span class="nome-com-icone">
                                    <jsp:include page="/WEB-INF/views/comum/icone-categoria.jsp" />
                                    <span><c:out value="${item.nomeProduto}" /></span>
                                </span>
                            </td>
                            <td class="numero-celula">
                                <fmt:formatNumber value="${item.quantidade}" maxFractionDigits="2" />
                                <c:out value="${unidades[item.produtoId]}" />
                            </td>
                            <td class="numero-celula">
                                <fmt:formatNumber value="${item.precoUnit}" minFractionDigits="2" maxFractionDigits="2" /> MT
                            </td>
                            <td class="numero-celula">
                                <fmt:formatNumber value="${item.subtotal}" minFractionDigits="2" maxFractionDigits="2" /> MT
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>

            <div class="resumo-totais">
                <p>Subtotal: <strong><fmt:formatNumber value="${subtotal}" minFractionDigits="2" maxFractionDigits="2" /> MT</strong></p>
                <p>IVA (16%): <strong><fmt:formatNumber value="${iva}" minFractionDigits="2" maxFractionDigits="2" /> MT</strong></p>
                <p class="total-destaque">Total a pagar: <strong><fmt:formatNumber value="${total}" minFractionDigits="2" maxFractionDigits="2" /> MT</strong></p>
            </div>

            <div class="carrinho-accoes">
                <a class="btn btn-sec" href="${ctx}/carrinho">Voltar ao carrinho</a>
                <form method="post" action="${ctx}/compra">
                    <button type="submit" class="btn">Confirmar compra</button>
                </form>
            </div>
        </section>
    </main>
</body>
</html>

