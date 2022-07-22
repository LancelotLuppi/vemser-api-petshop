create table cliente(
id_cliente numeric(11) unique not null,
nome text not null,
quantidade_pedidos numeric(3) not null,
valor_pagamento numeric(5, 2), --99999,99
primary key (id_cliente)
);

create table contato(
id_contato numeric(11) not null,
id_cliente numeric(11) not null,
telefone varchar(14) not null,
descricao text not null,
constraint pk_contato_id_contato primary key (id_contato, id_cliente),
constraint fk_contato_id_cliente foreign key (id_cliente) references cliente (id_cliente)
);

create table endereco(
id_endereco numeric(11) not null,
id_cliente numeric(11) not null,
logradouro text not null,
numero numeric(8) not null,
cep char(9) not null,
cidade text not null,
bairro text not null,
complemento text not null,
constraint pk_endereco_id_endereco primary key (id_endereco, id_cliente),
constraint fk_endereco_id_cliente foreign key (id_cliente) references cliente (id_cliente)
);

create table animal(
id_animal numeric(11) not null,
id_cliente numeric(11) not null,
nome text not null,
tipo text not null,
raca text not null,
pelagem numeric(1) not null,
porte numeric(1) not null,
idade numeric(3), 
constraint pk_animal_id_animal primary key (id_animal, id_cliente),
constraint fk_animal_id_cliente foreign key (id_cliente) references cliente (id_cliente)
);

create table pedido(
id_pedido numeric(11) not null,
id_cliente numeric (11) not null,
id_animal numeric(11) not null,
servico text not null,
valor numeric(5, 2) not null,
descricao text not null,
status text not null,
data_e_hora_gerada timestamp,
constraint pk_pedido_id_pedido primary key (id_pedido),
constraint fk_pedido_id_cliente foreign key (id_cliente) references cliente(id_cliente),
constraint fk_pedido_id_animal foreign key (id_animal, id_cliente) references animal(id_animal, id_cliente)
);

create sequence seq_id_cliente
increment 1
start 1;

create sequence seq_id_contato
increment 1
start 1;

create sequence seq_id_endereco
increment 1
start 1;

create sequence seq_id_animal
increment 1
start 1;

create sequence seq_id_pedido
increment 1
start 1;

alter table cliente 
add column email text;