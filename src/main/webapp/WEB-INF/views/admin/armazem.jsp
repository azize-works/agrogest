<%-- armazem.jsp: produtos com stock disponivel (> 0), ordenaveis por nome, preco ou stock. --%>
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
    <title>Armazém - AgroGest</title>
    <link rel="stylesheet" href="${ctx}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />
    <jsp:include page="/WEB-INF/views/admin/menu-admin.jsp">
        <jsp:param name="activo" value="armazem" />
    </jsp:include>

    <main class="conteudo">
        <h2>Armazém</h2>
        <p class="subtitulo">Produtos com stock disponível. Para repor stock use a página
            <a href="${ctx}/admin/produtos">Produtos</a>.</p>

        <section class="cartao">
            <div class="tabela-scroll">
                <table class="tabela-ordenavel">
                    <tr>
                        <th class="${ordem == 'nome' ? 'ordenada' : ''}"><a href="${ctx}/admin/armazem?ordem=nome">Produto</a></th>
                        <th>Categoria</th>
                        <th>Fornecedor</th>
                        <th class="${ordem == 'preco' ? 'ordenada' : ''}"><a href="${ctx}/admin/armazem?ordem=preco">Preço</a></th>
                        <th class="${ordem == 'stock' ? 'ordenada' : ''}"><a href="${ctx}/admin/armazem?ordem=stock">Stock</a></th>
                        <th>Validade</th>
                    </tr>
                    <c:forEach var="p" items="${produtos}">
                        <tr>
                            <td><c:out value="${p.nome}" /></td>
                            <td><span class="etiqueta">${p.categoria}</span></td>
                            <td><c:out value="${nomesFornecedores[p.fornecedorId]}" default="—" /></td>
                            <td class="numero-celula"><fmt:formatNumber value="${p.preco}" minFractionDigits="2" maxFractionDigits="2" /> MT</td>
                            <td class="numero-celula"><fmt:formatNumber value="${p.quantidade}" maxFractionDigits="2" /> <c:out value="${p.unidade}" /></td>
                            <td>${p.validadeTexto}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty produtos}">
                        <tr><td colspan="6" class="vazio">O armazém está vazio.</td></tr>
                    </c:if>
                </table>
            </div>
        </section>
    </main>
</body>
</html>

