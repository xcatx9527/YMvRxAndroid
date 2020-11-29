package com.xile.script.utils.common;

import com.chenyang.lloglib.LLog;

import java.util.Random;

/**
 * 作者：赵小飞<br>
 * 时间 2017/5/6.
 */

public class AccountUtil {


    private static String[] acountDigitRule = {"))$$$$$$$","))$$$$$$$",")?$$$$",")!$$$$",")!$$$$$",")?!",")?!$$",")?$$!","))$$?!","))?!",")!$$$$$",
            ")%$$$$",")%$$$$$",")%$$$",")?!$$$$",")?!$$$$$",")13$$$$$$$$$",")15$$$$$$$$$",")17$$$$$$$$$",")18$$$$$$$$$",
            ")13$$$$$$$$$",")15$$$$$$$$$",")17$$$$$$$$$",")18$$$$$$$$$",")$$$$?",")?$$$!"};


    private static String[] chinessFirstName = {"赵","钱","孙","李","周","吴","郑","王","冯","陈","褚","卫","蒋","沈","韩","杨","朱","秦","尤","许","何",
            "吕","施","张","孔","曹","严","华","金","魏","陶","姜","戚","谢","邹","喻","柏","水","","窦","章","云","苏","潘","葛","奚","范","彭","郎","鲁",
            "韦","昌","马","苗","凤","花","方","俞","任","袁","柳","酆","鲍","史","唐","费","廉","岑","薛","雷","贺","倪","汤","滕","殷","罗","毕","郝",
            "邬","安","常","乐","于","时","傅","皮","卞","齐","康","伍","余","元","卜","顾","孟","平","黄","穆","萧","尹","姚","邵","湛","汪","祁","毛",
            "禹","狄","米","贝","臧","伏","成","戴","谈","宋","茅","庞","熊","纪","舒","屈","项","祝","董","梁","杜阮","蓝","闵","席","季","麻","贾","路",
            "娄","危","江","童","颜","郭","林","刁","钟","徐","邱","骆","高","夏","蔡","田","樊","胡","凌","霍","虞","万","支","柯","昝","管","卢","莫",
            "经","房","裘","缪","干","解","应","宗","丁","宣","贲","邓","郁","单","洪","包","诸","左","石","崔","吉","钮","龚","程","嵇","邢","滑","陆",
            "荣","翁荀","羊","於","惠","甄","曲","家","封","芮","羿","储","靳","汲","邴","糜","松","井","段","富","巫","乌","焦","巴","弓","牧","隗","山",
            "谷","车","侯","宓","蓬","全","郗","班","仰","秋","仲","伊","宫","宁","仇","栾","暴","甘","钭","厉","戎","祖","武","符","刘","景","詹","束",
            "龙","叶","幸","司","韶","郜","黎","蓟","薄","印","宿","白","怀","蒲","邰","鄂","索","咸","籍","赖","卓","蔺","屠","蒙","池","乔","胥","能",
            "苍","双","闻","莘","党","翟","谭","贡","劳","逄","姬","申","扶","堵","冉","宰","郦","雍","卻","璩","桑","桂","濮","牛","寿","通","边","扈",
            "燕","冀","郏","浦","尚","农","温","别","庄","晏","柴","瞿","阎","充","慕","连","茹","习","宦","艾","鱼","容","向","古","易","慎","戈","廖",
            "庾","终","暨","居","衡","步","都","耿","满","弘","匡","国","文","寇","广","禄","阙","东","欧","殳","沃","利","蔚","越","夔","隆","巩","厍",
            "聂","晁","勾","敖","融","冷","訾","辛","阚","那","简","饶","曾","荆","司马","上官","欧阳","夏侯","诸葛","东方","赫","皇甫","尉迟","单","公孙",
            "轩辕","令狐","钟","宇文","慕容","司徒"};

