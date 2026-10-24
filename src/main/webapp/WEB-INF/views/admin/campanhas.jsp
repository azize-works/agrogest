<%-- campanhas.jsp: gestao de campanhas agricolas (formulario de criacao + tabela ordenavel com fechar, reabrir e remover). --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ordem" value="${empty param.ordem ? 'nome' : param.ordem}" />
<%-- Valores do formulario a repor apos um erro de criacao --%>
<c:set var="f" value="${param.accao == 'criar' ? param : null}" />
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Campanhas - AgroGest</title>
    <link rel="stylesheet" href="${ctx}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />
    <jsp:include page="/WEB-INF/views/admin/menu-admin.jsp">
        <jsp:param name="activo" value="campanhas" />
    </jsp:include>

    <main class="conteudo">
        <h2>Campanhas</h2>

        <%-- Avisos: erro de validacao ou sucesso depois do redirect --%>
        <c:if test="${not empty erro}">
            <p class="alerta alerta-erro"><c:out value="${erro}" /></p>
        </c:if>
        <c:if test="${not empty param.ok}">
            <p class="alerta alerta-ok"><c:out value="${param.ok}" /></p>
        </c:if>

        <%-- Formulario de criacao de campanha --%>
        <section class="cartao">
            <h3>Nova campanha</h3>
            <form method="post" action="${ctx}/admin/campanhas" class="form-grelha">
                <input type="hidden" name="accao" value="criar">
                <div>
                    <label for="nome">Nome *</label>
                    <input type="text" id="nome" name="nome" placeholder="Ex.: Campanha 2026/2027"
                           value="<c:out value='${f.nome}' />">
                </div>
                <div>
                    <label for="inicio">Data de início *</label>
                    <input type="date" id="inicio" name="inicio" value="<c:out value='${f.inicio}' />">
                </div>
                <div>
                    <label for="fim">Data de fim *</label>
                    <input type="date" id="fim" name="fim" value="<c:out value='${f.fim}' />">
                </div>
                <div class="form-accoes">
                    <button type="submit" class="btn">Criar campanha</button>
                </div>
            </form>
        </section>

        <%-- Tabela de campanhas: ordenavel por nome ou data de inicio --%>
        <section class="cartao">
            <h3>Lista de campanhas</h3>
            <div class="tabela-scroll">
                <table class="tabela-ordenavel">
                    <tr>
                        <th class="${ordem == 'nome' ? 'ordenada' : ''}"><a href="${ctx}/admin/campanhas?ordem=nome">Nome</a></th>
                        <th class="${ordem == 'inicio' ? 'ordenada' : ''}"><a href="${ctx}/admin/campanhas?ordem=inicio">Início</a></th>
                        <th>Fim</th>
                        <th>Estado</th>
                        <th>Acções</th>
                    </tr>
                    <c:forEach var="ca" items="${campanhas}">
                        <tr>
                            <td><c:out value="${ca.nome}" /></td>
                            <td>${ca.inicioTexto}</td>
                            <td>${ca.fimTexto}</td>
                            <td>
                                <span class="selo ${ca.aberta ? 'selo-aberta' : 'selo-fechada'}">${ca.estadoTexto}</span>
                            </td>
                            <td class="accoes-celula">
                                <c:choose>
                                    <c:when test="${ca.aberta}">
                                        <form method="post" action="${ctx}/admin/campanhas" class="form-inline">
                                            <input type="hidden" name="accao" value="fechar">
                                            <input type="hidden" name="id" value="${ca.id}">
                                            <button type="submit" class="btn btn-sec btn-pequeno">Fechar</button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <form method="post" action="${ctx}/admin/campanhas" class="form-inline">
                                            <input type="hidden" name="accao" value="reabrir">
                                            <input type="hidden" name="id" value="${ca.id}">
                                            <button type="submit" class="btn btn-pequeno">Reabrir</button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                                <form method="post" action="${ctx}/admin/campanhas" class="form-inline"
                                      onsubmit="return confirm('Remover esta campanha?');">
                                    <input type="hidden" name="accao" value="remover">
                                    <input type="hidden" name="id" value="${ca.id}">
                                    <button type="submit" class="btn btn-perigo btn-pequeno">Remover</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty campanhas}">
                        <tr><td colspan="5" class="vazio">Ainda não há campanhas registadas.</td></tr>
                    </c:if>
                </table>
            </div>
        </section>
    </main>
</body>
</html>

