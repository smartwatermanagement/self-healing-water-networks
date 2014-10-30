create table aggregations ( id int, issue_count int default 0, parent_id int, primary key (id), foreign key (parent_id) references aggregations(id));

create table assets (id int, type enum('source', 'sink', 'storage', 'pump', 'pipe', 'recycling_plant'), issue_count int default 0, property varchar(255), value text, parent_association_id int, primary key (id), foreign key (parent_association_id) references aggregations(id));

create table connections(from_id int, to_id int, property varchar(255), value text, primary key(from_id, to_id), foreign key (from_id) references assets(id), foreign key (to_id) references assets(id));
 
create table thresholds (id int, asset_id int, property varchar(255) not null, operator varchar(255) not null, value text not null, primary key (id), foreign key (asset_id) references assets(id));
 
create table issues(id int, asset_id int, aggregation_id int, type enum('thresholdbreach', 'leak', 'water-requirement-prediction', 'water-garden') not null, status enum('new', 'in-progress', 'resolved') not null default 'new', details text, created_at timestamp default current_timestamp, updated_at timestamp, primary key (id), foreign key (asset_id) references assets(id), foreign key (aggregation_id) references aggregations(id));

create trigger on_update_issues after update on issues

create table users(id int, name text not null, phone_number varchar(255), username varchar(255) not null unique, password varchar(255) not null, primary key (id));

create table subscriptions(id int, issueType enum('thresholdbreach', 'leak', 'water-requirement-prediction', 'water-garden') not null, user_id int, aggregation_id int, primary key (id), foreign key (user_id) references users(id), foreign key (aggregation_id) references aggregations(id)); 

create table notifications(id int, read_status boolean default false, primary key (id), user_id int, issue_id int, foreign key (user_id) references users(id), foreign key (issue_id) references issues(id));


 