    private static String[] chinessLastName = {"秀", "娟", "英", "华", "慧", "巧", "美", "娜", "静", "淑", "惠", "珠", "翠", "雅", "芝", "玉", "萍", "红", "娥", "玲", "芬",
            "芳", "燕", "彩", "春", "菊", "兰", "凤", "洁", "梅", "琳", "素", "云", "莲", "真", "环", "雪", "荣", "爱", "妹", "霞", "香", "月", "莺", "媛", "艳", "瑞", "凡", "佳",
            "嘉", "琼", "勤", "珍", "贞", "莉", "桂", "娣", "叶", "璧", "璐", "娅", "琦", "晶", "妍", "茜", "秋", "珊", "莎", "锦", "黛", "青", "倩", "婷", "姣", "婉", "娴", "瑾",
            "颖", "露", "瑶", "怡", "婵", "雁", "蓓", "纨", "仪", "荷", "丹", "蓉", "眉", "君", "琴", "蕊", "薇", "菁", "梦", "岚", "苑", "婕", "馨", "瑗", "琰", "韵", "融", "园",
            "艺", "咏", "卿", "聪", "澜", "纯", "毓", "悦", "昭", "冰", "爽", "琬", "茗", "羽", "希", "宁", "欣", "飘", "育", "滢", "馥", "筠", "柔", "竹", "霭", "凝", "晓", "欢",
            "霄", "枫", "芸", "菲", "寒", "伊", "亚", "宜", "可", "姬", "舒", "影", "荔", "枝", "思", "丽", "伟", "刚", "勇", "毅", "俊", "峰", "强", "军", "平", "保", "东", "文",
            "辉", "力", "明", "永", "健", "世", "广", "志", "义", "兴", "良", "海", "山", "仁", "波", "宁", "贵", "福", "生", "龙", "元", "全", "国", "胜", "学", "祥", "才", "发",
            "武", "新", "利", "清", "飞", "彬", "富", "顺", "信", "子", "杰", "涛", "昌", "成", "康", "星", "光", "天", "达", "安", "岩", "中", "茂", "进", "林", "有", "坚", "和",
            "彪", "博", "诚", "先", "敬", "震", "振", "壮", "会", "思", "群", "豪", "心", "邦", "承", "乐", "绍", "功", "松", "善", "厚", "庆", "磊", "民", "友", "裕", "河", "哲",
            "江", "超", "浩", "亮", "政", "谦", "亨", "奇", "固", "之", "轮", "翰", "朗", "伯", "宏", "言", "若", "鸣", "朋", "斌", "梁", "栋", "维", "启", "克", "伦", "翔", "旭",
            "鹏", "泽", "晨", "辰", "士", "以", "建", "家", "致", "树", "炎", "德", "行", "时", "泰", "盛", "雄", "琛", "钧", "冠", "策", "腾", "楠", "榕", "风"};



    //姓
    private static String[] firstNameArr = {"zhao", "qian", "sun", "li", "zhou", "wu", "zheng", "wang", "feng", "chen", "chu", "wei", "jiang", "shen", "han", "yang", "zhu",
            "qin", "you", "xu", "he", "lv", "shi", "zhang", "kong", "cao", "yan", "hua", "jin", "wei", "tao", "jiang", "qi", "xie", "zou", "yu", "bai", "shui", "dou", "zhang",
            "yun", "su", "pan", "ge", "xi", "fan", "peng", "lang", "lu", "wei", "chang", "ma", "miao", "feng", "hua", "fang", "yu", "Ren", "yuan", "Liu", "feng", "bao", "shi",
            "tang", "fei", "Lian", "cen", "xue", "Lei", "he", "ni", "tang", "teng", "yin", "Luo", "bi", "hao", "wu", "an", "chang", "yue", "yu", "shi", "fu", "pi", "bian",
            "qi", "kang", "wu", "yu", "yuan", "bu", "gu", "meng", "ping", "huang", "he", "mu", "xiao", "yin", "yao", "shao", "zhan", "wang", "qi", "mao", "yu", "dd", "md", "bei",
            "ming", "zang", "ji", "fu", "cheng", "dai", "tan", "song", "mao", "pang"};

    //名
    private static String[] lastNameArr = {"fang", "wei", "na", "min", "jing", "xiuying", "ying", "hua", "yu", "xiu", "wen", "ming", "lan", "jin", "guo", "chun", "zihan",
            "xinyi", "zixuan", "haoyu", "haoran", "juan", "jun", "guiying", "lei", "yucheng", "yanbing", "kangcheng", "yun", "qiang", "bo", "chao", "shan", "fei", "shuai",
            "lang", "lu", "wei", "chang", "ma", "miao", "feng", "hua", "fang", "yu", "Ren", "yuan", "Liu", "feng", "bao", "shi", "tang", "fei", "Lian", "cen", "xue", "Lei",
            "he", "ni", "tang", "cani", "caota", "weiin", "heci", "qiqi", "moqo", "hecheng", "nima", "chengcheng", "xilin", "xiaooyue", "xiaofei", "chaoshuai", "bo", "taisheng", "shiyu", "kaiyu",
            "wenling", "wenlong", "dongyu", "dayong", "teikou", "chao"};

    //英文名
    private static String[] englishNameArr = {"Chris", "Alan", "Bill", "Frank", "Henry", "Jack", "Lee", "Jimmy", "Mike", "Abby", "Betty", "Daisy", "Belle", "Ailsa", "Amy",
            "Brenda", "Laura", "Connie", "Daphne", "Elizabeth", "Eve", "Ken", "Grace", "Jenny", "Julie", "Jacob", "Jacob", "Michael", "Ethan", "Joshua", "Alexander", "Anthony",
            "William", "Christopher", "Jayden", "Andrew", "Joseph", "David", "Noad", "Aiden", "James", "Ryan", "Logan", "John", "Nathan", "Elijah", "Christian", "Gabriel", "Benjamin", "Jose",
            "Jonathan", "Tyler", "Samuel", "Nicholas", "Gavin", "Dylan", "Jackson", "Brandon", "Caleb", "Jackson", "Brandon", "Caleb", "Mason", "Angel", "Isaac", "Evan", "Jack", "Kevin", "Isaiah",
            "Luke", "Landon", "Justin", "Lucas", "Zachary", "Jordan", "Robert", "Aaron", "Brayden", "Thomas"};

