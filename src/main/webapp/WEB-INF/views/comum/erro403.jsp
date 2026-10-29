<%-- erro403.jsp: pagina mostrada quando um utilizador tenta aceder a uma rota que nao lhe pertence. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
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
            <%-- Logótipo versão pequena na página de erro 403 --%>
            <div class="auth-cabecalho">
                <img class="logo-img logo-auth-pequeno" src="${pageContext.request.contextPath}/assets/img/logo-agrogest.svg" width="168" height="44" alt="AgroGest">
                <h2>Acesso negado (403)</h2>
                <p class="auth-sub">Não tem permissão para aceder a esta página.</p>
            </div>
            <a class="btn btn-bloco" href="${pageContext.request.contextPath}/">Voltar ao início</a>
        </section>
    </main>
</body>
</html>
