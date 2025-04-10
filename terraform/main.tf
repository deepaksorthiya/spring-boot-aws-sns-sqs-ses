terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.94.1"
    }
  }
}

provider "aws" {
  profile = "localstack"
  region = "us-east-1"
  shared_config_files = ["C:/Users/deepak/.aws/conf"]
  shared_credentials_files = ["C:/Users/deepak/.aws/credentials"]

}

resource "aws_sqs_queue" "email_queue" {
  name = "email-notification-queue"
}

resource "aws_sns_topic" "email_topic" {
  name = "email-notifications"
}

resource "aws_sns_topic_subscription" "sns_subscription" {
  topic_arn = aws_sns_topic.email_topic.arn
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.email_queue.arn
}

output "aws_sqs_queue" {
  value = aws_sqs_queue.email_queue.arn
}

output "aws_sns_topic" {
  value = aws_sns_topic.email_topic.arn
}

output "aws_sns_topic_subscription" {
  value = aws_sns_topic_subscription.sns_subscription.topic_arn
}