# SpringBoot-Hazelcast-DistributedWebChat

# Vertx-WebChat

This is a WebChat with Springboot and HAzelcast

# Prerequisites

* Java 8
* Maven 3.0.5

## SetUp

```sh
$ mvn install
```

## Start application

```sh
$ mvn spring-boot:run
```

## Deploy

```sh
$ aws iam create-policy --policy-name HazelcastPolicy --policy-document file://policy.json > policy-info.json
$ aws iam create-role --role-name Cluster-Role --assume-role-policy-document file://role.json
$ aws iam attach-role-policy --policy-arn arn:aws:iam::207476795025:policy/HazelcastPolicy --role-name Cluster-Role
```
{
    "User": {
        "Path": "/",
        "UserName": "ClusterManager",
        "UserId": "AIDAIX66LHFWB3U4H3PHI",
        "Arn": "arn:aws:iam::207476795025:user/ClusterManager",
        "CreateDate": "2018-06-22T00:50:26.567Z"
    }
}


scp -i TFG.pem -o "StrictHostKeyChecking no" target/WebChatSpringBootHazlecast-0.0.1-SNAPSHOT.jar "ubuntu@${DNS}:/home/ubuntu/Node.jar"
