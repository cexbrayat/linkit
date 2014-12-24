
# Prerequisites

Runs only with [Play Framework 1.2.4](http://downloads.typesafe.com/releases/play-1.2.4.zip).

If you want to deploy on TEST or PROD environments, you need to get `conf/secrets.conf` file from us (ask gently).

# Execution

````
play deps --sync
play run
````

# Deploy on Cloud Foundry

 * [Install the cf client](https://github.com/cloudfoundry/cli#downloads)
 * `cf login -a https://api.run.pivotal.io`
 * Clone the Git repository
 * Retreive the `conf/secrets.conf`
 * Eventually run `play deps --sync`
 * `play war -o ../mixit.war`
 * `cf push`

If you're impacting DB with DDL SQL scripts, it's a good idea to make a database export before.
