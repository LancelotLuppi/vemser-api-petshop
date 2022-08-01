create table usuario(
    id_usuario numeric not null,
    id_cliente numeric,
    username text unique not null,
    senha text not null,
    primary key(id_usuario)
);

create sequence seq_usuario
increment 1
start 1;

create table cargo (
    id_cargo numeric not null,
    nome text unique not null,
    primary key(id_cargo)
);
create sequence seq_cargo
increment 1
start 1;
  
insert into cargo (id_cargo, nome)
values (nextval('seq_cargo'), 'ROLE_ADMIN'); -- 1

insert into cargo (id_cargo, nome)
values (nextval('seq_cargo'), 'ROLE_USER'); -- 2

insert into cargo (id_cargo, nome)
values (nextval('seq_cargo'), 'ROLE_TOSADOR'); -- 3

insert into cargo (id_cargo, nome)
values (nextval('seq_cargo'), 'ROLE_ATENDENTE'); -- 4

create table user_cargo (
    id_usuario numeric not null,
    id_cargo numeric not null,
    primary key(id_usuario, id_cargo),
    constraint fk_usuario_cargo_cargo foreign key (id_cargo) references cargo (id_cargo),
  	constraint fk_usuario_cargo_usuario foreign key (id_usuario) references usuario (id_usuario)
);
