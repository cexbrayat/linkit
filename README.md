## Get "conf/secrets.conf" from us.

## Execution
    play deps --sync
    play run

## Deploy CloudBees TEST
    play bees:app:deploy --%cloudbeestest


## Deploy CloudBees PROD
    play bees:app:deploy --%cloudbees
