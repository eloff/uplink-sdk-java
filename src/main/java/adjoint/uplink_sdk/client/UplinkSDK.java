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

import adjoint.uplink_sdk.client.header.RevokeAccountHeader;
import adjoint.uplink_sdk.client.header.CreateContractHeader;
import adjoint.uplink_sdk.client.header.TransferHeader;
import adjoint.uplink_sdk.client.header.CreateAccountHeader;
import adjoint.uplink_sdk.client.header.SyncLocalHeader;
import adjoint.uplink_sdk.client.header.BindAssetHeader;
import adjoint.uplink_sdk.client.header.CreateAssetHeader;

import adjoint.uplink_sdk.client.header.Actions;
import adjoint.uplink_sdk.client.header.Type;
import adjoint.uplink_sdk.client.header.Transaction;
import adjoint.uplink_sdk.client.header.TransactionParams;

import static adjoint.uplink_sdk.client.Crypto.Sign;
import static adjoint.uplink_sdk.client.Crypto.SignBytes;

import adjoint.uplink_sdk.client.header.AssetType;
import adjoint.uplink_sdk.client.header.CallContractHeader;
import adjoint.uplink_sdk.client.header.CirculateHeader;
import adjoint.uplink_sdk.client.parameters.wrappers.CallContract;
import adjoint.uplink_sdk.client.parameters.wrappers.CreateAccount;
import adjoint.uplink_sdk.client.parameters.wrappers.CreateAsset;
import adjoint.uplink_sdk.client.parameters.wrappers.CreateContract;
import adjoint.uplink_sdk.client.parameters.wrappers.RevokeAccount;
import adjoint.uplink_sdk.client.parameters.wrappers.TransferAsset;
import adjoint.uplink_sdk.client.parameters.wrappers.TxTypeHeader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
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

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.ArrayUtils;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

public class UplinkSDK {

  private String url;


  private MakeRequest request = new MakeRequest();
  private Gson gson = new Gson();

  /**
   * @author Adjoint Inc.
   */
  public UplinkSDK(String protocol, String address, String port) {
    this.url = protocol + address + ":" + port; // address is "https://xx.xxx.xxx", port is "1234"
  }

  /*======================*/
  //   READ
  /*======================*/
  public Response getBlocks() {

    String url = this.url + "/blocks";
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapterResp = RTAFgenerator(BlocksWrapper.class, ResponseEnum.RPC_RESP.name);
    RuntimeTypeAdapterFactory<TxHeader> adapter = RuntimeTypeAdapterFactory
        .of(TxHeader.class, GeneralEnum.TAG.name)
        .registerSubtype(TxAsset.class, TxTypeEnum.TX_ASSET.name)
        .registerSubtype(TxAccount.class, TxTypeEnum.TX_ACCOUNT.name)
        .registerSubtype(TxContract.class, TxTypeEnum.TX_CONTRACT.name);

    RuntimeTypeAdapterFactory<TxTypeHeader> adapterType = RuntimeTypeAdapterFactory
        .of(TxTypeHeader.class, GeneralEnum.TAG.name)
        .registerSubtype(CreateAccount.class, TxTypeEnum.CREATE_ACCOUNT.name)
        .registerSubtype(CreateAsset.class, TxTypeEnum.CREATE_ASSET.name)
        .registerSubtype(CreateContract.class, TxTypeEnum.CREATE_CONTRACT.name)
        .registerSubtype(TransferAsset.class, TxTypeEnum.TRANSFER_ASSET.name)
        .registerSubtype(RevokeAccount.class, TxTypeEnum.REVOKE_ACCOUNT.name)
        .registerSubtype(CallContract.class, TxTypeEnum.CALL_CONTRACT.name);

    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapterType)
        .registerTypeAdapterFactory(adapter)
        .registerTypeAdapterFactory(adapterResp)
        .create();

