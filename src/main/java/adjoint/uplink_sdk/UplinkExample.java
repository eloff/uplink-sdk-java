/*
 * Copyright 2017 Adjoint Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package adjoint.uplink_sdk;

import adjoint.uplink_sdk.client.*;
import static adjoint.uplink_sdk.client.Crypto.GenerateKeys;
import static adjoint.uplink_sdk.client.Crypto.ReadKeyFromFile;
import static adjoint.uplink_sdk.client.Crypto.ReadPrivateKey;
import static adjoint.uplink_sdk.client.Crypto.ReadPublicKey;
import static adjoint.uplink_sdk.client.Crypto.SaveKeyToFile;
import static adjoint.uplink_sdk.client.Crypto.DeriveAccountAddress;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class UplinkExample {

  public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException, ParseException, IOException, FileNotFoundException, InvalidKeySpecException, InvalidKeyException, SignatureException, InterruptedException {
    String unsecure = "http://";
    String secure =  "https://";

    UplinkSDK uplink = new UplinkSDK(unsecure, "localhost", "8545");

    KeyPair pair = GenerateKeys();
    PrivateKey priv = pair.getPrivate();
    PublicKey pub = pair.getPublic();

    // Convert to Bytes then Hex for new account params
    byte[] bytePriv = priv.getEncoded();
    byte[] bytePub = pub.getEncoded();

    // To save keys
    SaveKeyToFile(bytePriv, "private", "private");
    SaveKeyToFile(bytePub, "public", "public");

    // To read saved keys
    byte[] keyBytes = ReadKeyFromFile("private.pem");
    byte[] pubKeyBytes = ReadKeyFromFile("public.pem");

    PrivateKey privateKey = ReadPrivateKey(keyBytes);
    PublicKey publicKey = ReadPublicKey(pubKeyBytes);

    // Create a New account
    SortedMap meta  = new TreeMap();
    meta.put("foo", "bar");
    meta.put("shake", "bake");
    meta.put("a","z");
    String fromAddress =  null;

    Response createNewAccount = uplink.CreateAccount("EST", meta, privateKey, publicKey, fromAddress);
    String AcctAddress = DeriveAccountAddress(publicKey);
    System.out.println("Account: " + AcctAddress);

    String fromAddr = AcctAddress;
    String toAddr = AcctAddress;

    // Create a new asset
    int supply = 1000;
    short precision = 0;

    Thread.sleep(5000); // wait for account to be created fully
    Response newAsset = uplink.CreateAsset(privateKey, publicKey, fromAddr, "testCoin", supply, "Discrete", precision, "Token", fromAddr);

    Integer balance = 5;
    String assetAddr = newAsset.getTag();
    // Wait for asset to be created
    Thread.sleep(5000);

    // Transfer Asset
    Response TransferAsset = uplink.TransferAsset(fromAddr, assetAddr, toAddr, balance, privateKey);

    // Create a contract
    String Script = "global int x = 0 ;\n" +
"\n" +
"transition initial -> get;\n" +
"transition get -> terminal;\n" +
"\n" +
"@get\n" +
"getX () {\n" +
"  terminate(\"Now I die.\");\n" +
"  return x;\n" +
"}\n" +
"\n" +
"@initial\n" +
"setX (int y) {\n" +
"  x = 42;\n" +
"  transitionTo(:get);\n" +
"  return void;\n" +
"}";

    Thread.sleep(5000); // Wait for block to be created.
    Response newContract = uplink.CreateContract(privateKey, AcctAddress, Script);
    String contractAddress = newContract.getTag();
    Thread.sleep(5000); // Wait for contract to be created.
    // Call Method on deployed Contract
    String contractMethod = "setX";
    Map arguments  = new HashMap();
    arguments.put("int", "12");
    Response called = uplink.CallContract(privateKey, AcctAddress, contractAddress, contractMethod, arguments);

    // Revoke account - if validating node, cannot revoke account
    // Response revoke = uplink.RevokeAccount(privateKey, fromAddress, fromAddress);

    // Queries
    Response gotContract = uplink.GetContract(contractAddress);
    Response blocks = uplink.GetBlocks();
    Response transasctions = uplink.GetTransactions(1);
    Response peers = uplink.GetPeers();
    Response accounts = uplink.GetAccounts();
    Response Assets = uplink.GetAssets();
    Response gotAccount = uplink.GetAccount(AcctAddress);
    Response contracts = uplink.GetContracts();
  }
}

