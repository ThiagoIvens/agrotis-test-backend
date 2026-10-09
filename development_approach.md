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

- Criei os repositorios de cada entidade com [JpaRepository](https://docs.spring.io/spring-data/jpa/docs/1.6.0.RELEASE/reference/html/jpa.repositories.html).

- Fiz a parte 3 do teste (Endpoint Analítico e Consolidação (Laboratórios)).
	- Como não tinha entendido muito bem (alem da parte de gerar um relatorio), pedi para IA me explicar melhor o que seria esta etapa.
	- Ela me deu uma explicação jogando a carga para o service. Porem pensei que o banco pode fazer estes calculos, provavelmente por ser um relatorio não é algo requisitado com frequencia absurda.
	- Então optei por jogar a carga toda pro banco para consulta.
	- Criei uma query para a busca do relatorio, me trazendo os dados formatados, simplificando todo papel de manipulação do dado.
	- Para esta etapa em questão precisei adaptar minhas entidades para atender aos dados que foram requisitados.
		- Adicionando operationInitialDate, operationFinalDate e observations ao Grower, alem de mudar a relação de Grower->Laboratory para ManyToOne.
		- Mudei a relação em laboratory para OneToMany.
	- Para parte do DTO do report tinha ficado meio confuso para mim esse proxy que ele faz. Então tive que pesquisar um pouco para entender.
	- Criei o DTO de filtro do report.

- Criação dos services de cada entidade.
	- Visando ter listagens paginatadas pedi para IA gerar esse componente para otimizar tempo.
		```
			Atue como um Arquiteto de Software Java Sênior. Preciso da implementação de uma classe utilitária de resposta paginada genérica (`PaginatedResponse<T>` ou `PaginatedData<T>`) para ser utilizada nas camadas de Service e Controller de uma aplicação backend.
			Stack e Tecnologias:
				- Java 17 (Aproveite recursos modernos como `record` se apropriado, ou classes imutáveis com construtores adequados para serialização JSON).
				- Spring Boot 3.4.x (Spring Data `Page` / `Pageable`).
				- Jackson (para serialização/deserialização JSON limpa, sem campos nulos desnecessários).
			Requisitos Funcionais e Estrutura do DTO:
			1. A resposta deve ser genérica (`T`) e encapsular os dados de forma limpa para APIs REST.
			2. Deve conter a seguinte estrutura de metadados de paginação:
				- `content`: `List<T>` (lista de dados da página atual)
				- `pageNumber`: int (índice da página atual, 0-indexed)
				- `pageSize`: int (tamanho da página)
				- `totalElements`: long (total de elementos no banco)
				- `totalPages`: int (total de páginas disponíveis)
				- `isFirst`: boolean (se é a primeira página)
				- `isLast`: boolean (se é a última página)
				- `hasNext`: boolean (se existe próxima página)
				- `hasPrevious`: boolean (se existe página anterior)
			3. Forneça um método utilitário estático `from(Page<T> page)` que converta diretamente um objeto `org.springframework.data.domain.Page<T>` na estrutura criada.
			4. Forneça um método utilitário estático `from(Page<U> page, Function<U, T> mapper)` para permitir o mapeamento/conversão de Entidade (`U`) para DTO (`T`) diretamente ao instanciar a paginação.
			5. Forneça uma estrutura base de um mapper de entidade para DTO e vice-versa.
			
			Entregáveis Esperados:
			1. Código Java completo da estrutura `PaginatedResponse<T>`.
			2. Exemplo prático de uso no Service (convertendo Entidade para DTO via `from(...)`).
			3. Exemplo de consumo no Controller retornando `ResponseEntity<PaginatedResponse<MyDto>>`.
			4. Anotações OpenAPI/Swagger (Springdoc v2) adequadas, se aplicável, para documentação dos metadados.
		```
	- Pesquisei sobre o retorno de exceções em services no Spring.
	- Ele gerou um mapper meio estranho, pesquisando eu usei um mapper padrao como se faz em Nestjs mesmo so adicionando a anotação @Component.
	- Repliquei o codigo base que criei para LaboratoryService para os demais e fiz os ajustes necessarios de cada uma.

- Criação dos controllers com IA:
	```
		Atue como um Arquiteto de Software Java Sênior especializado em Spring Boot.

		Preciso da implementação completa e pronta para uso dos Controllers REST para 3 serviços da minha aplicação. Como não tenho domínio avançado sobre todas as anotações e configurações do ecossistema Java/Spring, QUERO QUE VOCÊ PENSE POR MIM e tome todas as decisões de design, anotações de API, validações e documentação OpenAPI/Swagger.

		### 1. SERVIÇOS E MÉTODOS QUE DEVEM SER ATENDIDOS

		1. LaboratoryService:
		- List<LaboratoryReportResponseDTO> generateReport(ReportFilterDTO filter);
		- PaginatedResponse<LaboratoryDTO> getAll(Pageable pageable);
		- LaboratoryDTO findById(UUID id);
		- LaboratoryDTO create(LaboratoryRequestDTO request);
		- LaboratoryDTO update(UUID id, LaboratoryRequestDTO request);
		- void delete(UUID id);

		2. GrowerService:
		- PaginatedResponse<GrowerDTO> getAll(Pageable pageable);
		- GrowerDTO findById(UUID id);
		- GrowerDTO create(GrowerRequestDTO request);
		- GrowerDTO update(UUID id, GrowerRequestDTO request);
		- void delete(UUID id);

		3. FarmsteadService:
		- PaginatedResponse<FarmsteadDTO> getAll(Pageable pageable);
		- FarmsteadDTO findById(UUID id);
		- FarmsteadDTO create(FarmsteadRequestDTO request);
		- FarmsteadDTO update(UUID id, FarmsteadRequestDTO request);
		- void delete(UUID id);

		### 2. O QUE VOCÊ DEVE CONFIGURAR AUTOMATICAMENTE (DECISÕES TÉCNICAS)

		- Mapeamento e Verbos REST:
		- Adicione as rotas base corretas em plural (ex: `/api/v1/laboratories`, `/api/v1/growers`, `/api/v1/farmsteads`).
		- Mapeie cada método para o verbo correto (GET para busca/relatório, POST para criação, PUT para edição, DELETE para remoção).
		- Trate a resposta do POST para retornar o status HTTP `201 Created` junto com o cabeçalho `Location` apontando para o novo ID.
		- Trate o DELETE para retornar HTTP `204 No Content`.
		- Trate as buscas e relatórios com HTTP `200 OK`.

		- Anotações do Spring Boot e Lombok:
		- Inclua todas as anotações de Controller (`@RestController`, `@RequestMapping`, `@CrossOrigin` se aplicável).
		- Adicione as anotações de validação (`@Valid`) nos corpos de requisição (`@RequestBody`) e nos parâmetros de rota (`@PathVariable`).

		- Documentação Completa do Swagger / OpenAPI 3:
		- Adicione todas as anotações do Swagger (`@Tag`, `@Operation`, `@ApiResponse`, `@ApiResponses`).
		- Mapeie e documente as respostas de sucesso (200, 201, 204) e as respostas padrão de erro (400 Bad Request, 404 Not Found, 500 Internal Error) em cada endpoint.
		- Para as rotas com paginação (`Pageable`), adicione a anotação correta (ex: `@ParameterObject` do springdoc) para que o Swagger exiba os campos `page`, `size` e `sort` de forma legível na interface visual.

		- Importações (Imports):
		- Liste TODOS os `import`s necessários no topo de cada classe para que eu só precise copiar e colar o código no meu projeto sem erros de compilação.

		### 3. FORMATO DA SAÍDA

		Forneça os códigos completos das 3 classes Controller Java (`LaboratoryController`, `GrowerController` e `FarmsteadController`), divididos em blocos de código independentes, com comentários explicativos nos pontos onde decisões importantes foram tomadas.
	```
	- O meu por estar em modo Mentor acabou gerando somente o primeiro como base e então repliquei para os demais.
	- Tambem usei o proprio corretor de warnings para ajustar algumas warnings que mostravam por ser Null Safe.
	- Pedi para a IA gerar alguns DTO e corrigir uns que havia deixado um pouco de lado na construção dos services.
	- Chequei o Swagger para ver se estava tudo certo.


	- Percebi um erro de CORS, pedi para a IA gerar um arquivo de configuração basico de CORS.
	- Ao conectar o Frontend com o backend, debuggando percebi o caso de N+1 para listagem de produtores e então resolvi ele.


