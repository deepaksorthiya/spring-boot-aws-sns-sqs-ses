[![Java Maven Build Test](https://github.com/deepaksorthiya/spring-boot-aws-sns-sqs-ses/actions/workflows/maven-build.yml/badge.svg)](https://github.com/deepaksorthiya/spring-boot-aws-sns-sqs-ses/actions/workflows/maven-build.yml)

---

### ** AWS Messaging: Spring Boot on LocalStack **

This sample Spring Boot application project demonstrates how to:

* Provision terraform infrastructure on LocalStack
* Configure SNS SQS subscriptions with terraform
* Receive SQS messages with the AWS Java SDK
* Send SES message with the AWS Java SDK

---

# Getting Started

### Requirements:

```
Git: 2.49.0
Spring Boot: 3.5.0
Maven: 3.9+
Java: 24
Docker Desktop: Tested on 4.41.0
Terraform: v1.11.3
AWS CLI: 2+
```

### Clone this repository:

```bash
git clone https://github.com/deepaksorthiya/spring-boot-aws-sns-sqs-ses.git
cd spring-boot-aws-sns-sqs-ses
```

### Setup the infrastructure on localstack

Sign Up for [LocalStack](https://app.localstack.cloud) and Copy/Generate your Auth Token
from  [here](https://app.localstack.cloud/settings/auth-tokens).
Set Auth Token in variable ```LOCALSTACK_AUTH_TOKEN``` in file [docker-compose.yml](docker-compose.yml)

Install [AWS Cli](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html).
In your user home directory find folder ```.aws```.
for windows ```C:\Users\deepak\.aws```
add/create below contents to file ```credentials``` file

```
[default]
endpoint_url = http://localhost:4566
aws_access_key_id = test
aws_secret_access_key = test
[localstack]
endpoint_url = http://localhost:4566
aws_access_key_id = test
aws_secret_access_key = test

```

and add/create below contents to ```config``` file

```
[default]
region = us-east-1
[localstack]
region = us-east-1

```

Edit [main.tf](terraform/main.tf) and add correct path

```
provider "aws" {
  profile = "localstack"
  region = "us-east-1"
  shared_config_files = ["C:/Users/deepak/.aws/conf"]
  shared_credentials_files = ["C:/Users/deepak/.aws/credentials"]

}
```

### Start LocalStack on Docker:

```bash
docker compose up
```

### Start AWS Services

```bash
cd terraform
```

```bash
terraform init
```

```bash
terraform plan
```

```bash
terraform apply --auto-approve
```

### Run Project:

Set environment variable
```AWS_ACCESS_KEY_ID=test```
```AWS_SECRET_ACCESS_KEY=test```
or run

```bash
AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test ./mvnw spring-boot:run
```

### Testing

Verify the sender email address configured in the app

    aws ses verify-email-identity --email-address no-reply@localstack.cloud

Send a message to the topic

    aws sns publish \
        --topic arn:aws:sns:us-east-1:000000000000:email-notifications \
        --message '{"subject":"hello", "address": "alice@example.com", "body": "hello world"}'

Check the `/list` endpoint for queued messages.

    curl -s localhost:8080/list | jq .

Run the `/process` endpoint to send the queued notifications as emails

    curl -s localhost:8080/process

Verify that the email has been sent:

* either check MailHog via the UI http://localhost:8025/
* or query the LocalStack internal SES endpoint: `curl -s localhost:4566/_localstack/ses | jq .`

### Destroy Infrastructure

```bash
terraform destroy --auto-approve
```

### Stop Docker Compose Service and Remove Volume

```bash
docker compose down -v
```

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/maven-plugin/build-image.html)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/reference/actuator/index.html)
* [Spring Web](https://docs.spring.io/spring-boot/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot//io/validation.html)
* [Flyway Migration](https://docs.spring.io/spring-boot/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
