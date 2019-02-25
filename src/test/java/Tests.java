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
import adjoint.uplink_sdk.client.Version;
import adjoint.uplink_sdk.client.parameters.wrappers.AccountsWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.AssetWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.BlocksWrapper;
import adjoint.uplink_sdk.client.parameters.wrappers.ContractsWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.PeersWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.TransactionsWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.MemPoolWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.MemPoolSize;
import adjoint.uplink_sdk.client.parameters.wrappers.MemPoolsWrap;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Adjoint Inc.
 */
public class Tests {
  String unsecure = "http://";
  UplinkSDK uplink = new UplinkSDK(unsecure, "localhost", "8545");
  String testAddr = "fwBVDsVh8SYQy98CzYpNPcbyTRczVUZ96HszhNRB8Ve";
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
  public void TestGetBlocks() {
    Response blocks = uplink.getBlocks();
    assertEquals(BlocksWrapper.class, blocks.getClass());
  }

  @Test
  public void TestGetPeers() {
    Response peers = uplink.getPeers();
    assertEquals(PeersWrap.class, peers.getClass());
  }

  @Test
  public void TestGetTransactions() {
    Response txs = uplink.getTransactions(0);
    assertEquals(TransactionsWrap.class, txs.getClass());
  }

  @Test
  public void TestGetMemPool() {
    Response pool = uplink.getMemPool();
    assertEquals(MemPoolWrap.class, pool.getClass());
  }

  @Test
  public void TestGetMemPoolSize() {
    Response pool = uplink.getMemPoolSize();
    assertEquals(MemPoolSize.class, pool.getClass());
  }

  @Test
  public void TestGetMemPools() {
    Response pools = uplink.getMemPools();
    assertEquals(MemPoolsWrap.class, pools.getClass());
    assertEquals(((MemPoolsWrap)pools).contents.size(), 1);
  }

  @Test
  public void TestGetAssets() {
    Response assets = uplink.getAssets();
    assertEquals(AssetWrap.class, assets.getClass());
  }

  @Test
  public void TestGetAccounts() {
    Response accounts = uplink.getAccounts();
    assertEquals(AccountsWrap.class, accounts.getClass());
  }

  @Test
  public void TestGetContracts() {
    Response contracts = uplink.getContracts();
    assertEquals(ContractsWrap.class, contracts.getClass());
  }

  // Creation
  @Test
  public void TestCreateAccount() throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException {
    SortedMap<String, String> meta = new TreeMap<>();
    meta.put("foo", "bar");
    meta.put("shake", "bake");
    meta.put("a", "z");
    String fromAddress = null;
    String timezone = "EST";

    Response account = uplink.createAccount(timezone, meta, this.privateKey, this.publicKey, fromAddress);

    assertEquals(ResponseOkay.class, account.getClass());

  }

  @Test
  public void TestCreateAsset() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, InterruptedException, InvalidAlgorithmParameterException {
    int supply = 100;
    Integer precision = null;

    SortedMap meta = new TreeMap();
    meta.put("foo", "bar");
    String fromAddress = null;
    String timezone = "EST";

    PrivateKey privateKey = this.pair.getPrivate();
    PublicKey publicKey = this.pair.getPublic();
    Response account = uplink.createAccount(timezone, meta, privateKey, publicKey, fromAddress);

    Thread.sleep(5000);
    Response asset = uplink.createAsset(privateKey, publicKey, accountAddress, "testCoin", supply, "Discrete", precision, "Token", accountAddress, meta);

    assertEquals(Response.class, asset.getClass());
  }

  @Test
  public void TestCirculateAsset() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, InterruptedException, InvalidAlgorithmParameterException {
    int supply = 100;
    Integer precision = 0;

    SortedMap meta = new TreeMap();
    meta.put("foo", "bar");
    String fromAddress = null;
    String timezone = "EST";

    PrivateKey privateKey = this.pair.getPrivate();
    PublicKey publicKey = this.pair.getPublic();
    Response account = uplink.createAccount(timezone, meta, privateKey, publicKey, fromAddress);
    Thread.sleep(4000);
    Response asset = uplink.createAsset(privateKey, publicKey, accountAddress, "testCoin", supply, "Discrete", precision, "Token", accountAddress, meta);
    assetAddress = asset.tag;
    Thread.sleep(4000);
    int amount = 10;
    Response circulated = uplink.circulateAsset(privateKey, accountAddress, assetAddress, amount);
    assertEquals(ResponseOkay.class, circulated.getClass());
  }

  @Test
  public void TestCreateContract() throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InterruptedException, InvalidAlgorithmParameterException {
    String Script = String.join("\n",
        "global int x = 0 ;",
        "transition initial -> set;",
        "transition set -> terminal;",
        "@set",
        "end () {",
        "  terminate(\"Now I die.\");",
        "}",
        "@initial",
        "setX (int y) {",
        "  x = 42;",
        "  transitionTo(:set);",
        "}");

    SortedMap<String, String> meta = new TreeMap<String, String>();
    meta.put("foo", "bar");
    String fromAddress = null;
    String timezone = "EST";

    PrivateKey privateKey = this.pair.getPrivate();
    PublicKey publicKey = this.pair.getPublic();

    // Create account
    uplink.createAccount(timezone, meta, privateKey, publicKey, fromAddress);
    Thread.sleep(10000);
    Response contract = uplink.createContract(privateKey, accountAddress, Script);
    contractAddress = contract.tag;
    assertEquals(Response.class, contract.getClass());
  }

