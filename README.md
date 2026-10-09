# 🌱 Agrotis Challenge API

API REST desenvolvida em **Java 17** e **Spring Boot 3.4.x** para o gerenciamento e integração de produtores rurais (`Growers`), propriedades rurais (`Farmsteads`) e laboratórios de análise (`Laboratories`), contemplando regras de negócio complexas, relatórios consolidados e validações fiscais customizadas (CPF/CNPJ).

---

## 🚀 Tecnologias e Ferramentas Utilizadas

* **Linguagem:** Java 17
* **Framework:** Spring Boot 3.4.x
  * **Spring Web:** Construção da API RESTful
  * **Spring Data JPA:** Persistência e mapeamento objeto-relacional
  * **Spring Validation:** Validação declarativa de payloads (`@Valid`)
* **Banco de Dados:** PostgreSQL / H2 (para testes de integração)
* **Documentação:** Springdoc OpenAPI 3 (Swagger UI)
* **Testes Automatizados:** JUnit 5, Mockito e MockMvc (Testes unitários e de camada Web)
* **Utilitários:** Lombok

---

## ⚙️ Arquitetura e Decisões de Design

* **Camadas Bem Definidas:** O projeto segue uma arquitetura em camadas (`Controller`, `Service`, `Repository`, `Entity` e `DTO`), promovendo baixo acoplamento e alta testabilidade.
* **Mapeamento com DTOs e Projeções:** Uso intensivo de DTOs de entrada e saída para isolar o modelo de domínio da API, além de *Interface Projections* para otimização de consultas de relatórios financeiros e gerenciais.
* **Validação de Documentos Customizada:** Implementação de anotações personalizadas (`@CpfOrCnpj`) com algoritmos nativos de validação matemática de dígitos verificadores da Receita Federal.
* **Padronização de Respostas Paginadas:** Envelope genérico `PaginatedResponse<T>` para padronizar as listagens baseadas em `Pageable` do Spring Data.

---

## 🔌 Documentação da API (Swagger UI)

Com a aplicação em execução, você pode acessar a documentação interativa da API (Swagger) através do navegador:

> **URL local:** `http://localhost:8080/swagger-ui/index.html`

A interface permite visualizar todos os endpoints disponíveis (`/api/v1/growers`, `/api/v1/farmsteads`, `/api/v1/laboratories`), contratos de DTOs e realizar requisições de teste diretamente.

---

## 📦 Como Executar o Projeto Localmente

### Pré-requisitos
* **Java 17** instalado e configurado na sua máquina.
* **Maven 3.8+** instalado.

### 1. Clonar o repositório
```bash
git clone [https://github.com/ThiagoIvens/agrotis-test-backend.git](https://github.com/ThiagoIvens/agrotis-test-backend.git)
cd agrotis-challenge