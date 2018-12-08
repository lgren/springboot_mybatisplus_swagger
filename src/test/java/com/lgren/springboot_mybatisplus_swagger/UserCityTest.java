package com.lgren.springboot_mybatisplus_swagger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Stopwatch;
import com.lgren.springboot_mybatisplus_swagger.entity.TagInfo;
import com.lgren.springboot_mybatisplus_swagger.entity.Teacher;
import com.lgren.springboot_mybatisplus_swagger.entity.UserCity;
import com.lgren.springboot_mybatisplus_swagger.service.ITagInfoService;
import com.lgren.springboot_mybatisplus_swagger.service.ITagLeafService;
import com.lgren.springboot_mybatisplus_swagger.service.ITeacherService;
import com.lgren.springboot_mybatisplus_swagger.service.IUserCityService;
import com.lgren.springboot_mybatisplus_swagger.util.LgrenUtil;
import com.lgren.springboot_mybatisplus_swagger.util.TagUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * TODO
 * @author Lgren
 * @create 2018-11-21 18:16
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableCaching
public class UserCityTest {
    @Autowired
    private ITagLeafService tagLeafService;
    @Autowired
    private ITagInfoService tagInfoService;
    @Autowired
    private IUserCityService userCityService;
    @Autowired
    private ITeacherService teacherService;

