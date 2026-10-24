<%-- erro403.jsp: pagina mostrada quando um utilizador tenta aceder a uma rota que nao lhe pertence. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Acesso negado - AgroGest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilo.css">
</head>
<body class="fundo-auth">
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />

    <main class="auth-pagina">
        <section class="cartao auth-cartao">
            <h2>Acesso negado (403)</h2>
            <p>Não tem permissão para aceder a esta página.</p>
            <a class="btn btn-bloco" href="${pageContext.request.contextPath}/">Voltar ao início</a>
        </section>
    </main>
</body>
</html>
