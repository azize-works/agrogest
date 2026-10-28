# AgroGest — Sistema de Gestão Agrícola

Trabalho prático de **Sistemas Distribuídos** (ISCTEM, Engenharia Informática).
Aplicação web em **Java (Servlets + JSP + JSTL)** publicada no **Apache Tomcat 10.1**, com os dados em memória e gravação em ficheiro (serialização Java). Sem base de dados.

**Autores:** Cleyton Azize Azize (20240534) ; Isma Cudbudin (20220133)

---

## Acessos rápidos

| O quê | Valor |
|---|---|
| Aplicação | http://localhost:8080/agrogest |
| **Administrador** | `admin@agro.mz` / `admin234` (conta fixa, criada no arranque) |
| **Cliente** | Auto-registo em `/registo` (nome, email único, palavra-passe, telefone, endereço) |
| Cliente de exemplo do enunciado | `joao@teste.mz` / `1234`: só existe depois de o registares |
| Página do Tomcat | http://localhost:8080 (sem utilizador nem palavra-passe configurados) |
| Repositório GitHub | https://github.com/azize-works/agrogest |
| Porta | 8080 (o diagrama do enunciado usa 8081; muda-se no `server.xml` no passo do deploy) |

> Não guardar neste ficheiro palavras-passe pessoais (GitHub, email, etc.). O repositório pode ser público.

### Onde está cada coisa (computador do Cleyton; no da Isma os caminhos podem variar)

| O quê | Caminho |
|---|---|
| Projecto | `C:\Users\Casa\VisualStudio\agrogest` |
| Tomcat | `C:\Tools\apache-tomcat-10.1.61` |
| Maven | `C:\Tools\apache-maven-3.10.0` |
| JDK | `C:\Program Files\Java\jdk-22` |
| WAR gerado | `target\agrogest.war` |
| Ficheiro de dados | `<pasta do Tomcat>\agrogest-dados\agrogest.ser` (gravado ao parar o Tomcat) |

**Apagar todos os dados:** pára o Tomcat, apaga `agrogest.ser` e arranca outra vez. O administrador é recriado automaticamente.

---

## Requisitos
- JDK 17 ou superior
- Apache Maven 3.9+
- Apache Tomcat 10.1
- Variáveis de ambiente `JAVA_HOME` e `CATALINA_HOME`; o Maven (`bin`) no `Path`

## Como instalar e executar
1. Abrir um terminal na pasta do projecto.
2. Executar `scripts\iniciar.bat`: compila com o Maven, copia o `agrogest.war` para o Tomcat e arranca-o.
3. Abrir http://localhost:8080/agrogest
4. Para parar: `scripts\parar.bat` (os dados são gravados ao parar).

Compilação manual: `mvn clean package` (gera `target/agrogest.war`).

## Páginas (rotas)

| Rota | Quem | Estado |
|---|---|---|
| `/` | Todos | Feita |
| `/login`, `/registo`, `/logout` | Todos | Feitas |
| `/catalogo` | Cliente | Provisória |
| `/admin` (painel) | Admin | Feita |
| `/admin/fornecedores` | Admin | Feita |
| `/admin/produtos` | Admin | Feita |
| `/admin/armazem` | Admin | Feita |
| `/admin/campanhas`, `/admin/cultivos`, `/admin/colheitas` | Admin | Em desenvolvimento |
| `/carrinho/*`, `/compra`, `/factura`, `/minhas-compras` | Cliente | Por fazer |

Rotas `/admin/*` só para o administrador (outros perfis recebem 403); as de cliente exigem sessão iniciada.

## Arquitectura (MVC)
```
src/main/java/mz/isctem/agrogest/
  model/      Classes do domínio (POJOs serializáveis)
  repo/       DataStore (listas em memória) e Persistencia (ficheiro .ser)
  service/    Regras de negócio
  servlet/    Controladores (servlet/admin para o administrador)
  filter/     AuthFilter: controlo de acesso por sessão e perfil
  listener/   Carrega os dados no arranque e grava-os ao encerrar
  util/       Funções auxiliares (hash de palavras-passe, números)
src/main/webapp/
  WEB-INF/views/   Páginas JSP (admin, cliente, comum)
  assets/          CSS, imagens e JS
scripts/           iniciar.bat e parar.bat
```


