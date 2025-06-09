ImobiFlow

ImobiFlow é uma aplicação web monolítica para gestão de imobiliárias, clientes, imóveis e negociações, desenvolvida em Java 17 com Spring Boot.

🎯 Visão Geral

CRUD completo de Usuários, Imobiliárias, Clientes, Imóveis e Negociações.

Autenticação e autorização via Spring Security (roles ADMIN e USER).

Integração com API ViaCEP para preenchimento automático de endereços.

Envio de e‑mails (recuperação de senha) via JavaMailSender.

Dashboard com indicadores e gráficos (Chart.js).

Isolamento user‑tenant: cada usuário acessa apenas seus próprios dados.

🛠️ Tech Stack

Linguagens: Java 17, HTML5, CSS3, JavaScript

Backend:

Spring Boot (MVC, Data JPA, Security, Validation, Mail, WebFlux)

Hibernate (JPA)

Spring Data JPA

Lombok

Maven

Frontend:

Thymeleaf

Bootstrap 5 + Bootstrap Icons

Animate.css

Chart.js

Banco de Dados: PostgreSQL

Migrações: Flyway

Testes: JUnit 5, Mockito, Spring Boot Starter Test

Controle de Versão: Git (Git Flow simplificado)

🚀 Pré-requisitos

Java 17+

Maven 3.6+

PostgreSQL 12+

Conta Gmail ou SMTP configurado para envio de e‑mail (opcional)

📦 Estrutura do Projeto

├── src
│   ├── main
│   │   ├── java/com/example/crud
│   │   │   ├── config         # Configurações Spring Boot e Security
│   │   │   ├── controllers    # Controllers MVC e REST
│   │   │   ├── services       # Lógica de negócio
│   │   │   ├── repositories   # Spring Data JPA
│   │   │   ├── domain         # Entidades JPA
│   │   │   ├── requests       # DTOs de requisição
│   │   │   └── integration    # Integrações externas (ViaCEP)
│   │   └── resources
│   │       ├── db/migration   # Scripts Flyway
│   │       ├── static         # CSS, imagens, JS
│   │       └── templates      # Thymeleaf templates
│   └── test                   # Testes unitários e de integração
└── pom.xml

🔒 Segurança

Senhas criptografadas com BCryptPasswordEncoder.

Autenticação via formulário customizado /login.

CSRF habilitado.

Roles: ROLE_ADMIN e ROLE_USER.

Isolamento de dados por usuário (user-tenant).

🔗 Integração ViaCEP

Serviço WebClient base URL: https://viacep.com.br/ws.

Endpoint interno: GET /api/cep/{cep} retorna JSON de endereço.

DTO: ViaCepResponse mapeia campos da API.

📈 Versionamento

Git Flow simplificado:

main: produção

develop: integração de features

feature/*: novas funcionalidades

bugfix/*: correções




