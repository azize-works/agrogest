<%-- cabecalho.jsp: barra superior comum a todas as paginas. Muda conforme o utilizador tem sessao ou nao. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<header class="topo">
    <a class="marca" href="${ctx}/">AgroGest</a>
    <nav>
        <c:choose>
            <%-- Sem sessao: Entrar e Registar --%>
            <c:when test="${empty sessionScope.utilizador}">
                <a href="${ctx}/login">Entrar</a>
                <a href="${ctx}/registo">Registar</a>
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
