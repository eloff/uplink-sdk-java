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
package adjoint.uplink_sdk.client;

import static adjoint.uplink_sdk.client.Crypto.DeriveAccountAddress;
import static adjoint.uplink_sdk.client.Crypto.DeriveAssetAddress;
import static adjoint.uplink_sdk.client.Crypto.DeriveContractAddress;
import static adjoint.uplink_sdk.client.Crypto.RemovePadding;
import adjoint.uplink_sdk.client.parameters.wrappers.AccountWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.AssetWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.ContractWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.BlocksWrapper;
import adjoint.uplink_sdk.client.parameters.wrappers.ContractsWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.AccountsWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.PeersWrap;
import adjoint.uplink_sdk.client.parameters.wrappers.TxAccount;
import adjoint.uplink_sdk.client.parameters.wrappers.TxAsset;
import adjoint.uplink_sdk.client.parameters.wrappers.TxContract;
import adjoint.uplink_sdk.client.parameters.wrappers.TxHeader;
import adjoint.uplink_sdk.client.parameters.wrappers.TransactionsWrap;

import adjoint.uplink_sdk.client.parameters.RevokeAccountHeader;
import adjoint.uplink_sdk.client.parameters.ContractHeader;
import adjoint.uplink_sdk.client.parameters.TransferHeader;
import adjoint.uplink_sdk.client.parameters.AccountHeader;
import adjoint.uplink_sdk.client.parameters.SyncLocalHeader;
import adjoint.uplink_sdk.client.parameters.BindAssetHeader;
import adjoint.uplink_sdk.client.parameters.AssetHeader;

import adjoint.uplink_sdk.client.parameters.Actions;
import adjoint.uplink_sdk.client.parameters.Type;
import adjoint.uplink_sdk.client.parameters.Transactions;
import adjoint.uplink_sdk.client.parameters.TransactionParams;

import static adjoint.uplink_sdk.client.Crypto.Sign;
import static adjoint.uplink_sdk.client.Crypto.SignBytes;
import adjoint.uplink_sdk.client.parameters.AssetType;
import adjoint.uplink_sdk.client.parameters.CallContractHeader;
import adjoint.uplink_sdk.client.parameters.wrappers.CallContract;
import adjoint.uplink_sdk.client.parameters.wrappers.CreateAccount;
import adjoint.uplink_sdk.client.parameters.wrappers.CreateAsset;
import adjoint.uplink_sdk.client.parameters.wrappers.CreateContract;
import adjoint.uplink_sdk.client.parameters.wrappers.RevokeAccount;
import adjoint.uplink_sdk.client.parameters.wrappers.TransferAsset;
import adjoint.uplink_sdk.client.parameters.wrappers.TxTypeHeader;

import com.sun.jersey.api.client.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.lang.ArrayUtils;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

public class UplinkSDK {

  private Client client;
  private String url;
  private String error;

  MakeRequest request = new MakeRequest();
  Gson gson = new Gson();

  /**
   * @author Adjoint Inc.
   */
    public UplinkSDK(String protocol, String address, String port){
        this.client = Client.create();
        this.url = protocol + address + ":" + port; // address is "https://xx.xxx.xxx", port is "1234"
    }

  /*======================*/
  //   READ
  /*======================*/
  public Response GetBlocks() {

    String url = this.url + "/blocks";
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapterResp = RTAFgenerator(BlocksWrapper.class, ResponseEnum.RPC_RESP.getName());
    RuntimeTypeAdapterFactory<TxHeader> adapter = RuntimeTypeAdapterFactory
            .of(TxHeader.class, GeneralEnum.TAG.getName())
            .registerSubtype(TxAsset.class, TxTypeEnum.TX_ASSET.getName())
            .registerSubtype(TxAccount.class, TxTypeEnum.TX_ACCOUNT.getName())
            .registerSubtype(TxContract.class, TxTypeEnum.TX_CONTRACT.getName());

    RuntimeTypeAdapterFactory<TxTypeHeader> adapterType = RuntimeTypeAdapterFactory
            .of(TxTypeHeader.class, GeneralEnum.TAG.getName())
            .registerSubtype(CreateAccount.class, TxTypeEnum.CREATE_ACCOUNT.getName())
            .registerSubtype(CreateAsset.class, TxTypeEnum.CREATE_ASSET.getName())
            .registerSubtype(CreateContract.class, TxTypeEnum.CREATE_CONTRACT.getName())
            .registerSubtype(TransferAsset.class, TxTypeEnum.TRANSFER_ASSET.getName())
            .registerSubtype(RevokeAccount.class, TxTypeEnum.REVOKE_ACCOUNT.getName())
            .registerSubtype(CallContract.class, TxTypeEnum.CALL_CONTRACT.getName());

    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(adapterType)
            .registerTypeAdapterFactory(adapter)
            .registerTypeAdapterFactory(adapterResp)
            .create();

    Response blocks = gson.fromJson(output, Response.class);
    return blocks;
  }
  public Response GetPeers(){
    String url = this.url + "/peers";
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(PeersWrap.class, ResponseEnum.RPC_RESP.getName());

    Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();

    Response peers = gson.fromJson(output, Response.class);
    return peers;
  }
  public Response GetAccounts(){
    String url = this.url + "/accounts";
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(AccountsWrap.class, ResponseEnum.RPC_RESP.getName());

    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(adapter)
            .create();

    Response accounts = gson.fromJson(output, Response.class);
    return accounts;
  }
  public Response GetAccount(String Address) {
    String url = this.url + "/accounts/" + Address;
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(AccountWrap.class, ResponseEnum.RPC_RESP.getName());

    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(adapter)
            .create();

    Response account = gson.fromJson(output, Response.class);
    return account;
  }

