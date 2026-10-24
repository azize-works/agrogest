<%-- cultivos.jsp: gestao de cultivos (formulario de criacao em campanhas abertas, filtro por campanha e tabela ordenavel). --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ordem" value="${empty param.ordem ? 'cultura' : param.ordem}" />
<%-- Valores do formulario a repor apos um erro de criacao --%>
<c:set var="f" value="${param.accao == 'criar' ? param : null}" />
<%-- Links de ordenacao: mantem o filtro de campanha escolhido --%>
<c:url var="urlCultura" value="/admin/cultivos"><c:param name="ordem" value="cultura" /><c:param name="campanha" value="${filtroCampanha}" /></c:url>
<c:url var="urlHectares" value="/admin/cultivos"><c:param name="ordem" value="hectares" /><c:param name="campanha" value="${filtroCampanha}" /></c:url>
<c:url var="urlPlantio" value="/admin/cultivos"><c:param name="ordem" value="plantio" /><c:param name="campanha" value="${filtroCampanha}" /></c:url>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cultivos - AgroGest</title>
    <link rel="stylesheet" href="${ctx}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />
    <jsp:include page="/WEB-INF/views/admin/menu-admin.jsp">
        <jsp:param name="activo" value="cultivos" />
    </jsp:include>

    <main class="conteudo">
        <h2>Cultivos</h2>

        <%-- Avisos: erro de validacao ou sucesso depois do redirect --%>
        <c:if test="${not empty erro}">
            <p class="alerta alerta-erro"><c:out value="${erro}" /></p>
        </c:if>
        <c:if test="${not empty param.ok}">
            <p class="alerta alerta-ok"><c:out value="${param.ok}" /></p>
        </c:if>

        <%-- Formulario de criacao de cultivo (so lista campanhas abertas) --%>
        <section class="cartao">
            <h3>Novo cultivo</h3>
            <c:if test="${empty campanhasAbertas}">
                <p class="alerta alerta-erro">Não há campanhas abertas. Crie ou reabra uma em
                    <a href="${ctx}/admin/campanhas">Campanhas</a>.</p>
            </c:if>
            <form method="post" action="${ctx}/admin/cultivos" class="form-grelha">
                <input type="hidden" name="accao" value="criar">
                <div>
                    <label for="campanhaId">Campanha (aberta) *</label>
                    <select id="campanhaId" name="campanhaId">
                        <option value="">-- escolha --</option>
                        <c:forEach var="ca" items="${campanhasAbertas}">
                            <option value="${ca.id}" ${ca.id.toString() == f.campanhaId ? 'selected' : ''}><c:out value="${ca.nome}" /> (${ca.inicioTexto} a ${ca.fimTexto})</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <label for="cultura">Cultura *</label>
                    <input type="text" id="cultura" name="cultura" placeholder="Ex.: Feijão nhemba"
                           value="<c:out value='${f.cultura}' />">
                </div>
                <div>
                    <label for="variedade">Variedade</label>
                    <input type="text" id="variedade" name="variedade" placeholder="Ex.: Macia"
                           value="<c:out value='${f.variedade}' />">
                </div>
                <div>
                    <label for="hectares">Área (hectares) *</label>
                    <input type="number" id="hectares" name="hectares" step="any"
                           value="<c:out value='${f.hectares}' />">
                </div>
                <div>
                    <label for="dataPlantio">Data de plantio *</label>
                    <input type="date" id="dataPlantio" name="dataPlantio"
                           value="<c:out value='${f.dataPlantio}' />">
                </div>
                <div class="form-accoes">
                    <button type="submit" class="btn">Criar cultivo</button>
                </div>
            </form>
        </section>

        <%-- Tabela de cultivos com filtro por campanha e ordenacao por cultura, hectares ou plantio --%>
        <section class="cartao">
            <div class="cartao-topo">
                <h3>Lista de cultivos</h3>
                <form method="get" action="${ctx}/admin/cultivos" class="form-filtro">
                    <input type="hidden" name="ordem" value="<c:out value='${ordem}' />">
                    <select name="campanha" aria-label="Campanha">
                        <option value="">Todas as campanhas</option>
                        <c:forEach var="ca" items="${todasCampanhas}">
                            <option value="${ca.id}" ${ca.id == filtroCampanha ? 'selected' : ''}><c:out value="${ca.nome}" /></option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="btn btn-sec btn-pequeno">Filtrar</button>
                </form>
            </div>
            <div class="tabela-scroll">
                <table class="tabela-ordenavel">
                    <tr>
                        <th class="${ordem == 'cultura' ? 'ordenada' : ''}"><a href="${urlCultura}">Cultura</a></th>
                        <th>Variedade</th>
                        <th>Campanha</th>
                        <th class="${ordem == 'hectares' ? 'ordenada' : ''}"><a href="${urlHectares}">Hectares</a></th>
                        <th class="${ordem == 'plantio' ? 'ordenada' : ''}"><a href="${urlPlantio}">Plantio</a></th>
                        <th></th>
                    </tr>
                    <c:forEach var="cu" items="${cultivos}">
                        <tr>
                            <td><c:out value="${cu.cultura}" /></td>
                            <td><c:out value="${cu.variedade}" default="—" /></td>
                            <td><c:out value="${nomesCampanhas[cu.campanhaId]}" default="—" /></td>
                            <td class="numero-celula"><fmt:formatNumber value="${cu.hectares}" maxFractionDigits="2" /> ha</td>
                            <td>${cu.dataPlantioTexto}</td>
                            <td>
                                <form method="post" action="${ctx}/admin/cultivos"
                                      onsubmit="return confirm('Remover este cultivo?');">
                                    <input type="hidden" name="accao" value="remover">
                                    <input type="hidden" name="id" value="${cu.id}">
                                    <button type="submit" class="btn btn-perigo btn-pequeno">Remover</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty cultivos}">
                        <tr><td colspan="6" class="vazio">Não há cultivos para mostrar.</td></tr>
                    </c:if>
                </table>
            </div>
        </section>
    </main>
</body>
</html>

