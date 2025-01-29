create table Sensor(
id serial primary key,
"name" varchar(30) not null unique,
created_at timestamp not null
)

create table Measurement(
id serial primary key,
sensor_name varchar(30) references sensor("name") on delete set null,
value numeric(3, 1) check (value >= -100 and value <= 100) not null,
raining boolean not null,
created_at timestamp not null
)