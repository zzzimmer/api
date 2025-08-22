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

- Aula 4 - Video 5 - Uso da variavel de ambiente para secret.
  Para aprimorar a segurança da aplicação, integramos uma biblioteca externa dedicada à geração e ao gerenciamento de 
tokens de autenticação. Toda a nova lógica foi centralizada no pacote infra.security, que agora contém as classes 
responsáveis pela estratégia de geração de tokens, a configuração do encoder e o DTO para o tráfego dos dados de 
acesso. A fim de suportar esta nova funcionalidade, também foi criada uma migration para o banco de dados. Por fim, 
desenvolvemos o novo endpoint de login, que consome toda essa infraestrutura para validar as credenciais do usuário e
associá-lo a um token de acesso.


## Aula 05
- Criar uma classe que intercepta as requisições antes delas chegarem ao MedicoController. Um *filter*
para todas as requisições
-  Para tal, utilizamos a classe que recebe todas as requisicoes no spring, a DispatcherServlet
- ![Captura de Tela 2025-08-18 às 18.04.51.png](../../../../var/folders/kj/9kfynl_12_9628kh01p7nz5w0000gn/T/TemporaryItems/NSIRD_screencaptureui_FUwXHv/Captura%20de%20Tela%202025-08-18%20%C3%A0s%2018.04.51.png)
- Deve-se criar um filter ou um interceptor
- https://cursos.alura.com.br/course/spring-boot-aplique-boas-praticas-proteja-api-rest/task/117194
- Desta forma, filtrar requisições:
  - O filtro é uma classe com componentes e herança.
  - @Component
    public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    filterChain.doFilter(request, response);
    }
    }
  - Esse filtro precisa ter uma logica implementada. Este, é chamado a cada request, como diz ali no extends
- A logica interna:
  - Essa logica vai no cabeçalho authorization. 
  - Deve-se fazer uma validação se o Token foi gerado dentro dessa API mesmo