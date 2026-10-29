<%-- dashboard.jsp: painel do administrador com os totais do sistema (valores vindos do AdminDashboardServlet). --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Painel - AgroGest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />
    <jsp:include page="/WEB-INF/views/admin/menu-admin.jsp">
        <jsp:param name="activo" value="painel" />
    </jsp:include>

    <main class="conteudo">
        <h2>Painel do administrador</h2>
        <%-- Cada cartao e um link para a respectiva pagina, com icone SVG e numero em destaque --%>
        <div class="grelha">
            <a class="cartao estatistica cartao-link" href="${pageContext.request.contextPath}/admin/fornecedores">
                <span class="icone-estatistica" aria-hidden="true">
                    <svg viewBox="0 0 24 24"><path d="M20 8h-3V4H3c-1.1 0-2 .9-2 2v11h2c0 1.66 1.34 3 3 3s3-1.34 3-3h6c0 1.66 1.34 3 3 3s3-1.34 3-3h2v-5l-3-4z"/></svg>
                </span>
                <span class="numero">${totalFornecedores}</span>
                <span>Fornecedores</span>
            </a>
            <a class="cartao estatistica cartao-link" href="${pageContext.request.contextPath}/admin/produtos">
                <span class="icone-estatistica" aria-hidden="true">
                    <svg viewBox="0 0 24 24"><path d="M21 16.5c0 .38-.21.71-.53.88l-7.9 4.44c-.16.12-.36.18-.57.18-.21 0-.41-.06-.57-.18l-7.9-4.44A.991.991 0 0 1 3 16.5v-9c0-.38.21-.71.53-.88l7.9-4.44c.16-.12.36-.18.57-.18.21 0 .41.06.57.18l7.9 4.44c.32.17.53.5.53.88v9z"/></svg>
                </span>
                <span class="numero">${totalProdutos}</span>
                <span>Produtos</span>
            </a>
            <a class="cartao estatistica cartao-link" href="${pageContext.request.contextPath}/admin/clientes">
                <span class="icone-estatistica" aria-hidden="true">
                    <svg viewBox="0 0 24 24"><path d="M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z"/></svg>
                </span>
                <span class="numero">${totalClientes}</span>
                <span>Clientes</span>
            </a>
            <a class="cartao estatistica cartao-link" href="${pageContext.request.contextPath}/admin/campanhas">
                <span class="icone-estatistica" aria-hidden="true">
                    <svg viewBox="0 0 24 24"><path d="M19 4h-1V2h-2v2H8V2H6v2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 16H5V10h14v10z"/></svg>
                </span>
                <span class="numero">${totalCampanhas}</span>
                <span>Campanhas</span>
            </a>
            <a class="cartao estatistica cartao-link" href="${pageContext.request.contextPath}/admin/compras">
                <span class="icone-estatistica" aria-hidden="true">
                    <svg viewBox="0 0 24 24"><path d="M18 17H6v-2h12v2zm0-4H6v-2h12v2zm0-4H6V7h12v2zM3 22l1.5-1.5L6 22l1.5-1.5L9 22l1.5-1.5L12 22l1.5-1.5L15 22l1.5-1.5L18 22l1.5-1.5L21 22V2l-1.5 1.5L18 2l-1.5 1.5L15 2l-1.5 1.5L12 2l-1.5 1.5L9 2 7.5 3.5 6 2 4.5 3.5 3 2v20z"/></svg>
                </span>
                <span class="numero">${totalVendas}</span>
                <span>Vendas</span>
            </a>
        </div>
    </main>
</body>
</html>