  public Response GetAssets() {
    String url = this.url + "/assets";
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(AssetWrap.class, ResponseEnum.RPC_RESP.getName());

    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(adapter)
            .create();

    Response assets = gson.fromJson(output, Response.class);
    return assets;
  }

  public Response GetTransactions(int BlockId) {
    String url = this.url + "/transactions/" + BlockId;
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapterResp = RTAFgenerator(TransactionsWrap.class, ResponseEnum.RPC_RESP.getName());

    RuntimeTypeAdapterFactory<TxHeader> adapter = RuntimeTypeAdapterFactory
            .of(TxHeader.class, GeneralEnum.TAG.getName())
            .registerSubtype(TxAsset.class, TxTypeEnum.TX_ASSET.getName())
            .registerSubtype(TxAccount.class, TxTypeEnum.TX_ACCOUNT.getName())
            .registerSubtype(TxContract.class, TxTypeEnum.TX_CONTRACT.getName());

    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(adapter)
            .registerTypeAdapterFactory(adapterResp)
            .create();

    Response transactions = gson.fromJson(output, Response.class);
    return transactions;
  }

  public Response GetContracts() {
    String url = this.url + "/contracts";
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ContractsWrap.class, ResponseEnum.RPC_RESP.getName());

    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(adapter)
            .create();

