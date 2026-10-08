## Decisões implementadas no desenvolvimento do projeto.

- Iniciei o projeto pelo inicializador [spring](https://start.spring.io/)
    - Adicionei as dependencias para [web](https://mvnrepository.com/artifact/org.springframework/spring-web) e banco de dados [Postgres](https://mvnrepository.com/artifact/org.postgresql/postgresql).
    - Como o inicializador retira versões antigas da UI, após inicializar o projeto eu dei downgrade do projeto para a versão 3.4.3 (uma estável que achei) e então atualizei o projeto.
    - Corrigi algumas dependencias por ter dado o downgrade do projeto.
    - Configurei o application.properties e adicionei ele no .gitignore
    - Configurei o pom.xml com dados que faltavam para especificar o projeto (nome, url, desenvolvedores, licensa, etc...)

- Fiz a modelagem do banco de dados no Excalidraw e coloquei no projeto no arquivo db_diagram.excalidraw

- Comecei criando as entidades que modelei.
    - BaseEntity
        - Para isto eu li um tutorial no [Medium](https://medium.com/@kouomeukevin/create-a-base-entity-with-jpa-8adb35d2b7a3) sobre como fazer uma classe genérica em JPA. Bem parecido com Nestjs.
        - Por implementar o Serializable eu preciso colocar um identificador de versão unico para cada classe que implementa esta interface
    - Laboratory
        - Para os relacionamentos eu olhei a doc do [jakarta](https://jakarta.ee/specifications/persistence/2.2/apidocs/javax/persistence) e percebi que relacionamentos tem difereça de FetchType, padronizado para cada um (OneToOne, OneToMany, ManyToOne e ManyToMany), alguns são Eager e outros são Lazy
    - Farmstead
    - Grower

- Rodei o projeto e verifiquei tudo com o [DBeaver CE](https://dbeaver.io/)
