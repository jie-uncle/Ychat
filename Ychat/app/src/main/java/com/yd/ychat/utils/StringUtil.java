package com.yd.ychat.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.yd.ychat.array.Face_List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jie on 2017/6/6.
 */

public class StringUtil {
    public static SpannableStringBuilder handler(Context context, String content) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "(\\[)\\w{3}(\\])";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();
            Integer id = Face_List.getInstanceMap().get(tempText);
            if (id != null) {
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0, 0, d.getIntrinsicWidth() / 2, d.getIntrinsicHeight() / 2);
                //用这个drawable对象代替字符串easy
                ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                sb.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return sb;
    }
}
