set referential_integrity false;  -- hsqldb, h2
-- set foreign_key_checks = false;  -- mysql

-- Tables used in domain "examples"
drop table if exists Classification cascade;
drop table if exists Person cascade;
drop table if exists DatesExample cascade;

-- Tables used in domain "security"
drop table if exists UserRole cascade;
drop table if exists Users cascade;
drop table if exists Role cascade;
drop table if exists UserPassword cascade;

-- Tables used in domain "workout"
drop table if exists Student cascade;
drop table if exists Teacher cascade;
drop table if exists Department cascade;
drop table if exists Building cascade;
drop table if exists Room cascade;
drop table if exists StringThing cascade;

set referential_integrity true;  -- hsqldb, h2
-- set foreign_key_checks = true;  -- mysql

commit;
