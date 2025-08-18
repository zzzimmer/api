## Aula 01

- Criar um projeto Spring Boot utilizando o site do Spring Initializr;
- Importar o projeto no IntelliJ e executar uma aplicação Spring Boot pela 
classe contendo o método main;
- Criar uma classe Controller e mapear uma URL nela utilizando as anotações 
@RestController e @RequestMapping;
- Realizar uma requisição de teste no browser acessando a URL mapeada no Controller.

## Aula 02

Mapear requisições POST em uma classe Controller;
Enviar requisições POST para a API utilizando o Insomnia;
Enviar dados para API no formato JSON;
Utilizar a anotação @RequestBody para receber os dados do corpo da requisição em um parâmetro no Controller;
Utilizar o padrão DTO (Data Transfer Object), via Java Records, para representar os dados recebidos em uma requisição POST.

## Aula 03

- Validação e persistência via modulos do Spring
  - Feita através do módulo Validation, integrado com o BeanValidation do Java.
  - Essa validação ocorre dentro dos campos que estão recebendo dados da API. 
    - Essa validação é expressa via notação dentro do record, como MedicoDTO
      onde só é necessário marcar quais tipos de validação você deseja. 
      @NotNull, @NotBlank, @Email, @Pattern (regexp) @Valid
- Migração via FlyWay
  - Alem da migração inicial, se realizou a adição do campo telefone na tabela Medico.
  Se alterou o DTO, adicionando o campo e a classe Medico (e construtor).
  - A migration funciona de maneira imutável, desta forma, deve-se criar uma nova
    para cada alteração. O Log do spring sempre apresenta informações da migration
  - Caso tenha algum problema de migration, existem recursos como:
  delete from flyway_schema_history where success = 0;
  - 