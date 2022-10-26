select distinct pid
from catalog
where cost < 10;

select pname
from parts
inner join catalog
on parts.pid = catalog.pid and catalog.cost < 10;

select address
from suppliers
inner join parts, catalog
on suppliers.sid = catalog.sid and parts.sname in ('Fire Hydrant Cap');

select sname
from suppliers
inner join catalog, parts
on suppliers.sid = catalog.sid and parts.color in ("green");

select suppliers.sname, parts.pname
from suppliers
inner join catalog, parts
on suppliers.sid = catalog.sid;

