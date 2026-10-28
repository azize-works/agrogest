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
        <%-- Cada cartao e um link para a respectiva pagina --%>
        <div class="grelha">
            <a class="cartao estatistica cartao-link" href="${pageContext.request.contextPath}/admin/fornecedores"><span class="numero">${totalFornecedores}</span><span>Fornecedores</span></a>
            <a class="cartao estatistica cartao-link" href="${pageContext.request.contextPath}/admin/produtos"><span class="numero">${totalProdutos}</span><span>Produtos</span></a>
            <a class="cartao estatistica cartao-link" href="${pageContext.request.contextPath}/admin/clientes"><span class="numero">${totalClientes}</span><span>Clientes</span></a>
            <a class="cartao estatistica cartao-link" href="${pageContext.request.contextPath}/admin/campanhas"><span class="numero">${totalCampanhas}</span><span>Campanhas</span></a>
            <a class="cartao estatistica cartao-link" href="${pageContext.request.contextPath}/admin/compras"><span class="numero">${totalVendas}</span><span>Vendas</span></a>
        </div>
    </main>
</body>
</html>
