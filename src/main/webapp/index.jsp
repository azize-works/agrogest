<%-- index.jsp: pagina inicial publica do AgroGest. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>AgroGest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilo.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/comum/cabecalho.jsp" />

    <%-- Secção principal de destaque com o logótipo AgroGest e atalhos rápidos --%>
    <section class="heroi">
        <div class="heroi-conteudo">
            <div class="heroi-logo-bloco">
                <img class="logo-img logo-heroi" src="${pageContext.request.contextPath}/assets/img/logo-agrogest.svg" width="240" height="63" alt="AgroGest">
            </div>
            <h1>Gestão Agrícola, simples e organizada</h1>
            <p>Fornecedores, cultivos, colheitas e vendas num só lugar.</p>
            <div class="heroi-accoes">
                <a class="btn btn-trigo" href="${pageContext.request.contextPath}/catalogo">Ver Catálogo</a>
                <c:if test="${empty sessionScope.utilizador}">
                    <a class="btn btn-contorno-claro" href="${pageContext.request.contextPath}/login">Entrar na Conta</a>
                </c:if>
            </div>
        </div>
    </section>

    <main class="conteudo">
        <div class="grelha">
            <div class="cartao cartao-destaque">
                <span class="icone-destaque" aria-hidden="true">
                    <svg viewBox="0 0 24 24" width="26" height="26" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/><polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/></svg>
                </span>
                <h3>Catálogo</h3>
                <p>Sementes, fertilizantes, maquinaria e ferramentas.</p>
            </div>
            <div class="cartao cartao-destaque">
                <span class="icone-destaque" aria-hidden="true">
                    <svg viewBox="0 0 24 24" width="26" height="26" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M7 20h10"/><path d="M10 20c5.5-2.5.8-6.4 3-10"/><path d="M9.5 9.4c1.1.8 1.8 2.2 2.3 3.7-2 .4-3.5.4-4.8-.3-1.2-.6-2.3-1.9-3-4.2 2.8-.5 4.4 0 5.5.8z"/><path d="M14.1 6a7 7 0 0 0-1.1 4c1.9-.1 3.3-.6 4.3-1.4 1-1 1.6-2.3 1.7-4.6-2.7.1-4 1-4.9 2z"/></svg>
                </span>
                <h3>Campanhas</h3>
                <p>Controlo dos cultivos e colheitas de cada campanha.</p>
            </div>
            <div class="cartao cartao-destaque">
                <span class="icone-destaque" aria-hidden="true">
                    <svg viewBox="0 0 24 24" width="26" height="26" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/></svg>
                </span>
                <h3>Facturas</h3>
                <p>Compras com factura detalhada e IVA de 16%.</p>
            </div>
        </div>
    </main>

    <footer class="rodape">AgroGest &middot; ISCTEM &middot; Sistemas Distribuídos</footer>
</body>
</html>
