import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.wltea.analyzer.IKSegmentation;

import fileclass.FileOperator;


public class TestPaper {
	
	static TextSimilar comText = new TextSimilar();
	
	public static void chooseFile(List<ArrayList<String>> list) throws IOException {
		// i为偶数表示不相似的值，i为奇数表示相似的值
		// j表示一个具体的文章
		// k表示和j比较的文章，遍历同一个文件夹中的没有

		for (int i = 0; i < list.size(); i += 2) {
			
			List<String> Item_unsimilar = list.get(i);
			List<String> Item_similar = list.get(i + 1);

			for (int j = 0; j < Item_unsimilar.size(); j++) {
				for (int k = j + 1; k < Item_unsimilar.size(); k++) {
					float res = comText.computeSimular(Item_unsimilar.get(j), Item_unsimilar.get(k));
					comText.computedata(res, false);
				}
				
				for (int j_similar = 0; j_similar < Item_similar.size(); j_similar++) {

					float res = comText.computeSimular(Item_unsimilar.get(j),
							Item_similar.get(j_similar));

					comText.computedata(res, false);
				}
			}
			
			for (int j = 0; j < Item_similar.size(); j++) {
				for (int k = j + 1; k < Item_similar.size(); k++) {
					float res = comText.computeSimular(Item_similar.get(j),
							Item_similar.get(k));
					comText.computedata(res, true);
				}
			}
			
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new IKSegmentation(new StringReader("厦门大学"));

		long time_begin = System.currentTimeMillis();
		System.out.println(new Date());
		
		FileOperator testFile = new FileOperator();
		List<ArrayList<String>> list = testFile.getData("测试");
		
		try {
			chooseFile(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		comText.showResult();
		
		long time_end = System.currentTimeMillis();
		System.out.println(new Date());
		
		System.out.println("所用时间" + (time_end - time_begin));
	}

}
