## Decisões implementadas no desenvolvimento do projeto.

- Iniciei o projeto pelo inicializador [spring](https://start.spring.io/)
    - Adicionei as dependencias para [web](https://mvnrepository.com/artifact/org.springframework/spring-web) e banco de dados [Postgres](https://mvnrepository.com/artifact/org.postgresql/postgresql).
    - Como o inicializador retira versões antigas da UI, após inicializar o projeto eu dei downgrade do projeto para a versão 3.4.3 (uma estável que achei) e então atualizei o projeto.
    - Corrigi algumas dependencias por ter dado o downgrade do projeto.
    - Configurei o application.properties e adicionei ele no .gitignore
    - Configurei o pom.xml com dados que faltavam para especificar o projeto (nome, url, desenvolvedores, licensa, etc...)

- Fiz a modelagem do banco de dados no Excalidraw e coloquei no projeto no arquivo `db_diagram.excalidraw`

- Comecei criando as entidades que modelei.
    - BaseEntity
        - Para isto eu li um tutorial no [Medium](https://medium.com/@kouomeukevin/create-a-base-entity-with-jpa-8adb35d2b7a3) sobre como fazer uma classe genérica em JPA. Bem parecido com Nestjs.
        - Por implementar o Serializable eu preciso colocar um identificador de versão unico para cada classe que implementa esta interface
    - Laboratory
        - Para os relacionamentos eu olhei a doc do [jakarta](https://jakarta.ee/specifications/persistence/2.2/apidocs/javax/persistence) e percebi que relacionamentos tem difereça de FetchType, padronizado para cada um (OneToOne, OneToMany, ManyToOne e ManyToMany), alguns são Eager e outros são Lazy
    - Farmstead
    - Grower

- Rodei o projeto e verifiquei tudo com o [DBeaver CE](https://dbeaver.io/)

- Parti para a criação dos DTOs
	- Resolve criar um DTO Base para padronizar atributos igual nas Entidades.
		- Pedi para a IA gerar o DTO base para mim:
			```
			Gere um DTO base abstrato de request para ser implementado por outros DTOs.
				- Utilize Java 17 + Spring 3.4.x + Hibernate;
				- Utilizei Lombok para Getters e Setters nas entidades;
				- Tem que ter Name, registration e address (todos String e notblank);
			```
		- Li um pouco sobre validação de cpf e cnpj em Java no [Medium](https://medium.com/blog-gilson-silva-ti/validando-cpf-cnpj-na-mesma-vari%C3%A1vel-com-bean-validation-4429a49e9bb5)
		- Visando não construir do zero isso. Resolvi usar esta dependencia (nos comentarios) e pedir para a IA gerar utilizando o seguinte prompt:
			```
			Gere um validador de um campo de inscrição fiscal (registration) em Java para CPF e CNPJ junto.
				- Use org.hibernate.validator.constraints.br para interface CPF e CNPJ.
				- Tenha uma mensagem padrão de erro.
				- Seja possivel a implementação de validação através do DTO.
			```
	- Somente criei os DTOs restantes.


- Segui para a etapa 2.
	- A principio pensei um pouco sobre como implementar isso. Os pontos levantados foram:
		- Preciso implementar um metodo que possa ser abstraido para cada classe e ela implemente o seu tipo de calculo.
			- Como olhei os metodos criado pela IA anteriormente. Resolvi criar uma interface para isso.
		- Preciso implementar em cada classe propriedades que façam sentido para um calculo.
			- Produtor -> produção total, comissão por produção.
				- Calcula o retorno da produção.
			- Propriedade -> area total em hectares, taxa por hectare.
				- Calcula a taxa total da propriedade.
			- Laboratorio -> custo de operação, taxa de operação
				- Impõe a taxa em cima dos custos de operação do laborátorio
		- Preciso de um tipo para calculo financeiro em Java.
			- BigDecimal foi o que encontrei na comunidade em geral.
			- Em Nestjs usamos decimal.
	- Como não conheço todas anotações possiveis ainda. Utilizei a IA para revisar as anotações dos meus DTOs nessa parte.
