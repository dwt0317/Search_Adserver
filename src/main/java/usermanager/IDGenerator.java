package usermanager;

import java.lang.management.ManagementFactory;

import com.alibaba.fastjson.JSONObject;

public class IDGenerator {
	

	
	public static String generate(){
		String id = "";
		id += System.getProperty("user.name");	//用户名（机器名）
		id += String.valueOf(System.nanoTime());	//时间		
		// get name representing the running Java virtual machine.    
		String name = ManagementFactory.getRuntimeMXBean().getName();    
		String pid = name.split("@")[0];    //进程号
		id += pid;	
		return id;
	}
	
	public static String generate2JSON(){
		String id = generate();
		id = "{\"id\":\"" + id +"\"}";	// convert to json format
		return id;
	}
	
	public static void main (String args[]){
		System.out.println(generate());
	}
}
