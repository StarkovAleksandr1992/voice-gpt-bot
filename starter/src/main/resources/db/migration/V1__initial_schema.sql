create table if not exists chat_info
(
    chat_id bigint not null,
    status  varchar(255),
    primary key (chat_id)
);
create table if not exists chat_messages
(
    chat_db_model_chat_id bigint not null,
    chat_message          varchar(4000),
    chat_role             varchar(255)
);

create table if not exists customer_info
(
    customer_id      bigint       not null,
    telegram_id      bigint       not null unique,
    token_account_id bigint unique,
    action_type      varchar(255),
    first_name       varchar(255) not null,
    gpt_model        varchar(255),
    language_code    varchar(255),
    last_name        varchar(255),
    state            varchar(255),
    primary key (customer_id)
);

create table if not exists customer_info_chats
(
    chats_chat_id                 bigint not null unique,
    customer_db_model_customer_id bigint not null
);

create table if not exists customer_request_info
(
    token_cost          numeric(38, 0),
    customer_id         bigint,
    customer_request_id bigint not null,
    request_data_id     bigint unique,
    result_data         varchar(4000),
    state               varchar(255),
    primary key (customer_request_id)
);

create table if not exists request_data_info
(
    voice_request_duration   integer,
    request_data_id          bigint      not null,
    data_type                varchar(31) not null,
    prompt                   varchar(1450),
    string_data              varchar(2550),
    recognized_voice_request varchar(4000),
    byte_data                bytea,
    primary key (request_data_id)
);

create table if not exists token_account_info
(
    balance          numeric(38, 0),
    token_account_id bigint not null,
    primary key (token_account_id)
);


alter table if exists chat_messages
    add foreign key (chat_db_model_chat_id)
        references chat_info;

alter table if exists customer_info
    add foreign key (token_account_id)
        references token_account_info;

alter table if exists customer_info_chats
    add foreign key (chats_chat_id)
        references chat_info;

alter table if exists customer_info_chats
    add foreign key (customer_db_model_customer_id)
        references customer_info;

alter table if exists customer_request_info
    add foreign key (customer_id)
        references customer_info;

alter table if exists customer_request_info
    add foreign key (request_data_id)
        references request_data_info;

create sequence if not exists customer_info_seq;
alter sequence if exists customer_info_seq owner to postgres;

create sequence if not exists customer_request_info_seq;
alter sequence customer_request_info_seq owner to postgres;

create sequence if not exists request_data_info_seq;
alter sequence request_data_info_seq owner to postgres;

create sequence if not exists recognized_text_info_seq;
alter sequence recognized_text_info_seq owner to postgres;

create sequence if not exists gpt_handled_recognized_text_info_seq;
alter sequence gpt_handled_recognized_text_info_seq owner to postgres;

create sequence if not exists chat_info_seq;
alter sequence chat_info_seq owner to postgres;

create sequence if not exists token_account_info_seq;
alter sequence token_account_info_seq owner to postgres;
