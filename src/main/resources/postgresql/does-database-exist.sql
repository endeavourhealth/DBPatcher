select
	case
		when d.datname is not null then true
		else false
	end as database_exists
from (select 1) a
left outer join pg_database d on d.datname = ''{0}'';