    Response contracts = gson.fromJson(output, Response.class);
    return contracts;

  }

  public Response GetContract(String address) {
    String url = this.url + "/contracts/" + address;

    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ContractWrap.class, ResponseEnum.RPC_RESP.getName());

    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(adapter)
            .create();

    Response contract = gson.fromJson(output, Response.class);
    return contract;
    }

  /*======================*/
  //   Create
  /*======================*/
  public Response CreateAccount(String Timezone, Map<String, String> meta, PrivateKey privateKey, PublicKey publicKey, String fromAddress) throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException {
    String url = this.url + "/";
    Integer TxType = TxTypeEnum.CREATE_ACCOUNT.getValue();
    long timestamp = System.currentTimeMillis() * 1000;

    // Serialization to convert Public key to hex string ========
    String X = ((BCECPublicKey) publicKey).getW().getAffineX().toString();
    String Y = ((BCECPublicKey) publicKey).getW().getAffineY().toString();

    BigInteger bigX = new BigInteger(X);
    BigInteger bigY = new BigInteger(Y);
    byte[] xBytes = bigX.toByteArray();
    byte[] yBytes = bigY.toByteArray();
    xBytes = RemovePadding(xBytes);
    yBytes = RemovePadding(yBytes);
    byte[] keyBytes = ArrayUtils.addAll(xBytes, yBytes);

    String pubKeyHex = DatatypeConverter.printHexBinary(keyBytes);
    pubKeyHex = pubKeyHex.toLowerCase();
    byte[] byteTimezone = Timezone.getBytes();

    int pubkeyLen = pubKeyHex.length();

    // Prepare Bytes to sign [Txtype, pubkeyhex, timezone, metadata]
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);
    stream.writeShort(TxType);
    stream.writeShort(pubkeyLen);
    stream.writeBytes(pubKeyHex);
    stream.writeShort(Timezone.length());
    stream.write(byteTimezone);

    // write bytes for hashmap of meta
    if (!meta.isEmpty()) {
      stream.writeShort(meta.size());
      meta.forEach((k, v) -> {
        try {
          stream.writeShort(k.length());
          stream.writeBytes(k);
          stream.writeShort(v.length());
          stream.writeBytes(v);
        } catch (IOException ex) {
          Logger.getLogger(UplinkSDK.class.getName()).log(Level.SEVERE, null, ex);
        }
      });
    } else {
      stream.writeShort(0);
    }
    stream.flush();
    byte[] convertedBytes = data.toByteArray();

    // Sign already!
    byte[] signedBytes = SignBytes(convertedBytes, privateKey);
    String signature = Sign(signedBytes);

    String toAddress = DeriveAccountAddress(publicKey);

    String origin = fromAddress;
    if (fromAddress == null) {
      origin = toAddress;
    }

    //Serialize Parameters
    AccountHeader aHead = new AccountHeader(pubKeyHex, Timezone, meta);
    Actions actions = new Actions(TxTypeEnum.CREATE_ACCOUNT.getName(), aHead);
    Type type = new Type(TxTypeEnum.TX_ACCOUNT.getName(), actions);
    Transactions trans = new Transactions(timestamp, signature, origin, toAddress, type);
    TransactionParams tx = new TransactionParams(trans);
    // Make it a json string
    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);
    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_RESP_OK.getName());

    Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeAdapterFactory(adapter)
            .create();

    Response account = gson.fromJson(output, Response.class);
    return account;
  }

  public Response CreateAsset(PrivateKey privateKey, PublicKey publicKey, String fromAddr, String Name, int Supply, String assetType, short precision, String reference, String issuer) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException, IOException {
    String url = this.url + "/";
    Integer TxType = TxTypeEnum.CREATE_ASSET.getValue();
    long timestamp = System.currentTimeMillis() * 1000;

    // Prepare Bytes to sign [Txtype, timezone, metadata]
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);

    stream.writeShort(TxType);
    stream.writeShort(Name.length());
    stream.writeBytes(Name);
    stream.writeLong(Supply);
    stream.writeShort(1);
    stream.writeShort(reference.length());
    stream.writeBytes(reference);
    stream.writeShort(assetType.length());
    stream.writeBytes(assetType);
    if ("Fractional".equals(assetType)) {
      stream.writeLong(precision);
    }

    stream.flush();
    byte[] convertedBytes = data.toByteArray();

    // Sign already!
    byte[] signedBytes = SignBytes(convertedBytes, privateKey);
    String signature = Sign(signedBytes);

    //Serialize
    AssetType aType = new AssetType(assetType, precision);
    AssetHeader aHead = new AssetHeader(Name, Supply, aType, reference, issuer);
    Actions actions = new Actions(TxTypeEnum.CREATE_ASSET.getName(), aHead);
    Type type = new Type(TxTypeEnum.TX_ASSET.getName(), actions);

    String toAddress = DeriveAssetAddress(signedBytes, fromAddr);
    Transactions trans = new Transactions(timestamp, signature, fromAddr, toAddress, type);
    TransactionParams tx = new TransactionParams(trans);

    // Jsonify
    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);
    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_RESP_OK.getName());

    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(adapter)
            .setPrettyPrinting()
            .create();

    Response asset = gson.fromJson(output, Response.class);

    if (asset.getClass() == ResponseOkay.class) {
      Response AssetAddress = new Response(toAddress, "assetAddress");
      return AssetAddress;
    } else {
      return asset;
    }
  };

  public Response CreateContract(PrivateKey privateKey, String fromAddr, String script) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    String url = this.url + "/";
    Integer TxType = TxTypeEnum.CREATE_CONTRACT.getValue();
    long timestamp = System.currentTimeMillis() * 1000;

    String contractAddress = DeriveContractAddress(script);
    byte[] byteContractAddr = Base58convert.decode(contractAddress);

    // Prepare Bytes to sign [Txtype, timezone, metadata]
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);

    stream.writeShort(TxType);
    stream.write(byteContractAddr);
    stream.writeShort(script.length());
    stream.writeBytes(script);

    stream.flush();
    byte[] convertedBytes = data.toByteArray();

    // Sign already!
    byte[] signedBytes = SignBytes(convertedBytes, privateKey);
    String signature = Sign(signedBytes);

    //Serialize
    ContractHeader aHead = new ContractHeader(script, fromAddr, contractAddress, timestamp);
    Actions actions = new Actions(TxTypeEnum.CREATE_CONTRACT.getName(), aHead);
    Type type = new Type(TxTypeEnum.TX_CONTRACT.getName(), actions);
    Transactions trans = new Transactions(timestamp, signature, fromAddr, contractAddress, type);
    TransactionParams tx = new TransactionParams(trans);

    // Json to string
    String params = gson.toJson(tx, TransactionParams.class);
    // Call the Server
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class,ResponseEnum.RPC_RESP_OK.getName());

    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(adapter)
            .setPrettyPrinting()
            .create();

    Response contract = gson.fromJson(output, Response.class);
    if (contract.getClass() == ResponseOkay.class) {
      Response ContractAddress = new Response(contractAddress, "contractAddress");
      return ContractAddress;
    } else {
      return contract;
    }
  };

  public Response TransferAsset(String fromAddr, String assetAddr, String toAddr, Integer balance, PrivateKey privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, FileNotFoundException, InvalidKeySpecException {
    String url = this.url + "/";
    Integer TxType = TxTypeEnum.TRANSFER_ASSET.getValue();
    long timestamp = System.currentTimeMillis() * 1000;

    // b58 decode
    byte[] byteAssetAddr = Base58convert.decode(assetAddr);
    byte[] byteToAddr = Base58convert.decode(toAddr);

    // Prepare Bytes to sign [Txtype, asset_addr, to_addr, balance]
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);
    stream.writeShort(TxType);
    stream.write(byteAssetAddr);
    stream.write(byteToAddr);
    stream.writeLong(balance);
    stream.flush();
    byte[] convertedBytes = data.toByteArray();

    // Sign already!
    byte[] signedBytes = SignBytes(convertedBytes, privateKey);
    String signature = Sign(signedBytes);

    //Serialize Parameters
    TransferHeader aHead = new TransferHeader(assetAddr, toAddr, balance);
    Actions actions = new Actions(TxTypeEnum.TRANSFER_ASSET.getName(), aHead);
    Type type = new Type(TxTypeEnum.TX_ASSET.getName(), actions);
    Transactions trans = new Transactions(timestamp, signature, fromAddr, toAddr, type);
    TransactionParams tx = new TransactionParams(trans);

    // Json to string
    String params = gson.toJson(tx, TransactionParams.class);
    // Call the Server
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_RESP_OK.getName());

    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(adapter)
            .create();

    Response transferred = gson.fromJson(output, Response.class);

    return transferred;
  }

  public Response RevokeAccount(PrivateKey privateKey, String fromAddr, String acctAddress) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    String url = this.url + "/";
    Integer TxType = TxTypeEnum.REVOKE_ACCOUNT.getValue();
    long timestamp = System.currentTimeMillis() * 1000;

    // b58 decode address
    byte[] byteAcctAddress = Base58convert.decode(acctAddress);

    // Prepare Bytes to sign
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);
    stream.writeShort(TxType);
    stream.write(byteAcctAddress);
    stream.flush();
    byte[] convertedBytes = data.toByteArray();

    // Sign Bytes
    byte[] signedBytes = SignBytes(convertedBytes, privateKey);
    String signature = Sign(signedBytes);
    String toAddr = null;
    // Serialize parameters
    RevokeAccountHeader aHead = new RevokeAccountHeader(acctAddress);
    Actions actions = new Actions(TxTypeEnum.REVOKE_ACCOUNT.getName(), aHead);
    Type type = new Type(TxTypeEnum.TX_ACCOUNT.getName(), actions);
    Transactions trans = new Transactions(timestamp, signature, fromAddr, toAddr, type);
    TransactionParams tx = new TransactionParams(trans);

    // Make it a json string
    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_RESP_OK.getName());

    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(adapter)
            .create();

    Response account = gson.fromJson(output, Response.class);
    return account;
  }

  public Response CallContract(PrivateKey privateKey, String fromAddr, String contractAddress, String contractMethod, Map<String, String> arguments) throws IOException, SignatureException, InvalidKeyException, NoSuchAlgorithmException {
    String url = this.url + "/";
    Integer TxType = TxTypeEnum.CALL_CONTRACT.getValue();
    long timestamp = System.currentTimeMillis() * 1000;

    // b58 decode address
    byte[] byteContractAddress = Base58convert.decode(contractAddress);

    // Prepare Bytes to sign
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);
    stream.writeShort(TxType);
    stream.write(byteContractAddress);
    stream.writeLong(contractMethod.length());
    stream.writeBytes(contractMethod);
    stream.writeLong(arguments.size());

    List<Object> argsList = new ArrayList<Object>();

    // write bytes for hashmap of arguments
    arguments.forEach((type, value) -> {
      if (FCLEnum.VACCOUNT.getHuman().equals(type)) {
        try {
          Map acctArg = new HashMap();
          acctArg.put(GeneralEnum.TAG.getName(), FCLEnum.VACCOUNT.getName());
          acctArg.put(GeneralEnum.CONTENTS.getName(), value);
          argsList.add(acctArg);

          byte[] vB = Base58convert.decode((String) value);
          stream.writeByte(FCLEnum.VACCOUNT.getValue());
          stream.write(vB);
        } catch (IOException ex) {
          Logger.getLogger(UplinkSDK.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      if (FCLEnum.VASSET.getHuman().equals(type)) {
        try {
          Map assetArg = new HashMap();
          assetArg.put(GeneralEnum.TAG.getName(), FCLEnum.VASSET.getName());
          assetArg.put(GeneralEnum.CONTENTS.getName(), value);
          argsList.add(assetArg);

          byte[] vB = Base58convert.decode((String) value);
          stream.writeByte(FCLEnum.VASSET.getValue());
          stream.write(vB);
        } catch (IOException ex) {
          Logger.getLogger(UplinkSDK.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      if (FCLEnum.VCONTRACT.getHuman().equals(type)) {
        try {
          Map ctrctArg = new HashMap();
          ctrctArg.put(GeneralEnum.TAG.getName(), FCLEnum.VCONTRACT.getName());
          ctrctArg.put(GeneralEnum.CONTENTS.getName(), value);
          argsList.add(ctrctArg);

          byte[] vB = Base58convert.decode((String) value);
          stream.writeByte(FCLEnum.VCONTRACT.getValue());
          stream.write(vB);
        } catch (IOException ex) {
          Logger.getLogger(UplinkSDK.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      if (FCLEnum.VINT.getHuman().equals(type)) {
        try {
          long vB = Long.valueOf(value);
          Map intArg = new HashMap();
          stream.writeByte(FCLEnum.VINT.getValue());
          stream.writeLong(vB);
          intArg.put(GeneralEnum.TAG.getName(), FCLEnum.VINT.getName());
          intArg.put(GeneralEnum.CONTENTS.getName(), vB);
          argsList.add(intArg);
        } catch (IOException ex) {
          Logger.getLogger(UplinkSDK.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      if (FCLEnum.VFLOAT.getHuman().equals(type)) {
        try {
          double vB = Double.parseDouble(value);
          Map floatArg = new HashMap();
          stream.writeByte(FCLEnum.VFLOAT.getValue());
          stream.writeDouble(vB);
          floatArg.put(GeneralEnum.TAG.getName(), FCLEnum.VFLOAT.getName());
          floatArg.put(GeneralEnum.CONTENTS.getName(), vB);
          argsList.add(floatArg);
        } catch (IOException ex) {
          Logger.getLogger(UplinkSDK.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      if (FCLEnum.VBOOL.getHuman().equals(type)) {
        try {
          boolean vB = Boolean.parseBoolean(value);
          Map boolArg = new HashMap();
          stream.writeByte(FCLEnum.VBOOL.getValue());
          stream.writeBoolean(vB);
          boolArg.put(GeneralEnum.TAG.getName(), FCLEnum.VBOOL.getName());
          boolArg.put(GeneralEnum.CONTENTS.getName(), vB);
          argsList.add(boolArg);
        } catch (IOException ex) {
          Logger.getLogger(UplinkSDK.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      if (FCLEnum.VMSG.getHuman().equals(type)) {
        try {
          Map msgArg = new HashMap();
          msgArg.put(GeneralEnum.TAG.getName(), FCLEnum.VMSG.getName());
          msgArg.put(GeneralEnum.CONTENTS.getName(), value);
          argsList.add(msgArg);
          int vlen = value.length();
          stream.writeByte(FCLEnum.VMSG.getValue());
          stream.writeLong(vlen);
          stream.writeBytes(value);
        } catch (IOException ex) {
          Logger.getLogger(UplinkSDK.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });

    stream.flush();
    byte[] convertedBytes = data.toByteArray();

    // Sign Bytes
    byte[] signedBytes = SignBytes(convertedBytes, privateKey);
    String signature = Sign(signedBytes);
    String toAddr = null;

    // Serialize parameters
    CallContractHeader aHead = new CallContractHeader(contractAddress, contractMethod, argsList);
    Actions actions = new Actions(TxTypeEnum.CALL_CONTRACT.getName(), aHead);
    Type type = new Type(TxTypeEnum.TX_CONTRACT.getName(), actions);
    Transactions trans = new Transactions(timestamp, signature, fromAddr, toAddr, type);
    TransactionParams tx = new TransactionParams(trans);

    // Make it a json string
    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);
    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_RESP_OK.getName());
    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(adapter)
            .create();

    Response contract = gson.fromJson(output, Response.class);
    return contract;
  }

  // Not implemented
  public Response SyncLocal(PrivateKey privateKey, String fromAddr, String contractAddress) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    String url = this.url + "/";
    Integer TxType = TxTypeEnum.SYNC_LOCAL.getValue();
    long timestamp = System.currentTimeMillis() * 1000;

    // b58 decode address
    byte[] byteContractAddress = Base58convert.decode(contractAddress);

    // Prepare Bytes to sign
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);
    stream.writeShort(TxType);
    stream.write(byteContractAddress);
    stream.flush();
    byte[] convertedBytes = data.toByteArray();

    // Sign Bytes
    byte[] signedBytes = SignBytes(convertedBytes, privateKey);
    String signature = Sign(signedBytes);
    String toAddr = null;
    // Serialize parameters
    SyncLocalHeader aHead = new SyncLocalHeader(contractAddress);
    Actions actions = new Actions(TxTypeEnum.SYNC_LOCAL.getName(), aHead);
    Type type = new Type(TxTypeEnum.TX_CONTRACT.getName(), actions);
    Transactions trans = new Transactions(timestamp, signature, fromAddr, toAddr, type);
    TransactionParams tx = new TransactionParams(trans);

    // Make it a json string
    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);
    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(AccountWrap.class, ResponseEnum.RPC_RESP.getName());
    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(adapter)
            .create();

    Response contract = gson.fromJson(output, Response.class);
    return contract;
  };

    // Not Implemented
  public Response BindAsset(PrivateKey privateKey, String fromAddr, String contractAddress, String assetAddress) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    String url = this.url + "/";
    Integer TxType = TxTypeEnum.BIND_ASSET.getValue();
    long timestamp = System.currentTimeMillis() * 1000;

    // b58 decode address
    byte[] byteContractAddress = Base58convert.decode(contractAddress);
    byte[] byteAssetAddress = Base58convert.decode(assetAddress);

    // Prepare Bytes to sign
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);
    stream.writeShort(TxType);
    stream.write(byteContractAddress);
    stream.write(byteAssetAddress);
    stream.flush();
    byte[] convertedBytes = data.toByteArray();

    // Sign Bytes
    byte[] signedBytes = SignBytes(convertedBytes, privateKey);
    String signature = Sign(signedBytes);
    String toAddr = null;
    // Serialize parameters
    BindAssetHeader aHead = new BindAssetHeader(contractAddress, assetAddress);
    Actions actions = new Actions("Bind", aHead);
    Type type = new Type("TxAsset", actions);
    Transactions trans = new Transactions(timestamp, signature, fromAddr, toAddr, type);
    TransactionParams tx = new TransactionParams(trans);

    // Make it a json string
    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(AssetWrap.class, ResponseEnum.RPC_RESP.getName());

    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(adapter)
            .create();

    Response contract = gson.fromJson(output, Response.class);
    return contract;
  }

  public RuntimeTypeAdapterFactory<Response> RTAFgenerator(Class responseTypeClass, String RespName) {
    RuntimeTypeAdapterFactory<Response> adapter = RuntimeTypeAdapterFactory
            .of(Response.class, GeneralEnum.TAG.getName())
            .registerSubtype(ErrorResp.class,  ResponseEnum.RPC_RESP_ERROR.getName())
            .registerSubtype(responseTypeClass, RespName);
    return adapter;
  }

  private static final char[] HEXCHARS_LC = "0123456789abcdef".toCharArray();

  public String HexDump(final byte[] buf, final int ofs, final int len) {
    final StringBuffer sb = new StringBuffer(3 * len);
    for (int i = ofs; i < ofs + len; ++i) {
      if (i < buf.length) {
        sb.append(HEXCHARS_LC[(buf[i] & 0xF0) >> 4]);
        sb.append(HEXCHARS_LC[buf[i] & 0x0F]);
        sb.append(' ');
      } else {
        sb.append("   ");
      }
    }
    return sb.toString().toUpperCase();
  }
}
