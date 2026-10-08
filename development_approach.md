## Decisões implementadas no desenvolvimento do projeto.

- Iniciei o projeto pelo inicializador [spring](https://start.spring.io/)
    - Adicionei as dependencias para [web](https://mvnrepository.com/artifact/org.springframework/spring-web) e banco de dados [Postgres](https://mvnrepository.com/artifact/org.postgresql/postgresql).
    - Como o inicializador retira versões antigas da UI, após inicializar o projeto eu dei downgrade do projeto para a versão 3.4.3 (uma estável que achei) e então atualizei o projeto.
    - Corrigi algumas dependencias por ter dado o downgrade do projeto.
    - Configurei o application.properties e adicionei ele no .gitignore
    - Configurei o pom.xml com dados que faltavam para especificar o projeto (nome, url, desenvolvedores, licensa, etc...)

- Fiz a modelagem do banco de dados no Excalidraw e coloquei no projeto no arquivo db_diagram.excalidraw
