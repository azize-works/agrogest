# AgroGest — Sistema de Gestão Agrícola

Trabalho prático de **Sistemas Distribuídos** (ISCTEM, Engenharia Informática).
Aplicação web em **Java (Servlets + JSP)** publicada no **Apache Tomcat 10.1**, com os dados em memória e gravação em ficheiro (serialização Java). Sem base de dados.

## Requisitos
- JDK 17 ou superior
- Apache Maven 3.9+
- Apache Tomcat 10.1
- Variável de ambiente `CATALINA_HOME` apontando para a pasta do Tomcat

## Como instalar e executar
1. Abrir a pasta do projecto num terminal.
2. Executar `scripts\iniciar.bat`. O script compila com o Maven, copia o `agrogest.war` para o Tomcat e arranca-o.
3. Abrir no browser: http://localhost:8080/agrogest
4. Para parar: `scripts\parar.bat`.

Compilação manual: `mvn clean package` (gera `target/agrogest.war`).

## Credenciais de teste
| Perfil | Email | Palavra-passe |
|---|---|---|
| Administrador | admin@agro.mz | admin234 |
| Cliente | auto-registo na aplicação | — |

## Arquitectura (MVC)
```
src/main/java/mz/isctem/agrogest/
  model/      Classes do domínio (POJOs serializáveis)
  repo/       DataStore (listas em memória) e persistência em ficheiro
  service/    Regras de negócio (ex.: venda atómica)
  servlet/    Controladores (servlet/admin para o administrador)
  filter/     Controlo de acesso (sessão e perfil)
  listener/   Carrega e grava os dados no arranque/encerramento
  util/       Funções auxiliares
src/main/webapp/
  WEB-INF/views/   Páginas JSP (admin, cliente, comum)
  assets/          CSS, imagens e JS
scripts/           iniciar.bat e parar.bat
```

## Estado do projecto
- [x] Esqueleto Maven e estrutura de pastas
- [x] Classes do `model`
- [x] DataStore, persistência e listener
- [ ] Autenticação e filtro
- [ ] Módulo Administrador
- [ ] Módulo Cliente
- [ ] Relatórios
- [ ] Deploy com systemd e manual de utilização
