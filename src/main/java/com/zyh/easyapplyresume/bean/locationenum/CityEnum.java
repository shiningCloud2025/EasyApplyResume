package com.zyh.easyapplyresume.bean.locationenum;

import lombok.Getter;

/**
 * 城市枚举（仅含id、name字段，对应数据库city表）
 * 枚举名采用大写英文命名，重复名称通过后缀区分（如不同省份的同名城市）
 * @author shiningCloud2025
 */
@Getter
public enum CityEnum {
    // 辽宁省
    BENXI(589, "本溪市"),
    CHAOYANG_LIAONING(632, "朝阳市"),
    DALIAN(573, "大连市"),
    DANDONG(593, "丹东市"),
    FUXIN(617, "阜新市"),
    FUSHUN(584, "抚顺市"),
    HULUDAO(604, "葫芦岛市"),
    JINZHOU(598, "锦州市"),
    LIAOYANG(621, "辽阳市"),
    PANJIN(613, "盘锦市"),
    SHENYANG(560, "沈阳市"),
    TONGLING_LIAONING(6858, "铁岭市"),
    YINGKOU(609, "营口市"),
    ANSHAN(579, "鞍山市"),

    // 福建省
    FUZHOU(1303, "福州市"),
    LONGYAN(1362, "龙岩市"),
    NINGDE(1370, "宁德市"),
    NANPING(1352, "南平市"),
    PUTIAN(1329, "莆田市"),
    QUANZHOU(1332, "泉州市"),
    XIAMEN(1315, "厦门市"),
    SANMING(1317, "三明市"),
    ZHANGZHOU(1341, "漳州市"),

    // 吉林省
    BAICHENG(681, "白城市"),
    BAISHAN(664, "白山市"),
    CHANGCHUN(639, "长春市"),
    JILIN_CITY(644, "吉林市"),
    LIAOYUAN(2992, "辽源市"),
    SIPING(651, "四平市"),
    SONGYUAN(674, "松原市"),
    TONGHUA(657, "通化市"),
    YANBIAN(687, "延边朝鲜族自治州"),

    // 天津市（各区）
    BAODI(51051, "宝坻区"),
    BEICHEN(51050, "北辰区"),
    BINHAI(51044, "滨海新区"),
    DAGANG(51049, "大港区"),
    DONGLI(51035, "东丽区"),
    HONGQIAO(51040, "红桥区"),
    HEBEI_TIANJIN(51037, "河北区"),
    HEDONG(51038, "河东区"),
    HEPING(51036, "和平区"),
    HEXI(51039, "河西区"),
    JINGHAI(51042, "静海区"),
    JIZHOU(51041, "蓟州区"),
    JINNAN(51047, "津南区"),
    NINGHE(51052, "宁河区"),
    NANKAI(51043, "南开区"),
    WUQING(51046, "武清区"),
    XIQING(51045, "西青区"),

    // 山东省
    BINZHOU(1090, "滨州市"),
    DEZHOU(1060, "德州市"),
    DONGYING(1025, "东营市"),
    HEZE(1099, "菏泽市"),
    JINING(2900, "济宁市"),
    JINAN(1000, "济南市"),
    LIAOCHENG(1081, "聊城市"),
    LINYI(1072, "临沂市"),
    QINGDAO(1007, "青岛市"),
    RIZHAO(1108, "日照市"),
    TAIAN(1112, "泰安市"),
    WEIFANG(1032, "潍坊市"),
    WEIHAI(1053, "威海市"),
    YANTAI(1042, "烟台市"),
    ZAOZHUANG(1022, "枣庄市"),
    ZIBO(1016, "淄博市"),

    // 湖南省
    CHANGDE(1530, "常德市"),
    CHANGSHA(1482, "长沙市"),
    CHENZHOU(1544, "郴州市"),
    HUAIHUA(1574, "怀化市"),
    HENGYANG(1501, "衡阳市"),
    LOUDI(1586, "娄底市"),
    SHAOYANG(1511, "邵阳市"),
    XIANGTAN(1495, "湘潭市"),
    XIANGXI(1592, "湘西州"),
    YUEYANG(1522, "岳阳市"),
    YIYANG(1555, "益阳市"),
    YONGZHOU(1560, "永州市"),
    ZHANGJIAJIE(1540, "张家界市"),
    ZHUZHOU(1488, "株洲市"),

