create function get_person(person_id integer)
returns integer as
$$
begin
  return person_id;
end;
$$ language plpgsql;