CREATE TABLE medicos (
    id bigint not null auto_increment,
    nome varchar(100) not null,
    email varchar (100) not null unique,
    crm varchar (6) not null unique,
    especialidade VARCHAR(100) NOT NULL,
    logradouro VARCHAR(100) NOT NULL,
    bairro VARCHAR (100) NOT NULL,
    cep VARCHAR (9) NOT NULL ,
    complemento VARCHAR (100),
    numero VARCHAR(20) NOT NULL,
    uf VARCHAR(2) NOT NULL ,
    cidade VARCHAR(100) NOT NULL ,

    PRIMARY KEY (id)
)