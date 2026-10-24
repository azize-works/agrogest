<%-- produtos.jsp: gestao de produtos (formulario de criacao, filtro por categoria, tabela ordenavel com repor stock e remover). --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ordem" value="${empty param.ordem ? 'nome' : param.ordem}" />
<%-- Valores do formulario a repor apos um erro (so quando o erro veio do "criar") --%>
<c:set var="f" value="${param.accao == 'criar' ? param : null}" />
<%-- Links de ordenacao: mantem o filtro de categoria escolhido --%>
<c:url var="urlNome" value="/admin/produtos"><c:param name="ordem" value="nome" /><c:param name="categoria" value="${filtro}" /></c:url>
<c:url var="urlPreco" value="/admin/produtos"><c:param name="ordem" value="preco" /><c:param name="categoria" value="${filtro}" /></c:url>
<c:url var="urlStock" value="/admin/produtos"><c:param name="ordem" value="stock" /><c:param name="categoria" value="${filtro}" /></c:url>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Produtos - AgroGest</title>
    <link rel="stylesheet" href="${ctx}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />
    <jsp:include page="/WEB-INF/views/admin/menu-admin.jsp">
        <jsp:param name="activo" value="produtos" />
    </jsp:include>

    <main class="conteudo">
        <h2>Produtos</h2>

        <%-- Avisos: erro de validacao ou sucesso depois do redirect --%>
        <c:if test="${not empty erro}">
            <p class="alerta alerta-erro"><c:out value="${erro}" /></p>
        </c:if>
        <c:if test="${not empty param.ok}">
            <p class="alerta alerta-ok"><c:out value="${param.ok}" /></p>
        </c:if>

        <%-- Formulario de criacao --%>
        <section class="cartao">
            <h3>Novo produto</h3>
            <c:if test="${empty fornecedores}">
                <p class="alerta alerta-erro">Crie primeiro um fornecedor em
                    <a href="${ctx}/admin/fornecedores">Fornecedores</a>.</p>
            </c:if>
            <form method="post" action="${ctx}/admin/produtos" class="form-grelha">
                <input type="hidden" name="accao" value="criar">
                <div>
                    <label for="nome">Nome *</label>
                    <input type="text" id="nome" name="nome" value="<c:out value='${f.nome}' />">
                </div>
                <div>
                    <label for="categoria">Categoria *</label>
                    <select id="categoria" name="categoria">
                        <c:forEach var="cat" items="${categorias}">
                            <option value="${cat}" ${cat.name() == f.categoria ? 'selected' : ''}>${cat}</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <label for="fornecedorId">Fornecedor *</label>
                    <select id="fornecedorId" name="fornecedorId">
                        <option value="">-- escolha --</option>
                        <c:forEach var="fo" items="${fornecedores}">
                            <option value="${fo.id}" ${fo.id.toString() == f.fornecedorId ? 'selected' : ''}><c:out value="${fo.nome}" /></option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <label for="preco">Preço por unidade (MT) *</label>
                    <input type="number" id="preco" name="preco" step="any" value="<c:out value='${f.preco}' />">
                </div>
                <div>
                    <label for="quantidade">Quantidade em stock *</label>
                    <input type="number" id="quantidade" name="quantidade" step="any" value="<c:out value='${f.quantidade}' />">
                </div>
                <div>
                    <label for="unidade">Unidade *</label>
                    <input type="text" id="unidade" name="unidade" placeholder="kg, saco, unidade..."
                           value="<c:out value='${f.unidade}' />">
                </div>
                <div>
                    <label for="validade">Validade</label>
                    <input type="date" id="validade" name="validade" value="<c:out value='${f.validade}' />">
                </div>
                <div>
                    <label for="fotoUrl">Foto (URL)</label>
                    <input type="url" id="fotoUrl" name="fotoUrl" placeholder="https://..."
                           value="<c:out value='${f.fotoUrl}' />">
                </div>
                <div class="campo-largo">
                    <label for="descricao">Descrição</label>
                    <textarea id="descricao" name="descricao" rows="2"><c:out value="${f.descricao}" /></textarea>
                </div>
                <div class="form-accoes">
                    <button type="submit" class="btn">Criar produto</button>
                </div>
            </form>
        </section>

        <%-- Lista com filtro por categoria e ordenacao pelos cabecalhos --%>
        <section class="cartao">
            <div class="cartao-topo">
                <h3>Lista de produtos</h3>
                <form method="get" action="${ctx}/admin/produtos" class="form-filtro">
                    <input type="hidden" name="ordem" value="<c:out value='${ordem}' />">
                    <select name="categoria" aria-label="Categoria">
                        <option value="">Todas as categorias</option>
                        <c:forEach var="cat" items="${categorias}">
                            <option value="${cat}" ${cat.name() == filtro ? 'selected' : ''}>${cat}</option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="btn btn-sec btn-pequeno">Filtrar</button>
                </form>
            </div>
            <div class="tabela-scroll">
                <table class="tabela-ordenavel">
                    <tr>
                        <th>Foto</th>
                        <th class="${ordem == 'nome' ? 'ordenada' : ''}"><a href="${urlNome}">Nome</a></th>
                        <th>Categoria</th>
                        <th>Fornecedor</th>
                        <th class="${ordem == 'preco' ? 'ordenada' : ''}"><a href="${urlPreco}">Preço</a></th>
                        <th class="${ordem == 'stock' ? 'ordenada' : ''}"><a href="${urlStock}">Stock</a></th>
                        <th>Validade</th>
                        <th>Repor stock</th>
                        <th></th>
                    </tr>
                    <c:forEach var="p" items="${produtos}">
                        <tr>
                            <td>
                                <c:if test="${not empty p.fotoUrl}">
                                    <img class="miniatura" src="<c:out value='${p.fotoUrl}' />" alt="<c:out value='${p.nome}' />">
                                </c:if>
                            </td>
                            <td><c:out value="${p.nome}" /></td>
                            <td><span class="etiqueta">${p.categoria}</span></td>
                            <td><c:out value="${nomesFornecedores[p.fornecedorId]}" default="—" /></td>
                            <td class="numero-celula"><fmt:formatNumber value="${p.preco}" minFractionDigits="2" maxFractionDigits="2" /> MT</td>
                            <td class="numero-celula ${p.quantidade == 0 ? 'sem-stock' : ''}">
                                <fmt:formatNumber value="${p.quantidade}" maxFractionDigits="2" /> <c:out value="${p.unidade}" />
                            </td>
                            <td>${p.validadeTexto}</td>
                            <td>
                                <form method="post" action="${ctx}/admin/produtos" class="form-inline">
                                    <input type="hidden" name="accao" value="repor">
                                    <input type="hidden" name="id" value="${p.id}">
                                    <input type="number" name="quantidade" step="any" placeholder="Qtd." aria-label="Quantidade a repor">
                                    <button type="submit" class="btn btn-pequeno">Repor</button>
                                </form>
                            </td>
                            <td>
                                <form method="post" action="${ctx}/admin/produtos"
                                      onsubmit="return confirm('Remover este produto?');">
                                    <input type="hidden" name="accao" value="remover">
                                    <input type="hidden" name="id" value="${p.id}">
                                    <button type="submit" class="btn btn-perigo btn-pequeno">Remover</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty produtos}">
                        <tr><td colspan="9" class="vazio">Não há produtos para mostrar.</td></tr>
                    </c:if>
                </table>
            </div>
        </section>
    </main>
</body>
</html>

