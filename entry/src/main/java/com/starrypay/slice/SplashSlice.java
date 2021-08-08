package com.starrypay.slice;

import com.starrypay.component.DragLayout;
import com.starrypay.myapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.text.RichText;
import ohos.agp.text.RichTextBuilder;
import ohos.agp.text.TextForm;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.multimodalinput.event.TouchEvent;

public class SplashSlice extends AbilitySlice {


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(new DirectionalLayout(this));


        showDialog(intent);
    }

    private void showDialog(Intent intent) {
        CommonDialog dialog = new CommonDialog(this);

        Component rootLayout = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_dialog_privacy, null, false);
        dialog.setSize(AttrHelper.vp2px(300, this), DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        dialog.setCornerRadius(AttrHelper.vp2px(10, this));

        String content = "请您务必审慎阅读、充分理解隐私政策”各条款，包括但不限于：为了向你提供服务，我们需要收集你的设备信息、操作日志等个人信息您可以阅读";
        RichTextBuilder textBuilder = new RichTextBuilder();
        textBuilder.addText(content);

        textBuilder.mergeForm(new TextForm().setTextColor(Color.BLUE.getValue()));
        textBuilder.addText("《隐私政策》");
        textBuilder.revertForm();

        textBuilder.addText("了解详细信息。如您同意，请点击“同意”开始接受我们的服务。");

        RichText richText = textBuilder.build();
        richText.addTouchEventListener(new RichText.TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                dialog.destroy();
                present(new PrivacyAbilitySlice(), intent);
                terminate();
                return true;
            }
        }, 67, 73);

        Text textContent = (Text) rootLayout.findComponentById(ResourceTable.Id_textContent);
        textContent.setRichText(richText);

        Button btnCancel = (Button) rootLayout.findComponentById(ResourceTable.Id_btnCancal);
        btnCancel.setClickedListener(component -> {
            dialog.destroy();
            terminate();
        });
        Button btnSubmit = (Button) rootLayout.findComponentById(ResourceTable.Id_btnSubmit);
        btnSubmit.setClickedListener(component -> {
            dialog.destroy();
            present(new MainAbilitySlice(), intent);
            terminate();
        });

        dialog.setContentCustomComponent(rootLayout);

        dialog.show();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }


}

