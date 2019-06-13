# rest-demo

A basic Clojure REST API Implementation using Compojure and HTTPKit

## Installation

Follow the tutorial over at medium:
https://medium.com/@functionalhuman/building-a-rest-api-in-clojure-3a1e1ae096e

## Usage

You can run the web server with the following command:

    lein run

## Bugs

Failing to pass in a parameter for :firstname or :surname crashes our server! We need to update our code to handle missing values better.
