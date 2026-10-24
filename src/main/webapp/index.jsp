<%-- index.jsp: pagina inicial do AgroGest (provisoria, confirma que a aplicacao esta a correr) --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>AgroGest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilo.css">
</head>
<body>
    <header class="topo">
        <span class="marca">AgroGest</span>
        <nav>
            <a href="#">Catálogo</a>
            <a href="#">Entrar</a>
            <a href="#">Registar</a>
        </nav>
    </header>

    <section class="heroi">
        <h1>Gestão Agrícola, simples e organizada</h1>
        <p>Fornecedores, cultivos, colheitas e vendas num só lugar.</p>
    </section>

    <main class="conteudo">
        <div class="grelha">
            <div class="cartao"><h3>Catálogo</h3><p>Sementes, fertilizantes, maquinaria e ferramentas.</p></div>
            <div class="cartao"><h3>Campanhas</h3><p>Controlo dos cultivos e colheitas de cada campanha.</p></div>
            <div class="cartao"><h3>Facturas</h3><p>Compras com factura detalhada e IVA de 16%.</p></div>
        </div>
    </main>

    <footer class="rodape">AgroGest &middot; ISCTEM &middot; Sistemas Distribuídos</footer>
</body>
</html>
