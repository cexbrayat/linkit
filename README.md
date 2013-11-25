First: get "conf/secrets.conf" from us.

## Execution

    play deps --sync
    play run

## Deploy CloudBees TEST

    play evolutions:apply --%cloudbeestest
    play bees:app:deploy --%cloudbeestest

## Deploy CloudBees PROD

    play evolutions:apply --%cloudbees
    play bees:app:deploy --%cloudbees
