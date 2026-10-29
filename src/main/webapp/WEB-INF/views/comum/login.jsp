<%-- login.jsp: formulario de entrada. Recebe "erro" do LoginServlet quando o login falha. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Entrar - AgroGest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilo.css">
</head>
<body class="fundo-auth">
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />

    <main class="auth-pagina">
        <section class="cartao auth-cartao">
            <div class="auth-cabecalho">
                <span class="auth-icone" aria-hidden="true">
                    <svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"/><polyline points="10 17 15 12 10 7"/><line x1="15" y1="12" x2="3" y2="12"/></svg>
                </span>
                <h2>Entrar</h2>
                <p class="auth-sub">Aceda à sua conta AgroGest.</p>
            </div>

            <%-- Mensagens: erro de login ou conta criada com sucesso --%>
            <c:if test="${not empty erro}">
                <p class="alerta alerta-erro"><c:out value="${erro}" /></p>
            </c:if>
            <c:if test="${param.registado == '1'}">
                <p class="alerta alerta-ok">Conta criada com sucesso. Já pode entrar.</p>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/login">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required
                       value="<c:out value='${param.email}' />">

                <label for="password">Palavra-passe</label>
                <input type="password" id="password" name="password" required>

                <button type="submit" class="btn btn-bloco">Entrar</button>
            </form>

            <p class="auth-rodape">Ainda não tem conta?
                <a href="${pageContext.request.contextPath}/registo">Registar-se</a></p>
        </section>
    </main>
</body>
</html>
