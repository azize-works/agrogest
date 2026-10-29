<%-- carrinho.jsp: carrinho de compras do cliente (tabela de itens, subtotais, IVA 16%, total e accoes). --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Carrinho - AgroGest</title>
    <link rel="stylesheet" href="${ctx}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />

    <main class="conteudo">
        <h2>Carrinho de compras</h2>

        <%-- Avisos de erro de validacao/stock ou de sucesso --%>
        <c:if test="${not empty erro}">
            <p class="alerta alerta-erro"><c:out value="${erro}" /></p>
        </c:if>
        <c:if test="${not empty param.ok}">
            <p class="alerta alerta-ok"><c:out value="${param.ok}" /></p>
        </c:if>

        <c:choose>
            <%-- Carrinho vazio --%>
            <c:when test="${empty linhas}">
                <section class="cartao vazio">
                    <p>O seu carrinho está vazio.</p>
                    <a class="btn" href="${ctx}/catalogo">Ir para o catálogo</a>
                </section>
            </c:when>

            <%-- Tabela com as linhas do carrinho e resumo de valores --%>
            <c:otherwise>
                <section class="cartao">
                    <div class="tabela-scroll">
                        <table>
                            <tr>
                                <th>Produto</th>
                                <th>Preço unit.</th>
                                <th>Quantidade</th>
                                <th>Subtotal</th>
                                <th></th>
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
                                        <fmt:formatNumber value="${item.precoUnit}" minFractionDigits="2" maxFractionDigits="2" /> MT
                                    </td>
                                    <td>
                                        <form method="post" action="${ctx}/carrinho/actualizar" class="form-inline">
                                            <input type="hidden" name="produtoId" value="${item.produtoId}">
                                            <input type="number" name="quantidade" step="any"
                                                   value="<fmt:formatNumber value='${item.quantidade}' groupingUsed='false' maxFractionDigits='2' />"
                                                   aria-label="Quantidade">
                                            <span><c:out value="${unidades[item.produtoId]}" /></span>
                                            <button type="submit" class="btn btn-sec btn-pequeno">Actualizar</button>
                                        </form>
                                    </td>
                                    <td class="numero-celula">
                                        <fmt:formatNumber value="${item.subtotal}" minFractionDigits="2" maxFractionDigits="2" /> MT
                                    </td>
                                    <td>
                                        <form method="post" action="${ctx}/carrinho/remover">
                                            <input type="hidden" name="produtoId" value="${item.produtoId}">
                                            <button type="submit" class="btn btn-perigo btn-pequeno">Remover</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>

                    <%-- Resumo financeiro: subtotal, IVA (16%) e total --%>
                    <div class="resumo-totais">
                        <p>Subtotal: <strong><fmt:formatNumber value="${subtotal}" minFractionDigits="2" maxFractionDigits="2" /> MT</strong></p>
                        <p>IVA (16%): <strong><fmt:formatNumber value="${iva}" minFractionDigits="2" maxFractionDigits="2" /> MT</strong></p>
                        <p class="total-destaque">Total: <strong><fmt:formatNumber value="${total}" minFractionDigits="2" maxFractionDigits="2" /> MT</strong></p>
                    </div>

                    <%-- Botoes de accao do carrinho --%>
                    <div class="carrinho-accoes">
                        <form method="post" action="${ctx}/carrinho/esvaziar"
                              onsubmit="return confirm('Esvaziar todo o carrinho?');">
                            <button type="submit" class="btn btn-perigo">Esvaziar carrinho</button>
                        </form>
                        <div class="accoes-direita">
                            <a class="btn btn-sec" href="${ctx}/catalogo">Continuar a comprar</a>
                            <a class="btn" href="${ctx}/compra">Finalizar compra</a>
                        </div>
                    </div>
                </section>
            </c:otherwise>
        </c:choose>
    </main>
</body>
</html>

