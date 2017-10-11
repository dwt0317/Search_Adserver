package global.util;

import java.util.ArrayList;
import java.util.List;

import admanager.entity.Advertisement;
import usermanager.IDGenerator;

/**
 * @author dwt
 * 广告投放前处理工具类
 */
public class DeliverUtil {
	/**
	 * 将创意、impression id和位置信息写入html
	 */
	public static void buildHTMLCode(List<Advertisement> adList){
		for(Advertisement ad : adList){
			String code = "<div class=\"advertising\" "		//&quot;不能替换为\"
					+ "data-click=\"{" 
					+ " &quot;ad&quot;:&quot;"+ ad.getId() + "&quot;,"
					+ " &quot;im&quot;:&quot;"+ ad.getImpressionID() + "&quot;,"
					+ " &quot;po&quot;:&quot;"+ ad.getPosition() + "&quot;"
					+ "}\" >";
			System.out.println(code);
			code += ad.getUrl() + "</div>"; 
			ad.setCode(code);		
		}
	}
	
	/**
	 * 生成impression Id
	 */
	public static void generateImpressionID(List<Advertisement> adList){
		for (Advertisement ad: adList){
			ad.setImpressionID("i" + IDGenerator.generate());
		}
	}
	
	//naive ad position assignment
	public static void assignPosition(List<Advertisement> adList){
		int position = 0;
		for (Advertisement ad: adList){
			ad.setPosition(String.valueOf(position++));
		}
	}
}
