create or replace function log.get_message(message_id integer)
returns integer as
$$
begin
  return message_id;
end;
$$ language plpgsql;