<%-- icone-categoria.jsp: mostra o icone SVG correspondente a categoria do produto (ou generico se desconhecida/nula). --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%-- Mapeia apenas os valores fixos do enum CategoriaProduto para ficheiros SVG locais --%>
<c:choose>
    <c:when test="${param.categoria == 'SEMENTE'}">
        <c:set var="ficheiroCat" value="semente.svg" />
        <c:set var="classeCat" value="cat-semente" />
        <c:set var="textoCat" value="Semente" />
    </c:when>
    <c:when test="${param.categoria == 'FERTILIZANTE'}">
        <c:set var="ficheiroCat" value="fertilizante.svg" />
        <c:set var="classeCat" value="cat-fertilizante" />
        <c:set var="textoCat" value="Fertilizante" />
    </c:when>
    <c:when test="${param.categoria == 'MAQUINARIA'}">
        <c:set var="ficheiroCat" value="maquinaria.svg" />
        <c:set var="classeCat" value="cat-maquinaria" />
        <c:set var="textoCat" value="Maquinaria" />
    </c:when>
    <c:when test="${param.categoria == 'FERRAMENTA'}">
        <c:set var="ficheiroCat" value="ferramenta.svg" />
        <c:set var="classeCat" value="cat-ferramenta" />
        <c:set var="textoCat" value="Ferramenta" />
    </c:when>
    <c:otherwise>
        <c:set var="ficheiroCat" value="generico.svg" />
        <c:set var="classeCat" value="cat-generico" />
        <c:set var="textoCat" value="Produto" />
    </c:otherwise>
</c:choose>
<span class="bloco-icone-cat ${classeCat} ${param.tamanho == 'grande' ? 'bloco-icone-grande' : 'bloco-icone-pequeno'}" aria-hidden="true">
    <img class="icone-cat ${param.tamanho == 'grande' ? 'icone-cat-grande' : 'icone-cat-pequeno'}"
         src="${pageContext.request.contextPath}/assets/img/categorias/${ficheiroCat}"
         width="${param.tamanho == 'grande' ? '40' : '20'}"
         height="${param.tamanho == 'grande' ? '40' : '20'}"
         alt="<c:out value='${textoCat}' />">
</span>