    // 河南省
    HIBI(454, "鹤壁市"),
    XUCHANG(482, "许昌市"),
    JIAOZUO(446, "焦作市"),
    JIYUAN(2780, "济源市"),
    KAIFENG(420, "开封市"),
    LUOHE(489, "漯河市"),
    LUOYANG(427, "洛阳市"),
    NANYANG(502, "南阳市"),
    PINGDINGSHAN(438, "平顶山市"),
    PUYANG(475, "濮阳市"),
    SHANGQIU(517, "商丘市"),
    SANMENXIA(495, "三门峡市"),
    XINYANG(549, "信阳市"),
    XINXIANG(458, "新乡市"),
    ZHENGZHOU(412, "郑州市"),
    ZHUMADIAN(538, "驻马店市"),
    ZHOUKOU(527, "周口市"),
    ANYANG(468, "安阳市"),

    // 江苏省
    CHANGZHOU(978, "常州市"),
    HUAIAN(925, "淮安市"),
    LIANYUNGANG(919, "连云港市"),
    WUXI(984, "无锡市"),
    NANJING(904, "南京市"),
    NANTONG(965, "南通市"),
    SUQIAN(933, "宿迁市"),
    SUZHOU_JIANGSU(988, "苏州市"),
    TAIZHOU_JIANGSU(959, "泰州市"),
    XUZHOU(911, "徐州市"),
    YANCHENG(939, "盐城市"),
    YANGZHOU(951, "扬州市"),
    ZHENJIANG(972, "镇江市"),

    // 安徽省
    BENGBU(1132, "蚌埠市"),
    BOZHOU(1174, "亳州市"),
    CHIZHOU(1201, "池州市"),
    CHUZHOU(1159, "滁州市"),
    FUYANG(1167, "阜阳市"),
    HEFEI(1116, "合肥市"),
    HUAIBEI(1124, "淮北市"),
    HUAINAN(1121, "淮南市"),
    HUANGSHAN(1151, "黄山市"),
    LUAN(1206, "六安市"),
    MAANSHAN(1137, "马鞍山市"),
    SUZHOU_ANHUI(1180, "宿州市"),
    TONGLING_ANHUI(1114, "铜陵市"),
    WUHU(1127, "芜湖市"),
    XUANCHENG(2971, "宣城市"),
    ANQING(1140, "安庆市"),

    // 贵州省
    BIJIE(2180, "毕节市"),
    GUIYANG(2144, "贵阳市"),
    LIUPANSHUI(2150, "六盘水市"),
    QIANDONGNAN(2205, "黔东南州"),
    QIANNAN(2222, "黔南州"),
    QIANXINAN(2196, "黔西南州"),
    TONGREN(2169, "铜仁市"),
    ZUNYI(2155, "遵义市"),
    ANSHUN(2189, "安顺市"),

    // 云南省
    BAOSHAN(2298, "保山市"),
    CHUXIONG(2336, "楚雄州"),
    DALI(2347, "大理州"),
    DEHONG(2360, "德宏州"),
    DIQING(4108, "迪庆州"),
    HONGHE(2318, "红河州"),
    KUNMING(2235, "昆明市"),
    LIJIANG(2304, "丽江市"),
    LINCANG(2291, "临沧市"),
    NUJIANG(2366, "怒江州"),
    PUER(2281, "普洱市"),
    QUJING(2247, "曲靖市"),
    WENSHAN(2309, "文山州"),
    XISHUANGBANNA(2332, "西双版纳州"),
    YUXI(2258, "玉溪市"),
    ZHAOTONG(2270, "昭通市"),

