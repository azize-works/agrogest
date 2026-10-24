<%-- colheitas.jsp: gestao de colheitas (formulario de criacao, filtro por cultivo, tabela ordenavel e linha de totais). --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ordem" value="${empty param.ordem ? 'data' : param.ordem}" />
<%-- Valores do formulario a repor apos um erro de criacao --%>
<c:set var="f" value="${param.accao == 'criar' ? param : null}" />
<%-- Links de ordenacao: mantem o filtro de cultivo escolhido --%>
<c:url var="urlData" value="/admin/colheitas"><c:param name="ordem" value="data" /><c:param name="cultivo" value="${filtroCultivo}" /></c:url>
<c:url var="urlKg" value="/admin/colheitas"><c:param name="ordem" value="kg" /><c:param name="cultivo" value="${filtroCultivo}" /></c:url>
<c:url var="urlValor" value="/admin/colheitas"><c:param name="ordem" value="valor" /><c:param name="cultivo" value="${filtroCultivo}" /></c:url>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Colheitas - AgroGest</title>
    <link rel="stylesheet" href="${ctx}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />
    <jsp:include page="/WEB-INF/views/admin/menu-admin.jsp">
        <jsp:param name="activo" value="colheitas" />
    </jsp:include>

    <main class="conteudo">
        <h2>Colheitas</h2>

        <%-- Avisos: erro de validacao ou sucesso depois do redirect --%>
        <c:if test="${not empty erro}">
            <p class="alerta alerta-erro"><c:out value="${erro}" /></p>
        </c:if>
        <c:if test="${not empty param.ok}">
            <p class="alerta alerta-ok"><c:out value="${param.ok}" /></p>
        </c:if>

        <%-- Formulario de registo de colheita --%>
        <section class="cartao">
            <h3>Nova colheita</h3>
            <c:if test="${empty cultivos}">
                <p class="alerta alerta-erro">Crie primeiro um cultivo em
                    <a href="${ctx}/admin/cultivos">Cultivos</a>.</p>
            </c:if>
            <form method="post" action="${ctx}/admin/colheitas" class="form-grelha">
                <input type="hidden" name="accao" value="criar">
                <div>
                    <label for="cultivoId">Cultivo *</label>
                    <select id="cultivoId" name="cultivoId">
                        <option value="">-- escolha --</option>
                        <c:forEach var="cu" items="${cultivos}">
                            <option value="${cu.id}" ${cu.id.toString() == f.cultivoId ? 'selected' : ''}><c:out value="${descricoesCultivos[cu.id]}" /></option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <label for="data">Data da colheita *</label>
                    <input type="date" id="data" name="data" value="<c:out value='${f.data}' />">
                </div>
                <div>
                    <label for="kg">Quantidade (kg) *</label>
                    <input type="number" id="kg" name="kg" step="any" value="<c:out value='${f.kg}' />">
                </div>
                <div>
                    <label for="precoKg">Preço por kg (MT) *</label>
                    <input type="number" id="precoKg" name="precoKg" step="any" value="<c:out value='${f.precoKg}' />">
                </div>
                <div class="form-accoes">
                    <button type="submit" class="btn">Registar colheita</button>
                </div>
            </form>
        </section>

        <%-- Tabela de colheitas com filtro por cultivo, ordenacao e linha de totais --%>
        <section class="cartao">
            <div class="cartao-topo">
                <h3>Lista de colheitas</h3>
                <form method="get" action="${ctx}/admin/colheitas" class="form-filtro">
                    <input type="hidden" name="ordem" value="<c:out value='${ordem}' />">
                    <select name="cultivo" aria-label="Cultivo">
                        <option value="">Todos os cultivos</option>
                        <c:forEach var="cu" items="${cultivos}">
                            <option value="${cu.id}" ${cu.id == filtroCultivo ? 'selected' : ''}><c:out value="${descricoesCultivos[cu.id]}" /></option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="btn btn-sec btn-pequeno">Filtrar</button>
                </form>
            </div>
            <div class="tabela-scroll">
                <table class="tabela-ordenavel">
                    <tr>
                        <th class="${ordem == 'data' ? 'ordenada' : ''}"><a href="${urlData}">Data</a></th>
                        <th>Cultivo</th>
                        <th class="${ordem == 'kg' ? 'ordenada' : ''}"><a href="${urlKg}">Quantidade</a></th>
                        <th>Preço/kg</th>
                        <th class="${ordem == 'valor' ? 'ordenada' : ''}"><a href="${urlValor}">Valor total</a></th>
                        <th></th>
                    </tr>
                    <c:forEach var="co" items="${colheitas}">
                        <tr>
                            <td>${co.dataTexto}</td>
                            <td><c:out value="${descricoesCultivos[co.cultivoId]}" default="—" /></td>
                            <td class="numero-celula"><fmt:formatNumber value="${co.kg}" maxFractionDigits="2" /> kg</td>
                            <td class="numero-celula"><fmt:formatNumber value="${co.precoKg}" minFractionDigits="2" maxFractionDigits="2" /> MT</td>
                            <td class="numero-celula"><fmt:formatNumber value="${co.valorTotal}" minFractionDigits="2" maxFractionDigits="2" /> MT</td>
                            <td>
                                <form method="post" action="${ctx}/admin/colheitas"
                                      onsubmit="return confirm('Remover esta colheita?');">
                                    <input type="hidden" name="accao" value="remover">
                                    <input type="hidden" name="id" value="${co.id}">
                                    <button type="submit" class="btn btn-perigo btn-pequeno">Remover</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${not empty colheitas}">
                        <tr class="linha-totais">
                            <td colspan="2">Total</td>
                            <td class="numero-celula"><fmt:formatNumber value="${totalKg}" maxFractionDigits="2" /> kg</td>
                            <td></td>
                            <td class="numero-celula"><fmt:formatNumber value="${totalValor}" minFractionDigits="2" maxFractionDigits="2" /> MT</td>
                            <td></td>
                        </tr>
                    </c:if>
                    <c:if test="${empty colheitas}">
                        <tr><td colspan="6" class="vazio">Não há colheitas para mostrar.</td></tr>
                    </c:if>
                </table>
            </div>
        </section>
    </main>
</body>
</html>

