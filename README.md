<a href="https://www.flaticon.com/free-icon/cryptocurrency_2272825">
  <img src="https://cdn-icons-png.flaticon.com/512/2272/2272825.png" align="right"
     alt="Project Image" height="300">
</a>

# CryptoScratch 
[![Activity](https://img.shields.io/github/commit-activity/m/Franek-Antoniak/crypto-scratch)](https://github.com/Franek-Antoniak/crypto-scratch) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/669705e8c8554ad1b6f26c930e5780b2)](https://www.codacy.com/gh/Franek-Antoniak/crypto-scratch/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Franek-Antoniak/crypto-scratch&amp;utm_campaign=Badge_Grade) [![LastCommit](https://img.shields.io/github/last-commit/Franek-Antoniak/crypto-scratch)](https://github.com/Franek-Antoniak/crypto-scratch)
> Secure place for transfers!

CryptoScratch is a combination of three apps that allow you to create and mine cryptocurrency. <br>
Send and manage your coins with transactions! <br>

* ***Create*** your copy of the ***cryptocurrency***
* Start ***mining*** blocks
* ***Compete*** with other ***miners***
* Send earned ***cryptocurrency*** to ***friends***

## Table of content

- [Installation and usage](#installation-and-usage)
  - [Prerequisites](#prerequisites)
  - [Node API](#node-api)
  - [Mine Client](#miner-client)
  - [Transfer Client](#transfer-client)
- [Technological description](#technological-description)
  - [Node](#node)
    - [Controllers](#controllers)
    - [The idea of a node](#the-idea-of-a-node)
    - [Modules-Libraries-Patterns](#modules-libraries-patterns)
  - [Clients](#clients)


## Installation and Usage

### Prerequisites
* [Java 18 or higher](https://jdk.java.net/18/)
* [Python 3](https://www.python.org/downloads/)
* Install requirments for Python:
``` bash
curl -OL https://raw.githubusercontent.com/Franek-Antoniak/crypto-scratch/master/src/python/requirments.txt | pip install -r requirments.txt
```
#
#### Node Client
* [Download Package](https://github.com/Franek-Antoniak/crypto-scratch/suites/9347585222/artifacts/439740626)
 ``` bash
 sudo apt-get install unzip
 mkdir node
 unzip Package.zip -d node
 cd node
 java -jar CryptoScratch-0.0.1-SNAPSHOT.jar
```
#
#### Mine Client
 ``` bash
curl -OL https://raw.githubusercontent.com/Franek-Antoniak/crypto-scratch/master/src/src/python/miner/mine-client.py | python mine-client.py <MAX CPU CORES>
```
#
#### Transfer Client
 ``` bash
curl -OL https://raw.githubusercontent.com/Franek-Antoniak/crypto-scratch/master/src/src/python/user/user-client.py | python user-client.py
```

## Technological description
### Node
#### Controllers
> You can communicate with the node via APIs that send requests to the 3 main controllers.

> The KeysController is responsible for:
> * generating private and public keys
> * signing messages
> * verifying the signature 

> The NodeController is responsible for:
> * adding a new block
> * retrieving information to create a new block
> * validating whether the miner is still digging a valid block
> * checking how much cryptocurrency the public key holds

> The TransactionReceiverController is responsible for:
> * send new transaction
> * get unique nonce

#### The idea of a node
> - The whole implementation of blockchain partly coincides with the idea of bitcoin.
> - Discovering new blocks is based on looking for a nonce value and hashing with SHA256 as it is in bitcoin.
> - Mathematically, the difficulty of digging is identical. 
> - The algorithm used to create the keys is the Elliptic Curve Digital Signature Algorithm. 
> - The most difficult thing I implemented was to validate the elements when adding them to the blockchain.
> - For a block to appear in the blockchain it requires a lot of work from the node (space for improvment)
> - In the future I will implement a merkle tree to make transactions smoother and validation faster. 
> - The value of earned cryptocurrencies decreases with how many are mined up. 
> - Using an infinite geometric series, we can calculate that there will be a maximum of 1 million coins
> - In order for the project to be a real cryptocurrency it needs:
>> - *validation of copies of the blockchain between multiple users*
>> - *working through peer to peer servers*
>> - *merkle tree root*
>> - *Base58 instead of Base64*
>> - *P2PKH*
>> - *Fees*
>> - *Coinbase* <br>
> - Currently the project is just a working outline of what I would like to implement in the future.
> - If you want to know more about the project take a look inside the files!<br>

#### Modules-Libraries-Patterns
> - To implent Node, I have used Java 17 along with the Spring framework with the following modules: 
>> - Spring Boot
>> - Spring Web
> - I have used Gradle for the build automation tool. 
> - Other libraries in the project:
>> - Lombok
>> - Hibernate Validator
>> - Apache Commons Codec
>> - Bouncy Castle
> - Patterns such as Builder, Facade, UseCase, Listener, Adapter, Singleton, Factory, Decorator, Observer, State, Dependency Injection,
and Proxy were used in the design all across of the application.

#

### Clients
> - The application has two clients written in Python 3.
> - The Mine Client uses SHA256 to calculate whether it has dug a block and communicates via a requests library with the node.
> - By using processes it can mine on multiple cores of our CPU.
> - In the future I intend to implement every function that I have used into implementation with only operations on matrices.
> - This would speed up block creation by enabling mining via the GPU
> - The second client is responsible for sending and signing transfers using encryption keys. 
> - It contains a simple console menu. A balance check for each address in the blockchain is also implemented. 
> - Most operations are carried out through communication with the Node.

#
<p align="center">
  <a href="https://github.com/Franek-Antoniak/crypto-scratch">
  <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white" alt="Java"/>
  </a>
  <a href="https://github.com/Franek-Antoniak/crypto-scratch">
  <img src="https://img.shields.io/badge/Python-%233776AB.svg?style=for-the-badge&logo=python&logoColor=white" alt="Python"/>
  </a>
  <a href="https://github.com/Franek-Antoniak/crypto-scratch">
  <img src="https://img.shields.io/badge/Blockchain-%23121D33.svg?style=for-the-badge&logo=blockchain.com&logoColor=white" alt="Blockchain"/>
  </a>
  <a href="https://github.com/Franek-Antoniak/crypto-scratch">
  <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white" alt="Spring"/>
  </a>
  <a href="https://github.com/Franek-Antoniak/crypto-scratch">
  <img src="https://img.shields.io/badge/Gradle-%2302303A.svg?style=for-the-badge&logo=hibernate&logoColor=white" alt="Gradle"/>
  </a>
</p>
