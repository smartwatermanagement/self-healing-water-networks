create table aggregations (id int auto_increment, aggregation_name text not null, issue_count int default 0, parent_id int, primary key (id) , foreign key (parent_id) references aggregations(id));

create table assets (id int auto_increment, type enum('storage', 'source', 'outlet', 'pump', 'recycling_plant', 'connection') not null, issue_count int default 0, latitude double not null, longitude double not null, primary key(id), parent_aggregation_id int, foreign key (parent_aggregation_id) references aggregations(id));

create table asset_property_value_map(id int auto_increment, property varchar(255) not null, value text not null, asset_id int not null, primary key (id), foreign key(asset_id) references assets(id));

create table connections(id int auto_increment, from_id int not null, to_id int not null, primary key(id), foreign key (from_id) references assets(id), foreign key (to_id) references assets(id));

create table sensors(id int, asset_id int not null, primary key(id), foreign key(asset_id) references assets(id));

create table thresholds (id int auto_increment, asset_id int not null, property varchar(255) not null, operator varchar(255) not null, value text not null, primary key (id), foreign key (asset_id) references assets(id));
 
create table issues(id int auto_increment , asset_id int, aggregation_id int, type enum('threshold_breach', 'leak', 'water_requirement_prediction', 'water_garden') not null, status enum('new', 'in_progress', 'resolved') not null default 'new', details text, created_at timestamp, updated_at timestamp, primary key (id), foreign key (asset_id) references assets(id), foreign key (aggregation_id) references aggregations(id));

create table users(id int auto_increment , name text not null, phone_number varchar(255), username varchar(255) not null unique, password varchar(255) not null, primary key (id));

create table subscriptions(id int auto_increment, issueType enum('threshold_breach', 'leak', 'water_requirement_prediction', 'water_garden') not null, user_id int, aggregation_id int, primary key (id), foreign key (user_id) references users(id), foreign key (aggregation_id) references aggregations(id)); 

create table notifications(id int auto_increment, read_status boolean default false, primary key (id), user_id int, issue_id int, foreign key (user_id) references users(id), foreign key (issue_id) references issues(id));


 
