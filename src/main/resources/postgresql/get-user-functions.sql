select
	n.nspname || '.' || proname || '(' || oidvectortypes(proargtypes) || ')' as function_signature
from pg_proc p
inner join pg_namespace n on p.pronamespace = n.oid
left outer join pg_type t on p.prorettype = t.oid
where n.nspname not in
(
	'pg_catalog',
	'information_schema'
)
and coalesce(t.typname, '') != 'trigger'
and p.probin is null
order by n.nspname, proname;
