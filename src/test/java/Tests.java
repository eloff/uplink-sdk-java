/*
 * Copyright 2017 Adjoint Inc..
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

import static adjoint.uplink_sdk.client.Crypto.DeriveAccountAddress;
import static adjoint.uplink_sdk.client.Crypto.GenerateKeys;
import adjoint.uplink_sdk.client.Response;
import adjoint.uplink_sdk.client.ResponseOkay;
import adjoint.uplink_sdk.client.UplinkSDK;
import adjoint.uplink_sdk.client.parameters.wrappers.AccountsWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.AssetWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.BlocksWrapper;
import adjoint.uplink_sdk.client.parameters.wrappers.ContractsWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.PeersWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.TransactionsWrap;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Adjoint Inc.
 */
public class Tests {
  String unsecure = "http://";
  UplinkSDK uplink = new UplinkSDK(unsecure, "localhost", "8545");

  KeyPair pair;
  PrivateKey privateKey;
  PublicKey publicKey;
  String accountAddress;
  String assetAddress;
  String contractAddress;

  public Tests() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
    this.pair = GenerateKeys();
    this.privateKey = this.pair.getPrivate();
    this.publicKey = this.pair.getPublic();
    this.accountAddress = DeriveAccountAddress(this.publicKey);
  }

  // querying
  @Test
  public void TestGetBlocks(){
    Response blocks = uplink.GetBlocks();
    assertTrue("getting blocks should return a valid response", blocks.getClass() == BlocksWrapper.class);
  }

  @Test
  public void TestGetPeers(){
    Response peers = uplink.GetPeers();
    assertTrue("getting peers should return a valid response", peers.getClass() == PeersWrap.class);
  }
  @Test
  public void TestGetTransactions(){
    Response Txs = uplink.GetTransactions(0);
    assertTrue("getting transaction should return a valid response", Txs.getClass() == TransactionsWrap.class);
  }

  @Test
  public void TestGetAssets(){
    Response assets = uplink.GetAssets();
    assertTrue("getting assets should return a valid response", assets.getClass() == AssetWrap.class);
  }

  @Test
  public void TestGetAccounts(){
    Response accounts = uplink.GetAccounts();
    assertTrue("getting accounts should return a valid response", accounts.getClass() == AccountsWrap.class);
  }

  @Test
  public void TestGetContracts(){
    Response contracts = uplink.GetContracts();
    assertTrue("getting contracts should return a valid responsee", contracts.getClass() == ContractsWrap.class);
  }

  // Creation
  @Test
  public void TestCreateAccount() throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException{
    SortedMap meta  = new TreeMap();
    meta.put("foo", "bar");
    meta.put("shake", "bake");
    meta.put("a","z");
    String fromAddress = null;
    String timezone = "EST";

    Response account = uplink.CreateAccount(timezone, meta, this.privateKey, this.publicKey, fromAddress);
    assertTrue("Creating account should return a valid response", account.getClass() == ResponseOkay.class);
  }

  @Test
  public void TestCreateAsset() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, InterruptedException, InvalidAlgorithmParameterException{
    int supply = 3;
    short precision = 0;

    SortedMap meta  = new TreeMap();
    meta.put("foo", "bar");
    String fromAddress = null;
    String timezone = "EST";

    KeyPair pair = GenerateKeys();
    PrivateKey privateKey = this.pair.getPrivate();
    PublicKey publicKey = this.pair.getPublic();
    Response account = uplink.CreateAccount(timezone, meta, privateKey, publicKey, fromAddress);

    Thread.sleep(5000);
    Response asset = uplink.CreateAsset(privateKey, publicKey, accountAddress, "testCoin", supply, "Discrete", precision, "Token", accountAddress);
    assetAddress = asset.getTag();

    assertTrue("Creating asset should return a valid response", asset.getClass() == Response.class);
  }

  @Test
  public void TestCreateContract() throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InterruptedException, InvalidAlgorithmParameterException{
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

    SortedMap meta  = new TreeMap();
    meta.put("foo", "bar");
    String fromAddress = null;
    String timezone = "EST";

    PrivateKey privateKey = this.pair.getPrivate();
    PublicKey publicKey = this.pair.getPublic();
    Response account = uplink.CreateAccount(timezone, meta, privateKey, publicKey, fromAddress);

    Thread.sleep(5000);
    Response contract = uplink.CreateContract(privateKey, accountAddress, Script);
    contractAddress = contract.getTag();
    assertTrue("Creating contract should return a valid response", contract.getClass() == Response.class);
  }

  @Test
  public void TestTransferAsset() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, FileNotFoundException, InvalidKeySpecException, InterruptedException, InvalidAlgorithmParameterException{
   int balance = 1;
    SortedMap meta  = new TreeMap();
    meta.put("foo", "bar");
    String fromAddress = null;
    String timezone = "EST";
    PrivateKey privateKey = this.pair.getPrivate();
    PublicKey publicKey = this.pair.getPublic();
    Response account = uplink.CreateAccount(timezone, meta, privateKey, publicKey, fromAddress);
    Thread.sleep(5000);
    int supply = 3;
    short precision = 0;
    Response asset = uplink.CreateAsset(privateKey, publicKey, accountAddress, "testCoin", supply, "Discrete", precision, "Token", accountAddress);
    assetAddress = asset.getTag();

    Thread.sleep(5000);
    Response TransferAsset = uplink.TransferAsset(accountAddress, assetAddress, accountAddress, balance, privateKey);
    assertTrue("Transferring asset should return a valid response", TransferAsset.getClass() == ResponseOkay.class);
  }

  @Test
  public void TestCallContract() throws IOException, SignatureException, InvalidKeyException, NoSuchAlgorithmException, InterruptedException, InvalidAlgorithmParameterException{
    String contractMethod = "setX";
    Map arguments  = new HashMap();
    arguments.put("int", "12");
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

    SortedMap meta  = new TreeMap();
    meta.put("foo", "bar");
    String fromAddress = null;
    String timezone = "EST";

    PrivateKey privateKey = this.pair.getPrivate();
    PublicKey publicKey = this.pair.getPublic();
    Response account = uplink.CreateAccount(timezone, meta, privateKey, publicKey, fromAddress);

    Thread.sleep(5000);
    Response contract = uplink.CreateContract(privateKey, accountAddress, Script);
    Thread.sleep(5000);
    contractAddress = contract.getTag();

    Response called = uplink.CallContract(privateKey, accountAddress, contractAddress, contractMethod, arguments);
    assertTrue("Calling contact method should return a valid response", called.getClass() == ResponseOkay.class);
}
}
