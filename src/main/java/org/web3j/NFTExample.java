package org.web3j;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import org.web3j.crypto.Credentials;
import org.web3j.generated.contracts.MyNFT;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class NFTExample {
    public static final String ADMIN_PRIVATE_KEY = "a0bd91e26132e73de43bcae8174dd829cb9379d0be6f1fd2408bd3dc483ae67a";
    public static void main(String[] args) throws Exception {
        Credentials credentials = Credentials.create(ADMIN_PRIVATE_KEY);
        Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:7545"));
        List<String> accounts = web3j.ethAccounts().send().getAccounts();
        MyNFT myNFT = MyNFT.deploy(web3j, credentials, new DefaultGasProvider(), "FirstClassName", "FirstClassSymbol").send();

//        MyNFT myNFT1 = MyNFT.load("0x115A0688BaDcD5F8f400fF5e4C40c8905B008179", web3j, credentials, new DefaultGasProvider());
        System.out.println("Contract address: " + myNFT.getContractAddress());
        System.out.println("Deployed address: " + myNFT.getDeployedAddress("5777"));
        TransactionReceipt transactionReceipt = myNFT.mintToken(accounts.get(4), "buymynft.com").send();
        System.out.println("Transaction status is: " + transactionReceipt.getStatus());


        System.out.println("Balance of account 4 is: " + myNFT.balanceOf(accounts.get(4)).send().toString());

        // Run this after running "ipfs daemon" on CMD
        IPFS ipfs = new IPFS("localhost", 5001);
        try {
            NamedStreamable.InputStreamWrapper file = new NamedStreamable.InputStreamWrapper(new FileInputStream("./src/main/resources/hello.txt"));
            MerkleNode response = ipfs.add(file).get(0);
            System.out.println("Response Hash: " + response.hash.toString());
        } catch (IOException ex) {
            throw new RuntimeException("Error while communicating with the IPFS node", ex);
        }
    }
}
