# Using Docker and Docker Compose
## Getting started
- Make sure that docker is running.
    - Locally on the MacBook this means that the Docker Desktop has started and the Docker label at the bottom left is
      green.
- From a bash terminal go to the directory with the `docker-compose.yml` deploy file
  ```bash
  docker compose up -d
  ```
    - This assumes the deployment file has the default name `docker-compose.yml`. If you have used another name, you can
      specify this with a `-f` parameter.
    - `-d` means in detached mode, to prevent all kinds of logging to be sent to the terminal. In stead, the terminal
      now may be used for other commands.
    - To stop the execution of the deployments use
  ```bash
  docker compose down
  ```
    - this will stop and remove all containers and networks and only leave all involved images, so a next start up
      won't require the same extend of downloads.

## Useful Docker commands
### `docker ps`
To see all currently running containers.
Add parameter `-a` to include containers that were stopped. When you use `docker compose down` all stopped containers
will be removed as well, so `-a` will check to make sure of that.
```bash
  docker ps -a
```

### `docker exec $BROKER_NAME $PATH_TO_SOME_SCRIPT`
To run a command or script in any of the brokers from the outside host we can use this command.
#### Examples running Kafka shell script tools that are shipped with any Kafka installation
```bash
  docker exec broker-2 /kafka/bin/kafka-topics.sh --bootstrap-server broker-2:29092 --create --topic basic-topic
  docker exec broker-1 /kafka/bin/kafka-topics.sh --bootstrap-server broker-1:29091 --list
  docker exec broker-3 bin/kafka-topics.sh --bootstrap-server broker-3:29093 --describe --topic basic-topic
```
In the above example th Kafka tool scripts weren't included in the $PATH variable, so we needed either the
absolute path to the script or a relative path from the containers home directory.
#### Use `-it` for an interactive session
To open a bash shell inside one of the running Docker containers, we can use `docker exec -it broker-1 bash`.
```bash
willem@linux-laptop:~/git/ps-securing-kafka$ docker exec -it broker-1 bash
[kafka@broker-1 ~]$ pwd
/kafka
[kafka@broker-1 ~]$ ls -la
total 84
drwxrwxrwx 1 kafka kafka  4096 Oct 10  2020 .
drwxr-xr-x 1 root  root   4096 Mar  4 20:57 ..
-rw-rw-rw- 1 kafka kafka    18 Apr  1  2020 .bash_logout
-rw-rw-rw- 1 kafka kafka   193 Apr  1  2020 .bash_profile
-rw-rw-rw- 1 kafka kafka   231 Apr  1  2020 .bashrc
-rw-rw-rw- 1 root  root  29975 Jul 28  2020 LICENSE
-rw-rw-rw- 1 root  root    337 Jul 28  2020 NOTICE
drwxrwxrwx 1 root  root   4096 Jul 28  2020 bin
drwxrwxrwx 1 root  root   4096 Mar  4 20:57 config
drwxrwxrwx 1 kafka kafka  4096 Oct 10  2020 config.orig
drwxrwxrwx 1 kafka kafka  4096 Oct 10  2020 data
drwxrwxrwx 1 root  root   4096 Oct 10  2020 libs
drwxrwxrwx 1 kafka kafka  4096 Mar  4 21:01 logs
[kafka@broker-1 ~]$ ./bin/kafka-topics.sh --bootstrap-server broker-1:29091 --describe --topic basic-topic
Topic: basic-topic      PartitionCount: 1       ReplicationFactor: 3    Configs: min.insync.replicas=2,segment.bytes=1073741824
        Topic: basic-topic      Partition: 0    Leader: 3       Replicas: 3,1,2 Isr: 3,1,2
[kafka@broker-1 ~]$ echo $PATH
/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
[kafka@broker-1 ~]$ exit
exit
willem@linux-laptop:~/git/ps-securing-kafka$ 
```
To break out of the interactive bash terminal session inside broker-1 use CTRL-D (Not CMD-D) or type exit.