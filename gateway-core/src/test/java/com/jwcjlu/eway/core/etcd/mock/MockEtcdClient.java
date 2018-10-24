package com.jwcjlu.gateway.core.etcd.mock;


import com.coreos.jetcd.data.KeyValue;
import com.jwcjlu.gateway.core.etcd.EtcdClient;
import com.jwcjlu.gateway.core.etcd.support.JEtcdClient;
import org.junit.Test;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MockEtcdClient {
     EtcdClient client= new JEtcdClient("http://10.60.38.51:2379");
     @Test
     public void testGet(){
       List<KeyValue> keyValueList= client.getKey("/test/ok");
       if(keyValueList==null)return;
       keyValueList.forEach((kv)->{
           System.out.println(kv.getKey().toStringUtf8());
           System.out.println(kv.getValue().toStringUtf8());
       });
     }
     @Test
     public void createEphemeral() throws InterruptedException {
         client.createEphemeral("/gateway/errrs");
         TimeUnit.SECONDS.sleep(10);
     }


    @Test
    public void  testWatch()throws Exception {
      List<String> childrens=  client.addChildListener("/gateway/service", (path,children)->
            System.out.println(path+"|||||childerns["+children+"]"));
       System.out.println(childrens);
        System.in.read();
    }

    // watch executes the "watch" command.
    @Test
    public void watch() throws Exception {
        client.addDataListener("/test/ok",(path,data)->{System.out.println(path+"["+data+"]");});
        System.in.read();
     /*  Watch.Watcher watcher = null;
        try {
            watcher = client.getWatchClient().watch(
                ByteSequence.fromString("/test/ok"),
                WatchOption.newBuilder().build()
            );

            while (true) {
                WatchResponse response = watcher.listen();
                for (WatchEvent event : response.getEvents()) {
                    System.out.println(event.getEventType().toString());
                    System.out.println(event.getKeyValue().getKey().toStringUtf8());
                    System.out.println(event.getKeyValue().getValue().toStringUtf8());
                }
            }

        } catch (Exception e) {
            if (watcher != null) {
                watcher.close();
            }
            throw e;
        }*/
    }
}
