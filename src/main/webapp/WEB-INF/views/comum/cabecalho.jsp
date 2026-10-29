<%-- cabecalho.jsp: barra superior comum a todas as paginas. Muda conforme o utilizador tem sessao ou nao. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<header class="topo">
    <a class="marca" href="${ctx}/">
        <svg class="icone-marca" viewBox="0 0 24 24" aria-hidden="true">
            <path d="M17 8C8 10 5.9 16.17 3.82 21.34l1.89.66.95-2.3c.48.17.98.3 1.34.3C19 20 22 3 22 3c-1 2-8 2.25-13 3.25S2 11.5 2 13.5s1.75 3.75 1.75 3.75C7 8 17 8 17 8z"/>
        </svg>
        <span>AgroGest</span>
    </a>
    <nav>
        <c:choose>
            <%-- Sem sessao: Entrar e Registar --%>
            <c:when test="${empty sessionScope.utilizador}">
                <a href="${ctx}/login">Entrar</a>
                <a class="btn-topo" href="${ctx}/registo">Registar</a>
            </c:when>
            <%-- Com sessao: menu do perfil + nome + Sair --%>
            <c:otherwise>
                <c:if test="${sessionScope.utilizador.perfil == 'ADMIN'}">
                    <a href="${ctx}/admin">Painel</a>
                </c:if>
                <c:if test="${sessionScope.utilizador.perfil == 'CLIENTE'}">
                    <a href="${ctx}/catalogo">Catálogo</a>
                    <a href="${ctx}/carrinho">Carrinho<c:if test="${not empty sessionScope.carrinho and sessionScope.carrinho.totalLinhas > 0}"> (${sessionScope.carrinho.totalLinhas})</c:if></a>
                    <a href="${ctx}/minhas-compras">Minhas compras</a>
                </c:if>
                <span class="utilizador"><c:out value="${sessionScope.utilizador.nome}" /></span>
                <a class="btn-sair" href="${ctx}/logout">Sair</a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>