  @Test
  public void TestTransferAsset() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, InvalidKeySpecException, InterruptedException, InvalidAlgorithmParameterException {
    int balance = 1;
    SortedMap meta = new TreeMap();
    meta.put("foo", "bar");
    String fromAddress = null;
    String timezone = "EST";

    KeyPair pair2 = GenerateKeys();
    PrivateKey privKey = pair2.getPrivate();
    PublicKey pubKey = pair2.getPublic();
    String accountAddressTO = DeriveAccountAddress(this.publicKey);
    uplink.createAccount(timezone, meta, privKey, pubKey, fromAddress);

    PrivateKey privateKey = this.pair.getPrivate();
    PublicKey publicKey = this.pair.getPublic();
    Response account = uplink.createAccount(timezone, meta, privateKey, publicKey, accountAddressTO);
    Thread.sleep(5000);
    int supply = 3;
    Integer precision = 0;
    Response asset = uplink.createAsset(privateKey, publicKey, accountAddress, "testCoin", supply, "Discrete", precision, "Token", accountAddress, meta);
    assetAddress = asset.tag;

    Thread.sleep(5000);
    Response TransferAsset = uplink.transferAsset(accountAddress, assetAddress, accountAddressTO, balance, privateKey);
    assertTrue("Transferring asset should return a valid response", TransferAsset.getClass() == ResponseOkay.class);
  }

  @Test
  public void TestUplinkVersion() throws Exception {
    final Properties properties = new Properties();
    properties.load(this.getClass().getResourceAsStream("project.properties"));

    String sdkVersion = properties.getProperty("version");
    String uplinkVersion = uplink.getUplinkVersion().version;

    assertTrue("SDK version should match Uplink version", sdkVersion.equals(uplinkVersion));
  }
  
  @Test
  public void TestCallContract() throws Exception {
    String Script = String.join("\n",
        "enum testEnum { Foo, Bar };",
        "transition initial -> end;",
        "transition end -> terminal;",
        "@initial",
        "fn_int(int a) {}",
        "@initial",
        "fn_float(float b) {}",
        "@initial",
        "fn_bool(bool x) {}",
        "@initial",
        "fn_msg(msg c) {}",
        "@initial",
        "fn_account(account a) {}",
        "@initial",
        "fn_asset(assetDisc a) {}",
        "@initial",
        "fn_contract(contract e) {}",
        "@initial",
        "fn_datetime(datetime e) {}",
        "@initial",
        "fn_fixed(fixed5 f) {}",
        "@initial",
        "fn_void(void a) {}",
        "@initial",
        "fn_enum(enum testEnum a) {}",
        "@initial",
        "never_called(void a) {",
        "    transitionTo(:end);",
        "}",
        "@end",
        "end() {",
        "  if (sender() == deployer()) {",
        "    terminate(\"This is the end\");",
        "  };",
        "}");

    SortedMap<String, String> meta = new TreeMap<String, String>();
    meta.put("foo", "bar");
    String fromAddress = null;
    String timezone = "EST";

    PrivateKey privateKey = this.pair.getPrivate();
    PublicKey publicKey = this.pair.getPublic();

    uplink.createAccount(timezone, meta, privateKey, publicKey, fromAddress);
    Thread.sleep(5000);

    Response contract = uplink.createContract(privateKey, accountAddress, Script);
    Thread.sleep(5000);

    contractAddress = contract.tag;
    List<AbstractMap.SimpleEntry<String, HashMap<String, String>>> args = new ArrayList<>();

    args.add(new AbstractMap.SimpleEntry<>("fn_int", new HashMap<String, String>() {
      {
        put("int", "12");
      }
    }));
    args.add(new AbstractMap.SimpleEntry<>("fn_float", new HashMap<String, String>() {
      {
        put("float", "12.2");
      }
    }));
    args.add(new AbstractMap.SimpleEntry<>("fn_bool", new HashMap<String, String>() {
      {
        put("bool", "True");
      }
    }));
    args.add(new AbstractMap.SimpleEntry<>("fn_msg", new HashMap<String, String>() {
      {
        put("msg", "Hello World");
      }
    }));
    args.add(new AbstractMap.SimpleEntry<>("fn_account", new HashMap<String, String>() {
      {
        put("account", testAddr);
      }
    }));
    args.add(new AbstractMap.SimpleEntry<>("fn_asset", new HashMap<String, String>() {
      {
        put("asset", testAddr);
      }
    }));
    args.add(new AbstractMap.SimpleEntry<>("fn_contract", new HashMap<String, String>() {
      {
        put("contract", testAddr);
      }
    }));
    args.add(new AbstractMap.SimpleEntry<>("fn_datetime", new HashMap<String, String>() {
      {
        put("datetime", "2017-11-29T16:50:40+00:00");
      }
    }));
    args.add(new AbstractMap.SimpleEntry<>("fn_fixed", new HashMap<String, String>() {
      {
        put("fixed5","6.54321");
      }
    }));
    args.add(new AbstractMap.SimpleEntry<>("fn_enum", new HashMap<String, String>() {
      {
        put("enum","Foo");
      }
    }));

    for (AbstractMap.SimpleEntry<String, HashMap<String, String>> arg : args) {
      Response called = uplink.callContract(privateKey, accountAddress, contractAddress, arg.getKey(), arg.getValue());
      assertEquals(ResponseOkay.class, called.getClass());
    }

  }
}
