
# Prerequisites

Runs only with [Play Framework 1.2.4](http://downloads.typesafe.com/releases/play-1.2.4.zip).

If you want to deploy on TEST or PROD environments, you need to get `conf/secrets.conf` file from us (ask gently).

# Running locally

````
play deps --sync
play run
````

That will create a H2 DB in memory, with default static data from `conf/init-data.yml`, and programmatic dummy data from `app/controllers/PopulateOnStart.java`.

# Deploy on Cloud Foundry

- [Install the CloudFoundry client](https://github.com/cloudfoundry/cli#downloads)
- `cf login -a https://api.run.pivotal.io`
    - Choose `production` space.
    - If space not chosen, you can do it later with `cf target -s production`
- Retrieve the `conf/secrets.conf`
- Eventually run `play deps --sync`
- `play war -o ../mixit.war --%cloudfoundry`
- `cf push`

App runs on http://mix-it.fr, which is a DNS alias for http://mixit.cfapps.io/

If you're impacting DB with DDL SQL scripts, it's a good idea to make a database export before.
