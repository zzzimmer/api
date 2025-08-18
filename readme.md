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
  
## Aula 04

- Implementação do método de listagem de Médicos, paginada e ordenada por nome.
- Cria-se o DTO ListagemMedico para devolver dados controlados ao controller de listagem
- Pode ter uma unica URL chamada por diferentes métodos HTTP
- Deve-se sempre utilizar DTO's para dados que chegam e saem da API.
- https://cursos.alura.com.br/course/spring-boot-3-desenvolva-api-rest-java/task/116068
- Paginação
  - Limitar a requisição de registros visando performance.
  - Se usa a interface Page, a qual é retornada pelo método listar, que passa a receber 
    Pageable paginacao. Dessa forma, ao buscar findAll, não é mais necessário usar o stream anterior.
  O método fica com essa cara:
  - @GetMapping("/listar")
    public Page<ListagemMedico> listar(Pageable paginacao){
    return medicoRepository.findAll(paginacao).map(m -> new ListagemMedico(m));
    }
  - Era: 
  - @GetMapping("/listar")
    public List<ListagemMedico> listar(){
    return medicoRepository.findAll().stream().map(m -> new ListagemMedico(m))
    .collect(Collectors.toList());
    }
  - O front-end usa isso através do endereço de requisição:
    localhost:8080/medicos/listar?size=1&page=1
    size e page tem funções, precisa pesquisar. Mas basicamente, agora você retorna
    um objeto Page, com novos campos no JSON de retorno indicando sobre essa paginação.
  
- Ordenação
  - Através de sort dentro da url
  - localhost:8080/medicos/listar?sort=crm ou localhost:8080/medicos/listar?sort=name
  - A ordenação pode ir de asc ou desc 
    - localhost:8080/medicos/listar?sort=crm,dsc
  - Pode-se combinar ordenação e paginação. 
  - Ademais, pode usar a notação @PageableDefault(siz =10, page =0, sort= {"name"})
    para enviar diretamente uma organização dos dados:
  - 