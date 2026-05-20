# No AWS credentials are stored here.
# Use GitHub Secrets or environment variables for AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY.

variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "project_name" {
  type    = string
  default = "proyecto-semestral"
}

variable "key_pair_name" {
  type    = string
  default = "deployer-key"
}

variable "instance_type" {
  type    = string
  default = "t3.micro"
}
