## Aula 01 - Boas práticas de API

- Padronizar os retornos de API
  - Inicio de uso do ResponseEntity
  - O retoro de um método cadastar requer um 201. O qual por sí, precisa de um cabeçalho e caminho para o acesso dos dados
  pelo front-end. 
    - Dessa forma, se aprende a encapsular o endereço da API, visto que nao roda sempre em localhost via URI.
    - O método cadastro devolve 201, cabeçalho location com URI e corpo da resposta uma representação do recurso novo:
    - 
      - @PostMapping("/cadastrar") // precisa retornar o 201
        @Transactional // explicitar transação
        public ResponseEntity cadastrar(@RequestBody @Valid MedicoDTO json, UriComponentsBuilder uriBuilder){
        var medico = new Medico(json);
        medicoRepository.save(new Medico(json));

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
        }
- Essa questão de cadastro, é interessante conhecer sobre os estados transient e managed dos objetos. Pode existir
  problemas nesse sentido.

- Detalhando dados na API
  - Nessa aula, você aprendeu como:
    Utilizar a classe ResponseEntity, do Spring, para personalizar os retornos dos métodos de uma classe Controller;
    Modificar o código HTTP devolvido nas respostas da API;
    Adicionar cabeçalhos nas respostas da API;
    Utilizar os códigos HTTP mais apropriados para cada operação realizada na API.

## Aula 02 - Lidando com Erros
- O tratamento do erro convem, entre tantas coisas, para ocultar informações sensíveis do funcionamento da API.
  - Essas respostas mais apropriadas podem apenas remover a propriedade trace do padrão do Spring.
  - Para devolve outra coisa nos erros, deve-se mexer no aplicaiton properties.
    - A documentação do Spring Boot Properties tem uma seção sobre os erros retornados. Em Server Properties, a propriedade
      server.error.include-stacktrace = never resolve esse problema.
  - Cria-se uma classe que trata o erro 501 para 404:
    - @ExceptionHandler(EntityNotFoundException.class)
      public ResponseEntity tratarErro404(){
      return ResponseEntity.notFound().build();
      }
  - Para o erro 400
  - Traduzir campos de erro:
    - https://cursos.alura.com.br/course/spring-boot-aplique-boas-praticas-proteja-api-rest/task/122117
    - https://cursos.alura.com.br/course/spring-boot-aplique-boas-praticas-proteja-api-rest/task/117181


## Aula 03 - Autorização e Autenticação - Implementação StateLess

- Introdução ao Modulo Spring Security
  - Ajuda com o controle de autenticação
  - Autorização de cada usuario para o *acesso* de determinados URL
  - Proteção contra conjunto de ataques padrão (CSRF, clickjacking,etc)
- Na API, faremos controle de *autenticação* e *acesso*
  - Através de Tokens JSWT
    - Se escolho isso para não ficar transitando com senha e user a cada requisição.
      - "A principal diferença é que, com tokens, o servidor não precisa manter nenhum estado sobre o usuário. 
      Cada requisição é independente e contém todas as informações necessárias para ser processada. 
      Isso torna a API mais escalável e fácil de manter, pois não há necessidade de gerenciar sessões em memória."
      - https://cursos.alura.com.br/course/spring-boot-aplique-boas-praticas-proteja-api-rest/task/117184
  - Como isso funciona?
    - aplicação cliente faz Login -> requisição para o backend um json com corpo log e senha -> busca no banco os dados de log e senha na 
    tabela usuarios (SELECT para validade dos dados) -> sendo valido: API gera um token JWT -> devolve o token para a 
    aplicação cliente;
    - O Token é guardado pela aplicação front/mobile. O token identifica que o user ta logado. 
    - Tend o Token, o front/mobile dispara requisições que, além de enviar o JSON dos dados (CRUD) mas também envia um 
    cabeçalho chamado Authorization onde esta o Token obtido anteriormente. 
      - Todas as URL ou requisições com proteção, deve-se validar o cabeçalho e seu Token. 