    private static Boolean[] randomFlag = {true, true, true, false, true};
    public static int[] ContantArr1 = {5,6,7,8,9,10,11,12,13,14,15,16,17,18,19};
    public static int[] ContantArr2 = {21,22,23,24,25,26,27,28,29,30,31,32,33,35,34,36,37,38,39};

    private static String[] ABCArr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static String[] abcArr = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private static String[] numberArr = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private static String[] ABCabcArr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private static String[] ABCabcnumberArr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public static String getRandomName() {
        String userName = "";
        int flagNum = (int) (Math.random() * randomFlag.length);
        if (randomFlag[flagNum]) {   //使用中文名
            int i = (int) (Math.random() * firstNameArr.length);
            String firstName = firstNameArr[i];
            int j = (int) (Math.random() * lastNameArr.length);
            String lastName = lastNameArr[j];
            userName = firstName + lastName + getRandomAccount(3);
        } else {  //使用英文名
            int k = (int) (Math.random() * englishNameArr.length);
            userName = englishNameArr[k];
        }
        return userName;
    }

    /**
     * (  ----->  大写英文
     * )  ----->  小写英文
     * &  ----->  大小写英文
     * $  ----->  数字
     * *  ----->  大小写英文数字
     * ?  ----->  姓
     * !  ----->  名
     * %  ----->  英文名
     * （以上全为英文符号）
     *
     * @param strType
     * @return
     */
    public static String getRandomName(String strType) {
        StringBuffer dataBuffer = new StringBuffer();
        char[] chars = strType.toCharArray();
        int length = chars.length;
        for (int i = 0; i < length; i++) {
            String str = String.valueOf(chars[i])
                    .replace("(", ABCArr[(int) (Math.random() * ABCArr.length)])
                    .replace(")", abcArr[(int) (Math.random() * abcArr.length)])
                    .replace("&", ABCabcArr[(int) (Math.random() * ABCabcArr.length)])
                    .replace("$", numberArr[(int) (Math.random() * numberArr.length)])
                    .replace("*", ABCabcnumberArr[(int) (Math.random() * ABCabcnumberArr.length)])
                    .replace("?", firstNameArr[(int) (Math.random() * firstNameArr.length)])
                    .replace("!", lastNameArr[(int) (Math.random() * lastNameArr.length)])
                    .replace("%", englishNameArr[(int) (Math.random() * englishNameArr.length)]);
            dataBuffer.append(str);
        }
        return dataBuffer.toString();
    }





    /**
     * 注册随机生成某个长度的用户名
     *
     * @param length
     * @return
     */
    public static String getRandomAccount(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789_";//生成用户名所用的字符串
        return getRandomStr(length, base);
    }

    /**
     * 注册随机生成某个长度的密码
     *
     * @param length
     * @return
     */

    public static String getRandomPassword(int length) {
        String base = "0123456789";//生成密码所用的字符串
        return getRandomStr(length, base);
    }


    public static String getRandomStr(int length, String base) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    public static String getRandamAccountRule(){
        int flagNum = (int) (Math.random() * acountDigitRule.length);
        String acountDigit =  acountDigitRule[flagNum];
        return  acountDigit;
    }


    /**
     * 获取随机中文姓名
     * @return
     */
    public static String getChinessNameStr (){
        int i = 0;
        int j = 0;
        String firstName = "";
        String lastName1 = "";
        String lastName2 = "";
        int rand=(int)(Math.random()*2)+1;
        LLog.e("rand ： "+rand);
        i = (int) (Math.random() * chinessFirstName.length);
        firstName = chinessFirstName[i];
        j = (int) (Math.random() * chinessLastName.length);
        lastName1 = chinessLastName[j];
        if (rand == 2){ //名字是两个字
            j = (int) (Math.random() * chinessLastName.length);
            lastName2 = chinessLastName[j];
        }
        LLog.e("名字 ： "+firstName+lastName1+lastName2);
        return firstName+lastName1+lastName2;
    }



    private static String[] telFirst="134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
    private static int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }
    /**
     * 获取手机号码
     */
    public static String getTel() {
        int index=getNum(0,telFirst.length-1);
        String first=telFirst[index];
        String second=String.valueOf(getNum(1,888)+10000).substring(1);
        String third=String.valueOf(getNum(1,9100)+10000).substring(1);
        LLog.e("电话号码 ： "+first+second+third);
        return first+second+third;
    }


    public static int getNumberArea(int min,int max){
        int num=(int)(Math.random()*(max -min))+min;
        return  num;
    }
}
