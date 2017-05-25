package querymanager.rewriting;

import java.util.List;
import java.util.Scanner;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * @author dwt
 * Get similar words by word2vec model
 */
public class RewritingHandler {

	private static RewritingHandler rewrite;
	private TTransport transport;
	private TProtocol protocol;
	private RewritingService.Client client;
	
	private RewritingHandler(){
		
	}
	
	public static RewritingHandler getInstance(){
        if (rewrite == null) {
            synchronized (RewritingHandler.class) {
                if (rewrite == null) {
                	rewrite = new RewritingHandler();
                    return rewrite;
                }
            }
        }
        return rewrite;
	}
	
	public void init(String url,int port){
        //配置服务端的请求信息
        transport = new TSocket(url, port);
        try {
            transport.open();
            protocol = new TBinaryProtocol(transport);
            client = new RewritingService.Client(protocol);
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (Exception e) {	
			// TODO Auto-generated catch block
			return ;
		}
	}
	
	public void close(){
		transport.close();
	}
	
	public String rewriteQuery(String query){
		String rs="";
		 //接口调用
        try {
			rs = client.rewrite(query);
			System.out.println("rewrite result:" + rs);  
		} catch (TException e) {
			e.printStackTrace();
		} catch (Exception e) {	
			return rs;
		}
		return rs;
	}
	
	public static void main(String[] args){
		RewritingHandler.getInstance().init("127.0.0.1", 9090);
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入:");
		while (sc.hasNext()){
			String s = sc.nextLine();
			System.out.println(RewritingHandler.getInstance().rewriteQuery(s));
		}
	}
	
}
