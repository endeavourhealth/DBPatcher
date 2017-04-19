with triggers as
(
	select
		trg.tgname as trigger_name,
		case trg.tgtype::integer & 66
		when 2 then 'before'
	    	when 64 then 'instead of'
	    	else 'after'
	    end as trigger_type,
	    case trg.tgtype::integer & cast(28 as int2)
	    	when 4 then 'insert'
	    	when 8 then 'delete'
	    	when 12 then 'insert or delete'
	    	when 16 then 'update'
	    	when 20 then 'insert or update'
	    	when 24 then 'update or delete'
	    	when 28 then 'insert or update or delete'
	    end as trigger_event,
	    ns.nspname || '.' || c.relname as trigger_table,
	    case trg.tgtype::integer & 1
	    	when 1 then 'row'::text
	    	else 'statement'::text
	    end as trigger_level
	from pg_trigger trg
	inner join pg_class c on trg.tgrelid = c.oid
	inner join pg_namespace ns on c.relnamespace = ns.oid
	where trg.tgisinternal = False
)
select
	trigger_name,
	trigger_type,
	trigger_event,
	trigger_table,
	trigger_level,
	trigger_name || ' on ' || trigger_table as short_description,
	trigger_name || ' ' || trigger_type || ' ' || trigger_event || ' on ' || trigger_table || ' for each ' || trigger_level as description
from triggers;