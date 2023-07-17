# Car Rent

## Link de acesso API:
https://car-rent-production.up.railway.app/

## Como Rodar

O projeto utiliza um banco de dados PostgreSQL. Para executar localmente, siga as etapas abaixo:

1. Abra o arquivo de configuração: **"Car-Rent\src\main\resources\application.properties"**.

2. Crie uma base de dados no PostgreSQL com o nome **car-rent**.

3. Modifique as seguintes configurações com as informações do seu banco de dados PostgreSQL:
   - ${DB_NAME}: Nome Banco de Dados PostgreSQL (car-rent)
   - ${DB_USERNAME}: Usuário PostgreSQL
   - ${DB_PASSWORD}: Senha PostgreSQL
     
```
## Conexão com o banco de dados

spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/${DB_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

```

4. Configurando Mail Sender:
    - ${MAIL_USERNAME}: Gmail que irá enviar as notificações
    - ${MAIL_PASSWORD}: Senha do Gmail 
        - Utilizar preferencialmente App Password: https://myaccount.google.com/apppasswords
        - Em nome do aplicativo coloque smtp.gmail.com

    - Caso não queira configurar isso manualmente, sinta se a vontade para usar esse email e senha:
        - ${MAIL_USERNAME}: noreply.carrent@gmail.com
        - ${MAIL_PASSWORD}: wzokbzfehpbkmplj 

```
## Mail Sender

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

```

5. No terminal, execute o seguinte comando para construir o projeto:

```bash
mvn clean install

mvn spring-boot:run
```

Parabéns a aplicação subiu localmente na porta 8080.

## Descrição da API

A API Car Rent é uma solução de controle de aluguel de veículos que permite aos usuários listar carros disponíveis, gerenciar suas contas e realizar aluguéis. Com essa API, é possível obter informações detalhadas sobre os veículos disponíveis, como modelo, marca, ano e preço de aluguel. Os usuários podem interagir com a API para visualizar as opções de carros, verificar a disponibilidade, calcular os custos e realizar reservas de forma conveniente e eficiente. A API Car Rent facilita o processo de aluguel de carros, tornando-o mais acessível e eficaz para os usuários.

## Tecnologias utilizadas

- Java
- Spring Boot
- Spring Data JPA (Interface de repositório)
- Spring Security (JWT Token)
- PostgreSQL (Banco de Dados)
- JUnit (Testes unitários)

## Organização Banco de Dados

![ER Diagram](/ER%20Car%20Rent.png)
