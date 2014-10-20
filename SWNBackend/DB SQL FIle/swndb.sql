create table aggregations ( id int, issue_count int default 0, parent_id int, primary key (id), foreign key (parent_id) references aggregations(id));

create table assets (id int, type enum('source', 'sink', 'storage', 'pump', 'pipe', 'recycling_plant'), issue_count int default 0, property varchar(255), value text, parent_association_id int, primary key (id), foreign key (parent_association_id) references aggregations(id));

create table connections(from_id int, to_id int, property varchar(255), value text, primary key(from_id, to_id), foreign key (from_id) references assets(id), foreign key (to_id) references assets(id));
 
create table thresholds (id int, asset_id int, property varchar(255) not null, operator varchar(255) not null, value text not null, primary key (id), foreign key (asset_id) references assets(id));
 
create table issues(id int, asset_id int, aggregation_id int, type enum('level', 'quality', 'leak', 'shortage', 'water-requirement') not null, status enum('new', 'in-progress', 'resolved') not null default 'new', details text, primary key (id), foreign key (asset_id) references assets(id), foreign key (aggregation_id) references aggregations(id));

create table user(id int, name text not null, phone_number varchar(255), username varchar(255) not null unique, password varchar(255) not null, primary key (id));
 