    // 陕西省
    BAOJI(2390, "宝鸡市"),
    HANZHONG(2442, "汉中市"),
    SHANGLUO(2468, "商洛市"),
    TONGCHUAN(2386, "铜川市"),
    WEINAN(2416, "渭南市"),
    XIANYANG(2402, "咸阳市"),
    XIAN(2376, "西安市"),
    YANAN(2428, "延安市"),
    YULIN_SHAANXI(2454, "榆林市"),
    ANKANG(2476, "安康市"),

    // 北京市（各区）
    CHAOYANG_BEIJING(72, "朝阳区"),
    CHANGPING(2901, "昌平区"),
    SHIJINGSHAN(2806, "石景山区"),
    DAXING_BEIJING(2810, "大兴区"),
    DONGCHENG(2802, "东城区"),
    FANGSHAN(2808, "房山区"),
    FENGTAI(2805, "丰台区"),
    HUAIROU(2814, "怀柔区"),
    HAIDIAN(2800, "海淀区"),
    MENTOUGOU(2807, "门头沟"),
    MIYUN(2816, "密云区"),
    PINGGU(2953, "平谷区"),
    SHUNYI(2812, "顺义区"),
    TONGZHOU(2809, "通州区"),
    XICHENG(2801, "西城区"),
    YANQING(3065, "延庆区"),

    // 上海市（各区）
    BAOSHAN_SHANGHAI(2824, "宝山区"),
    CHANGNING(2815, "长宁区"),
    CHONGMING(2919, "崇明区"),
    FENGXIAN(2837, "奉贤区"),
    HUANGPU(78, "黄浦区"),
    HONGKOU(2822, "虹口区"),
    JIADING(2826, "嘉定区"),
    JINGAN(2817, "静安区"),
    JINSHAN(2835, "金山区"),
    MINHANG(2825, "闵行区"),
    PUDONG(2830, "浦东新区"),
    PUTUO(2841, "普陀区"),
    QINGPU(2833, "青浦区"),
    SONGJIANG(2834, "松江区"),
    XUHUI(2813, "徐汇区"),
    YANGPU(2823, "杨浦区"),

    // 广东省
    CHAOZHOU(1705, "潮州市"),
    DONGGUAN(1655, "东莞市"),
    FOSHAN(1666, "佛山市"),
    GUANGZHOU(1601, "广州市"),
    HUIZHOU(1643, "惠州市"),
    HEYUAN(1627, "河源市"),
    JIANGMEN(1659, "江门市"),
    JIEYANG(1709, "揭阳市"),
    MAOMING(1684, "茂名市"),
    MEIZHOU(1634, "梅州市"),
    QINGYUAN(1704, "清远市"),
    SHANTOU(1611, "汕头市"),
    SHANWEI(1650, "汕尾市"),
    SHAOGUAN(1617, "韶关市"),
    SHENZHEN(1607, "深圳市"),
    YANGJIANG(1672, "阳江市"),
    YUNFU(1698, "云浮市"),
    ZHANJIANG(1677, "湛江市"),
    ZHAOQING(1690, "肇庆市"),
    ZHONGSHAN(1657, "中山市"),
    ZHUHAI(1609, "珠海市"),

    // 河北省
    BAODING(199, "保定市"),
    CHENGDE(239, "承德市"),
    CANGZHOU(264, "沧州市"),
    SHIJIAZHUANG(142, "石家庄市"),
    XINGTAI(164, "邢台市"),
    HANDAN(148, "邯郸市"),
    HENGSHUI(275, "衡水市"),
    LANGFANG(274, "廊坊市"),
    QINHUANGDAO(248, "秦皇岛市"),
    TANGSHAN(258, "唐山市"),
    ZHANGJIAKOU(224, "张家口市"),

    // 山西省
    CHANGZHI(3074, "长治市"),
    DATONG(309, "大同市"),
    JINCHENG(325, "晋城市"),
    JINZHONG(336, "晋中市"),
    LINFEN(379, "临汾市"),
    LULIANG(368, "吕梁市"),
    SHUOZHOU(330, "朔州市"),
    TAIYUAN(303, "太原市"),
    XINZHOU(350, "忻州市"),
    YANGQUAN(318, "阳泉市"),
    YUNCHENG(398, "运城市"),

