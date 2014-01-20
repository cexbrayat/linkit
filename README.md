First: get "conf/secrets.conf" from us.

## Execution

````
    play deps --sync
    play run
````

## Deploy CloudBees TEST

````
    play evolutions:apply --%cloudbeestest
    play bees:app:deploy --%cloudbeestest
````

## Deploy CloudBees PROD

If you're impacting DB with DDL SQL scripts, it's a good idea to take a snapshot of DB (to be able to restore it in case of dramatic failure) :

- Go to https://grandcentral.cloudbees.com/
- Log in
- Choose "mixitdatabase" DB
- Click (camera icon) "create snasphot"

````
    play evolutions:apply --%cloudbees
    play bees:app:deploy --%cloudbees
`````
