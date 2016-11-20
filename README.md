# DC Metro Status

DC Metro Status is an Alexa Flash Briefing skill that checks for
outages and delays from WMATA and notifies the user as part of 
their morning briefing. 

## Getting Started

This is a Scala application that runs on AWS Lambda.

### Dependencies

- [sbt](http://www.scala-sbt.org/)
- [scala](http://www.scala-lang.org/)
- [serverless](https://serverless.com/) (optional for deployment)

### Build and Deploy

- `sbt assembly && serverless deploy`

The API Gateway endpoint is what you will use for the Alexa Flash Briefing API.

## Credits

- Train by Oliviu Stoian from the Noun Project

## License

The MIT License (MIT)