    // 四川省
    BAZHONG(2042, "巴中市"),
    CHENGDU(1930, "成都市"),
    DAZHOU(2033, "达州市"),
    DEYANG(1962, "德阳市"),
    GUANGYUAN(1977, "广元市"),
    GUANGAN(2016, "广安市"),
    GANZI(2084, "甘孜州"),
    LIANGSHAN(2103, "凉山州"),
    LESHAN(1993, "乐山市"),
    LUZHOU(1954, "泸州市"),
    MIANYANG(1960, "绵阳市"),
    MEISHAN(2058, "眉山市"),
    NEIJIANG(1988, "内江市"),
    NANCHONG(2022, "南充市"),
    PANZHIHUA(1950, "攀枝花市"),
    SUINING(1983, "遂宁市"),
    YIBIN(2005, "宜宾市"),
    YAAN(2047, "雅安市"),
    ZIGONG(1946, "自贡市"),
    ZIYANG(2065, "资阳市"),
    ABA(2070, "阿坝州"),

    // 青海省
    GUOLUO(2605, "果洛州"),
    HUANGNAN(2597, "黄南州"),
    HAIBEI(2592, "海北州"),
    HAIDONG(2585, "海东地区"),
    HAINAN_QINGHAI(2603, "海南州"),
    HAIXI(2620, "海西州"),
    XINING(2580, "西宁市"),
    YUSHU(2612, "玉树州"),

    // 甘肃省
    BAIYIN(2495, "白银市"),
    DINGXI(3080, "定西市"),
    GANNAN(2564, "甘南州"),
    JIAYUGUAN(2509, "嘉峪关市"),
    JIUQUAN(2556, "酒泉市"),
    JINNAN_GANSU(2492, "金昌市"),
    LANZHOU(2487, "兰州市"),
    LINXIA(2573, "临夏州"),
    LONGNAN(2534, "陇南市"),
    PINGLIANG(2518, "平凉市"),
    QINGYANG(2525, "庆阳市"),
    TIANSHUI(2501, "天水市"),
    WUWEI(2544, "武威市"),
    ZHANGYE(2549, "张掖市"),

    // 黑龙江省
    DAQING(742, "大庆市"),
    DAXINGANLING(793, "大兴安岭地区"),
    HEGANG(727, "鹤岗市"),
    HARBIN(698, "哈尔滨市"),
    HEIHE(776, "黑河市"),
    JIAMUSI(765, "佳木斯市"),
    QIQIHAR(712, "齐齐哈尔市"),
    JIXI(737, "鸡西市"),
    MUDANJIANG(757, "牡丹江市"),
    QITAIHE(773, "七台河市"),
    SHUANGYASHAN(731, "双鸭山市"),
    SUIHUA(782, "绥化市"),
    YICHUN_HEILONGJIANG(753, "伊春市"),

    // 湖北省
    HUANGSHI(1387, "黄石市"),
    HUANGGANG(1441, "黄冈市"),
    JINGMEN(1477, "荆门市"),
    JINGZHOU(1413, "荆州市"),
    QIANJIANG(2922, "潜江市"),
    SHENNONGJIA(3154, "神农架林区"),
    SHIYAN(1405, "十堰市"),
    SUIZHOU(1479, "随州市"),
    TIANMEN(2980, "天门市"),
    WUHAN(1381, "武汉市"),
    XIAOGAN(1432, "孝感市"),
    XIANNING(1458, "咸宁市"),
    XIANGYANG(1396, "襄阳市"),
    XIANTAO(2983, "仙桃市"),
    YICHANG(1421, "宜昌市"),
    EZHOU(1475, "鄂州市"),
    ENSHI(1466, "恩施州"),

