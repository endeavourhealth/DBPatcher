select
	n.nspname || '.' || proname || '(' || oidvectortypes(proargtypes) || ');' as proc_name
from pg_proc p
inner join pg_namespace n on p.pronamespace = n.oid
where n.nspname not in
(
	'pg_catalog',
	'information_schema'
)
order by n.nspname, proname;