    String cityMapStr = "{\"004778\":\"郑州\",\"006714\":\"乌兰察布\",\"007803\":\"宿迁\",\"006955\":\"铁岭\",\"003446\":\"成都\",\"012041\":\"铜川\",\"008582\":\"泉州\",\"011068\":\"绵阳\",\"011187\":\"南充\",\"010896\":\"玉林\",\"011621\":\"黔南\",\"012710\":\"阿克苏\",\"007379\":\"大兴安岭\",\"009679\":\"开封\",\"012836\":\"塔城\",\"012837\":\"阿勒泰\",\"008466\":\"六安\",\"011988\":\"那曲\",\"009795\":\"安阳\",\"008342\":\"铜陵\",\"012838\":\"石河子\",\"006947\":\"盘锦\",\"002106\":\"清远\",\"001931\":\"天津\",\"000600\":\"佛山\",\"006823\":\"本溪\",\"006828\":\"丹东\",\"012272\":\"嘉峪关\",\"012026\":\"林芝\",\"011055\":\"德阳\",\"002909\":\"拉萨\",\"010763\":\"柳州\",\"010883\":\"贵港\",\"012701\":\"博尔塔拉\",\"012149\":\"延安\",\"002473\":\"湛江\",\"008455\":\"宿州\",\"006154\":\"衡水\",\"008333\":\"淮北\",\"008695\":\"龙岩\",\"006274\":\"晋中\",\"002933\":\"呼和浩特\",\"006737\":\"兴安盟\",\"006974\":\"朝阳\",\"007821\":\"湖州\",\"001729\":\"上海\",\"000514\":\"长沙\",\"010792\":\"桂林\",\"007394\":\"徐州\",\"006181\":\"大同\",\"008481\":\"亳州\",\"010552\":\"岳阳\",\"007271\":\"佳木斯\",\"012855\":\"阿拉尔\",\"012731\":\"克孜勒苏\",\"011885\":\"怒江\",\"010437\":\"潜江\",\"010679\":\"永州\",\"011648\":\"曲靖\",\"008005\":\"合肥\",\"011404\":\"凉山\",\"010317\":\"黄冈\",\"006064\":\"承德\",\"008906\":\"宜春\",\"006605\":\"通辽\",\"006845\":\"锦州\",\"007139\":\"齐齐哈尔\",\"007931\":\"舟山\",\"002126\":\"肇庆\",\"012054\":\"宝鸡\",\"012176\":\"汉中\",\"010420\":\"仙桃\",\"011870\":\"德宏\",\"007940\":\"台州\",\"005089\":\"长春\",\"002374\":\"汕头\",\"005884\":\"秦皇岛\",\"011756\":\"普洱\",\"004552\":\"兰州\",\"008353\":\"安庆\",\"012608\":\"海西\",\"002677\":\"琼中县\",\"002678\":\"屯昌县\",\"002679\":\"白沙县\",\"032649\":\"宁乡\",\"006358\":\"运城\",\"003764\":\"嘉兴\",\"003649\":\"宁波\",\"006917\":\"阜新\",\"012482\":\"定西\",\"009075\":\"枣庄\",\"012595\":\"玉树\",\"012354\":\"平凉\",\"011149\":\"内江\",\"009193\":\"潍坊\",\"011029\":\"攀枝花\",\"012236\":\"安康\",\"010731\":\"娄底\",\"002680\":\"昌江县\",\"009635\":\"滨州\",\"002681\":\"五指山市\",\"007215\":\"大庆\",\"006244\":\"晋城\",\"007575\":\"连云港\",\"010857\":\"防城港\",\"002683\":\"三亚\",\"004860\":\"太原\",\"010614\":\"郴州\",\"008663\":\"南平\",\"011949\":\"日喀则\",\"006485\":\"包头\",\"009993\":\"南阳\",\"011705\":\"昭通\",\"002424\":\"阳江\",\"000002\":\"深圳\",\"000123\":\"江门\",\"000122\":\"珠海\",\"000121\":\"中山\",\"009618\":\"聊城\",\"005138\":\"大连\",\"005930\":\"邯郸\",\"009978\":\"三门峡\",\"010172\":\"黄石\",\"000926\":\"武汉\",\"010295\":\"荆州\",\"011260\":\"达州\",\"010043\":\"信阳\",\"005381\":\"株洲\",\"010840\":\"北海\",\"011138\":\"遂宁\",\"011016\":\"自贡\",\"010965\":\"河池\",\"008413\":\"阜阳\",\"007322\":\"牡丹江\",\"002793\":\"三沙\",\"011817\":\"西双版纳\",\"010605\":\"益阳\",\"002457\":\"茂名\",\"003304\":\"苏州\",\"007904\":\"衢州\",\"006932\":\"辽阳\",\"008316\":\"马鞍山\",\"030568\":\"浏阳\",\"011292\":\"巴中\",\"011172\":\"乐山\",\"030561\":\"巴州\",\"010085\":\"周口\",\"006261\":\"朔州\",\"012257\":\"商洛\",\"010870\":\"钦州\",\"012497\":\"陇南\",\"010510\":\"邵阳\",\"009094\":\"东营\",\"011840\":\"大理\",\"007115\":\"延边\",\"007236\":\"伊春\",\"001372\":\"北京\",\"007358\":\"绥化\",\"006387\":\"忻州\",\"007476\":\"常州\",\"001011\":\"重庆\",\"005971\":\"邢台\",\"009411\":\"威海\",\"009653\":\"菏泽\",\"011729\":\"丽江\",\"006803\":\"抚顺\",\"003534\":\"杭州\",\"009757\":\"平顶山\",\"009878\":\"焦作\",\"010193\":\"十堰\",\"012011\":\"阿里\",\"011040\":\"泸州\",\"011275\":\"雅安\",\"012122\":\"渭南\",\"012369\":\"酒泉\",\"007104\":\"白城\",\"008557\":\"三明\",\"004630\":\"西宁\",\"006014\":\"张家口\",\"007345\":\"黑河\",\"010746\":\"湘西\",\"010988\":\"来宾\",\"006133\":\"廊坊\",\"008794\":\"新余\",\"009829\":\"鹤壁\",\"002636\":\"海口\",\"006557\":\"赤峰\",\"007646\":\"盐城\",\"009702\":\"洛阳\",\"030667\":\"莱阳\",\"032720\":\"湘乡\",\"030542\":\"望城\",\"002638\":\"澄迈县\",\"008185\":\"芜湖\",\"010254\":\"荆门\",\"010378\":\"随州\",\"007093\":\"松原\",\"011224\":\"宜宾\",\"012677\":\"哈密\",\"009954\":\"漯河\",\"011901\":\"昌都\",\"010139\":\"济源\",\"004388\":\"贵阳\",\"012317\":\"天水\",\"002085\":\"韶关\",\"006681\":\"巴彦淖尔\",\"002624\":\"潮州\",\"002981\":\"乌鲁木齐\",\"006548\":\"乌海\",\"007876\":\"金华\",\"004006\":\"青岛\",\"009933\":\"许昌\",\"002509\":\"梅州\",\"012670\":\"吐鲁番\",\"011340\":\"阿坝州\",\"012550\":\"海东\",\"010484\":\"湘潭\",\"011333\":\"资阳\",\"012301\":\"金昌\",\"012661\":\"克拉玛依\",\"012782\":\"伊犁\",\"011457\":\"遵义\",\"011578\":\"黔西南\",\"011579\":\"黔东南\",\"007080\":\"白山\",\"010247\":\"鄂州\",\"007762\":\"泰州\",\"012306\":\"白银\",\"010925\":\"百色\",\"002655\":\"儋州市\",\"002656\":\"洋浦经济开发区\",\"002658\":\"临高县\",\"002651\":\"乐东县\",\"006217\":\"长治\",\"007669\":\"扬州\",\"002653\":\"东方市\",\"005800\":\"唐山\",\"002533\":\"河源\",\"006458\":\"吕梁\",\"000119\":\"东莞\",\"012582\":\"果洛\",\"011494\":\"安顺\",\"012341\":\"张掖\",\"011001\":\"崇左\",\"012332\":\"武威\",\"010278\":\"孝感\",\"011367\":\"甘孜\",\"011247\":\"广安\",\"010954\":\"贺州\",\"008765\":\"九江\",\"011924\":\"山南\",\"003871\":\"济南\",\"011800\":\"文山\",\"007312\":\"七台河\",\"000120\":\"惠州\",\"008520\":\"莆田\",\"004284\":\"西安\",\"005252\":\"沈阳\",\"004702\":\"南宁\",\"002645\":\"万宁市\",\"004946\":\"哈尔滨\",\"008629\":\"漳州\",\"002526\":\"揭阳\",\"002647\":\"陵水县\",\"002640\":\"文昌市\",\"006206\":\"阳泉\",\"008505\":\"宣城\",\"002642\":\"定安县\",\"008867\":\"吉安\",\"002643\":\"琼海市\",\"008745\":\"景德镇\",\"002649\":\"保亭县\",\"012572\":\"黄南\",\"010022\":\"商丘\",\"005480\":\"衡阳\",\"010387\":\"恩施\",\"012563\":\"海北\",\"012684\":\"昌吉\",\"012205\":\"榆林\",\"011114\":\"广元\",\"002097\":\"汕尾\",\"007544\":\"南通\",\"008754\":\"萍乡\",\"008993\":\"淄博\",\"009840\":\"新乡\",\"010825\":\"梧州\",\"007726\":\"镇江\",\"008816\":\"赣州\",\"009903\":\"濮阳\",\"006999\":\"吉林\",\"007843\":\"绍兴\",\"008933\":\"抚州\",\"030745\":\"张家港\",\"012087\":\"咸阳\",\"011541\":\"毕节\",\"010571\":\"常德\",\"010210\":\"襄阳\",\"010694\":\"怀化\",\"009590\":\"德州\",\"012632\":\"吴忠\",\"010456\":\"天门\",\"003131\":\"南京\",\"006884\":\"营口\",\"005432\":\"宜昌\",\"009115\":\"烟台\",\"012516\":\"临夏\",\"007056\":\"辽源\",\"009353\":\"泰安\",\"006989\":\"葫芦岛\",\"008803\":\"鹰潭\",\"002267\":\"福州\",\"006625\":\"鄂尔多斯\",\"032499\":\"醴陵\",\"007162\":\"鸡西\",\"012740\":\"喀什\",\"011894\":\"迪庆\",\"008490\":\"池州\",\"011777\":\"临沧\",\"012869\":\"五家渠\",\"002154\":\"南昌\",\"006750\":\"锡林郭勒盟\",\"012625\":\"石嘴山\",\"001187\":\"广州\",\"012868\":\"图木舒克\",\"008376\":\"黄山\",\"007043\":\"四平\",\"008254\":\"蚌埠\",\"009464\":\"日照\",\"007627\":\"淮安\",\"000678\":\"厦门\",\"008715\":\"宁德\",\"004114\":\"石家庄\",\"008956\":\"上饶\",\"007986\":\"丽水\",\"002610\":\"云浮\",\"011692\":\"保山\",\"012411\":\"庆阳\",\"009493\":\"临沂\",\"010596\":\"张家界\",\"003390\":\"无锡\",\"010114\":\"驻马店\",\"012533\":\"甘南\",\"012654\":\"中卫\",\"006423\":\"临汾\",\"006786\":\"鞍山\",\"004245\":\"保定\",\"004487\":\"昆明\",\"011448\":\"六盘水\",\"011207\":\"眉山\",\"008288\":\"淮南\",\"007198\":\"双鸭山\",\"009254\":\"济宁\",\"030609\":\"沭阳\",\"030738\":\"东台\",\"003817\":\"温州\",\"011673\":\"玉溪\",\"011794\":\"楚雄\",\"006096\":\"沧州\",\"010340\":\"咸宁\",\"008391\":\"滁州\",\"007181\":\"鹤岗\",\"011557\":\"铜仁\",\"012643\":\"固原\",\"010467\":\"神农架\",\"011797\":\"红河\",\"012765\":\"和田\",\"006654\":\"呼伦贝尔\",\"006775\":\"阿拉善盟\",\"004597\":\"银川\",\"009487\":\"莱芜\",\"007065\":\"通化\"}";
    @Test
    public void userCityInsert() {
        Map<Integer, TagInfo> allTag = tagInfoService.selectAllTagMap();
        TagUtil tagUtil = new TagUtil(allTag);

        List<Long> teacherIdList = teacherService.list(new QueryWrapper<Teacher>().isNotNull("tag"))
                .stream().map(Teacher::getId).collect(Collectors.toList());
        int teacherIdListSize = teacherIdList.size();
        JSONObject cityMap = JSON.parseObject(cityMapStr);
        List<Map.Entry<String, Object>> cityList = new ArrayList<>(cityMap.entrySet());
        int cityMapSize = cityMap.size();

        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            Map.Entry<String, Object> city = cityList.get(random.nextInt(cityMapSize));
            Long userId = teacherIdList.get(random.nextInt(teacherIdListSize));
            String tag = TagUtil.getTagStr(tagUtil.randomTag());
            new UserCity()
                    .setCityCode(city.getKey())
                    .setCityName(city.getValue().toString())
                    .setUserId(userId).setTag(tag)
                    .insert();
        }
        System.out.println();
    }


    @Test
    public void 获取所有的偏好城市并组装所有偏好标签() {
//        Map<Integer, TagInfo> tagInfoAllMap = tagInfoService.selectAllMap();
//        Map<Integer, TagLeaf> tagLeafAllMap = tagLeafService.selectAllMap();
        Map<Integer, TagInfo> tagInfoAllEndMap = tagInfoService.selectAllTagMap();
        TagUtil tagUtil = new TagUtil(tagInfoAllEndMap);

        List<UserCity> userCityList = userCityService.list(new QueryWrapper<UserCity>().isNotNull("tag"));
        long nowTime = System.currentTimeMillis();
        userCityList.forEach(userCity -> userCity.setTagInfoList(tagUtil.parseTagStrToList(userCity.getTag())));
        long resultTime = System.currentTimeMillis() - nowTime;
        System.out.println();
    }

    @Test
    public void 获取用户() {
        Map<Integer, TagInfo> tagInfoAllEndMap = tagInfoService.selectAllTagMap();
        TagUtil tagUtil = new TagUtil(tagInfoAllEndMap);
        List<Long> userIdList = userCityService.list(new QueryWrapper<UserCity>().isNotNull("tag")).stream()
                .map(UserCity::getUserId).distinct().collect(Collectors.toList());


        Stopwatch watch1 = Stopwatch.createUnstarted();// 累计时间
        Stopwatch watch = Stopwatch.createStarted();
        watch1.start();
        List<Teacher> teacherList = teacherService.list(new QueryWrapper<Teacher>().lambda().in(Teacher::getId, userIdList));
        System.out.println("100W中查询1W数据用时:" + watch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        watch.reset().start();
        teacherList.forEach(t -> t.setUserCityList(userCityService.list(new QueryWrapper<UserCity>().eq("user_id", t.getId()))));
        System.out.println("将City组装进Teacher:" + watch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        watch.reset().start();
//100W中查询1W数据用时:939ms
//100W中查询1W数据用时:98444ms
        LgrenUtil.execute(Executors.newFixedThreadPool(5), 1000, teacherList, (t) -> {
            t.getUserCityList().forEach(userCity -> userCity.setTagInfoList(tagUtil.parseTagStrToList(userCity.getTag())));
        });
        System.out.println("组装所有用户耗时:" + watch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        System.out.println("总耗时" + watch1.elapsed(TimeUnit.MILLISECONDS) + "ms");

    }
    @Test
    public void userCitySelect() {
        List<UserCity> userCityList = new UserCity().selectList(new QueryWrapper());
        System.out.println(userCityList);
    }

}
