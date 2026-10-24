<%-- fornecedores.jsp: gestao de fornecedores (formulario de criacao + tabela ordenavel por nome ou provincia). --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ordem" value="${empty param.ordem ? 'nome' : param.ordem}" />
<%-- Valores do formulario a repor apos um erro (so quando o erro veio do "criar") --%>
<c:set var="f" value="${param.accao == 'criar' ? param : null}" />
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Fornecedores - AgroGest</title>
    <link rel="stylesheet" href="${ctx}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />
    <jsp:include page="/WEB-INF/views/admin/menu-admin.jsp">
        <jsp:param name="activo" value="fornecedores" />
    </jsp:include>

    <main class="conteudo">
        <h2>Fornecedores</h2>

        <%-- Avisos: erro de validacao ou sucesso depois do redirect --%>
        <c:if test="${not empty erro}">
            <p class="alerta alerta-erro"><c:out value="${erro}" /></p>
        </c:if>
        <c:if test="${not empty param.ok}">
            <p class="alerta alerta-ok"><c:out value="${param.ok}" /></p>
        </c:if>

        <%-- Formulario de criacao --%>
        <section class="cartao">
            <h3>Novo fornecedor</h3>
            <form method="post" action="${ctx}/admin/fornecedores" class="form-grelha">
                <input type="hidden" name="accao" value="criar">
                <div>
                    <label for="nome">Nome *</label>
                    <input type="text" id="nome" name="nome" value="<c:out value='${f.nome}' />">
                </div>
                <div>
                    <label for="tipo">Tipo *</label>
                    <select id="tipo" name="tipo">
                        <c:forEach var="t" items="${tipos}">
                            <option value="${t}" ${t.name() == f.tipo ? 'selected' : ''}>${t}</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <label for="contacto">Contacto *</label>
                    <input type="text" id="contacto" name="contacto" placeholder="Telefone ou email"
                           value="<c:out value='${f.contacto}' />">
                </div>
                <div>
                    <label for="provincia">Província *</label>
                    <input type="text" id="provincia" name="provincia" placeholder="Ex.: Maputo"
                           value="<c:out value='${f.provincia}' />">
                </div>
                <div class="form-accoes">
                    <button type="submit" class="btn">Criar fornecedor</button>
                </div>
            </form>
        </section>

        <%-- Lista: clicar no cabecalho muda a ordenacao --%>
        <section class="cartao">
            <h3>Lista de fornecedores</h3>
            <div class="tabela-scroll">
                <table class="tabela-ordenavel">
                    <tr>
                        <th>ID</th>
                        <th class="${ordem == 'nome' ? 'ordenada' : ''}"><a href="${ctx}/admin/fornecedores?ordem=nome">Nome</a></th>
                        <th>Tipo</th>
                        <th>Contacto</th>
                        <th class="${ordem == 'provincia' ? 'ordenada' : ''}"><a href="${ctx}/admin/fornecedores?ordem=provincia">Província</a></th>
                        <th></th>
                    </tr>
                    <c:forEach var="fo" items="${fornecedores}">
                        <tr>
                            <td>${fo.id}</td>
                            <td><c:out value="${fo.nome}" /></td>
                            <td><span class="etiqueta">${fo.tipo}</span></td>
                            <td><c:out value="${fo.contacto}" /></td>
                            <td><c:out value="${fo.provincia}" /></td>
                            <td>
                                <form method="post" action="${ctx}/admin/fornecedores"
                                      onsubmit="return confirm('Remover este fornecedor?');">
                                    <input type="hidden" name="accao" value="remover">
                                    <input type="hidden" name="id" value="${fo.id}">
                                    <button type="submit" class="btn btn-perigo btn-pequeno">Remover</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty fornecedores}">
                        <tr><td colspan="6" class="vazio">Ainda não há fornecedores.</td></tr>
                    </c:if>
                </table>
            </div>
        </section>
    </main>
</body>
</html>

