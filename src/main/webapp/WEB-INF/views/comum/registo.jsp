<%-- registo.jsp: formulario de auto-registo de clientes. Mantem os campos se houver erro. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registar - AgroGest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilo.css">
</head>
<body class="fundo-auth">
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />

    <main class="auth-pagina">
        <section class="cartao auth-cartao">
            <h2>Criar conta</h2>
            <p class="auth-sub">Registe-se para comprar no AgroGest.</p>

            <c:if test="${not empty erro}">
                <p class="alerta alerta-erro"><c:out value="${erro}" /></p>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/registo">
                <label for="nome">Nome completo</label>
                <input type="text" id="nome" name="nome" required
                       value="<c:out value='${param.nome}' />">

                <label for="email">Email</label>
                <input type="email" id="email" name="email" required
                       value="<c:out value='${param.email}' />">

                <label for="password">Palavra-passe (mínimo 4 caracteres)</label>
                <input type="password" id="password" name="password" required>

                <label for="telefone">Telefone</label>
                <input type="tel" id="telefone" name="telefone" required
                       value="<c:out value='${param.telefone}' />">

                <label for="endereco">Endereço</label>
                <input type="text" id="endereco" name="endereco" required
                       value="<c:out value='${param.endereco}' />">

                <button type="submit" class="btn btn-bloco">Criar conta</button>
            </form>

            <p class="auth-rodape">Já tem conta?
                <a href="${pageContext.request.contextPath}/login">Entrar</a></p>
        </section>
    </main>
</body>
</html>
