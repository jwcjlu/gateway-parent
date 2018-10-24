package com.jwcjlu.gateway.admin;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.kv.GetResponse;

import javax.xml.crypto.Data;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;


/**
 * @author chengzhuantuo
 */
public class App {

    public static void main(String[] args) {
       /* args = new String[] { "-configfile", "F:\\newProject1\\gateway\\gateway\\gateway-admin\\src\\main\\resources\\mybatis-generator-config.xml", "-overwrite" };
        ShellRunner.main(args);*/

        // create client

        //ByteSequence key = ByteSequence.fromString("test_key");
       // ByteSequence value = ByteSequence.fromString("test_value");

        // put the key-value

            Calendar calendar = Calendar.getInstance();
            Date timestamp = calendar.getTime();
            for(int j = 0;j<10;j++){
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Client client = Client.builder().endpoints("http://10.60.38.51:2379").build();
                            KV kvClient = client.getKVClient();
                            for(int i =0;i< 1000;i++){
                                ByteSequence key = ByteSequence.fromString("test_key");
                                GetResponse get = kvClient.get(key).get();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }


                    }
                });
                thread.start();
            }


            // get the CompletableFuture
            /*CompletableFuture<GetResponse> getFuture = kvClient.get(key);

            // get the value from CompletableFuture
            GetResponse response = getFuture.get();

            // delete the key
            kvClient.delete(key).get();*/




    }




}

