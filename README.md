<h1 style="text-align: center;">Spring Boot AWS SNS SQS SES Example (LocalStack)</h1>

<p style="text-align: center;">
  <a href="https://github.com/deepaksorthiya/spring-boot-aws-sns-sqs-ses/actions/workflows/maven-build.yml">
    <img src="https://github.com/deepaksorthiya/spring-boot-aws-sns-sqs-ses/actions/workflows/maven-build.yml/badge.svg" alt="Java Maven Build Test"/>
  </a>
  <a href="https://www.docker.com/">
    <img src="https://img.shields.io/badge/docker-ready-blue?logo=docker" alt="Docker"/>
  </a>
  <a href="https://www.terraform.io/">
    <img src="https://img.shields.io/badge/terraform-infra-blueviolet?logo=terraform" alt="Terraform"/>
  </a>
  <a href="https://spring.io/projects/spring-boot">
    <img src="https://img.shields.io/badge/spring--boot-3.5.0-brightgreen?logo=springboot" alt="Spring Boot"/>
  </a>
</p>

## Live Demo

TBD

---

## 📑 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Requirements](#-requirements)
- [Getting Started](#-getting-started)
- [Testing](#-testing)
- [Cleanup](#-cleanup)
- [Reference Documentation](#-reference-documentation)
- [License](#-license)

---

## 🚀 Overview

This project demonstrates how to build a **Spring Boot** application that integrates with **AWS SNS, SQS, and SES**
using **LocalStack** for local development and **Terraform** for infrastructure provisioning.

---

## ✨ Features

- Provision AWS resources (SNS, SQS, SES) locally with Terraform & LocalStack
- Publish notifications to SNS, receive via SQS, and send emails via SES
- REST endpoints to process, list, and purge notifications

---

## 🛠️ Requirements

- **Git**: 2.49.0+
- **Spring Boot**: 3.5.0
- **Maven**: 3.9+
- **Java**: 24
- **Docker Desktop**: 4.42.0+
- **Terraform**: v1.11.3
- **AWS CLI**: 2+

---

## 📦 Getting Started

### 1. Clone the Repository

```sh
git clone https://github.com/deepaksorthiya/spring-boot-aws-sns-sqs-ses.git
cd spring-boot-aws-sns-sqs-ses
```

### 2. Configure LocalStack & AWS CLI

- **Sign up for [LocalStack](https://app.localstack.cloud) and get your Auth Token.**
- Set `LOCALSTACK_AUTH_TOKEN` in [`docker-compose.yml`](docker-compose.yml).

- **Configure AWS CLI credentials:**

    - Edit `~/.aws/credentials` (Windows: `C:\Users\<user>\.aws\credentials`):

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

    - Edit `~/.aws/config`:

      ```
      [default]
      region = us-east-1
      [localstack]
      region = us-east-1
      ```

- **Edit [`terraform/main.tf`](terraform/main.tf)** to set the correct path for your AWS config and credentials.

### 3. Start LocalStack

- Docker should be installed and running.

```sh
docker compose up
```

### 4. Provision AWS Resources with Terraform

```sh
cd terraform
terraform init
terraform plan
terraform apply --auto-approve
```

### 5. Run the Spring Boot Application

Set environment variables:

```sh
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
```

Or run directly:

```sh
AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test ./mvnw spring-boot:run
```

---

## 🧪 Testing

### 1. Verify Sender Email

```sh
aws ses verify-email-identity --email-address no-reply@localstack.cloud
```

### 2. Publish a Notification

```sh
aws sns publish \
  --topic-arn arn:aws:sns:us-east-1:000000000000:email-notifications \
  --message '{"subject":"hello", "address": "alice@example.com", "body": "hello world"}'
```

### 3. List Queued Messages

```sh
curl -s localhost:8080/list | jq .
```

### 4. Process Notifications (Send Emails)

```sh
curl -s localhost:8080/process
```

### 5. Verify Email Delivery

- **MailPit UI:** [http://localhost:8025/](http://localhost:8025/)
- **LocalStack SES endpoint:**
  ```sh
  curl -s localhost:4566/_localstack/ses | jq .
  ```

---

## 🧹 Cleanup

### Destroy Infrastructure

```sh
terraform destroy --auto-approve
```

### Stop Docker Compose and Remove Volumes

```sh
docker compose down -v
```

---

## 📚 Reference Documentation

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Boot Maven Plugin](https://docs.spring.io/spring-boot/maven-plugin/)
- [AWS Java SDK v2](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html)
- [LocalStack Docs](https://docs.localstack.cloud/)
- [Terraform AWS Provider](https://registry.terraform.io/providers/hashicorp/aws/latest/docs)

---

## 📝 License

This project is licensed under the Apache License 2.0.

---

<p style="text-align: center;">
  <b>Happy Coding!</b> 🚀
</p>
