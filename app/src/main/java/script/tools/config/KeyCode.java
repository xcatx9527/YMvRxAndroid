package script.tools.config;

/**
 * date: 2017/3/1 11:17
 *
 * @scene adb shell input keyevent 对应的keycode
 */


public class KeyCode {

    public static final String KEYCODE_UNKNOWN = "0";           //未知按键
    public static final String KEYCODE_MENU_1 = "1";
    public static final String KEYCODE_SOFT_RIGHT = "2";        //按键Soft Right
    public static final String KEYCODE_HOME = "3";              //Home键
    public static final String KEYCODE_BACK = "4";              //回退键
    public static final String KEYCODE_CALL = "5";              //拨号键
    public static final String KEYCODE_ENDCALL = "6";           //挂机键
    public static final String KEYCODE_0 = "7";                  //数字"0"
    public static final String KEYCODE_1 = "8";                  //数字"1"
    public static final String KEYCODE_2 = "9";                  //数字"2"
    public static final String KEYCODE_3 = "10";                 //数字"3"
    public static final String KEYCODE_4 = "11";                 //数字"4"
    public static final String KEYCODE_5 = "12";                 //数字"5"
    public static final String KEYCODE_6 = "13";                 //数字"6"
    public static final String KEYCODE_7 = "14";                 //数字"7"
    public static final String KEYCODE_8 = "15";                 //数字"8"
    public static final String KEYCODE_9 = "16";                 //数字"9"
    public static final String KEYCODE_STAR = "17";              //按键'*'
    public static final String KEYCODE_POUND = "18";             //按键'#'
    public static final String KEYCODE_DPAD_UP = "19";          //导航键 向上
    public static final String KEYCODE_DPAD_DOWN = "20";        //导航键 向下
    public static final String KEYCODE_DPAD_LEFT = "21";        //导航键 向左
    public static final String KEYCODE_DPAD_RIGHT = "22";       //导航键 向右
    public static final String KEYCODE_DPAD_CENTER = "23";      //导航键 确定键
    public static final String KEYCODE_VOLUME_UP = "24";        //音量增加键
    public static final String KEYCODE_VOLUME_DOWN = "25";      //音量减小键
    public static final String KEYCODE_POWER = "26";             //电源键
    public static final String KEYCODE_CAMERA = "27";            //拍照键
    public static final String KEYCODE_CLEAR = "28";             //按键Clear
    public static final String KEYCODE_A = "29";                 //按键'A'
    public static final String KEYCODE_B = "30";                 //按键'B'
    public static final String KEYCODE_C = "31";                 //按键'C'
    public static final String KEYCODE_D = "32";                 //按键'D'
    public static final String KEYCODE_E = "33";                 //按键'E'
    public static final String KEYCODE_F = "34";                 //按键'F'
    public static final String KEYCODE_G = "35";                 //按键'G'
    public static final String KEYCODE_H = "36";                 //按键'H'
    public static final String KEYCODE_I = "37";                 //按键'I'
    public static final String KEYCODE_J = "38";                 //按键'J'
    public static final String KEYCODE_K = "39";                 //按键'K'
    public static final String KEYCODE_L = "40";                 //按键'L'
    public static final String KEYCODE_M = "41";                 //按键'M'
    public static final String KEYCODE_N = "42";                 //按键'N'
    public static final String KEYCODE_O = "43";                 //按键'O'
    public static final String KEYCODE_P = "44";                 //按键'P'
    public static final String KEYCODE_Q = "45";                 //按键'Q'
    public static final String KEYCODE_R = "46";                 //按键'R'
    public static final String KEYCODE_S = "47";                 //按键'S'
    public static final String KEYCODE_T = "48";                 //按键'T'
    public static final String KEYCODE_U = "49";                 //按键'U'
    public static final String KEYCODE_V = "50";                 //按键'V'
    public static final String KEYCODE_W = "51";                 //按键'W'
    public static final String KEYCODE_X = "52";                 //按键'X'
    public static final String KEYCODE_Y = "53";                 //按键'Y'
    public static final String KEYCODE_Z = "54";                 //按键'Z'
    public static final String KEYCODE_COMMA = "55";            //符号","
    public static final String KEYCODE_PERIOD = "56";           //符号"."
    public static final String KEYCODE_ALT_LEFT = "57";         //符号Alt+Left
    public static final String KEYCODE_ALT_RIGHT = "58";        //Alt+Right
    public static final String KEYCODE_SHIFT_LEFT = "59";       //Shift+Left
    public static final String KEYCODE_SHIFT_RIGHT = "60";      //Shift+Right
    public static final String KEYCODE_TAB = "61";               //Tab键
    public static final String KEYCODE_SPACE = "62";             //空格键
    public static final String KEYCODE_SYM = "63";               //按键Symbol modifier
    public static final String KEYCODE_EXPLORER = "64";         //按键Explorer special function
    public static final String KEYCODE_ENVELOPE = "65";         //按键Envelope special function
    public static final String KEYCODE_ENTER = "66";            //回车键
    public static final String KEYCODE_DEL = "67";              //退格键
    public static final String KEYCODE_GRAVE = "68";            //符号"`"
    public static final String KEYCODE_MINUS = "69";            //符号"-"
    public static final String KEYCODE_EQUALS = "70";           //符号"="
    public static final String KEYCODE_LEFT_BRACKET = "71";    //符号"["
    public static final String KEYCODE_RIGHT_BRACKET = "72";   //符号"]"
    public static final String KEYCODE_BACKSLASH = "73";        //符号"\"
    public static final String KEYCODE_SEMICOLON = "74";        //符号";"
    public static final String KEYCODE_APOSTROPHE = "75";       //符号"'"(单引号)
    public static final String KEYCODE_SLASH = "76";             //符号"/"
    public static final String KEYCODE_AT = "77";                //符号"@"
    public static final String KEYCODE_NUM = "78";               //按键Number modifier
    public static final String KEYCODE_HEADSETHOOK = "79";      //按键Headset Hook
    public static final String KEYCODE_FOCUS = "80";             //拍照对焦键
    public static final String KEYCODE_PLUS = "81";              //符号"+"
    public static final String KEYCODE_MENU_2 = "82";            //菜单键
    public static final String KEYCODE_NOTIFICATION = "83";     //通知键
    public static final String KEYCODE_SEARCH = "84";            //搜索键
    public static final String TAG_LAST_KEYCODE = "85";

}
