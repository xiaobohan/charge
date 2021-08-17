package com.starrypay.utils;

import com.starrypay.myapplication.ResourceTable;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.app.Context;
import ohos.data.resultset.ResultSet;
import ohos.security.permission.Permission;
import ohos.utils.net.Uri;

public class ContactUtils {


    public void requestPermission(Context context){
        context.requestPermissionsFromUser(new String[]{"ohos.permission.READ_CONTACTS"},200);
    }

    public void getContacts(Context context) {
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
                builder.append("\nname:").append(name).append(",number:").append(number);
            }

            String s = builder.toString();

        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }
    }

}
