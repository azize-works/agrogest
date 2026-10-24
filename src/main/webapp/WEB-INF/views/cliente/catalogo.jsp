<%-- catalogo.jsp: catalogo de produtos para o cliente, com filtro por categoria e adicao ao carrinho. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Catálogo - AgroGest</title>
    <link rel="stylesheet" href="${ctx}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />

    <main class="conteudo">
        <%-- Topo com titulo e filtro por categoria --%>
        <div class="cartao-topo">
            <h2>Catálogo de produtos</h2>
            <form method="get" action="${ctx}/catalogo" class="form-filtro">
                <select name="categoria" aria-label="Categoria">
                    <option value="">Todas as categorias</option>
                    <c:forEach var="cat" items="${categorias}">
                        <option value="${cat}" ${cat.name() == filtroCategoria ? 'selected' : ''}>${cat}</option>
                    </c:forEach>
                </select>
                <button type="submit" class="btn btn-sec btn-pequeno">Filtrar</button>
            </form>
        </div>

        <%-- Avisos de erro ou de sucesso ao adicionar ao carrinho --%>
        <c:if test="${not empty erro}">
            <p class="alerta alerta-erro"><c:out value="${erro}" /></p>
        </c:if>
        <c:if test="${not empty param.ok}">
            <p class="alerta alerta-ok"><c:out value="${param.ok}" /></p>
        </c:if>

        <%-- Grelha de cartoes de produtos --%>
        <div class="grelha">
            <c:forEach var="p" items="${produtos}">
                <article class="cartao cartao-produto">
                    <%-- Foto do produto ou espaco neutro com o nome --%>
                    <c:choose>
                        <c:when test="${not empty p.fotoUrl}">
                            <img class="produto-foto" src="<c:out value='${p.fotoUrl}' />" alt="<c:out value='${p.nome}' />">
                        </c:when>
                        <c:otherwise>
                            <div class="produto-sem-foto"><c:out value="${p.nome}" /></div>
                        </c:otherwise>
                    </c:choose>

                    <div class="produto-cabecalho">
                        <h3><c:out value="${p.nome}" /></h3>
                        <span class="etiqueta">${p.categoria}</span>
                    </div>

                    <p class="produto-meta">
                        <strong>Fornecedor:</strong> <c:out value="${nomesFornecedores[p.fornecedorId]}" default="—" /><br>
                        <strong>Validade:</strong> ${p.validadeTexto}
                    </p>

                    <c:if test="${not empty p.descricao}">
                        <p class="produto-desc"><c:out value="${p.descricao}" /></p>
                    </c:if>

                    <p class="produto-preco">
                        <fmt:formatNumber value="${p.preco}" minFractionDigits="2" maxFractionDigits="2" /> MT
                        <span class="produto-unidade">/ <c:out value="${p.unidade}" /></span>
                    </p>

                    <%-- Stock disponivel e formulario de adicionar ao carrinho (ou Esgotado se stock == 0) --%>
                    <c:choose>
                        <c:when test="${p.quantidade > 0}">
                            <p class="produto-stock">
                                Disponível: <fmt:formatNumber value="${p.quantidade}" maxFractionDigits="2" /> <c:out value="${p.unidade}" />
                            </p>
                            <form method="post" action="${ctx}/carrinho/adicionar" class="form-inline form-comprar">
                                <input type="hidden" name="produtoId" value="${p.id}">
                                <input type="hidden" name="categoria" value="<c:out value='${filtroCategoria}' />">
                                <input type="number" name="quantidade" step="any"
                                       value="<c:out value='${param.produtoId == p.id.toString() and not empty param.quantidade ? param.quantidade : 1}' />"
                                       aria-label="Quantidade">
                                <button type="submit" class="btn btn-pequeno">Adicionar ao carrinho</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <p class="esgotado">Esgotado</p>
                        </c:otherwise>
                    </c:choose>
                </article>
            </c:forEach>
        </div>

        <c:if test="${empty produtos}">
            <div class="cartao vazio">Não há produtos disponíveis nesta categoria.</div>
        </c:if>
    </main>
</body>
</html>
