package querymanager.rewriting;

import java.util.List;

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
public class Similar {

	private static Similar sl;
	private TTransport transport;
	private TProtocol protocol;
	private SimilarService.Client client;
	
	private Similar(){
		
	}
	
	public static Similar getInstance(){
        if (sl == null) {
            synchronized (Similar.class) {
                if (sl == null) {
                	sl = new Similar();
                    return sl;
                }
            }
        }
        return sl;
	}
	
	public void init(String url,int port){
        //配置服务端的请求信息
        transport = new TSocket(url, port);
        try {
            transport.open();
            protocol = new TBinaryProtocol(transport);
            client = new SimilarService.Client(protocol);
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
	
	public String similarWords(List<String> qwords){
		String rs="";
		 //接口调用
        try {
			rs = client.similar(qwords);
			System.out.println("similar words:" + rs);  
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {	
			// TODO Auto-generated catch block
			return rs;
		}
		
		return rs;
	}
	
}
