package com.spark.coinpaypddd.utils;

import android.content.Context;

import com.spark.modulebase.data.Language;
import com.spark.modulebase.utils.SharedPreferencesUtil;
import com.spark.moduleuc.entity.CountryEntity;

import java.util.Locale;

/**
 * Created by Administrator on 2019/2/25 0025.
 */

public class FormatDataUtils {
    /**
     * 根据语言获取显示的值
     *
     * @return
     */
    public static String getViewNameByCode(CountryEntity countryEntity, Context context) {
        int code = SharedPreferencesUtil.getInstance(context).getLanguageCode();
        Language language = Language.values()[code];
        Locale locale = new Locale(language.name());
        if (locale == Locale.CHINESE) {
            return countryEntity.getZhName();
        } else if (locale == Locale.ENGLISH) {
            return countryEntity.getEnName();
        } else if (locale == Locale.JAPAN) {
            return countryEntity.getJpLanguage();
        }
        return countryEntity.getZhName();
    }

}
