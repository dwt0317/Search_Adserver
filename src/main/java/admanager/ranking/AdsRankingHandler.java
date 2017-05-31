package admanager.ranking;

import java.util.Scanner;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;


public class AdsRankingHandler {
	private static AdsRankingHandler adRanker;
	private TTransport transport;
	private TProtocol protocol;
	private AdsRankingService.Client client;
	
	private AdsRankingHandler(){
		
	}
	
	public static AdsRankingHandler getInstance(){
        if (adRanker == null) {
            synchronized (AdsRankingHandler.class) {
                if (adRanker == null) {
                	adRanker = new AdsRankingHandler();
                    return adRanker;
                }
            }
        }
        return adRanker;
	}
	
	public void init(String url,int port){
        //配置服务端的请求信息
        transport = new TSocket(url, port);
        try {
            transport.open();
            protocol = new TBinaryProtocol(transport);
            client = new AdsRankingService.Client(protocol);
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
	
	public double ranking (String ad_query){
		double rs = .0;
		 //接口调用
        try {
			rs = client.ranking(ad_query);
//			System.out.println("ad ranking:" + rs);  
		} catch (TException e) {
			e.printStackTrace();
		} catch (Exception e) {	
			return rs;
		}
		return rs;
	}
	
	public static void main(String[] args){
		AdsRankingHandler.getInstance().init("127.0.0.1", 9095);
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入:");
		while (sc.hasNext()){
			String s = sc.nextLine();
			System.out.println(AdsRankingHandler.getInstance().ranking(s));
		}
	}
}
