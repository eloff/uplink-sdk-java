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
import adjoint.uplink_sdk.client.parameters.wrappers.MemPoolWrap;

import adjoint.uplink_sdk.client.header.RevokeAccountHeader;
import adjoint.uplink_sdk.client.header.CreateContractHeader;
import adjoint.uplink_sdk.client.header.TransferHeader;
import adjoint.uplink_sdk.client.header.CreateAccountHeader;
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

import adjoint.uplink_sdk.client.Version;

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
   * @link https://www.adjoint.io/docs/rpc.html#endpoints
   */
  public UplinkSDK(String protocol, String address, String port) {
    this.url = protocol + address + ":" + port; // address is "https://xx.xxx.xxx", port is "1234"
  }

  /**
   * Returns all blocks and associated transactions on the chain.
   * @returns all blocks
   * @url /blocks
   */
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

  /**
   * Returns all available peers (Uplink nodes).
   * @returns all peers
   * @url /peers
   */
  public Response getPeers() {
    String url = this.url + "/peers";
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(PeersWrap.class, ResponseEnum.RPC_RESP.name);

    Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();

    return gson.fromJson(output, Response.class);
  }

  /**
   * Returns all accounts on the chain.
   * @return all accounts
   * @url /accounts
   */
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

  /**
   * Returns a specific account and its details
   * @param Address an account address
   * @return account with specified address
   * @url /accounts/{address}
   */
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

  /**
   * Returns all assets on chain.
   * @return all assets
   * @url /assets
   */
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

  /**
   * Returns all transactions associated to a specific block.
   * @param BlockId block identifier
   * @return transactions in block
   * @url /transactions/{BlockId}
   */
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

  /**
   * Returns list of unconfirmed transactions
   * @return list of unconfirmed transactions on current node
   * @url /transactions/pool
   */
  public Response getMemPool() {
    String url = this.url + "/transactions/pool";
    String params = "";
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapterResp = RTAFgenerator(MemPoolWrap.class, ResponseEnum.RPC_RESP.name);

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

  /**
   * Returns all contracts on the chain.
   * @return all contracts
   * @url /contracts
   */
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

  /**
   * Returns a specific contract and its associated details.
   * @param address contract address
   * @return a specific contract
   * @url /contracts/{contract_address}
   */
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

  /**
   * Create an account.
   * @param Timezone Timezone of account
   * @param meta Metadata map of custom strings
   * @param privateKey Private key for new account
   * @param publicKey Public key for new account
   * @param fromAddress derived from public key of new account
   * @return new account address - will be the same as fromAddress
   * @url /
   */
  public Response createAccount(
      String Timezone,
      Map<String, String> meta,
      PrivateKey privateKey,
      PublicKey publicKey,
      String fromAddress
  ) throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException {
    String url = this.url + "/";

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
    Transaction trans = new Transaction(signature, origin, type);
    TransactionParams tx = new TransactionParams(trans);
    // Make it a json string
    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);
    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_TX_OK.name);

    Gson gson = new GsonBuilder()
        .disableHtmlEscaping()
        .registerTypeAdapterFactory(adapter)
        .create();

    return gson.fromJson(output, Response.class);
  }

  /**
   * Create an asset.
   * @param privateKey Private key of account creating asset
   * @param fromAddr Address of Account creating asset
   * @param name Name of new asset
   * @param supply Supply of new asset
   * @param assetType Discrete, Fractional, or Binary
   * @param precision Precision of Fractional assets
   * @param reference Token, Security, USD, GBP, CHF, EUR
   * @param issuer Address of account creating asset - same as fromAddr
   * @return new asset address
   * @url /
   */
  public Response createAsset(PrivateKey privateKey, String fromAddr, String name, int supply, String assetType, Integer precision, String reference, String issuer, Map<String, String> meta) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException, IOException {
    String url = this.url + "/";
    AssetType aType = new AssetType(assetType, precision);
    String assetAddr = DeriveAssetAddress(name, issuer, supply, reference, aType);
    CreateAssetHeader aHead =
        new CreateAssetHeader(name, supply, aType, reference, issuer, meta);

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
    Transaction trans = new Transaction(signature, fromAddr, type);
    TransactionParams tx = new TransactionParams(trans);


    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);
    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_TX_OK.name);

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

  /**
   * Create contract.
   * @param privateKey Private key of account creating contract
   * @param fromAddr address of account creating contract
   * @param script FCL contract code
   * @param privateKey Private key for new account
   * @return new contract address
   * @url /
   */
  public Response createContract(
      PrivateKey privateKey,
      String fromAddr,
      String script
  ) throws
      IOException,
      NoSuchAlgorithmException,
      InvalidKeyException,
      SignatureException {
    String url = this.url + "/";

    String contractAddress = DeriveContractAddress(script);
    CreateContractHeader aHead =
        new CreateContractHeader(script, fromAddr, contractAddress);

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
    Transaction trans = new Transaction(signature, fromAddr, type);
    TransactionParams tx = new TransactionParams(trans);

    // Json to string
    String params = gson.toJson(tx, TransactionParams.class);
    // Call the Server
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_TX_OK.name);

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

  /**
   * Transfer asset holdings from one account to another.
   * @param fromAddr Address of account transferring asset holdings
   * @param assetAddr Address of asset to transfer
   * @param toAddr Address of account to transfer asset holdings to
   * @param balance Amount of asset holdings being transferred
   * @param privateKey Private key of account doing transfer
   * @url /
   */
  public Response transferAsset(String fromAddr, String assetAddr, String toAddr, Integer balance, PrivateKey privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, FileNotFoundException, InvalidKeySpecException {
    String url = this.url + "/";

    // b58 decode
    byte[] byteAssetAddr = Base58convert.decode(assetAddr);
    byte[] byteToAddr = Base58convert.decode(toAddr);

    // Prepare Bytes to sign [Txtype, asset_addr, to_addr, balance]
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);
    stream.writeShort(TxTypeEnum.TRANSFER_ASSET.fstFlag);
    stream.writeShort(TxTypeEnum.TRANSFER_ASSET.sndFlag);
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
    Transaction trans = new Transaction(signature, fromAddr, type);
    TransactionParams tx = new TransactionParams(trans);

    // Json to string
    String params = gson.toJson(tx, TransactionParams.class);
    // Call the Server
    String output = request.Call(url, params);

    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_TX_OK.name);

    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .create();

    return gson.fromJson(output, Response.class);
  }

  /**
   * Revoke Account access to ledger. Can only be initiated by the same account to be revoked.
   * @param privateKey Private key of account being revoked
   * @param fromAddr address of account being revoked
   * @param acctAddress address of account being revoked
   * @url /
   */
  public Response revokeAccount(PrivateKey privateKey, String fromAddr, String acctAddress) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    String url = this.url + "/";

    // b58 decode address
    byte[] byteAcctAddress = Base58convert.decode(acctAddress);

    // Prepare Bytes to sign
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);
    stream.writeByte(TxTypeEnum.REVOKE_ACCOUNT.fstFlag);
    stream.writeByte(TxTypeEnum.REVOKE_ACCOUNT.sndFlag);
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
    Transaction trans = new Transaction(signature, fromAddr, type);
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

  /**
   * Call contract method.
   * @param privateKey Private key of account calling contract method
   * @param fromAddr Address of account calling contract method
   * @param contractAddress Address of contract being called
   * @param contractMethod Contract method being called
   * @param arguments HashMap of arguments to method
   * @url /
   */
  public Response callContract(PrivateKey privateKey, String fromAddr, String contractAddress, String contractMethod, HashMap<String, String> arguments) throws IOException, SignatureException, InvalidKeyException, NoSuchAlgorithmException, Exception {
    String url = this.url + "/";

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
      if (FCLEnum.VFIXED.human.equals(type)) {
        double doubleVal = Double.parseDouble(value);
        int integerPlaces = value.indexOf('.');
        int decimalPlaces = value.length() - integerPlaces - 1;

        if(decimalPlaces > 6){
          try {
            throw new Exception("Too many decimal places. Fixed type must be 6 places of precision or less.");
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        if(decimalPlaces <= 0){
          try {
            throw new Exception("Fixed type should have more than 0 decimal places.");
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        Map msgArg = new HashMap();
        msgArg.put(GeneralEnum.TAG.name, FCLEnum.VFIXED.name + Integer.toString(decimalPlaces));
        msgArg.put(GeneralEnum.CONTENTS.name, doubleVal);
        argsList.add(msgArg);
      }
      if (FCLEnum.VENUM.human.equals(type)) {
        Map msgArg = new HashMap();
        msgArg.put(GeneralEnum.TAG.name, FCLEnum.VENUM.name);
        msgArg.put(GeneralEnum.CONTENTS.name, value);
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
    Transaction trans = new Transaction(signature, fromAddr, type);
    TransactionParams tx = new TransactionParams(trans);

    // Make it a json string
    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);
    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(ResponseOkay.class, ResponseEnum.RPC_TX_OK.name);
    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .create();

    return gson.fromJson(output, Response.class);
  }

  /**
   * Circulate asset holdings to account that created asset.
   * @param privateKey Private key of account circulating asset
   * @param fromAddr Account address circulating asset
   * @param assetAddress Asset address being circulated
   * @param amount Amount of holdings being circulated
   * @url /
   */
  public Response circulateAsset(PrivateKey privateKey, String fromAddr, String assetAddress, int amount) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    String url = this.url + "/";

    // b58 decode address
    byte[] byteAssetAddress = Base58convert.decode(assetAddress);

    // Prepare Bytes to sign
    final ByteArrayOutputStream data = new ByteArrayOutputStream();
    final DataOutputStream stream = new DataOutputStream(data);
    stream.writeShort(TxTypeEnum.CIRCULATE_ASSET.fstFlag);
    stream.writeShort(TxTypeEnum.CIRCULATE_ASSET.sndFlag);
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
    Transaction trans = new Transaction(signature, fromAddr, type);
    TransactionParams tx = new TransactionParams(trans);

    // Make it a json string
    String params = gson.toJson(tx, TransactionParams.class);

    // Call the Server
    String output = request.Call(url, params);
    RuntimeTypeAdapterFactory<Response> adapter = RTAFgenerator(AccountWrap.class, ResponseEnum.RPC_TX_OK.name);
    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(adapter)
        .create();

    return gson.fromJson(output, ResponseOkay.class);
  }

  /**
   * Handles json for successful or unsuccessful response types.
   * @param responseTypeClass Wrapper java object of response type
   * @param RespName Response name: RPCResp, RPCRespOK, RPCRespError
   */
  public RuntimeTypeAdapterFactory<Response> RTAFgenerator(Class responseTypeClass, String RespName) {
    return (RuntimeTypeAdapterFactory<Response>) RuntimeTypeAdapterFactory
        .of(Response.class, GeneralEnum.TAG.name)
        .registerSubtype(ErrorResp.class, ResponseEnum.RPC_RESP_ERROR.name)
        .registerSubtype(responseTypeClass, RespName);
  }

  private static final char[] HEXCHARS_LC = "0123456789abcdef".toCharArray();
  /**
   * Utility function for debugging binary serialization of transaction headers.
   * @param buf bytes to dump into hexadecimal format
   * @returns hexadecimal string
   */
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

  public Version getUplinkVersion() {
    String url = this.url + "/version";
    String params = "";
    String output = request.Call(url, params);
    return new Gson().fromJson(output, Version.class);
  }
}
