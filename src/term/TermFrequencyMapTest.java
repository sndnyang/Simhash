package term;

import java.io.IOException;
import java.util.HashMap;

import junit.framework.TestCase;

public class TermFrequencyMapTest extends TestCase {

	public void setUp() throws Exception {
	}

	public void tearDown() throws Exception {
	}

	/*
	 * public void testGenerateMap() { String sentence =
	 * "新西兰奶粉出口协会决定对中国市场上鱼龙混杂的200多个号称新西兰配方奶粉品牌不再坐视不管，" +
	 * "并携协会成员企业主动到中国整顿“山寨”新西兰奶粉。4月13日，新西兰婴儿配方奶粉出口协会在京表示" +
	 * "，目前中国市场销售的来自新西兰配方奶粉多达200多个品牌，但是真正新西兰本土企业的品牌并不多，" +
	 * "目前也只有6个企业的20多个品牌销往中国市场。为了保护新西兰本土奶粉品牌和声誉，" +
	 * "该协会负责人表示，他们将通过各种途径告知中国消费者，哪些才是真正的新西兰奶粉品牌。"; TermFrequencyMap map = new
	 * TermFrequencyMap(sentence); try { map.generateMap(sentence); } catch
	 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace();
	 * } HashMap<String, Integer> result = map.getTermFrequency();
	 * System.out.print(result); }
	 */

	public void testGenerateMap() {
		String sentence = "买红妹主持节目扭伤腰 动弹不得发出惨叫(独家)"
				+ "7月31日下午，买红妹现身山东卫视《不亦乐乎》主持节目，意外扭伤腰，"
				+ "躺在软垫上动弹不得，迫使节目中断。最后节目组急忙打120叫来救护车对她进行急救。"
				+ "网易娱乐独家专稿（图文/Juna 视频/金山） 买红妹最近因孙楠离婚事件成为公众视线焦点，"
				+ "7月31日下午现身山东卫视《不亦乐乎》主持节目。"
				+ "就在临近结尾玩踩气球游戏时买红妹由于动作幅度过大意外扭伤腰，"
				+ "躺在软垫上动弹不得，迫使节目中断。最后节目组急忙打120，"
				+ "傍晚7时许济南市急救中心的救护车开到山东电视台将买红妹送往按摩中心推拿。"
				+ "玩游戏用力过猛扭伤 躺着录完结束语"
				+ "买红妹今天下午在录制《不亦乐乎》的最后一个游戏环节中由于动作幅度过大，"
				+ "整个人扑倒并发出惨叫，其他主持人和工作人员赶忙上前想要将她扶起，熟料她却动弹不得，"
				+ "表情十分痛苦，说腰痛得直不起来。见到买红妹整张脸上满是汗水，节目组工作人员忙叫来医生，"
				+ "不一会儿医生即背着药箱到来，不过由于医生对腰伤不在行，不敢随便移动买红妹的身体"
				+ "，只得打电话叫120。当时节目正在录播，因为买红妹发生意外不得不暂停，"
				+ "不过最后还是让动弹不得的买红妹躺着说了几句结束语。 大呼“我不要去医院”"
				+ " “胡须男”陪同去推拿"
				+ "当时躺在软垫上的买红妹一听到要叫120忙表示自己并无大碍，只是腰一时间被“冲”到了，"
				+ "还大呼“我不要去医院”。不过大约过了30分钟后，济南市急救中心的救护车便开到山东卫视，"
				+ "买红妹躺着软垫上被抬上了救护车。网易娱乐随后从知情人处了解到买红妹坚决不肯去医院，"
				+ "最后经过熟人介绍到了一间按摩中心做推拿。直至晚上8点半，"
				+ "换了一身宽松黑裙的买红妹在男助手和电视台工作人员的搀扶下走出了按摩中心，"
				+ "相比起下午主持节目时的神采飞扬，她显得无精打采而且步履蹒跚。"
				+ "陪伴其左右的男助手正是之前被媒体拍到了“胡须男”，"
				+ "他以买红妹现在状态不佳为由拒绝接受任何的采访或拍照，只是十分敷衍地说："
				+ "“她现在很好，谢谢关心。” "
				+ "--------------------------------------------------------------------------------"
				+ "孙楠方面回应：助手尚未告知孙楠 希望买姐注意安全"
				+ "得知买红妹发生意外后，网易娱乐在第一时间联系上孙楠的经纪人贾鹏。听闻此事，"
				+ "正在外地与孙楠一起出差的贾鹏先是感到震惊，“什么时候的事情，我还没听说，"
				+ "严重吗？我马上给买姐打电话问一下。”随后，在与买红妹方面取得联系后，"
				+ "贾鹏又告诉记者：“我刚给买姐打了电话，没什么大碍，可能当时比较疼，"
				+ "不是已经出院了吗？明天她还会继续在山东录制节目呢。”对于孙楠对此事的反应，"
				+ "贾鹏则称，“我还没给孙楠说呢，也没什么大碍，就希望买姐注意安全吧。”";
		try {
			ENSD map = new ENSD(sentence, null);

			map.generateEle();
			HashMap<String, Integer> result = map.getTermFrequency();
			System.out.print(result);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TermFrequencyMapTest.class);
	}
}
