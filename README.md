![Endeavour Logo](http://www.endeavourhealth.org/github/logo-text-left-cropped.png)

## Endeavour DB Patcher

Database (SQL) patching tool for Endeavour Health projects.

Supports Postgresql. MySQL support to be added.

### How to use

```
------------------------------------------------------------
DBPatcher v1.0
------------------------------------------------------------

 usage:  java -jar DBPatcher.jar DB_XML_FILE [options]
    or:  java -jar DBPatcher.jar ZIP_FILE [options]

 options:  --host=hostname     Override hostname in DB_XML_FILE
           --port=port         Override port in DB_XML_FILE
           --db=dbname         Override databasename in DB_XML_FILE
           --user=username     Override username in DB_XML_FILE
           --pass=password     Override password in DB_XML_FILE

           --xml=DB_XML_FILE   Specify DB_XML_FILE file name in root of
                               ZIP file; default database.xml
 
           --dropfns           Prompts to drop all functions before
                               applying fns in DB_XML_FILE
           --autodropfns       Automatically drops all functions before
                               applying fns in DB_XML_FILE

           --generatesamplexml Write sample DB_XML_FILE file
           --help              Print this message

```

### Sample database.xml

```
<Database>
    <DisplayName>sampledb</DisplayName>
    <DatabaseType>postgresql</DatabaseType>
    <Connection>
        <!-- note:  connection details passed via command line take precedence -->
        <Hostname>localhost</Hostname>
        <Port>5432</Port>
        <DatabaseName>sampledb</DatabaseName>
    </Connection>
    <Paths>
        <Schema>schema</Schema>
        <Functions>functions</Functions>
        <Scripts>scripts</Scripts>
    </Paths>
</Database>
```