    return gson.fromJson(output, Response.class);
  }

  public Response getPeers() {
    String url = this.url + "/peers";
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(PeersWrap.class, ResponseEnum.RPC_RESP.name);

    Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();

    return gson.fromJson(output, Response.class);
  }

  public Response getAccounts() {
    String url = this.url + "/accounts";
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(AccountsWrap.class, ResponseEnum.RPC_RESP.name);

    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .create();

    return gson.fromJson(output, Response.class);
  }

  public Response getAccount(String Address) {
    String url = this.url + "/accounts/" + Address;
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(AccountWrap.class, ResponseEnum.RPC_RESP.name);

    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .create();

    return gson.fromJson(output, Response.class);
  }

  public Response getAssets() {
    String url = this.url + "/assets";
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(AssetWrap.class, ResponseEnum.RPC_RESP.name);

    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .create();

    return gson.fromJson(output, Response.class);
  }

  public Response getTransactions(int BlockId) {
    String url = this.url + "/transactions/" + BlockId;
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapterResp = RTAFgenerator(TransactionsWrap.class, ResponseEnum.RPC_RESP.name);

    RuntimeTypeAdapterFactory<TxHeader> adapter = RuntimeTypeAdapterFactory
        .of(TxHeader.class, GeneralEnum.TAG.name)
        .registerSubtype(TxAsset.class, TxTypeEnum.TX_ASSET.name)
        .registerSubtype(TxAccount.class, TxTypeEnum.TX_ACCOUNT.name)
        .registerSubtype(TxContract.class, TxTypeEnum.TX_CONTRACT.name);

    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .registerTypeAdapterFactory(adapterResp)
        .create();

    return gson.fromJson(output, Response.class);
  }

  public Response getContracts() {
    String url = this.url + "/contracts";
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ContractsWrap.class, ResponseEnum.RPC_RESP.name);

    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .create();

    return gson.fromJson(output, Response.class);

  }

  public Response getContract(String address) {
    String url = this.url + "/contracts/" + address;

    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ContractWrap.class, ResponseEnum.RPC_RESP.name);

    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .create();

    return gson.fromJson(output, Response.class);
  }

  /*======================*/
  //   Create
  /*======================*/
  public Response createAccount(String Timezone, Map<String, String> meta, PrivateKey privateKey, PublicKey publicKey, String fromAddress) throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException {
    String url = this.url + "/";
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
    CreateAccountHeader aHead = new CreateAccountHeader(pubKeyHex, Timezone, meta);

    // Prepare Bytes to sign [Txtype, pubkeyhex, timezone, metadata]
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);
    aHead.writeBinary(stream);
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
    Actions actions = new Actions(TxTypeEnum.CREATE_ACCOUNT.name, aHead);
    Type type = new Type(TxTypeEnum.TX_ACCOUNT.name, actions);
    Transaction trans = new Transaction(timestamp, signature, origin, type);
    TransactionParams tx = new TransactionParams(trans);
    // Make it a json string
    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);
    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_RESP_OK.name);

    Gson gson = new GsonBuilder()
        .disableHtmlEscaping()
        .registerTypeAdapterFactory(adapter)
        .create();

    return gson.fromJson(output, Response.class);
  }

  public Response createAsset(PrivateKey privateKey, PublicKey publicKey, String fromAddr, String name, int supply, String assetType, Integer precision, String reference, String issuer) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException, IOException {
    String url = this.url + "/";
    long timestamp = System.currentTimeMillis() * 1000;
    AssetType aType = new AssetType(assetType, precision);
    String assetAddr = DeriveAssetAddress(name, issuer, supply, reference, aType, timestamp);
    CreateAssetHeader aHead = new CreateAssetHeader(name, assetAddr, supply, aType, reference, issuer);

    // Prepare Bytes to sign [Txtype, timezone, metadata]
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);

    aHead.writeBinary(stream);

    stream.flush();
    byte[] convertedBytes = data.toByteArray();
    byte[] signedBytes = SignBytes(convertedBytes, privateKey);
    String signature = Sign(signedBytes);

    Actions actions = new Actions(TxTypeEnum.CREATE_ASSET.name, aHead);
    Type type = new Type(TxTypeEnum.TX_ASSET.name, actions);
    Transaction trans = new Transaction(timestamp, signature, fromAddr, type);
    TransactionParams tx = new TransactionParams(trans);


    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);
    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_RESP_OK.name);

    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .setPrettyPrinting()
        .create();

    Response asset = gson.fromJson(output, Response.class);

    if (asset.getClass() == ResponseOkay.class) {
      return new Response(assetAddr, "assetAddress");
    } else {
      return asset;
    }
  }

  public Response createContract(PrivateKey privateKey, String fromAddr, String script) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    String url = this.url + "/";
    Integer TxType = TxTypeEnum.CREATE_CONTRACT.value;
    long timestamp = System.currentTimeMillis() * 1000;

    String contractAddress = DeriveContractAddress(script);
    CreateContractHeader aHead = new CreateContractHeader(script, fromAddr, contractAddress, timestamp);

    // Prepare Bytes to sign [Txtype, timezone, metadata]
    ByteArrayOutputStream data = new ByteArrayOutputStream();
    DataOutputStream stream = new DataOutputStream(data);

    aHead.writeBinary(stream);
    stream.flush();
    byte[] convertedBytes = data.toByteArray();

    // Sign already!
    byte[] signedBytes = SignBytes(convertedBytes, privateKey);
    String signature = Sign(signedBytes);

    //Serialize
    Actions actions = new Actions(TxTypeEnum.CREATE_CONTRACT.name, aHead);
    Type type = new Type(TxTypeEnum.TX_CONTRACT.name, actions);
    Transaction trans = new Transaction(timestamp, signature, fromAddr, type);
    TransactionParams tx = new TransactionParams(trans);

    // Json to string
    String params = gson.toJson(tx, TransactionParams.class);
    // Call the Server
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_RESP_OK.name);

    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .setPrettyPrinting()
        .create();

    Response contract = gson.fromJson(output, Response.class);
    if (contract.getClass() == ResponseOkay.class) {
      return new Response(contractAddress, "contractAddress");
    } else {
      return contract;
    }
  }

  public Response transferAsset(String fromAddr, String assetAddr, String toAddr, Integer balance, PrivateKey privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, FileNotFoundException, InvalidKeySpecException {
    String url = this.url + "/";
    Integer TxType = TxTypeEnum.TRANSFER_ASSET.value;
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
    Actions actions = new Actions(TxTypeEnum.TRANSFER_ASSET.name, aHead);
    Type type = new Type(TxTypeEnum.TX_ASSET.name, actions);
    Transaction trans = new Transaction(timestamp, signature, fromAddr, type);
    TransactionParams tx = new TransactionParams(trans);

    // Json to string
    String params = gson.toJson(tx, TransactionParams.class);
    // Call the Server
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_RESP_OK.name);

    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .create();

    return gson.fromJson(output, Response.class);
  }

  public Response revokeAccount(PrivateKey privateKey, String fromAddr, String acctAddress) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    String url = this.url + "/";
    Integer TxType = TxTypeEnum.REVOKE_ACCOUNT.value;
    long timestamp = System.currentTimeMillis() * 1000;

    // b58 decode address
    byte[] byteAcctAddress = Base58convert.decode(acctAddress);

    // Prepare Bytes to sign
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);
    stream.writeByte(TxTypeEnum.REVOKE_ACCOUNT.value);
    stream.write(byteAcctAddress);
    stream.flush();
    byte[] convertedBytes = data.toByteArray();

    // Sign Bytes
    byte[] signedBytes = SignBytes(convertedBytes, privateKey);
    String signature = Sign(signedBytes);
    String toAddr = null;
    // Serialize parameters
    RevokeAccountHeader aHead = new RevokeAccountHeader(acctAddress);
    Actions actions = new Actions(TxTypeEnum.REVOKE_ACCOUNT.name, aHead);
    Type type = new Type(TxTypeEnum.TX_ACCOUNT.name, actions);
    Transaction trans = new Transaction(timestamp, signature, fromAddr, type);
    TransactionParams tx = new TransactionParams(trans);

    // Make it a json string
    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_RESP_OK.name);

    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .create();

    return gson.fromJson(output, Response.class);
  }

  public Response callContract(PrivateKey privateKey, String fromAddr, String contractAddress, String contractMethod, HashMap<String, String> arguments) throws IOException, SignatureException, InvalidKeyException, NoSuchAlgorithmException {
    String url = this.url + "/";
    long timestamp = System.currentTimeMillis() * 1000;

    List<Map<String, String>> argsList = new ArrayList<>();
    arguments.forEach((type, value) -> {

      if (FCLEnum.VACCOUNT.human.equals(type)) {
        Map acctArg = new HashMap();
        acctArg.put(GeneralEnum.TAG.name, FCLEnum.VACCOUNT.name);
        acctArg.put(GeneralEnum.CONTENTS.name, value);
        argsList.add(acctArg);
      }
      if (FCLEnum.VASSET.human.equals(type)) {
        Map assetArg = new HashMap();
        assetArg.put(GeneralEnum.TAG.name, FCLEnum.VASSET.name);
        assetArg.put(GeneralEnum.CONTENTS.name, value);
        argsList.add(assetArg);
      }
      if (FCLEnum.VCONTRACT.human.equals(type)) {
        Map ctrctArg = new HashMap();
        ctrctArg.put(GeneralEnum.TAG.name, FCLEnum.VCONTRACT.name);
        ctrctArg.put(GeneralEnum.CONTENTS.name, value);
        argsList.add(ctrctArg);
      }
      if (FCLEnum.VINT.human.equals(type)) {
        long vB = Long.valueOf(value);
        Map intArg = new HashMap();
        intArg.put(GeneralEnum.TAG.name, FCLEnum.VINT.name);
        intArg.put(GeneralEnum.CONTENTS.name, vB);
        argsList.add(intArg);
      }
      if (FCLEnum.VFLOAT.human.equals(type)) {
        double vB = Double.parseDouble(value);
        Map floatArg = new HashMap();

        floatArg.put(GeneralEnum.TAG.name, FCLEnum.VFLOAT.name);
        floatArg.put(GeneralEnum.CONTENTS.name, vB);
        argsList.add(floatArg);
      }
      if (FCLEnum.VBOOL.human.equals(type)) {
        boolean vB = Boolean.parseBoolean(value);
        Map boolArg = new HashMap();

        boolArg.put(GeneralEnum.TAG.name, FCLEnum.VBOOL.name);
        boolArg.put(GeneralEnum.CONTENTS.name, vB);
        argsList.add(boolArg);
      }
      if (FCLEnum.VMSG.human.equals(type)) {
        Map msgArg = new HashMap();
        msgArg.put(GeneralEnum.TAG.name, FCLEnum.VMSG.name);
        msgArg.put(GeneralEnum.CONTENTS.name, value);
        argsList.add(msgArg);
      }
      if (FCLEnum.VDATETIME.human.equals(type)) {

        Map msgArg = new HashMap();
        msgArg.put(GeneralEnum.TAG.name, FCLEnum.VDATETIME.name);
        msgArg.put(GeneralEnum.CONTENTS.name, value.toString());
        argsList.add(msgArg);
      }
    });

    // Prepare Bytes to sign
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);

    CallContractHeader aHead = new CallContractHeader(contractAddress, contractMethod, argsList);
    aHead.writeBinary(stream);

    stream.flush();
    byte[] convertedBytes = data.toByteArray();

    // Sign Bytes
    byte[] signedBytes = SignBytes(convertedBytes, privateKey);
    String signature = Sign(signedBytes);


    // Serialize parameters
    Actions actions = new Actions(TxTypeEnum.CALL_CONTRACT.name, aHead);
    Type type = new Type(TxTypeEnum.TX_CONTRACT.name, actions);
    Transaction trans = new Transaction(timestamp, signature, fromAddr, type);
    TransactionParams tx = new TransactionParams(trans);

    // Make it a json string
    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);
    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_RESP_OK.name);
    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .create();

    return gson.fromJson(output, Response.class);
  }

  public Response circulateAsset(PrivateKey privateKey, String fromAddr, String assetAddress, int amount) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    String url = this.url + "/";
    Integer TxType = TxTypeEnum.CIRCULATE_ASSET.value;
    long timestamp = System.currentTimeMillis() * 1000;

    // b58 decode address
    byte[] byteAssetAddress = Base58convert.decode(assetAddress);

    // Prepare Bytes to sign
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);
    stream.writeShort(TxType);
    stream.write(byteAssetAddress);
    stream.writeLong(amount);
    stream.flush();
    byte[] convertedBytes = data.toByteArray();

    // Sign Bytes
    byte[] signedBytes = SignBytes(convertedBytes, privateKey);
    String signature = Sign(signedBytes);

    // Serialize parameters
    CirculateHeader aHead = new CirculateHeader(assetAddress, amount);
    Actions actions = new Actions(TxTypeEnum.CIRCULATE_ASSET.name, aHead);
    Type type = new Type(TxTypeEnum.TX_ASSET.name, actions);
    Transaction trans = new Transaction(timestamp, signature, fromAddr, type);
    TransactionParams tx = new TransactionParams(trans);

    // Make it a json string
    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);
    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(AccountWrap.class, ResponseEnum.RPC_RESP.name);
    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .create();

    return gson.fromJson(output, ResponseOkay.class);
  }

  // Not Implemented
  public Response BindAsset(PrivateKey privateKey, String fromAddr, String contractAddress, String assetAddress) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    String url = this.url + "/";
    Integer TxType = TxTypeEnum.BIND_ASSET.value;
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
    Transaction trans = new Transaction(timestamp, signature, fromAddr, type);
    TransactionParams tx = new TransactionParams(trans);

    // Make it a json string
    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(AssetWrap.class, ResponseEnum.RPC_RESP.name);

    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .create();

    return gson.fromJson(output, Response.class);
  }

  public RuntimeTypeAdapterFactory<Response> RTAFgenerator(Class responseTypeClass, String RespName) {
    return (RuntimeTypeAdapterFactory<Response>) RuntimeTypeAdapterFactory
        .of(Response.class, GeneralEnum.TAG.name)
        .registerSubtype(ErrorResp.class, ResponseEnum.RPC_RESP_ERROR.name)
        .registerSubtype(responseTypeClass, RespName);
  }

  private static final char[] HEXCHARS_LC = "0123456789abcdef".toCharArray();

  public String HexDump(final byte[] buf) {
    final StringBuffer sb = new StringBuffer(3 * buf.length);
    for (int i = 0; i < 0 + buf.length; ++i) {
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
