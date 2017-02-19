package querymanager.rewriting;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;


public class WordSegment {	
	
	private static WordSegment ws;
	private TTransport transport;
	private TProtocol protocol;
	private SegmentService.Client client;
	
	private WordSegment(){
		
	}
	
	public static WordSegment getInstance(){
        if (ws == null) {
            synchronized (WordSegment.class) {
                if (ws == null) {
                    ws = new WordSegment();
                    return ws;
                }
            }
        }
        return ws;
	}
	
	public void init(String url,int port){
        //配置服务端的请求信息
        transport = new TSocket(url, port);
        try {
            transport.open();
            protocol = new TBinaryProtocol(transport);
            client = new SegmentService.Client(protocol);
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
	}

	public void close(){
		transport.close();
	}
	
	
	public String[] querySegment(String q){				
		String rs="";
        //接口调用
        try {
			rs = client.segment(ToDBC(q));
			System.out.println("word segment:" + rs);  
		} catch (Exception e) {	
			// TODO Auto-generated catch block
			return q.split("/");
		}
        //打印调用结果       
        return rs.split("/");
	}
	
    /**
     * 全角转半角
     * @param input
     * @return
     */
    private String ToDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
          if (c[i] == '\u3000') {
            c[i] = ' ';
          } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
            c[i] = (char) (c[i] - 65248);
          }
        }
        String returnString = new String(c);  
        return returnString;
    }
	
}
