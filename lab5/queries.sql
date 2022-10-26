select distinct pid
from catalog
where cost < 10;

select pname
from parts
inner join catalog
on parts.pid = catalog.pid and catalog.cost < 10;

select address
from suppliers
inner join parts
on parts.sname in ('Fire Hydrant Cap')
inner join catalog
on suppliers.sid = catalog.sid;

select sname
from suppliers
inner join catalog
on suppliers.sid = catalog.sid
inner join parts
on parts.color in ('green');

select suppliers.sname, parts.pname
from suppliers
inner join catalog
on suppliers.sid = catalog.sid
inner join parts
on parts.pid = catalog.pid;