    // 广西壮族自治区
    BAISE(1806, "百色市"),
    BEIHAI(1746, "北海市"),
    CHONGZUO(3168, "崇左市"),
    FANGCHENGGANG(1749, "防城港市"),
    GUIGANG(1757, "贵港市"),
    GUILIN(1726, "桂林市"),
    HEZHOU(1792, "贺州市"),
    HECHI(1818, "河池市"),
    LIUZHOU(1720, "柳州市"),
    LAIBIN(3044, "来宾市"),
    NANNING(1715, "南宁市"),
    QINZHOU(1753, "钦州市"),
    WUZHOU(1740, "梧州市"),
    YULIN_GUANGXI(1761, "玉林市"),

    // 海南省
    BAISHA(3706, "白沙县"),
    BAOTING(3709, "保亭县"),
    CHENGMAI(3702, "澄迈县"),
    CHANGJIANG(3705, "昌江县"),
    DINGAN(3703, "定安县"),
    DANZHOU(3034, "儋州市"),
    DONGFANG(3173, "东方市"),
    HAIKOU(2121, "海口市"),
    LEDONG(3710, "乐东县"),
    LINGSHUI(3708, "陵水县"),
    LINGAO(3701, "临高县"),
    WANNING(3137, "万宁市"),
    QIONGHAI(3115, "琼海市"),
    QIONGZhong(3707, "琼中县"),
    SANSA(3711, "三沙市"),
    SANYA(3690, "三亚市"),
    Tunchang(3704, "屯昌县"),
    WENCHANG(3698, "文昌市"),
    WUZHISHAN(3699, "五指山市"),

    // 新疆维吾尔自治区
    BOERTALA(2723, "博尔塔拉州"),
    BAYINGOLENG(2704, "巴音郭楞州"),
    BEITUN(129142, "北屯市"),
    CHANGJI(2714, "昌吉州"),
    TACHENG(2736, "塔城地区"),
    SHIHEZI(2656, "石河子市"),
    HETIAN(2666, "和田地区"),
    HAMI(2662, "哈密地区"),
    KELAMAYI(2654, "克拉玛依市"),
    HUYANGHE(146206, "胡杨河市"),
    KIZILSUBEKIRGHIZ(2699, "克孜勒苏柯尔克孜自治州"),
    KASHGAR(2686, "喀什地区"),
    KEKEDALA(145492, "可克达拉市"),
    KUNYU(53668, "昆玉市"),
    TIEMENGUAN(53090, "铁门关市"),
    TUMUSHUKE(15946, "图木舒克市"),
    TURPAN(2658, "吐鲁番地区"),
    URUMQI(2652, "乌鲁木齐市"),
    WUJIAQU(4110, "五家渠市"),
    YILI(2727, "伊犁州"),
    AKSU(2675, "阿克苏地区"),
    ALTAY(2744, "阿勒泰地区"),
    ALAR(15945, "阿拉尔市"),

    // 宁夏回族自治区
    SHIZUISHAN(2632, "石嘴山市"),
    GUYUAN(2644, "固原市"),
    WUZHONG(2637, "吴忠市"),
    YINCHUAN(2628, "银川市"),
    ZHONGWEI(3071, "中卫市"),

    // 浙江省
    HANGZHOU(1213, "杭州市"),
    HUZHOU(1250, "湖州市"),
    JIAXING(1243, "嘉兴市"),
    JINHUA(1262, "金华市"),
    LISHUI(1280, "丽水市"),
    NINGBO(1158, "宁波市"),
    QUZHOU(1273, "衢州市"),
    SHAOXING(1255, "绍兴市"),
    TAIZHOU_ZHEJIANG(1290, "台州市"),
    WENZHOU(1233, "温州市"),
    ZHOUSHAN(1298, "舟山市"),

    // 江西省
    FUZHOU_JIANGXI(1885, "抚州市"),
    GANZHOU(1911, "赣州市"),
    JIUJIANG(1845, "九江市"),
    JIAN(1898, "吉安市"),
    JINGDEZHEN(1832, "景德镇市"),
    NANCHANG(1827, "南昌市"),
    PINGXIANG(1836, "萍乡市"),
    SHANGRAO(1861, "上饶市"),
    XINYU(1842, "新余市"),
    YICHUN_JIANGXI(1874, "宜春市"),
    YINGTAN(1857, "鹰潭市"),

