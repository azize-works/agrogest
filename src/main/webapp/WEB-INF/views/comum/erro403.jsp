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
            <div class="auth-cabecalho">
                <span class="auth-icone auth-icone-erro" aria-hidden="true">
                    <svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                </span>
                <h2>Acesso negado (403)</h2>
                <p class="auth-sub">Não tem permissão para aceder a esta página.</p>
            </div>
            <a class="btn btn-bloco" href="${pageContext.request.contextPath}/">Voltar ao início</a>
        </section>
    </main>
</body>
</html>
