<%-- index.jsp: pagina inicial publica do AgroGest. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>AgroGest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />

    <section class="heroi">
        <h1>Gestão Agrícola, simples e organizada</h1>
        <p>Fornecedores, cultivos, colheitas e vendas num só lugar.</p>
    </section>

    <main class="conteudo">
        <div class="grelha">
            <div class="cartao"><h3>Catálogo</h3><p>Sementes, fertilizantes, maquinaria e ferramentas.</p></div>
            <div class="cartao"><h3>Campanhas</h3><p>Controlo dos cultivos e colheitas de cada campanha.</p></div>
            <div class="cartao"><h3>Facturas</h3><p>Compras com factura detalhada e IVA de 16%.</p></div>
        </div>
    </main>

    <footer class="rodape">AgroGest &middot; ISCTEM &middot; Sistemas Distribuídos</footer>
</body>
</html>
