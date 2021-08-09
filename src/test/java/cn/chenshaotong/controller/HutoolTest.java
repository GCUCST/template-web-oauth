package cn.chenshaotong.controller;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.date.chinese.ChineseMonth;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.db.nosql.redis.RedisDS;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HutoolTest {


    @Test
    void test1(){
//        CodeGenerator a = new MathGenerator();
//        String generate = a.generate();
//        boolean verify = a.verify("12*3 =", "36");
//        GifCaptcha gifCaptcha = CaptchaUtil.createGifCaptcha(200, 200);
//        String imageBase64 = gifCaptcha.getImageBase64();
//        byte[] imageBytes = gifCaptcha.getImageBytes();
        Snowflake snowflake = IdUtil.createSnowflake(1, 2);
        String s = snowflake.nextIdStr();
        System.out.println(s);
//        String chineseMonthName = ChineseMonth.getChineseMonthName(false, 11, true);
//        System.out.println(chineseMonthName);



    }




}