- Conjunto de implementações de Spring Sec
  - Nova tabela de usuarios com user e senha
    - Migration, Entidade JPA ligadas a USER, com notações e ETC
    - https://cursos.alura.com.br/course/spring-boot-aplique-boas-praticas-proteja-api-rest/task/117185
    - Repository e Service 
      - A classe service contem a lógica de autorização do user, acessando repository. 
    - Ensinar ao spring qual o processo desejado de segurança:
      - Mudar do StateFull(Padrao do spring) para StateLess.
      - Essa configuração envolve classes e recursos do Spring, via código Java. Abaixo, classe SecurityConfigurations
      - Se desabilita a proteção vde CSRF - visto que Token ja trata isso.
      - e escolhe a politica de seção stateless
      - @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .build();
        }
    - Criação de Controller. -- Longo procedimento. 
      - Implementar o tratamento da requisição de usuario no sistema

Nesta aula:
Funciona o processo de autenticação e autorização em uma API Rest;
Adicionar o Spring Security ao projeto;
Funciona o comportamento padrão do Spring Security em uma aplicação;
Implementar o processo de autenticação na API, de maneira Stateless, utilizando as classes e configurações do Spring Security.


    

- Diferença entre StateFull e StateLess
  - StateFull: O servidor guarda a sessão e identifica esta para cada usuario. Mantito pelo servidor
  - StateLess: Sem guardar estados, sem sessão de dados armazenados no servidor. Cada requisição inicia e morre. 
  



  
## Aula 04 JSON Web Token

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
  - https://cursos.alura.com.br/course/spring-boot-3-desenvolva-api-rest-java/task/116069
- Nesta aula:
- Utilizar a anotação @GetMapping para mapear métodos em Controllers que produzem dados;
  Utilizar a interface Pageable do Spring para realizar consultas com paginação;
  Controlar a paginação e a ordenação dos dados devolvidos pela API com os parâmetros page, size e sort;
  Configurar o projeto para que os comandos SQL sejam exibidos no console.

## Aula 05
- Requisições PUT
- O Figma pode ajudar a visualizar os requisitos do sistema para a implementação
- Em geral, é necessário devolver o id nas listagens para usar. 
- Deve-se utilizar um DTO adequado para o PUT, visto as restrições de negócio
- Para o seguinte metodo:
- @PutMapping //usar url padrao
  @Transactional
  public void atualizar (@RequestBody @Valid AtualizarMedico json){
  var medico = medicoRepository.getReferenceById(json.id());
  medico.atualizarInformacoes(json);
  }
- Observar que os métodos atualizarInformações (Dentro da classe tem outro) é que mexem nos dados vindos
do JSON. O Repository apenas faz um "get" do Objeto selecionado. 
- https://cursos.alura.com.br/course/spring-boot-3-desenvolva-api-rest-java/task/116073
- https://cursos.alura.com.br/course/spring-boot-3-desenvolva-api-rest-java/task/116074
  

- Delete - Exclusão lógica e Exclusão real
  - A exclusão no front vai e listagem -> registro -> desativar perfil. Ou seja, é uma exclusao logica
  - Para a exclusao tradicional:
    -  @DeleteMapping("/{id}")
       @Transactional
       public void excluir (@PathVariable Long id){
       medicoRepository.deleteById(id);
       }
  - A exclusão necessita de uma nova migration na tabela, a qual
  adiciona uma coluna que referencia status de ativo do médico, um
  tinyInt. 
  No projeto, é a migration V3. 
  - Tem-se um novo listagem:
  - @GetMapping("/listar")
    public Page<ListagemMedico> listar(@PageableDefault(size = 10, sort ={"nome"} ) Pageable paginacao){
    return medicoRepository.findAllByAtivoTrue(paginacao).map(m -> new ListagemMedico(m));
    }
  - https://cursos.alura.com.br/course/spring-boot-3-desenvolva-api-rest-java/task/153864