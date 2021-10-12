package org.web3j;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.generated.contracts.FirstCoin;
import org.web3j.generated.contracts.HelloWorld;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>This is the generated class for <code>web3j new helloworld</code></p>
 * <p>It deploys the Hello World contract in src/main/solidity/ and prints its address</p>
 * <p>For more information on how to run this project, please refer to our <a href="https://docs.web3j.io/quickstart/#deployment">documentation</a></p>
 */
public class Web3App {
    public static String ADMIN_PRIVATE_KEY = "5738b096295a8aa8dda1e781b4d39108db7281d749cf72366cbad694af7f786b";
    public static void main(String[] args) throws Exception {

        Credentials credentials = Credentials.create(ADMIN_PRIVATE_KEY);
        Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:7545"));
        List<String> accounts = web3j.ethAccounts().send().getAccounts();
        System.out.println("Deploying HelloWorld contract ...");
        FirstCoin firstCoin = FirstCoin.deploy(web3j, credentials, new DefaultGasProvider(), BigInteger.valueOf(5000000)).send();

        System.out.println("Contract address: " + firstCoin.getContractAddress());

        String binary = firstCoin.getContractBinary();
        System.out.println("Contract binary: " + binary);

        firstCoin.transfer(accounts.get(1), BigInteger.valueOf(100)).send();
        System.out.printf("Balance of first account %s is: %s ", accounts.get(0), firstCoin.balanceOf(accounts.get(0)).send());
        System.out.printf("Balance of second account %s is: %s ", accounts.get(1), firstCoin.balanceOf(accounts.get(1)).send());


       Transaction transaction = Transaction.createContractTransaction(accounts.get(0), BigInteger.valueOf(1), null,
               BigInteger.valueOf(2), BigInteger.valueOf(1),"Hello World");

        BigInteger amountUsed = web3j.ethEstimateGas(transaction).send().getAmountUsed();
        // eth_gasPrice returns the current price per gas in weis.
        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        System.out.println("Estimated gas value is: " + amountUsed.toString());
        System.out.println("Gas price is: " + (ethGasPrice.getGasPrice().longValue()/1000000000));


        EthBlock ethBlock =
                web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
        long timestamp = ethBlock.getBlock().getTimestamp().longValueExact() * 1000;
        System.out.println(timestamp);



        // Gas limit below the fees
        // Where is base fees calculated? By the network/set by the miners
        // When we send the call, gas limit should not be below base fees
        // client can assert the gas limit
        // eth_call: No gas is required to run this call, eth_send
        // Next step: put it on issues and get it fixed.
        // Smart contract migration tool
        // BOM: purely NFT. Street art


        // Add a warning

//       CompletableFuture<EthEstimateGas> completableFuture = web3j.ethEstimateGas(transaction).sendAsync();
//       completableFuture.thenAccept(ethEstimateGas -> {
//           System.out.println("Estimated gas value is: " + ethEstimateGas.getAmountUsed().toString());
//       }).exceptionally(throwable -> {
//           System.out.println("Error is" + throwable.toString());
//           return null;
//       });
   }
}

