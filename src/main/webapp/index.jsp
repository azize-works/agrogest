<%-- index.jsp: pagina inicial publica do AgroGest. --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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

    <%-- Secção principal de destaque com ilustração SVG e atalhos rápidos --%>
    <section class="heroi">
        <div class="heroi-conteudo">
            <span class="heroi-ilustracao" aria-hidden="true">
                <svg viewBox="0 0 120 72" width="110" height="66" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                    <circle cx="88" cy="22" r="10" fill="rgba(255,213,79,0.35)" stroke="#ffd54f" />
                    <path d="M8 62c22-16 48-18 76-6 12 5 22 6 28 6" />
                    <path d="M22 62c20-10 46-12 82-2" opacity="0.7" />
                    <path d="M46 54V24" />
                    <path d="M46 38c-10-2-14-10-12-18 8 0 14 6 12 18z" fill="rgba(255,255,255,0.22)" />
                    <path d="M46 32c10-2 14-10 12-18-8 0-14 6-12 18z" fill="rgba(255,255,255,0.22)" />
                </svg>
            </span>
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
