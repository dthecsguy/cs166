select distinct pid
from parts
where cost < 10;

select pname
from parts
inner join catalog
on parts.pid = catalog.pid and parts.cost < 10;

select address
from suppliers
inner join catalog
on suppliers.sid = catalog.sid and suppliers.sname = "Fire Hydrant Cap";

select sname
from suppliers
inner join catalog
on suppliers.sid = catalog.sid and parts.color = "green";

select suppliers.sname, parts.pname
from suppliers
inner join catalog;

