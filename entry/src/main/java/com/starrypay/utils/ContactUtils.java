package com.starrypay.utils;

import com.example.library.github.promeg.pinyinhelper.Pinyin;
import com.starrypay.bean.ContactBean;
import com.starrypay.myapplication.ResourceTable;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.app.Context;
import ohos.data.resultset.ResultSet;
import ohos.security.permission.Permission;
import ohos.utils.net.Uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ContactUtils {

    public static ArrayList<ContactBean> getContacts(Context context) {
        ArrayList<ContactBean> resultList = new ArrayList<>();
        DataAbilityHelper helper = DataAbilityHelper.creator(context);
        String url = "dataability:///com.android.contacts/data/phones";
        Uri uri = Uri.parse(url);
        try {
            String[] strs = new String[]{"display_name", "data1"};
            ResultSet resultSet = helper.query(uri, strs, null);
            StringBuilder builder = new StringBuilder("查询结果：");

            while (resultSet.goToNextRow()) {
                String name = resultSet.getString(0);
                String number = resultSet.getString(1);
                resultList.add(new ContactBean(number, name));
            }

            PinyinComparator comparator = new PinyinComparator();
            resultList.sort(comparator);

        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public static class PinyinComparator implements Comparator<ContactBean> {
        public int compare(ContactBean o1, ContactBean o2) {
            String str1 = Pinyin.toPinyin(o1.name, "*");
            String str2 = Pinyin.toPinyin(o2.name, "*");
            int flag = str1.compareTo(str2);
            return flag;
        }
    }

}
