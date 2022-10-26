select distinct pid
from catalog
where cost < 10;

select pname
from parts
inner join catalog
on parts.pid = catalog.pid
where catalog.cost < 10;

select address
from supplies
inner join catalog
on suppliers.sid = catalog.sid
inner join parts
on parts.pid = catalog.pid
where parts.pname = 'Fire Hydrant Cap';

select sname
from suppliers
inner join catalog
on suppliers.sid = catalog.sid
inner join parts
on parts.pid = catalog.pid
where parts.color = 'green';

select distinct suppliers.sname, parts.pname
from suppliers
inner join catalog
on suppliers.sid = catalog.sid
inner join parts
on parts.pid = catalog.pid;

