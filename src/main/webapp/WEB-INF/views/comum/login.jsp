<%-- login.jsp: formulario de entrada. Recebe "erro" do LoginServlet quando o login falha. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
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
            <%-- Logótipo grande no topo do cartão de entrada --%>
            <div class="auth-cabecalho">
                <img class="logo-img logo-auth-grande" src="${pageContext.request.contextPath}/assets/img/logo-agrogest.svg" width="220" height="58" alt="AgroGest">
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
