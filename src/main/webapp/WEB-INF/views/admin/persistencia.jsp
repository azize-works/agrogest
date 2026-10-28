<%-- persistencia.jsp: estado do ficheiro de dados e botoes para gravar agora ou eliminar o ficheiro. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Persistência - AgroGest</title>
    <link rel="stylesheet" href="${ctx}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />
    <jsp:include page="/WEB-INF/views/admin/menu-admin.jsp">
        <jsp:param name="activo" value="persistencia" />
    </jsp:include>

    <main class="conteudo">
        <h2>Persistência dos dados</h2>

        <%-- Avisos: erro de E/S ou sucesso depois do redirect --%>
        <c:if test="${not empty erro}">
            <p class="alerta alerta-erro"><c:out value="${erro}" /></p>
        </c:if>
        <c:if test="${not empty param.ok}">
            <p class="alerta alerta-ok"><c:out value="${param.ok}" /></p>
        </c:if>

        <%-- Estado do ficheiro de dados --%>
        <section class="cartao cartao-persistencia">
            <h3>Ficheiro de dados</h3>
            <dl class="lista-estado">
                <dt>Estado</dt>
                <dd>
                    <span class="selo ${existe ? 'selo-aberta' : 'selo-fechada'}">${existe ? 'Existe' : 'Não existe'}</span>
                </dd>
                <dt>Caminho</dt>
                <dd><code><c:out value="${caminho}" /></code></dd>
                <dt>Tamanho</dt>
                <dd><c:choose>
                    <c:when test="${existe}"><fmt:formatNumber value="${tamanho}" /> bytes</c:when>
                    <c:otherwise>—</c:otherwise>
                </c:choose></dd>
                <dt>Última gravação</dt>
                <dd>${ultimaModificacao}</dd>
            </dl>

            <%-- Botoes: gravar agora e eliminar o ficheiro --%>
            <div class="carrinho-accoes">
                <form method="post" action="${ctx}/admin/persistencia">
                    <input type="hidden" name="accao" value="gravar">
                    <button type="submit" class="btn">Gravar agora</button>
                </form>
                <form method="post" action="${ctx}/admin/persistencia"
                      onsubmit="return confirm('Eliminar o ficheiro de dados? Os dados em memória mantêm-se.');">
                    <input type="hidden" name="accao" value="eliminar">
                    <button type="submit" class="btn btn-perigo">Eliminar ficheiro</button>
                </form>
            </div>

            <%-- Nota explicativa --%>
            <p class="nota">
                Os dados também são gravados automaticamente ao parar o Tomcat.
                Eliminar apaga só o ficheiro, não os dados em memória.
            </p>
        </section>
    </main>
</body>
</html>