    // 港澳
    HONGKONG(52994, "中国香港"),
    MACAO(52995, "中国澳门"),

    // 西藏自治区
    CHANGDU(3138, "昌都地区"),
    LINZHI(3971, "林芝市"),
    LHASA(2951, "拉萨市"),
    NAGQU(3107, "那曲地区"),
    SHIGATSE(3144, "日喀则地区"),
    SHANNAN(3129, "山南地区"),
    NGARI(3970, "阿里地区"),

    // 台湾
    TAIWAN(2768, "中国台湾"),

    // 内蒙古自治区
    BAOTOU(805, "包头市"),
    BAYANNUR(880, "巴彦淖尔市"),
    CHIFENG(812, "赤峰市"),
    HOHHOT(799, "呼和浩特市"),
    HULUNBUIR(848, "呼伦贝尔市"),
    TONGlIAO(902, "通辽市"),
    WUHAI(810, "乌海市"),
    ULANQAB(823, "乌兰察布市"),
    XILINGOL(835, "锡林郭勒盟"),
    XINGAN(895, "兴安盟"),
    ORDOS(870, "鄂尔多斯市"),
    ALASHAN(891, "阿拉善盟"),

    // 重庆市（各区/县）
    BISHAN(48131, "璧山区"),
    BANAN(48202, "巴南区"),
    BEIBEI(48203, "北碚区"),
    CHANGSHOU(48206, "长寿区"),
    CHENGKOU(4164, "城口县"),
    DIANJIANG(139, "垫江县"),
    DADUKOU(50954, "大渡口区"),
    SHIZHU(137, "石柱县"),
    DAZU(126, "大足区"),
    FENGJIE(131, "奉节县"),
    FULING(114, "涪陵区"),
    FENGDU(130, "丰都县"),
    HECHUAN(48201, "合川区"),
    JIANGBEI(50950, "江北区"),
    JIANGJIN(48204, "江津区"),
    JIULONGPO(50952, "九龙坡区"),
    KAIXIAN(132, "开州区"),
    LIANGPING(115, "梁平区"),
    WANZHOU(113, "万州区"),
    NANCHUAN(119, "南川区"),
    NANAN(50951, "南岸区"),
    PENGSHUI(138, "彭水县"),
    QIANJIANG_CHONGQING(128, "黔江区"),
    QIJIAO(50995, "綦江区"),
    RONGCHANG(48132, "荣昌区"),
    SHAOPINGBA(50953, "沙坪坝区"),
    TONGNIANG(48133, "铜梁区"),
    TONGNAN(123, "潼南区"),
    WUSHAN(136, "巫山县"),
    WUXI_CHONGQING(135, "巫溪县"),
    WULONG(129, "武隆区"),
    XIUSHAN(141, "秀山县"),
    YUBEI(48205, "渝北区"),
    YUNYANG(133, "云阳县"),
    YUZHONG(51026, "渝中区"),
    YONGCHUAN(48207, "永川区"),
    YOUYANG(140, "酉阳县"),
    ZHONGXIAN(134, "忠县"),

    // 全国
    QUANGUO(99999, "全国");

    /** 数据库对应ID */
    private final int id;
    /** 城市名称 */
    private final String name;

    CityEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 根据ID获取枚举
     * @param id 数据库ID
     * @return 对应的枚举，无匹配返回null
     */
    public static CityEnum getById(int id) {
        for (CityEnum city : values()) {
            if (city.getId() == id) {
                return city;
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举（精确匹配）
     * @param name 城市名称
     * @return 对应的枚举，无匹配返回null
     */
    public static CityEnum getByName(String name) {
        for (CityEnum city : values()) {
            if (city.getName().equals(name)) {
                return city;
            }
        }
        return null;
    }
    }