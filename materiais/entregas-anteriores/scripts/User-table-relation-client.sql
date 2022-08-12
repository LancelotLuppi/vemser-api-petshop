alter table cliente 
add column id_usuario numeric,
add constraint fk_cliente_usuario foreign key (id_usuario) references usuario (id_usuario);