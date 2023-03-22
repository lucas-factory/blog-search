create table keyword
(
    id         int unsigned auto_increment,
    created_at timestamp    not null,
    updated_at timestamp    not null,
    count      int unsigned,
    text       varchar(255) not null,
    primary key (id)
);

create index idx_text_updated_at on keyword (text, updated_at desc);
create index idx_count_updated_at on keyword (count desc , updated_at desc);