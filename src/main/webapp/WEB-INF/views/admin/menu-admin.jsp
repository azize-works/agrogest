<%-- menu-admin.jsp: barra de navegacao do administrador. Recebe o parametro "activo" para destacar a seccao actual. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<nav class="menu-admin">
    <a href="${ctx}/admin" class="${param.activo == 'painel' ? 'activo' : ''}">Painel</a>
    <a href="${ctx}/admin/fornecedores" class="${param.activo == 'fornecedores' ? 'activo' : ''}">Fornecedores</a>
    <a href="${ctx}/admin/produtos" class="${param.activo == 'produtos' ? 'activo' : ''}">Produtos</a>
    <a href="${ctx}/admin/armazem" class="${param.activo == 'armazem' ? 'activo' : ''}">Armazém</a>
    <a href="${ctx}/admin/campanhas" class="${param.activo == 'campanhas' ? 'activo' : ''}">Campanhas</a>
    <a href="${ctx}/admin/cultivos" class="${param.activo == 'cultivos' ? 'activo' : ''}">Cultivos</a>
    <a href="${ctx}/admin/colheitas" class="${param.activo == 'colheitas' ? 'activo' : ''}">Colheitas</a>
</nav>

