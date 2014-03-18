
# Prerequisites

Runs only with [Play Framework 1.2.4](http://downloads.typesafe.com/releases/play-1.2.4.zip).

If you want to deploy on TEST or PROD environments, you need to get `conf/secrets.conf` file from us (ask gently).

# Execution

````
play deps --sync
play run
````

# Deploy on real environments

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
