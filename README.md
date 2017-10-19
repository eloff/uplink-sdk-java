<p align="center">
  <a href="http://www.adjoint.io"><img src="https://www.adjoint.io/images/logo-small.png" width="250"/></a>
</p>
<h3 align="center">Community Edition</h3>

Java SDK
========

[![CircleCI](https://circleci.com/gh/adjoint-io/uplink-sdk-java/tree/master.svg?style=svg&circle-token=a0e451f3561d9b51f1802567716d4404930c2c4c)](https://circleci.com/gh/adjoint-io/uplink-sdk-java/tree/master)

Installation
------------

Download and import ``uplink_sdk-1.0.jar`` to the project's dependencies or as
Maven users add the following to ``pom.xml``:

```xml 
<dependencies>
    <dependency>
        <groupId>adjoint</groupdId>
        <artifactId>uplink_sdk</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```

Usage
-----

The Uplink Java SDK uses GSON for de/serialization of JSON to Java Objects and
contains 4 packages

1. *adjoint.uplink_sdk* contains UplinkExample.java which are examples of sdk
   usage.

2. *adjoint.uplink_sdk.client* contains UplinkSDKjava which are the main sdk
   methods to interact with the Uplink ledger. They also contain utility based
   java classes which the client depends upon:

    * *RuntimeTypeAdapterFactory* - Adapts values whose runtime type may differ from their declaration type.
    * *Response* - Handles all general responses returned from Uplink.
    * *ResponseOkay* - A successful response wrapper to deserialize JSON to Java Objects.
    * *ErrorResp* - An unsucessful response wrapper to deserialize JSON to Java Objects.
    * *MakeRequest* - This class is used when the SDK makes a request to the Uplink ledger.
    * *Crypto* - This class contains cryptographic utilities for signing transactions for the ledger.
    * *Base58Convert* - Converts to and from Base58 encoding and UTF-8.
    * *AddressFormatException* - An exception thrown from a malformed address.

3. *adjoint.uplink_sdk.client.parameters* contains the java objects to serialize
   into json before making a request to the Uplink ledger.

4. *adjoint.uplink_sdk.client.wrappers* contains the java objects to deserialize
   from json after a response has been returned.

#### Project Integration 

After installing uplink-sdk-java, import the sdk into your project, and create a
new instance of Uplink:

```java
package com.yourproject

import adjoint.uplink_sdk.client

public class YourProject{
    public static void main(String[] args){
        
        // protocol https:// if ssl/tls,
        // localhost if you're running a local instance of Uplink,
        // and the port (8545) to connect on.
        UplinkSDK uplink = new UplinkSDK("http://", "localhost", "8545")
    }
}
```

#### Simple Example

The following is a simple example to get blocks, a list of peers, current
accounts and assets.

```java
package com.yourproject;

import adjoint.uplink_sdk.client.*;

public class YourProject{
    public static void main(String[] args){
        // Create New Uplink
        UplinkSDK matrix = new UplinkSDK("http://", "localhost", "8545");

        // Get Blocks, Peers, Accounts, and Assets
        Response blocks = uplink.GetBlocks();
        Response peers = uplink.GetPeers();
        Response accounts = uplink.GetAccounts();
        Response assets = uplink.GetAssets();
    }
}

```

#### Create Account

Creating accounts will likely be a first step in interacting with the Uplink ledger. It will require a Elliptic Curve public and private key or can be generated through the SDK. The publicKey is used for verifying the authenticity of the user account. If it is an initial self signed account that is being created: ``fromAddress`` can be set to ``null``.  ``DeriveAccountAddress`` will derive the account address from the public key. 


```java
// To generate a key and use it to create and account.
KeyPair pair = GenerateKeys();
PrivateKey priv = pair.getPrivate();
PublicKey pub = pair.getPublic();

// Convert to Bytes then Hex for new account parameters
byte[] bytePriv = priv.getEncoded();
byte[] bytePub = pub.getEncoded();

// Uplink will only accept the private key in hexadecimal format
String privKey = DatatypeConverter.printHexBinary(bytePriv);

// Save the key as a pem so it can be used later
SaveKeyToFile(bytePriv, "private", "private");
SaveKeyToFile(bytePub, "public", "public");

// Or Read from a currently saved pem format EC key
byte[] keyBytes = ReadKeyFromFile("privateEC.pem"); 
byte[] pubKeyBytes = ReadKeyFromFile("publicEC.pem");


// Metadata you'd like to save for the account and account timezone.
Map meta  = new HashMap();
meta.put("foo", "bah");
meta.put("first_name", "Regular")
meta.put("last_name", "Joe")
meta.put("more","information")

String timezone = "EST"

// Create the account
Response newAccount = uplink.CreateAccount(timezone, meta, privateKey, publicKey, fromAddress);
```

#### Create Asset

Creating an Asset requires an asset name, quantity(supply), type, reference, and if the assetType is *Fractional* then precision is required. Also include your private and public key and account address.

```java
String fromAddr = yourAccountAddress;
String name = "money";
Integer supply = 22;
String assetType = "Discrete";
String reference = "Token";
Integer precision = 0;

Response newAsset = uplink.CreateAsset(privateKey, publicKey, fromAddr, name, supply, assetType, precision, reference, fromAddr);

```
When a new asset transaction is sent correctly, a Response object will be returned with `tag` as the new asset address.

```java
String assetAddress = newAsset.getTag();
```

#### Create Contract

Contract creation requires your private key, account address and script.


```java

String fromAddress = yourAccountAddress;
String script = "<< insert contract here >>";

Response newContract = uplink.CreateContract(privateKey, fromAddress, script);
String contractAddress = newContract.getTag();
```

When a new contract transaction is sent correctly, a Response object with be returned with `tag` as the new contract address.

```java
String contractAddress = newContract.getTag();
```

Documentation
-------------

To learn more about the SDK please visit the
[documentation](https://www.adjoint.io/docs/sdks.html)

License
-------

Copyright 2017 Adjoint Inc

Released under Apache 2.0.
